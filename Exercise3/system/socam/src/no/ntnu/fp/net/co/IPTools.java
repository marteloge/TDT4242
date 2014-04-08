package no.ntnu.fp.net.co;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Class for misc IP-related tools
 * @author Eirik Haver
 * 
 */
public class IPTools {

	/**
	 * Method for deciding the hosts external ipv6-address
	 * @return The hosts external ipv4-address (as a String)
	 */
	public static String findPublicIPv4() {
		try {
			String myAddress = null;
			
			// Find all network interfaces
			Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
			while (ifaces.hasMoreElements()) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Investigate addresses of interface
				Enumeration addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = (InetAddress) addresses.nextElement();
					
					// Choose any non-local address
					if (!address.isLoopbackAddress() && address.toString().length() < 17 && !address.toString().equals("/127.0.0.1") && !address.toString().startsWith("/10.")) {
						
						myAddress = address.getHostAddress();
						break;
					}
				}
				if (myAddress != null)
					break;
			}
			// Last resort: Use loopback

			if (myAddress == null)
				myAddress = InetAddress.getByName("localhost").getHostAddress();

			return myAddress;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * Method for deciding the hosts external ipv6-address
	 * @return The hosts external ipv4-address (as an InetAddress)
	 */
	
	public static InetAddress findPublicIPv4adr() {
		try {
			InetAddress myAddress = null;
			
			// Find all network interfaces
			Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
			while (ifaces.hasMoreElements()) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Investigate addresses of interface
				Enumeration addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = (InetAddress) addresses.nextElement();
					
					// Choose any non-local address
					if (!address.isLoopbackAddress() && address.toString().length() < 17 && !address.toString().equals("/127.0.0.1") && !address.toString().startsWith("/10.")) {
						myAddress = address;
						break;
					}
				}
				if (myAddress != null)
					break;
			}
			// Last resort: Use loopback

			if (myAddress == null)
				myAddress = InetAddress.getByName("localhost");

			return myAddress;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
}
