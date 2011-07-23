package org.mobicents.protocols.ss7.tools.traceparser;

import org.mobicents.protocols.ss7.tools.traceparser.TraceReaderListener;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface TraceReaderDriver {

	/**
	 * Add TraceListener
	 * 
	 * @param listener
	 */
	public void addTraceListener(TraceReaderListener listener);

	/**
	 * Remove TraceListener
	 * 
	 * @param listener
	 */
	public void removeTraceListener(TraceReaderListener listener);

	/**
	 * Open a trace file and start parsing 
	 * 
	 */
	public void startTraceFile() throws TraceReaderException;

	/**
	 * Stop trace file parsing
	 * 
	 */
	public void stop();

}
