package ua.avtopoisk.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import org.androidannotations.annotations.*;
import ua.avtopoisk.BrandsAndRegionsHolder;
import ua.avtopoisk.Constants;
import ua.avtopoisk.R;
import ua.avtopoisk.db.DBHelper;
import ua.avtopoisk.model.Brand;
import ua.avtopoisk.parser.AvtopoiskParser;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Splash screen activity
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 19.10.12
 */
@EActivity(R.layout.activity_splash)
public class SplashScreenActivity extends BaseActivity {

    @Bean
    protected AvtopoiskParser parser;

    @Bean
    BrandsAndRegionsHolder brandsAndRegionsHolder;

    @ViewById(R.id.splash_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @UiThread
    protected void showSplashError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.splash_error_text)
                .setTitle(R.string.error)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Background
    protected void loadData() {
        publishProgress(30);
        LinkedHashMap<String, Integer> brands = null;
        for (int i = 0; i < 1; ++i) {
            try {
                brands = parser.getBrands();
                break;
            } catch (IOException e) {
                String err = (e.getMessage() == null) ? "No message" : e.getMessage();
                Log.e(Constants.LOG_TAG, err);
                e.printStackTrace();
            }
        }

        if (brands == null) {
            showSplashError();
            return;
        }
/*

        for(java.util.Map.Entry<String, Integer> entry : brands.entrySet()) {
            Brand brand = new Brand();
        }
*/

        publishProgress(50);

        LinkedHashMap<String, Integer> regions = buildRegionsMap();
        publishProgress(70);

        populateData(brands, regions);
    }

    @UiThread
    protected void populateData(LinkedHashMap<String, Integer> brands, LinkedHashMap<String, Integer> regions) {
        Intent intent = new Intent(SplashScreenActivity.this, SearchActivity_.class);
        brandsAndRegionsHolder.brandsMap = brands;
        brandsAndRegionsHolder.regionsMap = regions;
        publishProgress(100);
        startActivity(intent);
        finish();
    }

    @UiThread
    void publishProgress(int progress) {
        progressBar.setProgress(progress);
    }

    private LinkedHashMap<String, Integer> buildRegionsMap() {
        LinkedHashMap<String, Integer> regionsMap = new LinkedHashMap<String, Integer>();
        int[] regionCodes = getResources().getIntArray(R.array.region_codes);
        String[] regionNames = getResources().getStringArray(R.array.region_names);
        int i = 0;
        for (int c : regionCodes) {
            regionsMap.put(regionNames[i], c);
            ++i;
        }
        return regionsMap;
    }

}
