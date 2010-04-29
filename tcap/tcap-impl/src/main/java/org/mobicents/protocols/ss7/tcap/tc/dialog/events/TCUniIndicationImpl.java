/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * 
 */
public class TCUniIndicationImpl extends DialogIndicationImpl implements TCUniIndication {
	private Byte qos;
	private SccpAddress originatingAddress, destinationAddress;

	// fields
	private ApplicationContextName applicationContextName;
	private UserInformation userInformation;

	TCUniIndicationImpl() {
		super(EventType.Uni);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * getApplicationContextName()
	 */
	public ApplicationContextName getApplicationContextName() {
		return applicationContextName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * getDestinationAddress()
	 */
	public SccpAddress getDestinationAddress() {

		return this.destinationAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * getOriginatingAddress()
	 */
	public SccpAddress getOriginatingAddress() {

		return this.originatingAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * getQOS()
	 */
	public Byte getQOS() {

		return this.qos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * getUserInformation()
	 */
	public UserInformation getUserInformation() {

		return this.userInformation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * setApplicationContextName
	 * (org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName)
	 */
	public void setApplicationContextName(ApplicationContextName acn) {
		this.applicationContextName = acn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * setDestinationAddress
	 * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public void setDestinationAddress(SccpAddress dest) {
		this.destinationAddress = dest;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * setOriginatingAddress
	 * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public void setOriginatingAddress(SccpAddress dest) {
		this.originatingAddress = dest;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * setQOS(java.lang.Byte)
	 */
	public void setQOS(Byte b) throws IllegalArgumentException {
		this.qos = b;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
	 * setUserInformation(org.mobicents.protocols.ss7.tcap.asn.UserInformation)
	 */
	public void setUserInformation(UserInformation acn) {
		this.userInformation = acn;

	}

}
