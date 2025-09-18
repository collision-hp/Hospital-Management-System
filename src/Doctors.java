import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctors {
    private Connection conn;
        public Doctors(Connection conn){
            this.conn=conn;
        }

        public void viewDoctors(){
            String query="select * from doctor;";
            try {
                PreparedStatement prepstmt=conn.prepareStatement(query);
                ResultSet rs=prepstmt.executeQuery();
                System.out.println("+------------+--------------------+------------------+");
                System.out.println("| Doctor Id  | Name               | Specialization   |");
                System.out.println("+------------+--------------------+------------------+");
                while(rs.next()){
                    int id=rs.getInt("id");
                    String name=rs.getString("name");
                    String specialization=rs.getString("specialization");
                    System.out.printf("| %-10s | %-18s | %-16s |\n", id, name, specialization);
                    System.out.println("+------------+--------------------+------------------+");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public boolean getDoctorById(int id){
            String query="select * from doctor where id=?;";
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
