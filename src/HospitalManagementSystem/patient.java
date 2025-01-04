package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class patient {
    private Connection connection;
    private Scanner scanner;
    public patient(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void addpatient(){
        System.out.println(" Enter Patient Name:");
        String name=scanner.next();
        System.out.println("enter patient age:");
        int age= scanner.nextInt();
        System.out.println("enter patient gender:");
        String gender=scanner.next();
        try {
            String query="INSERT INTO patients(name,age,gender)VALUE(?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows=preparedStatement.executeUpdate();
            if (affectedRows>0){
                System.out.println("patient added successfully");
            }else {
                System.out.println("failed to add patient");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public  void viewpatient(){
        String query="select * from Patients";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("patients:");
            System.out.println("+---------------+---------------+------------------+-----------------+");
            System.out.println("|   Patient Id  |     Name      |         Age      |      Gender     |");
            System.out.println("+---------------+---------------+------------------+-----------------+");
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String name =resultSet.getString("name");
                int age=resultSet.getInt("age");
                String gender=resultSet.getString("gender");
                System.out.printf("|%-15s|%-15s|%-18s|%-17s|\n",id,name,age,gender);
                System.out.println("+---------------+---------------+------------------+-----------------+");
            }
         }catch (SQLException e){
          e.printStackTrace();
        }
    }
    public boolean getPatientByid(int id){
        String query=" SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}