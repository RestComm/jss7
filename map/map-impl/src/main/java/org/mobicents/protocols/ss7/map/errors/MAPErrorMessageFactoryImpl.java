/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.errors;

import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.mobicents.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.mobicents.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;
import org.mobicents.protocols.ss7.map.api.errors.CUGRejectCause;
import org.mobicents.protocols.ss7.map.api.errors.CallBarringCause;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCUGReject;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSsErrorStatus;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSsIncompatibility;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSubscriberBusyForMtSms;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.PWRegistrationFailureCause;
import org.mobicents.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;
import org.mobicents.protocols.ss7.map.api.errors.RoamingNotAllowedCause;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;
import org.mobicents.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;

/**
 * The factory of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageFactoryImpl implements MAPErrorMessageFactory {

    /**
     * Generate the empty message depends of the error code (for incoming messages)
     *
     * @param errorCode
     * @return
     */
    public MAPErrorMessage createMessageFromErrorCode(Long errorCode) {
        long ec = (long) errorCode;
        switch ((int) ec) {
            case MAPErrorCode.unexpectedDataValue:
            case MAPErrorCode.dataMissing:
            case MAPErrorCode.unidentifiedSubscriber:
            case MAPErrorCode.illegalSubscriber:
            case MAPErrorCode.illegalEquipment:
            case MAPErrorCode.teleserviceNotProvisioned:
            case MAPErrorCode.messageWaitingListFull:
            case MAPErrorCode.unauthorizedRequestingNetwork:
            case MAPErrorCode.resourceLimitation:
            case MAPErrorCode.unknownOrUnreachableLCSClient:
            case MAPErrorCode.incompatibleTerminal:
            case MAPErrorCode.noRoamingNumberAvailable:
            case MAPErrorCode.noSubscriberReply:
            case MAPErrorCode.forwardingFailed:
            case MAPErrorCode.orNotAllowed:
            case MAPErrorCode.forwardingViolation:
            case MAPErrorCode.numberChanged:
            case MAPErrorCode.unknownMSC:
            case MAPErrorCode.unknownEquipment:
            case MAPErrorCode.bearerServiceNotProvisioned:
            case MAPErrorCode.mmEventNotSupported:
            case MAPErrorCode.illegalSSOperation:
            case MAPErrorCode.ssNotAvailable:
            case MAPErrorCode.ssSubscriptionViolation:
            case MAPErrorCode.unknownAlphabet:
            case MAPErrorCode.ussdBusy:
            case MAPErrorCode.negativePWCheck:
            case MAPErrorCode.numberOfPWAttemptsViolation:
            case MAPErrorCode.shortTermDenial:
            case MAPErrorCode.longTermDenial:
                return new MAPErrorMessageExtensionContainerImpl(errorCode);

            case MAPErrorCode.smDeliveryFailure:
                return new MAPErrorMessageSMDeliveryFailureImpl();

            case MAPErrorCode.absentSubscriberSM:
                return new MAPErrorMessageAbsentSubscriberSMImpl();

            case MAPErrorCode.systemFailure:
                return new MAPErrorMessageSystemFailureImpl();

            case MAPErrorCode.callBarred:
                return new MAPErrorMessageCallBarredImpl();

            case MAPErrorCode.facilityNotSupported:
                return new MAPErrorMessageFacilityNotSupImpl();

            case MAPErrorCode.unknownSubscriber:
                return new MAPErrorMessageUnknownSubscriberImpl();

            case MAPErrorCode.subscriberBusyForMTSMS:
                return new MAPErrorMessageSubscriberBusyForMtSmsImpl();

            case MAPErrorCode.absentSubscriber:
                return new MAPErrorMessageAbsentSubscriberImpl();

            case MAPErrorCode.unauthorizedLCSClient:
                return new MAPErrorMessageUnauthorizedLCSClientImpl();

            case MAPErrorCode.positionMethodFailure:
                return new MAPErrorMessagePositionMethodFailureImpl();

            case MAPErrorCode.busySubscriber:
                return new MAPErrorMessageBusySubscriberImpl();

            case MAPErrorCode.cugReject:
                return new MAPErrorMessageCUGRejectImpl();

            case MAPErrorCode.roamingNotAllowed:
                return new MAPErrorMessageRoamingNotAllowedImpl();

            case MAPErrorCode.ssErrorStatus:
                return new MAPErrorMessageSsErrorStatusImpl();

            case MAPErrorCode.ssIncompatibility:
                return new MAPErrorMessageSsIncompatibilityImpl();

            case MAPErrorCode.pwRegistrationFailure:
                return new MAPErrorMessagePwRegistrationFailureImpl();

            default:
                return new MAPErrorMessageParameterlessImpl(errorCode);
        }

    }

    public MAPErrorMessageParameterless createMAPErrorMessageParameterless(Long errorCode) {
        return new MAPErrorMessageParameterlessImpl(errorCode);
    }

    public MAPErrorMessageExtensionContainer createMAPErrorMessageExtensionContainer(Long errorCode,
            MAPExtensionContainer extensionContainer) {
        return new MAPErrorMessageExtensionContainerImpl(errorCode, extensionContainer);
    }

    public MAPErrorMessageSMDeliveryFailure createMAPErrorMessageSMDeliveryFailure(
            SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause, byte[] signalInfo,
            MAPExtensionContainer extensionContainer) {
        return new MAPErrorMessageSMDeliveryFailureImpl(smEnumeratedDeliveryFailureCause, signalInfo, extensionContainer);
    }

    public MAPErrorMessageFacilityNotSup createMAPErrorMessageFacilityNotSup(MAPExtensionContainer extensionContainer,
            Boolean shapeOfLocationEstimateNotSupported, Boolean neededLcsCapabilityNotSupportedInServingNode) {
        return new MAPErrorMessageFacilityNotSupImpl(extensionContainer, shapeOfLocationEstimateNotSupported,
                neededLcsCapabilityNotSupportedInServingNode);
    }

    public MAPErrorMessageSystemFailure createMAPErrorMessageSystemFailure(long mapVersion, NetworkResource networkResource,
            AdditionalNetworkResource additionalNetworkResource, MAPExtensionContainer extensionContainer) {
        return new MAPErrorMessageSystemFailureImpl(mapVersion, networkResource, additionalNetworkResource, extensionContainer);
    }

    public MAPErrorMessageUnknownSubscriber createMAPErrorMessageUnknownSubscriber(MAPExtensionContainer extensionContainer,
            UnknownSubscriberDiagnostic unknownSubscriberDiagnostic) {
        return new MAPErrorMessageUnknownSubscriberImpl(extensionContainer, unknownSubscriberDiagnostic);
    }

    public MAPErrorMessageAbsentSubscriberSM createMAPErrorMessageAbsentSubscriberSM(
            AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer,
            AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
        return new MAPErrorMessageAbsentSubscriberSMImpl(absentSubscriberDiagnosticSM, extensionContainer,
                additionalAbsentSubscriberDiagnosticSM);
    }

    public MAPErrorMessageSubscriberBusyForMtSms createMAPErrorMessageSubscriberBusyForMtSms(
            MAPExtensionContainer extensionContainer, Boolean gprsConnectionSuspended) {
        return new MAPErrorMessageSubscriberBusyForMtSmsImpl(extensionContainer, gprsConnectionSuspended);
    }

    public MAPErrorMessageCallBarred createMAPErrorMessageCallBarred(Long mapVersion, CallBarringCause callBarringCause,
            MAPExtensionContainer extensionContainer, Boolean unauthorisedMessageOriginator) {
        return new MAPErrorMessageCallBarredImpl(mapVersion, callBarringCause, extensionContainer,
                unauthorisedMessageOriginator);
    }

    public MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(MAPExtensionContainer extensionContainer,
            AbsentSubscriberReason absentSubscriberReason) {
        return new MAPErrorMessageAbsentSubscriberImpl(extensionContainer, absentSubscriberReason);
    }

    public MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(Boolean mwdSet) {
        return new MAPErrorMessageAbsentSubscriberImpl(mwdSet);
    }

    public MAPErrorMessageUnauthorizedLCSClient createMAPErrorMessageUnauthorizedLCSClient(
            UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic, MAPExtensionContainer extensionContainer) {
        return new MAPErrorMessageUnauthorizedLCSClientImpl(unauthorizedLCSClientDiagnostic, extensionContainer);
    }

    public MAPErrorMessagePositionMethodFailure createMAPErrorMessagePositionMethodFailure(
            PositionMethodFailureDiagnostic positionMethodFailureDiagnostic, MAPExtensionContainer extensionContainer) {
        return new MAPErrorMessagePositionMethodFailureImpl(positionMethodFailureDiagnostic, extensionContainer);
    }

    public MAPErrorMessageBusySubscriber createMAPErrorMessageBusySubscriber(MAPExtensionContainer extensionContainer,
            boolean ccbsPossible, boolean ccbsBusy) {
        return new MAPErrorMessageBusySubscriberImpl(extensionContainer, ccbsPossible, ccbsBusy);
    }

    public MAPErrorMessageCUGReject createMAPErrorMessageCUGReject(CUGRejectCause cugRejectCause,
            MAPExtensionContainer extensionContainer) {
        return new MAPErrorMessageCUGRejectImpl(cugRejectCause, extensionContainer);
    }

    public MAPErrorMessageRoamingNotAllowed createMAPErrorMessageRoamingNotAllowed(
            RoamingNotAllowedCause roamingNotAllowedCause, MAPExtensionContainer extensionContainer,
            AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause) {
        return new MAPErrorMessageRoamingNotAllowedImpl(roamingNotAllowedCause, extensionContainer,
                additionalRoamingNotAllowedCause);
    }

    public MAPErrorMessageSsErrorStatus createMAPErrorMessageSsErrorStatus(int data) {
        return new MAPErrorMessageSsErrorStatusImpl(data);
    }

    public MAPErrorMessageSsErrorStatus createMAPErrorMessageSsErrorStatus(boolean qBit, boolean pBit, boolean rBit,
            boolean aBit) {
        return new MAPErrorMessageSsErrorStatusImpl(qBit, pBit, rBit, aBit);
    }

    public MAPErrorMessageSsIncompatibility createMAPErrorMessageSsIncompatibility(SSCode ssCode,
            BasicServiceCode basicService, SSStatus ssStatus) {
        return new MAPErrorMessageSsIncompatibilityImpl(ssCode, basicService, ssStatus);
    }

    public MAPErrorMessagePwRegistrationFailure createMAPErrorMessagePwRegistrationFailure(
            PWRegistrationFailureCause pwRegistrationFailureCause) {
        return new MAPErrorMessagePwRegistrationFailureImpl(pwRegistrationFailureCause);
    }

}
