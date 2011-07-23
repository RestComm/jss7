package org.mobicents.protocols.ss7.tools.traceparser;

import java.util.ArrayList;
import org.apache.log4j.Logger;

public abstract class TraceReaderDriverBase implements TraceReaderDriver {
	
	protected Logger loger = Logger.getLogger(TraceReaderDriverBase.class);
	
	protected ArrayList<TraceReaderListener> listeners = new ArrayList<TraceReaderListener>();
	protected boolean isStarted = false;
	
	protected ProcessControl processControl;
	protected String fileName;

	
	public TraceReaderDriverBase(ProcessControl processControl, String fileName) {
		this.processControl = processControl;
		this.fileName = fileName;
	}
	

	@Override
	public void addTraceListener(TraceReaderListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeTraceListener(TraceReaderListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void stop() {
		this.isStarted = false;
	}

}
