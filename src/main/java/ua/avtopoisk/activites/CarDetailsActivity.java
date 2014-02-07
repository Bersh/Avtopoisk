package ua.avtopoisk.activites;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import ua.avtopoisk.Constants;
import ua.avtopoisk.R;
import ua.avtopoisk.model.Car;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 17.09.13
 */
@EActivity(R.layout.activity_car_details)
public class CarDetailsActivity extends BaseActivity {
    @Extra(Constants.KEY_EXTRA_CAR)
    protected Car currentCar;

    @ViewById(R.id.info)
    protected WebView webViewCarDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); //request progress bar in action bar
    }

    @AfterViews
    protected void init() {
        WebSettings webSettings = webViewCarDetails.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Avtopoisk-android");

        webViewCarDetails.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(false);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setProgressBarIndeterminateVisibility(true);
                super.onPageStarted(view, url, favicon);
            }
        });

        webViewCarDetails.loadUrl(currentCar.getLinkToDetails());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewCarDetails.canGoBack()) {
            webViewCarDetails.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
