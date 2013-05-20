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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

/**
 *
 MAP V2:
 *
 * registerSS OPERATION ::= { --Timer m ARGUMENT RegisterSS-Arg RESULT SS-Info -- optional ERRORS { systemFailure | dataMissing
 * | unexpectedDataValue | bearerServiceNotProvisioned | teleserviceNotProvisioned | callBarred | illegalSS-Operation |
 * ss-ErrorStatus | ss-Incompatibility} CODE local:10 }
 *
 * RegisterSS-Arg ::= SEQUENCE { ss-Code SS-Code, basicService BasicServiceCode OPTIONAL, forwardedToNumber [4] AddressString
 * OPTIONAL, forwardedToSubaddress [6] ISDN-SubaddressString OPTIONAL, noReplyConditionTime [5] NoReplyConditionTime OPTIONAL,
 * ..., defaultPriority [7] EMLPP-Priority OPTIONAL, nbrUser [8] MC-Bearers OPTIONAL, longFTN-Supported [9] NULL OPTIONAL }
 *
 * NoReplyConditionTime ::= INTEGER (5..30)
 *
 * MC-Bearers ::= INTEGER (1..7)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RegisterSSRequest extends SupplementaryMessage {

    SSCode getSsCode();

    BasicServiceCode getBasicService();

    AddressString getForwardedToNumber();

    ISDNAddressString getForwardedToSubaddress();

    Integer getNoReplyConditionTime();

    EMLPPPriority getDefaultPriority();

    Integer getNbrUser();

    ISDNAddressString getLongFTNSupported();

}