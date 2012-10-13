package ua.avtopoisk.activites;


import android.app.ListActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import ua.avtopoisk.R;
import ua.avtopoisk.broadcastRecievers.SearchResultReceiver;
import ua.avtopoisk.services.SearchService;

/**
 * Search result activity. List of cars returned by parser
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 12.10.12
 */
public class SearchResultActivity extends ListActivity {
    private SearchResultReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        IntentFilter intentFilter = new IntentFilter(SearchResultReceiver.ACTION);
        receiver = new SearchResultReceiver(getListView());
        registerReceiver(receiver, intentFilter);

        Intent intent = new Intent(this, SearchService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
