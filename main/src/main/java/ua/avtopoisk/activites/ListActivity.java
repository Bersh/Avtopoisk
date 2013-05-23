package ua.avtopoisk.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private ArrayAdapter<String> adapter;

    @ViewById(R.id.edit_filter)
    EditText filter;

    private final TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s);
        }

    };

    @Override
    protected void onStop() {
        super.onStop();
        filter.removeTextChangedListener(filterTextWatcher);
    }

    @AfterViews
    protected void init() {
        filter.addTextChangedListener(filterTextWatcher);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, brandNames);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_EXTRA_BRAND, adapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
