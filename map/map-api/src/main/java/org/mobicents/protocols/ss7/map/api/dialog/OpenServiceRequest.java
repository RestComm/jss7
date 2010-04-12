package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * ETS 300 974 - 5.4 Sequencing of services
 * 
 * The MAP-OPEN service is invoked before any user specific service-primitive is
 * accepted. The sequence may contain none, one or several user specific
 * service-primitives. If no user specific service-primitive is contained
 * between the MAP-OPEN and the MAP-DELIMITER primitives, then this will
 * correspond to sending an empty Begin message in TC. If more than one user
 * specific service-primitive is included, all are to be sent in the same Begin
 * message. The sequence ends with a MAP-DELIMITER primitive.
 * 
 * @author amit bhayani
 * 
 */
public interface OpenServiceRequest {

	public long[] getApplicationContextName();

	public void setApplicationContextName(long[] appCntxName);

	// Set this at SCCP level
	// public long getDestinationAddress();
	// public void setDestinationAddress(long destAddress);
}
