package ua.avtopoisk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

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
    private Drawable defaultImage;
    private String imageKey;

    public CarAdapter(Context context, List<? extends Map<String, ?>> data,
                      int resource, String[] from, int[] to, String imageKey) {
        super(context, data, resource, from, to);
        defaultImage = context.getResources().getDrawable(R.drawable.no_photo);
        this.imageKey = imageKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        if (data.get(imageKey) != null) {
            ((ImageView) convertView.findViewById(R.id.img)).setImageBitmap((Bitmap) data.get(imageKey));
        } else {
            ((ImageView) convertView.findViewById(R.id.img)).setImageDrawable(defaultImage);
        }

        return convertView;
    }

}