package ua.avtopoisk.activites;


import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import com.commonsware.cwac.endless.EndlessAdapter;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.StringRes;
import de.akquinet.android.androlog.Log;
import domain.Car;
import domain.SortType;
import org.apache.commons.lang.StringUtils;
import parsers.AvtopoiskParser;
import ua.avtopoisk.AvtopoiskApplication;
import ua.avtopoisk.CarAdapter;
import ua.avtopoisk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Search result activity. List of cars returned by parser
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 12.10.12
 */

@EActivity(R.layout.layout_search_result)
@OptionsMenu(R.menu.search_result_menu)
@RoboGuice
public class SearchResultActivity extends ListActivity {
    public static final int CARS_PER_PAGE = 10;
    private ProgressDialog progressDialog;

    @Extra(SearchActivity.BRAND_ID_KEY)
    int brandId;

    @Extra(SearchActivity.MODEL_ID_KEY)
    int modelId;

    @Extra(SearchActivity.REGION_ID_KEY)
    int regionId;

    @Extra(SearchActivity.YEAR_FROM_KEY)
    String yearFrom;

    @Extra(SearchActivity.YEAR_TO_KEY)
    String yearTo;

    @Extra(SearchActivity.PRICE_FROM_KEY)
    String priceFrom;

    @Extra(SearchActivity.PRICE_TO_KEY)
    String priceTo;

    @Extra(SearchActivity.SORT_TYPE_KEY)
    SortType sortType;

    @StringRes(R.string.any)
    String anyString;

    @StringRes(R.string.any2)
    String anyString2;

    @Inject
    private AvtopoiskParser parser;

    @App
    AvtopoiskApplication application;

    private EndlessAdapter adapter;
    private ArrayList<Car> currentResults = new ArrayList<Car>();
    private int loadedCount;
    private int currentYearFrom;
    private int currentYearTo;
    private int currentPriceFrom;
    private int currentPriceTo;

    private DialogInterface.OnClickListener dataLoadingErrorDialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    loadResults();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);

        currentYearFrom = StringUtils.isEmpty(yearFrom) || yearFrom.equals(anyString) ? 0 : Integer.parseInt(yearFrom);
        currentYearTo = StringUtils.isEmpty(yearTo) || yearTo.equals(anyString) ? 0 : Integer.parseInt(yearTo);
        currentPriceFrom = StringUtils.isEmpty(priceFrom) || priceFrom.equals(anyString2) ? 0 : Integer.parseInt(priceFrom);
        currentPriceTo = StringUtils.isEmpty(priceTo) || priceTo.equals(anyString2) ? 0 : Integer.parseInt(priceTo);
    }

    @AfterViews
    protected void init() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);
        getListView().setDividerHeight(0);
        getListView().setFooterDividersEnabled(false);

        loadResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.getItem(0).getSubMenu().getItem(sortType.ordinal()).setChecked(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        parser.resetCurrentPage();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @ItemClick
    public void listItemClicked(Car clicked) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clicked.getLinkToDetails()));
        startActivity(intent);
    }

    @UiThread
    void showProgressDialog() {
        progressDialog = ProgressDialog.show(SearchResultActivity.this, "", getString(R.string.dlg_progress_data_loading), true);
    }

    @UiThread
    protected void showDataLoadingErrorDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        application.showDataLoadingErrorDialog(this, dataLoadingErrorDialogClickListener);
    }

    @Background
    void loadResults() {
        showProgressDialog();
        ArrayList<Car> cars;

        try {
            cars = parser.parse(brandId, modelId, regionId, currentYearFrom, currentYearTo, currentPriceFrom,
                    currentPriceTo, sortType);
        } catch (Throwable e) {
            String err = (e.getMessage() == null) ? "No message" : e.getMessage();
            Log.e(err);
            showDataLoadingErrorDialog();
            return;
        }

        populateResults(cars);
    }


    @UiThread
    void populateResults(ArrayList<Car> cars) {
        ListView listView = getListView();
/*        loadedCount += cars.size();
        int resultsCount = parser.getLastRequestResultsCount();*/

/*        //add load more button and text
        TextView loadedCountText = (TextView) loadMoreView.findViewById(R.id.loaded_count_text);
        loadedCountText.setText(String.format(getString(R.string.loaded_count_text), loadedCount, resultsCount));

        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(loadMoreView);
        }
        if ((cars.isEmpty() || *//*cars.size() < CARS_PER_PAGE ||*//* loadedCount == resultsCount) && listView.getFooterViewsCount() > 0) {
            View loadTenMoreText = loadMoreView.findViewById(R.id.load_ten_more_text);
            ((LinearLayout) loadMoreView).removeView(loadTenMoreText);
            loadedCountText.setPadding(0, 10, 0, 10);
            loadMoreView.setOnClickListener(null);
        }*/
        currentResults.addAll(cars);

        if (adapter == null) {
            final CarAdapter carAdapter = new CarAdapter(this, R.layout.cars_list_item, currentResults);
            adapter = new EndlessAdapter(this, carAdapter, R.layout.loading_item) {
                private List<Car> cars = null;

                @Override
                protected boolean cacheInBackground() throws Exception {
                    try {
                        cars = parser.parse(brandId, modelId, regionId, currentYearFrom, currentYearTo, currentPriceFrom,
                                currentPriceTo, sortType);
                    } catch (Throwable e) {
                        String err = (e.getMessage() == null) ? "No message" : e.getMessage();
                        Log.e(err);
                        showDataLoadingErrorDialog();
                    }
                    return (cars != null && !cars.isEmpty());
                }

                @Override
                protected void appendCachedData() {
                    if (cars != null) {
                        carAdapter.addAll(cars);
                    }
                }
            };
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        progressDialog.dismiss();
    }

    @OptionsItem(R.id.sort_by_price_desc)
    void sortByPriceDesc(MenuItem item) {
        sortType = SortType.PRICE_DESC;
        item.setChecked(true);
        reloadResults();
    }

    @OptionsItem(R.id.sort_by_price_inc)
    void sortByPriceInc(MenuItem item) {
        sortType = SortType.PRICE_INC;
        item.setChecked(true);
        reloadResults();
    }

    @OptionsItem(R.id.sort_by_year_desc)
    void sortByYearDesc(MenuItem item) {
        sortType = SortType.YEAR_DESC;
        item.setChecked(true);
        reloadResults();
    }

    @OptionsItem(R.id.sort_by_year_inc)
    void sortByYearInc(MenuItem item) {
        sortType = SortType.YEAR_INC;
        item.setChecked(true);
        reloadResults();
    }

    @OptionsItem(R.id.sort_by_date)
    void sortByDate(MenuItem item) {
        sortType = SortType.DATE;
        item.setChecked(true);
        reloadResults();
    }

    private void reloadResults() {
        currentResults.clear();
        loadedCount = 0;
        loadResults();
    }
}
