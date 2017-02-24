package HW5Task1;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by Олексій on 24.02.2017.
 */
public class ServiceTest {
    public static Service service = new Service();

    @Test
    public void runTask1() throws Exception {

        Connection connection = service.GetConnection("jdbc:postgresql://127.0.0.1:5432/postgres",
                "postgres","Zelcar1945");
        Car car2 = new Car();
        car2.CarID = 15;
        car2.Model = "Test Model";
        car2.Manufacturer = "Test Manufacturer";
        car2.EngineID = 1;
        car2.Price = 1000;
        car2.Engine = new Engine();
        String query = "DELETE FROM car WHERE car_id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1,car2.CarID);
        ps.executeUpdate();
        service.InsertCar(car2,connection);
        query = "SELECT * FROM car WHERE car_id = ?";
        ResultSet set = service.SearchByID(connection,query,15);
        Car car = new Car();
        try {
            car = new Car(set);
            car.Engine = new Engine();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals("Маємо машини",car2,car);
        ps.executeUpdate();
        try
        {
            if(connection!=null)
            {
                connection.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void runTask2() throws Exception {

    }

    @Test
    public void runTask3() throws Exception {

    }

    @Test
    public void runTask4() throws Exception {

    }

}