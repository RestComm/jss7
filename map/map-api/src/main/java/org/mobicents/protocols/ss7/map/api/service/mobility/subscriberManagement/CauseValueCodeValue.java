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
package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 * <pre>
 * -- The cause value is divided in to two fields, a class (bits 5 through 7)
 * -- and a value within the class (bits 1 through4). The class indicates
 * -- the general nature of the event.
 *
 * -- ex:   _______________________________________________________________________
 * --      | Class |  value within | decimal  |                                    |
 * --      |       |   the class   |  value   |          Description               |
 * --      |_______|_______________|__________|____________________________________|
 * --      | 000   |   0001        |   1      |   UnallocatedOrUnassignedNumber    |
 * --      |_______|_______________|_________ |____________________________________|
 * --
 * -- For the use of cause value refer to ITU-T Recommendation Q.850.
 * </pre>
 *
 *
 * @author Lasith Waruna Perera
 *
 */
public enum CauseValueCodeValue {

    /* 000 0001 1 */UnallocatedOrUnassignedNumber(0x01),
    /* 000 0010 2 */NoRouteToSpecifiedTransitNetwork(0x02),
    /* 000 0011 3 */NoRouteToDestination(0x03),
    /* 000 0100 4 */SendSpecialInformationTone(0x04),
    /* 000 0101 5 */MisdialledTrunkPrefix(0x05),
    /* 000 0110 6 */ChannelUnacceptable(0x06),
    /* 000 0111 7 */CallAwardedAndBeingDeliveredInAnEstablished(0x07),
    /* 000 1000 8 */PreemptionDSS(0x08),
    /* 000 1001 9 */PreemptionCircuitReservedForReuse(0x09),
    /* 001 0000 16 */NormalCallClearing(0x10),
    /* 001 0001 17 */UserBusyCCBSIndicator(0x11),
    /* 001 0010 18 */NoUserResponding(0x12),
    /* 001 0011 19 */NoAnswerFromUser(0x13),
    /* 001 0100 20 */SubscriberAbsent(0x14),
    /* 001 0101 21 */CallRejected(0x15),
    /* 001 0110 22 */NumberChangedNewdestination(0x16),
    /* 001 0111 23 */RedirectionToNewDestination(0x17),
    /* 001 1001 25 */ExchangeRoutingError(0x19),
    /* 001 1010 26 */NonSelectedUserClearing(0x1A),
    /* 001 1011 27 */DestinationOutOfOrder(0x1B),
    /* 001 1100 28 */InvalidNumberFormat(0x1C),
    /* 001 1101 29 */FacilityRejected(0x1D),
    /* 001 1110 30 */ResponseToSTATUS_ENQUIRY(0x1E),
    /* 001 1111 31 */NormalUnspecified(0x1F),
    /* 010 0010 34 */NoCircuitOrChannelAvailable(0x22),
    /* 010 0110 38 */NetworkOutOfOrder(0x26),
    /* 010 0111 39 */PermanentFrameModeConnection(0x27),
    /* 010 1000 40 */PermanentFrameModeConnectionOperational(0x28),
    /* 010 1001 41 */TemporaryFailure(0x29),
    /* 010 1010 42 */SwitchingEquipmentCongestion(0x2A),
    /* 010 1011 43 */AccessInformationDiscarded(0x2B),
    /* 010 1100 44 */RequestedCircuitChannelNotAvailable(0x2C),
    /* 010 1110 46 */PrecedenceCallBlocked(0x2E),
    /* 010 1111 47 */ResourceUnavailableUnspecified(0x2F),
    /* 011 0001 49 */QualityOfServiceNotAvailable(0x31),
    /* 011 0010 50 */RequestedFacilityNotSubscribed(0x32),
    /* 011 0101 53 */OutgoingCallsBarred(0x35),
    /* 011 0111 55 */IncomingCallsBarred(0x37),
    /* 011 1001 57 */BearerCapabilityNotAuthorized(0x39),
    /* 011 1010 58 */BearerCapabilityNotPresentlyAvailable(0x3A),
    /* 011 1110 62 */InconsistencyInDesignatedOutgoingAccessInformation(0x3E),
    /* 011 1111 63 */ServiceOrOptionNotAvailable(0x3F),
    /* 100 0001 65 */BearerCapabilityNotImplemented(0x41),
    /* 100 0010 66 */ChannelTypeNotImplemented(0x42),
    /* 100 0101 69 */RequestedFacilityNotImplemented(0x45),
    /* 100 0110 70 */OnlyRestrictedDigitalInformationBearerCapabilityIsAvailable(0x46),
    /* 100 1111 79 */ServiceOrOptionNotImplemented(0x4F),
    /* 101 0001 81 */InvalidCallReferenceValue(0x51),
    /* 101 0010 82 */IdentifiedChannelDoesNotExist(0x52),
    /* 101 0011 83 */ASuspendedCallExists(0x53),
    /* 101 0100 84 */CallIdentityInUse(0x54),
    /* 101 0101 85 */NoCallSuspended(0x55),
    /* 101 0110 86 */CallHavingTheRequestedCallIdentityHasBeenCleared(0x56),
    /* 101 0111 87 */UserNotMemberOfCUG(0x57),
    /* 101 1000 88 */IncompatibleDestinationIncompatibleParameter(0x58),
    /* 101 1010 90 */NonExistentCUG(0x5A),
    /* 101 1011 91 */InvalidTransitNetworkSelection(0x5B),
    /* 101 1111 95 */InvalidMessageUnspecified(0x5F),
    /* 110 0000 96 */MandatoryInformationElementIsMissing(0x60),
    /* 110 0001 97 */MessageTypeNonExistentOrNotImplemented(0x61),
    /* 110 0010 98 */MessageNotCompatibleWithCallStateOrMessageType(0x62),
    /* 110 0011 99 */InformationElementOrParameterNonexistentOrNotImplemented(0x63),
    /* 110 0100 100 */InvalidInformationElementContents(0x64),
    /* 110 0101 101 */MessageNotCompatibleWithCallState(0x65),
    /* 110 0110 102 */RecoveryOnTimer(0x66),
    /* 110 0111 103 */ParameterNonExistentOrNotImplemented(0x67),
    /* 110 1110 110 */MessageWithUnrecognizedParameter(0x6E),
    /* 110 1111 111 */ProtocolErrorUnspecified(0x6F),
    /* 111 1111 127 */InterworkingUnspecified(0x7F);

    private int code;

    private CauseValueCodeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CauseValueCodeValue getInstance(int code) {
        switch (code) {
            case 0x01:
                return UnallocatedOrUnassignedNumber;
            case 0x02:
                return NoRouteToSpecifiedTransitNetwork;
            case 0x03:
                return NoRouteToDestination;
            case 0x04:
                return SendSpecialInformationTone;
            case 0x05:
                return MisdialledTrunkPrefix;
            case 0x06:
                return ChannelUnacceptable;
            case 0x07:
                return CallAwardedAndBeingDeliveredInAnEstablished;
            case 0x08:
                return PreemptionDSS;
            case 0x09:
                return PreemptionCircuitReservedForReuse;
            case 0x10:
                return NormalCallClearing;
            case 0x11:
                return UserBusyCCBSIndicator;
            case 0x12:
                return NoUserResponding;
            case 0x13:
                return NoAnswerFromUser;
            case 0x14:
                return SubscriberAbsent;
            case 0x15:
                return CallRejected;
            case 0x16:
                return NumberChangedNewdestination;
            case 0x17:
                return RedirectionToNewDestination;
            case 0x19:
                return ExchangeRoutingError;
            case 0x1A:
                return NonSelectedUserClearing;
            case 0x1B:
                return DestinationOutOfOrder;
            case 0x1C:
                return InvalidNumberFormat;
            case 0x1D:
                return FacilityRejected;
            case 0x1E:
                return ResponseToSTATUS_ENQUIRY;
            case 0x1F:
                return NormalUnspecified;
            case 0x22:
                return NoCircuitOrChannelAvailable;
            case 0x26:
                return NetworkOutOfOrder;
            case 0x27:
                return PermanentFrameModeConnection;
            case 0x28:
                return PermanentFrameModeConnectionOperational;
            case 0x29:
                return TemporaryFailure;
            case 0x2A:
                return SwitchingEquipmentCongestion;
            case 0x2B:
                return AccessInformationDiscarded;
            case 0x2C:
                return RequestedCircuitChannelNotAvailable;
            case 0x2E:
                return PrecedenceCallBlocked;
            case 0x2F:
                return ResourceUnavailableUnspecified;
            case 0x31:
                return QualityOfServiceNotAvailable;
            case 0x32:
                return RequestedFacilityNotSubscribed;
            case 0x35:
                return OutgoingCallsBarred;
            case 0x37:
                return IncomingCallsBarred;
            case 0x39:
                return BearerCapabilityNotAuthorized;
            case 0x3A:
                return BearerCapabilityNotPresentlyAvailable;
            case 0x3E:
                return InconsistencyInDesignatedOutgoingAccessInformation;
            case 0x3F:
                return ServiceOrOptionNotAvailable;
            case 0x41:
                return BearerCapabilityNotImplemented;
            case 0x42:
                return ChannelTypeNotImplemented;
            case 0x45:
                return RequestedFacilityNotImplemented;
            case 0x46:
                return OnlyRestrictedDigitalInformationBearerCapabilityIsAvailable;
            case 0x4F:
                return ServiceOrOptionNotImplemented;
            case 0x51:
                return InvalidCallReferenceValue;
            case 0x52:
                return IdentifiedChannelDoesNotExist;
            case 0x53:
                return ASuspendedCallExists;
            case 0x54:
                return CallIdentityInUse;
            case 0x55:
                return NoCallSuspended;
            case 0x56:
                return CallHavingTheRequestedCallIdentityHasBeenCleared;
            case 0x57:
                return UserNotMemberOfCUG;
            case 0x58:
                return IncompatibleDestinationIncompatibleParameter;
            case 0x5A:
                return NonExistentCUG;
            case 0x5B:
                return InvalidTransitNetworkSelection;
            case 0x5F:
                return InvalidMessageUnspecified;
            case 0x60:
                return MandatoryInformationElementIsMissing;
            case 0x61:
                return MessageTypeNonExistentOrNotImplemented;
            case 0x62:
                return MessageNotCompatibleWithCallStateOrMessageType;
            case 0x63:
                return InformationElementOrParameterNonexistentOrNotImplemented;
            case 0x64:
                return InvalidInformationElementContents;
            case 0x65:
                return MessageNotCompatibleWithCallState;
            case 0x66:
                return RecoveryOnTimer;
            case 0x67:
                return ParameterNonExistentOrNotImplemented;
            case 0x6E:
                return MessageWithUnrecognizedParameter;
            case 0x6F:
                return ProtocolErrorUnspecified;
            case 0x7F:
                return InterworkingUnspecified;
            default:
                return null;
        }
    }
}
