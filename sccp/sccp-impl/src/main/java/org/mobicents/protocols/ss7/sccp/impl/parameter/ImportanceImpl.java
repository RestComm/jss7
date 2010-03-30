/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.OptionalParameter;

/**
 * @author baranowb
 * 
 */
public class ImportanceImpl extends OptionalParameter implements Importance {

	
	// default is lowest priority :)
	private byte importance = 0;

	/**
	 * 
	 */
	public ImportanceImpl() {
		// TODO Auto-generated constructor stub
	}

	public ImportanceImpl(byte importance) {
		super();
		this.importance = (byte) (importance & 0x07);
	}

	public byte getImportance() {
		return importance;
	}

	public void setImportance(byte importance) {
		this.importance = (byte) (importance & 0x07);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.OptionalParameter#decode(byte[])
	 */
	@Override
	public void decode(byte[] buffer) throws IOException {
		this.importance = (byte) (buffer[0] & 0x07);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.OptionalParameter#encode()
	 */
	@Override
	public byte[] encode() throws IOException {
		// TODO Auto-generated method stub
		return new byte[] { (byte) (importance & 0x07) };
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + importance;
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
		ImportanceImpl other = (ImportanceImpl) obj;
		if (importance != other.importance)
			return false;
		return true;
	}

	
}
