/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl.ud;



import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.ud.UDBase;

/**
 * Base for UD.
 * 
 * @author baranowb
 * 
 */
abstract class UDBaseImpl extends RoutingLabel implements UDBase {

	protected ProtocolClass pClass;
	protected SccpAddressImpl calledParty;
	protected SccpAddressImpl callingParty;
	protected byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setCalledParty(SccpAddress calledParty) {
		this.calledParty = (SccpAddressImpl) calledParty;
	}

	public void setCallingParty(SccpAddress callingParty) {
		this.callingParty = (SccpAddressImpl) callingParty;
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
