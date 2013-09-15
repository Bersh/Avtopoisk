package ua.avtopoisk.parser;

import org.apache.commons.codec.DecoderException;
import ua.avtopoisk.model.Car;
import ua.avtopoisk.model.SortType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Parser interface
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 22.10.12
 */
public interface AvtopoiskParserInterface {
    /**
     * Basic parse function
     *
     * @param brandId   brand id
     * @param modelId   model id
     * @param regionId  region id
     * @param yearFrom  year from
     * @param yearTo    year to
     * @param priceFrom price from
     * @param priceTo   price to
     * @param sortType  sorting type (for example "by date")
     * @param bodyType  car body type (for example "sedan")
     * @param addedType added filter
     * @return list of {@link Car} by given params
     * @throws IOException      if parser fails
     * @throws DecoderException if URL decoder fails
     */
    ArrayList<Car> parse(int brandId, int modelId, int regionId, int yearFrom, int yearTo, int priceFrom, int priceTo,
                         SortType sortType, int bodyType, int addedType) throws IOException, DecoderException;

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

    int getLastRequestResultsCount();

    /**
     * Set current page number to 0
     */
    void resetCurrentPage();
}
