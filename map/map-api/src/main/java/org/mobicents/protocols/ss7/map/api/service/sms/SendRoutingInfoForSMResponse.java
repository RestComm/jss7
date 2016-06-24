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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 * <code>
 * RoutingInfoForSM-Res ::= SEQUENCE {
 *          imsi IMSI,
 *          locationInfoWithLMSI [0] LocationInfoWithLMSI,
 *          extensionContainer [4] ExtensionContainer OPTIONAL,
 *          ...,
 *          ip-sm-gwGuidance [5] IP-SM-GW-Guidance OPTIONAL
 *          }
 *
 *
 * MAP V2: RoutingInfoForSM-Res::= SEQUENCE {
 *          imsi IMSI,
 *          locationInfoWithLMSI [0] LocationInfoWithLMSI,
 *          mwd-Set [2] BOOLEAN OPTIONAL, -- mwd-Set must be absent in version greater 1 ...
 *          }
 * </code>
 *
 * @author sergey vetyutnev
 *
 */
public interface SendRoutingInfoForSMResponse extends SmsMessage {

    IMSI getIMSI();

    LocationInfoWithLMSI getLocationInfoWithLMSI();

    MAPExtensionContainer getExtensionContainer();

    // for MAP V1 only
    Boolean getMwdSet();

    IpSmGwGuidance getIpSmGwGuidance();

}
