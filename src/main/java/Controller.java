import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

  @FXML
  private Label lblOutput;

  @FXML
  private TextField txtEmpId;

  @FXML
  private Label lblEmpInfo;

  @FXML
  private ComboBox<String> cmdQuantity;

  @FXML
  void showDetails(ActionEvent event){
    connectToDb();
  }

  public void initialize(){

    for (int i = 1; i <= 10; i++) {
      cmdQuantity.getItems().add(Integer.toString(i));
    }
    cmdQuantity.getSelectionModel().selectFirst();
  }

  public void connectToDb(){
    //JBDC driver name and database URL
    final String JDBC_DRIVER = "org.h2.Driver";  //where we associate the driver - Driver class by default
    final String DB_URL = "jdbc:h2:./res/HR";   //location of database

    //  Database credentials
    final String USER = "";  //User and password left as default for now
    final String PASS = "";
    Connection conn = null;  //Connection and Statement objects
    Statement stmt = null;

    try {
      // STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);  //Registering the JDBC database driver

      //STEP 2: Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);  //Connecting to database...

      //STEP 3: Execute a query(peticion)
      stmt = conn.createStatement();  // Creating a statement - creating database

      String empID = txtEmpId.getText();

      /*String insertSql = "INSERT INTO EMPLOYEES(employee_id, first_name, last_name, email, "
          + "phone_int, hire_date, job_id, salary, commission_pct, manager_id, department_id, bonus) VALUES (300, 'Jaysson',"
          + "'Balbuena', 'jbalbuena', '564.786.8974', '2015-9-9', 'AD_PRES', 50000, 450, '300', 90, 56)";*/

     // stmt.executeUpdate(insertSql);

      String sql = "SELECT email, first_name, last_name"
          + " FROM EMPLOYEES"
          + " WHERE EMPLOYEE_ID = " + empID;

      ResultSet rs = stmt.executeQuery(sql); //Executing a statement and receiving Resultset.

      //while (rs.next()) {
      rs.next();

      String empEmail = rs.getString(1);

      String empFirstName = rs.getString(2);

      String empLastName = rs.getString(3);
      //System.out.println(empEmail + " " + empFirstName + " " + empLastName + "@Arthrex.com");
      //}
      lblEmpInfo.setText(empFirstName + " " +empLastName + " " + empEmail  + "@Arthrex.com");

      // STEP 4: Clean-up the environment
      stmt.close();  //Explicitly closing all the database resources versus relying on the JVM's garbage collection
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }


}