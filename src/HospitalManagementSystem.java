import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password = "Sbc@421926";
    public static void main(String[] args) {
        try{
            //load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner sc=new Scanner(System.in);
        try {
            Connection conn=DriverManager.getConnection(url, username, password);
            Patients patients=new Patients(conn, sc);
            Doctors doctors=new Doctors(conn);

            while(true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                int choice=sc.nextInt();
                sc.nextLine(); // Consume the newline after nextInt()
                switch(choice){
                    case 1:
                    patients.addPatient();System.out.println();break;
                    case 2:
                    patients.viewPatient();System.out.println();break;
                    case 3:
                    doctors.viewDoctors();System.out.println();break;
                    case 4:
                    bookAppointment(patients,doctors,conn,sc);System.out.println();break;
                    case 5:System.out.println("Thank You");return;
                    default:System.out.println("Enter a Valid Choice");break;
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patients patient, Doctors doctor ,Connection conn,Scanner sc){
        System.out.println("Enter patient id");
        int patientid=sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter doctor id");
        int doctorid=sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String appointmentDate=sc.next();
        if(patient.getPatientById(patientid) && doctor.getDoctorById(doctorid)){
            if(checkDoctorAvailability(doctorid,appointmentDate,conn)){
                String appointmentQuery="insert into appointment(patient_id,doctor_id,appointment_date) values(?,?,?);";
                try {
                    PreparedStatement prepstmt=conn.prepareStatement(appointmentQuery);
                    prepstmt.setInt(1, patientid);
                    prepstmt.setInt(2, doctorid);
                    prepstmt.setString(3, appointmentDate);
                    int affectedRows=prepstmt.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("Appointment booked");
                    }
                    else{
                        System.out.println("Failed to book appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor not available on this date");
            }
        }
        else{
            System.out.println("Either doctor or patient doesn't exist");
        }
    }
    public static boolean checkDoctorAvailability(int doctorid, String appointmentDate,Connection conn){
        String query="select count(*) from appointment where doctor_id=? and appointment_date=?;";
        try {
            PreparedStatement prepstmt=conn.prepareStatement(query);
            prepstmt.setInt(1, doctorid);
            prepstmt.setString(2, appointmentDate);
            ResultSet rs=prepstmt.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if(count==0){
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
