package parsers;

import com.google.inject.Singleton;
import domain.Car;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 09.10.12
 */

@Singleton
public class AvtopoiskParserImpl implements AvtopoiskParser{
    private static final String baseUrl = "http://www.avtopoisk.ua/";
    private static Document baseDoc;
    static {
        try {
            baseDoc = Jsoup.connect(baseUrl).get();
        } catch (IOException e) {

        }
    }

    /**
     * Build params string from given params values
     *
     * @param brandId  brand id
     * @param modelId  model is
     * @param regionId region id
     * @return result params string like &m[]=223&n[]=1761&r[]=2
     */
    private String buildParamsString(int brandId, int modelId, int regionId) {
        StringBuilder sb = new StringBuilder();
        if (brandId > 0) {
            sb.append("&m[]=");
            sb.append(brandId);
        }
        if (modelId > 0) {
            sb.append("&n[]=");
            sb.append(modelId);
        }
        if (regionId > 0) {
            sb.append("&r[]=");
            sb.append(regionId);
        }
        return sb.toString();
    }

    public ArrayList<Car> parse(int brandId, int modelId, int regionId) throws IOException, DecoderException {
        ArrayList<Car> resultList = new ArrayList<Car>();
        String paramsString = buildParamsString(brandId, modelId, regionId);
        for (int w = 1; w < 2; ++w) {
            Document doc = Jsoup.connect(baseUrl + "?w=" + w + paramsString).get();
            Elements carElements = doc.getElementsByClass("car");

            for (Element carElement : carElements) {
                Element info = carElement.getElementsByClass("info").get(0);

                if (!info.getElementsByClass("sold").isEmpty()) {       //check if car already sold
                    continue;
                }

                long price = (long) (Float.parseFloat(carElement.getElementsByClass("price").get(0).text().replace("$", "").replace(".", "")) * 100);
                int year = Integer.parseInt(info.child(0).text());
                String s = info.child(1).child(0).text(); //tmp string
                String[] strings = s.split(" ", 2);  //formatted like "ВАЗ 2101"
                String brand = strings[0]; //ВАЗ
                String model = strings[1]; //2101

                s = info.child(1).child(0).attr("href"); // /go/?s=1&c=217972&u=http%3A%2F%2Favtobazar.infocar.ua%2Fcar%2Fdnepropetrovskaya-oblast%2Fdnepropetrovsk%2Fvaz%2F2106%2Fsedan-1986-217972.html
                s = s.substring(s.indexOf("http"));
                URLCodec codec = new URLCodec();
                String linkToDetails = codec.decode(s);

                Element imageContainer = carElement.getElementsByClass("foto").get(0);
                s = imageContainer.child(0).attr("style");   //background-image:url('http://i2.avtopoisk.ua/foto/1/4618918.jpg')
                strings = s.split("'");
                String imageUrl = strings[1];


                Element values = info.getElementsByClass("values").get(0); // get values separated by <br>

                strings = values.html().replaceAll("\n", "").split("<br />");
                String engineDesc = Jsoup.parse(strings[0]).text(); //line 1.6, i (инж.), бензин

                float engineCapacity;
                try {
                    engineCapacity = Float.parseFloat(strings[0].substring(0, 3));
                } catch (Exception e) {
                    engineCapacity = 0;    //if not present
                }

                int mileage = Integer.parseInt(strings[1].trim());
                if (mileage < 1000) {
                    mileage *= 1000;
                }

                values = info.getElementsByClass("city").get(0);  // city + datePosted + site + id   separated <by br>
                strings = values.html().replaceAll("\n", "").split("<br />");
                String city = Jsoup.parse(strings[0]).text();
                String datePosted = Jsoup.parse(strings[1]).text().replace(" ", "");

                resultList.add(new Car(model, brand, mileage, year, engineCapacity, price, linkToDetails, imageUrl, city, datePosted, engineDesc));
            }
        }
        return resultList;
    }

    /**
     * Parse brands list from select
     *
     * @return brands list
     * @throws IOException is parsing fails
     */
    public LinkedHashMap<String, Integer> getBrands() throws IOException {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        Document doc = Jsoup.connect(baseUrl).get();
        Elements brands = doc.getElementsByClass("select_mark").get(0).children();
        for (Element brand : brands) {
            result.put(brand.text(), Integer.parseInt(brand.val()));
        }
        return result;
    }

    /**
     * Parse models list from select for given brand
     *
     * @return models list
     * @throws IOException is parsing fails
     */
    public LinkedHashMap<String, Integer> getModels(int brandId) throws IOException {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        Document doc = Jsoup.connect(baseUrl + "?m[]=" + brandId).get();
        Elements models = doc.getElementsByAttributeValue("name", "n[]").get(0).children();
        for (Element model : models) {
            result.put(model.text(), Integer.parseInt(model.val()));
        }
        return result;
    }

    /**
     * Parse regions list from select
     *
     * @return regions list
     * @throws IOException is parsing fails
     */
    public LinkedHashMap<String, Integer> getRegions() throws IOException {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        Document doc = Jsoup.connect(baseUrl).get();
        Elements regions = doc.getElementsByAttributeValue("name", "r[]").get(0).children();
        for (Element region : regions) {
            result.put(region.text(), Integer.parseInt(region.val()));
        }
        return result;
    }


    /**
     * Parse years list from select
     *
     * @return years list
     * @throws IOException is parsing fails
     */
    public LinkedHashMap<String, Integer> getYears() throws IOException {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        Document doc = Jsoup.connect(baseUrl).get();
        Elements regions = doc.getElementsByAttributeValue("name", "r[]").get(0).children();
        for (Element region : regions) {
            result.put(region.text(), Integer.parseInt(region.val()));
        }
        return result;
    }
}
