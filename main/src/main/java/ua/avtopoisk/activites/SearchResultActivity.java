package ua.avtopoisk.activites;


import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import domain.Car;
import parsers.AvtopoiskParser;
import ua.avtopoisk.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends ListActivity {

    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final String YEAR = "year";
    private static final String PRICE = "price";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        AvtopoiskParser parser = new AvtopoiskParser();

        ArrayList<Car> cars = null;
        try {
            cars = parser.parse();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
            finish();
        }
        final ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

        for (Car car : cars) {
            final HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put(BRAND, car.getBrand());
            hm.put(MODEL, car.getModel());
            hm.put(YEAR, car.getYear());
            hm.put(PRICE, Float.valueOf(car.getPrice() / 100).toString() + "$");

            URL url = null;
            Bitmap bmp = null;
            if (!car.getImageUrl().contains("no_foto")) {
                try {
                    url = new URL(car.getImageUrl());
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    Log.e(getString(R.string.app_name), e.getMessage());
                }
            }
            hm.put(IMAGE, bmp);
            items.add(hm);
        }

        CarAdapter adapter = new CarAdapter(this,
                items, R.layout.list_item,
                new String[]{BRAND, MODEL, YEAR, PRICE, IMAGE},
                new int[]{R.id.brand, R.id.model, R.id.year, R.id.price, R.id.img});

        setListAdapter(adapter);
    }

    private class CarAdapter extends SimpleAdapter {

        public CarAdapter(Context context, List<? extends Map<String, ?>> data,
                          int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            if (data.get(IMAGE) != null) {
                ((ImageView) convertView.findViewById(R.id.img)).setImageBitmap((Bitmap) data.get(IMAGE));
            } else {
                ((ImageView) convertView.findViewById(R.id.img)).setImageResource(R.drawable.no_photo);
            }

            return convertView;
        }

    }
}
