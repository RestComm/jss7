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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

import java.io.Serializable;

/**
 *
 QualityOfService ::= SEQUENCE { requested-QoS [0] GPRS-QoS OPTIONAL, subscribed-QoS [1] GPRS-QoS OPTIONAL, negotiated-QoS [2]
 * GPRS-QoS OPTIONAL, ..., requested-QoS-Extension [3] GPRS-QoS-Extension OPTIONAL, subscribed-QoS-Extension [4]
 * GPRS-QoS-Extension OPTIONAL, negotiated-QoS-Extension [5] GPRS-QoS-Extension OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface QualityOfService extends Serializable {

    GPRSQoS getRequestedQoS();

    GPRSQoS getSubscribedQoS();

    GPRSQoS getNegotiatedQoS();

    GPRSQoSExtension getRequestedQoSExtension();

    GPRSQoSExtension getSubscribedQoSExtension();

    GPRSQoSExtension getNegotiatedQoSExtension();

}