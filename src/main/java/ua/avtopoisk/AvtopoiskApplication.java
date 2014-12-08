package ua.avtopoisk;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.splunk.mint.Mint;

import org.androidannotations.annotations.EApplication;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 12.10.12
 */
@EApplication
public class AvtopoiskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!Constants.IS_DEBUG) {
            Mint.initAndStartSession(this, "d41ee481");
        }
    }

    public static void showDataLoadingErrorDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);
        adb.setTitle(R.string.error);
        adb.setMessage(R.string.error_dlg_text);
        adb.setPositiveButton(R.string.error_dlg_yes, onClickListener);
        adb.setNegativeButton(R.string.error_dlg_no, onClickListener);
        adb.create().show();
    }
}
