package no.ntnu.fp.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertTestDataGarageDb {
	
	public static void main(String[] arg)
	  {
	    try {
	      // Laster inn Derbys Embedded JDBC-driver. Kan kaste ClassNotFoundException
	      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

	      // Kobler opp mot databasen
	      Connection c = DriverManager.getConnection("jdbc:derby:GarageDB;");

	      // Lager et statement
	      Statement s = c.createStatement();
	      
	      s.execute("DELETE FROM customer");
	      
	      String q1 = "INSERT INTO customer VALUES(1,'Arne','mail1@mail.no'," +
	      		"'gate1','by1',1)";
	      s.execute(q1);
	      String q2 = "INSERT INTO customer VALUES(2,'Bernt','mail2@mail.no'," +
    		"'gate2','by2',2)";
	      s.execute(q2);
	      String q3 = "INSERT INTO customer VALUES(3,'Daniel','mail3@mail.no'," +
    		"'gate3','by3',3)";
	      s.execute(q3);
	      String q4 = "INSERT INTO customer VALUES(4,'Erik','mail4@mail.no'," +
    		"'gate4','by4',4)";
	      s.execute(q4);
	      String q5 = "INSERT INTO customer VALUES(5,'Frank','mail5@mail.no'," +
    		"'gate5','by5',5)";
	      s.execute(q5);
	      
	      c.commit();

	      c.close();
	      s.close();
	    } catch (ClassNotFoundException e) {
	      System.err.println("S�rg for at derby.jar er i classpathen.\n" +
	                         "Du kan hente dem fra http://www.idi.ntnu.no/emner/tdt4145/programvare/javadb.html");
	    } catch (ArrayIndexOutOfBoundsException e) {
	      System.err.println("Oppgi databasebrukernavn og -passord som argumenter");
	    } catch (SQLException e) {
	      System.err.println("Det ble noe SQL-tr�bbel; n�rmere bestemt " + e);
	    }
	   
	  }

}
