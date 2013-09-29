package ua.avtopoisk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import ua.avtopoisk.Constants;
import ua.avtopoisk.model.Brand;
import ua.avtopoisk.model.Region;

import java.sql.SQLException;

@EBean(scope = EBean.Scope.Singleton)
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "avtopoisk.db";
    private static final int DATABASE_VERSION = 1;
    @RootContext
    static Context context;

    private Dao<Brand, Long> brandsDao = null;
    private Dao<Region, Long> regionsDao = null;

    public DBHelper() {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            createDB(connectionSource);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            dropDB(connectionSource);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    private void createDB(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, Brand.class);
        TableUtils.createTable(connectionSource, Region.class);
    }

    private void dropDB(ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, Brand.class, true);
        TableUtils.dropTable(connectionSource, Region.class, true);
    }

    public void recreateDB() {
        try {
            dropDB(getConnectionSource());
            createDB(getConnectionSource());
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }
    }

    public Dao<Brand, Long> getBrandsDao() throws SQLException {
        if (brandsDao == null) {
            brandsDao = getDao(Brand.class);
        }
        return brandsDao;
    }

    public Dao<Region, Long> getRegionsDao() throws SQLException {
        if (regionsDao == null) {
            regionsDao = getDao(Region.class);
        }
        return regionsDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        brandsDao = null;
        regionsDao = null;
    }
}
