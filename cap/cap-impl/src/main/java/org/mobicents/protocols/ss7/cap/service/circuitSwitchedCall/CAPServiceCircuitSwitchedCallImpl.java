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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.CAPServiceBaseImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;
import org.mobicents.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.cap.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.cap.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPServiceCircuitSwitchedCallImpl extends CAPServiceBaseImpl implements CAPServiceCircuitSwitchedCall {

    protected Logger loger = Logger.getLogger(CAPServiceCircuitSwitchedCallImpl.class);

    public CAPServiceCircuitSwitchedCallImpl(CAPProviderImpl capProviderImpl) {
        super(capProviderImpl);
    }

    @Override
    public CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException {
        return this.createNewDialog(appCntx, origAddress, destAddress, null);
    }

    @Override
    public CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress, Long localTrId)
            throws CAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new CAPException(
                    "Cannot create CAPDialogCircuitSwitchedCall because CAPServiceCircuitSwitchedCall is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        CAPDialogCircuitSwitchedCallImpl dialog = new CAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog,
                this.capProviderImpl, this);

        this.putCAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    public void addCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener) {
        super.addCAPServiceListener(capServiceListener);
    }

    @Override
    public void removeCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener) {
        super.removeCAPServiceListener(capServiceListener);
    }

    @Override
    protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
        return new CAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog, this.capProviderImpl, this);
    }

    @Override
    public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {

        switch (dialogApplicationContext) {
            case CapV1_gsmSSF_to_gsmSCF:
            case CapV2_gsmSSF_to_gsmSCF:
            case CapV2_assistGsmSSF_to_gsmSCF:
            case CapV2_gsmSRF_to_gsmSCF:
            case CapV3_gsmSSF_scfGeneric:
            case CapV3_gsmSSF_scfAssistHandoff:
            case CapV3_gsmSRF_gsmSCF:
            case CapV4_gsmSSF_scfGeneric:
            case CapV4_gsmSSF_scfAssistHandoff:
            case CapV4_scf_gsmSSFGeneric:
            case CapV4_gsmSRF_gsmSCF:
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    public long[] getLinkedOperationList(long operCode) {
        if (operCode == CAPOperationCode.playAnnouncement || operCode == CAPOperationCode.promptAndCollectUserInformation) {
            return new long[] { CAPOperationCode.specializedResourceReport };
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, CAPDialog capDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws CAPParsingComponentException {

        CAPDialogCircuitSwitchedCallImpl capDialogCircuitSwitchedCallImpl = (CAPDialogCircuitSwitchedCallImpl) capDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        CAPApplicationContext acn = capDialog.getApplicationContext();
        int ocValueInt = (int) (long) ocValue;

        switch (ocValueInt) {
            case CAPOperationCode.initialDP:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric) {
                    if (compType == ComponentType.Invoke) {
                        this.initialDpRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.requestReportBCSMEvent:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        this.requestReportBCSMEventRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.applyCharging:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        this.applyChargingRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.eventReportBCSM:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        eventReportBCSMRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.continueCode:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        continueRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.continueWithArgument:
                if (acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        continueWithArgumentRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.applyChargingReport:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        applyChargingReportRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.releaseCall:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        releaseCallRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.connect:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        connectRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.callInformationRequest:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        callInformationRequestRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.callInformationReport:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        callInformationReportRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.activityTest:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        activityTestRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        activityTestResponse(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.assistRequestInstructions:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                    if (compType == ComponentType.Invoke) {
                        assistRequestInstructionsRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.establishTemporaryConnection:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        establishTemporaryConnectionRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.disconnectForwardConnection:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        disconnectForwardConnectionRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.disconnectLeg:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                    if (compType == ComponentType.Invoke) {
                        disconnectLegRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        disconnectLegResponse(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.dFCWithArgument:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                    if (compType == ComponentType.Invoke) {
                        dFCWithArgument(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.initiateCallAttempt:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                    if (compType == ComponentType.Invoke) {
                        initiateCallAttemptRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        initiateCallAttemptResponse(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.connectToResource:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        connectToResourceRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.resetTimer:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        resetTimerRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.furnishChargingInformation:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        furnishChargingInformationRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.sendChargingInformation:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric) {
                    if (compType == ComponentType.Invoke) {
                        sendChargingInformationRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.specializedResourceReport:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                    if (compType == ComponentType.Invoke) {
                        specializedResourceReportRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId, linkedId,
                                linkedInvoke);
                    }
                }
                break;

            case CAPOperationCode.playAnnouncement:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                    if (compType == ComponentType.Invoke) {
                        playAnnouncementRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.promptAndCollectUserInformation:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                    if (compType == ComponentType.Invoke) {
                        promptAndCollectUserInformationRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        promptAndCollectUserInformationResponse(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.cancelCode:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                    if (compType == ComponentType.Invoke) {
                        cancelRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.moveLeg:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                    if (compType == ComponentType.Invoke) {
                        moveLegRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        moveLegResponse(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.splitLeg:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                    if (compType == ComponentType.Invoke) {
                        splitLegRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        splitLegResponse(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            case CAPOperationCode.collectInformation:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke) {
                        collectInformationRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
                    }
                }
                break;

            default:
                throw new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    private void initialDpRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding initialDpRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding initialDpRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        InitialDPRequestImpl ind = new InitialDPRequestImpl(
                capDialogImpl.getApplicationContext().getVersion().getVersion() >= 3);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onInitialDPRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing initialDpRequest: " + e.getMessage(), e);
            }
        }
    }

    private void requestReportBCSMEventRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding requestReportBCSMEventRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding requestReportBCSMEventRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        RequestReportBCSMEventRequestImpl ind = new RequestReportBCSMEventRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onRequestReportBCSMEventRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing requestReportBCSMEventRequest: " + e.getMessage(), e);
            }
        }
    }

    private void applyChargingRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ApplyChargingRequestImpl ind = new ApplyChargingRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing requestReportBCSMEventRequest: " + e.getMessage(), e);
            }
        }
    }

    private void eventReportBCSMRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding eventReportBCSMRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding eventReportBCSMRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        EventReportBCSMRequestImpl ind = new EventReportBCSMRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onEventReportBCSMRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing eventReportBCSMRequest: " + e.getMessage(), e);
            }
        }
    }

    private void continueRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ContinueRequestImpl ind = new ContinueRequestImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onContinueRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing continueRequest: " + e.getMessage(), e);
            }
        }
    }

    private void continueWithArgumentRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding continueWithArgumentRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding continueWithArgumentRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        ContinueWithArgumentRequestImpl ind = new ContinueWithArgumentRequestImpl();
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onContinueWithArgumentRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing continueWithArgumentRequest: " + e.getMessage(), e);
            }
        }
    }

    private void applyChargingReportRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingReportRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || !parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingReportRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ApplyChargingReportRequestImpl ind = new ApplyChargingReportRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingReportRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing applyChargingReportRequest: " + e.getMessage(), e);
            }
        }
    }

    private void releaseCallRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding releaseCallRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || !parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding releaseCallRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ReleaseCallRequestImpl ind = new ReleaseCallRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onReleaseCallRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing applyChargingReportRequest: " + e.getMessage(), e);
            }
        }
    }

    private void connectRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException("Error while decoding connectRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding connectRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ConnectRequestImpl ind = new ConnectRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onConnectRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing eventReportBCSMRequest: " + e.getMessage(), e);
            }
        }
    }

    private void callInformationRequestRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding callInformationRequestRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding callInformationRequestRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        CallInformationRequestRequestImpl ind = new CallInformationRequestRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onCallInformationRequestRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing eventReportBCSMRequest: " + e.getMessage(), e);
            }
        }
    }

    private void callInformationReportRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding callInformationReportRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding callInformationReportRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        CallInformationReportRequestImpl ind = new CallInformationReportRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onCallInformationReportRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing eventReportBCSMRequest: " + e.getMessage(), e);
            }
        }
    }

    private void activityTestRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ActivityTestRequestImpl ind = new ActivityTestRequestImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onActivityTestRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing activityTestRequest: " + e.getMessage(), e);
            }
        }
    }

    private void activityTestResponse(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ActivityTestResponseImpl ind = new ActivityTestResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onActivityTestResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing activityTestResponse: " + e.getMessage(), e);
            }
        }
    }

    private void assistRequestInstructionsRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding assistRequestInstructionsRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding assistRequestInstructionsRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        AssistRequestInstructionsRequestImpl ind = new AssistRequestInstructionsRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onAssistRequestInstructionsRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing assistRequestInstructionsRequest: " + e.getMessage(), e);
            }
        }
    }

    private void establishTemporaryConnectionRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding establishTemporaryConnectionRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding establishTemporaryConnectionRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        EstablishTemporaryConnectionRequestImpl ind = new EstablishTemporaryConnectionRequestImpl(capDialogImpl
                .getApplicationContext().getVersion().getVersion() >= 3);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onEstablishTemporaryConnectionRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing establishTemporaryConnectionRequest: " + e.getMessage(), e);
            }
        }
    }

    private void disconnectForwardConnectionRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        DisconnectForwardConnectionRequestImpl ind = new DisconnectForwardConnectionRequestImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing disconnectForwardConnectionRequest: " + e.getMessage(), e);
            }
        }
    }

    private void disconnectLegRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding disconnectLegRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding disconnectLegRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        DisconnectLegRequestImpl ind = new DisconnectLegRequestImpl();
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectLegRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing disconnectLegRequest: " + e.getMessage(), e);
            }
        }
    }

    private void disconnectLegResponse(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        DisconnectLegResponseImpl ind = new DisconnectLegResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectLegResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing disconnectLegResponse: " + e.getMessage(), e);
            }
        }
    }

    private void dFCWithArgument(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding dFCWithArgument: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding dFCWithArgument: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        DisconnectForwardConnectionWithArgumentRequestImpl ind = new DisconnectForwardConnectionWithArgumentRequestImpl();
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionWithArgumentRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing dFCWithArgument: " + e.getMessage(), e);
            }
        }
    }

    private void initiateCallAttemptRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding initiateCallAttemptRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding initiateCallAttemptRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        InitiateCallAttemptRequestImpl ind = new InitiateCallAttemptRequestImpl();

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onInitiateCallAttemptRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing initiateCallAttemptRequest: " + e.getMessage(), e);
            }
        }
    }

    private void initiateCallAttemptResponse(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding initiateCallAttemptResponse: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding initiateCallAttemptResponse: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        InitiateCallAttemptResponseImpl ind = new InitiateCallAttemptResponseImpl();

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onInitiateCallAttemptResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing initiateCallAttemptResponse: " + e.getMessage(), e);
            }
        }
    }

    private void connectToResourceRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding connectToResourceRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding connectToResourceRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ConnectToResourceRequestImpl ind = new ConnectToResourceRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onConnectToResourceRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing connectToResourceRequest: " + e.getMessage(), e);
            }
        }
    }

    private void resetTimerRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding resetTimerRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding resetTimerRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ResetTimerRequestImpl ind = new ResetTimerRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onResetTimerRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing resetTimerRequest: " + e.getMessage(), e);
            }
        }
    }

    private void furnishChargingInformationRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding furnishChargingInformationRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || !parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding furnishChargingInformationRequest: Bad tag or tagClass or parameter is not primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        FurnishChargingInformationRequestImpl ind = new FurnishChargingInformationRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onFurnishChargingInformationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing furnishChargingInformationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void sendChargingInformationRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding sendChargingInformationRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding sendChargingInformationRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        SendChargingInformationRequestImpl ind = new SendChargingInformationRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onSendChargingInformationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing sendChargingInformationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void specializedResourceReportRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding specializedResourceReportRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (capDialogImpl.getApplicationContext().getVersion().getVersion() < 4) {
            if (parameter.getTag() != Tag.NULL || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || !parameter.isPrimitive())
                throw new CAPParsingComponentException(
                        "Error while decoding specializedResourceReportRequest: Bad tag or tagClass or parameter is not primitive, received tag="
                                + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !parameter.isPrimitive())
                throw new CAPParsingComponentException(
                        "Error while decoding specializedResourceReportRequest: Bad tagClass or parameter is not primitive, received tag="
                                + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        SpecializedResourceReportRequestImpl ind = new SpecializedResourceReportRequestImpl(capDialogImpl
                .getApplicationContext().getVersion().getVersion() >= 4);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setLinkedId(linkedId);
        ind.setLinkedInvoke(linkedInvoke);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onSpecializedResourceReportRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing specializedResourceReportRequest: " + e.getMessage(), e);
            }
        }
    }

    private void playAnnouncementRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding playAnnouncementRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding playAnnouncementRequest: Bad tag or tagClass or parameter is not primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        PlayAnnouncementRequestImpl ind = new PlayAnnouncementRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onPlayAnnouncementRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing playAnnouncementRequest: " + e.getMessage(), e);
            }
        }
    }

    private void promptAndCollectUserInformationRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding promptAndCollectUserInformationRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding playAnnouncementRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        PromptAndCollectUserInformationRequestImpl ind = new PromptAndCollectUserInformationRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onPromptAndCollectUserInformationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing promptAndCollectUserInformationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void promptAndCollectUserInformationResponse(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl,
            Long invokeId) throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding promptAndCollectUserInformationResponse: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
            throw new CAPParsingComponentException(
                    "Error while decoding promptAndCollectUserInformationResponse: bad tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        PromptAndCollectUserInformationResponseImpl ind = new PromptAndCollectUserInformationResponseImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onPromptAndCollectUserInformationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing promptAndCollectUserInformationResponse: " + e.getMessage(), e);
            }
        }
    }

    private void moveLegRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding moveLegRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.getTag() != Tag.SEQUENCE || parameter.isPrimitive())
            throw new CAPParsingComponentException("Error while decoding moveLegRequest: bad tagClass or tag",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        MoveLegRequestImpl ind = new MoveLegRequestImpl();

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onMoveLegRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing moveLegRequest: " + e.getMessage(), e);
            }
        }
    }

    private void moveLegResponse(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        MoveLegResponseImpl ind = new MoveLegResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onMoveLegResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing moveLegResponse: " + e.getMessage(), e);
            }
        }
    }

    private void splitLegRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding splitLegRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.getTag() != Tag.SEQUENCE || parameter.isPrimitive())
            throw new CAPParsingComponentException("Error while decoding splitLegRequest: bad tagClass or tag",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        SplitLegRequestImpl ind = new SplitLegRequestImpl();

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onSplitLegRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing splitLegRequest: " + e.getMessage(), e);
            }
        }
    }

    private void splitLegResponse(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        SplitLegResponseImpl ind = new SplitLegResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onSplitLegResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing splitLegResponse: " + e.getMessage(), e);
            }
        }
    }

    private void cancelRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException("Error while decoding cancelRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
            throw new CAPParsingComponentException("Error while decoding cancelRequest: bad tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        CancelRequestImpl ind = new CancelRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onCancelRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing cancelRequest: " + e.getMessage(), e);
            }
        }
    }

    private void collectInformationRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        CollectInformationRequestImpl ind = new CollectInformationRequestImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceCircuitSwitchedCallListener) serLis).onCollectInformationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing collectInformationRequest: " + e.getMessage(), e);
            }
        }
    }
}
