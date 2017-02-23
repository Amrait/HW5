package HW5Task1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Олексій on 23.02.2017.
 */
public class Car {
    public int CarID;
    public String Model;
    public String Manufacturer;
    public Engine Engine;
    public int EngineID;
    public long Price;


    public Car(){}
    //конструктор для створення машини з запиту по базі данних
    //поки що використовується лише для генерації машин, які підтягуються для двигуна
    public Car (ResultSet resultSet) throws SQLException {
        //оскільки у нас лише унікальні id, результат має бути лише один
        if (resultSet.next()) {
            //реєструємо усі поля, окрім поля типу Engine
            this.CarID = resultSet.getInt("car_id");
            this.Model = resultSet.getString("model");
            this.Manufacturer = resultSet.getString("manufacturer");
            this.EngineID = resultSet.getInt("engine_id");
            this.Price = resultSet.getLong("price");
        }
    }
    //це аналог конструктора з параметрами, але їх ми отримуємо через внутрішні виклики
    public Car(boolean dummy){
        Scanner input = new Scanner(System.in);
        //задаємо ідентифікатор машини. UserInput є static і не дозволить вийти з нього, поки не буде введено int
        this.CarID = Service.UserInput("Вкажіть ідентифікатор машини для додавання її в таблицю:");
        //зчитуємо назву автомобіля
        System.out.println("Введіть назву моделі автомобіля:");
        this.Model = input.nextLine();
        //зчитуємо виробника автомобіля
        System.out.println("Введіть назву виробника автомобіля:");
        this.Manufacturer = input.nextLine();
        //задаємо ідентифікатор двигуна, що використовується
        this.EngineID = Service.UserInput("Вкажіть ідентифікатор двигуна автомобіля:");
        //задаємо ціну
        this.Price = Service.UserInput("Вкажіть ціну автомобіля:");
        //лишаємо null, за потреби застосуємо конструктор для Engine власноруч
        this.Engine = null;
    }

    @Override
    public String toString() {
        return "Car{" +
                "CarID=" + CarID +
                ", Model='" + Model + '\'' +
                ", Manufacturer='" + Manufacturer + '\'' +
                ", EngineID=" + EngineID +
                ", Engine Displacement=" + Engine.Displacement +
                ", Engine Power=" + Engine.Power +
                ", Car Price=" + Price +
                '}';
    }
}
