package ua.avtopoisk.db;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.splunk.mint.Mint;

import ua.avtopoisk.Constants;
import ua.avtopoisk.model.Brand;
import ua.avtopoisk.model.Region;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 29.03.2014
 */
public class DBManager {
    private Dao<Brand, Long> brandsDao;
    private Dao<Region, Long> regionsDao;

    public DBManager(@Nonnull DBHelper dbHelper) throws SQLException {
        brandsDao = dbHelper.getBrandsDao();
        regionsDao = dbHelper.getRegionsDao();
    }

    @Nonnull
    public List<Brand> getAllBrands() {
        List<Brand> brands = new ArrayList<Brand>();
        try {
            brands.addAll(brandsDao.queryForAll());
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "SQLException", e);
            Mint.logException(e);
        }
        return brands;
    }

    @Nonnull
    public List<Region> getAllRegions() {
        List<Region> regions = new ArrayList<Region>();
        try {
            regions.addAll(regionsDao.queryForAll());
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "SQLException", e);
            Mint.logException(e);
        }
        return regions;
    }

    @Nullable
    public Brand getBrandById(long brandId) {
        Brand result = null;
        try {
            result = brandsDao.queryForId(brandId);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "SQLException", e);
            Mint.logException(e);
        }
        return result;
    }

    public void createBrandsAndRegions(Set<Map.Entry<String, Integer>> brands, Set<Map.Entry<String, Integer>> regions) {
        try {
            for (Map.Entry<String, Integer> entry : brands) {
                Brand brand = new Brand(entry.getValue(), entry.getKey());
                brandsDao.createIfNotExists(brand);
            }

            for (Map.Entry<String, Integer> entry : regions) {
                Region region = new Region(entry.getValue(), entry.getKey());
                regionsDao.createIfNotExists(region);
            }
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "SQLException createBrandsAndRegions", e);
            Mint.logException(e);
        }
    }
}
