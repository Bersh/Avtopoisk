package ua.avtopoisk.activites;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;
import ua.avtopoisk.Constants;
import ua.avtopoisk.R;

import java.util.ArrayList;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 22.05.13
 */
@EActivity(R.layout.activity_list)
public class ListActivity extends Activity {
    @Extra(Constants.KEY_EXTRA_BRAND_NAMES)
    ArrayList<String> brandNames;

    @ViewById(R.id.list)
    ListView list;

    @AfterViews
    protected void init() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, brandNames);
        list.setAdapter(adapter);
    }
}
