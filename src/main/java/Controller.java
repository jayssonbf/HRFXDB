import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
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

  public void connectToDb() {
    //JBDC driver name and database URL
    final String JDBC_DRIVER = "org.h2.Driver";  //where we associate the driver - Driver class by default
    final String DB_URL = "jdbc:h2:./res/HR";   //location of database

    //  Database credentials
    final String USER = "";  //User and password left as default for now
    final String PASS = "";
    Connection conn = null;
    //PreparedStatement allows to write parameterized query which gives better performance
    PreparedStatement pStmt = null;

    try {
      //STEP 1: Register JDBC driver
      Class.forName(JDBC_DRIVER);  //Registering the JDBC database driver

      //STEP 2: Open a connection
      conn = DriverManager.getConnection(DB_URL, USER, PASS);  //Connecting to database...

      //Get the employeeID# input from textfield
      String empID = txtEmpId.getText();

      String sql = "SELECT email, first_name, last_name"
          + " FROM EMPLOYEES"
          + " WHERE EMPLOYEE_ID =?";

      //STEP 3: Execute a query
      pStmt = conn.prepareStatement(sql); // Creating a statement - creating database
      pStmt.setString(1, empID);
      ResultSet result = pStmt.executeQuery();


      if (result.next()) {
        String empEmail = result.getString(1);
        String empFirstName = result.getString(2);
        String empLastName = result.getString(3);
        //Display employee's info to the user interface
        lblEmpInfo.setText(empFirstName + " " + empLastName + " "
            + empEmail + "@Arthrex.com");
      } else if (empID.equals("300")) {

        lblEmpInfo.setText("Inserting record to the Database...");

        String insertSql = "INSERT INTO EMPLOYEES(employee_id, first_name, last_name, email, "
            + "phone_int, hire_date, job_id, salary, commission_pct, manager_id, department_id, bonus) VALUES (?, ?,"
            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        pStmt = conn.prepareStatement(insertSql);
        pStmt.setString(1, empID);
        pStmt.setString(2, "Jaysson");
        pStmt.setString(3, "Balbuena");
        pStmt.setString(4, "jbalbuena");
        pStmt.setString(5, "564.786.8974");
        pStmt.setString(6, "2015-9-9");
        pStmt.setString(7, "AD_PRES");
        pStmt.setString(8, "50000");
        pStmt.setString(9, "450");
        pStmt.setString(10, "300");
        pStmt.setString(11, "90");
        pStmt.setString(12, "56");

        int rowUpdated = pStmt.executeUpdate();
        lblEmpInfo.setText("Employee has been inserted to the Database!");

        /*lblEmpInfo.setText(result.getString(1) + " " + result.getString(2) + " "
            + result.getString(3) + "@Arthrex.com");*/
      } else {
        lblEmpInfo.setText("Employee no found, try again!");
      }

      // STEP 4: Clean-up the environment
      pStmt.close();  //Explicitly closing all the database resources versus relying on the JVM's garbage collection
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }


}