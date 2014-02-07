package ua.avtopoisk.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import org.androidannotations.annotations.*;
import ua.avtopoisk.Constants;
import ua.avtopoisk.R;
import ua.avtopoisk.SharedPreferencesManager;
import ua.avtopoisk.parser.AvtopoiskParser;

import java.io.IOException;
import java.util.Date;
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

    @ViewById(R.id.splash_progress)
    ProgressBar progressBar;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        Date lastUpdateDate = sharedPreferencesManager.getLastUpdateDate();
        Date currentDate = new Date();
        int daysDiff = Math.abs((int) ((currentDate.getTime() - lastUpdateDate.getTime()) / (3600 * 24 * 1000)));
        if (daysDiff > Constants.DATA_UPDATE_INTERVAL) {
            loadData();
        } else {
            startSearchActivity();
        }
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
                Log.e(Constants.LOG_TAG, err, e);
                e.printStackTrace();
            }
        }

        if (brands == null || brands.isEmpty()) {
            showSplashError();
            return;
        }

        publishProgress(50);

        LinkedHashMap<String, Integer> regions = buildRegionsMap();
        publishProgress(70);

        populateData(brands, regions);
    }

    @UiThread
    protected void populateData(LinkedHashMap<String, Integer> brands, LinkedHashMap<String, Integer> regions) {
        dbManager.createBrandsAndRegions(brands.entrySet(), regions.entrySet());
        sharedPreferencesManager.setLastUpdateDate(new Date());
        publishProgress(100);
        startSearchActivity();
    }

    private void startSearchActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, SearchActivity_.class);
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
