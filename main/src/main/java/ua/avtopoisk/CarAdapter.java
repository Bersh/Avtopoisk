package ua.avtopoisk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom adapter. Created to load images properly
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 13.10.12
 */
public class CarAdapter extends SimpleAdapter {
    private static final String HEADER = "header";
    private static final String PRICE = "price";
    private static final String IMAGE = "image";
    private static final String CITY = "city";
    private static final String DATE_POSTED = "datePosted";
    private static final String ENGINE_DESC = "engineDesc";

    private Drawable defaultImage;
    private String imageKey;
    private Context context;

    public CarAdapter(Context context, List<? extends Map<String, ?>> data,
                      int resource, String[] from, int[] to, String imageKey) {
        super(context, data, resource, from, to);
        defaultImage = context.getResources().getDrawable(R.drawable.no_photo);
        this.imageKey = imageKey;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = super.getView(position, convertView, parent);
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

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
        if(data != null) {
            ViewHolder holder = (ViewHolder)v.getTag();

            if (data.get(imageKey) != null) {
                holder.imageView.setImageBitmap((Bitmap) data.get(imageKey));
            } else {
                holder.imageView.setImageDrawable(defaultImage);
            }

            holder.carInfoHeader.setText((String)data.get(HEADER));
            holder.engineDesc.setText((String)data.get(ENGINE_DESC));
            holder.city.setText((String)data.get(CITY));
            holder.price.setText((String)data.get(PRICE));
            holder.datePosted.setText((String)data.get(DATE_POSTED));
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