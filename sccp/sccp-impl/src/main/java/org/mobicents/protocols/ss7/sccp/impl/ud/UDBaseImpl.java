/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl.ud;



import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.ud.UDBase;

/**
 * Base for UD.
 * 
 * @author baranowb
 * 
 */
abstract class UDBaseImpl implements UDBase {

	protected ProtocolClass pClass;
	protected SccpAddress calledParty;
	protected SccpAddress callingParty;
	protected byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setCalledParty(SccpAddress calledParty) {
		this.calledParty = calledParty;
	}

	public void setCallingParty(SccpAddress callingParty) {
		this.callingParty = callingParty;
	}

	public SccpAddress getCalledParty() {
		return calledParty;
	}

	public SccpAddress getCallingParty() {
		return callingParty;
	}
	public ProtocolClass getpClass() {
		return pClass;
	}

	public void setpClass(ProtocolClass pClass) {
		this.pClass = pClass;
	}

}
