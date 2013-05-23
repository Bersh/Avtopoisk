package ua.avtopoisk.activites;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.google.inject.Inject;
import org.androidannotations.annotations.*;
import de.akquinet.android.androlog.Log;
import domain.SortType;
import parsers.AvtopoiskParser;
import ua.avtopoisk.AvtopoiskApplication;
import ua.avtopoisk.BrandsAndRegionsHolder;
import ua.avtopoisk.Constants;
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
@EActivity(R.layout.layout_search)
@RoboGuice
public class SearchActivity extends Activity {
    private static final int REQUEST_CODE_BRANDS_LIST = 1;
    private static final int REQUEST_CODE_MODELS_LIST = 2;

    @Bean
    BrandsAndRegionsHolder brandsAndRegionsHolder;

    @ViewById(R.id.brands)
    protected Button brands;

    @ViewById(R.id.models)
    protected Button models;

    @ViewById(R.id.regions)
    protected Spinner regions;

    @ViewById(R.id.year_from)
    protected Spinner yearFrom;

    @ViewById(R.id.sort_by)
    protected Spinner sortBy;

    @ViewById(R.id.body_type)
    protected Spinner bodyType;

    @ViewById(R.id.added_type)
    protected Spinner addedType;

    @ViewById(R.id.year_to)
    protected Spinner yearTo;

    @ViewById(R.id.price_from)
    protected Spinner priceFrom;

    @ViewById(R.id.price_to)
    protected Spinner priceTo;

    @App
    AvtopoiskApplication application;

    private ArrayAdapter<String> adapter;

    LinkedHashMap<String, Integer> brandsMap;
    LinkedHashMap<String, Integer> regionsMap;

    private LinkedHashMap<String, Integer> modelsMap;
    private LinkedHashMap<String, SortType> sortTypesMap;
    private LinkedHashMap<String, Integer> bodyTypesMap = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> addedTypesMap = new LinkedHashMap<String, Integer>();

    @Inject
    private AvtopoiskParser parser;
    private ProgressDialog progressDialog;
    private String currentBrand = "";
    private ArrayList<String> brandNames;
    private ArrayList<String> modelNames;

    private DialogInterface.OnClickListener dataLoadingErrorDialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    brandSelected();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    models.setEnabled(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brandsMap = brandsAndRegionsHolder.brandsMap;
        regionsMap = brandsAndRegionsHolder.regionsMap;
        if (brandsMap == null || regionsMap == null) {
            finish();
        }

        buildSortTypesMap();
        buildBodyTypesMap();
        buildAddedTypesMap();

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    private void buildSortTypesMap() {
        sortTypesMap = new LinkedHashMap<String, SortType>();
        sortTypesMap.put(getString(R.string.sort_by_date_text), SortType.DATE);
        sortTypesMap.put(getString(R.string.sort_by_year_desc_text), SortType.YEAR_DESC);
        sortTypesMap.put(getString(R.string.sort_by_year_inc_text), SortType.YEAR_INC);
        sortTypesMap.put(getString(R.string.sort_by_price_desc_text), SortType.PRICE_DESC);
        sortTypesMap.put(getString(R.string.sort_by_price_inc_text), SortType.PRICE_INC);
    }

    @AfterViews
    protected void init() {
        populateBrands();
        populateRegions();
        populateYears();
        populatePrices();
        populateSortTypes();
        populateBodyTypes();
        populateAddedTypes();

        models.setEnabled(false);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);
    }

    private void brandSelected() {
        models.setEnabled(!TextUtils.isEmpty(currentBrand));
        if (models.isEnabled()) {
            progressDialog = ProgressDialog.show(SearchActivity.this, "", getString(R.string.dlg_progress_data_loading), true);
            getModels(brandsMap.get(currentBrand));
        }
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Click(R.id.btn_find)
    protected void btnFindOnClick(View view) {
        final Intent intent = new Intent(SearchActivity.this, SearchResultActivity_.class);
        intent.putExtra(Constants.BRAND_ID_KEY, brandsMap.get(brands.getText().toString()));
        int modelId = (models.isEnabled()) ? modelsMap.get(models.getText().toString()) : 0;
        intent.putExtra(Constants.MODEL_ID_KEY, modelId);
        intent.putExtra(Constants.YEAR_FROM_KEY, yearFrom.getSelectedItem().toString());
        intent.putExtra(Constants.YEAR_TO_KEY, yearTo.getSelectedItem().toString());
        intent.putExtra(Constants.PRICE_FROM_KEY, priceFrom.getSelectedItem().toString());
        intent.putExtra(Constants.PRICE_TO_KEY, priceTo.getSelectedItem().toString());
        intent.putExtra(Constants.REGION_ID_KEY, regionsMap.get(regions.getSelectedItem().toString()));
        intent.putExtra(Constants.SORT_TYPE_KEY, sortTypesMap.get(sortBy.getSelectedItem().toString()));
        intent.putExtra(Constants.BODY_TYPE_KEY, bodyTypesMap.get(bodyType.getSelectedItem().toString()));
        intent.putExtra(Constants.ADDED_TYPE_KEY, addedTypesMap.get(addedType.getSelectedItem().toString()));
        startActivity(intent);
    }

    @Click(R.id.brands)
    protected void btnSelectBrandClick(View view) {
        Intent intent = new Intent(this, ListActivity_.class);
        intent.putStringArrayListExtra(Constants.KEY_EXTRA_COLLECTION, brandNames);
        startActivityForResult(intent, REQUEST_CODE_BRANDS_LIST);
    }

    @Click(R.id.models)
    protected void btnSelectModelClick(View view) {
        Intent intent = new Intent(this, ListActivity_.class);
        intent.putStringArrayListExtra(Constants.KEY_EXTRA_COLLECTION, modelNames);
        startActivityForResult(intent, REQUEST_CODE_MODELS_LIST);
    }

    @Background
    protected void getModels(int brandId) {
        LinkedHashMap<String, Integer> aModels = null;
        try {
            aModels = parser.getModels(brandId);
        } catch (IOException e) {
            Log.e(e.getMessage());
        }
        populateModels(aModels);
    }

    protected void populateBrands() {
        brandNames = new ArrayList<String>(brandsMap.keySet());
        brands.setText(brandNames.get(0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_BRANDS_LIST:
                if (data != null) {
                    currentBrand = data.getStringExtra(Constants.KEY_EXTRA_SELECTED);
                    brands.setText(currentBrand);
                    models.setText("");
                    models.setEnabled(false);
                    brandSelected();
                }
                break;
            case REQUEST_CODE_MODELS_LIST:
                if (data != null) {
                    models.setText(data.getStringExtra(Constants.KEY_EXTRA_SELECTED));
                }
                break;
        }
    }

    protected void populateYears() {
        ArrayAdapter yearsAdapter = ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearFrom.setAdapter(yearsAdapter);
        yearFrom.setPrompt(getString(R.string.year_prompt));
        yearTo.setAdapter(yearsAdapter);
        yearTo.setPrompt(getString(R.string.year_prompt));
    }

    protected void populateSortTypes() {
        ArrayAdapter sortTypesAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(sortTypesMap.keySet()));
        sortTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(sortTypesAdapter);
        sortBy.setPrompt(getString(R.string.sort_hint));
    }

    private void buildAddedTypesMap() {
        int[] addedTypeCodes = getResources().getIntArray(R.array.added_codes);
        String[] addedTypeNames = getResources().getStringArray(R.array.added_types);
        int i = 0;
        for (int c : addedTypeCodes) {
            addedTypesMap.put(addedTypeNames[i], c);
            ++i;
        }
    }

    protected void populateAddedTypes() {
        ArrayAdapter addedTypesAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(addedTypesMap.keySet()));
        addedTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addedType.setAdapter(addedTypesAdapter);
        addedType.setPrompt(getString(R.string.added_type_hint));
    }

    private void buildBodyTypesMap() {
        int[] bodyTypeCodes = getResources().getIntArray(R.array.body_type_codes);
        String[] bodyTypeNames = getResources().getStringArray(R.array.body_types);
        int i = 0;
        for (int c : bodyTypeCodes) {
            bodyTypesMap.put(bodyTypeNames[i], c);
            ++i;
        }
    }

    protected void populateBodyTypes() {
        ArrayAdapter bodyTypesAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(bodyTypesMap.keySet()));
        bodyTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bodyType.setAdapter(bodyTypesAdapter);
        bodyType.setPrompt(getString(R.string.body_type_hint));
    }

    protected void populatePrices() {
        ArrayAdapter pricesAdapter = ArrayAdapter.createFromResource(this, R.array.prices, android.R.layout.simple_spinner_item);
        pricesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceFrom.setAdapter(pricesAdapter);
        priceFrom.setPrompt(getString(R.string.price_prompt));
        priceTo.setAdapter(pricesAdapter);
        priceTo.setPrompt(getString(R.string.price_prompt));
    }

    @UiThread
    protected void populateModels(LinkedHashMap<String, Integer> aModels) {
        modelsMap = aModels;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (modelsMap == null) {
            models.setEnabled(false);
            application.showDataLoadingErrorDialog(this, dataLoadingErrorDialogClickListener);
        } else {
            modelNames = new ArrayList<String>(modelsMap.keySet());
            String currentModel = modelNames.get(0);
            models.setText(currentModel);
            models.setEnabled(!TextUtils.isEmpty(currentModel));
        }
    }

    protected void populateRegions() {
        adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(regionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regions.setPrompt(getString(R.string.regions_prompt));
        regions.setAdapter(adapter);
        regions.invalidate();
    }
}
