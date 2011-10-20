package org.mobicents.ss7.hardware.dialogic;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

public class DialogicTest {
	
	private static final Logger logger = Logger.getLogger(DialogicMtp3UserPart.class);
	
	// TODO: set the correct values of sourceModuleId & destinationModuleId
	private int sourceModuleId = 0;
	private int destinationModuleId = 0;

	private InterProcessCommunicator ipc = new InterProcessCommunicator(sourceModuleId, destinationModuleId);
	
	public void BoardTest() throws Exception {
		
		// starting two threads for listening
		MtpProcessReading p1 = new MtpProcessReading(1);
		MtpProcessReading p2 = new MtpProcessReading(2);
		Thread t1 = new Thread(p1); 
		Thread t2 = new Thread(p2);
		t1.start();
		t2.start();
		
		Thread.sleep(5000);
		
		MtpProcessWriting p3 = new MtpProcessWriting(1);
		MtpProcessWriting p4 = new MtpProcessWriting(2);
		Thread t3 = new Thread(p3); 
		Thread t4 = new Thread(p4);
		t3.start();
		t4.start();
		
		// working for 600 sec (10 min)
		Thread.sleep(600000);
	
	}
	
	private class MtpProcessReading implements Runnable {
		
		int num;
		
		public MtpProcessReading(int num) {
			this.num = num;
		}

		@Override
		public void run() {
			while( true ) {
				// reading the message
				byte[] buf = ipc.read();
				
				// logging the result
				StringBuilder sb = new StringBuilder();
				sb.append("Data read: Num=");
				sb.append(num);
				sb.append(": ");
				if (buf == null)
					sb.append("null");
				else {
					sb.append("length=");
					sb.append(buf.length);
				}

				logger.error(sb.toString());
			}
		}
	}
	
	private class MtpProcessWriting implements Runnable {
		
		int num;
		
		public MtpProcessWriting(int num) {
			this.num = num;
		}

		@Override
		public void run() {
			while( true ) {
				// writing the message
				
				// TODO: fill fields with correct values !!!
				int si = 3;
				int ni = 2;
				int mp = 0;
				int opc = 1;
				int dpc = 2;
				int sls = 0;
				byte[] data = new byte[10];
				Mtp3TransferPrimitive msg = new Mtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data); 
				byte[] buf = msg.encodeMtp3();
				try {
					ipc.write(buf);
					logger.error("Data written: Num=" + num);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Error data writing: Num=" + num);
					e.printStackTrace();
				}
				
				try {
					// we are sending a message per a second
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

