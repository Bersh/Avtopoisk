package parsers;

import domain.Car;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Parser interface
 */
public interface AvtopoiskParser {
    ArrayList<Car> parse(int brandId, int modelId, int regionId) throws IOException, DecoderException;
    LinkedHashMap<String, Integer> getBrands() throws IOException;
    LinkedHashMap<String, Integer> getModels(int brandId) throws IOException;
    LinkedHashMap<String, Integer> getRegions() throws IOException;
    LinkedHashMap<String, Integer> getYears() throws IOException;
}
