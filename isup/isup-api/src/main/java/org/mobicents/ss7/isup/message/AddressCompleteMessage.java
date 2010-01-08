package org.mobicents.ss7.isup.message;

import org.mobicents.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.ss7.isup.message.parameter.ApplicationTransportParameter;
import org.mobicents.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.ss7.isup.message.parameter.CallReference;
import org.mobicents.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:09:41:44 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table3" style="FONT-SIZE: 9pt; WIDTH: 584px; COLOR: black; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * 
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px" align="center" colSpan="3">
 * <TABLE id="Table33" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * ACM (Address Complete Message)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 * 
 * An Address Complete Message (ACM) is sent in the "backward" direction to
 * indicate that the remote end of a trunk circuit has been reserved.
 * <P>
 * The originating switch responds to an ACM message by connecting the calling
 * party's line to the trunk to complete the voice circuit from the calling
 * party to the called party. The terminating switch sends a ringing tone to the
 * calling party's line. <BR>
 * </P>
 * </TD>
 * </TR>
 * </TABLE>
 * </TD>
 * </TR>
 * <TR>
 * 
 * <TD style="FONT-WEIGHT: bold; WIDTH: 239px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * 
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Backward Call Indicators</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>2</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Optional Backward Call Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Call Reference</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Cause Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">User to User Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Generic Notification Indicator</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Transmission Medium Used</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Echo Control Information</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Access Delivery Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Redirection Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-12</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Parameter Compatibility
 * Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Call Diversion Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Remote Operations</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Service Activation</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Redirection Number Restriction</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * </TR>
 * </TABLE>
 * 
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface AddressCompleteMessage extends ISUPMessage {

	public void setBackwardCallIndicators(BackwardCallIndicators indicators);

	public BackwardCallIndicators getBackwardCallIndicators();

	public void setOptionalBakwardCallIndicators(OptionalBackwardCallIndicators value);

	public OptionalBackwardCallIndicators getOptionalBakwardCallIndicators();

	public void setCallReference(CallReference value);

	public CallReference getCallReference();

	public void setCauseIndicators(CauseIndicators value);

	public CauseIndicators getCauseIndicators();

	public void setUserToUserIndicators(UserToUserIndicators value);

	public UserToUserIndicators getUserToUserIndicators();

	public void setUserToUserInformation(UserToUserInformation value);

	public UserToUserInformation getUserToUserInformation();

	public void setAccessTransport(AccessTransport value);

	public AccessTransport getAccessTransport();

	public void setGenericNotificationIndicator(GenericNotificationIndicator value);

	public GenericNotificationIndicator getGenericNotificationIndicator();

	public void setTransmissionMediumUsed(TransmissionMediumUsed value);

	public TransmissionMediumUsed getTransmissionMediumUsed();

	public void setEchoControlInformation(EchoControlInformation value);

	public EchoControlInformation getEchoControlInformation();

	public void setAccessDeliveryInformation(AccessDeliveryInformation value);

	public AccessDeliveryInformation getAccessDeliveryInformation();

	public void setRedirectionNumber(RedirectionNumber value);

	public RedirectionNumber getRedirectionNumber();

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation value);

	public ParameterCompatibilityInformation getParameterCompatibilityInformation();

	public void setCallDiversionInformation(CallDiversionInformation value);

	public CallDiversionInformation getCallDiversionInformation();

	public void setNetworkSpecificFacility(NetworkSpecificFacility value);

	public NetworkSpecificFacility getNetworkSpecificFacility();

	public void setRemoteOperations(RemoteOperations value);

	public RemoteOperations getRemoteOperations();

	public void setServiceActivation(ServiceActivation value);

	public RedirectionNumberRestriction getRedirectionNumberRestriction();

	public void setRedirectionNumberRestriction(RedirectionNumberRestriction value);

	public ServiceActivation getServiceActivation();

	public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators value);

	public ConferenceTreatmentIndicators getConferenceTreatmentIndicators();

	public void setUIDActionIndicators(UIDActionIndicators value);

	public UIDActionIndicators getUIDActionIndicators();

	public void setApplicationTransportParameter(ApplicationTransportParameter value);

	public ApplicationTransportParameter getApplicationTransportParameter();

	public void setCCNRPossibleIndicator(CCNRPossibleIndicator value);

	public CCNRPossibleIndicator getCCNRPossibleIndicator();

	public void setHTRInformation(HTRInformation value);

	public HTRInformation getHTRInformation();

	public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation value);

	public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation();

	public void setRedirectStatus(RedirectStatus value);

	public RedirectStatus getRedirectStatus();

}
