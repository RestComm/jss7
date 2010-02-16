/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.ss7.sctp.MTPProvider;

/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPStackImpl implements ISUPStack {

	private MTPProvider transportProvider;
	private ISUPProviderImpl isupProvider;
	

	private boolean started = false;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(8);
	
	
	private long _GENERAL_TRANSACTION_TIMEOUT=60*1000;
	private long _CLIENT_TRANSACTION_ANSWER_TIMEOUT=30*1000;
	

	public ISUPStackImpl(MTPProvider transportProvider) {
		super();
		this.transportProvider = transportProvider;
		
		this.isupProvider = new ISUPProviderImpl(this.transportProvider, this);

	}


	public ISUPProvider getIsupProvider() {
		return isupProvider;
	}

	public void start() {
		if (!started) {
			configure();
			this.transportProvider.addMtpListener(this.isupProvider);
			this.started = true;
		}
	}

	public void stop() {
		if (started) {
			this.transportProvider.removeMtpListener(this.isupProvider);
			this.transportProvider.close();
			terminate();
			this.started = false;

		}
	}

	// ///////////////
	// CONF METHOD //
	// ///////////////
	/**
	 * 
	 */
	private void configure() {
		this.executor = Executors.newScheduledThreadPool(8);

	}

	/**
	 * 
	 */
	private void terminate() {
		this.executor.shutdownNow();
	}
	//possibly something similar as in MGCP
	ScheduledExecutorService getExecutors() {
		return this.executor;
	}

	/**
	 * @return
	 */
	public long getTransactionGeneralTimeout() {
		return _GENERAL_TRANSACTION_TIMEOUT;
	}

	/**
	 * @return
	 */
	public long getClientTransactionAnswerTimeout() {
		return _CLIENT_TRANSACTION_ANSWER_TIMEOUT;
	}
}
