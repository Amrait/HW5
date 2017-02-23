package HW5Task1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Олексій on 23.02.2017.
 */
public class Engine {
    public int EngineID;
    public String Displacement;
    public int Power;
    public Set<Car> SupportedCars;

    public Engine(){}
    //конструктор для створення двигуна з запиту по базі данних
    public Engine (ResultSet resultSet) throws SQLException {
        //оскільки id унікальні, то матимемо лише один результат або жодного
        if (resultSet.next()) {
            this.EngineID = resultSet.getInt("id");
            this.Displacement = resultSet.getString("displacement");
            this.Power = resultSet.getInt("power");
            this.SupportedCars = null;
        }
    }
    //це аналог конструктора з параметрами, але їх ми отримуємо через внутрішні виклики
    public Engine(boolean dummy)
    {
        Scanner input = new Scanner(System.in);
        //зчитуємо id. UserInput є static і не дозволить вийти з нього, поки не буде введено int
        this.EngineID = Service.UserInput("Вкажіть ідентифікатор двигуна, аби додати його в базу даних:");
        //зчитуємо розташування двигуна в машині
        System.out.println("Вкажіть розташування двигуна в машині:");
        this.Displacement = input.nextLine();
        //зчитуємо потужність двигуна
        this.Power = Service.UserInput("Вкажіть потужність двигуна:");
    }
    //генеруємо список машин, які використовують даний двигун. Можливо, є краще рішення
    public void GenerateCarSet(ResultSet resultSet) throws SQLException {
        this.SupportedCars = new HashSet<Car>();
        while (resultSet.next()) {
            Car car = new Car();
            car.CarID = resultSet.getInt("car_id");
            car.Model = resultSet.getString("model");
            car.Manufacturer = resultSet.getString("manufacturer");
            car.EngineID = resultSet.getInt("engine_id");
            car.Price = resultSet.getLong("price");
            car.Engine = this;
            SupportedCars.add(car);
        }
    }

    @Override
    public String toString() {
        return "Engine{" +
                "EngineID=" + EngineID +
                ", Displacement='" + Displacement + '\'' +
                ", Power=" + Power +
                ", SupportedCars=" + SupportedCars +
                '}';
    }
}
