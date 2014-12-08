package ua.avtopoisk;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 22.05.13
 */
public class Constants {
    public static final String LOG_TAG = "Avtopoisk";

    public static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public static final String BRAND_ID_KEY = "brand";
    public static final String MODEL_ID_KEY = "model";
    public static final String REGION_ID_KEY = "region";
    public static final String YEAR_FROM_KEY = "yearFrom";
    public static final String YEAR_TO_KEY = "yearTo";
    public static final String PRICE_FROM_KEY = "priceFrom";
    public static final String PRICE_TO_KEY = "priceTo";
    public static final String SORT_TYPE_KEY = "sortType";
    public static final String BODY_TYPE_KEY = "bodyType";
    public static final String ADDED_TYPE_KEY = "addedType";

    public static final String KEY_EXTRA_COLLECTION = "collection";
    public static final String KEY_EXTRA_SELECTED = "selected";
    public static final String KEY_EXTRA_CAR = "car";

    public static final String KEY_EXTRA_SELECTION_MODE= "selection_mode";

    public static final int DEFAULT_TIMEOUT = 10000;
    public static final int DATA_UPDATE_INTERVAL = 20; //in days

    public static class SelectionMode {
        public static final int SELECTION_MODE_BRANDS = 0;
        public static final int SELECTION_MODE_MODELS = 1;
    }

    public static class SharedPreferences {
        public static final String SHARED_PREFERENCES_NAME = "Avtopoisk";
        public static final String SHARED_PREFERENCES_KEY_LAST_UPDATED_DATE = "last_updated_date";
    }
}
