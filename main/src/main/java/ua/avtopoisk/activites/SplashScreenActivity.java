package ua.avtopoisk.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import de.akquinet.android.androlog.Log;
import parsers.AvtopoiskParser;
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
@EActivity(R.layout.splash)
@RoboGuice
public class SplashScreenActivity extends Activity {
    @Inject
    private AvtopoiskParserImpl parser;

    @Bean
    BrandsAndRegionsHolder brandsAndRegionsHolder;

    @ViewById(R.id.splash_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBrandsAndRegions();
    }

    @Background
    protected void getBrandsAndRegions() {
        publishProgress(10);
        LinkedHashMap<String, Integer> aBrands = null;
        try {
            aBrands = parser.getBrands();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
        publishProgress(40);
        LinkedHashMap<String, Integer> aRegions = null;
        try {
            aRegions = parser.getRegions();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
        populateData(aBrands, aRegions);
        publishProgress(70);
    }

    @UiThread
    protected void populateData(LinkedHashMap<String, Integer> aBrands, LinkedHashMap<String, Integer> aRegions) {
        Intent intent = new Intent(SplashScreenActivity.this, SearchActivity_.class);
        brandsAndRegionsHolder.brandsMap = aBrands;
        brandsAndRegionsHolder.regionsMap = aRegions;
        publishProgress(100);
        startActivity(intent);
        finish();
    }

    @UiThread
    void publishProgress(int progress) {
        progressBar.setProgress(progress);
    }

}
