package org.mobicents.protocols.ss7.tools.traceparser;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface ProcessControl {

	public boolean isFinished();

	public String getErrorMessage();

	public void interrupt();
	
	public boolean checkNeedInterrupt();

	public int getMsgCount();

}
