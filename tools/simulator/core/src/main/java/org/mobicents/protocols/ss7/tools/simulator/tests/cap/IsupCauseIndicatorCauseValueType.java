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

import java.util.Hashtable;

import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author mnowa
 *
 */
public class IsupCauseIndicatorCauseValueType extends EnumeratedBase {

    private static final long serialVersionUID = 8873666813197137562L;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(IsupCauseIndicatorCauseValue.unallocated.getCode(), IsupCauseIndicatorCauseValue.unallocated.toString());
        intMap.put(IsupCauseIndicatorCauseValue.noRouteToTransitNet.getCode(), IsupCauseIndicatorCauseValue.noRouteToTransitNet.toString());
        intMap.put(IsupCauseIndicatorCauseValue.noRouteToDest.getCode(), IsupCauseIndicatorCauseValue.noRouteToDest.toString());
        intMap.put(IsupCauseIndicatorCauseValue.sendSpecialTone.getCode(), IsupCauseIndicatorCauseValue.sendSpecialTone.toString());
        intMap.put(IsupCauseIndicatorCauseValue.misdialedTrunkPrefix.getCode(), IsupCauseIndicatorCauseValue.misdialedTrunkPrefix.toString());
        intMap.put(IsupCauseIndicatorCauseValue.allClear.getCode(), IsupCauseIndicatorCauseValue.allClear.toString());
        intMap.put(IsupCauseIndicatorCauseValue.userBusy.getCode(), IsupCauseIndicatorCauseValue.userBusy.toString());
        intMap.put(IsupCauseIndicatorCauseValue.noUserResponse.getCode(), IsupCauseIndicatorCauseValue.noUserResponse.toString());
        intMap.put(IsupCauseIndicatorCauseValue.noAnswer.getCode(), IsupCauseIndicatorCauseValue.noAnswer.toString());
        intMap.put(IsupCauseIndicatorCauseValue.subscriberAbsent.getCode(), IsupCauseIndicatorCauseValue.subscriberAbsent.toString());
        intMap.put(IsupCauseIndicatorCauseValue.callRejected.getCode(), IsupCauseIndicatorCauseValue.callRejected.toString());
        intMap.put(IsupCauseIndicatorCauseValue.numberChanged.getCode(), IsupCauseIndicatorCauseValue.numberChanged.toString());
        intMap.put(IsupCauseIndicatorCauseValue.rejectedDueToAcrSuppServices.getCode(), IsupCauseIndicatorCauseValue.rejectedDueToAcrSuppServices.toString());
        intMap.put(IsupCauseIndicatorCauseValue.exchangeRoutingError.getCode(), IsupCauseIndicatorCauseValue.exchangeRoutingError.toString());
        intMap.put(IsupCauseIndicatorCauseValue.destinationOutOfOrder.getCode(), IsupCauseIndicatorCauseValue.destinationOutOfOrder.toString());
        intMap.put(IsupCauseIndicatorCauseValue.addressIncomplete.getCode(), IsupCauseIndicatorCauseValue.addressIncomplete.toString());
        intMap.put(IsupCauseIndicatorCauseValue.facilityRejected.getCode(), IsupCauseIndicatorCauseValue.facilityRejected.toString());
        intMap.put(IsupCauseIndicatorCauseValue.normalUnspecified.getCode(), IsupCauseIndicatorCauseValue.normalUnspecified.toString());
        intMap.put(IsupCauseIndicatorCauseValue.noCircuitAvailable.getCode(), IsupCauseIndicatorCauseValue.noCircuitAvailable.toString());
        intMap.put(IsupCauseIndicatorCauseValue.networkOutOfOrder.getCode(), IsupCauseIndicatorCauseValue.networkOutOfOrder.toString());
        intMap.put(IsupCauseIndicatorCauseValue.temporaryFailure.getCode(), IsupCauseIndicatorCauseValue.temporaryFailure.toString());
        intMap.put(IsupCauseIndicatorCauseValue.switchEquipmentCongestion.getCode(), IsupCauseIndicatorCauseValue.switchEquipmentCongestion.toString());
        intMap.put(IsupCauseIndicatorCauseValue.userInformationDiscarded.getCode(), IsupCauseIndicatorCauseValue.userInformationDiscarded.toString());
        intMap.put(IsupCauseIndicatorCauseValue.requestedCircuitUnavailable.getCode(), IsupCauseIndicatorCauseValue.requestedCircuitUnavailable.toString());
        intMap.put(IsupCauseIndicatorCauseValue.preemption.getCode(), IsupCauseIndicatorCauseValue.preemption.toString());
        intMap.put(IsupCauseIndicatorCauseValue.resourceUnavailable.getCode(), IsupCauseIndicatorCauseValue.resourceUnavailable.toString());
        intMap.put(IsupCauseIndicatorCauseValue.facilityNotSubscribed.getCode(), IsupCauseIndicatorCauseValue.facilityNotSubscribed.toString());
        intMap.put(IsupCauseIndicatorCauseValue.incomingCallBarredWithinCug.getCode(), IsupCauseIndicatorCauseValue.incomingCallBarredWithinCug.toString());
        intMap.put(IsupCauseIndicatorCauseValue.bearerCapabilityNotAuthorized.getCode(), IsupCauseIndicatorCauseValue.bearerCapabilityNotAuthorized.toString());
        intMap.put(IsupCauseIndicatorCauseValue.bearerCapabilityNotAvailable.getCode(), IsupCauseIndicatorCauseValue.bearerCapabilityNotAvailable.toString());
        intMap.put(IsupCauseIndicatorCauseValue.serviceOrOptionNotAvailable.getCode(), IsupCauseIndicatorCauseValue.serviceOrOptionNotAvailable.toString());
        intMap.put(IsupCauseIndicatorCauseValue.bearerCapabilityNotImplemented.getCode(), IsupCauseIndicatorCauseValue.bearerCapabilityNotImplemented.toString());
        intMap.put(IsupCauseIndicatorCauseValue.facilityNotImplemented.getCode(), IsupCauseIndicatorCauseValue.facilityNotImplemented.toString());
        intMap.put(IsupCauseIndicatorCauseValue.restrictedDigitalBearedAvailable.getCode(), IsupCauseIndicatorCauseValue.restrictedDigitalBearedAvailable.toString());
        intMap.put(IsupCauseIndicatorCauseValue.serviceOrOptionNotImplemented.getCode(), IsupCauseIndicatorCauseValue.serviceOrOptionNotImplemented.toString());
        intMap.put(IsupCauseIndicatorCauseValue.invalidCallReference.getCode(), IsupCauseIndicatorCauseValue.invalidCallReference.toString());
        intMap.put(IsupCauseIndicatorCauseValue.calledUserNotMemberOfCug.getCode(), IsupCauseIndicatorCauseValue.calledUserNotMemberOfCug.toString());
        intMap.put(IsupCauseIndicatorCauseValue.incompatibleDestination.getCode(), IsupCauseIndicatorCauseValue.incompatibleDestination.toString());
        intMap.put(IsupCauseIndicatorCauseValue.invalidTransitNetworkSelection.getCode(), IsupCauseIndicatorCauseValue.invalidTransitNetworkSelection.toString());
        intMap.put(IsupCauseIndicatorCauseValue.invalidMessageUnspecified.getCode(), IsupCauseIndicatorCauseValue.invalidMessageUnspecified.toString());
        intMap.put(IsupCauseIndicatorCauseValue.mandatoryElementMissing.getCode(), IsupCauseIndicatorCauseValue.mandatoryElementMissing.toString());
        intMap.put(IsupCauseIndicatorCauseValue.messageTypeNonExistent.getCode(), IsupCauseIndicatorCauseValue.messageTypeNonExistent.toString());
        intMap.put(IsupCauseIndicatorCauseValue.parameterNonExistentDiscard.getCode(), IsupCauseIndicatorCauseValue.parameterNonExistentDiscard.toString());
        intMap.put(IsupCauseIndicatorCauseValue.invalidParameterContent.getCode(), IsupCauseIndicatorCauseValue.invalidParameterContent.toString());
        intMap.put(IsupCauseIndicatorCauseValue.timeoutRecovery.getCode(), IsupCauseIndicatorCauseValue.timeoutRecovery.toString());
        intMap.put(IsupCauseIndicatorCauseValue.parameterNonExistentPassAlong.getCode(), IsupCauseIndicatorCauseValue.parameterNonExistentPassAlong.toString());
        intMap.put(IsupCauseIndicatorCauseValue.messageWithUnrecognizedParameterDiscarded.getCode(), IsupCauseIndicatorCauseValue.messageWithUnrecognizedParameterDiscarded.toString());
        intMap.put(IsupCauseIndicatorCauseValue.protocolError.getCode(), IsupCauseIndicatorCauseValue.protocolError.toString());
        intMap.put(IsupCauseIndicatorCauseValue.internetworkingUnspecified.getCode(), IsupCauseIndicatorCauseValue.internetworkingUnspecified.toString());

        stringMap.put(IsupCauseIndicatorCauseValue.unallocated.toString(), IsupCauseIndicatorCauseValue.unallocated.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.noRouteToTransitNet.toString(), IsupCauseIndicatorCauseValue.noRouteToTransitNet.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.noRouteToDest.toString(), IsupCauseIndicatorCauseValue.noRouteToDest.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.sendSpecialTone.toString(), IsupCauseIndicatorCauseValue.sendSpecialTone.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.misdialedTrunkPrefix.toString(), IsupCauseIndicatorCauseValue.misdialedTrunkPrefix.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.allClear.toString(), IsupCauseIndicatorCauseValue.allClear.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.userBusy.toString(), IsupCauseIndicatorCauseValue.userBusy.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.noUserResponse.toString(), IsupCauseIndicatorCauseValue.noUserResponse.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.noAnswer.toString(), IsupCauseIndicatorCauseValue.noAnswer.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.subscriberAbsent.toString(), IsupCauseIndicatorCauseValue.subscriberAbsent.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.callRejected.toString(), IsupCauseIndicatorCauseValue.callRejected.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.numberChanged.toString(), IsupCauseIndicatorCauseValue.numberChanged.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.rejectedDueToAcrSuppServices.toString(), IsupCauseIndicatorCauseValue.rejectedDueToAcrSuppServices.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.exchangeRoutingError.toString(), IsupCauseIndicatorCauseValue.exchangeRoutingError.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.destinationOutOfOrder.toString(), IsupCauseIndicatorCauseValue.destinationOutOfOrder.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.addressIncomplete.toString(), IsupCauseIndicatorCauseValue.addressIncomplete.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.facilityRejected.toString(), IsupCauseIndicatorCauseValue.facilityRejected.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.normalUnspecified.toString(), IsupCauseIndicatorCauseValue.normalUnspecified.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.noCircuitAvailable.toString(), IsupCauseIndicatorCauseValue.noCircuitAvailable.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.networkOutOfOrder.toString(), IsupCauseIndicatorCauseValue.networkOutOfOrder.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.temporaryFailure.toString(), IsupCauseIndicatorCauseValue.temporaryFailure.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.switchEquipmentCongestion.toString(), IsupCauseIndicatorCauseValue.switchEquipmentCongestion.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.userInformationDiscarded.toString(), IsupCauseIndicatorCauseValue.userInformationDiscarded.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.requestedCircuitUnavailable.toString(), IsupCauseIndicatorCauseValue.requestedCircuitUnavailable.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.preemption.toString(), IsupCauseIndicatorCauseValue.preemption.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.resourceUnavailable.toString(), IsupCauseIndicatorCauseValue.resourceUnavailable.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.facilityNotSubscribed.toString(), IsupCauseIndicatorCauseValue.facilityNotSubscribed.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.incomingCallBarredWithinCug.toString(), IsupCauseIndicatorCauseValue.incomingCallBarredWithinCug.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.bearerCapabilityNotAuthorized.toString(), IsupCauseIndicatorCauseValue.bearerCapabilityNotAuthorized.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.bearerCapabilityNotAvailable.toString(), IsupCauseIndicatorCauseValue.bearerCapabilityNotAvailable.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.serviceOrOptionNotAvailable.toString(), IsupCauseIndicatorCauseValue.serviceOrOptionNotAvailable.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.bearerCapabilityNotImplemented.toString(), IsupCauseIndicatorCauseValue.bearerCapabilityNotImplemented.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.facilityNotImplemented.toString(), IsupCauseIndicatorCauseValue.facilityNotImplemented.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.restrictedDigitalBearedAvailable.toString(), IsupCauseIndicatorCauseValue.restrictedDigitalBearedAvailable.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.serviceOrOptionNotImplemented.toString(), IsupCauseIndicatorCauseValue.serviceOrOptionNotImplemented.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.invalidCallReference.toString(), IsupCauseIndicatorCauseValue.invalidCallReference.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.calledUserNotMemberOfCug.toString(), IsupCauseIndicatorCauseValue.calledUserNotMemberOfCug.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.incompatibleDestination.toString(), IsupCauseIndicatorCauseValue.incompatibleDestination.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.invalidTransitNetworkSelection.toString(), IsupCauseIndicatorCauseValue.invalidTransitNetworkSelection.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.invalidMessageUnspecified.toString(), IsupCauseIndicatorCauseValue.invalidMessageUnspecified.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.mandatoryElementMissing.toString(), IsupCauseIndicatorCauseValue.mandatoryElementMissing.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.messageTypeNonExistent.toString(), IsupCauseIndicatorCauseValue.messageTypeNonExistent.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.parameterNonExistentDiscard.toString(), IsupCauseIndicatorCauseValue.parameterNonExistentDiscard.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.invalidParameterContent.toString(), IsupCauseIndicatorCauseValue.invalidParameterContent.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.timeoutRecovery.toString(), IsupCauseIndicatorCauseValue.timeoutRecovery.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.parameterNonExistentPassAlong.toString(), IsupCauseIndicatorCauseValue.parameterNonExistentPassAlong.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.messageWithUnrecognizedParameterDiscarded.toString(), IsupCauseIndicatorCauseValue.messageWithUnrecognizedParameterDiscarded.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.protocolError.toString(), IsupCauseIndicatorCauseValue.protocolError.getCode());
        stringMap.put(IsupCauseIndicatorCauseValue.internetworkingUnspecified.toString(), IsupCauseIndicatorCauseValue.internetworkingUnspecified.getCode());
    }

    public IsupCauseIndicatorCauseValueType() {
    }

    public IsupCauseIndicatorCauseValueType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public IsupCauseIndicatorCauseValueType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public IsupCauseIndicatorCauseValueType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static IsupCauseIndicatorCauseValueType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new IsupCauseIndicatorCauseValueType(EquipmentStatus.whiteListed.getCode());
        else
            return new IsupCauseIndicatorCauseValueType(i1);
    }

    @Override
    protected Hashtable<Integer, String> getIntTable() {
        return intMap;
    }

    @Override
    protected Hashtable<String, Integer> getStringTable() {
        return stringMap;
    }
}
