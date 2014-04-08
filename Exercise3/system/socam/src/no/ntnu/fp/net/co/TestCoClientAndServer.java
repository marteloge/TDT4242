package no.ntnu.fp.net.co;

public class TestCoClientAndServer{

	/**
	 * Test both clases at once
	 */
	public static void main(String[] args) {
		( new Thread() {

			public void run() {
				new TestCoServer();
			}
		}
		).start();
		
		( new Thread() {
			public void run() {
				new TestCoClient();
			}
		}
		).start();
	}

	
}
