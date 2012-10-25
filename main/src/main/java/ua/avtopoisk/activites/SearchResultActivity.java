package ua.avtopoisk.activites;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.StringRes;
import domain.Car;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang.StringUtils;
import parsers.AvtopoiskParserImpl;
import ua.avtopoisk.CarAdapter;
import ua.avtopoisk.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Search result activity. List of cars returned by parser
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 12.10.12
 */

@EActivity(R.layout.layout_search_result)
@RoboGuice
public class SearchResultActivity extends ListActivity {
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

    @StringRes(R.string.any)
    String anyString;

    @Inject
    private AvtopoiskParserImpl parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);
        progressDialog = ProgressDialog.show(this, "", getString(R.string.dlg_progress_data_loading), true);
    }

    @AfterViews
    protected void init() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_title);
        new SearchAsyncTask().execute(getListView());
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

    private class SearchAsyncTask extends AsyncTask<ListView, Void, Void> {
        private ListView listView;
        private ArrayList<Car> cars;

        @Override
        protected Void doInBackground(ListView... lv) {
            this.listView = lv[0];
            int aYearFrom = StringUtils.isEmpty(yearFrom) || yearFrom.equals(anyString) ? 0 : Integer.parseInt(yearFrom);
            int aYearTo = StringUtils.isEmpty(yearTo) || yearTo.equals(anyString) ? 0 : Integer.parseInt(yearTo);
            int aPriceFrom =  StringUtils.isEmpty(priceFrom) || priceFrom.equals(anyString) ? 0 : Integer.parseInt(priceFrom);
            int aPriceTo = StringUtils.isEmpty(priceTo) || priceTo.equals(anyString) ? 0 : Integer.parseInt(priceTo);
            try {
                cars = parser.parse(brandId, modelId, regionId, aYearFrom, aYearTo, aPriceFrom, aPriceTo);
            } catch (IOException e) {
                Log.e(listView.getContext().getString(R.string.app_name), e.getMessage());
            } catch (DecoderException e) {
                Log.e(listView.getContext().getString(R.string.app_name), e.getMessage());
            }

            if (cars == null) {
                return null;
            }

            for (Car car : cars) {
                URL url;
                Bitmap bmp = null;
                if (car.getImageUrl().contains("no_foto")) {  //if no photo load default image
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.no_photo);
                } else {  //if no photo present load it from net
                    try {
                        url = new URL(car.getImageUrl());
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (Exception e) {
                        Log.e(listView.getContext().getString(R.string.app_name), e.getMessage());
                    }
                }
                car.setImage(bmp);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            CarAdapter adapter = new CarAdapter(listView.getContext(), R.layout.cars_list_item, cars);

            listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
}
