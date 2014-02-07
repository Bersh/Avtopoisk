package ua.avtopoisk.activites;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import ua.avtopoisk.Constants;
import ua.avtopoisk.db.DBHelper;
import ua.avtopoisk.db.DBManager;

import java.sql.SQLException;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 07.11.13
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected DBHelper dbHelper;
    protected DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        try {
            dbManager = new DBManager(dbHelper);
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "Can't create DBManager");
            Log.e(Constants.LOG_TAG, "SQLException", e);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return null;  //TODO implement this
    }
}
