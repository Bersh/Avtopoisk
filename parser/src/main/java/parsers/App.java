package parsers;

import domain.Car;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        AvtopoiskParser parser = new AvtopoiskParser();
        List<Car> cars = parser.parse();
        for (Car car : cars) {
            System.out.println(car.getBrand() + " " + car.getModel() + " EngineCapacity:" + car.getEngineCapacity() +
                    " year:" + car.getYear() + " milage:" + car.getMileage() + " price:" + car.getPrice() / 100 + "$");
        }
        System.out.println("ВСЕГО: " + cars.size());
    }
}
