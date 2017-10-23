/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.functional;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.AllowedServices;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CCBSIndicators;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.mobicents.protocols.ss7.map.api.service.oam.SendImsiRequest;
import org.mobicents.protocols.ss7.map.api.service.oam.SendImsiResponse;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertReason;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GuidanceInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.callhandling.RoutingInfoImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.SendRoutingInformationRequestImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.SendRoutingInformationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.TripletListTest;
import org.mobicents.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.CAMELSubscriptionInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.mobicents.protocols.ss7.map.service.oam.SendImsiRequestImpl;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.RegisterSSRequestImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.OperationCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPFunctionalTest extends SccpHarness {

    private static Logger logger = Logger.getLogger(MAPFunctionalTest.class);
    protected static final String USSD_STRING = "*133#";
    protected static final String USSD_MENU = "Select 1)Wallpaper 2)Ringtone 3)Games";
    protected static final String USSD_RESPONSE = "1";
    protected static final String USSD_FINAL_RESPONSE = "Thank you";

    private static final int _TCAP_DIALOG_RELEASE_TIMEOUT = 0;
    private static final int _LITTLE_DELAY = 100;
    private static final int _WAIT_TIMEOUT = _TCAP_DIALOG_RELEASE_TIMEOUT + 500;

    private MAPStackImpl stack1;
    private MAPStackImpl stack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;

    @BeforeClass
    public void setUpClass() throws Exception {

        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        // this.setupLog4j();
        System.out.println("setUpTest");

        this.sccpStack1Name = "MAPFunctionalTestSccpStack1";
        this.sccpStack2Name = "MAPFunctionalTestSccpStack2";

        super.setUp();

        // this.setupLog4j();

        // create some fake addresses.

        peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN());

        this.stack1 = new MAPStackImplWrapper(this.sccpProvider1, getSSN());
        this.stack2 = new MAPStackImplWrapper(this.sccpProvider2, getSSN());

        this.stack1.start();
        this.stack2.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */

    @AfterMethod
    public void tearDown() {
        System.out.println("tearDownTest");
        this.stack1.stop();
        this.stack2.stop();
        super.tearDown();
    }

    private void setupLog4j() {

        InputStream inStreamLog4j = getClass().getResourceAsStream("/log4j.properties");

        Properties propertiesLog4j = new Properties();

        try {
            propertiesLog4j.load(inStreamLog4j);
            PropertyConfigurator.configure(propertiesLog4j);
        } catch (Exception e) {
            e.printStackTrace();
            BasicConfigurator.configure();
        }

        logger.debug("log4j configured");

    }

    /**
     * Below are test for MAP Dialog normal and abnormal actions
     */

    /**
     * Complex TC Dialog
     *
     * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest TC-CONTINUE + ExtensionContainer +
     * addUnstructuredSSRequest TC-CONTINUE + addUnstructuredSSResponse TC-END + addProcessUnstructuredSSResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComplexTCWithDialog() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            private int dialogStep;

            @Override
            public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
                super.onUnstructuredSSRequest(unstrReqInd);

                try {
                    String ussdString = unstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = unstrReqInd.getMSISDNAddressString();
                    this.debug("Received UnstructuredSSRequestIndication " + ussdString);

                    assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);

                    MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();
                    Long invokeId = unstrReqInd.getInvokeId();

                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_RESPONSE);
                    mapDialog.addUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);
                } catch (MAPException e) {
                    this.error("Erro while trying to send UnstructuredSSResponse", e);
                    fail("Erro while trying to add UnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null,
                                sequence++));
                        mapDialog.send();
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Erro while trying to send UnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                super.onDialogAccept(mapDialog, extensionContainer);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            private int dialogStep;
            private long processUnstructuredSSRequestInvokeId = 0l;

            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                    processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();
                    this.debug("InvokeId =  " + processUnstructuredSSRequestInvokeId);
                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
                    mapDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdStringObj, null, null);
                } catch (MAPException e) {
                    this.error("Error while trying to send UnstructuredSSRequest", e);
                    fail("Erro while trying to add UnstructuredSSRequest");
                }
            }

            @Override
            public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
                super.onUnstructuredSSResponse(unstrResInd);
                try {
                    String ussdString = unstrResInd.getUSSDString().getString(null);
                    logger.debug("Received UnstructuredSSResponse " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_RESPONSE, ussdString);
                    MAPDialogSupplementary mapDialog = unstrResInd.getMAPDialog();
                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
                    mapDialog.addProcessUnstructuredSSResponse(processUnstructuredSSRequestInvokeId,
                            new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);
                } catch (MAPException e) {
                    logger.error(e);
                    fail("Erro while trying to add ProcessUnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                    MAPExtensionContainer extensionContainer) {
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null,
                                sequence++));
                        mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
                        mapDialog.send();
                    } else {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication,
                                null, sequence++));
                        mapDialog.close(false);
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Erro while trying to send UnstructuredSSRequest or ProcessUnstructuredSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UnstructuredSSResponseIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Ending Dialog in the middle of conversation by "close(true)" - without sending components
     *
     * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest TC-CONTINUE + ExtensionContainer +
     * addUnstructuredSSRequest TC-END
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDialogEndAtTheMiddleConversation() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            private int dialogStep;

            @Override
            public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
                super.onUnstructuredSSRequest(unstrReqInd);

                try {
                    String ussdString = unstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = unstrReqInd.getMSISDNAddressString();
                    this.debug("Received UnstructuredSSRequestIndication " + ussdString);

                    assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);

                    MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();
                    Long invokeId = unstrReqInd.getInvokeId();

                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_RESPONSE);
                    mapDialog.addUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);
                } catch (MAPException e) {
                    this.error("Erro while trying to send UnstructuredSSResponse", e);
                    fail("Erro while trying to add UnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null,
                                sequence++));
                        mapDialog.close(true);
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Error while trying to send UnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                super.onDialogAccept(mapDialog, extensionContainer);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            private int dialogStep;
            private long processUnstructuredSSRequestInvokeId = 0l;

            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                    processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();
                    this.debug("InvokeId =  " + processUnstructuredSSRequestInvokeId);
                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
                    mapDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdStringObj, null, null);
                } catch (MAPException e) {
                    this.error("Error while trying to send UnstructuredSSRequest", e);
                    fail("Erro while trying to add UnstructuredSSRequest");
                }
            }

            @Override
            public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
                super.onUnstructuredSSResponse(unstrResInd);
                try {
                    String ussdString = unstrResInd.getUSSDString().getString(null);
                    logger.debug("Received UnstructuredSSResponse " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_RESPONSE, ussdString);
                    MAPDialogSupplementary mapDialog = unstrResInd.getMAPDialog();
                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
                    mapDialog.addProcessUnstructuredSSResponse(processUnstructuredSSRequestInvokeId,
                            new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);
                } catch (MAPException e) {
                    logger.error(e);
                    fail("Erro while trying to add ProcessUnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                    MAPExtensionContainer extensionContainer) {
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null,
                                sequence++));
                        mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
                        mapDialog.send();
                    } else {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication,
                                null, sequence++));
                        mapDialog.close(false);
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Erro while trying to send UnstructuredSSRequest or ProcessUnstructuredSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Server reject a Dialog with InvalidDestinationReference reason
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest refuse() -> TC-ABORT + MapRefuseInfo + ExtensionContainer
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDialogRefuse() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
                    ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);
                assertEquals(refuseReason, MAPRefuseReason.InvalidDestinationReference);
                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                String ussdString;
                try {
                    ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                } catch (MAPException e) {
                    this.error("Error while trying to parse ussdString", e);
                    fail("Erro while trying to parse ussdString");
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
                    mapDialog.refuse(Reason.invalidDestinationReference);
                } catch (MAPException e) {
                    this.error("Error while trying to send Refuse", e);
                    fail("Error while trying to send Refuse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Server reject a Dialog because of ApplicationContextName does not supported (Bad ACN is simulated)
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-ABORT(Reason=ACN_Not_Supprted) + alternativeApplicationContextName
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testInvalidApplicationContext() throws Exception {

        ((MAPServiceSupplementaryImplWrapper) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(1);

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
                    ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);
                assertEquals(refuseReason, MAPRefuseReason.ApplicationContextNotSupported);
                assertNotNull(alternativeApplicationContext);
                assertTrue(Arrays.equals(alternativeApplicationContext.getOid(), new long[] { 1, 2, 3 }));
                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address);

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        // !!!! ---
//         this.saveTrafficInFile();
        // !!!! ---

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * User-Abort as a response to TC-CONTINUE by a Client
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-CONTINUE + addUnstructuredSSRequest TC-ABORT(MAP-UserAbortInfo) +
     * ExtensionContainer
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDialogUserAbort() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
                super.onUnstructuredSSRequest(unstrReqInd);

                String ussdString;
                try {
                    ussdString = unstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = unstrReqInd.getMSISDNAddressString();
                    this.debug("Received UnstructuredSSRequestIndication " + ussdString);

                    assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);
                } catch (MAPException e) {
                    this.error("Error while trying to parse ussdString", e);
                    fail("Error while trying to parse ussdString");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);

                try {
                    mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
                    MAPUserAbortChoice choice = this.mapParameterFactory.createMAPUserAbortChoice();
                    choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
                    mapDialog.abort(choice);
                } catch (MAPException e) {
                    this.error("Error while trying to send UserAbort", e);
                    fail("Error while trying to send UserAbort");
                }
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
                    mapDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdStringObj, null, null);
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null,
                            sequence++));
                    mapDialog.send();
                } catch (MAPException e) {
                    this.error("Error while trying to send UnstructuredSSRequest", e);
                    fail("Error while trying to send UnstructuredSSRequest");
                }
            }

            @Override
            public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
                    MAPExtensionContainer extensionContainer) {
                super.onDialogUserAbort(mapDialog, userReason, extensionContainer);
                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                assertTrue(userReason.isProcedureCancellationReason());
                assertEquals(ProcedureCancellationReason.handoverCancellation, userReason.getProcedureCancellationReason());
                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Simulating a ProviderAbort from a Server (InvalidPDU)
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-ABORT(MAP-ProviderAbortInfo)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testReceivedDialogAbortInfo() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
                    MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {

                super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);

                this.debug("Received DialogProviderAbort " + abortProviderReason.toString());
                assertEquals(abortProviderReason, MAPAbortProviderReason.InvalidPDU);
                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address);

        ((MAPProviderImplWrapper) this.stack2.getMAPProvider()).setTestMode(1);

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Ericsson-style OpenInfo Dialog
     *
     * TC-BEGIN + Ericsson-style MAP-OpenInfo + addProcessUnstructuredSSRequest TC-END
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testEricssonDialog() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                    AddressString eriImsi, AddressString eriVlrNo) {
                super.onDialogRequestEricsson(mapDialog, destReference, origReference, eriImsi, eriVlrNo);
                assertNotNull(eriImsi);
                assertEquals(eriImsi.getAddress(), "12345");

                assertNotNull(eriVlrNo);
                assertEquals(eriVlrNo.getAddress(), "556677");

                assertNotNull(destReference);
                assertEquals(destReference.getAddress(), "888777");

                assertNotNull(origReference);
                assertEquals(origReference.getAddress(), "1115550000");
            }

            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                String ussdString;
                try {
                    ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                } catch (MAPException e) {
                    this.error("Error while trying to parse ussdString", e);
                    fail("Error while trying to parse ussdString");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogClose, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send empty response for received ProcessUnstructuredSSRequest", e);
                    fail("Error while trying to send empty response for received ProcessUnstructuredSSRequest");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogEricssonRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DialogClose, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionEricssonDialog();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Rejecting a dialog because of service is inactive
     *
     * TC-BEGIN + alertServiceCentre V2 TC-ABORT + DialogReject+ACNNotSupported
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testRejectServiceIsNotActive() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
                    ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);

                assertEquals(refuseReason, MAPRefuseReason.ApplicationContextNotSupported);
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        server.mapProvider.getMAPServiceSms().deactivate();
        // this.saveTrafficInFile();

        client.sendAlertServiceCentreRequestV2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Rejecting a dialog because of service is inactive - MAP V1
     *
     * TC-BEGIN + alertServiceCentre V1 TC-ABORT + DialogReject+ACNNotSupported
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testRejectServiceIsNotActiveV1() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        server.mapProvider.getMAPServiceSms().deactivate();
//        this.saveTrafficInFile();

        client.sendAlertServiceCentreRequestV1();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Below are test for MAP Component processing
     */

    /**
     * Sending ReturnError (MAPErrorMessageSystemFailure) component from the Server as a response to
     * ProcessUnstructuredSSRequest
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-END + ReturnError(systemFailure)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComponentErrorMessageSystemFailure() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
                super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
                assertTrue(mapErrorMessage.isEmSystemFailure());
                MAPErrorMessageSystemFailure mes = mapErrorMessage.getEmSystemFailure();
                assertNotNull(mes);
                assertTrue(mes.getAdditionalNetworkResource() == null);
                assertTrue(mes.getNetworkResource() == null);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                    MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSystemFailure(2, null, null, null);
                    mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
                } catch (MAPException e) {
                    this.error("Error while trying to add Error Component", e);
                    fail("Error while trying to add Error Component");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to send Error Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Sending ReturnError (SM-DeliveryFailure + SM-DeliveryFailureCause) component from the Server as a response to
     * ProcessUnstructuredSSRequest
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-END + ReturnError(SM-DeliveryFailure + SM-DeliveryFailureCause)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComponentErrorMessageSMDeliveryFailure() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
                super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
                assertTrue(mapErrorMessage.isEmSMDeliveryFailure());
                MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
                assertNotNull(mes);
                assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
                assertTrue(mes.getSignalInfo() == null);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                    MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSMDeliveryFailure(3,
                            SMEnumeratedDeliveryFailureCause.scCongestion, null, null);
                    mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
                } catch (MAPException e) {
                    this.error("Error while trying to add Error Component", e);
                    fail("Error while trying to add Error Component");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to send Error Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Responses as ReturnResult (this case is simulated) and ReturnResultLast
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-CONTINUE + ReturnResult (addProcessUnstructuredSSResponse) TC-CONTINUE
     * TC-END + ReturnResultLast (addProcessUnstructuredSSResponse)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComponentD() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            int responseReceived = 0;

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    mapDialog.send();
                } catch (MAPException e) {
                    this.error("Error while sending response", e);
                    fail("Error while trying to send empty response");
                }
            }

            @Override
            public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResponse) {
                super.onProcessUnstructuredSSResponse(procUnstrResponse);
                String ussdString;
                try {
                    ussdString = procUnstrResponse.getUSSDString().getString(null);
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    responseReceived++;
                    assertEquals(ussdString, "Your balance is 500");
                } catch (MAPException e) {
                    this.error("Error while trying to parse ussdString", e);
                    fail("Error while trying to parse ussdString");
                }

            }

            @Override
            public void onDialogRelease(MAPDialog mapDialog) {
                super.onDialogRelease(mapDialog);
                assertEquals(this.responseReceived, 2);
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            private long processUnstructuredSSRequestInvokeId = 0l;
            private int dialogStep;

            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                String ussdString;
                try {
                    ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                } catch (MAPException e1) {
                    this.error("Error while trying to parse ussdString", e1);
                    fail("Error while trying to parse ussdString");
                }

                MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

                processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();

                ReturnResult returnResult = ((MAPProviderImpl) this.mapProvider).getTCAPProvider()
                        .getComponentPrimitiveFactory().createTCResultRequest();
                returnResult.setInvokeId(processUnstructuredSSRequestInvokeId);

                // Operation Code
                OperationCode oc = TcapFactory.createOperationCode();
                oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
                returnResult.setOperationCode(oc);

                try {
                    CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
                    USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500",
                            ussdDataCodingScheme, null);
                    ProcessUnstructuredSSResponseImpl req = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme,
                            ussdStrObj);
                    AsnOutputStream aos = new AsnOutputStream();

                    req.encodeData(aos);

                    Parameter p = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory()
                            .createParameter();
                    p.setTagClass(req.getTagClass());
                    p.setPrimitive(req.getIsPrimitive());
                    p.setTag(req.getTag());
                    p.setData(aos.toByteArray());
                    returnResult.setParameter(p);

                    mapDialog.sendReturnResultComponent(returnResult);
                } catch (MAPException e) {
                    this.error("Error while trying to send ProcessUnstructuredSSResponse", e);
                    fail("Error while trying to send ProcessUnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication,
                                null, sequence++));
                        mapDialog.send();
                    } else {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication,
                                null, sequence++));
                        USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString(
                                "Your balance is 500");
                        CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
                        ((MAPDialogSupplementary) mapDialog).addProcessUnstructuredSSResponse(
                                this.processUnstructuredSSRequestInvokeId, ussdDataCodingScheme, ussdStrObj);

                        mapDialog.close(false);
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Error while trying to send ProcessUnstructuredSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Responses as Reject (DuplicateInvokeID) component from the Server as a response to ProcessUnstructuredSSRequest
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-END + Reject (ResourceLimitation) - manually sent Reject
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComponentDuplicateInvokeID() throws Exception {
        // Action_Component_E

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
                InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
                assertNotNull(invokeProblemType);
                assertEquals(invokeProblemType, InvokeProblemType.ResourceLimitation);
                assertTrue(problem.getGeneralProblemType() == null);
                assertTrue(problem.getReturnErrorProblemType() == null);
                assertTrue(problem.getReturnResultProblemType() == null);
                assertFalse(isLocalOriginated);
                assertEquals((long) invokeId, 0);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

                    Problem problem = this.mapProvider.getMAPParameterFactory().createProblemInvoke(
                            InvokeProblemType.ResourceLimitation);

                    mapDialog.sendRejectComponent(procUnstrReqInd.getInvokeId(), problem);
                } catch (MAPException e) {
                    this.error("Error while trying to send Duplicate InvokeId Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to send Duplicate InvokeId Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Responses as ReturnError component from the Server as a response to ProcessUnstructuredSSRequest but the error received
     * because of "close(true)"
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-END + ReturnError(systemFailure) - using "close(true)" - so no ReturnError
     * must be sent !
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComponentErrorCloseTrue() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
                super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
                assertTrue(mapErrorMessage.isEmSMDeliveryFailure());
                MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
                assertNotNull(mes);
                assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
                assertTrue(mes.getSignalInfo() == null);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                    MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSMDeliveryFailure(3,
                            SMEnumeratedDeliveryFailureCause.scCongestion, null, null);

                    mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
                } catch (MAPException e) {
                    this.error("Error while trying to add Error Component", e);
                    fail("Error while trying to add Error Component");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
                    mapDialog.close(true);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to send Error Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Responses as Reject (ResourceLimitation without invokeId!) component from the Server as a response to
     * ProcessUnstructuredSSRequest
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest TC-END + Reject (invokeProblem-ResourceLimitation) without invokeId! - this
     * Reject is Invoked by MAP-user
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testComponentGeneralProblemTypeComponent() throws Exception {
        // Action_Component_G

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
                InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
                assertNotNull(invokeProblemType);
                assertEquals(invokeProblemType, InvokeProblemType.ResourceLimitation);
                assertTrue(problem.getGeneralProblemType() == null);
                assertTrue(problem.getReturnErrorProblemType() == null);
                assertTrue(problem.getReturnResultProblemType() == null);
                assertFalse(isLocalOriginated);
                assertNull(invokeId);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

                    Problem problem = this.mapProvider.getMAPParameterFactory().createProblemInvoke(
                            InvokeProblemType.ResourceLimitation);

                    mapDialog.sendRejectComponent(null, problem);
                } catch (MAPException e) {
                    this.error("Error while trying to add Duplicate InvokeId Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.RejectComponent, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.RejectComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Rejecting an Invoke with a bad OperationCode==1000
     *
     * TC-BEGIN + Invoke(bad opCode==1000) TC-END + Reject (generalProblem-UnrecognizedOperation) without invokeId!
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testInvokeUnrecognizedOperation() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
                InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
                assertEquals(invokeProblemType, InvokeProblemType.UnrecognizedOperation);
                assertFalse(isLocalOriginated);
                assertEquals((long) invokeId, 10L);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                } catch (MAPException e) {
                    this.error("Error while trying to add Duplicate InvokeId Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
                InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
                assertEquals(invokeProblemType, InvokeProblemType.UnrecognizedOperation);
                assertTrue(isLocalOriginated);
                assertEquals((long) invokeId, 10L);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendUnrecognizedOperation();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Rejecting an Invoke with a bad Parameter (decoding error)
     *
     * TC-BEGIN + Invoke(bad opCode==1000) TC-END + Reject (generalProblem-MistypedParameter) without invokeId!
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testInvokeMistypedParameter() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
                InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
                assertEquals(invokeProblemType, InvokeProblemType.MistypedParameter);
                assertFalse(isLocalOriginated);
                assertEquals((long) invokeId, 10L);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);
                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                } catch (MAPException e) {
                    this.error("Error while trying to add Duplicate InvokeId Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
                InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
                assertEquals(invokeProblemType, InvokeProblemType.MistypedParameter);
                assertTrue(isLocalOriginated);
                assertEquals((long) invokeId, 10L);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendMystypedParameter();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + sendRoutingInfoForSMRequest + reportSMDeliveryStatusRequest TC-CONTINUE sendRoutingInfoForSMResponse +
     * sendRoutingInfoForSMResponse for the same InvokeId + SystemFailureError for the same InvokeId TC-END +
     * Reject(ReturnResultProblemType.UnrecognizedInvokeID) + Reject(ReturnErrorProblemType.UnrecognizedInvokeID)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testUnrecognizedInvokeID() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            private int stepRej = 0;

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

                stepRej++;

                if (stepRej == 1) {
                    assertEquals(problem.getType(), ProblemType.ReturnResult);
                    assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.UnrecognizedInvokeID);
                } else {
                    assertEquals(problem.getType(), ProblemType.ReturnError);
                    assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedInvokeID);
                }
                assertTrue(isLocalOriginated);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the TC-CONTINUE", e);
                    fail("Error while sending the TC-CONTINUE");
                }
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            private int step = 0;
            private int stepRej = 0;
            private long invokeId1;
            private long invokeId2;

            @Override
            public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest ind) {
                super.onSendRoutingInfoForSMRequest(ind);
                invokeId1 = ind.getInvokeId();
            }

            @Override
            public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest ind) {
                super.onReportSMDeliveryStatusRequest(ind);
                invokeId2 = ind.getInvokeId();
            }

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

                stepRej++;

                assertEquals((long) invokeId, invokeId1);
                if (stepRej == 1) {
                    assertEquals(problem.getType(), ProblemType.ReturnResult);
                    assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.UnrecognizedInvokeID);
                } else {
                    assertEquals(problem.getType(), ProblemType.ReturnError);
                    assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedInvokeID);
                }
                assertFalse(isLocalOriginated);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    step++;
                    MAPDialogSms clientDialogSms = (MAPDialogSms) mapDialog;

                    switch (step) {
                        case 1:

                            ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(
                                    AddressNature.international_number, NumberingPlan.ISDN, "11223344");
                            // AddressString serviceCentreAddress =
                            // this.mapParameterFactory.createAddressString(AddressNature.international_number,
                            // NumberingPlan.ISDN, "1122334455");
                            IMSI imsi = this.mapParameterFactory.createIMSI("777222");
                            LocationInfoWithLMSI locationInfoWithLMSI = this.mapParameterFactory.createLocationInfoWithLMSI(
                                    msisdn, null, null, false, null);
                            clientDialogSms.addSendRoutingInfoForSMResponse(invokeId1, imsi, locationInfoWithLMSI, null, null, null);

                            this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication,
                                    null, sequence++));

                            imsi = this.mapParameterFactory.createIMSI("777222222");
                            clientDialogSms.addSendRoutingInfoForSMResponse(invokeId1, imsi, locationInfoWithLMSI, null, null, null);

                            this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication,
                                    null, sequence++));

                            MAPErrorMessage mapErrorMessage = this.mapErrorMessageFactory.createMAPErrorMessageSystemFailure(3,
                                    NetworkResource.hlr, null, null);
                            clientDialogSms.sendErrorComponent(invokeId1, mapErrorMessage);

                            this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));

                            mapDialog.send();
                            break;
                    }

                } catch (MAPException e) {
                    this.error("Error while sending TC-CONTINUE or TC-END", e);
                    fail("Error while sending TC-CONTINUE or TC-END");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.send_sendRoutingInfoForSMRequest_reportSMDeliveryStatusRequest();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Rejecting - an ReturtResult with a bad Parameter (decoding error) ReturtResultProblem.MistypedParameter - an ReturtError
     * with a bad Parameter (decoding error) ReturtErrorProblem.MistypedParameter - an ReturtError with a bad code
     * ReturtErrorProblem.UnrecognizedError
     *
     * TC-BEGIN + addProcessUnstructuredSSRequest + addProcessUnstructuredSSRequest + addProcessUnstructuredSSRequest
     * TC-CONTINUE + ReturnResultLast with a bad Parameter + ReturnError with a bad Parameter + ReturnError with a bad errorCode
     * (=1000) TC-END + Reject (ReturnResultProblem.MistypedParameter) + Reject (ReturnErrorProblem.MistypedParameter) + Reject
     * (ReturnErrorProblem.UnrecognizedError)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testResultErrorMistypedParameter() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            private int rejectStep;

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

                rejectStep++;

                switch (rejectStep) {
                    case 1:
                        assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.MistypedParameter);
                        assertTrue(isLocalOriginated);
                        assertEquals((long) invokeId, 0L);
                        break;
                    case 2:
                        assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.MistypedParameter);
                        assertTrue(isLocalOriginated);
                        assertEquals((long) invokeId, 1L);
                        break;
                    case 3:
                        assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedError);
                        assertTrue(isLocalOriginated);
                        assertEquals((long) invokeId, 2L);
                        break;
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);

                try {
                    mapDialog.close(false);
                } catch (Exception e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            private int step;
            private long invokeId1;
            private long invokeId2;
            private long invokeId3;
            private int rejectStep;

            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
                super.onProcessUnstructuredSSRequest(procUnstrReqInd);

                try {
                    String ussdString = procUnstrReqInd.getUSSDString().getString(null);
                    AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
                    this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
                    assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
                    MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
                } catch (MAPException e) {
                    this.error("Error while trying to add Duplicate InvokeId Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }

                step++;
                switch (step) {
                    case 1:
                        invokeId1 = procUnstrReqInd.getInvokeId();
                        break;
                    case 2:
                        invokeId2 = procUnstrReqInd.getInvokeId();
                        break;
                    case 3:
                        invokeId3 = procUnstrReqInd.getInvokeId();
                        break;
                }
            }

            @Override
            public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
                super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

                rejectStep++;

                switch (rejectStep) {
                    case 1:
                        assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.MistypedParameter);
                        assertFalse(isLocalOriginated);
                        assertEquals((long) invokeId, invokeId1);
                        break;
                    case 2:
                        assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.MistypedParameter);
                        assertFalse(isLocalOriginated);
                        assertEquals((long) invokeId, invokeId2);
                        break;
                    case 3:
                        assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedError);
                        assertFalse(isLocalOriginated);
                        assertEquals((long) invokeId, invokeId3);
                        break;
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    ReturnResultLast rrl = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createTCResultLastRequest();
                    rrl.setInvokeId(invokeId1);
                    OperationCode opCode = new OperationCodeImpl();
                    opCode.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
                    rrl.setOperationCode(opCode);
                    Parameter par = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createParameter();
                    par.setData(new byte[] { 1, 1, 1, 1, 1 });
                    rrl.setParameter(par);
                    mapDialog.sendReturnResultLastComponent(rrl);

                    ReturnError err = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createTCReturnErrorRequest();
                    err.setInvokeId(invokeId2);
                    ErrorCode ec = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createErrorCode();
                    ec.setLocalErrorCode((long) MAPErrorCode.systemFailure);
                    err.setErrorCode(ec);
                    par = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createParameter();
                    par.setData(new byte[] { 1, 1, 1, 1, 1 });
                    err.setParameter(par);
                    ((MAPDialogImpl) mapDialog).getTcapDialog().sendComponent(err);

                    err = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createTCReturnErrorRequest();
                    err.setInvokeId(invokeId3);
                    ec = ((MAPProviderImpl) mapDialog.getService().getMAPProvider()).getTCAPProvider()
                            .getComponentPrimitiveFactory().createErrorCode();
                    ec.setLocalErrorCode(1000L);
                    err.setErrorCode(ec);
                    rrl.setOperationCode(opCode);
                    ((MAPDialogImpl) mapDialog).getTcapDialog().sendComponent(err);

                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));

                    mapDialog.send();
                } catch (Exception e) {
                    this.error("Error while trying to send Error Component", e);
                    fail("Error while trying to add Duplicate InvokeId Component");
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionAAA();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + addProcessUnstructuredSSRequest (releasing Dialog at a client side) TC-CONTINUE
     * addProcessUnstructuredSSResponse TC-ABORT (UnrecognizedTxID)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSupportingDialogueTransactionReleased() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            private long invokeId;

            @Override
            public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
                    MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
                super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);

                assertEquals(abortProviderReason, MAPAbortProviderReason.SupportingDialogueTransactionReleased);
                assertEquals(abortSource, MAPAbortSource.TCProblem);
            }

            @Override
            public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest unstrResInd) {
                super.onProcessUnstructuredSSRequest(unstrResInd);
                try {
                    String ussdString = unstrResInd.getUSSDString().getString(null);
                    logger.debug("Received UnstructuredSSResponse " + ussdString);
                    invokeId = unstrResInd.getInvokeId();
                } catch (MAPException e) {
                    logger.error(e);
                    fail("Erro while trying to add ProcessUnstructuredSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {

                super.onDialogDelimiter(mapDialog);

                try {
                    Thread.sleep(_LITTLE_DELAY);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
                    MAPDialogSupplementary mapDialogSupp = (MAPDialogSupplementary) mapDialog;
                    mapDialogSupp.addProcessUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);

                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null,
                            sequence++));

                    mapDialog.send();

                } catch (MAPException e) {
                    this.error("Error while sending TC-CONTINUE or TC-END", e);
                    fail("Error while sending TC-CONTINUE or TC-END");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++,
                (stamp + _LITTLE_DELAY + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++,
                (stamp + _LITTLE_DELAY + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
                (stamp + _LITTLE_DELAY + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.actionA();
        client.clientDialog.release();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + addProcessUnstructuredSSRequest (bad sccp address + setReturnMessageOnError) TC-NOTICE
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testTcNotice() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
                    ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);

                assertEquals(refuseReason, MAPRefuseReason.RemoteNodeNotReachable);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.actionB();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Below are test for MAP V1 Dialogs
     */

    /**
     * TC-BEGIN+INVOKE(opCode=47) -> TC-END+RRL(opCode=47) (47=reportSM-DeliveryStatus)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV1ReportSMDeliveryStatus() throws Exception {
        // Action_V1_A
        Client client = new Client(stack1, this, peer1Address, peer2Address);

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
                super.onReportSMDeliveryStatusRequest(reportSMDeliveryStatusInd);
                MAPDialogSms d = reportSMDeliveryStatusInd.getMAPDialog();

                ISDNAddressString msisdn = reportSMDeliveryStatusInd.getMsisdn();
                AddressString sca = reportSMDeliveryStatusInd.getServiceCentreAddress();
                SMDeliveryOutcome sMDeliveryOutcome = reportSMDeliveryStatusInd.getSMDeliveryOutcome();
                Integer absentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAbsentSubscriberDiagnosticSM();
                MAPExtensionContainer extensionContainer = reportSMDeliveryStatusInd.getExtensionContainer();
                Boolean gprsSupportIndicator = reportSMDeliveryStatusInd.getGprsSupportIndicator();
                Boolean deliveryOutcomeIndicator = reportSMDeliveryStatusInd.getDeliveryOutcomeIndicator();
                SMDeliveryOutcome additionalSMDeliveryOutcome = reportSMDeliveryStatusInd.getAdditionalSMDeliveryOutcome();
                Integer additionalAbsentSubscriberDiagnosticSM = reportSMDeliveryStatusInd
                        .getAdditionalAbsentSubscriberDiagnosticSM();

                Assert.assertNotNull(msisdn);
                Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(msisdn.getAddress(), "111222333");
                Assert.assertNotNull(sca);
                Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(sca.getAddress(), "999000");
                Assert.assertNull(sMDeliveryOutcome);
                Assert.assertNull(absentSubscriberDiagnosticSM);
                Assert.assertFalse(gprsSupportIndicator);
                Assert.assertFalse(deliveryOutcomeIndicator);
                Assert.assertNull(additionalSMDeliveryOutcome);
                Assert.assertNull(additionalAbsentSubscriberDiagnosticSM);
                Assert.assertNull(extensionContainer);

                try {
                    d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), null, null);
                } catch (MAPException e) {
                    this.error("Error while trying to add ReportSMDeliveryStatusResponse", e);
                    fail("Error while trying to add ReportSMDeliveryStatusResponse");
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send ReportSMDeliveryStatusResponse", e);
                    fail("Error while trying to send ReportSMDeliveryStatusResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendReportSMDeliveryStatusV1();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN+INVOKE(opCode=49) -> release()
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV1AlertServiceCentreRequest() throws Exception {
        // Action_V1_B

        Client client = new Client(stack1, this, peer1Address, peer2Address);

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
                super.onAlertServiceCentreRequest(alertServiceCentreInd);
                MAPDialogSms d = alertServiceCentreInd.getMAPDialog();

                ISDNAddressString msisdn = alertServiceCentreInd.getMsisdn();
                AddressString serviceCentreAddress = alertServiceCentreInd.getServiceCentreAddress();

                Assert.assertNotNull(msisdn);
                Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(msisdn.getAddress(), "111222333");
                Assert.assertNotNull(serviceCentreAddress);
                Assert.assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.subscriber_number);
                Assert.assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(serviceCentreAddress.getAddress(), "0011");

                if (d.getApplicationContext().getApplicationContextVersion() == MAPApplicationContextVersion.version1)
                    d.processInvokeWithoutAnswer(alertServiceCentreInd.getInvokeId());
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                mapDialog.release();
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendAlertServiceCentreRequestV1();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN(empty - no components) -> TC-ABORT V1
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV1AlertServiceCentreRequestReject() throws Exception {
        // Action_V1_C

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
                    ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);
                assertNotNull(refuseReason);
                assertEquals(refuseReason, MAPRefuseReason.PotentialVersionIncompatibility);
                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address);

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.sendEmptyV1Request();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN(unsupported opCode) -> TC-ABORT V1
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV1AlertServiceCentreRequestReject2() throws Exception {
        // Action_V1_D

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
                    ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);
                assertNotNull(refuseReason);
                assertEquals(refuseReason, MAPRefuseReason.PotentialVersionIncompatibility);
                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address);

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.sendV1BadOperationCode();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN+INVOKE(opCode=46) -> TC-CONTINUE(empty) -> TC-ABORT(UserReason) (->Abort V1)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV1ForwardShortMessageRequest() throws Exception {
        // Action_V1_E

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    MAPUserAbortChoice choice = this.mapParameterFactory.createMAPUserAbortChoice();
                    choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
                    mapDialog.abort(choice);
                } catch (MAPException e) {
                    this.error("Error while trying to send Abort", e);
                    fail("Error while trying to send Abort");
                }
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
                super.onForwardShortMessageRequest(forwSmInd);
                MAPDialogSms d = forwSmInd.getMAPDialog();

                SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
                SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
                SmsSignalInfo sm_RP_UI = forwSmInd.getSM_RP_UI();

                Assert.assertNotNull(sm_RP_DA);
                Assert.assertNotNull(sm_RP_DA.getIMSI());
                Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
                Assert.assertNotNull(sm_RP_OA);
                Assert.assertNotNull(sm_RP_OA.getMsisdn());
                Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
                Assert.assertNotNull(sm_RP_UI);
                Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
                Assert.assertFalse(forwSmInd.getMoreMessagesToSend());

            }

            @Override
            public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
                    MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
                super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);

                Assert.assertEquals(abortProviderReason, MAPAbortProviderReason.AbnormalMAPDialogueLocal);
                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    // this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageRespIndication, null,
                    // sequence++));
                    mapDialog.send();
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error while sending the empty ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent
                .createReceivedEvent(EventType.DialogProviderAbort, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendForwardShortMessageRequestV1();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Below are test from testSmsService
     */

    /**
     * TC-BEGIN + AlertServiceCentreRequest TC-END
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV2AlertServiceCentreRequest() throws Exception {
        // Action_Sms_AlertServiceCentre

        Client client = new Client(stack1, this, peer1Address, peer2Address);

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
                super.onAlertServiceCentreRequest(alertServiceCentreInd);
                MAPDialogSms d = alertServiceCentreInd.getMAPDialog();

                ISDNAddressString msisdn = alertServiceCentreInd.getMsisdn();
                AddressString serviceCentreAddress = alertServiceCentreInd.getServiceCentreAddress();

                Assert.assertNotNull(msisdn);
                Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(msisdn.getAddress(), "111222333");
                Assert.assertNotNull(serviceCentreAddress);
                Assert.assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.subscriber_number);
                Assert.assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(serviceCentreAddress.getAddress(), "0011");

                try {
                    d.addAlertServiceCentreResponse(alertServiceCentreInd.getInvokeId());
                } catch (MAPException e) {
                    this.error("Error when adding AlertServiceCentreResponse", e);
                    fail("Error when adding AlertServiceCentreResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while trying to send AlertServiceCentreResponse", e);
                    fail("Error when sending AlertServiceCentreResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.AlertServiceCentreRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendAlertServiceCentreRequestV2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + ForwardSMRequest_V2 TC-END + ForwardSMResponse_V2
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testV2ForwardShortMessageRequest() throws Exception {
        // Action_Sms_ForwardSM

        Client client = new Client(stack1, this, peer1Address, peer2Address);

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
                super.onForwardShortMessageRequest(forwSmInd);
                MAPDialogSms d = forwSmInd.getMAPDialog();

                SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
                SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
                SmsSignalInfo sm_RP_UI = forwSmInd.getSM_RP_UI();

                Assert.assertNotNull(sm_RP_DA);
                Assert.assertNotNull(sm_RP_DA.getIMSI());
                Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
                Assert.assertNotNull(sm_RP_OA);
                Assert.assertNotNull(sm_RP_OA.getMsisdn());
                Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
                Assert.assertNotNull(sm_RP_UI);
                Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
                Assert.assertTrue(forwSmInd.getMoreMessagesToSend());
                try {
                    d.addForwardShortMessageResponse(forwSmInd.getInvokeId());
                } catch (MAPException e) {
                    this.error("Error while adding ForwardShortMessageResponse", e);
                    fail("Error when adding ForwardShortMessageResponse");
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error when sending ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ForwardShortMessageRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendForwardShortMessageRequestV2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + MoForwardSMRequest TC-END + MoForwardSMResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testMoForwardShortMessageRequest() throws Exception {
        // Action_Sms_MoForwardSM

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
                super.onMoForwardShortMessageResponse(moForwSmRespInd);
                SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
                MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

                Assert.assertNotNull(sm_RP_UI);
                Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
                super.onMoForwardShortMessageRequest(moForwSmInd);
                MAPDialogSms d = moForwSmInd.getMAPDialog();

                SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
                SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
                SmsSignalInfo sm_RP_UI = moForwSmInd.getSM_RP_UI();
                MAPExtensionContainer extensionContainer = moForwSmInd.getExtensionContainer();
                IMSI imsi2 = moForwSmInd.getIMSI();

                Assert.assertNotNull(sm_RP_DA);
                Assert.assertNotNull(sm_RP_DA.getIMSI());
                Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
                Assert.assertNotNull(sm_RP_OA);
                Assert.assertNotNull(sm_RP_OA.getMsisdn());
                Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
                Assert.assertNotNull(sm_RP_UI);

                try {
                    SmsSubmitTpdu tpdu = (SmsSubmitTpdu) sm_RP_UI.decodeTpdu(true);
                    tpdu.getUserData().decode();
                    Assert.assertFalse(tpdu.getRejectDuplicates());
                    Assert.assertTrue(tpdu.getReplyPathExists());
                    Assert.assertFalse(tpdu.getStatusReportRequest());
                    Assert.assertEquals(tpdu.getMessageReference(), 55);
                    Assert.assertEquals(tpdu.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
                    Assert.assertEquals(tpdu.getDestinationAddress().getNumberingPlanIdentification(),
                            NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
                    Assert.assertTrue(tpdu.getDestinationAddress().getAddressValue().equals("700007"));
                    Assert.assertEquals(tpdu.getProtocolIdentifier().getCode(), 0);
                    Assert.assertEquals((int) tpdu.getValidityPeriod().getRelativeFormatValue(), 100);
                    Assert.assertEquals(tpdu.getUserData().getDataCodingScheme().getCode(), 0);
                    Assert.assertTrue(tpdu.getUserData().getDecodedMessage().equals("Hello, world !!!"));
                } catch (MAPException e) {
                    this.error("Erro while trying to decode SmsSubmitTpdu", e);
                    fail("Erro while trying to decode SmsSubmitTpdu");
                }

                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                Assert.assertNotNull(imsi2);
                Assert.assertEquals(imsi2.getData(), "25007123456789");

                SmsSignalInfo sm_RP_UI2 = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
                try {
                    d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2,
                            MAPExtensionContainerTest.GetTestExtensionContainer());
                } catch (MAPException e) {
                    this.error("Error while adding MoForwardShortMessageResponse", e);
                    fail("Error while adding MoForwardShortMessageResponse");
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error while sending the empty ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendMoForwardShortMessageRequest();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + MtForwardSMRequest TC-END + MtForwardSMResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testMtForwardShortMessageRequest() throws Exception {
        // Action_Sms_MtForwardSM

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
                super.onMtForwardShortMessageResponse(mtForwSmRespInd);
                SmsSignalInfo sm_RP_UI = mtForwSmRespInd.getSM_RP_UI();
                MAPExtensionContainer extensionContainer = mtForwSmRespInd.getExtensionContainer();

                Assert.assertNotNull(sm_RP_UI);
                Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
                super.onMtForwardShortMessageRequest(mtForwSmInd);

                MAPDialogSms d = mtForwSmInd.getMAPDialog();

                SM_RP_DA sm_RP_DA = mtForwSmInd.getSM_RP_DA();
                SM_RP_OA sm_RP_OA = mtForwSmInd.getSM_RP_OA();
                SmsSignalInfo sm_RP_UI = mtForwSmInd.getSM_RP_UI();
                MAPExtensionContainer extensionContainer = mtForwSmInd.getExtensionContainer();
                Boolean moreMessagesToSend = mtForwSmInd.getMoreMessagesToSend();

                Assert.assertNotNull(sm_RP_DA);
                Assert.assertNotNull(sm_RP_DA.getLMSI());
                Assert.assertTrue(Arrays.equals(sm_RP_DA.getLMSI().getData(), new byte[] { 49, 48, 47, 46 }));
                Assert.assertNotNull(sm_RP_OA);
                Assert.assertNotNull(sm_RP_OA.getServiceCentreAddressOA());
                Assert.assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "111222333");
                Assert.assertNotNull(sm_RP_UI);
                Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                Assert.assertTrue(moreMessagesToSend);

                SmsSignalInfo sm_RP_UI2 = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

                try {
                    d.addMtForwardShortMessageResponse(mtForwSmInd.getInvokeId(), sm_RP_UI2,
                            MAPExtensionContainerTest.GetTestExtensionContainer());
                } catch (MAPException e) {
                    this.error("Error while adding MtForwardShortMessageResponse", e);
                    fail("Error while adding MtForwardShortMessageResponse");
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.MtForwardShortMessageRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error while sending the empty ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.MtForwardShortMessageIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MtForwardShortMessageRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MtForwardShortMessageIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.MtForwardShortMessageRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendMtForwardShortMessageRequest();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * MAP V3 TC-BEGIN + ReportSMDeliveryStatusRequest TC-END + ReportSMDeliveryStatusResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testReportSMDeliveryStatusRequestV3() throws Exception {
        // Action_Sms_ReportSMDeliveryStatus

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
                super.onReportSMDeliveryStatusResponse(reportSMDeliveryStatusRespInd);
                ISDNAddressString storedMSISDN = reportSMDeliveryStatusRespInd.getStoredMSISDN();
                MAPExtensionContainer extensionContainer = reportSMDeliveryStatusRespInd.getExtensionContainer();

                Assert.assertNotNull(storedMSISDN);
                Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(storedMSISDN.getAddress(), "111000111");
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
                super.onReportSMDeliveryStatusRequest(reportSMDeliveryStatusInd);

                MAPDialogSms d = reportSMDeliveryStatusInd.getMAPDialog();

                ISDNAddressString msisdn = reportSMDeliveryStatusInd.getMsisdn();
                AddressString sca = reportSMDeliveryStatusInd.getServiceCentreAddress();
                SMDeliveryOutcome sMDeliveryOutcome = reportSMDeliveryStatusInd.getSMDeliveryOutcome();
                Integer absentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAbsentSubscriberDiagnosticSM();
                MAPExtensionContainer extensionContainer = reportSMDeliveryStatusInd.getExtensionContainer();
                Boolean gprsSupportIndicator = reportSMDeliveryStatusInd.getGprsSupportIndicator();
                Boolean deliveryOutcomeIndicator = reportSMDeliveryStatusInd.getDeliveryOutcomeIndicator();
                SMDeliveryOutcome additionalSMDeliveryOutcome = reportSMDeliveryStatusInd.getAdditionalSMDeliveryOutcome();
                Integer additionalAbsentSubscriberDiagnosticSM = reportSMDeliveryStatusInd
                        .getAdditionalAbsentSubscriberDiagnosticSM();

                Assert.assertNotNull(msisdn);
                Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(msisdn.getAddress(), "111222333");
                Assert.assertNotNull(sca);
                Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(sca.getAddress(), "999000");
                Assert.assertEquals(sMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);

                Assert.assertNotNull(absentSubscriberDiagnosticSM);
                Assert.assertEquals((int) absentSubscriberDiagnosticSM, 555);
                Assert.assertTrue(gprsSupportIndicator);
                Assert.assertTrue(deliveryOutcomeIndicator);
                Assert.assertEquals(additionalSMDeliveryOutcome, SMDeliveryOutcome.successfulTransfer);
                Assert.assertNotNull(additionalAbsentSubscriberDiagnosticSM);
                Assert.assertEquals((int) additionalAbsentSubscriberDiagnosticSM, 444);
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

                ISDNAddressString storedMSISDN = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.network_specific_number, NumberingPlan.national, "111000111");

                try {
                    d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), storedMSISDN,
                            MAPExtensionContainerTest.GetTestExtensionContainer());
                } catch (MAPException e) {
                    this.error("Error while adding ReportSMDeliveryStatusResponse", e);
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error while sending the empty ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendReportSMDeliveryStatus3();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * MAP V2 TC-BEGIN + ReportSMDeliveryStatusRequest TC-END + ReportSMDeliveryStatusResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testReportSMDeliveryStatusRequestV2() throws Exception {
        // Action_Sms_ReportSMDeliveryStatus

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
                super.onReportSMDeliveryStatusResponse(reportSMDeliveryStatusRespInd);
                ISDNAddressString storedMSISDN = reportSMDeliveryStatusRespInd.getStoredMSISDN();
                MAPExtensionContainer extensionContainer = reportSMDeliveryStatusRespInd.getExtensionContainer();

                Assert.assertNotNull(storedMSISDN);
                Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(storedMSISDN.getAddress(), "111000111");
                Assert.assertNull(extensionContainer);

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
                super.onReportSMDeliveryStatusRequest(reportSMDeliveryStatusInd);

                MAPDialogSms d = reportSMDeliveryStatusInd.getMAPDialog();

                ISDNAddressString msisdn = reportSMDeliveryStatusInd.getMsisdn();
                AddressString sca = reportSMDeliveryStatusInd.getServiceCentreAddress();
                SMDeliveryOutcome sMDeliveryOutcome = reportSMDeliveryStatusInd.getSMDeliveryOutcome();

                Integer absentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAbsentSubscriberDiagnosticSM();
                MAPExtensionContainer extensionContainer = reportSMDeliveryStatusInd.getExtensionContainer();
                boolean gprsSupportIndicator = reportSMDeliveryStatusInd.getGprsSupportIndicator();
                boolean deliveryOutcomeIndicator = reportSMDeliveryStatusInd.getDeliveryOutcomeIndicator();
                SMDeliveryOutcome additionalSMDeliveryOutcome = reportSMDeliveryStatusInd.getAdditionalSMDeliveryOutcome();
                Integer additionalAbsentSubscriberDiagnosticSM = reportSMDeliveryStatusInd
                        .getAdditionalAbsentSubscriberDiagnosticSM();

                Assert.assertNotNull(msisdn);
                Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(msisdn.getAddress(), "111222333");
                Assert.assertNotNull(sca);
                Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(sca.getAddress(), "999000");
                Assert.assertEquals(sMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);

                Assert.assertNull(absentSubscriberDiagnosticSM);
                Assert.assertFalse(gprsSupportIndicator);
                Assert.assertFalse(deliveryOutcomeIndicator);
                Assert.assertNull(additionalSMDeliveryOutcome);
                Assert.assertNull(additionalAbsentSubscriberDiagnosticSM);
                Assert.assertNull(extensionContainer);

                ISDNAddressString storedMSISDN = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.network_specific_number, NumberingPlan.national, "111000111");

                try {
                    d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), storedMSISDN, null);
                } catch (MAPException e) {
                    this.error("Error while adding ReportSMDeliveryStatusResponse", e);
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error while sending the empty ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendReportSMDeliveryStatus2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + SendRoutingInfoForSMRequest TC-END + SendRoutingInfoForSMResponse + InformServiceCentreRequest
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendRoutingInfoForSM() throws Exception {
        // Action_Sms_SendRoutingInfoForSM

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
                super.onSendRoutingInfoForSMResponse(sendRoutingInfoForSMRespInd);
                IMSI imsi = sendRoutingInfoForSMRespInd.getIMSI();
                MAPExtensionContainer extensionContainer = sendRoutingInfoForSMRespInd.getExtensionContainer();
                LocationInfoWithLMSI locationInfoWithLMSI = sendRoutingInfoForSMRespInd.getLocationInfoWithLMSI();
                ISDNAddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
                LMSI lmsi = locationInfoWithLMSI.getLMSI();
                MAPExtensionContainer extensionContainer2 = locationInfoWithLMSI.getExtensionContainer();
                AdditionalNumber additionalNumber = locationInfoWithLMSI.getAdditionalNumber();

                Assert.assertNotNull(imsi);
                Assert.assertEquals(imsi.getData(), "25099777000");
                Assert.assertNotNull(networkNodeNumber);
                Assert.assertEquals(networkNodeNumber.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(networkNodeNumber.getAddress(), "111000111");
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer2));
                Assert.assertTrue(locationInfoWithLMSI.getGprsNodeIndicator());
                Assert.assertNotNull(lmsi);
                Assert.assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 75, 74, 73, 72 }));
                Assert.assertNotNull(additionalNumber);
                Assert.assertEquals(additionalNumber.getSGSNNumber().getAddressNature(), AddressNature.subscriber_number);
                Assert.assertEquals(additionalNumber.getSGSNNumber().getNumberingPlan(), NumberingPlan.private_plan);
                Assert.assertEquals(additionalNumber.getSGSNNumber().getAddress(), "000111000");
                Assert.assertNull(extensionContainer);

            }

            public void onInformServiceCentreRequest(InformServiceCentreRequest ind) {
                super.onInformServiceCentreRequest(ind);

                assertNull(ind.getExtensionContainer());
                assertTrue(ind.getStoredMSISDN().getAddress().equals("111222333"));
                assertFalse(ind.getMwStatus().getScAddressNotIncluded());
                assertTrue(ind.getMwStatus().getMnrfSet());
                assertFalse(ind.getMwStatus().getMcefSet());
                assertTrue(ind.getMwStatus().getMnrgSet());
                assertEquals((int) ind.getAbsentSubscriberDiagnosticSM(), 555);
                assertEquals((int) ind.getAdditionalAbsentSubscriberDiagnosticSM(), 444);

                ind.getMAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
                super.onSendRoutingInfoForSMRequest(sendRoutingInfoForSMInd);

                MAPDialogSms d = sendRoutingInfoForSMInd.getMAPDialog();

                ISDNAddressString msisdn = sendRoutingInfoForSMInd.getMsisdn();
                Boolean sm_RP_PRI = sendRoutingInfoForSMInd.getSm_RP_PRI();
                AddressString sca = sendRoutingInfoForSMInd.getServiceCentreAddress();
                MAPExtensionContainer extensionContainer = sendRoutingInfoForSMInd.getExtensionContainer();
                Boolean gprsSupportIndicator = sendRoutingInfoForSMInd.getGprsSupportIndicator();
                SM_RP_MTI sM_RP_MTI = sendRoutingInfoForSMInd.getSM_RP_MTI();
                SM_RP_SMEA sM_RP_SMEA = sendRoutingInfoForSMInd.getSM_RP_SMEA();

                Assert.assertNotNull(msisdn);
                Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(msisdn.getAddress(), "111222333");
                Assert.assertFalse(sm_RP_PRI);
                Assert.assertNotNull(sca);
                Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
                Assert.assertEquals(sca.getAddress(), "999000");
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
                Assert.assertTrue(gprsSupportIndicator);
                Assert.assertEquals(sM_RP_MTI, SM_RP_MTI.SMS_Status_Report);
                Assert.assertTrue(Arrays.equals(sM_RP_SMEA.getData(), new byte[] { 90, 91 }));

                IMSI imsi = this.mapParameterFactory.createIMSI("25099777000");
                ISDNAddressString networkNodeNumber = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.network_specific_number, NumberingPlan.national, "111000111");
                LMSI lmsi = this.mapParameterFactory.createLMSI(new byte[] { 75, 74, 73, 72 });
                ISDNAddressString sgsnAdditionalNumber = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.subscriber_number, NumberingPlan.private_plan, "000111000");
                AdditionalNumber additionalNumber = this.mapParameterFactory.createAdditionalNumberSgsnNumber(sgsnAdditionalNumber);
                LocationInfoWithLMSI locationInfoWithLMSI = this.mapParameterFactory.createLocationInfoWithLMSI(networkNodeNumber, lmsi,
                        MAPExtensionContainerTest.GetTestExtensionContainer(), true, additionalNumber);

                ISDNAddressString storedMSISDN = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.international_number, NumberingPlan.ISDN, "111222333");
                MWStatus mwStatus = this.mapParameterFactory.createMWStatus(false, true, false, true);
                Integer absentSubscriberDiagnosticSM = 555;
                Integer additionalAbsentSubscriberDiagnosticSM = 444;

                try {
                    d.addSendRoutingInfoForSMResponse(sendRoutingInfoForSMInd.getInvokeId(), imsi, locationInfoWithLMSI, null,
                            null, null);
                    d.addInformServiceCentreRequest(storedMSISDN, mwStatus, null, absentSubscriberDiagnosticSM,
                            additionalAbsentSubscriberDiagnosticSM);
                } catch (MAPException e) {
                    this.error("Error while adding SendRoutingInfoForSMResponse", e);
                    fail("Error while adding SendRoutingInfoForSMResponse");
                }

            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ForwardShortMessageResponse", e);
                    fail("Error while sending the empty ForwardShortMessageResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InformServiceCentreIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendRoutingInfoForSM();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * testMsgLength test
     */

    /**
     * Sending a short SMS message (20 bytes) This message is fit to the TC-BEGIN message with Dialog portion
     *
     * TC-BEGIN+MtForward(Short SMS) -> TC-END+MtForward(Response)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testAction_TestMsgLength_A() throws Exception {
        // Action_Sms_MoForwardSM

        Client_TestMsgLength client = new Client_TestMsgLength(stack1, this, peer1Address, peer2Address, 20); // 170

        Server_TestMsgLength server = new Server_TestMsgLength(this.stack2, this, peer2Address, peer1Address);

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendMoForwardShortMessageRequest_WithLengthChecking();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Sending a long SMS message (170 bytes) This message is not fit to the TC-BEGIN message with Dialog portion In the
     * TC-BEGIN message only Dialog portion is sent, MtForward message is sent in the second (TC-CONTINUE) message
     *
     * TC-BEGIN -> TC-CONTINUE -> TC-CONTINUE+MtForward(Long SMS) -> TC-END+MtForward(Response)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testAction_TestMsgLength_B() throws Exception {
        // Action_Sms_MoForwardSM

        Client_TestMsgLength client = new Client_TestMsgLength(stack1, this, peer1Address, peer2Address, 170);

        Server_TestMsgLength server = new Server_TestMsgLength(this.stack2, this, peer2Address, peer1Address);

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();

        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendMoForwardShortMessageRequest_WithLengthChecking();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    private class Client_TestMsgLength extends Client {

        protected boolean messageIsSent = false;
        protected int dataLength;

        public Client_TestMsgLength(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress,
                SccpAddress remoteAddress, int dataLength) {
            super(mapStack, runningTestCase, thisAddress, remoteAddress);

            this.dataLength = dataLength;
        }

        public void sendMoForwardShortMessageRequest_WithLengthChecking() throws Exception {
            this.mapProvider.getMAPServiceSms().acivate();

            MAPApplicationContext appCnt = null;
            appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                    MAPApplicationContextVersion.version3);
            AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                    NumberingPlan.ISDN, "31628968300");
            AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                    NumberingPlan.land_mobile, "204208300008002");

            clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                    this.remoteAddress, destReference);
            clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

            sendMoForwardShortMessageRequest_WithLengthChecking_2(this.dataLength, clientDialogSms);

        }

        protected void sendMoForwardShortMessageRequest_WithLengthChecking_2(int dataLength, MAPDialogSms dlg)
                throws MAPException {
            SmsSignalInfo sm_RP_UI;
            sm_RP_UI = new SmsSignalInfoImpl(new byte[dataLength], null);
            Arrays.fill(sm_RP_UI.getData(), (byte) 11);

            IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
            SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
            ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                    NumberingPlan.ISDN, "111222333");
            SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
            IMSI imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

            Long invokeId = dlg.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi2);

            int maxMsgLen = dlg.getMaxUserDataLength();
            int curMsgLen = dlg.getMessageUserDataLengthOnSend();
            if (curMsgLen > maxMsgLen)
                dlg.cancelInvocation(invokeId);
            else {
                this.observerdEvents
                        .add(TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, sequence++));
                messageIsSent = true;
            }

            dlg.send();
        }

        @Override
        public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
            super.onMoForwardShortMessageResponse(moForwSmRespInd);
            SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
            MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

            Assert.assertNotNull(sm_RP_UI);
            Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
            Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        }

        @Override
        public void onDialogDelimiter(MAPDialog mapDialog) {
            super.onDialogDelimiter(mapDialog);

            if (!this.messageIsSent) {
                try {
                    sendMoForwardShortMessageRequest_WithLengthChecking_2(this.dataLength, (MAPDialogSms) mapDialog);
                } catch (MAPException e) {
                    this.error("Error while trying invoke sendMoForwardShortMessageRequest_WithLengthChecking_2", e);
                    fail("Erro while trying to invoke sendMoForwardShortMessageRequest_WithLengthChecking_2");
                }
            }
        }

        @Override
        public void onDialogClose(MAPDialog mapDialog) {
            super.onDialogClose(mapDialog);
        }
    };

    private class Server_TestMsgLength extends Server {
        Server_TestMsgLength(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress,
                SccpAddress remoteAddress) {
            super(mapStack, runningTestCase, thisAddress, remoteAddress);
            // TODO Auto-generated constructor stub
        }

        protected boolean messageIsReceived = false;

        @Override
        public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
            super.onMoForwardShortMessageRequest(moForwSmInd);
            MAPDialogSms d = moForwSmInd.getMAPDialog();

            SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
            SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
            SmsSignalInfo sm_RP_UI = moForwSmInd.getSM_RP_UI();
            MAPExtensionContainer extensionContainer = moForwSmInd.getExtensionContainer();
            IMSI imsi2 = moForwSmInd.getIMSI();

            Assert.assertNotNull(sm_RP_DA);
            Assert.assertNotNull(sm_RP_DA.getIMSI());
            Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
            Assert.assertNotNull(sm_RP_OA);
            Assert.assertNotNull(sm_RP_OA.getMsisdn());
            Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
            Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
            Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
            Assert.assertNotNull(sm_RP_UI);

            // Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
            Assert.assertNotNull(imsi2);
            Assert.assertEquals(imsi2.getData(), "25007123456789");

            SmsSignalInfo sm_RP_UI2 = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
            try {
                d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2,
                        MAPExtensionContainerTest.GetTestExtensionContainer());
            } catch (MAPException e) {
                this.error("Error while adding MoForwardShortMessageResponse", e);
                fail("Error while adding MoForwardShortMessageResponse");
            }

            messageIsReceived = true;
        }

        @Override
        public void onDialogDelimiter(MAPDialog mapDialog) {
            super.onDialogDelimiter(mapDialog);
            try {
                if (messageIsReceived) {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null,
                            sequence++));
                    mapDialog.close(false);
                } else
                    mapDialog.send();
            } catch (MAPException e) {
                this.error("Error while sending the empty ForwardShortMessageResponse", e);
                fail("Error while sending the empty ForwardShortMessageResponse");
            }
        }
    };

    /**
     * TC-BEGIN + sendAuthenticationInfoRequest_V3 TC-END + sendAuthenticationInfoResponse_V3
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendAuthenticationInfo_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
                super.onSendAuthenticationInfoResponse(ind);

                AuthenticationSetList asl = ind.getAuthenticationSetList();
                AuthenticationTriplet at = asl.getTripletList().getAuthenticationTriplets().get(0);

                Assert.assertEquals(ind.getMapProtocolVersion(), 3);
                Assert.assertEquals(asl.getMapProtocolVersion(), 3);
                Assert.assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
                Assert.assertTrue(Arrays.equals(at.getRand(), TripletListTest.getRandData()));
                Assert.assertTrue(Arrays.equals(at.getSres(), TripletListTest.getSresData()));
                Assert.assertTrue(Arrays.equals(at.getKc(), TripletListTest.getKcData()));
                Assert.assertNull(asl.getQuintupletList());
                Assert.assertNull(ind.getEpsAuthenticationSetList());
                Assert.assertNull(ind.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
                super.onSendAuthenticationInfoRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();

                Assert.assertEquals(ind.getMapProtocolVersion(), 3);
                Assert.assertTrue(imsi.getData().equals("4567890"));
                Assert.assertEquals(ind.getNumberOfRequestedVectors(), 3);
                Assert.assertTrue(ind.getSegmentationProhibited());
                Assert.assertTrue(ind.getImmediateResponsePreferred());
                Assert.assertNull(ind.getReSynchronisationInfo());
                Assert.assertNull(ind.getExtensionContainer());
                Assert.assertEquals(ind.getRequestingNodeType(), RequestingNodeType.sgsn);
                Assert.assertNull(ind.getRequestingPlmnId());
                Assert.assertEquals((int) ind.getNumberOfRequestedAdditionalVectors(), 5);
                Assert.assertFalse(ind.getAdditionalVectorsAreForEPS());

                ArrayList<AuthenticationTriplet> authenticationTriplets = new ArrayList<AuthenticationTriplet>();
                AuthenticationTriplet at = this.mapParameterFactory.createAuthenticationTriplet(TripletListTest.getRandData(),
                        TripletListTest.getSresData(), TripletListTest.getKcData());
                authenticationTriplets.add(at);
                TripletList tripletList = this.mapParameterFactory.createTripletList(authenticationTriplets);
                AuthenticationSetList asl = this.mapParameterFactory.createAuthenticationSetList(tripletList);

                try {
                    d.addSendAuthenticationInfoResponse(ind.getInvokeId(), asl, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding SendAuthenticationInfoResponse", e);
                    fail("Error while adding SendAuthenticationInfoResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V3, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty SendAuthenticationInfoResp_V3", e);
                    fail("Error while sending the empty SendAuthenticationInfoResp_V3");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V3, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfoResp_V3, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfo_V3, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V3, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendAuthenticationInfo_V3();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + sendAuthenticationInfoRequest_V2 TC-END + sendAuthenticationInfoResponse_V2
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendAuthenticationInfo_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
                super.onSendAuthenticationInfoResponse(ind);

                AuthenticationSetList asl = ind.getAuthenticationSetList();
                AuthenticationTriplet at = asl.getTripletList().getAuthenticationTriplets().get(0);

                Assert.assertEquals(ind.getMapProtocolVersion(), 2);
                Assert.assertEquals(asl.getMapProtocolVersion(), 2);
                Assert.assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
                Assert.assertTrue(Arrays.equals(at.getRand(), TripletListTest.getRandData()));
                Assert.assertTrue(Arrays.equals(at.getSres(), TripletListTest.getSresData()));
                Assert.assertTrue(Arrays.equals(at.getKc(), TripletListTest.getKcData()));
                Assert.assertNull(asl.getQuintupletList());
                Assert.assertNull(ind.getEpsAuthenticationSetList());
                Assert.assertNull(ind.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
                super.onSendAuthenticationInfoRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();

                Assert.assertEquals(ind.getMapProtocolVersion(), 2);
                Assert.assertTrue(imsi.getData().equals("456789000"));
                Assert.assertEquals(ind.getNumberOfRequestedVectors(), 0);
                Assert.assertFalse(ind.getSegmentationProhibited());
                Assert.assertFalse(ind.getImmediateResponsePreferred());
                Assert.assertNull(ind.getReSynchronisationInfo());
                Assert.assertNull(ind.getExtensionContainer());
                Assert.assertNull(ind.getRequestingNodeType());
                Assert.assertNull(ind.getRequestingPlmnId());
                Assert.assertNull(ind.getNumberOfRequestedAdditionalVectors());
                Assert.assertFalse(ind.getAdditionalVectorsAreForEPS());

                ArrayList<AuthenticationTriplet> authenticationTriplets = new ArrayList<AuthenticationTriplet>();
                AuthenticationTriplet at = this.mapParameterFactory.createAuthenticationTriplet(TripletListTest.getRandData(),
                        TripletListTest.getSresData(), TripletListTest.getKcData());
                authenticationTriplets.add(at);
                TripletList tripletList = this.mapParameterFactory.createTripletList(authenticationTriplets);
                AuthenticationSetList asl = this.mapParameterFactory.createAuthenticationSetList(tripletList);

                try {
                    d.addSendAuthenticationInfoResponse(ind.getInvokeId(), asl, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding SendAuthenticationInfoResponse", e);
                    fail("Error while adding SendAuthenticationInfoResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V2, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty SendAuthenticationInfoResp_V2", e);
                    fail("Error while sending the empty SendAuthenticationInfoResp_V2");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V2, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfoResp_V2, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfo_V2, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V2, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendAuthenticationInfo_V2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + updateLocation TC-END + updateLocationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testUpdateLocation() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onUpdateLocationResponse(UpdateLocationResponse ind) {
                super.onUpdateLocationResponse(ind);

                ISDNAddressString hlrNumber = ind.getHlrNumber();

                Assert.assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertTrue(hlrNumber.getAddress().equals("765765765"));
                Assert.assertNull(ind.getExtensionContainer());
                Assert.assertTrue(ind.getAddCapability());
                Assert.assertFalse(ind.getPagingAreaCapability());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onUpdateLocationRequest(UpdateLocationRequest ind) {
                super.onUpdateLocationRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();
                ISDNAddressString mscNumber = ind.getMscNumber();
                ISDNAddressString vlrNumber = ind.getVlrNumber();
                LMSI lmsi = ind.getLmsi();
                ADDInfo addInfo = ind.getADDInfo();

                Assert.assertEquals(ind.getMapProtocolVersion(), 3);
                Assert.assertTrue(imsi.getData().equals("45670000"));
                Assert.assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertTrue(mscNumber.getAddress().equals("8222333444"));
                Assert.assertNull(ind.getRoamingNumber());
                Assert.assertEquals(vlrNumber.getAddressNature(), AddressNature.network_specific_number);
                Assert.assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertTrue(vlrNumber.getAddress().equals("700000111"));
                Assert.assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 1, 2, 3, 4 }));
                Assert.assertNull(ind.getExtensionContainer());
                Assert.assertNull(ind.getVlrCapability());
                Assert.assertTrue(ind.getInformPreviousNetworkEntity());
                Assert.assertFalse(ind.getCsLCSNotSupportedByUE());
                Assert.assertNull(ind.getVGmlcAddress());
                Assert.assertTrue(addInfo.getImeisv().getIMEI().equals("987654321098765"));
                Assert.assertNull(ind.getPagingArea());
                Assert.assertFalse(ind.getSkipSubscriberDataUpdate());
                Assert.assertTrue(ind.getRestorationIndicator());

                ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.international_number, NumberingPlan.ISDN, "765765765");

                try {
                    d.addUpdateLocationResponse(ind.getInvokeId(), hlrNumber, null, true, false);
                } catch (MAPException e) {
                    this.error("Error while adding UpdateLocationResponse", e);
                    fail("Error while adding UpdateLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateLocationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty UpdateLocationResponse", e);
                    fail("Error while sending the empty UpdateLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.UpdateLocation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UpdateLocationResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UpdateLocation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UpdateLocationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendUpdateLocation();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
<code>
TC-BEGIN + anyTimeInterrogationRequest
TC-END + anyTimeInterrogationResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testAnyTimeInterrogation() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse ind) {
                super.onAnyTimeInterrogationResponse(ind);

                SubscriberInfo si = ind.getSubscriberInfo();
                SubscriberState ss = si.getSubscriberState();
                Assert.assertEquals(ss.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
                Assert.assertNull(ss.getNotReachableReason());
                Assert.assertNull(si.getLocationInformation());
                Assert.assertNull(si.getExtensionContainer());
                Assert.assertNull(si.getGPRSMSClass());
                Assert.assertNull(si.getIMEI());
                Assert.assertNull(si.getLocationInformationGPRS());
                Assert.assertNull(si.getMNPInfoRes());
                Assert.assertNull(si.getMSClassmark2());
                Assert.assertNull(si.getPSSubscriberState());
                Assert.assertNull(ind.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest ind) {
                super.onAnyTimeInterrogationRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();
                SubscriberIdentity subscriberIdentity = ind.getSubscriberIdentity();
                Assert.assertTrue(subscriberIdentity.getIMSI().getData().equals("33334444"));
                RequestedInfo requestedInfo = ind.getRequestedInfo();
                Assert.assertTrue(requestedInfo.getLocationInformation());
                Assert.assertTrue(requestedInfo.getSubscriberState());
                Assert.assertFalse(requestedInfo.getCurrentLocation());
                Assert.assertNull(requestedInfo.getRequestedDomain());
                Assert.assertFalse(requestedInfo.getImei());
                Assert.assertFalse(requestedInfo.getMsClassmark());
                ISDNAddressString gsmSCFAddress = ind.getGsmSCFAddress();
                Assert.assertTrue(gsmSCFAddress.getAddress().equals("11112222"));
                Assert.assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);

                SubscriberState ss = this.mapParameterFactory.createSubscriberState(SubscriberStateChoice.camelBusy, null);
                SubscriberInfo si = this.mapParameterFactory.createSubscriberInfo(null, ss, null, null, null, null, null, null,
                        null);

                try {
                    d.addAnyTimeInterrogationResponse(ind.getInvokeId(), si, null);
                } catch (MAPException e) {
                    this.error("Error while adding AnyTimeInterrogationResponse", e);
                    fail("Error while adding AnyTimeInterrogationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeInterrogationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty AnyTimeInterrogationResponse", e);
                    fail("Error while sending the empty AnyTimeInterrogationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AnyTimeInterrogation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogation, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.AnyTimeInterrogationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendAnyTimeInterrogation();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     <code>
     TC-BEGIN + anyTimeSubscriptionInterrogationRequest
     TC-END + anyTimeSubscriptionInterrogationResponse
     </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testAyTimeSubscriptionInterrogation() throws Exception {
        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse ind) {
                super.onAnyTimeSubscriptionInterrogationResponse(ind);

                OCSI ocsi = ind.getCamelSubscriptionInfo().getOCsi();
                Assert.assertNotNull(ocsi.getOBcsmCamelTDPDataList());
                Assert.assertEquals(ocsi.getOBcsmCamelTDPDataList().size(), 1);

                OBcsmCamelTDPData tdpData = ocsi.getOBcsmCamelTDPDataList().get(0);
                Assert.assertEquals(tdpData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
                Assert.assertEquals(tdpData.getServiceKey(), 3);
                Assert.assertEquals(tdpData.getDefaultCallHandling(), DefaultCallHandling.continueCall);

                ISDNAddressString gsmSCFAddress = tdpData.getGsmSCFAddress();
                Assert.assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
                Assert.assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
                Assert.assertEquals(gsmSCFAddress.getAddress(), "123456789");

                SupportedCamelPhases supportedCamelPhasesVlr = ind.getsupportedVlrCamelPhases();
                Assert.assertTrue(supportedCamelPhasesVlr.getPhase1Supported());
                Assert.assertTrue(supportedCamelPhasesVlr.getPhase2Supported());
                Assert.assertTrue(supportedCamelPhasesVlr.getPhase3Supported());
                Assert.assertTrue(supportedCamelPhasesVlr.getPhase4Supported());

                OfferedCamel4CSIs offeredCamel4CSIsVlr = ind.getOfferedCamel4CSIsInVlr();
                Assert.assertTrue(offeredCamel4CSIsVlr.getOCsi());
                Assert.assertFalse(offeredCamel4CSIsVlr.getDCsi());
                Assert.assertFalse(offeredCamel4CSIsVlr.getVtCsi());
                Assert.assertFalse(offeredCamel4CSIsVlr.getTCsi());
                Assert.assertFalse(offeredCamel4CSIsVlr.getMtSmsCsi());
                Assert.assertFalse(offeredCamel4CSIsVlr.getMgCsi());
                Assert.assertFalse(offeredCamel4CSIsVlr.getPsiEnhancements());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest ind) {
                super.onAnyTimeSubscriptionInterrogationRequest(ind);

                SubscriberIdentity subscriberIdentity = ind.getSubscriberIdentity();
                Assert.assertEquals(subscriberIdentity.getMSISDN().getAddress(), "111222333");

                ISDNAddressString gsmSCFAddressReq = ind.getGsmScfAddress();
                Assert.assertEquals(gsmSCFAddressReq.getAddress(), "1234567890");

                RequestedSubscriptionInfo requestedSubscriptionInfo = ind.getRequestedSubscriptionInfo();
                Assert.assertNull(requestedSubscriptionInfo.getRequestedSSInfo());
                Assert.assertFalse(requestedSubscriptionInfo.getOdb());
                Assert.assertEquals(requestedSubscriptionInfo.getRequestedCAMELSubscriptionInfo(),
                        RequestedCAMELSubscriptionInfo.oCSI);
                Assert.assertTrue(requestedSubscriptionInfo.getSupportedVlrCamelPhases());
                Assert.assertFalse(requestedSubscriptionInfo.getSupportedSgsnCamelPhases());
                Assert.assertNull(requestedSubscriptionInfo.getExtensionContainer());
                Assert.assertEquals(requestedSubscriptionInfo.getAdditionalRequestedCamelSubscriptionInfo(),
                        AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI);
                Assert.assertFalse(requestedSubscriptionInfo.getMsisdnBsList());
                Assert.assertTrue(requestedSubscriptionInfo.getCsgSubscriptionDataRequested());
                Assert.assertFalse(requestedSubscriptionInfo.getCwInfo());
                Assert.assertFalse(requestedSubscriptionInfo.getClipInfo());
                Assert.assertFalse(requestedSubscriptionInfo.getClirInfo());
                Assert.assertFalse(requestedSubscriptionInfo.getHoldInfo());
                Assert.assertFalse(requestedSubscriptionInfo.getEctInfo());

                // send response
                ISDNAddressString gsmSCFAddress = mapProvider.getMAPParameterFactory()
                        .createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "123456789");
                final OBcsmCamelTDPData tdpData = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3,
                        gsmSCFAddress, DefaultCallHandling.continueCall, null);

                OCSI ocsi = new OCSIImpl(new ArrayList<OBcsmCamelTDPData>() {{add(tdpData);}}, null, null, false, true);
                CAMELSubscriptionInfo camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(ocsi, null, null, null, null,
                        null, null, false, false, null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null);
                SupportedCamelPhases supportedCamelPhasesVlr = new SupportedCamelPhasesImpl(true, true, true, true);
                OfferedCamel4CSIs offeredCamel4CSIsVlr = new OfferedCamel4CSIsImpl(true, false, false, false, false, false, false);

                MAPDialogMobility d = ind.getMAPDialog();
                try {
                    d.addAnyTimeSubscriptionInterrogationResponse(ind.getInvokeId(), null, null, null, camelSubscriptionInfo,
                            supportedCamelPhasesVlr, null, null, offeredCamel4CSIsVlr, null, null, null, null, null, null,
                            null ,null);
                } catch (MAPException e) {
                    this.error("Error while adding AnyTimeInterrogationResponse", e);
                    fail("Error while adding AnyTimeInterrogationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeSubscriptionInterrogationRes,
                            null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty AnyTimeSubscriptionInterrogationResponse", e);
                    fail("Error while sending the empty AnyTimeSubscriptionInterrogationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AnyTimeSubscriptionInterrogation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AnyTimeSubscriptionInterrogationRes, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AnyTimeSubscriptionInterrogation, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.AnyTimeSubscriptionInterrogationRes, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendAnyTimeSubscriptionInterrogation();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
<code>
TC-BEGIN + provideSubscriberInfoRequest
TC-END + provideSubscriberInfoResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testProvideSubscriberInfo() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse ind) {
                super.onProvideSubscriberInfoResponse(ind);

                SubscriberInfo si = ind.getSubscriberInfo();
                SubscriberState ss = si.getSubscriberState();
                Assert.assertEquals(ss.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
                Assert.assertNull(ss.getNotReachableReason());
                Assert.assertNull(si.getExtensionContainer());
                Assert.assertNull(si.getGPRSMSClass());
                Assert.assertNull(si.getIMEI());
                Assert.assertNull(si.getLocationInformationGPRS());
                Assert.assertNull(si.getMNPInfoRes());
                Assert.assertNull(si.getMSClassmark2());
                Assert.assertNull(si.getPSSubscriberState());
                Assert.assertNull(ind.getExtensionContainer());

                LocationInformation locationInformation = si.getLocationInformation();
                assertEquals((int) locationInformation.getAgeOfLocationInformation(), 10);
                assertTrue(Math.abs(locationInformation.getGeographicalInformation().getLatitude() - 30) < 0.01);
                assertTrue(Math.abs(locationInformation.getGeographicalInformation().getLongitude() - 60) < 0.01);
                assertTrue(Math.abs(locationInformation.getGeographicalInformation().getUncertainty() - 10) < 1);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest ind) {
                super.onProvideSubscriberInfoRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();
                assertEquals(ind.getImsi().getData(), "33334444");
                RequestedInfo requestedInfo = ind.getRequestedInfo();
                Assert.assertTrue(requestedInfo.getLocationInformation());
                Assert.assertTrue(requestedInfo.getSubscriberState());
                Assert.assertFalse(requestedInfo.getCurrentLocation());
                Assert.assertNull(requestedInfo.getRequestedDomain());
                Assert.assertFalse(requestedInfo.getImei());
                Assert.assertFalse(requestedInfo.getMsClassmark());

                try {
                    GeographicalInformation geographicalInformation = this.mapParameterFactory.createGeographicalInformation(30, 60, 10);
                    // latitude, longitude, uncertainty
                    LocationInformation locationInformation = this.mapParameterFactory.createLocationInformation(10, geographicalInformation, null, null, null,
                            null, null, null, null, false, false, null, null);
                    SubscriberState ss = this.mapParameterFactory.createSubscriberState(SubscriberStateChoice.camelBusy, null);
                    SubscriberInfo si = this.mapParameterFactory.createSubscriberInfo(locationInformation, ss, null, null, null, null, null, null,
                            null);

                    d.addProvideSubscriberInfoResponse(ind.getInvokeId(), si, null);
                } catch (MAPException e) {
                    this.error("Error while adding ProvideSubscriberInfoResponse", e);
                    fail("Error while adding ProvideSubscriberInfoResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberInfoResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ProvideSubscriberInfoResponse", e);
                    fail("Error while sending the empty ProvideSubscriberInfoResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProvideSubscriberInfo, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberInfoResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberInfo, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProvideSubscriberInfoResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendProvideSubscriberInfo();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + provideSubscriberLocationRequest TC-END + provideSubscriberLocationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testProvideSubscriberLocation() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse ind) {
                super.onProvideSubscriberLocationResponse(ind);

//                Assert.assertTrue(Arrays.equals(ind.getLocationEstimate().getData(), new byte[] { 50 }));
                Assert.assertEquals((int) ind.getAgeOfLocationEstimate(), 6);

                Assert.assertTrue(ind.getLocationEstimate().getLatitude() - (-31) < 0.001);
                Assert.assertTrue(ind.getLocationEstimate().getLongitude() - (-53) < 0.001);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest ind) {
                super.onProvideSubscriberLocationRequest(ind);

                MAPDialogLsm d = ind.getMAPDialog();

                Assert.assertEquals(ind.getLocationType().getLocationEstimateType(),
                        LocationEstimateType.cancelDeferredLocation);
                Assert.assertTrue(ind.getMlcNumber().getAddress().equals("11112222"));

                try {
                    ExtGeographicalInformation locationEstimate = this.mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(-31, -53);
                    d.addProvideSubscriberLocationResponse(ind.getInvokeId(), locationEstimate, null, null, 6, null, null,
                            false, null, false, null, null, false, null, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding ProvideSubscriberLocationResponse", e);
                    fail("Error while adding ProvideSubscriberLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberLocationResp, null,
                            sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending close()", e);
                    fail("Error while sending close()");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProvideSubscriberLocation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberLocationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberLocation, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProvideSubscriberLocationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

//        this.saveTrafficInFile();

        client.sendProvideSubscriberLocation();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + subscriberLocationReportRequest TC-END + subscriberLocationReportResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSubscriberLocationReport() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse ind) {
                super.onSubscriberLocationReportResponse(ind);

                Assert.assertTrue(ind.getNaESRD().getAddress().equals("11114444"));
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest ind) {
                super.onSubscriberLocationReportRequest(ind);

                MAPDialogLsm d = ind.getMAPDialog();

                Assert.assertEquals(ind.getLCSEvent(), LCSEvent.emergencyCallOrigination);
                Assert.assertEquals(ind.getLCSClientID().getLCSClientType(), LCSClientType.plmnOperatorServices);
                Assert.assertTrue(ind.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("11113333"));

                ISDNAddressString naEsrd = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                        NumberingPlan.ISDN, "11114444");

                try {
                    d.addSubscriberLocationReportResponse(ind.getInvokeId(), naEsrd, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding SubscriberLocationReportResponse", e);
                    fail("Error while adding SubscriberLocationReportResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent
                            .createSentEvent(EventType.SubscriberLocationReportResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending close()", e);
                    fail("Error while sending close()");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SubscriberLocationReport, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SubscriberLocationReportResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SubscriberLocationReport, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SubscriberLocationReportResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSubscriberLocationReport();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + sendRoutingInforForLCSRequest TC-END + sendRoutingInforForLCSResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendRoutingInforForLCS() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse ind) {
                super.onSendRoutingInfoForLCSResponse(ind);

                Assert.assertTrue(ind.getTargetMS().getIMSI().getData().equals("6666644444"));
                Assert.assertTrue(ind.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("11114444"));
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest ind) {
                super.onSendRoutingInfoForLCSRequest(ind);

                MAPDialogLsm d = ind.getMAPDialog();

                Assert.assertTrue(ind.getMLCNumber().getAddress().equals("11112222"));
                Assert.assertTrue(ind.getTargetMS().getIMSI().getData().equals("5555544444"));

                IMSI imsi = this.mapParameterFactory.createIMSI("6666644444");
                SubscriberIdentity targetMS = this.mapParameterFactory.createSubscriberIdentity(imsi);
                ISDNAddressString networkNodeNumber = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.international_number, NumberingPlan.ISDN, "11114444");
                ;
                LCSLocationInfo lcsLocationInfo = this.mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null,
                        false, null, null, null, null, null);

                try {
                    d.addSendRoutingInfoForLCSResponse(ind.getInvokeId(), targetMS, lcsLocationInfo, null, null, null, null,
                            null);
                } catch (MAPException e) {
                    this.error("Error while adding SendRoutingInfoForLCSResponse", e);
                    fail("Error while adding SendRoutingInfoForLCSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForLCSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending close()", e);
                    fail("Error while sending close()");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInfoForLCS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForLCSResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForLCS, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInfoForLCSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendRoutingInforForLCS();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + checkImeiRequest TC-END + checkImeiResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testCheckImei() throws Exception {
        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onCheckImeiResponse(CheckImeiResponse ind) {
                super.onCheckImeiResponse(ind);

                Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
                Assert.assertTrue(ind.getBmuef().getUESBI_IuA().getData().get(0));
                Assert.assertFalse(ind.getBmuef().getUESBI_IuB().getData().get(0));
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
            };
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onCheckImeiRequest(CheckImeiRequest ind) {
                super.onCheckImeiRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                Assert.assertTrue(ind.getIMEI().getIMEI().equals("111111112222222"));
                Assert.assertTrue(ind.getRequestedEquipmentInfo().getEquipmentStatus());
                Assert.assertFalse(ind.getRequestedEquipmentInfo().getBmuef());
                Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));

                BitSetStrictLength bsUESBIIuA = new BitSetStrictLength(1);
                bsUESBIIuA.set(0);
                BitSetStrictLength bsUESBIIuB = new BitSetStrictLength(1);

                UESBIIuA uesbiIuA = this.mapParameterFactory.createUESBIIuA(bsUESBIIuA);
                UESBIIuB uesbiIuB = this.mapParameterFactory.createUESBIIuB(bsUESBIIuB);
                UESBIIu bmuef = this.mapParameterFactory.createUESBIIu(uesbiIuA, uesbiIuB);

                MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
                try {
                    d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, bmuef, extensionContainer);
                } catch (MAPException e) {
                    this.error("Error while adding CheckImeiResponse", e);
                    fail("Error while adding CheckImeiResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending close()", e);
                    fail("Error while sending close()");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCheckImei();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * TC-BEGIN + checkImeiRequest_V2 TC-END + checkImeiResponse_V2
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testCheckImei_V2() throws Exception {
        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onCheckImeiResponse(CheckImeiResponse ind) {
                super.onCheckImeiResponse(ind);

                Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
                Assert.assertNull(ind.getBmuef());
                Assert.assertNull(ind.getExtensionContainer());
            };
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onCheckImeiRequest(CheckImeiRequest ind) {
                super.onCheckImeiRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                Assert.assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
                Assert.assertNull(ind.getRequestedEquipmentInfo());
                Assert.assertNull(ind.getExtensionContainer());

                try {
                    d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding CheckImeiResponse_V2", e);
                    fail("Error while adding CheckImeiResponse_V2");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending close()", e);
                    fail("Error while sending close()");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCheckImei_V2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * TC-BEGIN + checkImeiRequest_V2_Huawei_extention_test TC-END + checkImeiResponse_V2
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testCheckImei_Huawei_V2() throws Exception {
        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onCheckImeiResponse(CheckImeiResponse ind) {
                super.onCheckImeiResponse(ind);

                Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
                Assert.assertNull(ind.getBmuef());
                Assert.assertNull(ind.getExtensionContainer());
            };
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onCheckImeiRequest(CheckImeiRequest ind) {
                super.onCheckImeiRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                Assert.assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
                Assert.assertNull(ind.getRequestedEquipmentInfo());
                Assert.assertNull(ind.getExtensionContainer());
                CheckImeiRequestImpl impl = (CheckImeiRequestImpl) ind;
                Assert.assertTrue(impl.getIMSI().getData().equals("999999998888888"));

                try {
                    d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding CheckImeiResponse_V2", e);
                    fail("Error while adding CheckImeiResponse_V2");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending close()", e);
                    fail("Error while sending close()");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCheckImei_Huawei_V2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Some not real test for testing: - sendDelayed() / closeDelayed() - getTCAPMessageType() - saving origReferense,
     * destReference, extContainer in MAPDialog TC-BEGIN + extContainer + checkImeiRequest + checkImeiRequest TC-CONTINUE +
     * sendDelayed(checkImeiResponse) + sendDelayed(checkImeiResponse) TC-END + closeDelayed(checkImeiResponse) +
     * sendDelayed(checkImeiResponse)
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDelayedSendClose() throws Exception {
        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            int dialogStep = 0;

            @Override
            public void onCheckImeiResponse(CheckImeiResponse ind) {
                super.onCheckImeiResponse(ind);

                Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
                Assert.assertNull(ind.getBmuef());
                Assert.assertNull(ind.getExtensionContainer());

                MAPDialogMobility d = ind.getMAPDialog();
                assertEquals(d.getTCAPMessageType(), MessageType.Continue);

                try {
                    IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");
                    d.addCheckImeiRequest(imei, null, null);
                    if (dialogStep == 0) {
                        d.closeDelayed(false);
                    } else {
                        d.sendDelayed();
                    }
                    dialogStep++;
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
                } catch (MAPException e) {
                    this.error("Error while adding CheckImeiRequest/sending", e);
                    fail("Error while adding CheckImeiRequest/sending");
                }
            };
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            int dialogStep = 0;

            public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                    MAPExtensionContainer extensionContainer) {
                super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);

                assertEquals(origReference.getAddressNature(), AddressNature.international_number);
                assertEquals(origReference.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(origReference.getAddress().equals("11335577"));

                assertEquals(destReference.getAddressNature(), AddressNature.international_number);
                assertEquals(destReference.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(destReference.getAddress().equals("22446688"));

                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Begin);
            }

            @Override
            public void onCheckImeiRequest(CheckImeiRequest ind) {
                super.onCheckImeiRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                Assert.assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
                Assert.assertNull(ind.getRequestedEquipmentInfo());
                Assert.assertNull(ind.getExtensionContainer());

                assertEquals(d.getReceivedOrigReference().getAddressNature(), AddressNature.international_number);
                assertEquals(d.getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(d.getReceivedOrigReference().getAddress().equals("11335577"));

                assertEquals(d.getReceivedDestReference().getAddressNature(), AddressNature.international_number);
                assertEquals(d.getReceivedDestReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(d.getReceivedDestReference().getAddress().equals("22446688"));

                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(d.getReceivedExtensionContainer()));

                if (dialogStep < 2) {
                    assertEquals(d.getTCAPMessageType(), MessageType.Begin);

                    try {
                        d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, null, null);
                        d.sendDelayed();
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
                    } catch (MAPException e) {
                        this.error("Error while adding CheckImeiResponse/sending", e);
                        fail("Error while adding CheckImeiResponse/sending");
                    }
                } else {
                    assertEquals(d.getTCAPMessageType(), MessageType.End);
                }

                dialogStep++;
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCheckImei_ForDelayedTest();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Some not real test for testing: - closeDelayed(true) - getTCAPMessageType() TC-BEGIN + checkImeiRequest +
     * checkImeiRequest TC-END + Prearranged + [checkImeiResponse + checkImeiResponse]
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDelayedClosePrearranged() throws Exception {
        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            int dialogStep = 0;
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            int dialogStep = 0;

            public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                    MAPExtensionContainer extensionContainer) {
                super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);

                assertNull(origReference);
                assertNull(destReference);
                assertNull(extensionContainer);

                assertEquals(mapDialog.getTCAPMessageType(), MessageType.Begin);
            }

            @Override
            public void onCheckImeiRequest(CheckImeiRequest ind) {
                super.onCheckImeiRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
                assertNull(ind.getRequestedEquipmentInfo());
                assertNull(ind.getExtensionContainer());

                assertNull(d.getReceivedOrigReference());
                assertNull(d.getReceivedDestReference());
                assertNull(d.getReceivedExtensionContainer());

                assertEquals(d.getTCAPMessageType(), MessageType.Begin);

                try {
                    d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, null, null);
                    if (dialogStep == 0)
                        d.sendDelayed();
                    else
                        d.closeDelayed(true);
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
                } catch (MAPException e) {
                    this.error("Error while adding CheckImeiResponse/sending", e);
                    fail("Error while adding CheckImeiResponse/sending");
                }

                dialogStep++;
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCheckImei_ForDelayedTest2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * TC-BEGIN + cancelLocation MAV V3 TC-END + cancleLocationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testCancelLocation() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onCancelLocationResponse(CancelLocationResponse ind) {
                super.onCancelLocationResponse(ind);
                MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onCancelLocationRequest(CancelLocationRequest ind) {
                super.onCancelLocationRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();
                IMSIWithLMSI imsiWithLmsi = ind.getImsiWithLmsi();
                CancellationType cancellationType = ind.getCancellationType();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                TypeOfUpdate typeOfUpdate = ind.getTypeOfUpdate();
                boolean mtrfSupportedAndAuthorized = ind.getMtrfSupportedAndAuthorized();
                boolean mtrfSupportedAndNotAuthorized = ind.getMtrfSupportedAndNotAuthorized();
                ISDNAddressString newMSCNumber = ind.getNewMSCNumber();
                ISDNAddressString newVLRNumber = ind.getNewVLRNumber();
                LMSI newLmsi = ind.getNewLmsi();
                long mapProtocolVersion = ind.getMapProtocolVersion();

                assertTrue(imsi.getData().equals("1111122222"));
                assertNull(imsiWithLmsi);
                assertEquals(cancellationType.getCode(), 1);
                assertNotNull(extensionContainer);
                MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer);
                assertEquals(typeOfUpdate.getCode(), 0);
                assertFalse(mtrfSupportedAndAuthorized);
                assertFalse(mtrfSupportedAndNotAuthorized);
                assertTrue(newMSCNumber.getAddress().equals("22228"));
                assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(newVLRNumber.getAddress().equals("22229"));
                assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(Arrays.equals(newLmsi.getData(), new byte[] { 0, 3, 98, 39 }));
                assertEquals(mapProtocolVersion, 3);

                try {
                    d.addCancelLocationResponse(ind.getInvokeId(), extensionContainer);
                } catch (MAPException e) {
                    this.error("Error while adding UpdateLocationResponse", e);
                    fail("Error while adding UpdateLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty CancelLocationResponse", e);
                    fail("Error while sending the empty CancelLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CancelLocation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CancelLocationResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CancelLocation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CancelLocationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCancelLocation();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + cancelLocationRequest MAV V2 TC-END + cancleLocationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testCancelLocation_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onCancelLocationResponse(CancelLocationResponse ind) {
                super.onCancelLocationResponse(ind);
                assertNull(ind.getExtensionContainer());
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onCancelLocationRequest(CancelLocationRequest ind) {
                super.onCancelLocationRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();
                IMSIWithLMSI imsiWithLmsi = ind.getImsiWithLmsi();
                CancellationType cancellationType = ind.getCancellationType();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                TypeOfUpdate typeOfUpdate = ind.getTypeOfUpdate();
                boolean mtrfSupportedAndAuthorized = ind.getMtrfSupportedAndAuthorized();
                boolean mtrfSupportedAndNotAuthorized = ind.getMtrfSupportedAndNotAuthorized();
                ISDNAddressString newMSCNumber = ind.getNewMSCNumber();
                ISDNAddressString newVLRNumber = ind.getNewVLRNumber();
                LMSI newLmsi = ind.getNewLmsi();
                long mapProtocolVersion = ind.getMapProtocolVersion();

                assertTrue(imsi.getData().equals("1111122222"));
                assertNull(imsiWithLmsi);
                assertNull(cancellationType);
                assertNull(extensionContainer);
                assertNull(typeOfUpdate);
                assertFalse(mtrfSupportedAndAuthorized);
                assertFalse(mtrfSupportedAndNotAuthorized);
                assertNull(newMSCNumber);
                assertNull(newVLRNumber);
                assertNull(newLmsi);
                assertEquals(mapProtocolVersion, 2);

                try {
                    d.addCancelLocationResponse(ind.getInvokeId(), null);
                } catch (MAPException e) {
                    this.error("Error while adding UpdateLocationResponse", e);
                    fail("Error while adding UpdateLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty CancelLocationResponse", e);
                    fail("Error while sending the empty CancelLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.CancelLocation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CancelLocationResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.CancelLocation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.CancelLocationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendCancelLocation_V2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + provideRoamingNumber V3 TC-END + provideRoamingNumberResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testProvideRoamingNumber() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse ind) {

                super.onProvideRoamingNumberResponse(ind);
                ISDNAddressString roamingNumber = ind.getRoamingNumber();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                boolean releaseResourcesSupported = ind.getReleaseResourcesSupported();
                ISDNAddressString vmscAddress = ind.getVmscAddress();
                long mapProtocolVersion = ind.getMapProtocolVersion();

                assertNotNull(roamingNumber);
                assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(roamingNumber.getAddress(), "49883700292");
                assertEquals(mapProtocolVersion, 3);
                MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer);

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest ind) {
                super.onProvideRoamingNumberRequest(ind);

                MAPDialogCallHandling d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();
                ISDNAddressString mscNumber = ind.getMscNumber();
                ISDNAddressString msisdn = ind.getMsisdn();
                LMSI lmsi = ind.getLmsi();
                ExternalSignalInfo gsmBearerCapability = ind.getGsmBearerCapability();
                ExternalSignalInfo networkSignalInfo = ind.getNetworkSignalInfo();
                boolean suppressionOfAnnouncement = ind.getSuppressionOfAnnouncement();
                ISDNAddressString gmscAddress = ind.getGmscAddress();
                CallReferenceNumber callReferenceNumber = ind.getCallReferenceNumber();
                boolean orInterrogation = ind.getOrInterrogation();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                AlertingPattern alertingPattern = ind.getAlertingPattern();
                boolean ccbsCall = ind.getCcbsCall();
                SupportedCamelPhases supportedCamelPhasesInInterrogatingNode = ind.getSupportedCamelPhasesInInterrogatingNode();
                ExtExternalSignalInfo additionalSignalInfo = ind.getAdditionalSignalInfo();
                boolean orNotSupportedInGMSC = ind.getOrNotSupportedInGMSC();
                boolean prePagingSupported = ind.getPrePagingSupported();
                boolean longFTNSupported = ind.getLongFTNSupported();
                boolean suppressVtCsi = ind.getSuppressVtCsi();
                OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode = ind.getOfferedCamel4CSIsInInterrogatingNode();
                boolean mtRoamingRetrySupported = ind.getMtRoamingRetrySupported();
                PagingArea pagingArea = ind.getPagingArea();
                EMLPPPriority callPriority = ind.getCallPriority();
                boolean mtrfIndicator = ind.getMtrfIndicator();
                ISDNAddressString oldMSCNumber = ind.getOldMSCNumber();
                long mapProtocolVersion = ind.getMapProtocolVersion();

                assertNotNull(imsi);
                assertEquals(imsi.getData(), "011220200198227");
                assertNotNull(mscNumber);
                assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(mscNumber.getAddress(), "22228");
                assertNotNull(msisdn);
                assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(msisdn.getAddress(), "22227");
                assertNotNull(lmsi);
                assertNotNull(gsmBearerCapability);
                assertNotNull(networkSignalInfo);
                assertFalse(suppressionOfAnnouncement);
                assertNotNull(gmscAddress);
                assertEquals(gmscAddress.getAddressNature(), AddressNature.international_number);
                assertEquals(gmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(gmscAddress.getAddress(), "22226");
                MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer);

                ISDNAddressString roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                        NumberingPlan.ISDN, "49883700292");
                boolean releaseResourcesSupported = false;
                ISDNAddressString vmscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                        NumberingPlan.ISDN, "29113123311");

                try {
                    d.addProvideRoamingNumberResponse(ind.getInvokeId(), roamingNumber, extensionContainer,
                            releaseResourcesSupported, vmscAddress);
                } catch (MAPException e) {
                    this.error("Error while adding UpdateLocationResponse", e);
                    fail("Error while adding UpdateLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumberResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty CancelLocationResponse", e);
                    fail("Error while sending the empty CancelLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideRoamingNumberResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideRoamingNumber, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProvideRoamingNumberResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendProvideRoamingNumber();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + provideRoamingNumberRequest V2 TC-END + provideRoamingNumberResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testProvideRoamingNumber_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse ind) {

                super.onProvideRoamingNumberResponse(ind);
                ISDNAddressString roamingNumber = ind.getRoamingNumber();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                boolean releaseResourcesSupported = ind.getReleaseResourcesSupported();
                ISDNAddressString vmscAddress = ind.getVmscAddress();
                long mapProtocolVersion = ind.getMapProtocolVersion();

                assertNotNull(roamingNumber);
                assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(roamingNumber.getAddress(), "49883700292");
                assertFalse(releaseResourcesSupported);
                assertNull(extensionContainer);
                assertNull(vmscAddress);
                assertEquals(mapProtocolVersion, 2);

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest ind) {
                super.onProvideRoamingNumberRequest(ind);

                MAPDialogCallHandling d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();
                ISDNAddressString mscNumber = ind.getMscNumber();
                ISDNAddressString msisdn = ind.getMsisdn();
                LMSI lmsi = ind.getLmsi();
                ExternalSignalInfo gsmBearerCapability = ind.getGsmBearerCapability();
                ExternalSignalInfo networkSignalInfo = ind.getNetworkSignalInfo();
                boolean suppressionOfAnnouncement = ind.getSuppressionOfAnnouncement();
                ISDNAddressString gmscAddress = ind.getGmscAddress();
                CallReferenceNumber callReferenceNumber = ind.getCallReferenceNumber();
                boolean orInterrogation = ind.getOrInterrogation();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                AlertingPattern alertingPattern = ind.getAlertingPattern();
                boolean ccbsCall = ind.getCcbsCall();
                SupportedCamelPhases supportedCamelPhasesInInterrogatingNode = ind.getSupportedCamelPhasesInInterrogatingNode();
                ExtExternalSignalInfo additionalSignalInfo = ind.getAdditionalSignalInfo();
                boolean orNotSupportedInGMSC = ind.getOrNotSupportedInGMSC();
                boolean prePagingSupported = ind.getPrePagingSupported();
                boolean longFTNSupported = ind.getLongFTNSupported();
                boolean suppressVtCsi = ind.getSuppressVtCsi();
                OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode = ind.getOfferedCamel4CSIsInInterrogatingNode();
                boolean mtRoamingRetrySupported = ind.getMtRoamingRetrySupported();
                PagingArea pagingArea = ind.getPagingArea();
                EMLPPPriority callPriority = ind.getCallPriority();
                boolean mtrfIndicator = ind.getMtrfIndicator();
                ISDNAddressString oldMSCNumber = ind.getOldMSCNumber();
                long mapProtocolVersion = ind.getMapProtocolVersion();

                assertNotNull(imsi);
                assertEquals(imsi.getData(), "011220200198227");
                assertNotNull(mscNumber);
                assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(mscNumber.getAddress(), "22228");
                assertNull(msisdn);
                assertNull(lmsi);
                assertFalse(suppressionOfAnnouncement);
                assertNull(gmscAddress);
                assertNull(extensionContainer);
                assertEquals(mapProtocolVersion, 2);

                ISDNAddressString roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                        NumberingPlan.ISDN, "49883700292");

                try {
                    d.addProvideRoamingNumberResponse(ind.getInvokeId(), roamingNumber, null, false, null);
                } catch (MAPException e) {
                    this.error("Error while adding UpdateLocationResponse", e);
                    fail("Error while adding UpdateLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumberResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty CancelLocationResponse", e);
                    fail("Error while sending the empty CancelLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideRoamingNumberResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ProvideRoamingNumber, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ProvideRoamingNumberResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendProvideRoamingNumber_V2();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + istCommandRequest  TC-END + istCommandResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testIstCommand() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onIstCommandResponse(IstCommandResponse ind) {

                super.onIstCommandResponse(ind);
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
                assertNotNull(extensionContainer);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onIstCommandRequest(IstCommandRequest ind) {
                super.onIstCommandRequest(ind);

                MAPDialogCallHandling d = ind.getMAPDialog();

                IMSI imsi = ind.getImsi();
                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();

                assertNotNull(imsi);
                assertEquals(imsi.getData(), "011220200198227");
                assertNotNull(extensionContainer);

                try {
                    d.addIstCommandResponse(ind.getInvokeId(), extensionContainer);
                } catch (MAPException e) {
                    this.error("Error while adding IstCommandResponse", e);
                    fail("Error while adding IstCommandResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.IstCommandResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty CancelLocationResponse", e);
                    fail("Error while sending the empty CancelLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.IstCommand, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.IstCommandResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.IstCommand, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.IstCommandResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendIstCommand();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
//        System.out.println(client.observerdEvents);
  //      System.out.println(server.observerdEvents);
    }

    /**
<code>
TC-BEGIN + InsertSubscriberDataRequest MAV V3
TC-END + InsertSubscriberDataRequestResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testInsertSubscriberData_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request) {
                super.onInsertSubscriberDataResponse(request);

                InsertSubscriberDataResponseImpl ind = (InsertSubscriberDataResponseImpl) request;
                ArrayList<ExtTeleserviceCode> teleserviceList = ind.getTeleserviceList();
                assertNotNull(teleserviceList);
                assertEquals(teleserviceList.size(), 1);
                ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
                assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
                ArrayList<ExtBearerServiceCode> bearerServiceList = ind.getBearerServiceList();
                assertNotNull(bearerServiceList);
                assertEquals(bearerServiceList.size(), 1);
                ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
                assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
//                MAPExtensionContainerTest.CheckTestExtensionContainer(request.getExtensionContainer());

                long mapProtocolVersion = ind.getMapProtocolVersion();
                assertEquals(mapProtocolVersion, 3);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
                super.onInsertSubscriberDataRequest(request);

                MAPDialogMobility d = request.getMAPDialog();
                InsertSubscriberDataRequestImpl ind = (InsertSubscriberDataRequestImpl) request;

                long mapProtocolVersion = ind.getMapProtocolVersion();
                assertNull(ind.getProvisionedSS());
                assertNull(ind.getODBData());
                assertTrue(ind.getRoamingRestrictionDueToUnsupportedFeature());
                assertNull(ind.getRegionalSubscriptionData());
                assertNull(ind.getVbsSubscriptionData());
                assertNull(ind.getVgcsSubscriptionData());
                assertNull(ind.getVlrCamelSubscriptionInfo());
                assertNull(ind.getNAEAPreferredCI());
                assertNull(ind.getGPRSSubscriptionData());
                assertTrue(ind.getRoamingRestrictedInSgsnDueToUnsupportedFeature());
                assertNull(ind.getNetworkAccessMode());
                assertNull(ind.getLSAInformation());
                assertTrue(ind.getLmuIndicator());
                assertNull(ind.getLCSInformation());
                assertNull(ind.getIstAlertTimer());
                assertNull(ind.getSuperChargerSupportedInHLR());
                assertNull(ind.getMcSsInfo());
                assertNull(ind.getCSAllocationRetentionPriority());
                assertNull(ind.getSgsnCamelSubscriptionInfo());
                assertNull(ind.getChargingCharacteristics());
                assertNull(ind.getAccessRestrictionData());
                assertNull(ind.getIcsIndicator());
                assertNull(ind.getEpsSubscriptionData());
                assertNull(ind.getCsgSubscriptionDataList());
                assertTrue(ind.getUeReachabilityRequestIndicator());

                assertNull(ind.getMmeName());
                assertNull(ind.getSubscribedPeriodicRAUTAUtimer());
                assertTrue(ind.getVplmnLIPAAllowed());
                assertNull(ind.getMdtUserConsent());
                assertNull(ind.getSubscribedPeriodicLAUtimer());
                assertEquals(mapProtocolVersion, 3);

                IMSI imsi = ind.getImsi();
                assertTrue(imsi.getData().equals("1111122222"));

                ISDNAddressString msisdn = ind.getMsisdn();
                assertTrue(msisdn.getAddress().equals("22234"));
                assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Category category = ind.getCategory();
                assertEquals(category.getData(), 5);
                SubscriberStatus subscriberStatus = ind.getSubscriberStatus();
                assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);
                ArrayList<ExtBearerServiceCode> bearerServiceList = ind.getBearerServiceList();
                assertNotNull(bearerServiceList);
                assertEquals(bearerServiceList.size(), 1);
                ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
                assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
                ArrayList<ExtTeleserviceCode> teleserviceList = ind.getTeleserviceList();
                assertNotNull(teleserviceList);
                assertEquals(teleserviceList.size(), 1);
                ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
                assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

                MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
//                assertNotNull(ind.getExtensionContainer());
//                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
                ISDNAddressString sgsnNumber = ind.getSgsnNumber();
                assertNotNull(sgsnNumber);
                assertTrue(sgsnNumber.getAddress().equals("22228"));
                assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);

                ArrayList<SSCode> ssList = null;
                ODBGeneralData odbGeneralData = null;
                RegionalSubscriptionResponse regionalSubscriptionResponse = null;
                SupportedCamelPhases supportedCamelPhases = null;
                OfferedCamel4CSIs offeredCamel4CSIs = null;
                SupportedFeatures supportedFeatures = null;

                try {
                    d.addInsertSubscriberDataResponse(ind.getInvokeId(), teleserviceList, bearerServiceList, ssList,
                            odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
                            offeredCamel4CSIs, supportedFeatures);
                } catch (MAPException e) {
                    this.error("Error while adding InsertSubscriberDataResponse", e);
                    fail("Error while adding InsertSubscriberDataResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberDataResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty InsertSubscriberDataResponse", e);
                    fail("Error while sending the empty InsertSubscriberDataResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.InsertSubscriberData, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InsertSubscriberDataResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InsertSubscriberData, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.InsertSubscriberDataResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendInsertSubscriberData_V3();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
<code>
TC-BEGIN + InsertSubscriberDataRequest MAV V2
TC-END + InsertSubscriberDataRequestResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testInsertSubscriberData_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request) {
                super.onInsertSubscriberDataResponse(request);

                InsertSubscriberDataResponseImpl ind = (InsertSubscriberDataResponseImpl) request;

                ArrayList<ExtTeleserviceCode> teleserviceList = ind.getTeleserviceList();
                assertNotNull(teleserviceList);
                assertEquals(teleserviceList.size(), 1);
                ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
                assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

                ArrayList<ExtBearerServiceCode> bearerServiceList = ind.getBearerServiceList();
                assertNotNull(bearerServiceList);
                assertEquals(bearerServiceList.size(), 1);
                ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
                assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
                MAPExtensionContainerTest.CheckTestExtensionContainer(request.getExtensionContainer());

                long mapProtocolVersion = ind.getMapProtocolVersion();
                assertEquals(mapProtocolVersion, 2);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
                super.onInsertSubscriberDataRequest(request);

                MAPDialogMobility d = request.getMAPDialog();
                InsertSubscriberDataRequestImpl ind = (InsertSubscriberDataRequestImpl) request;

                long mapProtocolVersion = ind.getMapProtocolVersion();
                assertNull(ind.getProvisionedSS());
                assertNull(ind.getODBData());
                assertTrue(ind.getRoamingRestrictionDueToUnsupportedFeature());
                assertNull(ind.getRegionalSubscriptionData());
                assertNull(ind.getVbsSubscriptionData());
                assertNull(ind.getVgcsSubscriptionData());
                assertNull(ind.getVlrCamelSubscriptionInfo());
                assertNull(ind.getNAEAPreferredCI());
                assertNull(ind.getGPRSSubscriptionData());
                assertFalse(ind.getRoamingRestrictedInSgsnDueToUnsupportedFeature());
                assertNull(ind.getNetworkAccessMode());
                assertNull(ind.getLSAInformation());
                assertFalse(ind.getLmuIndicator());
                assertNull(ind.getLCSInformation());
                assertNull(ind.getIstAlertTimer());
                assertNull(ind.getSuperChargerSupportedInHLR());
                assertNull(ind.getMcSsInfo());
                assertNull(ind.getCSAllocationRetentionPriority());
                assertNull(ind.getSgsnCamelSubscriptionInfo());
                assertNull(ind.getChargingCharacteristics());
                assertNull(ind.getAccessRestrictionData());
                assertNull(ind.getIcsIndicator());
                assertNull(ind.getEpsSubscriptionData());
                assertNull(ind.getCsgSubscriptionDataList());
                assertFalse(ind.getUeReachabilityRequestIndicator());

                assertNull(ind.getMmeName());
                assertNull(ind.getSubscribedPeriodicRAUTAUtimer());
                assertFalse(ind.getVplmnLIPAAllowed());
                assertNull(ind.getMdtUserConsent());
                assertNull(ind.getSubscribedPeriodicLAUtimer());
                assertEquals(mapProtocolVersion, 2);

                IMSI imsi = ind.getImsi();
                assertTrue(imsi.getData().equals("1111122222"));

                ISDNAddressString msisdn = ind.getMsisdn();
                assertTrue(msisdn.getAddress().equals("22234"));
                assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                Category category = ind.getCategory();
                assertEquals(category.getData(), 5);
                SubscriberStatus subscriberStatus = ind.getSubscriberStatus();
                assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);
                ArrayList<ExtBearerServiceCode> bearerServiceList = ind.getBearerServiceList();
                assertNotNull(bearerServiceList);
                assertEquals(bearerServiceList.size(), 1);
                ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
                assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
                ArrayList<ExtTeleserviceCode> teleserviceList = ind.getTeleserviceList();
                assertNotNull(teleserviceList);
                assertEquals(teleserviceList.size(), 1);
                ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
                assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);
                assertNull(ind.getExtensionContainer());

                ArrayList<SSCode> ssList = null;
                ODBGeneralData odbGeneralData = null;
                RegionalSubscriptionResponse regionalSubscriptionResponse = null;

                try {
                    d.addInsertSubscriberDataResponse(ind.getInvokeId(), teleserviceList, bearerServiceList, ssList,
                            odbGeneralData, regionalSubscriptionResponse);
                } catch (MAPException e) {
                    this.error("Error while adding InsertSubscriberDataResponse", e);
                    fail("Error while adding InsertSubscriberDataResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberDataResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty InsertSubscriberDataResponse", e);
                    fail("Error while sending the empty InsertSubscriberDataResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.InsertSubscriberData, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InsertSubscriberDataResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InsertSubscriberData, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.InsertSubscriberDataResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendInsertSubscriberData_V2();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
<code>
TC-BEGIN + DeleteSubscriberDataRequest MAV V3
TC-END + DeleteSubscriberDataRequestResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDeleteSubscriberData_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request) {
                super.onDeleteSubscriberDataResponse(request);

                assertEquals(request.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);
                assertNull(request.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
                super.onDeleteSubscriberDataRequest(request);

                MAPDialogMobility d = request.getMAPDialog();

                assertEquals(request.getImsi().getData(), "1111122222");
                assertEquals(request.getBasicServiceList().size(), 2);
                assertEquals(request.getBasicServiceList().get(0).getExtBearerService().getBearerServiceCodeValue(),
                        BearerServiceCodeValue.padAccessCA_9600bps);
                assertEquals(request.getBasicServiceList().get(1).getExtBearerService().getBearerServiceCodeValue(),
                        BearerServiceCodeValue.padAccessCA_9600bps);
                assertEquals(request.getSsList().size(), 2);
                assertEquals(request.getSsList().get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.allForwardingSS);
                assertEquals(request.getSsList().get(1).getSupplementaryCodeValue(), SupplementaryCodeValue.allLineIdentificationSS);

                assertFalse(request.getRoamingRestrictionDueToUnsupportedFeature());
                assertNull(request.getRegionalSubscriptionIdentifier());

                try {
                    d.addDeleteSubscriberDataResponse(request.getInvokeId(), RegionalSubscriptionResponse.tooManyZoneCodes, null);
                } catch (MAPException e) {
                    this.error("Error while adding DeleteSubscriberDataResponse", e);
                    fail("Error while adding DeleteSubscriberDataResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberDataResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty DeleteSubscriberDataResponse", e);
                    fail("Error while sending the empty DeleteSubscriberDataResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DeleteSubscriberDataResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DeleteSubscriberData, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DeleteSubscriberDataResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendDeleteSubscriberData_V3();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
<code>
TC-BEGIN + DeleteSubscriberDataRequest MAV V2
TC-END + DeleteSubscriberDataRequestResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDeleteSubscriberData_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request) {
                super.onDeleteSubscriberDataResponse(request);

                assertNull(request.getRegionalSubscriptionResponse());
                assertNull(request.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
                super.onDeleteSubscriberDataRequest(request);

                MAPDialogMobility d = request.getMAPDialog();

                assertEquals(request.getImsi().getData(), "1111122222");
                assertNull(request.getBasicServiceList());
                assertNull(request.getSsList());

                assertTrue(request.getRoamingRestrictionDueToUnsupportedFeature());
                assertEquals(request.getRegionalSubscriptionIdentifier().getValue(), 10);

                try {
                    d.addDeleteSubscriberDataResponse(request.getInvokeId(), null, null);
                } catch (MAPException e) {
                    this.error("Error while adding DeleteSubscriberDataResponse", e);
                    fail("Error while adding DeleteSubscriberDataResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberDataResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty DeleteSubscriberDataResponse", e);
                    fail("Error while sending the empty DeleteSubscriberDataResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DeleteSubscriberDataResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DeleteSubscriberData, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DeleteSubscriberDataResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);
        
        client.sendDeleteSubscriberData_V2();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
<code>
TC-BEGIN + SendRoutingInformation MAV V3
TC-END + SendRoutingInformationResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendRoutingInformation_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onSendRoutingInformationResponse(SendRoutingInformationResponse response) {
                super.onSendRoutingInformationResponse(response);

                SendRoutingInformationResponseImpl ind = (SendRoutingInformationResponseImpl) response;

                IMSI imsi = ind.getIMSI();
                ExtendedRoutingInfo extRoutingInfo = ind.getExtendedRoutingInfo();
                RoutingInfo routingInfo = extRoutingInfo.getRoutingInfo();
                ISDNAddressString roamingNumber = routingInfo.getRoamingNumber();

                assertNotNull(imsi);
                assertEquals(imsi.getData(), "011220200198227");
                assertNotNull(roamingNumber);
                assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
                assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(roamingNumber.getAddress(), "79273605819");

                long mapProtocolVersion = ind.getMapProtocolVersion();
                assertEquals(mapProtocolVersion, 3);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
                super.onSendRoutingInformationRequest(request);

                MAPDialogCallHandling d = request.getMAPDialog();
                SendRoutingInformationRequestImpl ind = (SendRoutingInformationRequestImpl) request;

                long mapProtocolVersion = ind.getMapProtocolVersion();
                ISDNAddressString msisdn = ind.getMsisdn();
                InterrogationType type = ind.getInterogationType();
                ISDNAddressString gmsc = ind.getGmscOrGsmSCFAddress();

                assertEquals(mapProtocolVersion, 3);
                assertNotNull(msisdn);
                assertNotNull(type);
                assertNotNull(gmsc);
                assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(msisdn.getAddress().equals("29113123311"));
                assertEquals(gmsc.getAddressNature(), AddressNature.international_number);
                assertEquals(gmsc.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(gmsc.getAddress().equals("49883700292"));
                assertEquals(type, InterrogationType.forwarding);

                IMSI imsi = this.mapParameterFactory.createIMSI("011220200198227");

                try {
                    ISDNAddressString roamingNumber = this.mapParameterFactory.createISDNAddressString(
                            AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
                    RoutingInfo routingInfo = this.mapParameterFactory.createRoutingInfo(roamingNumber);
                    ExtendedRoutingInfo extRoutingInfo = this.mapParameterFactory.createExtendedRoutingInfo(routingInfo);

                    CUGCheckInfo cugCheckInfo = null;
                    boolean cugSubscriptionFlag = false;
                    CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = this.mapParameterFactory
                            .createCellGlobalIdOrServiceAreaIdFixedLength(250, 1, 1111, 222);
                    LAIFixedLength laiFixedLength = this.mapParameterFactory.createLAIFixedLength(250, 1, 1111);
                    CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = this.mapParameterFactory
                            .createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);
                    LocationInformationGPRS locationInformationGPRS = this.mapParameterFactory.createLocationInformationGPRS(
                            cellGlobalIdOrServiceAreaIdOrLAI, null, null, null, null, null, false, null, false, null);
                    SubscriberInfo subscriberInfo = this.mapParameterFactory.createSubscriberInfo(null, null, null,
                            locationInformationGPRS, null, null, null, null, null);
                    ArrayList<SSCode> ssList = null;
                    ExtBasicServiceCode basicService = null;
                    boolean forwardingInterrogationRequired = false;
                    ISDNAddressString vmscAddress = null;
                    MAPExtensionContainer extensionContainer = null;
                    NAEAPreferredCI naeaPreferredCI = null;
                    CCBSIndicators ccbsIndicators = null;
                    NumberPortabilityStatus nrPortabilityStatus = null;
                    Integer istAlertTimer = null;
                    SupportedCamelPhases supportedCamelPhases = null;
                    OfferedCamel4CSIs offeredCamel4CSIs = null;
                    RoutingInfo routingInfo2 = null;
                    ArrayList<SSCode> ssList2 = null;
                    ExtBasicServiceCode basicService2 = null;
                    AllowedServices allowedServices = null;
                    UnavailabilityCause unavailabilityCause = null;
                    boolean releaseResourcesSupported = false;
                    ExternalSignalInfo gsmBearerCapability = null;

                    d.addSendRoutingInformationResponse(ind.getInvokeId(), imsi, extRoutingInfo, cugCheckInfo,
                            cugSubscriptionFlag, subscriberInfo, ssList, basicService, forwardingInterrogationRequired,
                            vmscAddress, extensionContainer, naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus,
                            istAlertTimer, supportedCamelPhases, offeredCamel4CSIs, routingInfo2, ssList2, basicService2,
                            allowedServices, unavailabilityCause, releaseResourcesSupported, gsmBearerCapability);
                } catch (MAPException e) {
                    this.error("Error while adding SendRoutingInformationResponse", e);
                    fail("Error while adding SendRoutingInformationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty SendRoutingInformationResponse", e);
                    fail("Error while sending the empty SendRoutingInformationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInformation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformation, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendRoutingInformation_V3();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }


    /**
<code>
MAV V3
TC-BEGIN + SendRoutingInformation
  TC-CONTINUE + SendRoutingInformationResponse-NonLast
TC-CONTINUE
  TC-END + SendRoutingInformationResponse-Last
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendRoutingInformation_V3_NonLast() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            private int dialogStep;

            @Override
            public void onSendRoutingInformationResponse(SendRoutingInformationResponse response) {
                super.onSendRoutingInformationResponse(response);

                SendRoutingInformationResponseImpl ind = (SendRoutingInformationResponseImpl) response;

                if (dialogStep == 0) {
                    IMSI imsi = ind.getIMSI();
                    ExtendedRoutingInfo extRoutingInfo = ind.getExtendedRoutingInfo();
                    RoutingInfo routingInfo = extRoutingInfo.getRoutingInfo();
                    ISDNAddressString roamingNumber = routingInfo.getRoamingNumber();

                    assertNotNull(imsi);
                    assertEquals(imsi.getData(), "011220200198227");
                    assertNotNull(roamingNumber);
                    assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
                    assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
                    assertEquals(roamingNumber.getAddress(), "79273605819");

                    assertNull(ind.getVmscAddress());

                    long mapProtocolVersion = ind.getMapProtocolVersion();
                    assertEquals(mapProtocolVersion, 3);
                    assertTrue(ind.isReturnResultNotLast());

                    dialogStep++;
                    
                    
                } else if (dialogStep == 1) {
                    ISDNAddressString isdn = ind.getVmscAddress();
                    assertEquals(isdn.getAddress(), "22233300");
                    assertFalse(ind.isReturnResultNotLast());

                    assertNull(ind.getIMSI());

                    long mapProtocolVersion = ind.getMapProtocolVersion();
                    assertEquals(mapProtocolVersion, 3);
                    assertFalse(ind.isReturnResultNotLast());

                    dialogStep++;
                } else {
                    fail("Wrong dialogStep - in testSendRoutingInformation_V3_NonLast - Client " + dialogStep);
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    if (dialogStep == 1) {
                        mapDialog.send();
                    }
                } catch (MAPException e) {
                    this.error("Error while sending testSendRoutingInformation_V3_NonLast - empty TC-CONTINUE", e);
                    fail("Error while sending testSendRoutingInformation_V3_NonLast - empty TC-CONTINUE");
                }
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            private int dialogStep;
            private long invokeId;

            @Override
            public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
                super.onSendRoutingInformationRequest(request);

                MAPDialogCallHandling d = request.getMAPDialog();
                SendRoutingInformationRequestImpl ind = (SendRoutingInformationRequestImpl) request;
                invokeId = ind.getInvokeId();

                long mapProtocolVersion = ind.getMapProtocolVersion();
                ISDNAddressString msisdn = ind.getMsisdn();
                InterrogationType type = ind.getInterogationType();
                ISDNAddressString gmsc = ind.getGmscOrGsmSCFAddress();

                assertEquals(mapProtocolVersion, 3);
                assertNotNull(msisdn);
                assertNotNull(type);
                assertNotNull(gmsc);
                assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(msisdn.getAddress().equals("29113123311"));
                assertEquals(gmsc.getAddressNature(), AddressNature.international_number);
                assertEquals(gmsc.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(gmsc.getAddress().equals("49883700292"));
                assertEquals(type, InterrogationType.forwarding);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {

                    MAPDialogCallHandling d = (MAPDialogCallHandling)mapDialog;

                    if (dialogStep == 0) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, sequence++));

                        IMSI imsi = this.mapParameterFactory.createIMSI("011220200198227");

                        ISDNAddressString roamingNumber = this.mapParameterFactory.createISDNAddressString(
                                AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
                        RoutingInfo routingInfo = this.mapParameterFactory.createRoutingInfo(roamingNumber);
                        ExtendedRoutingInfo extRoutingInfo = this.mapParameterFactory.createExtendedRoutingInfo(routingInfo);

                        CUGCheckInfo cugCheckInfo = null;
                        boolean cugSubscriptionFlag = false;
                        CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = this.mapParameterFactory
                                .createCellGlobalIdOrServiceAreaIdFixedLength(250, 1, 1111, 222);
                        CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = this.mapParameterFactory
                                .createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);
                        LocationInformationGPRS locationInformationGPRS = this.mapParameterFactory
                                .createLocationInformationGPRS(cellGlobalIdOrServiceAreaIdOrLAI, null, null, null, null, null,
                                        false, null, false, null);
                        SubscriberInfo subscriberInfo = this.mapParameterFactory.createSubscriberInfo(null, null, null,
                                locationInformationGPRS, null, null, null, null, null);
                        ArrayList<SSCode> ssList = null;
                        ExtBasicServiceCode basicService = null;
                        boolean forwardingInterrogationRequired = false;
                        ISDNAddressString vmscAddress = null;
                        MAPExtensionContainer extensionContainer = null;
                        NAEAPreferredCI naeaPreferredCI = null;
                        CCBSIndicators ccbsIndicators = null;
                        NumberPortabilityStatus nrPortabilityStatus = null;
                        Integer istAlertTimer = null;
                        SupportedCamelPhases supportedCamelPhases = null;
                        OfferedCamel4CSIs offeredCamel4CSIs = null;
                        RoutingInfo routingInfo2 = null;
                        ArrayList<SSCode> ssList2 = null;
                        ExtBasicServiceCode basicService2 = null;
                        AllowedServices allowedServices = null;
                        UnavailabilityCause unavailabilityCause = null;
                        boolean releaseResourcesSupported = false;
                        ExternalSignalInfo gsmBearerCapability = null;

                        d.addSendRoutingInformationResponse_NonLast(invokeId, imsi, extRoutingInfo,
                                cugCheckInfo, cugSubscriptionFlag, subscriberInfo, ssList, basicService,
                                forwardingInterrogationRequired, vmscAddress, extensionContainer, naeaPreferredCI,
                                ccbsIndicators, null, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                                offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                                releaseResourcesSupported, gsmBearerCapability);
                        d.send();
                        dialogStep++;
                    } else if (dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, sequence++));

                        ISDNAddressString vmscAddress = this.mapParameterFactory.createISDNAddressString(
                                AddressNature.international_number, NumberingPlan.ISDN, "22233300");
                        d.addSendRoutingInformationResponse(invokeId, null, null, null, false, null, null,
                                null, false, vmscAddress, null, null, null, null, null, null, null, null, null, null, null,
                                null, null, false, null);

                        d.close(false);
                        dialogStep++;
                    } else {
                        fail("Wrong dialogStep - in testSendRoutingInformation_V3_NonLast - Client " + dialogStep);
                    }
                } catch (MAPException e) {
                    this.error("Error while sending testSendRoutingInformation_V3_NonLast - error of sending of back a response", e);
                    fail("Error while sending the - error of sending of back a response");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInformation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformation, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        this.saveTrafficInFile();
        
        client.sendSendRoutingInformation_V3();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
<code>
TC-BEGIN + SendRoutingInformation MAV V2
TC-END + SendRoutingInformationResponse
</code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendRoutingInformation_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onSendRoutingInformationResponse(SendRoutingInformationResponse response) {
                super.onSendRoutingInformationResponse(response);

                SendRoutingInformationResponseImpl ind = (SendRoutingInformationResponseImpl) response;

                IMSI imsi = ind.getIMSI();
                assertNull(ind.getExtendedRoutingInfo());

                assertNotNull(imsi);
                assertEquals(imsi.getData(), "011220200198227");

                long mapProtocolVersion = ind.getMapProtocolVersion();
                assertEquals(mapProtocolVersion, 2);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
                super.onSendRoutingInformationRequest(request);

                MAPDialogCallHandling d = request.getMAPDialog();
                SendRoutingInformationRequestImpl ind = (SendRoutingInformationRequestImpl) request;

                long mapProtocolVersion = ind.getMapProtocolVersion();
                ISDNAddressString msisdn = ind.getMsisdn();

                assertEquals(mapProtocolVersion, 2);
                assertNotNull(msisdn);
                assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
                assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(msisdn.getAddress().equals("29113123311"));

                IMSI imsi = this.mapParameterFactory.createIMSI("011220200198227");
                ISDNAddressString roamingNumber = this.mapParameterFactory.createISDNAddressString(
                        AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
                RoutingInfoImpl routingInfo = new RoutingInfoImpl(roamingNumber);

                try {
                    d.addSendRoutingInformationResponse(ind.getInvokeId(), imsi, null, routingInfo);
                } catch (MAPException e) {
                    this.error("Error while adding SendRoutingInformationResponse", e);
                    fail("Error while adding SendRoutingInformationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty SendRoutingInformationResponse", e);
                    fail("Error while sending the empty SendRoutingInformationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInformation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInformation, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInformationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendRoutingInformation_V2();
        waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + sendIdentificationRequest MAV V2 TC-END + sendIdentificationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendIdentification_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSendIdentificationResponse(SendIdentificationResponse ind) {
                super.onSendIdentificationResponse(ind);
                assertEquals(ind.getImsi().getData(), "011220200198227");
                assertNull(ind.getExtensionContainer());
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendIdentificationRequest(SendIdentificationRequest ind) {
                super.onSendIdentificationRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                assertTrue(Arrays.equals(ind.getTmsi().getData(), new byte[] { 1, 2, 3, 4 }));
                long mapProtocolVersion = ((SendIdentificationRequestImpl) ind).getMapProtocolVersion();

                assertEquals(mapProtocolVersion, 2);

                IMSI imsi = new IMSIImpl("011220200198227");

                try {
                    d.addSendIdentificationResponse(ind.getInvokeId(), imsi, null, null, null);

                } catch (MAPException e) {
                    this.error("Error while adding SendIdentificationResponse", e);
                    fail("Error while adding SendIdentificationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentificationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the SendIdentificationResponse", e);
                    fail("Error while sending the empty SendIdentificationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendIdentification, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendIdentificationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendIdentification, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendIdentificationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendIdentification_V2();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + sendIdentificationRequest MAV V3 TC-END + sendIdentificationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendIdentification_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onSendIdentificationResponse(SendIdentificationResponse ind) {
                super.onSendIdentificationResponse(ind);
                assertEquals(ind.getImsi().getData(), "011220200198227");
                assertNull(ind.getExtensionContainer());
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onSendIdentificationRequest(SendIdentificationRequest ind) {
                super.onSendIdentificationRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                assertTrue(Arrays.equals(ind.getTmsi().getData(), new byte[] { 1, 2, 3, 4 }));
                long mapProtocolVersion = ((SendIdentificationRequestImpl) ind).getMapProtocolVersion();
                assertEquals(mapProtocolVersion, 3);

                IMSI imsi = new IMSIImpl("011220200198227");

                try {
                    d.addSendIdentificationResponse(ind.getInvokeId(), imsi, null, null, null);
                } catch (MAPException e) {
                    this.error("Error while adding SendIdentificationResponse", e);
                    fail("Error while adding SendIdentificationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentificationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the SendIdentificationResponse", e);
                    fail("Error while sending the empty SendIdentificationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendIdentification, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendIdentificationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendIdentification, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendIdentificationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendIdentification_V3();
        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + UpdateGprsLocationRequest MAV V3 TC-END + UpdateGprsLocationResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testUpdateGprsLocation_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
            @Override
            public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse ind) {
                super.onUpdateGprsLocationResponse(ind);
                assertTrue(ind.getHlrNumber().getAddress().equals("22228"));
                assertEquals(ind.getHlrNumber().getAddressNature(), AddressNature.international_number);
                assertEquals(ind.getHlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(ind.getAddCapability());
                assertTrue(ind.getSgsnMmeSeparationSupported());
            }
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
            @Override
            public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest ind) {
                super.onUpdateGprsLocationRequest(ind);

                MAPDialogMobility d = ((UpdateGprsLocationRequestImpl) ind).getMAPDialog();

                assertTrue(ind.getImsi().getData().equals("111222"));
                assertTrue(ind.getSgsnNumber().getAddress().equals("22228"));
                assertEquals(ind.getSgsnNumber().getAddressNature(), AddressNature.international_number);
                assertEquals(ind.getSgsnNumber().getNumberingPlan(), NumberingPlan.ISDN);
                assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
                assertTrue(ind.getSGSNCapability().getSolsaSupportIndicator());
                assertTrue(ind.getInformPreviousNetworkEntity());
                assertTrue(ind.getPsLCSNotSupportedByUE());
                assertTrue(ind.getADDInfo().getImeisv().getIMEI().equals("12341234"));
                assertTrue(ind.getEPSInfo().getIsrInformation().getCancelSGSN());
                assertTrue(ind.getServingNodeTypeIndicator());
                assertTrue(ind.getSkipSubscriberDataUpdate());
                assertEquals(ind.getUsedRATType(), UsedRATType.gan);
                assertTrue(ind.getGprsSubscriptionDataNotNeeded());
                assertTrue(ind.getNodeTypeIndicator());
                assertTrue(ind.getAreaRestricted());
                assertTrue(ind.getUeReachableIndicator());
                assertTrue(ind.getEpsSubscriptionDataNotNeeded());
                assertEquals(ind.getUESRVCCCapability(), UESRVCCCapability.ueSrvccSupported);

                ISDNAddressString hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "22228");

                try {
                    d.addUpdateGprsLocationResponse(((UpdateGprsLocationRequestImpl) ind).getInvokeId(), hlrNumber, null, true,
                            true);
                } catch (MAPException e) {
                    this.error("Error while adding UpdateGprsLocationResponse", e);
                    fail("Error while adding UpdateGprsLocationResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateGprsLocationResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty UpdateGprsLocationResponse", e);
                    fail("Error while sending the empty UpdateGprsLocationResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.UpdateGprsLocation, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UpdateGprsLocationResp, null, count++,
                (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.UpdateGprsLocation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.UpdateGprsLocationResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendUpdateGprsLocation_V3();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }
    
    /**
     * TC-BEGIN + PurgeMSRequest MAV V3 TC-END + PurgeMSResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testPurgeMSRequest_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onPurgeMSResponse(PurgeMSResponse ind) {
                super.onPurgeMSResponse(ind);
                assertTrue(ind.getFreezeMTMSI());
                assertTrue(ind.getFreezePTMSI());

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onPurgeMSRequest(PurgeMSRequest request) {
                super.onPurgeMSRequest(request);

                MAPDialogMobility d = ((PurgeMSRequestImpl) request).getMAPDialog();

                assertTrue(request.getImsi().getData().equals("111222"));
                assertTrue(request.getSgsnNumber().getAddress().equals("22228"));

                try {
                    d.addPurgeMSResponse(((PurgeMSRequestImpl) request).getInvokeId(), true, true, null, true);

                } catch (MAPException e) {
                    this.error("Error while adding PurgeMSResponse", e);
                    fail("Error while adding PurgeMSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty PurgeMSResponse", e);
                    fail("Error while sending the empty PurgeMSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.PurgeMS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.PurgeMSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.PurgeMS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.PurgeMSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendPurgeMS_V3();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * TC-BEGIN + PurgeMSRequest MAP V2 TC-END + PurgeMSResponse
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testPurgeMSRequest_V2() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onPurgeMSResponse(PurgeMSResponse ind) {
                super.onPurgeMSResponse(ind);
                assertFalse(ind.getFreezeMTMSI());
                assertFalse(ind.getFreezePTMSI());

            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onPurgeMSRequest(PurgeMSRequest request) {
                super.onPurgeMSRequest(request);

                MAPDialogMobility d = ((PurgeMSRequestImpl) request).getMAPDialog();

                assertTrue(request.getImsi().getData().equals("111222"));
                assertTrue(request.getVlrNumber().getAddress().equals("22228"));

                try {
                    d.addPurgeMSResponse(((PurgeMSRequestImpl) request).getInvokeId(), false, false, null, false);

                } catch (MAPException e) {
                    this.error("Error while adding PurgeMSResponse", e);
                    fail("Error while adding PurgeMSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty PurgeMSResponse", e);
                    fail("Error while sending the empty PurgeMSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.PurgeMS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.PurgeMSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.PurgeMS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.PurgeMSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendPurgeMS_V2();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + reset MAP V1
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testResetRequest_V1() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onResetRequest(ResetRequest request) {
                super.onResetRequest(request);

                assertEquals(request.getNetworkResource(), NetworkResource.hlr);
                assertEquals(request.getHlrNumber().getAddress(), "22220000");
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                mapDialog.release();
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Reset, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.Reset, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendReset_V1();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + forwardCheckSSIndicationRequest MAP V3
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testForwardCheckSSIndicationRequest_V3() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest request) {
                super.onForwardCheckSSIndicationRequest(request);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                mapDialog.release();
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ForwardCheckSSIndication, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ForwardCheckSSIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendForwardCheckSSIndicationRequest_V3();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + RestoreDataRequest 
     * TC-END + RestoreDataRequest
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testRestoreDataRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onRestoreDataResponse(RestoreDataResponse ind) {
                super.onRestoreDataResponse(ind);

                assertEquals(ind.getHlrNumber().getAddress(), "9992222");
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onRestoreDataRequest(RestoreDataRequest request) {
                super.onRestoreDataRequest(request);

                MAPDialogMobility d = ((RestoreDataRequestImpl) request).getMAPDialog();

                assertTrue(request.getImsi().getData().equals("00000222229999"));

                try {
                    ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN,
                            "9992222");
                    d.addRestoreDataResponse(((RestoreDataRequestImpl) request).getInvokeId(), hlrNumber, false, null);

                } catch (MAPException e) {
                    this.error("Error while adding RestoreDataResponse", e);
                    fail("Error while adding RestoreDataResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.RestoreDataResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty RestoreDataResponse", e);
                    fail("Error while sending the empty RestoreDataResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.RestoreData, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RestoreDataResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RestoreData, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.RestoreDataResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendRestoreData();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + SendImsiRequest 
     * TC-END + SendImsiResponse
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendImsiRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onSendImsiResponse(SendImsiResponse ind) {
                super.onSendImsiResponse(ind);

                assertEquals(ind.getImsi().getData(), "88888999991111");
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onSendImsiRequest(SendImsiRequest request) {
                super.onSendImsiRequest(request);

                MAPDialogOam d = ((SendImsiRequestImpl) request).getMAPDialog();

                assertEquals(request.getMsisdn().getAddress(), "9992222");

                try {
                    IMSI imsi = this.mapParameterFactory.createIMSI("88888999991111");
                    d.addSendImsiResponse(((SendImsiRequestImpl) request).getInvokeId(), imsi);

                } catch (MAPException e) {
                    this.error("Error while adding SendImsiResponse", e);
                    fail("Error while adding SendImsiResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendImsiResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty SendImsiResponse", e);
                    fail("Error while sending the empty SendImsiResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendImsi, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendImsiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendImsi, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendImsiResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);
        
        client.sendSendImsi();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + RegisterSSRequest
     * TC-END + RegisterSSResponse with parameter
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testRegisterSSRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onRegisterSSResponse(RegisterSSResponse ind) {
                super.onRegisterSSResponse(ind);

                SSData ssData = ind.getSsInfo().getSsData();
                assertEquals(ssData.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
                assertTrue(ssData.getSsStatus().getABit());
                assertFalse(ssData.getSsStatus().getQBit());
                assertFalse(ssData.getSsStatus().getPBit());
                assertFalse(ssData.getSsStatus().getRBit());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onRegisterSSRequest(RegisterSSRequest request) {
                super.onRegisterSSRequest(request);

                MAPDialogSupplementary d = ((RegisterSSRequestImpl) request).getMAPDialog();

                assertEquals(request.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
                assertEquals(request.getBasicService().getBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

                assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
                assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(), NumberingPlan.land_mobile);
                assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

                try {
                    SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
                    SSStatus ssStatus = this.mapParameterFactory.createSSStatus(false, false, false, true);
                    SSData ssData = this.mapParameterFactory.createSSData(ssCode, ssStatus, null, null, null, null);
                    SSInfo ssInfo = this.mapParameterFactory.createSSInfo(ssData);
                    d.addRegisterSSResponse(request.getInvokeId(), ssInfo);

                } catch (MAPException e) {
                    this.error("Error while adding RegisterSSResponse", e);
                    fail("Error while adding RegisterSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterSSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty RegisterSSResponse", e);
                    fail("Error while sending the empty RegisterSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.RegisterSS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RegisterSSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RegisterSS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.RegisterSSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);
        
        client.sendRegisterSS();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + EraseSSRequest
     * TC-END + EraseSSResponse without parameter
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testEraseSSRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onEraseSSResponse(EraseSSResponse ind) {
                super.onEraseSSResponse(ind);

                assertNull(ind.getSsInfo());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onEraseSSRequest(EraseSSRequest request) {
                super.onEraseSSRequest(request);

                MAPDialogSupplementary d = request.getMAPDialog();

                assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
                assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(), NumberingPlan.land_mobile);
                assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

                assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

                try {
                    d.addRegisterSSResponse(request.getInvokeId(), null);

                } catch (MAPException e) {
                    this.error("Error while adding EraseSSResponse", e);
                    fail("Error while adding EraseSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.EraseSSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty EraseSSResponse", e);
                    fail("Error while sending the empty EraseSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.EraseSS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.EraseSSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.EraseSS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.EraseSSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);
        
        client.sendEraseSS();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + ActivateSSRequest
     * TC-END + ActivateSSResponse with parameter
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testActivateSSRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onActivateSSResponse(ActivateSSResponse ind) {
                super.onActivateSSResponse(ind);

                SSData ssData = ind.getSsInfo().getSsData();
                assertEquals(ssData.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
                assertTrue(ssData.getSsStatus().getABit());
                assertFalse(ssData.getSsStatus().getQBit());
                assertFalse(ssData.getSsStatus().getPBit());
                assertFalse(ssData.getSsStatus().getRBit());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onActivateSSRequest(ActivateSSRequest request) {
                super.onActivateSSRequest(request);

                MAPDialogSupplementary d = request.getMAPDialog();

                assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
                assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(), NumberingPlan.land_mobile);
                assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

                assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

                try {
                    SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
                    SSStatus ssStatus = this.mapParameterFactory.createSSStatus(false, false, false, true);
                    SSData ssData = this.mapParameterFactory.createSSData(ssCode, ssStatus, null, null, null, null);
                    SSInfo ssInfo = this.mapParameterFactory.createSSInfo(ssData);
                    d.addActivateSSResponse(request.getInvokeId(), ssInfo);

                } catch (MAPException e) {
                    this.error("Error while adding ActivateSSResponse", e);
                    fail("Error while adding ActivateSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateSSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ActivateSSResponse", e);
                    fail("Error while sending the empty ActivateSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ActivateSS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ActivateSSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ActivateSS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ActivateSSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);
        
        client.sendActivateSS();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + DeactivateSSRequest
     * TC-END + DeactivateSSResponse without parameter
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testDeactivateSSRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onDeactivateSSResponse(DeactivateSSResponse ind) {
                super.onDeactivateSSResponse(ind);

                assertNull(ind.getSsInfo());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onDeactivateSSRequest(DeactivateSSRequest request) {
                super.onDeactivateSSRequest(request);

                MAPDialogSupplementary d = request.getMAPDialog();

                assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
                assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(), NumberingPlan.land_mobile);
                assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

                assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

                try {
                    d.addDeactivateSSResponse(request.getInvokeId(), null);

                } catch (MAPException e) {
                    this.error("Error while adding DeactivateSSResponse", e);
                    fail("Error while adding DeactivateSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeactivateSSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty DeactivateSSResponse", e);
                    fail("Error while sending the empty DeactivateSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.DeactivateSS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DeactivateSSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DeactivateSS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.DeactivateSSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);
        
        client.sendDeactivateSS();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + InterrogateSSRequest
     * TC-END + InterrogateSSResponse
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testInterrogateSSRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onInterrogateSSResponse(InterrogateSSResponse ind) {
                super.onInterrogateSSResponse(ind);

                assertTrue(ind.getGenericServiceInfo().getSsStatus().getPBit());
                assertFalse(ind.getGenericServiceInfo().getSsStatus().getABit());
                assertFalse(ind.getGenericServiceInfo().getSsStatus().getQBit());
                assertFalse(ind.getGenericServiceInfo().getSsStatus().getRBit());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onInterrogateSSRequest(InterrogateSSRequest request) {
                super.onInterrogateSSRequest(request);

                MAPDialogSupplementary d = request.getMAPDialog();

                assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
                assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
                assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(), NumberingPlan.land_mobile);
                assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

                assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

                try {
                    SSStatus ssStatus = this.mapParameterFactory.createSSStatus(false, true, false, false);
                    GenericServiceInfo genericServiceInfo = this.mapParameterFactory.createGenericServiceInfo(ssStatus, null, null, null, null, null, null,
                            null);
                    d.addInterrogateSSResponse_GenericServiceInfo(request.getInvokeId(), genericServiceInfo);

                } catch (MAPException e) {
                    this.error("Error while adding InterrogateSSResponse", e);
                    fail("Error while adding InterrogateSSResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.InterrogateSSResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty InterrogateSSResponse", e);
                    fail("Error while sending the empty DeactivateSSResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.InterrogateSS, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InterrogateSSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.InterrogateSS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.InterrogateSSResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

//        this.saveTrafficInFile();

        client.sendInterrogateSS();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + ReadyForSMRequest
     * TC-END + ReadyForSMResponse
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testReadyForSMRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onReadyForSMResponse(ReadyForSMResponse ind) {
                super.onReadyForSMResponse(ind);

                assertNull(ind.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onReadyForSMRequest(ReadyForSMRequest request) {
                super.onReadyForSMRequest(request);

                MAPDialogSms d = request.getMAPDialog();

                assertEquals(request.getImsi().getData(), "88888777773333");
                assertEquals(request.getAlertReason(), AlertReason.memoryAvailable);

                try {
                    d.addReadyForSMResponse(request.getInvokeId(), null);

                } catch (MAPException e) {
                    this.error("Error while adding ReadyForSMResponse", e);
                    fail("Error while adding ReadyForSMResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReadyForSMResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty ReadyForSMResponse", e);
                    fail("Error while sending the empty ReadyForSMResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ReadyForSM, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReadyForSMResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ReadyForSM, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ReadyForSMResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendReadyForSM();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + noteSubscriberPresent
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testNoteSubscriberPresentRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {
        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
                super.onNoteSubscriberPresentRequest(request);

                assertEquals(request.getIMSI().getData(), "88888777773333");
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                mapDialog.release();
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.NoteSubscriberPresent, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, stamp);
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.NoteSubscriberPresent, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendNoteSubscriberPresent();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + SendRoutingInfoForGprsRequest
     * TC-END + SendRoutingInfoForGprsResponse
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testSendRoutingInfoForGprsRequest() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onSendRoutingInfoForGprsResponse(SendRoutingInfoForGprsResponse ind) {
                super.onSendRoutingInfoForGprsResponse(ind);

                byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
                assertEquals(ind.getSgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
                assertEquals(ind.getSgsnAddress().getGSNAddressData(), addressData);
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onSendRoutingInfoForGprsRequest(SendRoutingInfoForGprsRequest request) {
                super.onSendRoutingInfoForGprsRequest(request);

                MAPDialogPdpContextActivation d = request.getMAPDialog();

                byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
                assertEquals(request.getImsi().getData(), "88888777773333");
                assertEquals(request.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
                assertEquals(request.getGgsnAddress().getGSNAddressData(), addressData);
                assertEquals(request.getGgsnNumber().getAddress(), "31628838002");

                try {
                    GSNAddress sgsnAddress = this.mapParameterFactory.createGSNAddress(GSNAddressAddressType.IPv4, addressData);
                    d.addSendRoutingInfoForGprsResponse(request.getInvokeId(), sgsnAddress, null, null, null);
//                    GSNAddress sgsnAddress, GSNAddress ggsnAddress, Integer mobileNotReachableReason,
//                    MAPExtensionContainer extensionContainer

                } catch (MAPException e) {
                    this.error("Error while adding SendRoutingInfoForGprsResponse", e);
                    fail("Error while adding SendRoutingInfoForGprsResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForGprsResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty SendRoutingInfoForGprsResponse", e);
                    fail("Error while sending the empty SendRoutingInfoForGprsResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInfoForGprs, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForGprsResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForGprs, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.SendRoutingInfoForGprsResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendSendRoutingInfoForGprsRequest();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + ActivateTraceModeRequest (OAM ACN)
     * TC-END + ActivateTraceModeResponse (empty)
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testActivateTraceModeRequest_Oam() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onActivateTraceModeResponse_Oam(ActivateTraceModeResponse_Oam ind) {
                super.onActivateTraceModeResponse_Oam(ind);

                assertNull(ind.getExtensionContainer());
                assertFalse(ind.getTraceSupportIndicator());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onActivateTraceModeRequest_Oam(ActivateTraceModeRequest_Oam request) {
                super.onActivateTraceModeRequest_Oam(request);

                MAPDialogOam d = request.getMAPDialog();

                assertEquals(request.getImsi().getData(), "88888777773333");

                byte[] traceReferenceData = new byte[] { 19 };
                assertEquals(request.getTraceReference().getData(), traceReferenceData);
                assertEquals(request.getTraceType().getData(), 21);

                try {
                    d.addActivateTraceModeResponse(request.getInvokeId(), null, false);
                    // MAPExtensionContainer extensionContainer, boolean traceSupportIndicator

                } catch (MAPException e) {
                    this.error("Error while adding addActivateTraceModeResponseResponse", e);
                    fail("Error while adding addActivateTraceModeResponseResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceModeResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty addActivateTraceModeResponseResponse", e);
                    fail("Error while sending the empty addActivateTraceModeResponseResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ActivateTraceMode, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ActivateTraceModeResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ActivateTraceMode, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ActivateTraceModeResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendActivateTraceModeRequest_Oam();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + ActivateTraceModeRequest (Mobility ACN)
     * TC-END + ActivateTraceModeResponse (with primitive)
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testActivateTraceModeRequest_Mobility() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility ind) {
                super.onActivateTraceModeResponse_Mobility(ind);

                assertNull(ind.getExtensionContainer());
                assertTrue(ind.getTraceSupportIndicator());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility request) {
                super.onActivateTraceModeRequest_Mobility(request);

                MAPDialogMobility d = request.getMAPDialog();

                assertEquals(request.getImsi().getData(), "88888777773333");

                byte[] traceReferenceData = new byte[] { 19 };
                assertEquals(request.getTraceReference().getData(), traceReferenceData);
                assertEquals(request.getTraceType().getData(), 21);

                try {
                    d.addActivateTraceModeResponse(request.getInvokeId(), null, true);
                    // MAPExtensionContainer extensionContainer, boolean traceSupportIndicator

                } catch (MAPException e) {
                    this.error("Error while adding addActivateTraceModeResponseResponse", e);
                    fail("Error while adding addActivateTraceModeResponseResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceModeResp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty addActivateTraceModeResponseResponse", e);
                    fail("Error while sending the empty addActivateTraceModeResponseResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.ActivateTraceMode, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ActivateTraceModeResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.ActivateTraceMode, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.ActivateTraceModeResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendActivateTraceModeRequest_Mobility();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + RegisterPasswordRequest
     *   TC-CONTINUE + GetPasswordRequest
     * TC-CONTINUE + GetPasswordResponse
     *   TC-END + RegisterPasswordResponse
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testRegisterPassword_GetPassword() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            private int dialogStep = 0;
            private long getPasswordInvokeId;

            @Override
            public void onGetPasswordRequest(GetPasswordRequest ind) {
                super.onGetPasswordRequest(ind);

                getPasswordInvokeId = ind.getInvokeId();

                assertNotNull(ind.getLinkedId());
                assertNotNull(ind.getLinkedInvoke());

                assertEquals(ind.getGuidanceInfo(), GuidanceInfo.enterNewPW);
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);

                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        Password password = this.mapParameterFactory.createPassword("9876");
                        ((MAPDialogSupplementary) mapDialog).addGetPasswordResponse(getPasswordInvokeId, password);
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.GetPasswordResp, null,
                                sequence++));

                        mapDialog.send();
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Erro while trying to send UnstructuredSSResponse");
                }
            }

            @Override
            public void onRegisterPasswordResponse(RegisterPasswordResponse ind) {
                super.onRegisterPasswordResponse(ind);

                assertEquals(ind.getPassword().getData(), "5555");
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            private int dialogStep = 0;
            private long registerPasswordInvokeId;

            @Override
            public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
                super.onRegisterPasswordRequest(request);

                MAPDialogSupplementary d = request.getMAPDialog();

                assertEquals(request.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allCondForwardingSS);

                registerPasswordInvokeId = request.getInvokeId();

                try {
                    d.addGetPasswordRequest(registerPasswordInvokeId, GuidanceInfo.enterNewPW);

                } catch (MAPException e) {
                    this.error("Error while adding addGetPasswordRequest", e);
                    fail("Error while adding addGetPasswordRequest");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);

                this.dialogStep++;
                try {
                    if (this.dialogStep == 1) {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.GetPassword, null,
                                sequence++));
                        mapDialog.send();
                    } else {
                        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterPasswordResp, null,
                                sequence++));
                        mapDialog.close(false);
                    }
                } catch (MAPException e) {
                    this.error("Error while trying to send Response", e);
                    fail("Erro while trying to send UnstructuredSSResponse");
                }
            }

            @Override
            public void onGetPasswordResponse(GetPasswordResponse request) {
                super.onGetPasswordResponse(request);

                MAPDialogSupplementary d = request.getMAPDialog();

                assertEquals(request.getPassword().getData(), "9876");
                
                try {
                    Password password = this.mapParameterFactory.createPassword("5555");
                    d.addRegisterPasswordResponse(registerPasswordInvokeId, password);

                } catch (MAPException e) {
                    this.error("Error while adding addRegisterPasswordResponse", e);
                    fail("Error while adding addRegisterPasswordResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.RegisterPassword, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.GetPassword, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.GetPasswordResp, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RegisterPasswordResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.RegisterPassword, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.GetPassword, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.GetPasswordResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.RegisterPasswordResp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        client.sendRegisterPassword();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * <code>
     * TC-BEGIN + AuthenticationFailureReportRequest
     * TC-END + AuthenticationFailureReportResponse (without parameter)
     * </code>
     */
    @Test(groups = { "functional.flow", "dialog" })
    public void testAuthenticationFailureReport() throws Exception {

        Client client = new Client(stack1, this, peer1Address, peer2Address) {

            @Override
            public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse request) {
                super.onAuthenticationFailureReportResponse(request);

                assertNull(request.getExtensionContainer());
            }

        };

        Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

            @Override
            public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest ind) {
                super.onAuthenticationFailureReportRequest(ind);

                MAPDialogMobility d = ind.getMAPDialog();

                assertEquals(ind.getImsi().getData(), "88888777773333");
                assertEquals(ind.getFailureCause(), FailureCause.wrongNetworkSignature);
                assertNull(ind.getExtensionContainer());

                try {
                    d.addAuthenticationFailureReportResponse(ind.getInvokeId(), null);

                } catch (MAPException e) {
                    this.error("Error while adding addAuthenticationFailureReportResponse", e);
                    fail("Error while adding addAuthenticationFailureReportResponse");
                }
            }

            @Override
            public void onDialogDelimiter(MAPDialog mapDialog) {
                super.onDialogDelimiter(mapDialog);
                try {
                    this.observerdEvents.add(TestEvent.createSentEvent(EventType.AuthenticationFailureReport_Resp, null, sequence++));
                    mapDialog.close(false);
                } catch (MAPException e) {
                    this.error("Error while sending the empty addAuthenticationFailureReportResponse", e);
                    fail("Error while sending the empty addAuthenticationFailureReportResponse");
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int count = 0;
        // Client side events
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.AuthenticationFailureReport, null, count++, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AuthenticationFailureReport_Resp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        clientExpectedEvents.add(te);

        count = 0;
        // Server side events
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.AuthenticationFailureReport, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

        te = TestEvent.createSentEvent(EventType.AuthenticationFailureReport_Resp, null, count++, stamp);
        serverExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
        serverExpectedEvents.add(te);

//        this.saveTrafficInFile();

        client.sendAuthenticationFailureReport();

        waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    private void waitForEnd() {
        try {
            Thread.currentThread().sleep(_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            fail("Interrupted on wait!");
        }
    }

}
