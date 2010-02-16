package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.message.parameter.CCSS;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericReference;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.MLPPPrecedence;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkManagementControls;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingISCPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PropagationDelayCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.TransitNetworkSelection;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformationPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table15" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px" align="center" colSpan="3">
 * <TABLE id="Table37" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * 
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * IAM (Initial Address Message)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 * An Initial Address Message (IAM) is sent in the "forward" direction by each
 * switch needed to complete the circuit between the calling party and called
 * party until the circuit connects to the destination switch. An IAM contains
 * the called party number in the mandatory variable part and may contain the
 * calling party name and number in the optional part.
 * </P>
 * </TD>
 * </TR>
 * </TABLE>
 * 
 * </TD>
 * </TR>
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 283px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Nature of Connection Indicators</TD>
 * 
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Forward&nbsp;Call Indicators</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * 
 * <TD>2</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Calling Party's Category</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; HEIGHT: 12px; TEXT-ALIGN: left">Transmission Medium
 * Requirement</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 12px">F</TD>
 * <TD style="HEIGHT: 12px">1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Called Party Number</TD>
 * 
 * <TD style="WIDTH: 145px">V</TD>
 * <TD>4-11</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; HEIGHT: 18px; TEXT-ALIGN: left">Transit Network
 * Selection</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 18px">O</TD>
 * <TD style="HEIGHT: 18px">4-?</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Call Reference</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Calling Party Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Optional Forward Call Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirecting Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-12</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-4</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Closed User Group Interlock Code</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>6</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Connection Request</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7-9</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Origional Called Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-12</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; HEIGHT: 17px; TEXT-ALIGN: left">User to User
 * Information</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 17px">O</TD>
 * <TD style="HEIGHT: 17px">3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User Service Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-13</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Number</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-13</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Propagation Delay Counter</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User Service Information Prime</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-13</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Digit</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Originating ISC Point Code</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User Service Information Prime</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Remote Operations</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility
 * Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Notification</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Service Activation</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Reference</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>5-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">MLPP Precedence</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>8</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Transmission Medium Requirement
 * Prime</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Location Number</TD>
 * 
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * 
 * </TR>
 * </TABLE>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface InitialAddressMessage extends ISUPMessage {
	public NatureOfConnectionIndicators getNatureOfConnectionIndicators();

	public void setNatureOfConnectionIndicators(NatureOfConnectionIndicators v);

	public ForwardCallIndicators getForwardCallIndicators();

	public void setForwardCallIndicators(ForwardCallIndicators v);

	public CallingPartyCategory getCallingPartCategory();

	public void setCallingPartCategory(CallingPartyCategory v);

	public TransmissionMediumRequirement getTransmissionMediumRequirement();

	public void setTransmissionMediumRequirement(TransmissionMediumRequirement v);

	public CalledPartyNumber getCalledPartyNumber();

	public void setCalledPartyNumber(CalledPartyNumber v);

	public TransitNetworkSelection getTransitNetworkSelection();

	public void setTransitNetworkSelection(TransitNetworkSelection v);

	public CallReference getCallReference();

	public void setCallReference(CallReference v);

	public CallingPartyNumber getCallingPartyNumber();

	public void setCallingPartyNumber(CallingPartyNumber v);

	public OptionalForwardCallIndicators getOptForwardCallIndicators();

	public void setOptForwardCallIndicators(OptionalForwardCallIndicators v);

	public RedirectingNumber getRedirectingNumber();

	public void setRedirectingNumber(RedirectingNumber v);

	public RedirectionInformation getRedirectionInformation();

	public void setRedirectionInformation(RedirectionInformation v);

	public ClosedUserGroupInterlockCode getCUserGroupInterlockCode();

	public void setCUserGroupInterlockCode(ClosedUserGroupInterlockCode v);

	public ConnectionRequest getConnectionRequest();

	public void setConnectionRequest(ConnectionRequest v);

	public OriginalCalledNumber getOriginalCalledNumber();

	public void setOriginalCalledNumber(OriginalCalledNumber v);

	public UserToUserInformation getU2UInformation();

	public void setU2UInformation(UserToUserInformation v);

	public UserServiceInformation getUserServiceInformation();

	public void setUserServiceInformation(UserServiceInformation v);

	public NetworkSpecificFacility getNetworkSpecificFacility();

	public void setNetworkSpecificFacility(NetworkSpecificFacility v);

	public GenericDigits getGenericDigits();

	public void setGenericDigits(GenericDigits v);

	public OriginatingISCPointCode getOriginatingISCPointCode();

	public void setOriginatingISCPointCode(OriginatingISCPointCode v);

	public UserTeleserviceInformation getUserTeleserviceInformation();

	public void setUserTeleserviceInformation(UserTeleserviceInformation v);

	public RemoteOperations getRemoteOperations();

	public void setRemoteOperations(RemoteOperations v);

	public ParameterCompatibilityInformation getParameterCompatibilityInformation();

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation v);

	public GenericNotificationIndicator getGenericNotificationIndicator();

	public void setGenericNotificationIndicator(GenericNotificationIndicator v);

	public ServiceActivation getServiceActivation();

	public void setServiceActivation(ServiceActivation v);

	public GenericReference getGenericReference();

	public void setGenericReference(GenericReference v);

	public MLPPPrecedence getMLPPPrecedence();

	public void setMLPPPrecedence(MLPPPrecedence v);

	public TransimissionMediumRequierementPrime getTransimissionMediumReqPrime();

	public void setTransimissionMediumReqPrime(TransimissionMediumRequierementPrime v);

	public LocationNumber getLocationNumber();

	public void setLocationNumber(LocationNumber v);

	public ForwardGVNS getForwardGVNS();

	public void setForwardGVNS(ForwardGVNS v);

	public CCSS getCCSS();

	public void setCCSS(CCSS v);

	public NetworkManagementControls getNetworkManagementControls();

	public void setNetworkManagementControls(NetworkManagementControls v);

	/**
	 * @param usip
	 */
	public void setUserServiceInformationPrime(UserServiceInformationPrime v);

	public UserServiceInformationPrime getUserServiceInformationPrime();

	/**
	 * @param pdc
	 */
	public void setPropagationDelayCounter(PropagationDelayCounter v);

	public PropagationDelayCounter getPropagationDelayCounter();

	/**
	 * @param gn
	 */
	public void setGenericNumber(GenericNumber v);

	public GenericNumber getGenericNumber();

	/**
	 * @param utui
	 */
	public void setU2UIndicators(UserToUserIndicators v);

	public UserToUserIndicators getU2UIndicators();

	/**
	 * @param at
	 */
	public void setAccessTransport(AccessTransport v);

	public AccessTransport getAccessTransport();
}
