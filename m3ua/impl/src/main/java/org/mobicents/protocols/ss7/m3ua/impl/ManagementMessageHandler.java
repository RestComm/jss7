package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Error;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * 
 * @author amit bhayani
 *
 */
public class ManagementMessageHandler extends MessageHandler {

	private static final Logger logger = Logger.getLogger(ManagementMessageHandler.class);

	public ManagementMessageHandler(AspFactory aspFactory) {
		super(aspFactory);
	}

	public void handleNotify(Notify notify) {

		RoutingContext rc = notify.getRoutingContext();

		if (aspFactory.getFunctionality() == Functionality.AS
				|| (aspFactory.getFunctionality() == Functionality.SGW && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP
						&& aspFactory.getExchangeType() == ExchangeType.SE && aspFactory.getIpspType() == IPSPType.CLIENT)) {

			if (rc == null) {

				Asp asp = this.getAspForNullRc();
				if (asp == null) {
					logger.error(String
							.format("Rx : NTFY=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
									notify, this.aspFactory.getName()));
					return;
				}

				try {
					// Received NTFY, so peer FSM has to be used.
					FSM fsm = asp.getAs().getPeerFSM();

					if (fsm == null) {
						logger.error(String.format("Received NTFY=%s for ASP=%s. But Peer FSM is null.", notify,
								this.aspFactory.getName()));
						return;
					}
					fsm.setAttribute(As.ATTRIBUTE_ASP, asp);
					fsm.signal(TransitionState.getTransition(notify));
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				long[] rcs = notify.getRoutingContext().getRoutingContexts();
				for (int count = 0; count < rcs.length; count++) {
					Asp asp = this.aspFactory.getAsp(rcs[count]);

					if (asp == null) {
						// this is error. Send back error
						RoutingContext rcObj = this.aspFactory.parameterFactory
								.createRoutingContext(new long[] { rcs[count] });
						ErrorCode errorCodeObj = this.aspFactory.parameterFactory
								.createErrorCode(ErrorCode.Invalid_Routing_Context);
						sendError(rcObj, errorCodeObj);
						logger.error(String
								.format("Rx : NTFY=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
										notify, rcs[count], this.aspFactory.getName()));
						continue;
					}

					try {
						// Received NTFY, so peer FSM has to be set.
						FSM fsm = asp.getAs().getPeerFSM();

						if (fsm == null) {
							logger.error(String.format("Received NTFY=%s for ASP=%s. But Peer FSM is null.", notify,
									this.aspFactory.getName()));
							return;
						}
						fsm.setAttribute(As.ATTRIBUTE_ASP, asp);
						fsm.signal(TransitionState.getTransition(notify));
					} catch (UnknownTransitionException e) {
						logger.error(e.getMessage(), e);
					}
				}// end of for
			}// if (rc == null) {
		} else {
			// NTFY is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(rc, errorCodeObj);
		}

	}

	public void handleError(Error error) {
		logger.error(error);
	}

}
