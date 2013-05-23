package ua.avtopoisk;

import org.androidannotations.annotations.EBean;

import java.util.LinkedHashMap;

/**
 * It's fucking antipattern, but i didn't manage to share LinkedHashMap thru activities without losing elements order.
 * Needs to be removed later
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 19.10.12
 */
@EBean(scope = EBean.Scope.Singleton)
public class BrandsAndRegionsHolder {
    public LinkedHashMap<String, Integer> brandsMap;
    public LinkedHashMap<String, Integer> regionsMap;
}
