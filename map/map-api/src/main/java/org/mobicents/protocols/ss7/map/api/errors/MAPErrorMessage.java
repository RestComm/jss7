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

package org.mobicents.protocols.ss7.map.api.errors;

import java.io.Serializable;

/**
 * Base class of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPErrorMessage extends Serializable {

    Long getErrorCode();

    boolean isEmParameterless();

    boolean isEmExtensionContainer();

    boolean isEmFacilityNotSup();

    boolean isEmSMDeliveryFailure();

    boolean isEmSystemFailure();

    boolean isEmUnknownSubscriber();

    boolean isEmAbsentSubscriberSM();

    boolean isEmAbsentSubscriber();

    boolean isEmSubscriberBusyForMtSms();

    boolean isEmCallBarred();

    boolean isEmUnauthorizedLCSClient();

    boolean isEmPositionMethodFailure();

    MAPErrorMessageParameterless getEmParameterless();

    MAPErrorMessageExtensionContainer getEmExtensionContainer();

    MAPErrorMessageFacilityNotSup getEmFacilityNotSup();

    MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure();

    MAPErrorMessageSystemFailure getEmSystemFailure();

    MAPErrorMessageUnknownSubscriber getEmUnknownSubscriber();

    MAPErrorMessageAbsentSubscriberSM getEmAbsentSubscriberSM();

    MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber();

    MAPErrorMessageSubscriberBusyForMtSms getEmSubscriberBusyForMtSms();

    MAPErrorMessageCallBarred getEmCallBarred();

    MAPErrorMessageUnauthorizedLCSClient getEmUnauthorizedLCSClient();

    MAPErrorMessagePositionMethodFailure getEmPositionMethodFailure();

}
