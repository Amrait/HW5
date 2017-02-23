package HW5Task1;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by Олексій on 23.02.2017.
 */
public class Service {
    public static void main(String[] args) {
        Service service = new Service();
        service.Run();
    }
    public void Run(){
        this.LoadDriver();
        Connection connection = this.GetConnection("jdbc:postgresql://127.0.0.1:5432/postgres",
                "postgres","Zelcar1945");
        int SelectedTask;
        try {
            while(true){
                SelectedTask = SelectTask();
                switch (SelectedTask){
                    case 1:{
                        this.RunTask1(connection);
                        break;
                    }
                    case 2:{
                        this.RunTask2(connection);
                        break;
                    }
                    case 3:{
                        this.RunTask3(connection);
                        break;
                    }
                    case 4:{
                        this.RunTask4(connection);
                        break;
                    }
                }
                if(!this.Repeat()){
                    break;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //завантаження драйверу для роботи з БД
    private void LoadDriver(){
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер підключено.");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не було підключено.");
        }
    }
    //встановалюємо підключення
    private Connection GetConnection(String url, String name, String password){
        //створюємо підключення
        Connection connection = null;
        try {
            //намагаємось підключитися
            connection = DriverManager.getConnection(url,name,password);
            System.out.println("Підключення встановлено.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        //повертаємо наше підключення
        return connection;
    }
    //для зчитування цілих чисел
    //в метод передаємо текст повідомлення, яке буде надруковане користувачу
    public static int UserInput(String  message) {
        Scanner input = new Scanner(System.in);
        boolean redo = true;
        int result = 0;
        //зациклюємось, поки не введуть коректне значення
        while (redo == true) {
            System.out.println(message);
            //якщо є ціле число
            if (input.hasNextInt()) {
                //записуємо
                result = input.nextInt();
                input.nextLine();
                //виходимо з циклу
                redo = false;
            } else {
                System.out.printf("Некоректне введення. Спробуйте ще раз.\n");
                input.nextLine();
            }
        }
        return result;
    }
    //інструкція по вибору завдання.
    private int SelectTask(){
        System.out.println("Вітаємо в демонстрації роботи з базою даних зусиллями java. " +
                "Оберіть бажану дію та введіть її номер в консоль.");
        System.out.printf("1. Отримання машини за ідентифікатором.\n" +
                "2. Отримання двигуна за ідентифікатором.\n" +
                "3. Додавання машини в таблицю.\n" +
                "4. Додавання двигуна в таблицю.\n");
        int result = this.UserInput("Оберіть завдання:");
        while (result<1 || result >4){
            System.out.println("Опції з введеним номером немає. Спробуйте ще раз.");
            result = this.UserInput("Оберіть завдання:");
        }
        return result;
    }
    //перевірка, чи бажає користувач продовжити і зробити ще щось
    private boolean Repeat(){
        Scanner input = new Scanner(System.in);
        boolean redo;
        while (true) {
            System.out.println("Бажаєте зробити ще щось? (Y/N)");
            String in = input.nextLine();
            if (!"Y".equals(in) && !"y".equals(in)) {
                redo=false;
                break;
            } else {
                redo = true;
                break;
            }
        }
        return redo;
    }
    //шукаємо щось по id. Потім отримуємо ResultSet і з нього будуємо об'єкти
    private ResultSet SearchByID(Connection connection, String query, int id){
        ResultSet resultSet = null;
        PreparedStatement preparedStatement;//для параметризованого запиту
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    //для додавання машини в таблицю машин
    private void InsertCar(Car car, Connection connection){
        try {
            //перевірка на наявність запису з таким id, який вже у об'єкта
            car.CarID = IfExists(connection,"SELECT car_id FROM car WHERE car_id = ?",car.CarID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //створюємо запит
        String query = "INSERT INTO car VALUES(?,?,?,?,?)";
        try {
            //задаємо параметри
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,car.CarID);
            ps.setString(2,car.Model);
            ps.setString(3,car.Manufacturer);
            ps.setInt(4,car.EngineID);
            ps.setLong(5,car.Price);
            //вносимо зміни
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //аналогічне попередньому, тільки для двигунів
    public void InsertEngine(Engine engine, Connection connection){
        try {
            engine.EngineID = IfExists(connection,"SELECT id FROM engine WHERE id = ?",engine.EngineID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "INSERT INTO engine VALUES(?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,engine.EngineID);
            ps.setString(2,engine.Displacement);
            ps.setInt(3,engine.Power);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //перевірка наявності запису з id, який ввів користувач
    private int IfExists(Connection connection, String query, int id) throws SQLException {
        //готуємо і параметризуємо запит для пошуку по id
        ResultSet temp;
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1,id);
        try {
            while (true){
                temp = ps.executeQuery();
                //якщо немає співпадінь, виходимо з циклу
                if(!temp.next()){
                    break;
                }
                //якщо ж вони є, змушуємо задати інший id
                else {
                    id = UserInput("Запис з таким ідентифікатором вже існує. Введіть інший ідентифікатор:");
                    //підставляємо нове значення в запит для перевірки
                    ps.setInt(1,id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //встановлюємо остаточне id. Якщо воно при вході було унікальним, то таким і залишиться.
        //В іншому випадку ми його перезаписуємо примусово.
        int result = id;
        return result;
    }
    //шукаємо автомобіль по id
    public void RunTask1(Connection connection){
        //наш запит
        String query = "SELECT * FROM car WHERE car_id = ?";
        //запитуємо у користувача ідентифікатор
        int id = this.UserInput("Вкажіть ідентифікатор автомобіля:");
        //робимо параметризований пошук
        ResultSet set = this.SearchByID(connection,query,id);
        //технічно, може видати NULL поля. Поки що не знаю, як виправити
        try {
            //створюємо машину по отриманому сету
            Car car = new Car(set);
            //робимо запит на параметри двигуна
            query = "SELECT * FROM engine where id = ?";
            set = this.SearchByID(connection,query,car.EngineID);
            //створюємо двигун
            car.Engine = new Engine(set);
            System.out.println(car.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //шукаємо двигун по id
    public void RunTask2(Connection connection){
        //запит
        String query = "SELECT * FROM engine where id = ?";
        //користувач вводить ідентифікатор
        int id = this.UserInput("Вкажіть ідентифікатор двигуна:");
        ResultSet set = this.SearchByID(connection,query,id);
        try {
            //створюємо двигун
            Engine engine = new Engine(set);
            //змінюємо запит, аби знайти усі машини, які використовуються даний двигун
            query = "SELECT * FROM car WHERE engine_id = ?";
            set = this.SearchByID(connection,query,engine.EngineID);
            //генеруємо список машин-користувачів даного двигуна
            engine.GenerateCarSet(set);
            System.out.println(engine.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //додавання машини до таблиці
    public void RunTask3(Connection connection){
        //створюємо машину, яку будемо додавати
        Car car = new Car(true);
        //закидажмо її в таблицю
        InsertCar(car, connection);
    }
    //додавання двигуна в таблицю, аналогічне додаванню машини.
    public void RunTask4(Connection connection){
        Engine engine = new Engine(true);
        InsertEngine(engine, connection);
    }
}