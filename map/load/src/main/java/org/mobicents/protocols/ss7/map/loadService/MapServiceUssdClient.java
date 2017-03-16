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

package org.mobicents.protocols.ss7.map.loadService;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
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
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
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
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author sergey vetyutnev
 *
 */
public class MapServiceUssdClient implements MAPDialogListener, MAPServiceSupplementaryListener {

    private MAPStack mapStack;
    private MAPProvider mapProvider;
    protected final Logger logger;

    private int delayBeforeLoad = 60;
    private int numberOfDialogs = 10000;
    private int maxConcurrentDialogs = 15;

    private int endCount = -100;
    volatile long start = 0L;
    volatile long prev = 0L;
    private RateLimiter rateLimiterObj = null;
    private boolean stopped = false;

    protected static int CLIENT_SPC = 1;
    protected static int SERVER_SPC = 2;
    protected static int SSN = 8;
    protected SccpAddress SCCP_CLIENT_ADDRESS;
    protected SccpAddress SCCP_SERVER_ADDRESS;

    public MapServiceUssdClient(MAPStack mapStack) {
        this.mapStack = mapStack;

        this.logger = Logger.getLogger(MapServiceUssdClient.class.getCanonicalName());
    }

    public void start() throws Exception {
        this.logger.info("Starting MapServerUssdClient ...");

        this.mapProvider = this.mapStack.getMAPProvider();

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSupplementary().acivate();

        this.rateLimiterObj = RateLimiter.create(getMaxConcurrentDialogs()); // rate

        SCCP_CLIENT_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, CLIENT_SPC, SSN);
        SCCP_SERVER_ADDRESS = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, SERVER_SPC, SSN);

        Thread mainThread = new Thread(new MapServerUssdClient_StartClass());
        mainThread.start();

        this.logger.info("Started MapServerUssdClient ...");
    }

    public void stop() {
        this.logger.info("Stopping MapServerUssdClient ...");

        stopped = true;

        this.logger.info("Stopped MapServerUssdClient ...");
    }

    private void execute() {
        try {
            // delay before starting of work

            Thread.sleep(1000 * getDelayBeforeLoad());

            while (endCount < getNumberOfDialogs() && !stopped) {

                if (endCount < 0) {
                    start = System.currentTimeMillis();
                    prev = start;
                    // logger.warn("StartTime = " + client.start);
                }

                initiateUSSD();
            }
        } catch (Exception e) {
            logger.error("General MapServerUssdClient executing Exception: " + e.getMessage(), e);
        }
    }

    private void initiateUSSD() throws MAPException {
        NetworkIdState networkIdState = this.mapStack.getMAPProvider().getNetworkIdState(0);
        int executorCongestionLevel = this.mapStack.getMAPProvider().getExecutorCongestionLevel();
        if (!(networkIdState == null || networkIdState.isAvailavle() && networkIdState.getCongLevel() <= 0
                && executorCongestionLevel <= 0)) {
            // congestion or unavailable
            logger.warn("**** Outgoing congestion control: MAP load test client: networkIdState=" + networkIdState
                    + ", executorCongestionLevel=" + executorCongestionLevel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.rateLimiterObj.acquire();
        // System.out.println("initiateUSSD");

        // First create Dialog
        AddressString origRef = this.mapProvider.getMAPParameterFactory().createAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "12345");
        AddressString destRef = this.mapProvider.getMAPParameterFactory().createAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "67890");
        MAPDialogSupplementary mapDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(
                MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
                        MAPApplicationContextVersion.version2), SCCP_CLIENT_ADDRESS, origRef, SCCP_SERVER_ADDRESS, destRef);

        CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);

        // USSD String: *125*+31628839999#
        // The Charset is null, here we let system use default Charset (UTF-7 as
        // explained in GSM 03.38. However if MAP User wants, it can set its own
        // impl of Charset
        USSDString ussdString = this.mapProvider.getMAPParameterFactory().createUSSDString("*125*+31628839999#", null, null);

        ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory().createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

        mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString, null, msisdn);

        // This will initiate the TC-BEGIN with INVOKE component
        mapDialog.send();
    }

    private class MapServerUssdClient_StartClass implements Runnable {
        @Override
        public void run() {
            execute();
        }
    }

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s",
                mapDialog.getLocalDialogId(), invokeId, mapErrorMessage));
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s isLocalOriginated=%s",
                mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated));
    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        logger.error(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getLocalDialogId(), invokeId));

    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
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
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        // This error condition. Client should never receive the
        // ProcessUnstructuredSSRequestIndication
        logger.error(String.format("onProcessUnstructuredSSRequestIndication for Dialog=%d and invokeId=%d", procUnstrReqInd
                .getMAPDialog().getLocalDialogId(), procUnstrReqInd.getInvokeId()));
    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Rx ProcessUnstructuredSSResponseIndication.  USSD String=%s",
                    procUnstrResInd.getUSSDString()));
        }
    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Rx UnstructuredSSRequestIndication. USSD String=%s ", unstrReqInd.getUSSDString()));
        }

        MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();

        try {
            CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);

            USSDString ussdString = this.mapProvider.getMAPParameterFactory().createUSSDString("1", null, null);

            AddressString msisdn = this.mapProvider.getMAPParameterFactory().createAddressString(
                    AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

            mapDialog.addUnstructuredSSResponse(unstrReqInd.getInvokeId(), ussdDataCodingScheme, ussdString);
            mapDialog.send();

        } catch (MAPException e) {
            logger.error(String.format("Error while sending UnstructuredSSResponse for Dialog=%d", mapDialog.getLocalDialogId()));
        }
    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSResponseIndication
        logger.error(String.format("onUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d", unstrResInd
                .getMAPDialog().getLocalDialogId(), unstrResInd.getInvokeId()));
    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSNotifyRequestIndication
        logger.error(String.format("onUnstructuredSSNotifyRequestIndication for Dialog=%d and invokeId=%d", unstrNotifyInd
                .getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getLocalDialogId()));
        }
    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
                    mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
        }
    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            AddressString eriImsi, AddressString eriVlrNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ",
                    mapDialog.getLocalDialogId(), destReference, origReference));
        }
    }

    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(),
                    extensionContainer));
        }
    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        logger.error(String.format(
                "onDialogReject for DialogId=%d MAPRefuseReason=%s ApplicationContextName=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer));
    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), userReason, extensionContainer));
    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        logger.error(String.format(
                "onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer));
    }

    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getLocalDialogId()));
        }
    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
                mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogResease for DialogId=%d", mapDialog.getLocalDialogId()));
        }

        this.endCount++;

        if (this.endCount < getNumberOfDialogs()) {
            if ((this.endCount % 2000) == 0) {
                long current = System.currentTimeMillis();
                float sec = (float) (current - prev) / 1000f;
                prev = current;
                logger.warn("Completed 2000 Dialogs, dlg per a sec: " + (float) (2000 / sec));
            }
        } else {
            if (this.endCount == getNumberOfDialogs()) {
                long current = System.currentTimeMillis();
                logger.warn("Start Time = " + start);
                logger.warn("Current Time = " + current);
                float sec = (float) (current - start) / 1000f;

                logger.warn("Total time in sec = " + sec);
                logger.warn("Throughput = " + (float) (getNumberOfDialogs() / sec));
            }
        }
    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        logger.error(String.format("onDialogTimeout for DialogId=%d", mapDialog.getLocalDialogId()));
    }

    public int getDelayBeforeLoad() {
        return delayBeforeLoad;
    }

    public void setDelayBeforeLoad(int delayBeforeLoad) {
        this.delayBeforeLoad = delayBeforeLoad;
    }

    public int getNumberOfDialogs() {
        return numberOfDialogs;
    }

    public void setNumberOfDialogs(int numberOfDialogs) {
        this.numberOfDialogs = numberOfDialogs;
    }

    public int getMaxConcurrentDialogs() {
        return maxConcurrentDialogs;
    }

    public void setMaxConcurrentDialogs(int maxConcurrentDialogs) {
        this.maxConcurrentDialogs = maxConcurrentDialogs;
    }

}
