package ua.avtopoisk.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import domain.Car;
import parsers.AvtopoiskParser;
import ua.avtopoisk.R;
import ua.avtopoisk.broadcastRecievers.SearchResultReceiver;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 13.10.12
 */
public class SearchService extends IntentService {

    public SearchService(){
        super("SearchService");
    }

    public SearchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AvtopoiskParser parser = new AvtopoiskParser();

        ArrayList<Car> cars = null;
        try {
            cars = parser.parse();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }

        Intent resultIntent = new Intent(SearchResultReceiver.ACTION);
        resultIntent.putParcelableArrayListExtra("cars", cars);
        sendBroadcast(resultIntent);
    }
}
