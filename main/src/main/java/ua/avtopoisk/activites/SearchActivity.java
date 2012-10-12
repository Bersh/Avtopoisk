package ua.avtopoisk.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ua.avtopoisk.R;

public class SearchActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Button btnFind = (Button) findViewById(R.id.btn_find);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                startActivity(intent);
            }
        });
    }
}
