package parsers;

import domain.Car;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 09.10.12
 */
public class AvtopoiskParser {
    private static final String baseUrl = "http://www.avtopoisk.ua/";

    public ArrayList<Car> parse() throws IOException {
        ArrayList<Car> resultList = new ArrayList<Car>();
        for (int w = 1; w < 2; ++w) {
            Document doc = Jsoup.connect(baseUrl + "?w=" + w).get();
            Elements carElements = doc.getElementsByClass("car");

            for (Element carElement : carElements) {
                Element info = carElement.getElementsByClass("info").get(0);

                if(!info.getElementsByClass("sold").isEmpty()) {       //check if car already sold
                    continue;
                }

                long price = (long) (Float.parseFloat(carElement.getElementsByClass("price").get(0).text().replace("$", "").replace(".", "")) * 100);
                int year = Integer.parseInt(info.child(0).text());
                String s = info.child(1).child(0).text(); //tmp string
                String[] strings = s.split(" ", 2);  //formatted like "ВАЗ 2101"
                String brand = strings[0]; //ВАЗ
                String model = strings[1]; //2101

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

                resultList.add(new Car(model, brand, mileage, year, engineCapacity, price, imageUrl, city, datePosted, engineDesc));
            }
        }
        return resultList;
    }

    /**
     * Parse brands list from select
     * @return brands list
     * @throws IOException is parsing fails
     */
    public HashMap<String, Integer> getBrands() throws IOException {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        Document doc = Jsoup.connect(baseUrl).get();
        Elements brands = doc.getElementsByClass("select_mark").get(0).children();
        for(Element brand : brands) {
            result.put(brand.text(), Integer.parseInt(brand.val()));
        }
        return result;
    }
}
