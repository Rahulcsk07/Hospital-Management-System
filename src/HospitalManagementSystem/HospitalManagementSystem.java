package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private  static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="Rahul@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try {
            Connection connection= DriverManager.getConnection(url,username,password);
            patient patient=new patient(connection,scanner);
            Doctor doctor=new Doctor((connection));
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.Add Patient");
                System.out.println("2.View Patient");
                System.out.println("3.view Doctors");
                System.out.println("4.Book Appointment");
                System.out.println("5.exist");
                System.out.println("Enter Your Choice: ");
                int choice=scanner.nextInt();

                switch (choice){
                    case 1:
                        //Add patient
                        patient.addpatient();
                        System.out.println();
                    case 2:
                        //View Patient
                        patient.viewpatient();
                        System.out.println();
                    case 3:
                        //view doctor
                        doctor.viewDoctors();
                        System.out.println();
                    case 4:
                        //Book appointment
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                    case 5:
                        return;
                    default:
                        System.out.println("enter valid choice!!!");

                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient id:");
        int patientId=scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int doctorId=scanner.nextInt();
        System.out.println("Enter Appointment Date(YYYY-MM-DD): ");
        String appointmentDate=scanner.next();
        if (patient.getPatientByid(patientId)&&doctor.getDoctorByid(doctorId)){

            if (checkDoctorAvailability(doctorId,appointmentDate,connection)){

                String appointmentQuery="INSERT INTO appointments(patient_id,doctor_id,appointment_date)VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowAffected = preparedStatement.executeUpdate();
                    if (rowAffected > 0) {
                        System.out.println("Appointment Booked!");
                    } else {
                        System.out.println("Failed to book appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not Available on this date");
            }

        }else {
            System.out.println("either patient or doctor does not exist");
        }

    }
    public  static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection){
        String query="SELECT COUNT (*) FROM appointments WHERE doctor_id=?AND appointment_date=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                int count=resultSet.getInt(1);
                if (count==0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
