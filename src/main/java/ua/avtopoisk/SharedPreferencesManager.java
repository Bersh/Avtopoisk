package ua.avtopoisk;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 29.03.2014
 */
public class SharedPreferencesManager {
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    /**
     * Removes given item from shared preferences
     *
     * @param context context
     * @param key     key string resource id
     */
    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SharedPreferences.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * Sets long preference value.
     *
     * @param context the context
     * @param key     key
     * @param value   the value
     */
    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SharedPreferences.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Gets long preference value.
     *
     * @param context      the context
     * @param key          key
     * @param defaultValue the default value
     */
    public static long getLong(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Constants.SharedPreferences.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Set date when brands and regions was updated last time
     *
     * @param lastUpdateDate date when brands and regions was updated last time
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        putLong(context, Constants.SharedPreferences.SHARED_PREFERENCES_KEY_LAST_UPDATED_DATE, lastUpdateDate.getTime());
    }

    /**
     * Get date when when brands and regions was updated last time
     *
     * @return date when brands and regions was updated last time
     */
    public Date getLastUpdateDate() {
        long dateMs = getLong(context, Constants.SharedPreferences.SHARED_PREFERENCES_KEY_LAST_UPDATED_DATE, 0);
        return new Date(dateMs);
    }

}
