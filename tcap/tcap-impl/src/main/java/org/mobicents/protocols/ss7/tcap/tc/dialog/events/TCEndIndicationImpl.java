/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * 
 */
public class TCEndIndicationImpl extends DialogIndicationImpl implements TCEndIndication {

	private Byte qos;
	private TerminationType terminationType;

	// fields
	private ApplicationContextName applicationContextName;
	private UserInformation userInformation;

	TCEndIndicationImpl() {
		super();
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
