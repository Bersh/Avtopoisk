package ua.avtopoisk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.RootContext;
import domain.Car;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom adapter. Created to load images properly
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 13.10.12
 */
public class CarAdapter extends ArrayAdapter<Car> {
    private Context context;

    public CarAdapter(Context context, int resource, List<Car> data) {
        super(context, resource, data);
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.cars_list_item, null);
            ViewHolder holder = new ViewHolder();
            holder.imageView = (ImageView)v.findViewById(R.id.img);
            holder.carInfoHeader = (TextView)v.findViewById(R.id.car_info_header);
            holder.city = (TextView)v.findViewById(R.id.city);
            holder.price = (TextView)v.findViewById(R.id.price);
            holder.datePosted = (TextView)v.findViewById(R.id.date_posted);
            holder.engineDesc = (TextView)v.findViewById(R.id.engine_desc);
            v.setTag(holder);
        }

        Car car = (Car) getItem(position);
        if(car != null) {
            ViewHolder holder = (ViewHolder)v.getTag();

            holder.imageView.setImageBitmap(car.getImage());
            holder.carInfoHeader.setText(car.getYear() + "' " + car.getBrand() + " " + car.getModel());
            holder.engineDesc.setText(car.getEngineDesc());
            holder.city.setText(car.getCity());
            holder.price.setText(Long.valueOf(car.getPrice() / 100).toString() + " $");
            holder.datePosted.setText(car.getDatePosted());
        }

        return v;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView carInfoHeader;
        TextView engineDesc;
        TextView city;
        TextView price;
        TextView datePosted;
    }
}