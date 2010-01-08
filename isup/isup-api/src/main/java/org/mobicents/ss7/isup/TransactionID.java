/**
 * Start time:08:56:23 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup;

/**
 * Start time:08:56:23 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class TransactionID implements Comparable<TransactionID> {

	// add more?

	private long transactionID;

	public TransactionID(long transactionID) {
		super();
		this.transactionID = transactionID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (transactionID ^ (transactionID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionID other = (TransactionID) obj;
		if (transactionID != other.transactionID)
			return false;
		return true;
	}

	public int compareTo(TransactionID o) {
		if (o == null)
			return 1;
		if (o == this)
			return 0;
		long l = this.transactionID - o.transactionID;
		if (l > 0)
			return 1;
		if (l < 0)
			return -1;
		return 0;
	}

}
