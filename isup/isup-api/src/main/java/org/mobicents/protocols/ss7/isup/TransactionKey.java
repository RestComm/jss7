/**
 * Start time:12:01:03 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

/**
 * Start time:12:01:03 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * Class to hide more about transaction.
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class TransactionKey{

	
	private String description;
	private long cic;
	
	public TransactionKey(String description, long cic) {
		super();
		this.description = description;
		this.cic = cic;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the cic
	 */
	public long getCic() {
		return cic;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cic ^ (cic >>> 32));
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionKey other = (TransactionKey) obj;
		if (cic != other.cic)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactionKey [cic=" + cic + ", description=" + description + "]";
	}
	

}
