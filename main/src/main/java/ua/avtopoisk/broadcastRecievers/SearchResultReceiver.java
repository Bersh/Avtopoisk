package ua.avtopoisk.broadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import domain.Car;
import ua.avtopoisk.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BroadcastReceiver to process search result
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 13.10.12
 */
public class SearchResultReceiver extends BroadcastReceiver {
    public static final String ACTION =
            "ua.avtopoisk.broadcastRecievers.CARS_LOADED";

    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final String YEAR = "year";
    private static final String PRICE = "price";
    private static final String IMAGE = "image";

    private final ListView listView;

    public SearchResultReceiver(ListView listView) {
        super();
        this.listView = listView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Car> cars = intent.getParcelableArrayListExtra("cars");
        final ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

        for (Car car : cars) {
            final HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put(BRAND, car.getBrand());
            hm.put(MODEL, car.getModel());
            hm.put(YEAR, car.getYear());
            hm.put(PRICE, Long.valueOf(car.getPrice() / 100).toString() + " $");

            URL url = null;
            Bitmap bmp = null;
            if (!car.getImageUrl().contains("no_foto")) {
                try {
                    url = new URL(car.getImageUrl());
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    Log.e(listView.getContext().getString(R.string.app_name), e.getMessage());
                }
            }
            hm.put(IMAGE, bmp);
            items.add(hm);
        }

        CarAdapter adapter = new CarAdapter(listView.getContext(),
                items, R.layout.list_item,
                new String[]{BRAND, MODEL, YEAR, PRICE, IMAGE},
                new int[]{R.id.brand, R.id.model, R.id.year, R.id.price, R.id.img});

        listView.setAdapter(adapter);
        listView.invalidate();
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
