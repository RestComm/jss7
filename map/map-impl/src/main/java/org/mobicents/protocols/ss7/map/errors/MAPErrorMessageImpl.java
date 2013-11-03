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

import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageBusySubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCUGReject;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
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
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * Base class of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 *
 */
public abstract class MAPErrorMessageImpl implements MAPErrorMessage, MAPAsnPrimitive {

    protected Long errorCode;

    protected MAPErrorMessageImpl(Long errorCode) {
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public boolean isEmParameterless() {
        return false;
    }

    public boolean isEmExtensionContainer() {
        return false;
    }

    public boolean isEmFacilityNotSup() {
        return false;
    }

    public boolean isEmSMDeliveryFailure() {
        return false;
    }

    public boolean isEmSystemFailure() {
        return false;
    }

    public boolean isEmUnknownSubscriber() {
        return false;
    }

    public boolean isEmAbsentSubscriberSM() {
        return false;
    }

    public boolean isEmAbsentSubscriber() {
        return false;
    }

    public boolean isEmSubscriberBusyForMtSms() {
        return false;
    }

    public boolean isEmCallBarred() {
        return false;
    }

    public boolean isEmUnauthorizedLCSClient() {
        return false;
    }

    public boolean isEmPositionMethodFailure() {
        return false;
    }

    public boolean isEmBusySubscriber() {
        return false;
    }

    public boolean isEmCUGReject() {
        return false;
    }

    public boolean isEmRoamingNotAllowed() {
        return false;
    }

    public boolean isEmSsErrorStatus() {
        return false;
    }

    public boolean isEmSsIncompatibility() {
        return false;
    }

    public boolean isEmPwRegistrationFailure() {
        return false;
    }

    public MAPErrorMessageParameterless getEmParameterless() {
        return null;
    }

    public MAPErrorMessageExtensionContainer getEmExtensionContainer() {
        return null;
    }

    public MAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
        return null;
    }

    public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
        return null;
    }

    public MAPErrorMessageSystemFailure getEmSystemFailure() {
        return null;
    }

    public MAPErrorMessageUnknownSubscriber getEmUnknownSubscriber() {
        return null;
    }

    public MAPErrorMessageAbsentSubscriberSM getEmAbsentSubscriberSM() {
        return null;
    }

    public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
        return null;
    }

    public MAPErrorMessageSubscriberBusyForMtSms getEmSubscriberBusyForMtSms() {
        return null;
    }

    public MAPErrorMessageCallBarred getEmCallBarred() {
        return null;
    }

    public MAPErrorMessageUnauthorizedLCSClient getEmUnauthorizedLCSClient() {
        return null;
    }

    public MAPErrorMessagePositionMethodFailure getEmPositionMethodFailure() {
        return null;
    }

    public MAPErrorMessageBusySubscriber getEmBusySubscriber() {
        return null;
    }

    public MAPErrorMessageCUGReject getEmCUGReject() {
        return null;
    }

    public MAPErrorMessageRoamingNotAllowed getEmRoamingNotAllowed() {
        return null;
    }

    public MAPErrorMessageSsErrorStatus getEmSsErrorStatus() {
        return null;
    }

    public MAPErrorMessageSsIncompatibility getEmSsIncompatibility() {
        return null;
    }

    public MAPErrorMessagePwRegistrationFailure getEmPwRegistrationFailure() {
        return null;
    }

}
