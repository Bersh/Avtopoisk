package ua.avtopoisk;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import com.google.inject.Module;
import com.googlecode.androidannotations.annotations.EApplication;
import de.akquinet.android.androlog.Log;
import parsers.AvtopoiskParser;
import parsers.AvtopoiskParserImpl;
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
            bind(AvtopoiskParser.class).to(AvtopoiskParserImpl.class);
        }
    }
}
