package ua.avtopoisk.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import de.akquinet.android.androlog.Log;
import domain.SortType;
import parsers.AvtopoiskBaseParser;
import parsers.AvtopoiskParser;
import ua.avtopoisk.AvtopoiskApplication;
import ua.avtopoisk.BrandsAndRegionsHolder;
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
    public static final String BRAND_ID_KEY = "brand";
    public static final String MODEL_ID_KEY = "model";
    public static final String REGION_ID_KEY = "region";
    public static final String YEAR_FROM_KEY = "yearFrom";
    public static final String YEAR_TO_KEY = "yearTo";
    public static final String PRICE_FROM_KEY = "priceFrom";
    public static final String PRICE_TO_KEY = "priceTo";
    public static final String SORT_TYPE_KEY = "sortType";

    @Bean
    BrandsAndRegionsHolder brandsAndRegionsHolder;

    @ViewById(R.id.brands)
    protected Button brands;

    @ViewById(R.id.models)
    protected Spinner models;

    @ViewById(R.id.regions)
    protected Spinner regions;

    @ViewById(R.id.year_from)
    protected Spinner yearFrom;

    @ViewById(R.id.sort_by)
    protected Spinner sortBy;

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

    @Inject
    private AvtopoiskParser parser;
    private ProgressDialog progressDialog;
    private String currentBrand = "";


    private DialogInterface.OnClickListener dataLoadingErrorDialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    brandSelected(brandsMap.get(currentBrand));
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
        if( brandsMap == null || regionsMap == null) {
            finish();
        }

        buildSortTypesMap();

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

/*        brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        models.setEnabled(false);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);
    }

    private void brandSelected(int position) {
        models.setEnabled(position > 0);
        if (models.isEnabled()) {
            progressDialog = ProgressDialog.show(SearchActivity.this, "", getString(R.string.dlg_progress_data_loading), true);
            getModels(brandsMap.get(brands.getText().toString()));
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
        intent.putExtra(BRAND_ID_KEY, brandsMap.get(brands.getText().toString()));
        int modelId = (models.isEnabled()) ? modelsMap.get(models.getSelectedItem().toString()) : 0;
        intent.putExtra(MODEL_ID_KEY, modelId);
        intent.putExtra(YEAR_FROM_KEY, yearFrom.getSelectedItem().toString());
        intent.putExtra(YEAR_TO_KEY, yearTo.getSelectedItem().toString());
        intent.putExtra(PRICE_FROM_KEY, priceFrom.getSelectedItem().toString());
        intent.putExtra(PRICE_TO_KEY, priceTo.getSelectedItem().toString());
        intent.putExtra(REGION_ID_KEY, regionsMap.get(regions.getSelectedItem().toString()));
        intent.putExtra(SORT_TYPE_KEY, sortTypesMap.get(sortBy.getSelectedItem().toString()));
        startActivity(intent);
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
        final ArrayAdapter brandsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(brandsMap.keySet()));
        brandsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
/*        brands.setAdapter(adapter);
        brands.setPrompt(getString(R.string.brands_prompt));*/
        brands.setText((String)brandsMap.keySet().toArray()[0]);
        brands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder;
                if (Build.VERSION.SDK_INT >= 11) {
                    dialogBuilder = new AlertDialog.Builder(SearchActivity.this);//, android.R.style.Theme_NoTitleBar);
                } else {
                    dialogBuilder = new AlertDialog.Builder(SearchActivity.this);
                }
                View dialogView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.layout_list, null);
                dialogBuilder.setView(dialogView);
                final ListView list = (ListView)dialogView.findViewById(R.id.list);
                list.setAdapter(brandsAdapter);
                dialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        currentBrand = (String)list.getSelectedItem();
                    }
                });

                dialogBuilder.create().show();
            }
        });
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
        yearFrom.setPrompt(getString(R.string.sort_hint));
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
            adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>(modelsMap.keySet()));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            models.setPrompt(getString(R.string.models_prompt));
            models.setAdapter(adapter);
            models.invalidate();
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
