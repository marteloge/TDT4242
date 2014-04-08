/*
 * Created on Oct 27, 2004
 *
 */
package no.ntnu.fp.net.co;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 * Simplest possible test application, client part.
 *
 * @author seb, steinjak
 */
public class TestCoClient {

  /**
   * Empty.
   */
  public TestCoClient() {
	  kjor();
  }

  /**
   * Program Entry Point.
   */
  public static void main (String args[]){

    kjor();
  }

private static void kjor() {
	// Set up log
    Log.setLogName("Client");

    // Connection object listening on 4001
    Connection conn = new ConnectionImpl(4001, "localhost");
    InetAddress addr;  // will hold address of host to connect to
    try {
      // get address of local host and connect
      //addr = InetAddress.getByName("localhost");
      addr = IPTools.findPublicIPv4adr();
    	conn.connect(addr,5555);
      // send two messages to server
    for(int i=0;i<100;i++){ 
    	conn.send("En slags PaKKE nr "+i+"\n");
    }
      // write a message in the log and close the connection
      Log.writeToLog("Client is now closing the connection!",
		     "TestApplication");
     //String msg = conn.receive();
     //System.out.println(msg);
     conn.close();
    }

    catch (ConnectException e){
      Log.writeToLog(e.getMessage(),"TestApplication");
      e.printStackTrace();
    }
    catch (UnknownHostException e){
      Log.writeToLog(e.getMessage(),"TestApplication");
      e.printStackTrace();
    }
    catch (IOException e){
      Log.writeToLog(e.getMessage(),"TestApplication");
      e.printStackTrace();
    }

    System.out.println("CLIENT TEST FINISHED");
    Log.writeToLog("CLIENT TEST FINISHED","TestApplication");
}

}
