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

package org.mobicents.protocols.ss7.tools.traceparser;

import javolution.util.FastMap;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.*;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPServiceGprsListener;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CancelGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.EventReportGPRSResponse;
import org.mobicents.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener;
import org.mobicents.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.ContinueSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.impl.CircuitManagerImpl;
import org.mobicents.protocols.ss7.isup.impl.ISUPProviderImpl;
import org.mobicents.protocols.ss7.isup.impl.ISUPStackImpl;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPServiceOamListener;
import org.mobicents.protocols.ss7.map.api.service.oam.SendImsiRequest;
import org.mobicents.protocols.ss7.map.api.service.oam.SendImsiResponse;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivationListener;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsCommandTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsStatusReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.tcap.DialogImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.Utils;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SS7TraceParser implements TraceReaderListener, MAPDialogListener, CAPDialogListener, TCListener, Runnable,
        ProcessControl, MAPServiceMobilityListener, MAPServiceCallHandlingListener, MAPServiceOamListener,
        MAPServicePdpContextActivationListener, MAPServiceSupplementaryListener, MAPServiceSmsListener, MAPServiceLsmListener,
        CAPServiceCircuitSwitchedCallListener, CAPServiceGprsListener, CAPServiceSmsListener, ISUPListener {

    private Ss7ParseParameters par;
    private Thread t;
    private boolean taskIsFinished = false;
    private boolean needInterrupt = false;
    private String errorMessage = null;
    private PrintWriter pw;
    private DialogImpl curTcapDialog;
    private int msgCount;
    private String persistDir;

    private TraceReaderDriver driver;
    private TCAPProviderImplWrapper tcapProvider;
    private TCAPStackImplWrapper tcapStack;
    private TCAPCounterProvider tcapCntProv;
    private SccpProviderWrapper sccpProvider;
    private SccpStackImpl sccpStack = new SccpStackImpl("TraceParserSccpStack");
    private MAPProviderImpl mapProvider;
    private CAPProviderImpl capProvider;
    private MessageFactoryImpl msgFact;
    private Scheduler scheduler;
    private ISUPProviderImpl isupProvider;
    private ISUPStackImpl isupStack;

    private Map<Integer, Map<Long, DialogImplWrapper>> dialogs = new HashMap<Integer, Map<Long, DialogImplWrapper>>();
    private long dialogEnumerator = 0;
    private long tcapLogMsg = 0;
    private ArrayList<String> msgDetailBuffer = new ArrayList<String>();
//    private FastMap<String, AddrData> addressLst = new FastMap<String, AddrData>();
    private ArrayList<DialogMessage> lstDialogMessageChainSource = new ArrayList<DialogMessage>();

    public SS7TraceParser(String persistDir, Ss7ParseParameters par) {
        this.persistDir = persistDir;
        this.par = par;
    }

    public void parse() {
        this.t = new Thread(this);
        this.t.start();
    }

    @Override
    public void run() {

        String filePath = this.par.getSourceFilePath();

        switch (this.par.getFileTypeN()) {
            case Acterna:
                this.driver = new TraceReaderDriverActerna(this, filePath);
                break;
            case SimpleSeq:
                this.driver = new TraceReaderDriverSimpleSeq(this, filePath);
                break;
            case Pcap:
                this.driver = new TraceReaderDriverPcap(this, filePath);
                break;
            case HexStream:
                this.driver = new TraceReaderDriverHexStream(this, filePath);
                break;
            default:
                this.setFinishedState("Unknown TraceReaderDriver: " + this.par.getFileTypeN());
                return;
        }

        try {
            if (this.checkNeedInterrupt())
                return;

            // opening message log file
            String logFileName = par.getMsgLogFilePath();
            if (logFileName != null && !logFileName.equals("")) {
                try {
                    FileOutputStream fos = new FileOutputStream(logFileName);
                    pw = new PrintWriter(fos);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.setFinishedState("Exception while opening th message log file:\nFileName=" + logFileName
                            + "\nMessage=" + e.getMessage());
                    return;
                }
            }

            this.xLst.clear();

            if (this.par.getParseProtocol() == ParseProtocol.Map || this.par.getParseProtocol() == ParseProtocol.Cap) {
                this.sccpProvider = new SccpProviderWrapper(this.sccpStack);
                this.msgFact = new MessageFactoryImpl(this.sccpStack);
                this.tcapStack = new TCAPStackImplWrapper(this.sccpProvider, 1);

                this.tcapStack.setPersistDir(persistDir);
                this.tcapStack.setPreviewMode(true);
                this.tcapProvider = (TCAPProviderImplWrapper) this.tcapStack.getProvider();
                this.tcapProvider.addTCListener(this);

                this.tcapStack.start();
                this.tcapStack.setMaxDialogs(100000);

                // starting TCAP statistic counters
                this.tcapStack.setStatisticsEnabled(true);
                tcapCntProv = this.tcapStack.getCounterProvider();
                tcapCntProv.getOutgoingDialogsPerApplicatioContextName("a1");
                tcapCntProv.getIncomingDialogsPerApplicatioContextName("a1");
                tcapCntProv.getOutgoingInvokesPerOperationCode("a1");
                tcapCntProv.getIncomingInvokesPerOperationCode("a1");
                tcapCntProv.getOutgoingErrorsPerErrorCode("a1");
                tcapCntProv.getIncomingErrorsPerErrorCode("a1");
                tcapCntProv.getOutgoingRejectPerProblem("a1");
                tcapCntProv.getIncomingRejectPerProblem("a1");

                if (this.par.getParseProtocol() == ParseProtocol.Map) {
                    this.mapProvider = new MAPProviderImpl("Trace-Parser", this.tcapProvider);

                    this.mapProvider.getMAPServiceMobility().acivate();
                    this.mapProvider.getMAPServiceCallHandling().acivate();
                    this.mapProvider.getMAPServiceOam().acivate();
                    this.mapProvider.getMAPServicePdpContextActivation().acivate();
                    this.mapProvider.getMAPServiceSupplementary().acivate();
                    this.mapProvider.getMAPServiceSms().acivate();
                    this.mapProvider.getMAPServiceLsm().acivate();
                    this.mapProvider.getMAPServiceLsm().acivate();

                    this.mapProvider.addMAPDialogListener(this);
                    this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
                    this.mapProvider.getMAPServiceCallHandling().addMAPServiceListener(this);
                    this.mapProvider.getMAPServiceOam().addMAPServiceListener(this);
                    this.mapProvider.getMAPServicePdpContextActivation().addMAPServiceListener(this);
                    this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
                    this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);
                    this.mapProvider.getMAPServiceLsm().addMAPServiceListener(this);

                    this.mapProvider.start();
                } else {
                    this.capProvider = new CAPProviderImpl("Trace-Parser", this.tcapProvider);

                    this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
                    this.capProvider.getCAPServiceGprs().acivate();
                    this.capProvider.getCAPServiceSms().acivate();
                    this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
                    this.capProvider.getCAPServiceGprs().addCAPServiceListener(this);
                    this.capProvider.getCAPServiceSms().addCAPServiceListener(this);

                    this.capProvider.start();

                    this.capProvider.addCAPDialogListener(this);
                }
            } else {
                // ISUP
                this.scheduler = new Scheduler();
                DefaultClock clock = new DefaultClock();
                this.scheduler.setClock(clock);
                this.isupStack = new ISUPStackImpl(scheduler, 1, 2, false);
                this.isupProvider = (ISUPProviderImpl) this.isupStack.getIsupProvider();
                this.isupStack.setMtp3UserPart(new Mtp3UserPartProxy());
                this.isupStack.setCircuitManager(new CircuitManagerImpl());
                this.scheduler.start();
                this.isupStack.start();
                this.isupProvider.addListener(this);
            }

            this.driver.addTraceListener(this);

            if (this.checkNeedInterrupt())
                return;

            this.driver.startTraceFile();

            this.setFinishedState(null);

        } catch (Exception e) {
            e.printStackTrace();
            this.setFinishedState("Exception while parsing: " + e.getMessage());
        } finally {
            if (this.pw != null)
                this.pw.close();
            if (this.mapProvider != null)
                this.mapProvider.stop();
            if (this.capProvider != null)
                this.capProvider.stop();
            if (this.tcapStack != null)
                this.tcapStack.stop();
            if (this.driver != null)
                this.driver.removeTraceListener(this);
        }
    }

    public void uppendStringLongPull(StringBuilder sb, Map<String, LongValue> data, String title) {
        sb.append("\n");
        sb.append(title);
        sb.append("\n");
        for (String s : data.keySet()) {
            LongValue lv = data.get(s);
            sb.append(s);
            sb.append("\t");
            sb.append(lv.getValue());
            sb.append("\n");
        }
    }

    public String getStatisticData() {

        StringBuilder sb = new StringBuilder();

        if (this.tcapCntProv != null) {
            sb.append("TCAP counters --------");
            sb.append("\n");
            sb.append("\n");
            sb.append("TcUniReceivedCount : \t" + this.tcapCntProv.getTcUniReceivedCount());
            sb.append("\n");
            sb.append("TcBeginReceivedCount : \t" + this.tcapCntProv.getTcBeginReceivedCount());
            sb.append("\n");
            sb.append("TcContinueReceivedCount : \t" + this.tcapCntProv.getTcContinueReceivedCount());
            sb.append("\n");
            sb.append("TcEndReceivedCount : \t" + this.tcapCntProv.getTcEndReceivedCount());
            sb.append("\n");
            sb.append("TcPAbortReceivedCount : \t" + this.tcapCntProv.getTcPAbortReceivedCount());
            sb.append("\n");
            sb.append("TcUserAbortReceivedCount : \t" + this.tcapCntProv.getTcUserAbortReceivedCount());
            sb.append("\n");

            sb.append("InvokeReceivedCount : \t" + this.tcapCntProv.getInvokeReceivedCount());
            sb.append("\n");
            sb.append("ReturnResultReceivedCount : \t" + this.tcapCntProv.getReturnResultReceivedCount());
            sb.append("\n");
            sb.append("ReturnResultLastReceivedCount : \t" + this.tcapCntProv.getReturnResultLastReceivedCount());
            sb.append("\n");
            sb.append("ReturnErrorReceivedCount : \t" + this.tcapCntProv.getReturnErrorReceivedCount());
            sb.append("\n");
            sb.append("RejectReceivedCount : \t" + this.tcapCntProv.getRejectReceivedCount());
            sb.append("\n");

            sb.append("AllEstablishedDialogsCount : \t" + this.tcapCntProv.getAllEstablishedDialogsCount());
            sb.append("\n");

            this.uppendStringLongPull(sb, this.tcapCntProv.getIncomingDialogsPerApplicatioContextName("a1"), "IncomingDialogsPerApplicatioContextName");
            this.uppendStringLongPull(sb, this.tcapCntProv.getIncomingInvokesPerOperationCode("a1"), "IncomingInvokesPerOperationCode");
            this.uppendStringLongPull(sb, this.tcapCntProv.getIncomingErrorsPerErrorCode("a1"), "IncomingErrorsPerErrorCode");
            this.uppendStringLongPull(sb, this.tcapCntProv.getIncomingRejectPerProblem("a1"), "IncomingRejectPerProblem");
        }

        return sb.toString();
    }

    private void setFinishedState(String errorMessage) {

        // finalizing DialogMessageChain
        if (this.par.getMessageChainFilePath() != null) {

            ArrayList<DialogMessageChain> lst1 = new ArrayList<DialogMessageChain>();
            FastMap<Long, DialogMessageChain> lst11 = new FastMap<Long, DialogMessageChain>();

            for (DialogMessage dm : this.lstDialogMessageChainSource) {
                DialogMessageChain dmc = lst11.get(dm.dialogId1);
                if (dmc == null && dm.dialogId2 != 0) {
                    dmc = lst11.get(dm.dialogId2);
                }
                if (dmc == null) {
                    dmc = new DialogMessageChain();
                    lst1.add(dmc);
                }
                dmc.setDialogId(dm.dialogId1);
                dmc.setDialogId(dm.dialogId2);
                lst11.put(dmc.dialogId1, dmc);
                if (dmc.dialogId2 != 0)
                    lst11.put(dmc.dialogId2, dmc);

                dmc.addDialogMessage(dm);
            }

            FastMap<DialogMessageChain, DialogMessageChain> lst2 = new FastMap<DialogMessageChain, DialogMessageChain>();
            try {
                for (DialogMessageChain dmc : lst1) {
                    if (dmc.checkFullDialog()) {
                        DialogMessageChain dmc2 = lst2.get(dmc);
                        if (dmc2 == null) {
                            dmc2 = dmc;
                            lst2.put(dmc, dmc);
                        }
                        dmc2.addChainCount();
                    }
                }
            } catch (Throwable e) {
                int g = 0;
                g++;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                FileOutputStream fos = new FileOutputStream(this.par.getMessageChainFilePath());
                pw = new PrintWriter(fos);
                for (DialogMessageChain dmc : lst2.values()) {
                    pw.append(dmc.toString());
                    pw.append("\n");
                }
                pw.close();
                fos.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (errorMessage != null)
            this.errorMessage = errorMessage;

        this.taskIsFinished = true;
    }

    @Override
    public void ss7Message(int si, int ni, int priority, int opc, int dpc, int sls, byte[] data) throws TraceReaderException {
        this.msgCount++;

        if (this.msgCount == 328628) {
            // TODO: for tests - remove it
            int fff = 0;
            fff++;
        }

        if (data == null || data.length < 3) {
            throw new TraceReaderException("Too little data in the raw data");
        }

        try {
            ByteArrayInputStream in0 = new ByteArrayInputStream(data);
            DataInputStream in = new DataInputStream(in0);

            int type = in.readUnsignedByte();
            try {
                SccpMessageImpl msg = msgFact.createMessage(type, opc, dpc, sls, in, this.par.getSccpProtocolVersion(), 0);
                if (msg != null && (msg instanceof SccpDataMessageImpl)) {
                    this.onMessage((SccpDataMessageImpl) msg, 0);
                } else {
                    // unknown sccp message type
                    return;
                }
            } catch (ParseException pe) {
                throw new TraceReaderException(pe);
            }
        } catch (IOException e) {
            throw new TraceReaderException("IOException: " + e.getMessage(), e);
        }
    }

    private void printIsupMsgData() {

        for (String s : this.msgDetailBuffer) {
            this.pw.print("--- ");
            this.pw.print(s);
            this.pw.println();
        }
        this.pw.println();
        this.pw.println();
        this.pw.flush();
    }

    HashMap<MessageReassemblyProcess, XUnitDataDef> xLst = new HashMap<MessageReassemblyProcess, XUnitDataDef>();

    private class XUnitDataDef {
        public int remainingSegm;
        public ArrayList<byte[]> dLst = new ArrayList<byte[]>();
    }

    public class MessageReassemblyProcess {
        private int segmentationLocalRef;
        private SccpAddress callingPartyAddress;

        public MessageReassemblyProcess(int segmentationLocalRef, SccpAddress callingPartyAddress) {
            this.segmentationLocalRef = segmentationLocalRef;
            this.callingPartyAddress = callingPartyAddress;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof MessageReassemblyProcess))
                return false;
            MessageReassemblyProcess x = (MessageReassemblyProcess) obj;
            if (this.segmentationLocalRef != x.segmentationLocalRef)
                return false;

            if (this.callingPartyAddress == null || x.callingPartyAddress == null)
                return false;

            return this.callingPartyAddress.equals(x.callingPartyAddress);
        }

        @Override
        public int hashCode() {
            return this.segmentationLocalRef;
        }
    }

    SccpDataMessageImpl sccpMessage;

    public void onMessage(SccpDataMessageImpl message, int seqControl) {
        sccpMessage = message;

        try {
            this.msgDetailBuffer.clear();
            this.curTcapDialog = null;

            byte[] data = message.getData();
            SccpAddress localAddress = message.getCalledPartyAddress();
            SccpAddress remoteAddress = message.getCallingPartyAddress();

            Segmentation sgm = message.getSegmentation();
            if (sgm != null) {
                if (sgm.isFirstSegIndication()) {
                    if (sgm.getRemainingSegments() > 0) {
                        MessageReassemblyProcess mrp = new MessageReassemblyProcess(sgm.getSegmentationLocalRef(),
                                message.getCallingPartyAddress());
                        XUnitDataDef dd = new XUnitDataDef();
                        dd.remainingSegm = sgm.getRemainingSegments();
                        dd.dLst.add(data);
                        xLst.put(mrp, dd);

                        return;
                    }
                } else {
                    MessageReassemblyProcess mrp = new MessageReassemblyProcess(sgm.getSegmentationLocalRef(),
                            message.getCallingPartyAddress());
                    XUnitDataDef dd = xLst.get(mrp);
                    if (dd == null)
                        return;
                    dd.remainingSegm--;
                    if (dd.remainingSegm != sgm.getRemainingSegments())
                        return;
                    dd.dLst.add(data);

                    if (dd.remainingSegm > 0)
                        return;
                    xLst.remove(mrp);
                    ByteArrayOutputStream stm = new ByteArrayOutputStream();
                    for (byte[] buf : dd.dLst) {
                        stm.write(buf);
                    }
                    data = stm.toByteArray();
                }
            }
            message.setData(data);

            if (this.par.getMessageChainFilePath() != null) {
                DialogMessage dm = new DialogMessage();
                dm.opc = message.getIncomingOpc();
                dm.dpc = message.getIncomingDpc();
                dm.callingSsn = message.getCallingPartyAddress().getSubsystemNumber();
                dm.calledSsn = message.getCalledPartyAddress().getSubsystemNumber();

                GlobalTitle gt = message.getCallingPartyAddress().getGlobalTitle();
                if (gt != null)
                    dm.callingPA = gt.getDigits();
                gt = message.getCalledPartyAddress().getGlobalTitle();
                if (gt != null)
                    dm.calledPA = gt.getDigits();

                byte[] dataT = message.getData();
                AsnInputStream ais2 = new AsnInputStream(dataT);
                int tag2 = ais2.readTag();
                switch (tag2) {
                case TCBeginMessage._TAG:
                    dm.tcapMessage = "TC-BEGIN";
                    try {
                        TCBeginMessage tbm = TcapFactory.createTCBeginMessage(ais2);
                        dm.dialogId1 = Utils.decodeTransactionId(tbm.getOriginatingTransactionId());
                    } catch (Exception e) {
                    }
                    break;
                case TCContinueMessage._TAG:
                    dm.tcapMessage = "TC-CONTINUE";
                    try {
                        TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais2);
                        dm.dialogId1 = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
                        dm.dialogId2 = Utils.decodeTransactionId(tcm.getDestinationTransactionId());
                    } catch (Exception e) {
                    }
                    break;
                case TCEndMessage._TAG:
                    dm.tcapMessage = "TC-END";
                    try {
                        TCEndMessage tbm = TcapFactory.createTCEndMessage(ais2);
                        dm.dialogId1 = Utils.decodeTransactionId(tbm.getDestinationTransactionId());
                    } catch (Exception e) {
                    }
                    break;
                case TCAbortMessage._TAG:
                    dm.tcapMessage = "TC-ABORT";
                    try {
                        TCAbortMessage tbm = TcapFactory.createTCAbortMessage(ais2);
                        dm.dialogId1 = Utils.decodeTransactionId(tbm.getDestinationTransactionId());
                    } catch (Exception e) {
                    }
                    break;
                }
                dm.messageNum = this.msgCount;

                this.lstDialogMessageChainSource.add(dm);
            }

            if (this.par.getOpcDpcFilter() != null) {
                boolean inSet = false;
                for (Integer I1 : this.par.getOpcDpcFilter()) {
                    if (I1 != null) {
                        int i1 = I1;
                        if (message.getIncomingOpc() == i1 || message.getIncomingDpc() == i1) {
                            inSet = true;
                            break;
                        }
                    }
                }

                if (inSet) {
                    this.tcapProvider.onMessage(message);
                }
            } else {
                this.tcapProvider.onMessage(message);
            }

            if (this.curTcapDialog == null || this.curTcapDialog.getPrevewDialogData() == null
                    || this.curTcapDialog.getPrevewDialogData().getUpperDialog() == null) {
                return;
            }

            AsnInputStream ais = new AsnInputStream(data);
            AsnInputStream aisMsg = new AsnInputStream(data);

            int tag = ais.readTag();

            switch (tag) {
                case TCContinueMessage._TAG:
                    if (this.pw != null) {
                        TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
                        long originatingTransactionId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
                        long destinationTransactionId = Utils.decodeTransactionId(tcm.getDestinationTransactionId());

                        if (!CheckTransactionIdFilter(this.curTcapDialog.getApplicationContextName()))
                            return;
                        if (!CheckDialogIdFilter(destinationTransactionId, originatingTransactionId))
                            return;

                        this.pw.print("TC-CONTINUE: ");
                        this.printAddresses(message, originatingTransactionId, destinationTransactionId);
                        if (this.par.getTcapMsgData()) {
                            LogDataTag(aisMsg, "Continue", tcm.getComponent(), this.curTcapDialog.getApplicationContextName(),
                                    tcm.getDialogPortion());
                        }
                        this.printMsgData();
                    }
                    break;

                case TCBeginMessage._TAG:
                    if (this.pw != null) {
                        TCBeginMessage tcb = TcapFactory.createTCBeginMessage(ais);
                        long originatingTransactionId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId());

                        if (!CheckTransactionIdFilter(this.curTcapDialog.getApplicationContextName()))
                            return;
                        if (!CheckDialogIdFilter(originatingTransactionId, originatingTransactionId))
                            return;

                        this.pw.print("TC-BEGIN: ");
                        this.printAddresses(message, originatingTransactionId, -1);
                        if (this.par.getTcapMsgData()) {
                            LogDataTag(aisMsg, "Begin", tcb.getComponent(), this.curTcapDialog.getApplicationContextName(),
                                    tcb.getDialogPortion());
                        }
                        this.printMsgData();
                    }
                    break;

                case TCEndMessage._TAG:
                    if (this.pw != null) {
                        TCEndMessage teb = TcapFactory.createTCEndMessage(ais);
                        long destinationTransactionId = Utils.decodeTransactionId(teb.getDestinationTransactionId());

                        if (!CheckTransactionIdFilter(this.curTcapDialog.getApplicationContextName()))
                            return;
                        if (!CheckDialogIdFilter(destinationTransactionId, destinationTransactionId))
                            return;

                        this.pw.print("TC-END: ");
                        this.printAddresses(message, -1, destinationTransactionId);
                        if (this.par.getTcapMsgData()) {
                            LogDataTag(aisMsg, "End", teb.getComponent(), this.curTcapDialog.getApplicationContextName(),
                                    teb.getDialogPortion());
                        }
                        this.printMsgData();
                    }
                    break;

                case TCAbortMessage._TAG:
                    if (this.pw != null) {
                        TCAbortMessage tub = TcapFactory.createTCAbortMessage(ais);
                        long destinationTransactionId = Utils.decodeTransactionId(tub.getDestinationTransactionId());

                        if (!CheckTransactionIdFilter(this.curTcapDialog.getApplicationContextName()))
                            return;
                        if (!CheckDialogIdFilter(destinationTransactionId, destinationTransactionId))
                            return;

                        this.pw.print("TC-ABORT: ");
                        this.printAddresses(message, -1, destinationTransactionId);
                        if (this.par.getTcapMsgData()) {
                            LogDataTag(aisMsg, "Abort", null, this.curTcapDialog.getApplicationContextName(),
                                    tub.getDialogPortion());
                        }
                        this.printMsgData();

                        this.pw.println();
                        this.pw.flush();
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMessageX(SccpDataMessageImpl message, int seqControl) {
        // try {
        // this.msgDetailBuffer.clear();
        //
        // byte[] data = message.getData();
        // SccpAddress localAddress = message.getCalledPartyAddress();
        // SccpAddress remoteAddress = message.getCallingPartyAddress();
        //
        // Segmentation sgm = message.getSegmentation();
        // if (sgm != null) {
        // if (sgm.isFirstSegIndication()) {
        // if (sgm.getRemainingSegments() > 0) {
        // MessageReassemblyProcess mrp = new MessageReassemblyProcess(sgm.getSegmentationLocalRef(),
        // message.getCallingPartyAddress());
        // XUnitDataDef dd = new XUnitDataDef();
        // dd.remainingSegm = sgm.getRemainingSegments();
        // dd.dLst.add(data);
        // xLst.put(mrp, dd);
        //
        // return;
        // }
        // } else {
        // MessageReassemblyProcess mrp = new MessageReassemblyProcess(sgm.getSegmentationLocalRef(),
        // message.getCallingPartyAddress());
        // XUnitDataDef dd = xLst.get(mrp);
        // if (dd == null)
        // return;
        // dd.remainingSegm--;
        // if (dd.remainingSegm != sgm.getRemainingSegments())
        // return;
        // dd.dLst.add(data);
        //
        // if (dd.remainingSegm > 0)
        // return;
        // xLst.remove(mrp);
        // ByteArrayOutputStream stm = new ByteArrayOutputStream();
        // for (byte[] buf : dd.dLst) {
        // stm.write(buf);
        // }
        // data = stm.toByteArray();
        // }
        // }
        //
        // // asnData - it should pass
        // AsnInputStream ais = new AsnInputStream(data);
        // AsnInputStream aisMsg = new AsnInputStream(data);
        //
        // // this should have TC message tag :)
        // int tag = ais.readTag();
        //
        // switch (tag) {
        // // continue first, usually we will get more of those. small perf
        // // boost
        // case TCContinueMessage._TAG: {
        // TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
        // // received continue, destID == localDialogId(originatingTxId of
        // // begin);
        // long originatingTransactionId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
        // long destinationTransactionId = Utils.decodeTransactionId(tcm.getDestinationTransactionId());
        // int dpc = message.getIncomingDpc();
        // Map<Long, DialogImplWrapper> dpcData = this.dialogs.get(dpc);
        // int acnValue = 0;
        // int acnVersion = 0;
        // if (dpcData != null) {
        // DialogImplWrapper di = dpcData.get(destinationTransactionId);
        // if (di != null) {
        // int opc = message.getIncomingOpc();
        // Map<Long, DialogImplWrapper> opcData = this.dialogs.get(opc);
        // if (opcData == null) {
        // opcData = new HashMap<Long, DialogImplWrapper>();
        // this.dialogs.put(opc, opcData);
        // }
        // opcData.put(originatingTransactionId, di);
        //
        // acnValue = di.getAcnValue();
        // acnVersion = di.getAcnVersion();
        //
        // di.SetStateActive();
        //
        // Integer applicationContextFilter = par.getApplicationContextFilter();
        // if (applicationContextFilter != null && applicationContextFilter != acnValue)
        // return;
        //
        // if (!CheckDialogIdFilter(originatingTransactionId, destinationTransactionId))
        // return;
        //
        // di.curOpc = message.getIncomingOpc();
        // di.processContinue(tcm, localAddress, remoteAddress);
        //
        // if (this.pw != null) {
        // this.pw.print("TC-CONTINUE: ");
        // this.printAddresses(message, originatingTransactionId, destinationTransactionId);
        // if (this.par.getTcapMsgData()) {
        // LogDataTag(aisMsg, "Continue", tcm.getComponent(), acnValue, acnVersion, tcm.getDialogPortion());
        // }
        // this.printMsgData();
        // }
        // }
        // }
        //
        // break;
        // }
        //
        // case TCBeginMessage._TAG: {
        // TCBeginMessage tcb = TcapFactory.createTCBeginMessage(ais);
        // DialogImplWrapper di = new DialogImplWrapper(localAddress, remoteAddress, ++this.dialogEnumerator, true,
        // this.tcapProvider.getExecuter(),
        // this.tcapProvider, 0);
        // long originatingTransactionId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId());
        // int opc = message.getIncomingOpc();
        // Map<Long, DialogImplWrapper> opcData = this.dialogs.get(opc);
        // if (opcData == null) {
        // opcData = new HashMap<Long, DialogImplWrapper>();
        // this.dialogs.put(opc, opcData);
        // }
        // opcData.put(originatingTransactionId, di);
        //
        // DialogPortion dp = tcb.getDialogPortion();
        // if (dp != null) {
        // DialogAPDU apduN = dp.getDialogAPDU();
        // if (apduN instanceof DialogRequestAPDU) {
        // DialogRequestAPDU apdu = (DialogRequestAPDU) apduN;
        // ApplicationContextName acnV = apdu.getApplicationContextName();
        // if (acnV != null) {
        // if (acnV.getOid()[5] == 0) {
        // di.setAcnValue(((int) acnV.getOid()[6]));
        // di.setAcnVersion(((int) acnV.getOid()[7]));
        // } else {
        // di.setAcnValue(((int) acnV.getOid()[7]));
        // di.setAcnVersion(((int) acnV.getOid()[5]));
        // }
        // }
        // }
        // }
        //
        // Integer applicationContextFilter = par.getApplicationContextFilter();
        // if (applicationContextFilter != null && applicationContextFilter != di.getAcnValue())
        // return;
        //
        // if (!CheckDialogIdFilter(originatingTransactionId, Integer.MIN_VALUE))
        // return;
        //
        // di.curOpcOrig = message.getIncomingOpc();
        // di.curOpc = message.getIncomingOpc();
        // di.processBegin(tcb, localAddress, remoteAddress);
        //
        // if (this.pw != null) {
        // this.pw.print("TC-BEGIN: ");
        // this.printAddresses(message, originatingTransactionId, -1);
        // if (this.par.getTcapMsgData()) {
        // LogDataTag(aisMsg, "Begin", tcb.getComponent(), di.getAcnValue(), di.getAcnVersion(), tcb.getDialogPortion());
        // }
        // this.printMsgData();
        // }
        // break;
        // }
        //
        // case TCEndMessage._TAG: {
        // TCEndMessage teb = TcapFactory.createTCEndMessage(ais);
        // long destinationTransactionId = Utils.decodeTransactionId(teb.getDestinationTransactionId());
        //
        // int dpc = message.getIncomingDpc();
        // Map<Long, DialogImplWrapper> dpcData = this.dialogs.get(dpc);
        // int acnValue = 0;
        // int acnVersion = 0;
        // if (dpcData != null) {
        // DialogImplWrapper di = dpcData.get(destinationTransactionId);
        // if (di != null) {
        // dpcData.remove(destinationTransactionId);
        //
        // acnValue = di.getAcnValue();
        // acnVersion = di.getAcnVersion();
        //
        // Integer applicationContextFilter = par.getApplicationContextFilter();
        // if (applicationContextFilter != null && applicationContextFilter != acnValue)
        // return;
        //
        // if (!CheckDialogIdFilter(destinationTransactionId, Integer.MIN_VALUE))
        // return;
        //
        // di.curOpc = message.getIncomingOpc();
        // di.processEnd(teb, localAddress, remoteAddress);
        //
        // if (this.pw != null) {
        // this.pw.print("TC-END: ");
        // this.printAddresses(message, -1, destinationTransactionId);
        // if (this.par.getTcapMsgData()) {
        // LogDataTag(aisMsg, "End", teb.getComponent(), acnValue, acnVersion, teb.getDialogPortion());
        // }
        // this.printMsgData();
        // }
        // }
        // }
        //
        // break;
        // }
        //
        // case TCAbortMessage._TAG: {
        // TCAbortMessage tub = TcapFactory.createTCAbortMessage(ais);
        // long destinationTransactionId = Utils.decodeTransactionId(tub.getDestinationTransactionId());
        //
        // int dpc = message.getIncomingDpc();
        // Map<Long, DialogImplWrapper> dpcData = this.dialogs.get(dpc);
        // int acnValue = 0;
        // int acnVersion = 0;
        //
        // if (dpcData != null) {
        // DialogImplWrapper di = dpcData.get(destinationTransactionId);
        // if (di != null) {
        // acnValue = di.getAcnValue();
        // acnVersion = di.getAcnVersion();
        //
        // dpcData.remove(destinationTransactionId);
        //
        // Integer applicationContextFilter = par.getApplicationContextFilter();
        // if (applicationContextFilter != null && applicationContextFilter != acnValue)
        // return;
        //
        // if (!CheckDialogIdFilter(destinationTransactionId, Integer.MIN_VALUE))
        // return;
        //
        // di.curOpc = message.getIncomingOpc();
        // di.processAbort(tub, localAddress, remoteAddress);
        //
        // if (this.pw != null) {
        // this.pw.print("TC-ABORT: ");
        // this.printAddresses(message, -1, destinationTransactionId);
        // if (this.par.getTcapMsgData()) {
        // LogDataTag(aisMsg, "Abort", null, acnValue, acnVersion, tub.getDialogPortion());
        // }
        // this.printMsgData();
        // this.pw.println();
        // this.pw.flush();
        // }
        // }
        // }
        //
        // break;
        // }
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    private void printMsgData() {

        // if (this.msgDetailBuffer.size() > 1) {
        // int i1 = 0;
        // i1++;
        // }

        for (String s : this.msgDetailBuffer) {
            this.pw.print("--- ");
            this.pw.print(s);
            this.pw.println();
        }
        this.pw.println();
        this.pw.println();
        this.pw.flush();
    }

    private void printAddresses(SccpDataMessageImpl message, long originationTransactionId, long destinationTransactionId) {
        this.pw.print("OPC=" + message.getIncomingOpc() + ", DPC=" + message.getIncomingDpc());
        if (originationTransactionId != -1)
            this.pw.print(", originationTransactionId=" + originationTransactionId);
        if (destinationTransactionId != -1)
            this.pw.print(", destinationTransactionId=" + destinationTransactionId);
        this.pw.println();
        if (message.getCalledPartyAddress() != null)
            this.pw.print("CalledPartyAddress=" + message.getCalledPartyAddress().toString());
        if (message.getCallingPartyAddress() != null)
            this.pw.print(", CallingPartyAddress=" + message.getCallingPartyAddress().toString());

        // this.pw.print("OPC=" + message.getOpc() + ", DPC=" + message.getDpc() + ", destinationTransactionId=" +
        // destinationTransactionId);
    }

    private boolean CheckDialogIdFilter(long originatingTransactionId, long destinationTransactionId) {
        Long dialogIdFilter = par.getDialogIdFilter();
        Long dialogIdFilter2 = par.getDialogIdFilter2();
        if (dialogIdFilter == null && dialogIdFilter2 == null)
            return true;

        if (dialogIdFilter != null
                && (dialogIdFilter == originatingTransactionId || dialogIdFilter == destinationTransactionId))
            return true;

        if (dialogIdFilter2 != null
                && (dialogIdFilter2 == originatingTransactionId || dialogIdFilter2 == destinationTransactionId))
            return true;

        return false;
    }

    private boolean CheckTransactionIdFilter(ApplicationContextName acn) {
        Integer applicationContextFilter = par.getApplicationContextFilter();
        if (applicationContextFilter == null)
            return true;

        if (acn == null || acn.getOid().length != 8)
            return false;

        if (acn.getOid()[6] == applicationContextFilter)
            return true;

        return false;
    }

    private void LogDataTag(AsnInputStream aisMsg, String name, Component[] comp, ApplicationContextName acn, DialogPortion dp) {

        try {
            aisMsg.readTag();
            aisMsg.readLength();

            int pos = aisMsg.position();
            aisMsg.position(0);
            byte[] buf = new byte[pos];
            aisMsg.read(buf);

            this.writeDataArray(name + " tag+length: ", buf);

            while (aisMsg.available() > 0) {
                pos = aisMsg.position();
                int tag = aisMsg.readTag();
                String ttl = "???";
                switch (tag) {
                    case 8:
                        ttl = "OrigTransactionId";
                        break;
                    case 9:
                        ttl = "DestTransactionId";
                        break;
                    case 10:
                        ttl = "P-AbortCause";
                        break;
                    case 11:
                        ttl = "DialogPortion";
                        break;
                    case 12:
                        ttl = "ComponentPortion";
                        break;
                }
                int length = aisMsg.readLength();
                int newPos;
                if (length == Tag.Indefinite_Length) {
                    aisMsg.readIndefinite();
                    newPos = aisMsg.position();
                } else {
                    newPos = aisMsg.position() + length;
                }
                aisMsg.position(pos);
                buf = new byte[newPos - pos];
                aisMsg.read(buf);

                this.writeDataArray(ttl + ": ", buf);

                if (tag == 11 && this.par.getDetailedDialog())
                    this.LogDialog(dp, buf);
                if (tag == 12 && this.par.getDetailedComponents())
                    this.LogComponents(comp, acn, buf);
            }

            this.pw.println();

        } catch (Exception e) {
            e.printStackTrace();
            this.pw.println();
            this.pw.print("Exception parsing TCAP msg");
            this.pw.print(e.getMessage());
        }
    }

    private void writeDataArray(String title, byte[] data) {
        this.pw.println();
        this.pw.print(title);
        int i1 = 0;
        for (byte b : data) {
            if (i1 == 0)
                i1 = 1;
            else
                this.pw.print(",");
            this.pw.print(" ");

            if (b < 0)
                this.pw.print("(byte)");
            this.pw.print((int) (b & 0xFF));
        }
    }

    private void LogDialog(DialogPortion dp, byte[] logData) {

        AsnInputStream ais = new AsnInputStream(logData);
        try {
            int tag = ais.readTag();
            int length = ais.readLength();
            tag = ais.readTag();
            length = ais.readLength();
            int pos = ais.position();
            ais.position(0);
            byte[] buf = new byte[pos];
            ais.read(buf);
            this.writeDataArray("\tDialogPort+External tags: ", buf);

            tag = ais.readTag();
            long[] oid = ais.readObjectIdentifier();
            int pos2 = ais.position();
            ais.position(pos);
            buf = new byte[pos2 - pos];
            ais.read(buf);
            this.writeDataArray("\tExternal OId: ", buf);

            tag = ais.readTag();
            length = ais.readLength();
            int pos3 = ais.position();
            ais.position(pos2);
            buf = new byte[pos3 - pos2];
            ais.read(buf);
            this.writeDataArray("\tExternal Asn tag: ", buf);

            buf = new byte[ais.available()];
            ais.read(buf);
            this.writeDataArray("\t\tDialogAPDU_" + dp.getDialogAPDU().getType().toString(), buf);

            DialogAPDU apdu = dp.getDialogAPDU();
            if (apdu instanceof DialogRequestAPDU) {
                DialogRequestAPDU req = (DialogRequestAPDU) apdu;
                UserInformation userInfo = req.getUserInformation();
                if (userInfo != null) {
                    if (par.getParseProtocol() == ParseProtocol.Map) {
                        try {
                            byte[] asnData = userInfo.getEncodeType();

                            AsnInputStream aisx = new AsnInputStream(asnData);
                            int tagx = aisx.readTag();

                            // It should be MAP_OPEN Tag
                            if (tagx == MAPOpenInfoImpl.MAP_OPEN_INFO_TAG) {
                                MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
                                mapOpenInfoImpl.decodeAll(aisx);
                                AddressString destReference = mapOpenInfoImpl.getDestReference();
                                AddressString origReference = mapOpenInfoImpl.getOrigReference();

                                if (destReference != null) {
                                    this.pw.println();
                                    this.pw.print("DestReference: " + destReference);
                                }
                                if (origReference != null) {
                                    this.pw.println();
                                    this.pw.print("OrigReference: " + origReference);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void LogComponents(Component[] comp, ApplicationContextName acn, byte[] logData) {
        this.pw.println();
        this.pw.print("\tAcn: ");

        if (acn != null) {
            if (acn.getOid().length == 8 && acn.getOid()[0] == 0 && acn.getOid()[1] == 4 && acn.getOid()[2] == 0
                    && acn.getOid()[3] == 0 && acn.getOid()[4] == 1 && acn.getOid()[5] == 0) {
                this.pw.print(acn.getOid()[6] + "-" + acn.getOid()[7]);
            } else {
                boolean first = true;
                for (long i1 : acn.getOid()) {
                    this.pw.print(i1);
                    if (first)
                        first = false;
                    else
                        this.pw.print(".");
                }
            }
        } else
            this.pw.print("???");

        this.pw.print("\tComponents: ");
        if (comp != null) {
            int i1 = 0;
            for (Component c : comp) {
                if (i1 == 0)
                    i1 = 1;
                else
                    this.pw.print(", ");
                this.pw.print(c.getType());
                this.pw.print(":");
                switch (c.getType()) {
                    case Invoke:
                        Invoke inv = (Invoke) c;
                        this.LogOperationCode(inv.getOperationCode());
                        break;
                    case ReturnResult:
                        ReturnResult rr = (ReturnResult) c;
                        this.LogOperationCode(rr.getOperationCode());
                        break;
                    case ReturnResultLast:
                        ReturnResultLast rrl = (ReturnResultLast) c;
                        this.LogOperationCode(rrl.getOperationCode());
                        break;
                    case ReturnError:
                        ReturnError re = (ReturnError) c;
                        this.pw.print(re.getErrorCode());
                        break;
                    case Reject:
                        Reject rej = (Reject) c;
                        this.pw.print(rej.getClass());
                        break;
                }
            }
        }

        if (logData != null) {
            try {
                AsnInputStream ais = new AsnInputStream(logData);
                this.LogSequence(ais, 1, "Components");
            } catch (Exception e) {
                e.printStackTrace();
                this.pw.println();
                this.pw.print("Exception parsing TCAP components");
                this.pw.print(e.getMessage());
            }
        }

        // if (comp != null) {
        // for (Component c : comp) {
        // this.pw.print("\n");
        // this.pw.print(c.getType());
        // this.pw.print(": ");
        // }
        // }
    }

    private String LodSequenceName(int dep, int tag, int tagClass, int ind, String parent) {

        if (dep == 1) {
            switch (tag) {
                case 1:
                    return "Invoke";
                case 2:
                    return "ReturnResultLast";
                case 3:
                    return "ReturnError";
                case 4:
                    return "Reject";
                case 7:
                    return "ReturnResult";
            }
        }

        if (dep == 2) {
            if (ind == 0)
                return "invokeId";

            if (tag == Tag.INTEGER && tagClass == Tag.CLASS_UNIVERSAL)
                return "operationCode";

            if (tag == 0 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC) {
                if (parent.equals("Invoke"))
                    return "linkedId";
                else
                    return "GeneralProblem";
            }
            if (tag == 1 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC)
                return "InvokeProblem";
            if (tag == 2 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC)
                return "ReturnResultProblem";
            if (tag == 3 && tagClass == Tag.CLASS_CONTEXT_SPECIFIC)
                return "ReturnErrorProblem";

            if (parent.equals("ReturnResultLast") || parent.equals("ReturnResult")) {
                if (tag == Tag.SEQUENCE)
                    return "ReturnResultData";
            }

            if (parent.equals("ReturnError")) {
                if (ind == 0)
                    return "ErrorCode";
            }

            return "Parameter";
        }

        if (dep == 3) {
            if (parent.equals("ReturnResultData")) {
                if (tag == Tag.INTEGER && tagClass == Tag.CLASS_UNIVERSAL)
                    return "operationCode";
                else
                    return "Parameter";
            }
        }

        return "";
    }

    private void LogSequence(AsnInputStream ais, int dep, String name) throws IOException, AsnException {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dep; i++) {
            sb.append("\t");
        }
        String pref = sb.toString();

        int pos = ais.position();
        int tag = ais.readTag();
        int length = ais.readLength();
        int newPos = ais.position();
        byte[] buf = new byte[newPos - pos];
        ais.position(pos);
        ais.read(buf);
        this.writeDataArray(pref + name + ": tag+length: ", buf);

        int ind = 0;
        while (ais.available() > 0) {
            pos = ais.position();
            tag = ais.readTag();

            length = ais.readLength();

            boolean isConstr = !ais.isTagPrimitive();
            if (isConstr && length == Tag.Indefinite_Length) {
                ais.readSequenceData(length);
                newPos = ais.position();
            } else
                newPos = ais.position() + length;

            buf = new byte[newPos - pos];
            ais.position(pos);
            ais.read(buf);
            String name2 = this.LodSequenceName(dep, tag, ais.getTagClass(), ind, name);
            this.writeDataArray(pref + name2 + ": ", buf);

            if (isConstr) {

                AsnInputStream ais2 = new AsnInputStream(buf);
                this.LogSequence(ais2, dep + 1, name2);
            }

            ind++;
        }
    }

    private void LogOperationCode(OperationCode op) {
        this.pw.print("OpCode=");
        if (op != null)
            this.pw.print(op.getLocalOperationCode());
        else
            this.pw.print("???");
    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

        try {
            mapDialog.send();
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {

        if (((MAPDialogImpl) mapDialog).getTcapDialog() instanceof DialogImplWrapper) {
            DialogImplWrapper di = (DialogImplWrapper) ((MAPDialogImpl) mapDialog).getTcapDialog();
            if (mapDialog.getApplicationContext() != null) {
                di.setAcnValue(mapDialog.getApplicationContext().getApplicationContextName().getApplicationContextCode());
                di.setAcnVersion(mapDialog.getApplicationContext().getApplicationContextVersion().getVersion());
            }
        }
    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            IMSI eriImsi, AddressString eriVlrNo) {

        DialogImplWrapper di = (DialogImplWrapper) ((MAPDialogImpl) mapDialog).getTcapDialog();
        if (mapDialog.getApplicationContext() != null) {
            di.setAcnValue(mapDialog.getApplicationContext().getApplicationContextName().getApplicationContextCode());
            di.setAcnVersion(mapDialog.getApplicationContext().getApplicationContextVersion().getVersion());
        }
    }

    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

        mapDialog.keepAlive();
    }

    @Override
    public boolean isFinished() {
        return this.taskIsFinished;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public void interrupt() {
        this.needInterrupt = true;
    }

    @Override
    public boolean checkNeedInterrupt() {
        if (this.needInterrupt) {
            this.errorMessage = "User break";
            this.taskIsFinished = true;
            return true;
        } else
            return false;
    }

    @Override
    public int getMsgCount() {
        return this.msgCount;
    }

    private class LogData {
        public byte[] dialogPortion;
        public byte[] componentPortion;
    }

    @Override
    public void onDialogAccept(CAPDialog arg0, CAPGprsReferenceNumber arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(CAPDialog arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        // TODO Auto-generated method stub

        try {
            capDialog.send();
        } catch (CAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogNotice(CAPDialog arg0, CAPNoticeProblemDiagnostic arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogProviderAbort(CAPDialog arg0, PAbortCauseType arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber arg1) {
        if (((CAPDialogImpl) capDialog).getTcapDialog() instanceof DialogImplWrapper) {
            DialogImplWrapper di = (DialogImplWrapper) ((CAPDialogImpl) capDialog).getTcapDialog();
            if (capDialog.getApplicationContext() != null) {
                di.setAcnVersion(capDialog.getApplicationContext().getVersion().getVersion());
            }
        }

        // !!!!!!!!!!!!!!!!!!!! TODO - statistic
//        SccpAddress ao = capDialog.getRemoteAddress();
//        int opc = this.sccpMessage.getIncomingOpc();
//        int oSsn = ao.getSubsystemNumber();
//        String oAddr = "";
//        if (ao.getGlobalTitle() != null)
//            oAddr = ao.getGlobalTitle().getDigits();
//        SccpAddress ad = capDialog.getLocalAddress();
//        int dpc = this.sccpMessage.getIncomingDpc();
//        int dSsn = ad.getSubsystemNumber();
//        String dAddr = "";
//        if (ad.getGlobalTitle() != null)
//            dAddr = ad.getGlobalTitle().getDigits();
//
//        String key = opc + "_" + oSsn + "_" + oAddr + "_" + dpc + "_" + dSsn + "_" + dAddr;
//        AddrData res = addressLst.get(key);
//        if (res == null) {
//            res = new AddrData();
//            addressLst.put(key, res);
//
//            res.opc = opc;
//            res.oSsn = oSsn;
//            res.oAddr = oAddr;
//            res.dpc = dpc;
//            res.dSsn = dSsn;
//            res.dAddr = dAddr;
//        }
//        res.cnt++;
        // !!!!!!!!!!!!!!!!!!!! TODO - statistic
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        // TODO Auto-generated method stub

        capDialog.keepAlive();
    }

    @Override
    public void onDialogUserAbort(CAPDialog arg0, CAPGeneralAbortReason arg1, CAPUserAbortReason arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        msgDetailBuffer.add(mapErrorMessage.toString());
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        // TODO Auto-generated method stub

    }

    // SMS service listener
    private void parseSmsSignalInfo(SmsSignalInfo si, boolean isMo, boolean isMt) {

        if (si == null)
            return;

        if (isMo) {
            try {
                SmsTpdu tpdu = si.decodeTpdu(true);
                parseSmsTpdu(tpdu);
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (isMt) {
            try {
                SmsTpdu tpdu = si.decodeTpdu(false);
                parseSmsTpdu(tpdu);
            } catch (MAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void parseSmsTpdu(SmsTpdu tpdu) throws MAPException {

        UserData ud = null;
        switch (tpdu.getSmsTpduType()) {
            case SMS_DELIVER_REPORT: {
                SmsDeliverReportTpdu t = (SmsDeliverReportTpdu) tpdu;
                // ud = t.getUserData();
            }
                break;
            case SMS_SUBMIT: {
                SmsSubmitTpdu t = (SmsSubmitTpdu) tpdu;
                ud = t.getUserData();
            }
                break;
            case SMS_COMMAND: {
                SmsCommandTpdu t = (SmsCommandTpdu) tpdu;
            }
                break;
            case SMS_DELIVER: {
                SmsDeliverTpdu t = (SmsDeliverTpdu) tpdu;
                ud = t.getUserData();
            }
                break;
            case SMS_SUBMIT_REPORT: {
                SmsSubmitTpdu t = (SmsSubmitTpdu) tpdu;
                // ud = t.getUserData();
            }
                break;
            case SMS_STATUS_REPORT: {
                SmsStatusReportTpdu t = (SmsStatusReportTpdu) tpdu;
                // ud = t.getUserData();
            }
                break;
        }

        if (ud != null) {
            ud.decode();
            int i1 = 0;
            i1++;
        }
    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
        if (forwSmInd.getSM_RP_DA().getServiceCentreAddressDA() != null)
            this.parseSmsSignalInfo(forwSmInd.getSM_RP_UI(), true, false);
        else
            this.parseSmsSignalInfo(forwSmInd.getSM_RP_UI(), false, true);
    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
        this.parseSmsSignalInfo(moForwSmInd.getSM_RP_UI(), true, false);
    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
        this.parseSmsSignalInfo(moForwSmRespInd.getSM_RP_UI(), false, true);
    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
        this.parseSmsSignalInfo(mtForwSmInd.getSM_RP_UI(), false, true);
    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
        this.parseSmsSignalInfo(mtForwSmRespInd.getSM_RP_UI(), true, false);
    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
    }

    @Override
    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
    }

    @Override
    public void onMAPMessage(MAPMessage msg) {

        msgDetailBuffer.add(msg.toString());
    }

    @Override
    public void onDialogRelease(MAPDialog arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onErrorComponent(CAPDialog arg0, Long arg1, CAPErrorMessage arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(CAPDialog arg0, Long arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRejectComponent(CAPDialog arg0, Long arg1, Problem arg2, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCAPMessage(CAPMessage msg) {

        msgDetailBuffer.add(msg.toString());
    }

    @Override
    public void onDialogRelease(CAPDialog arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationRequestRequest(CallInformationRequestRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelRequest(CancelRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectRequest(ConnectRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse ind) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener#onAnyTimeInterrogationRequest(org.mobicents
     * .protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest)
     */
    @Override
    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener#onAnyTimeInterrogationResponse(org.mobicents
     * .protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse)
     */
    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInformationRequest(SendRoutingInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInformationResponse(SendRoutingInformationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onIstCommandRequest(IstCommandRequest request) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onIstCommandResponse(IstCommandResponse response) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogReleased(Dialog arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogTimeout(Dialog arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(Invoke arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTCBegin(TCBeginIndication ind) {
        this.curTcapDialog = (DialogImpl) ind.getDialog();
    }

    @Override
    public void onTCContinue(TCContinueIndication ind) {
        this.curTcapDialog = (DialogImpl) ind.getDialog();
    }

    @Override
    public void onTCEnd(TCEndIndication ind) {
        this.curTcapDialog = (DialogImpl) ind.getDialog();
    }

    @Override
    public void onTCNotice(TCNoticeIndication arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTCPAbort(TCPAbortIndication ind) {
        this.curTcapDialog = (DialogImpl) ind.getDialog();
    }

    @Override
    public void onTCUni(TCUniIndication ind) {
        this.curTcapDialog = (DialogImpl) ind.getDialog();
    }

    @Override
    public void onTCUserAbort(TCUserAbortIndication ind) {
        this.curTcapDialog = (DialogImpl) ind.getDialog();
    }

    @Override
    public void onInitialDpGprsRequest(InitialDpGprsRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingGPRSRequest(ApplyChargingGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEntityReleasedGPRSRequest(EntityReleasedGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEntityReleasedGPRSResponse(EntityReleasedGPRSResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectGPRSRequest(ConnectGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueGPRSRequest(ContinueGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseGPRSRequest(ReleaseGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerGPRSRequest(ResetTimerGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationGPRSRequest(FurnishChargingInformationGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelGPRSRequest(CancelGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendChargingInformationGPRSRequest(SendChargingInformationGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportGPRSRequest(ApplyChargingReportGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportGPRSResponse(ApplyChargingReportGPRSResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportGPRSRequest(EventReportGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportGPRSResponse(EventReportGPRSResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityTestGPRSRequest(ActivityTestGPRSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivityTestGPRSResponse(ActivityTestGPRSResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectSMSRequest(ConnectSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportSMSRequest(EventReportSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationSMSRequest(FurnishChargingInformationSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPSMSRequest(InitialDPSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseSMSRequest(ReleaseSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestReportSMSEventRequest(RequestReportSMSEventRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerSMSRequest(ResetTimerSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueSMSRequest(ContinueSMSRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse arg0) {
        // TODO Auto-generated method stub

    }

    public class AddrData {
        long cnt;

        int opc;
        int oSsn;
        String oAddr;
        int dpc;
        int dSsn;
        String dAddr;
    }

    @Override
    public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectLegRequest(DisconnectLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectLegResponse(DisconnectLegResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse initiateCallAttemptResponse) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoveLegRequest(MoveLegRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMoveLegResponse(MoveLegResponse ind) {
        // TODO Auto-generated method stub

    }

    public class Mtp3TransferPrimitiveProxy extends Mtp3TransferPrimitive {
        public Mtp3TransferPrimitiveProxy(int si, int ni, int mp, int opc, int dpc, int sls, byte[] data, RoutingLabelFormat pointCodeFormat) {
            super(si, ni, mp, opc, dpc, sls, data, pointCodeFormat);
        }
    }

    @Override
    public void onEvent(ISUPEvent event) {

        msgDetailBuffer.add(event.toString());

        printIsupMsgData();
    }

    @Override
    public void onTimeout(ISUPTimeoutEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterSSRequest(RegisterSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterSSResponse(RegisterSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEraseSSRequest(EraseSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEraseSSResponse(EraseSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateSSRequest(ActivateSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateSSResponse(ActivateSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivateSSRequest(DeactivateSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivateSSResponse(DeactivateSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInterrogateSSRequest(InterrogateSSRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInterrogateSSResponse(InterrogateSSResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInfoForGprsRequest(SendRoutingInfoForGprsRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendRoutingInfoForGprsResponse(SendRoutingInfoForGprsResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendImsiRequest(SendImsiRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendImsiResponse(SendImsiResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetRequest(ResetRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeRequest_Oam(ActivateTraceModeRequest_Oam ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeResponse_Oam(ActivateTraceModeResponse_Oam ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCollectInformationRequest(CollectInformationRequest arg0) {
        // TODO Auto-generated method stub

    }

    public class DialogMessage {
        public int opc;
        public int dpc;
        public int callingSsn;
        public int calledSsn;
        public String callingPA;
        public String calledPA;
        public String tcapMessage;
        public long dialogId1;
        public long dialogId2;
        public int messageNum;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("opc=");
            sb.append(opc);
            sb.append(", dpc=");
            sb.append(dpc);
            sb.append(", callingPA=");
            sb.append(callingPA);
            sb.append(", calledPA=");
            sb.append(calledPA);
            sb.append(", callingSsn=");
            sb.append(callingSsn);
            sb.append(", calledSsn=");
            sb.append(calledSsn);
            sb.append(", ");
            sb.append(tcapMessage);
            sb.append(", ");
            sb.append(messageNum);
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof DialogMessage))
                return false;
            DialogMessage b = (DialogMessage) obj;
            if (this.opc == b.opc && this.dpc == b.dpc && this.calledPA != null && b.calledPA != null && this.calledPA.equals(b.calledPA)
                    && this.callingPA != null && b.callingPA != null && this.callingPA.equals(b.callingPA) && this.tcapMessage != null && b.tcapMessage != null
                    && this.tcapMessage.equals(b.tcapMessage))
                return true;
            else
                return false;
        }

        @Override
        public int hashCode() {
            return this.opc + this.dpc * 1000000;
        }
    }

    public class DialogMessageChain {
        private ArrayList<DialogMessage> lst = new ArrayList<DialogMessage>();
        private int cnt;
        public long dialogId1;
        public long dialogId2;

        public void addDialogMessage(DialogMessage dm) {
            this.lst.add(dm);
        }

        public void addChainCount() {
            this.cnt++;
        }

        public void setDialogId(long val) {
            if (dialogId1 == 0) {
                dialogId1 = val;
                return;
            }
            if (dialogId1 == val) {
                return;
            }
            if (dialogId2 == 0) {
                dialogId2 = val;

                return;
            }
        }

        public boolean checkFullDialog() {
//            String s1 = lst.get(0).tcapMessage;
//            String s2 = lst.get(lst.size() - 1).tcapMessage;
//            if (s1 == null || s2 == null)
//                return false;
//            if (!s1.equals("TC-BEGIN")) {
//                return false;
//            }
//            if (!s2.equals("TC-END") && !s2.equals("TC-ABORT")) {
//                return false;
//            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DialogMessageChain, cnt=");
            sb.append(cnt);
            sb.append(", dialogId1=");
            sb.append(dialogId1);
            sb.append(", dialogId2=");
            sb.append(dialogId2);
            for (int i1 = 0; i1 < this.lst.size(); i1++) {
                sb.append("\n  ");
                DialogMessage ma = this.lst.get(i1);
                sb.append(ma);
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof DialogMessageChain))
                return false;
            DialogMessageChain b = (DialogMessageChain) obj;

            if (this.lst.size() != b.lst.size())
                return false;
            for (int i1 = 0; i1 < this.lst.size(); i1++) {
                DialogMessage ma = this.lst.get(i1);
                DialogMessage mb = b.lst.get(i1);
                if (!ma.equals(mb))
                    return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int res = 0;
            for (int i1 = 0; i1 < this.lst.size(); i1++) {
                DialogMessage ma = this.lst.get(i1);
                res = res ^ ma.hashCode();
            }
            return res;
        }
    }

    @Override
    public void onGetPasswordRequest(GetPasswordRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetPasswordResponse(GetPasswordResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse arg0) {
        // TODO Auto-generated method stub

    }

//    public class DialogMessageChainKey {
//        private long dialogId1;
//        private long dialogId2;
//
//        public void setDialogId(long val) {
//            if (dialogId1 == 0) {
//                dialogId1 = val;
//                return;
//            }
//            if (dialogId1 == val) {
//                return;
//            }
//            if (dialogId2 == 0) {
//                dialogId2 = val;
//
//                // we must provide that dialogId1 > dialogId2 to have a proper hashCode
//                if (dialogId1 < dialogId2) {
//                    long x = dialogId2;
//                    dialogId2 = dialogId1;
//                    dialogId1 = x;
//                }
//
//                return;
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null)
//                return false;
//            if (!(obj instanceof DialogMessageChainKey))
//                return false;
//            DialogMessageChainKey b = (DialogMessageChainKey) obj;
//
//            if (this.dialogId1 != 0) {
//                if (this.dialogId1 == b.dialogId1 || this.dialogId1 == b.dialogId2)
//                    return true;
//            }
//            if (this.dialogId2 != 0) {
//                if (this.dialogId2 == b.dialogId1 || this.dialogId2 == b.dialogId2)
//                    return true;
//            }
//
//            return false;
//        }
//
//        @Override
//        public int hashCode() {
//            int res = (int) dialogId1;
//            return res;
//        }
//    }
}

