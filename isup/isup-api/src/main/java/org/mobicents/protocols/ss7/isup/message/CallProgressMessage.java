package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransportParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table6" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table38" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Call Progress Message (CPG)</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 * CPG is sent in either direction during the set-up or active phase of the
 * call, indicating that an event, which is of significance, and should be
 * relayed to the originating or terminating access, has occurred.
 * </P>
 * </TD>
 * </TR>
 * </TABLE>
 * </TD>
 * 
 * </TR>
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 283px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Event Information</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * 
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Cause Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Backward Call Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Optional Backward Call Indicators</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Call Reference</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Connected Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>4-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Notification Indicator</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Call Diversion Information</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Transmission Medium Used</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Delivery Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility
 * Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Remote Operations</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Service Activation</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Number Restriction</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * </TR>
 * </TABLE>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallProgressMessage extends ISUPMessage {
	/**
	 * Call Progres Message, Q.763 reference table 23 <br>
	 * {@link CallProgressMessage}
	 */
	public static final int MESSAGE_CODE = 0x2C;

	public void setRedirectStatus(RedirectStatus rS);

	public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation pRBI);

	public void setCCNRPossibleIndicator(CCNRPossibleIndicator cCNR);

	public void setApplicationTransportParameter(ApplicationTransportParameter aTP);

	public void setUIDActionIndicators(UIDActionIndicators uIDA);

	public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators cTI);

	public void setCallHistoryInformation(CallHistoryInformation cHI);

	public void setGenericNumber(GenericNumber gN);

	public void setBackwardGVNS(BackwardGVNS bGVNS);

	public void setConnectedNumber(ConnectedNumber cN);

	public void setEchoControlInformation(EchoControlInformation eCI);

	public void setCallTransferNumber(CallTransferNumber cTR);

	public void setRedirectionNumberRestriction(RedirectionNumberRestriction rNR);

	public void setServiceActivation(ServiceActivation sA);

	public void setCallDiversionInformation(CallDiversionInformation cDI);

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation pCI);

	public void setAccessDeliveryInformation(AccessDeliveryInformation aDI);

	public void setTransmissionMediumUsed(TransmissionMediumUsed tMU);

	public void setRemoteOperations(RemoteOperations rO);

	public void setNetworkSpecificFacility(NetworkSpecificFacility nSF);

	public void setGenericNotificationIndicator(GenericNotificationIndicator gNI);

	public void setUserToUserInformation(UserToUserInformation u2uii);

	public void setRedirectionNumber(RedirectionNumber rN);

	public void setUserToUserIndicators(UserToUserIndicators u2ui);

	public void setAccessTransport(AccessTransport aT);

	public void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators oBCI);

	public void setBackwardCallIndicators(BackwardCallIndicators bCMI);

	public void setCallReference(CallReference cR);

	public void setCauseIndicators(CauseIndicators cI);

	public RedirectStatus getRedirectStatus();

	public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation();

	public CCNRPossibleIndicator getCCNRPossibleIndicator();

	public ApplicationTransportParameter getApplicationTransportParameter();

	public UIDActionIndicators getUIDActionIndicators();

	public ConferenceTreatmentIndicators getConferenceTreatmentIndicators();

	public CallHistoryInformation getCallHistoryInformation();

	public GenericNumber getGenericNumber();

	public BackwardGVNS getBackwardGVNS();

	public ConnectedNumber getConnectedNumber();

	public EchoControlInformation getEchoControlInformation();

	public CallTransferNumber getCallTransferNumber();

	public RedirectionNumberRestriction getRedirectionNumberRestriction();

	public ServiceActivation getServiceActivation();

	public CallDiversionInformation getCallDiversionInformation();

	public ParameterCompatibilityInformation getParameterCompatibilityInformation();

	public AccessDeliveryInformation getAccessDeliveryInformation();

	public TransmissionMediumUsed getTransmissionMediumUsed();

	public RemoteOperations getRemoteOperations();

	public NetworkSpecificFacility getNetworkSpecificFacility();

	public GenericNotificationIndicator getGenericNotificationIndicator();

	public UserToUserInformation getUserToUserInformation();

	public RedirectionNumber getRedirectionNumber();

	public UserToUserIndicators getUserToUserIndicators();

	public AccessTransport getAccessTransport();

	public OptionalBackwardCallIndicators getOptionalBackwardCallIndicators();

	public BackwardCallIndicators getBackwardCallIndicators();

	public CallReference getCallReference();

	public CauseIndicators getCauseIndicators();

	public void setEventInformation(EventInformation _ei);

	public EventInformation getEventInformation();
}
