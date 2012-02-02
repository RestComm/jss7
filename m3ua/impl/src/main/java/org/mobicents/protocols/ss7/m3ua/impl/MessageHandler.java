package org.mobicents.protocols.ss7.m3ua.impl;

import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Error;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class MessageHandler {

	protected AspFactory aspFactory = null;

	public MessageHandler(AspFactory aspFactory) {
		this.aspFactory = aspFactory;
	}

	protected void sendError(RoutingContext rc, ErrorCode errorCode) {
		Error error = (Error) this.aspFactory.messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.ERROR);
		error.setErrorCode(errorCode);
		if (rc != null) {
			error.setRoutingContext(rc);
		}
		this.aspFactory.write(error);
	}

	/**
	 * Get's the ASP for any ASP Traffic Maintenance, Management, Signalling
	 * Network Management and Transfer m3ua message's received which has null
	 * Routing Context
	 * 
	 * @return
	 */
	protected Asp getAspForNullRc() {
		// We know if null RC, ASP cannot be shared and AspFactory will
		// have only one ASP

		Asp asp = this.aspFactory.getAspList().get(0);

		if (this.aspFactory.getAspList().size() > 1) {
			// verify that AS to which this ASP is added is also having null
			// RC or this asp is not shared by any other AS in which case we
			// know messages are intended for same AS

			ErrorCode errorCodeObj = this.aspFactory.parameterFactory
					.createErrorCode(ErrorCode.Invalid_Routing_Context);
			sendError(null, errorCodeObj);
			return null;
		}

		return asp;
	}

	protected FSM getAspFSMForRxPayload(Asp asp) {
		FSM fsm = null;
		if (aspFactory.getFunctionality() == Functionality.AS
				|| (aspFactory.getFunctionality() == Functionality.SGW && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP
						&& aspFactory.getExchangeType() == ExchangeType.SE && aspFactory.getIpspType() == IPSPType.CLIENT)) {
			fsm = asp.getLocalFSM();

		} else {
			fsm = asp.getPeerFSM();
		}

		return fsm;
	}

}
