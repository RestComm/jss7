/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

/**
 *
 * IsupCauseIndicatorCauseValue ::= ENUMERATED { unallocated(1),
 * noRouteToTransitNet(2), noRouteToDest(3), sendSpecialTone(4),
 * misdialedTrunkPrefix(5), allClear(16), userBusy(17),
 *
 * noUserResponse(18), noAnswer(19), subscriberAbsent(20), callRejected(21),
 * numberChanged(22), rejectedDueToAcrSuppServices(24),
 *
 * exchangeRoutingError(25), destinationOutOfOrder(27), addressIncomplete(28),
 * facilityRejected(29), normalUnspecified(31),
 *
 * noCircuitAvailable(34), networkOutOfOrder(38), temporaryFailure(41),
 * switchEquipmentCongestion(42), userInformationDiscarded(43),
 *
 * requestedCircuitUnavailable(44), preemption(47), resourceUnavailable(47),
 * facilityNotSubscribed(50), incomingCallBarredWithinCug(55),
 *
 * bearerCapabilityNotAuthorized(57), bearerCapabilityNotAvailable(58),
 * serviceOrOptionNotAvailable(63), bearerCapabilityNotImplemented(65),
 *
 * facilityNotImplemented(69), restrictedDigitalBearedAvailable(70),
 * serviceOrOptionNotImplemented(79), invalidCallReference(81),
 *
 * calledUserNotMemberOfCug(87), incompatibleDestination(88),
 * invalidTransitNetworkSelection(91), invalidMessageUnspecified(95),
 *
 * mandatoryElementMissing(96), messageTypeNonExistent(97),
 * parameterNonExistentDiscard(99), invalidParameterContent(100),
 *
 * timeoutRecovery(102), parameterNonExistentPassAlong(103),
 * messageWithUnrecognizedParameterDiscarded(110),
 *
 * protocolError(111), internetworkingUnspecified(127) }
 *
 * @author mnowa
 *
 * @see Q.850
 */
public enum IsupCauseIndicatorCauseValue {
    unallocated(1), noRouteToTransitNet(2), noRouteToDest(3), sendSpecialTone(4), misdialedTrunkPrefix(5), allClear(16), userBusy(17),

    noUserResponse(18), noAnswer(19), subscriberAbsent(20), callRejected(21), numberChanged(22), rejectedDueToAcrSuppServices(24),

    exchangeRoutingError(25), destinationOutOfOrder(27), addressIncomplete(28), facilityRejected(29), normalUnspecified(31),

    noCircuitAvailable(34), networkOutOfOrder(38), temporaryFailure(41), switchEquipmentCongestion(42), userInformationDiscarded(43),

    requestedCircuitUnavailable(44), preemption(47), resourceUnavailable(47), facilityNotSubscribed(50), incomingCallBarredWithinCug(55),

    bearerCapabilityNotAuthorized(57), bearerCapabilityNotAvailable(58), serviceOrOptionNotAvailable(63), bearerCapabilityNotImplemented(65),

    facilityNotImplemented(69), restrictedDigitalBearedAvailable(70), serviceOrOptionNotImplemented(79), invalidCallReference(81),

    calledUserNotMemberOfCug(87), incompatibleDestination(88), invalidTransitNetworkSelection(91), invalidMessageUnspecified(95),

    mandatoryElementMissing(96), messageTypeNonExistent(97), parameterNonExistentDiscard(99), invalidParameterContent(100),

    timeoutRecovery(102), parameterNonExistentPassAlong(103), messageWithUnrecognizedParameterDiscarded(110),

    protocolError(111), internetworkingUnspecified(127);


    private int code;

    private IsupCauseIndicatorCauseValue(int code) {
        this.code = code;
    }

    public static IsupCauseIndicatorCauseValue getInstance(int code) {

        for (IsupCauseIndicatorCauseValue cv : values()){
            if ( cv.getCode() == code) {
                return cv;
            }
        }
        return null;
    }

    public int getCode() {
        return this.code;
    }
}
