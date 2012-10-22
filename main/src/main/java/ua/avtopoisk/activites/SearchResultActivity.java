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
import android.widget.ListView;
import com.google.inject.Inject;
import com.googlecode.androidannotations.annotations.*;
import domain.Car;
import org.apache.commons.codec.DecoderException;
import parsers.AvtopoiskParser;
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

@EActivity(R.layout.search_result)
@RoboGuice
public class SearchResultActivity extends ListActivity {
    private ProgressDialog progressDialog;

    @Extra(SearchActivity.BRAND_ID_KEY)
    int brandId;

    @Extra(SearchActivity.MODEL_ID_KEY)
    int modelId;

    @Extra(SearchActivity.REGION_ID_KEY)
    int regionId;

    @Inject
    private AvtopoiskParserImpl parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(this, "", getString(R.string.dlg_progress_data_loading), true);
    }

    @AfterViews
    protected void init() {
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

            try {
                cars = parser.parse(brandId, modelId, regionId);
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
