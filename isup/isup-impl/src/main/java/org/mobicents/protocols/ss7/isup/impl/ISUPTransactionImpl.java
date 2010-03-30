/**
 * Start time:12:57:29 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ISUPTransaction;
import org.mobicents.protocols.ss7.isup.TransactionID;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Start time:12:57:29 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public abstract class ISUPTransactionImpl implements ISUPTransaction {

	private static final AtomicLong txID = new AtomicLong(0);
	protected final TransactionID transactionID = new TransactionID(txID.incrementAndGet());

	protected ISUPMessage message;

	protected ISUPProviderImpl provider;
	protected ISUPStackImpl stack;

	protected Future generalTimeoutFuture;

	public ISUPTransactionImpl(ISUPMessage message, ISUPProviderImpl provider, ISUPStackImpl stack) {
		super();
		this.message = message;
		this.provider = provider;
		this.stack = stack;
		this.generalTimeoutFuture = stack.getExecutors().schedule(new ISUPTransactionTimeoutTask(this), stack.getTransactionGeneralTimeout(), TimeUnit.MILLISECONDS);
	}

	public ISUPProviderImpl getProvider() {
		return provider;
	}

	public void setProvider(ISUPProviderImpl provider) {
		this.provider = provider;
	}

	public ISUPStackImpl getStack() {
		return stack;
	}

	public void setStack(ISUPStackImpl stack) {
		this.stack = stack;
	}

	public void setMessage(ISUPMessage message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransaction#getMessage()
	 */
	public ISUPMessage getOriginalMessage() {
		return this.message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransaction#getTransactionID()
	 */
	public TransactionID getTransactionID() {
		return this.transactionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPTransaction#isServerTransaction()
	 */
	public boolean isServerTransaction() {
		return this instanceof ISUPServerTransaction;
	}

	protected void cancelGeneralTimer() {

		if (this.generalTimeoutFuture != null) {
			this.generalTimeoutFuture.cancel(false);
			this.generalTimeoutFuture = null;
		}
	}

	protected abstract void doGeneralTimeout();

	private class ISUPTransactionTimeoutTask implements Runnable {
		private ISUPTransactionImpl transaction;

		public ISUPTransactionTimeoutTask(ISUPTransactionImpl transaction) {
			super();
			this.transaction = transaction;
		}

		public void run() {

			transaction.doGeneralTimeout();
		}

	}

}
