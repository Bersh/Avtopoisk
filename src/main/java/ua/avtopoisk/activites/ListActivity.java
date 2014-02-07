package ua.avtopoisk.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import org.androidannotations.annotations.*;
import ua.avtopoisk.Constants;
import ua.avtopoisk.R;
import ua.avtopoisk.model.Brand;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 22.05.13
 */
@EActivity(R.layout.activity_list)
public class ListActivity extends BaseActivity {
    @Extra(Constants.KEY_EXTRA_COLLECTION)
    ArrayList<String> collection;

    @Extra(Constants.KEY_EXTRA_SELECTION_MODE)
    int selectionMode;

    @ViewById(R.id.list)
    ListView list;
    private ArrayAdapter adapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        filter.removeTextChangedListener(filterTextWatcher);
    }

    @Click(R.id.btn_clear_filter)
    protected void btnClearFilterClick(View view) {
        filter.setText("");
    }

    @AfterViews
    protected void init() {
        try {
            switch (selectionMode) {
                case Constants.SelectionMode.SELECTION_MODE_BRANDS:
                    List<Brand> brands = dbHelper.getBrandsDao().queryForAll();
                    adapter = new ArrayAdapter<Brand>(this, android.R.layout.simple_list_item_1, brands);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.KEY_EXTRA_SELECTED, ((Brand)adapter.getItem(position)).getId());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    break;
                default:
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, collection);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.KEY_EXTRA_SELECTED, adapter.getItem(position).toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    break;
            }
            filter.addTextChangedListener(filterTextWatcher);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
