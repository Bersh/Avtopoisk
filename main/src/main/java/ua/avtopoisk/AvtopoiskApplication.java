package ua.avtopoisk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import com.google.inject.Module;
import com.googlecode.androidannotations.annotations.EApplication;
import de.akquinet.android.androlog.Log;
import parsers.AvtopoiskBaseParser;
import parsers.AvtopoiskParser;
import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Application class to switch on strict mode if present
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 12.10.12
 */
@EApplication
public class AvtopoiskApplication extends RoboApplication {
    @Override
    public void onCreate() {
        int applicationFlags = getApplicationInfo().flags;
        if ((applicationFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            try {
                Class strictMode = Class.forName("android.os.StrictMode");
                Method enableDefaults = strictMode.getMethod("enableDefaults");
                enableDefaults.invoke(strictMode);
            } catch (Throwable throwable) {
                Log.d("No StrictMode");
            }
        }
        super.onCreate();
    }

    @Override
    protected void addApplicationModules(List<Module> modules) {
        modules.add(new AvtopoiskModule());
    }

    static class AvtopoiskModule extends AbstractAndroidModule {
        @Override
        protected void configure() {
            bind(AvtopoiskParser.class).to(AvtopoiskBaseParser.class);
        }
    }

    public void showDataLoadingErrorDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setCancelable(false);
        adb.setTitle(R.string.error);
        adb.setMessage(R.string.error_dlg_text);
        adb.setPositiveButton(R.string.error_dlg_yes, onClickListener);
        adb.setNegativeButton(R.string.error_dlg_no, onClickListener);
        adb.create().show();
    }
}
