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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

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
public interface QualityOfService {

    GPRSQoS getRequestedQoS();

    GPRSQoS getSubscribedQoS();

    GPRSQoS getNegotiatedQoS();

    GPRSQoSExtension getRequestedQoSExtension();

    GPRSQoSExtension getSubscribedQoSExtension();

    GPRSQoSExtension getNegotiatedQoSExtension();

}