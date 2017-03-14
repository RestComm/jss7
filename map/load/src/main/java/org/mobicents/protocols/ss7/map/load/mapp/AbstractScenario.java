package org.mobicents.protocols.ss7.map.load.mapp;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
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
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

public abstract class AbstractScenario implements MAPScenario {

    MAPpContext ctx;

    @Override
    public void init(MAPpContext ctx) {
        this.ctx = ctx;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onErrorComponent
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long,
     * org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage)
     */
    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        ctx.logger.debug(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s",
                mapDialog.getLocalDialogId(), invokeId, mapErrorMessage));
        ctx.incrementCounter("onErrorComponent");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onRejectComponent
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long, org.mobicents.protocols.ss7.tcap.asn.comp.Problem)
     */
    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        ctx.logger.debug(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s isLocalOriginated=%s",
                mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated));
        ctx.incrementCounter("onRejectComponent");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onInvokeTimeout
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long)
     */
    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        ctx.logger.debug(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getLocalDialogId(), invokeId));
        ctx.incrementCounter("onInvokeTimeout");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onProcessUnstructuredSSRequestIndication(org .mobicents.protocols.ss7.map.
     * api.service.supplementary.ProcessUnstructuredSSRequestIndication)
     */
    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        ctx.logger.debug(String.format("onProcessUnstructuredSSRequestIndication for Dialog=%d and invokeId=%d", procUnstrReqInd
                .getMAPDialog().getLocalDialogId(), procUnstrReqInd.getInvokeId()));
        ctx.incrementCounter("onProcessUnstructuredSSRequest");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onProcessUnstructuredSSResponseIndication( org.mobicents.protocols.ss7.map
     * .api.service.supplementary.ProcessUnstructuredSSResponseIndication)
     */
    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("Rx ProcessUnstructuredSSResponseIndication.  USSD String=%s",
                    procUnstrResInd.getUSSDString()));
        }
        ctx.incrementCounter("onProcessUnstructuredSSResponse");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSRequestIndication(org.mobicents .protocols.ss7.map.api.service
     * .supplementary.UnstructuredSSRequestIndication)
     */
    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("Rx UnstructuredSSRequestIndication. USSD String=%s ", unstrReqInd.getUSSDString()));
        }
        ctx.incrementCounter("onUnstructuredSSRequest");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSResponseIndication(org.mobicents .protocols.ss7.map.api.service
     * .supplementary.UnstructuredSSResponseIndication)
     */
    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSResponseIndication
        ctx.logger.debug(String.format("onUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d", unstrResInd
                .getMAPDialog().getLocalDialogId(), unstrResInd.getInvokeId()));
        ctx.incrementCounter("onUnstructuredSSResponse");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary. MAPServiceSupplementaryListener
     * #onUnstructuredSSNotifyRequestIndication(org .mobicents.protocols.ss7.map.api
     * .service.supplementary.UnstructuredSSNotifyRequestIndication)
     */
    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSNotifyRequestIndication
        ctx.logger.debug(String.format("onUnstructuredSSNotifyRequestIndication for Dialog=%d and invokeId=%d", unstrNotifyInd
                .getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
        ctx.incrementCounter("onUnstructuredSSNotifyRequest");
    }

    public void onUnstructuredSSNotifyResponseIndication(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // This error condition. Client should never receive the
        // UnstructuredSSNotifyRequestIndication
        ctx.logger.debug(String.format("onUnstructuredSSNotifyResponseIndication for Dialog=%d and invokeId=%d", unstrNotifyInd
                .getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
        ctx.incrementCounter("onUnstructuredSSNotifyResponseIndication");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogDelimiter
     * (org.mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getLocalDialogId()));
        }
        ctx.incrementCounter("onDialogDelimiter");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequest
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format(
                    "onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
                    mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
        }
        ctx.incrementCounter("onDialogRequest");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequestEricsson
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.map.api.primitives.IMSI,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString)
     */
    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            IMSI arg3, AddressString arg4) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ",
                    mapDialog.getLocalDialogId(), destReference, origReference));
        }
        ctx.incrementCounter("onDialogRequestEricsson");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogAccept( org.mobicents.protocols.ss7.map.api.MAPDialog,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(),
                    extensionContainer));
        }
        ctx.incrementCounter("onDialogAccept");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogReject( org.mobicents.protocols.ss7.map.api.MAPDialog,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason, org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError,
     * org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        ctx.logger.debug(String.format(
                "onDialogReject for DialogId=%d MAPRefuseReason=%s ApplicationContextName=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer));
        ctx.incrementCounter("onDialogReject");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogUserAbort
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        ctx.logger.debug(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), userReason, extensionContainer));
        ctx.incrementCounter("onDialogUserAbort");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogProviderAbort
     * (org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        ctx.logger.debug(String.format(
                "onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer));
        ctx.incrementCounter("onDialogProviderAbort");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogClose(org .mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getLocalDialogId()));
        }
        ctx.incrementCounter("onDialogClose");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogNotice( org.mobicents.protocols.ss7.map.api.MAPDialog,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic)
     */
    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        ctx.logger.debug(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
                mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
        ctx.incrementCounter("onDialogNotice");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogResease
     * (org.mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (ctx.logger.isDebugEnabled()) {
            ctx.logger.debug(String.format("onDialogResease for DialogId=%d", mapDialog.getLocalDialogId()));
        }
        ctx.incrementCounter("onDialogRelease");
    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        ctx.logger.debug(mapDialog);
        ctx.incrementCounter("onDialogTimeout");
    }

    @Override
    public void onRegisterSSRequest(RegisterSSRequest request) {
        ctx.logger.debug(request);
        ctx.incrementCounter("onRegisterSSRequest");
    }

    @Override
    public void onRegisterSSResponse(RegisterSSResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onRegisterSSResponse");
    }

    @Override
    public void onEraseSSRequest(EraseSSRequest request) {
        ctx.logger.info(request);
        ctx.incrementCounter("onEraseSSRequest");
    }

    @Override
    public void onEraseSSResponse(EraseSSResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onEraseSSResponse");
    }

    @Override
    public void onActivateSSRequest(ActivateSSRequest request) {
        ctx.logger.debug(request);
        ctx.incrementCounter("onActivateSSRequest");
    }

    @Override
    public void onActivateSSResponse(ActivateSSResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onActivateSSResponse");
    }

    @Override
    public void onDeactivateSSRequest(DeactivateSSRequest request) {
        ctx.logger.debug(request);
        ctx.incrementCounter("onDeactivateSSRequest");
    }

    @Override
    public void onDeactivateSSResponse(DeactivateSSResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onDeactivateSSResponse");
    }

    @Override
    public void onInterrogateSSRequest(InterrogateSSRequest request) {
        ctx.logger.debug(request);
        ctx.incrementCounter("onInterrogateSSRequest");
    }

    @Override
    public void onInterrogateSSResponse(InterrogateSSResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onInterrogateSSResponse");
    }

    @Override
    public void onGetPasswordRequest(GetPasswordRequest request) {
        ctx.logger.debug(request);
        ctx.incrementCounter("onGetPasswordRequest");
    }

    @Override
    public void onGetPasswordResponse(GetPasswordResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onGetPasswordResponse");
    }

    @Override
    public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
        ctx.logger.debug(request);
        ctx.incrementCounter("onRegisterPasswordRequest");
    }

    @Override
    public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
        ctx.logger.debug(response);
        ctx.incrementCounter("onRegisterPasswordResponse");
    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        ctx.logger.debug(unstrNotifyInd);
        ctx.incrementCounter("onUnstructuredSSNotifyResponse");
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        ctx.logger.debug(mapMessage);
        ctx.incrementCounter("onMAPMessage");
    }

}
