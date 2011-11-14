package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationAvailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationRestricted;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationStateAudit;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUPUnavailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.SignallingCongestion;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;

/**
 * 
 * @author amit bhayani
 *
 */
public class SignalingNetworkManagementHandler extends MessageHandler {

	private static final Logger logger = Logger.getLogger(SignalingNetworkManagementHandler.class);

	public SignalingNetworkManagementHandler(AspFactory aspFactory) {
		super(aspFactory);
	}

	public void handleDestinationUnavailable(DestinationUnavailable duna) {

		RoutingContext rcObj = duna.getRoutingContexts();

		if (aspFactory.getFunctionality() == Functionality.AS) {

			if (rcObj == null) {
				Asp asp = this.getAspForNullRc();
				if (asp == null) {
					ErrorCode errorCodeObj = this.aspFactory.parameterFactory
							.createErrorCode(ErrorCode.Invalid_Routing_Context);
					sendError(rcObj, errorCodeObj);
					logger.error(String
							.format("Rx : DUNA=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
									duna, this.aspFactory.getName()));
					return;
				}

				FSM fsm = asp.getLocalFSM();

				if (fsm == null) {
					logger.error(String.format("Rx : DUNA=%s for ASP=%s. But Local FSM is null.", duna,
							this.aspFactory.getName()));
					return;
				}

				AspState aspState = AspState.getState(fsm.getState().getName());

				if (aspState == AspState.ACTIVE) {
					AffectedPointCode affectedPcObjs = duna.getAffectedPointCodes();
					int[] affectedPcs = affectedPcObjs.getPointCodes();

					for (int i = 0; i < affectedPcs.length; i++) {
						Mtp3PausePrimitive mtpPausePrimi = new Mtp3PausePrimitive(affectedPcs[i]);
						asp.getAs().getM3UAManagement().sendPauseMessageToLocalUser(mtpPausePrimi);
					}
				} else {
					logger.error(String.format("Rx : DUNA for null RoutingContext. But ASP State=%s. Message=%s",
							aspState, duna));
				}

			} else {
				long[] rcs = rcObj.getRoutingContexts();
				for (int count = 0; count < rcs.length; count++) {
					Asp asp = this.aspFactory.getAsp(rcs[count]);
					if (asp == null) {
						// this is error. Send back error
						RoutingContext rcObjTemp = this.aspFactory.parameterFactory
								.createRoutingContext(new long[] { rcs[count] });
						ErrorCode errorCodeObj = this.aspFactory.parameterFactory
								.createErrorCode(ErrorCode.Invalid_Routing_Context);
						sendError(rcObjTemp, errorCodeObj);
						logger.error(String
								.format("Rx : DUNA=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
										duna, rcs[count], this.aspFactory.getName()));
						continue;
					}// if (asp == null)

					FSM fsm = asp.getLocalFSM();

					if (fsm == null) {
						logger.error(String.format("Rx : DUNA=%s for ASP=%s. But Local FSM is null.", duna,
								this.aspFactory.getName()));
						return;
					}

					AspState aspState = AspState.getState(fsm.getState().getName());

					if (aspState == AspState.ACTIVE) {
						AffectedPointCode affectedPcObjs = duna.getAffectedPointCodes();
						int[] affectedPcs = affectedPcObjs.getPointCodes();

						for (int i = 0; i < affectedPcs.length; i++) {
							Mtp3PausePrimitive mtpPausePrimi = new Mtp3PausePrimitive(affectedPcs[i]);
							asp.getAs().getM3UAManagement().sendPauseMessageToLocalUser(mtpPausePrimi);
						}
					} else {
						logger.error(String.format("Rx : DUNA for RoutingContext=%d. But ASP State=%s. Message=%s",
								rcs[count], aspState, duna));
					}
				}// for loop
			}

		} else {
			// TODO : Should we silently drop DUNA?

			// ASPACTIVE_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(rcObj, errorCodeObj);
		}
	}

	public void handleDestinationAvailable(DestinationAvailable dava) {
		RoutingContext rcObj = dava.getRoutingContexts();

		if (aspFactory.getFunctionality() == Functionality.AS) {

			if (rcObj == null) {
				Asp asp = this.getAspForNullRc();
				if (asp == null) {
					ErrorCode errorCodeObj = this.aspFactory.parameterFactory
							.createErrorCode(ErrorCode.Invalid_Routing_Context);
					sendError(rcObj, errorCodeObj);
					logger.error(String
							.format("Rx : DAVA=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
									dava, this.aspFactory.getName()));
					return;
				}

				FSM fsm = asp.getLocalFSM();

				if (fsm == null) {
					logger.error(String.format("Rx : DAVA=%s for ASP=%s. But Local FSM is null.", dava,
							this.aspFactory.getName()));
					return;
				}

				AspState aspState = AspState.getState(fsm.getState().getName());

				if (aspState == AspState.ACTIVE) {
					AffectedPointCode affectedPcObjs = dava.getAffectedPointCodes();
					int[] affectedPcs = affectedPcObjs.getPointCodes();

					for (int i = 0; i < affectedPcs.length; i++) {
						Mtp3ResumePrimitive mtpResumePrimi = new Mtp3ResumePrimitive(affectedPcs[i]);
						asp.getAs().getM3UAManagement().sendResumeMessageToLocalUser(mtpResumePrimi);
					}
				} else {
					logger.error(String.format("Rx : DAVA for null RoutingContext. But ASP State=%s. Message=%s",
							aspState, dava));
				}

			} else {
				long[] rcs = rcObj.getRoutingContexts();
				for (int count = 0; count < rcs.length; count++) {
					Asp asp = this.aspFactory.getAsp(rcs[count]);
					if (asp == null) {
						// this is error. Send back error
						RoutingContext rcObjTemp = this.aspFactory.parameterFactory
								.createRoutingContext(new long[] { rcs[count] });
						ErrorCode errorCodeObj = this.aspFactory.parameterFactory
								.createErrorCode(ErrorCode.Invalid_Routing_Context);
						sendError(rcObjTemp, errorCodeObj);
						logger.error(String
								.format("Rx : DAVA=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
										dava, rcs[count], this.aspFactory.getName()));
						continue;
					}// if (asp == null)

					FSM fsm = asp.getLocalFSM();

					if (fsm == null) {
						logger.error(String.format("Rx : DAVA=%s for ASP=%s. But Local FSM is null", dava,
								this.aspFactory.getName()));
						return;
					}

					AspState aspState = AspState.getState(fsm.getState().getName());

					if (aspState == AspState.ACTIVE) {
						AffectedPointCode affectedPcObjs = dava.getAffectedPointCodes();
						int[] affectedPcs = affectedPcObjs.getPointCodes();

						for (int i = 0; i < affectedPcs.length; i++) {
							Mtp3PausePrimitive mtpPausePrimi = new Mtp3PausePrimitive(affectedPcs[i]);
							asp.getAs().getM3UAManagement().sendPauseMessageToLocalUser(mtpPausePrimi);
						}
					} else {
						logger.error(String.format("Rx : DAVA for RoutingContext=%d. But ASP State=%s. Message=%s",
								rcs[count], aspState, dava));
					}
				}// for loop
			}

		} else {
			// TODO : Should we silently drop DUNA?

			// ASPACTIVE_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(rcObj, errorCodeObj);
		}
	}

	public void handleDestinationStateAudit(DestinationStateAudit daud) {
		RoutingContext rcObj = daud.getRoutingContexts();
		if (aspFactory.getFunctionality() == Functionality.SGW) {
			logger.warn(String.format("Received DAUD=%s. Handling of DAUD message is not yet implemented", daud));
		} else {
			// TODO : Should we silently drop DUNA?

			// ASPACTIVE_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(rcObj, errorCodeObj);
		}
	}

	public void handleSignallingCongestion(SignallingCongestion scon) {
		RoutingContext rcObj = scon.getRoutingContexts();
		if (aspFactory.getFunctionality() == Functionality.AS || aspFactory.getFunctionality() == Functionality.IPSP) {
			if (rcObj == null) {
				Asp asp = this.getAspForNullRc();
				if (asp == null) {
					ErrorCode errorCodeObj = this.aspFactory.parameterFactory
							.createErrorCode(ErrorCode.Invalid_Routing_Context);
					sendError(rcObj, errorCodeObj);
					logger.error(String
							.format("Rx : SCON=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
									scon, this.aspFactory.getName()));
					return;
				}

				FSM fsm = asp.getLocalFSM();

				if (fsm == null) {
					logger.error(String.format("Rx : SCON=%s for ASP=%s. But Local FSM is null.", scon,
							this.aspFactory.getName()));
					return;
				}

				AspState aspState = AspState.getState(fsm.getState().getName());

				if (aspState == AspState.ACTIVE) {
					AffectedPointCode affectedPcObjs = scon.getAffectedPointCodes();
					int[] affectedPcs = affectedPcObjs.getPointCodes();

					int cong = 0;
					for (int i = 0; i < affectedPcs.length; i++) {
						CongestedIndication congeInd = scon.getCongestedIndication();
						if (congeInd != null) {
							CongestionLevel congLevel = congeInd.getCongestionLevel();
							if (congLevel != null) {
								cong = congLevel.getLevel();
							}
						}

						Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
								Mtp3StatusCause.SignallingNetworkCongested, cong);
						asp.getAs().getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
					}
				} else {
					logger.error(String.format("Rx : SCON for null RoutingContext. But ASP State=%s. Message=%s",
							aspState, scon));
				}
			} else {
				long[] rcs = rcObj.getRoutingContexts();
				for (int count = 0; count < rcs.length; count++) {
					Asp asp = this.aspFactory.getAsp(rcs[count]);
					if (asp == null) {
						// this is error. Send back error
						RoutingContext rcObjTemp = this.aspFactory.parameterFactory
								.createRoutingContext(new long[] { rcs[count] });
						ErrorCode errorCodeObj = this.aspFactory.parameterFactory
								.createErrorCode(ErrorCode.Invalid_Routing_Context);
						sendError(rcObjTemp, errorCodeObj);
						logger.error(String
								.format("Rx : SCON=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
										scon, rcs[count], this.aspFactory.getName()));
						continue;
					}// if (asp == null)

					FSM fsm = asp.getLocalFSM();

					if (fsm == null) {
						logger.error(String.format("Rx : SCON=%s for ASP=%s. But Local FSM is null", scon,
								this.aspFactory.getName()));
						return;
					}

					AspState aspState = AspState.getState(fsm.getState().getName());

					if (aspState == AspState.ACTIVE) {
						AffectedPointCode affectedPcObjs = scon.getAffectedPointCodes();
						int[] affectedPcs = affectedPcObjs.getPointCodes();

						int cong = 0;
						for (int i = 0; i < affectedPcs.length; i++) {
							CongestedIndication congeInd = scon.getCongestedIndication();
							if (congeInd != null) {
								CongestionLevel congLevel = congeInd.getCongestionLevel();
								if (congLevel != null) {
									cong = congLevel.getLevel();
								}
							}

							Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
									Mtp3StatusCause.SignallingNetworkCongested, cong);
							asp.getAs().getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
						}
					} else {
						logger.error(String.format("Rx : DAVA for RoutingContext=%d. But ASP State=%s. Message=%s",
								rcs[count], aspState, scon));
					}
				}// for loop
			}

		} else {
			// TODO : Should we silently drop DUNA?

			// SCON is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(rcObj, errorCodeObj);
		}
	}

	public void handleDestinationUPUnavailable(DestinationUPUnavailable dupu) {
		RoutingContext rcObj = dupu.getRoutingContext();

		if (aspFactory.getFunctionality() == Functionality.AS) {

			if (rcObj == null) {
				Asp asp = this.getAspForNullRc();
				if (asp == null) {
					ErrorCode errorCodeObj = this.aspFactory.parameterFactory
							.createErrorCode(ErrorCode.Invalid_Routing_Context);
					sendError(rcObj, errorCodeObj);
					logger.error(String
							.format("Rx : DUPU=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
									dupu, this.aspFactory.getName()));
					return;
				}

				FSM fsm = asp.getLocalFSM();

				if (fsm == null) {
					logger.error(String.format("Rx : DUPU=%s for ASP=%s. But Local FSM is null.", dupu,
							this.aspFactory.getName()));
					return;
				}

				AspState aspState = AspState.getState(fsm.getState().getName());

				if (aspState == AspState.ACTIVE) {
					AffectedPointCode affectedPcObjs = dupu.getAffectedPointCode();
					int[] affectedPcs = affectedPcObjs.getPointCodes();

					int cause = 0;
					for (int i = 0; i < affectedPcs.length; i++) {

						UserCause userCause = dupu.getUserCause();
						cause = userCause.getCause();
						Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
								Mtp3StatusCause.getMtp3StatusCause(cause), 0);
						asp.getAs().getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
					}
				} else {
					logger.error(String.format("Rx : DUPU for null RoutingContext. But ASP State=%s. Message=%s",
							aspState, dupu));
				}

			} else {
				long[] rcs = rcObj.getRoutingContexts();
				for (int count = 0; count < rcs.length; count++) {
					Asp asp = this.aspFactory.getAsp(rcs[count]);
					if (asp == null) {
						// this is error. Send back error
						RoutingContext rcObjTemp = this.aspFactory.parameterFactory
								.createRoutingContext(new long[] { rcs[count] });
						ErrorCode errorCodeObj = this.aspFactory.parameterFactory
								.createErrorCode(ErrorCode.Invalid_Routing_Context);
						sendError(rcObjTemp, errorCodeObj);
						logger.error(String
								.format("Rx : DUPU=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
										dupu, rcs[count], this.aspFactory.getName()));
						continue;
					}// if (asp == null)

					FSM fsm = asp.getLocalFSM();

					if (fsm == null) {
						logger.error(String.format("Rx : DUPU=%s for ASP=%s. But Local FSM is null", dupu,
								this.aspFactory.getName()));
						return;
					}

					AspState aspState = AspState.getState(fsm.getState().getName());

					if (aspState == AspState.ACTIVE) {
						AffectedPointCode affectedPcObjs = dupu.getAffectedPointCode();
						int[] affectedPcs = affectedPcObjs.getPointCodes();
						int cause = 0;
						for (int i = 0; i < affectedPcs.length; i++) {

							UserCause userCause = dupu.getUserCause();
							cause = userCause.getCause();
							Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
									Mtp3StatusCause.getMtp3StatusCause(cause), 0);
							asp.getAs().getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
						}
					} else {
						logger.error(String.format("Rx : DUPU for RoutingContext=%d. But ASP State=%s. Message=%s",
								rcs[count], aspState, dupu));
					}
				}// for loop
			}

		} else {
			// TODO : Should we silently drop DUNA?

			// ASPACTIVE_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(rcObj, errorCodeObj);
		}
	}

	public void handleDestinationRestricted(DestinationRestricted drst) {

		if (aspFactory.getFunctionality() == Functionality.AS) {
			if (logger.isEnabledFor(Level.WARN)) {
				logger.warn(String.format("Received DRST message for AS side. Not implemented yet", drst));
			}
		} else {
			// TODP log error
		}
	}

}
