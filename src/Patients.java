import java.sql.*;
import java.util.*;
public class Patients {
        private Connection conn;
        private Scanner sc;
        public Patients(Connection conn,Scanner sc){
            this.conn=conn;
            this.sc=sc;
        }

        public void addPatient(){
            System.out.println("Enter patient name");
            String name=sc.nextLine();
            System.out.println("Enter patient age");
            int age=sc.nextInt();
            sc.nextLine();
            System.out.println("Enter patient gender");
            String gender=sc.nextLine();
            try {
                String query="insert into patients (name,age,gender) values(?,?,?);";
                PreparedStatement prepstmt=conn.prepareStatement(query);
                prepstmt.setString(1, name);
                prepstmt.setInt(2, age);
                prepstmt.setString(3, gender);
                int affectedRows=prepstmt.executeUpdate();
                if(affectedRows>0){
                    System.out.println("Patient added successfully!!");
                }
                else{
                    System.out.println("Failed to add patient!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void viewPatient(){
            String query="select * from patients;";
            try {
                PreparedStatement prepstmt=conn.prepareStatement(query);
                ResultSet rs=prepstmt.executeQuery();
                System.out.println("Patients");
                System.out.println("+------------+--------------------+----------+------------+");
                System.out.println("| Patient Id | Name               | Age      | Gender     |");
                System.out.println("+------------+--------------------+----------+------------+");
                while(rs.next()){
                    int id=rs.getInt("id");
                    String name=rs.getString("name");
                    int age=rs.getInt("age");
                    String gender=rs.getString("gender");
                    System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                    System.out.println("+------------+--------------------+----------+------------+");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public boolean getPatientById(int id){
            String query="select * from patients where id=?;";
            try {
                PreparedStatement prepstmt=conn.prepareStatement(query);
                prepstmt.setInt(1, id);
                ResultSet rs=prepstmt.executeQuery();
                if(rs.next()){
                    return true;
                }
                else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
}
