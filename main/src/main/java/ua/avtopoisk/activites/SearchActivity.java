package ua.avtopoisk.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.googlecode.androidannotations.annotations.*;
import de.akquinet.android.androlog.Log;
import parsers.AvtopoiskParser;
import ua.avtopoisk.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Start activity. Search params located here
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 12.10.12
 */
@EActivity(R.layout.search)
public class SearchActivity extends Activity {
    public static final String BRAND_ID_KEY = "brand";
    public static final String MODEL_ID_KEY = "model";
    public static final String REGION_ID_KEY = "region";

    @ViewById(R.id.brands)
    protected Spinner brands;

    @ViewById(R.id.models)
    protected Spinner models;

    @ViewById(R.id.regions)
    protected Spinner regions;

    private ArrayAdapter<String> adapter;
    private LinkedHashMap<String, Integer> brandsMap;
    private LinkedHashMap<String, Integer> modelsMap;
    private LinkedHashMap<String, Integer> regionsMap;
    private AvtopoiskParser parser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parser = new AvtopoiskParser();
        progressDialog = ProgressDialog.show(this, "", getString(R.string.dlg_progress_data_loading), true);
        getRegions();
        getBrands();
    }

    @AfterViews
    protected void init() {
        brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                models.setEnabled(position > 0);
                if (models.isEnabled()) {
                    progressDialog = ProgressDialog.show(SearchActivity.this, "", getString(R.string.dlg_progress_data_loading), true);
                    getModels(brandsMap.get(brands.getSelectedItem().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        models.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Click(R.id.btn_find)
    public void btnFindOnClick(View view) {
        final Intent intent = new Intent(SearchActivity.this, SearchResultActivity_.class);
        intent.putExtra(BRAND_ID_KEY, brandsMap.get(brands.getSelectedItem().toString()));
        int modelId = (models.isEnabled()) ? modelsMap.get(models.getSelectedItem().toString()) : 0;
        intent.putExtra(MODEL_ID_KEY, modelId);
        intent.putExtra(REGION_ID_KEY, regionsMap.get(regions.getSelectedItem().toString()));
        startActivity(intent);
    }

    @Background
    protected void getBrands() {
        LinkedHashMap<String, Integer> aBrands = null;
        try {
            aBrands = parser.getBrands();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
        populateBrands(aBrands);
    }

    @UiThread
    protected void populateBrands(LinkedHashMap<String, Integer> aBrands) {
        brandsMap = aBrands;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(brandsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brands.setAdapter(adapter);
        brands.setPrompt(getString(R.string.brands_prompt));
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Background
    protected void getModels(int brandId) {
        LinkedHashMap<String, Integer> aModels = null;
        try {
            aModels = parser.getModels(brandId);
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
        populateModels(aModels);
    }

    @UiThread
    protected void populateModels(LinkedHashMap<String, Integer> aModels) {
        modelsMap = aModels;
        adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(modelsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        models.setPrompt(getString(R.string.models_prompt));
        models.setAdapter(adapter);
        models.invalidate();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Background
    protected void getRegions() {
        LinkedHashMap<String, Integer> aRegions = null;
        try {
            aRegions = parser.getRegions();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
        populateRegions(aRegions);
    }

    @UiThread
    protected void populateRegions(LinkedHashMap<String, Integer> aRegions) {
        regionsMap = aRegions;
        adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(regionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regions.setPrompt(getString(R.string.regions_prompt));
        regions.setAdapter(adapter);
        regions.invalidate();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
