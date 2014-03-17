/*
 * Created on Oct 27, 2004
 *
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;



  

/**
 *
 * Implementation of the Connection-interface.
 * <br><br>
 * This class implements the behaviour in the methods specified in the
 * interface {@link Connection} over the unreliable, connectionless network
 * realised in {@link ClSocket}. The base class, {@link AbstractConnection}
 * implements some of the functionality, leaving message passing and error
 * handling to this implementation.
 *
 * @author Sebj�rn Birkeland and Stein Jakob Nordb�
 *
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {
	
	/**
	 * Static variable debug, used for debugging 
	 * and tracing. 
	 */
	public static boolean debug = true; 
	
	/** Keeps track of the used ports for each server port. */

	
	
  /** Keeps track of the used ports for each server port. */
	private static int lastServerPort = 50000;
	// private static Map usedPorts = Collections.synchronizedMap(new HashMap());

  /**
   * Initialise initial sequence number and setup state machine.
   *
   * JM� var her 21.04.08<br>
   *
   * @param myPort - the local port to associate with this connection
   */
  public ConnectionImpl(int myPort) {
	 super();
	 this.myPort = myPort;
	 try {
		//myAddress = InetAddress.getByName("localhost").getHostAddress();
		//myAddress = IPTools.findPublicIPv4();
		 myAddress = "localhost";
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  /**
   * 
   * Your port and destination IP
   * @param myPort
   * @param toAdress 
   */
  public ConnectionImpl(int myPort,String remoteAdress) {
		 super();
		 this.myPort = myPort;
		 this.remoteAddress = remoteAdress;
		 try {
				//myAddress = InetAddress.getByName("localhost").getHostAddress();
				myAddress = IPTools.findPublicIPv4();
			} catch (Exception e) {
				e.printStackTrace();
			}
	  }

  /**
   * Establish a connection to a remote location.
   * 
   * JM� var her 21.04.08<br>
   * Tror denne er ferdig...
   *
   * @param remoteAddress - the remote IP-address to connect to
   * @param remotePort - the remote portnumber to connect to
   *
   * @throws IOException If connection could not be made.
   *
   * @see AbstractConnection#sendPacket(KtnDatagram)
   */
  public void connect(InetAddress remoteAddress, int remotePort)
    throws IOException, SocketTimeoutException {
	if(state == AbstractConnection.CLOSED){
	  //this.remoteAddress = remoteAddress.getHostAddress();
		//this.remoteAddress ="129.241.206.197";
	  this.remotePort = remotePort;
	  KtnDatagram packet = constructPacket(KtnDatagram.SYN, null);
	  //Maa sette state for sendPacket, pga state brukes i doReceive ack
	  this.state = AbstractConnection.SYN_SENT;
	  //Faar tilbake en SYN_ACK pakke som forteller hvor vi skal utveksle data
	  packet = this.sendPacket(packet);
	  if(packet == null){
		  throw new SocketTimeoutException("F�r ikke syn_ack");
	  }
	  else{
		  this.state = AbstractConnection.ESTABLISHED;
		  //Klient lar datatilkobling gaar over samme port som til aa begynne med
		  //this.myPort = lastServerPort++;
		  packet.setDest_port(this.myPort);
		  this.sendAck(packet, KtnDatagram.ACK);
		  //Til denne porten skal data sende i ETABL. state
		  this.remotePort = packet.getSrc_port();
	  }
	}else if(state == AbstractConnection.SYN_SENT){
		KtnDatagram packet = receiveAck();
		if(packet!= null && packet.getFlags()==KtnDatagram.SYN_ACK){
			sendAck(packet, KtnDatagram.ACK);
			state = AbstractConnection.ESTABLISHED;
		}else{
			throw new IOException("F�r ikke SYN_ACK");
		}	
	}else{
		throw new IOException("Feil state");
	}
  }

  /**
   * Listen for, and accept, incoming connections.
   *
   * @return A new ConnectionImpl-object representing the new connection.
   */
  
  public Connection accept()
    throws IOException, SocketTimeoutException {
	if(state == AbstractConnection.CLOSED){
	  // -Lytte etter SYN via myPort
	  this.state = AbstractConnection.LISTEN;
	  KtnDatagram packet = this.doReceive(true);
	  
	  // -Svare med SYN_ACK. Sette orginalporten paa serverporten
	  if(packet != null && packet.getFlags()== KtnDatagram.SYN){
		  //Porten data skal lyttes mottas gjennom her.
		  int tmp = myPort;
		  myPort = lastServerPort;
		  this.state = AbstractConnection.SYN_RCVD;
		  this.sendAck(packet, KtnDatagram.SYN_ACK);
		  myPort = tmp;
	  }
	  else
		  throw new IOException();
	 
	  //Vente paa ACK - lag ny connection som lytter etter data paa nytt portnummer
	  ConnectionImpl conn = new ConnectionImpl(lastServerPort);//f.eks 50 000
	  //Oker porten vi onsker aa utveksle data med klienter på.
	  lastServerPort++;
	  conn.state = AbstractConnection.SYN_RCVD;
	  packet = conn.receiveAck();
	  if(packet == null){
		  //Hvis vi aldri mottar en ACK, kast SocketTimeoutException. 
		  throw new SocketTimeoutException();
	  }
	  else{
		  //Jippi, vi har en ACK, dvs data kan lyttes etter paa f.eks port 60 000++
		  conn.remoteAddress = packet.getSrc_addr();
	  	  conn.remotePort = packet.getSrc_port();
	  	  conn.state = AbstractConnection.ESTABLISHED;
	  }
	  return conn;
	}else if(state == AbstractConnection.CLOSED){
		throw new IOException("Er i CLOSED state");
	}else{
		throw new IOException("Feil state");
	}
  }

  /**
   * Send a message from the application.<br>
   *
   * @param msg - the String to be sent.
   *
   * @throws ConnectException If no connection could be made.
   * @throws IOException If no ACK was received.
   *
   * @see AbstractConnection#sendPacket(KtnDatagram)
   * @see no.ntnu.fp.net.co.Connection#send(String)
   */
  public void send(String msg) throws ConnectException, IOException {
	  if(state == ESTABLISHED){
	  KtnDatagram packet = this.constructPacket(-1, msg);
	  KtnDatagram ack = this.sendPacket(packet);
		  if( ack !=null && ack.getFlags() == KtnDatagram.ACK ){ 
			  System.out.println("\n ACK recieved on send, message is sent.");
		  }else{ 
			  throw new IOException("\n ACK is not recieved, send is not complete");
		  }
	  }
	  else{
			 throw new ConnectException("\n Connection not established");
	 }
  }

  /**
   * Wait for incoming data.
   *
   * @return The received data's payload as a String.
   *
   * @see ConnectionImpl#doReceive
   */
  public String receive() throws ConnectException, IOException {
	  if (state == ESTABLISHED) {			
			// Receving incoming packets, 

			try {
				KtnDatagram packet = doReceive(false);
				
				if (debug) System.out.println("\n  receive() - Packet received." + 

						" - Payload=" + packet.getPayload().toString() +

						" - Seq=" + packet.getSeq_nr()

						);
				return packet.getPayload().toString();
			}
			catch (EOFException e) {
				// Other side wants to disconnect
				System.out.println(" Disconnection request from other side");
				throw e;
				
			}

		} else {

			throw new ConnectException("Not connected!");
		}
    }
  

  /**
   * Close the connection.
   * 
   * Audun
   *
   * @see ConnectionImpl#doClose
   */
  public void close() throws IOException {
	  if(state == ESTABLISHED){
		  System.out.println(" Active Close - close() is called");
		  closeActive();
	  }
	  else if (state == CLOSE_WAIT){
		  System.out.println(" Passive Close - close() is called");
		  closePassive();
	  }
	  else{ throw new IOException("\n Error: Wrong State - Close()");}
  }
  
  public void closeActive() throws IOException{
	  if (state == ESTABLISHED) {
			KtnDatagram fin = constructPacket(KtnDatagram.FIN, null);
			state = FIN_WAIT_1;
			KtnDatagram ack = sendPacket(fin);

			if (ack.getFlags() == KtnDatagram.ACK) {
				state = FIN_WAIT_2;
				KtnDatagram ack_2 = doReceive(true);
				if (ack_2.getFlags() == KtnDatagram.FIN) {
					state = TIME_WAIT;
					sendAck(ack_2, KtnDatagram.ACK);
					state = CLOSED;
				} 
				else {
					state = ESTABLISHED;
					throw new IOException(" Did not receive  FIN nr 2 - unable to disconnect");
				}

			} 
			
			else {
				state = ESTABLISHED;
				throw new IOException("Did not receive ACK on FIN nr 1");
			}
	  }

}

		
/**
 * 
 * 
 * @throws IOException
 */
public void closePassive() throws IOException{

	  // Send FIN and ACK
	  if(state == CLOSE_WAIT){
	  
	  if(ackedFin){
	  state = LAST_ACK;	  
	  KtnDatagram fin = constructPacket(KtnDatagram.FIN,null);
	  KtnDatagram ack = sendPacket(fin);
	  if(ack.getFlags() == KtnDatagram.ACK){
		 /* Recieved last ACK - Disconnect complete */
		  state = CLOSED;
	  }
	  else {
		  // ACK not recieved - Disconnect incomplete
		  state = ESTABLISHED; 
		  throw new IOException ("Disconnect is incomplete, ACK not recived. (passive) ");
	  }
	  }
	  }
	  else { throw new IOException("Cannont disconnect, if not connected (passive)");}
}
  

  /**
   * Test a packet for transmission errors. This function is only called in
   * the ESTABLISHED state.
   * 
   * JM� gjort litt 21.04.08 ref feilh�ndtering.doc
   *
   * @param packet Packet to test.
   * @return true if packet is free of errors, false otherwise.
   */
  protected boolean isValid(KtnDatagram packet) {
		// 1. If null-values
	  	if (packet == null) {
	  		if(debug) System.out.println(" isValid: 348");
	  		return false;
		}
		// 2. Checking for ghost package
		else if (!packet.getSrc_addr().equals(remoteAddress) || packet.getSrc_port() != remotePort) {
		if(debug) {
			System.out.println("packet.getSrc_addr()"+packet.getSrc_addr());
			System.out.println("remoteAddress"+remoteAddress);
			System.out.println("packet.getSrc_port()"+packet.getSrc_port());
			System.out.println("remotePort"+remotePort);
			System.out.println("isValid: 341");
		}
			return false;
		}
		// 3. Errors in Checksum: 
		else if (packet.calculateChecksum() != packet.getChecksum()) {
			if(debug) System.out.println("isValid: 346");
			return false;
		}
		// 4. Error in sequence number:
		else if (lastPacketReceived != null && lastPacketReceived.getSeq_nr()+1 != packet.getSeq_nr()) {
			System.out.println("isValid: 351");
			return false;

		}
		return true;
  }
}
