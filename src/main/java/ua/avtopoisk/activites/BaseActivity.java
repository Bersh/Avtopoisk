package ua.avtopoisk.activites;


import android.support.v7.app.ActionBarActivity;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 07.11.13
 */
public abstract class BaseActivity extends ActionBarActivity {
    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return null;  //TODO implement this
    }
}
