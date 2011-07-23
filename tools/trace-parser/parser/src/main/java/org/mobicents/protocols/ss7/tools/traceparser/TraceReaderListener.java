package org.mobicents.protocols.ss7.tools.traceparser;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface TraceReaderListener {

	/**
	 * Deliver the ss7 message data.
	 * Data is a raw data that includes BSN+Bib + FSN+Fib + LI + SIO + SIF
	 * 
	 * @param data
	 */
	public void ss7Message(byte[] data) throws TraceReaderException;

}
