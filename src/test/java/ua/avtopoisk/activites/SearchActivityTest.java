/*
package ua.avtopoisk.activites;

import android.widget.Spinner;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import ua.avtopoisk.BrandsAndRegionsHolder;
import ua.avtopoisk.R;

import java.util.LinkedHashMap;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

*/
/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 27.10.12
 *//*


@RunWith(RobolectricTestRunner.class)
public class SearchActivityTest {
    private SearchActivity_ searchActivity = new SearchActivity_();
    private LinkedHashMap<String, Integer> brandsMap = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> regionsMap = new LinkedHashMap<String, Integer>();
    private BrandsAndRegionsHolder brandsAndRegionsHolder;

    @Before
    public void setUp() {
        brandsMap.put("test brand", 1);
        regionsMap.put("test region", 1);
        brandsAndRegionsHolder = Mockito.mock(BrandsAndRegionsHolder.class);
        brandsAndRegionsHolder.brandsMap = brandsMap;
        brandsAndRegionsHolder.regionsMap = regionsMap;
        searchActivity.brandsAndRegionsHolder = brandsAndRegionsHolder;

//        searchActivity.onCreate(null);
    }

    @Test
    public void appName() throws Exception {
        String appName = searchActivity.getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Avtopoisk"));
    }

    @Test
    @Ignore
    public void viewsPresent() throws Exception {
        assertNotNull(searchActivity.brands);
        assertEquals(searchActivity.brands.getClass(), Spinner.class);
        assertNotNull(searchActivity.models);
        assertEquals(searchActivity.models.getClass(), Spinner.class);
        assertNotNull(searchActivity.regions);
        assertEquals(searchActivity.regions.getClass(), Spinner.class);
        assertNotNull(searchActivity.yearFrom);
        assertEquals(searchActivity.yearFrom.getClass(), Spinner.class);
        assertNotNull(searchActivity.yearTo);
        assertEquals(searchActivity.yearTo.getClass(), Spinner.class);
        assertNotNull(searchActivity.priceFrom);
        assertEquals(searchActivity.priceFrom.getClass(), Spinner.class);
        assertNotNull(searchActivity.priceTo);
        assertEquals(searchActivity.priceTo.getClass(), Spinner.class);

    }
}
*/
