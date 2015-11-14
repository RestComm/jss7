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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 O-BcsmCamelTDP-Criteria ::= SEQUENCE { o-BcsmTriggerDetectionPoint O-BcsmTriggerDetectionPoint, destinationNumberCriteria [0]
 * DestinationNumberCriteria OPTIONAL, basicServiceCriteria [1] BasicServiceCriteria OPTIONAL, callTypeCriteria [2]
 * CallTypeCriteria OPTIONAL, ..., o-CauseValueCriteria [3] O-CauseValueCriteria OPTIONAL, extensionContainer [4]
 * ExtensionContainer OPTIONAL }
 *
 * BasicServiceCriteria ::= SEQUENCE SIZE(1..5) OF Ext-BasicServiceCode
 *
 * O-CauseValueCriteria ::= SEQUENCE SIZE(1..5) OF CauseValue
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface OBcsmCamelTdpCriteria extends Serializable {

    OBcsmTriggerDetectionPoint getOBcsmTriggerDetectionPoint();

    DestinationNumberCriteria getDestinationNumberCriteria();

    ArrayList<ExtBasicServiceCode> getBasicServiceCriteria();

    CallTypeCriteria getCallTypeCriteria();

    ArrayList<CauseValue> getOCauseValueCriteria();

    MAPExtensionContainer getExtensionContainer();

}
