package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

/**
 * 
 * @author amit bhayani
 *
 */
public class TransferMessageHandler extends MessageHandler {

	private static final Logger logger = Logger.getLogger(TransferMessageHandler.class);

	public TransferMessageHandler(AspFactory aspFactory) {
		super(aspFactory);
	}

	public void handlePayload(PayloadData payload) {
		RoutingContext rc = payload.getRoutingContext();

		if (rc == null) {
			Asp asp = this.getAspForNullRc();

			if (asp == null) {
				// Error condition
				logger.error(String
						.format("Rx : PayloadData=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
								payload, this.aspFactory.getName()));
				return;
			}

			FSM fsm = getAspFSMForRxPayload(asp);
			AspState aspState = AspState.getState(fsm.getState().getName());

			if (aspState == AspState.ACTIVE) {
				ProtocolData protocolData = payload.getData();
				Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(protocolData.getSI(),
						protocolData.getNI(), protocolData.getMP(), protocolData.getOpc(), protocolData.getDpc(),
						protocolData.getSLS(), protocolData.getData());
				asp.getAs().getM3UAManagement()
						.sendTransferMessageToLocalUser(mtp3TransferPrimitive, payload.getData().getSLS());
			} else {
				logger.error(String.format(
						"Rx : PayloadData for Aspfactory=%s for RoutingContext=%d. But ASP State=%s. Message=%s", rc,
						this.aspFactory.getName(), aspState, payload));
			}

		} else {
			// Payload is always for single AS
			long rcl = payload.getRoutingContext().getRoutingContexts()[0];
			Asp asp = this.aspFactory.getAsp(rcl);

			if (asp == null) {
				// this is error. Send back error
				RoutingContext rcObj = this.aspFactory.parameterFactory.createRoutingContext(new long[] { rcl });
				ErrorCode errorCodeObj = this.aspFactory.parameterFactory
						.createErrorCode(ErrorCode.Invalid_Routing_Context);
				sendError(rcObj, errorCodeObj);
				logger.error(String
						.format("Rx : Payload=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
								payload, rcl, this.aspFactory.getName()));
				return;
			}

			FSM fsm = getAspFSMForRxPayload(asp);
			AspState aspState = AspState.getState(fsm.getState().getName());

			if (aspState == AspState.ACTIVE) {
				ProtocolData protocolData = payload.getData();
				Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(protocolData.getSI(),
						protocolData.getNI(), protocolData.getMP(), protocolData.getOpc(), protocolData.getDpc(),
						protocolData.getSLS(), protocolData.getData());
				asp.getAs().getM3UAManagement()
						.sendTransferMessageToLocalUser(mtp3TransferPrimitive, payload.getData().getSLS());
			} else {
				logger.error(String.format(
						"Rx : PayloadData for Aspfactory=%s for RoutingContext=%d. But ASP State=%s. Message=%s",
						this.aspFactory.getName(), rc, aspState, payload));
			}
		}
	}
}
