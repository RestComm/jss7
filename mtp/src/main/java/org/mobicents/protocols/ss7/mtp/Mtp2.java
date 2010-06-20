/**
 * 
 */
package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;

/**
 * @author baranowb
 * 
 */
public interface Mtp2 {

	final static int STATE_MTP2_OUT_OF_SERVICE = 0;
	/**
	 * Initial state of IAC phase, we send "O" here. "E","O","N" have never been
	 * received.
	 */
	final static int STATE_MTP2_NOT_ALIGNED = 1;
	/**
	 * Second state of IAC, we received one of: E,O,N. Depending on state we
	 * send E or N.
	 */
	final static int STATE_MTP2_ALIGNED = 2;
	/**
	 * Third state, entered from ALIGNED on receival of N or E
	 */
	final static int STATE_MTP2_PROVING = 3;
	/**
	 * Etnered on certain condition when T4 fires.
	 */
	final static int STATE_MTP2_ALIGNED_READY = 4;
	/**
	 * In service state, entered from ALIGNED_READY on FISU/MSU
	 */
	final static int STATE_MTP2_INSERVICE = 5;

	public boolean isEmergency();

	/**
	 * Sets emergency mode for alignment
	 * 
	 * @param emergency
	 */
	public void setEmergency(boolean emergency);

	/**
	 * Fetches state information. It returns on of values:
	 * <ul>
	 * <li>{@link #STATE_MTP2_OUT_OF_SERVICE}</li>
	 * <li>{@link #STATE_MTP2_NOT_ALIGNED}</li>
	 * <li>{@link #STATE_MTP2_ALIGNED}</li>
	 * <li>{@link #STATE_MTP2_PROVING}</li>
	 * <li>{@link #STATE_MTP2_ALIGNED_READY}</li>
	 * <li>{@link #STATE_MTP2_INSERVICE}</li>
	 * 
	 * </ul>
	 * 
	 * @return
	 */
	public int getState();

	public void setLayer1(Mtp1 layer1);

	public Mtp1 getLayer1();

	public void addMtp2Listener(Mtp2Listener lst);

	public void removeMtp2Listener(Mtp2Listener lst);

	public void setName(String name);
	
	public String getName();
	
	public void setSls(int sls);
	
	public int getSls();
	
	public void start() throws IOException;

	public void stop();

	public void fail();

	public boolean send(byte[] msg) throws IOException;

	public void doRead();

	public void doWrite();
	
	public void trace(String s);
	
	public void setSLTMTest(SLTMTest test);
	
	public SLTMTest getSLTMTest();
}
