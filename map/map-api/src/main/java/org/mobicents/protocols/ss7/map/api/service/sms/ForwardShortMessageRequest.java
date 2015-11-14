/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.sms;

/**
 *
 MAP V1-2:
 *
 * ForwardSM ::= OPERATION --Timer ml ARGUMENT forwardSM-Arg ForwardSM-Arg RESULT ERRORS { SystemFailure, DataMissing, --
 * DataMissing must not be used in version 1 UnexpectedDataValue, FacilityNotSupported, UnidentifiedSubscriber,
 * IllegalSubscriber, IllegalEquipment, -- IllegalEquipment must not be used in version 1 AbsentSubscriber,
 * SubscriberBusyForMT-SMS, -- SubscriberBusyForMT-SMS must not be used in version 1 SM-DeliveryFailure}
 *
 * ForwardSM-Arg ::= SEQUENCE { sm-RP-DA SM-RP-DA, sm-RP-OA SM-RP-OA, sm-RP-UI SignalInfo, moreMessagesToSend NULL OPTIONAL, --
 * moreMessagesToSend must be absent in version 1 ...}
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ForwardShortMessageRequest extends SmsMessage {

    SM_RP_DA getSM_RP_DA();

    SM_RP_OA getSM_RP_OA();

    SmsSignalInfo getSM_RP_UI();

    boolean getMoreMessagesToSend();

}
