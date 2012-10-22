package parsers;

import domain.Car;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Parser interface
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 22.10.12
 */
public interface AvtopoiskParser {
    /**
     * Basic parse function
     * @param brandId brand id
     * @param modelId model id
     * @param regionId region id
     * @param yearFrom year from
     * @param yearTo year to
     * @return list of {@link domain.Car} by given params
     * @throws IOException if parser fails
     * @throws DecoderException if URL decoder fails
     */
    ArrayList<Car> parse(int brandId, int modelId, int regionId, int yearFrom, int yearTo) throws IOException, DecoderException;

    /**
     * Parse brands list from select
     *
     * @return brands list
     * @throws IOException is parsing fails
     */
    LinkedHashMap<String, Integer> getBrands() throws IOException;

    /**
     * Parse models list from select for given brand
     *
     * @return models list
     * @throws IOException is parsing fails
     */
    LinkedHashMap<String, Integer> getModels(int brandId) throws IOException;

    /**
     * Parse regions list from select
     *
     * @return regions list
     * @throws IOException is parsing fails
     */
    LinkedHashMap<String, Integer> getRegions() throws IOException;

    /**
     * Parse years list from select
     *
     * @return years list
     * @throws IOException is parsing fails
     */
    ArrayList<String> getYears() throws IOException;
}
