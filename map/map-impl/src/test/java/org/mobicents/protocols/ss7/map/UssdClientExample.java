package org.mobicents.protocols.ss7.map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
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
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * A simple example show-casing how to use MAP stack. Demonstrates how new MAP Dialog is craeted and Invoke is sent to peer.
 *
 * @author Amit Bhayani
 *
 */
public class UssdClientExample implements MAPDialogListener, MAPServiceSupplementaryListener {

    private MAPProvider mapProvider;
    private MAPParameterFactory paramFact;

    public UssdClientExample() throws NamingException {
        InitialContext ctx = new InitialContext();
        try {
            String providerJndiName = "java:/mobicents/ss7/map";
            this.mapProvider = ((MAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }
    }

    public MAPProvider getMAPProvider() {
        return mapProvider;
    }

    public void start() {
        // Listen for Dialog events
        mapProvider.addMAPDialogListener(this);
        // Listen for USSD related messages
        mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

        // Make the supplementary service activated
        mapProvider.getMAPServiceSupplementary().acivate();
    }

    public void stop() {
        mapProvider.getMAPServiceSupplementary().deactivate();
    }

    public void sendProcessUssdRequest(SccpAddress origAddress, AddressString origReference, SccpAddress remoteAddress,
            AddressString destReference, String ussdMessage, AlertingPattern alertingPattern, ISDNAddressString msisdn)
            throws MAPException {
        // First create Dialog
        MAPDialogSupplementary currentMapDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(
                MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
                        MAPApplicationContextVersion.version2), origAddress, destReference, remoteAddress, destReference);

        CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
        // The Charset is null, here we let system use default Charset (UTF-7 as
        // explained in GSM 03.38. However if MAP User wants, it can set its own
        // impl of Charset
        USSDString ussdString = paramFact.createUSSDString(ussdMessage, null, null);

        currentMapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString, alertingPattern, msisdn);
        // This will initiate the TC-BEGIN with INVOKE component
        currentMapDialog.send();
    }

    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse ind) {
        USSDString ussdString = ind.getUSSDString();
        try {
            String response = ussdString.getString(null);
            // processing USSD response
        } catch (MAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        // TODO Auto-generated method stub

    }

    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        // TODO Auto-generated method stub

    }

    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        // TODO Auto-generated method stub

    }

    public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
        // TODO Auto-generated method stub

    }

    public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        // TODO Auto-generated method stub

    }

    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        // TODO Auto-generated method stub

    }

    public void onDialogDelimiter(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            AddressString eriImsi, AddressString eriVlrNo) {
        // TODO Auto-generated method stub

    }

    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        // TODO Auto-generated method stub

    }

    public void onDialogClose(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

    }

    public void onDialogRelease(MAPDialog mapDialog) {
    }

    public void onDialogTimeout(MAPDialog mapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
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
}
