package ua.avtopoisk.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import de.akquinet.android.androlog.Log;
import parsers.AvtopoiskParserImpl;
import ua.avtopoisk.BrandsAndRegionsHolder;
import ua.avtopoisk.R;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Splash screen activity
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 19.10.12
 */
@EActivity(R.layout.layout_splash)
@RoboGuice
public class SplashScreenActivity extends Activity {
    public static final String YEARS_KEY = "years";

    @Inject
    private AvtopoiskParserImpl parser;

    @Bean
    BrandsAndRegionsHolder brandsAndRegionsHolder;

    @ViewById(R.id.splash_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @Background
    protected void loadData() {
        publishProgress(10);
        LinkedHashMap<String, Integer> brands = null;
        try {
            brands = parser.getBrands();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
        publishProgress(50);

        LinkedHashMap<String, Integer> regions = null;
        try {
            regions = parser.getRegions();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }

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

}
