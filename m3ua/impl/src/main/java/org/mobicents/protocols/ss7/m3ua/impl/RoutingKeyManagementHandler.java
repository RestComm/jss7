package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationRequest;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationResponse;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationRequest;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class RoutingKeyManagementHandler extends MessageHandler {
	private static final Logger logger = Logger.getLogger(RoutingKeyManagementHandler.class);

	public RoutingKeyManagementHandler(AspFactory aspFactory) {
		super(aspFactory);
	}

	public void handleRegistrationRequest(RegistrationRequest registrationRequest) {
		logger.error(String.format("Received REGREQ=%s. Handling of RKM message is not supported", registrationRequest));

	}

	public void handleRegistrationResponse(RegistrationResponse registrationResponse) {
		logger.error(String
				.format("Received REGRES=%s. Handling of RKM message is not supported", registrationResponse));
	}

	public void handleDeregistrationRequest(DeregistrationRequest deregistrationRequest) {
		logger.error(String.format("Received DEREGREQ=%s. Handling of RKM message is not supported",
				deregistrationRequest));
	}

	public void handleDeregistrationResponse(DeregistrationResponse deregistrationResponse) {
		logger.error(String.format("Received DEREGRES=%s. Handling of RKM message is not supported",
				deregistrationResponse));
	}

}
