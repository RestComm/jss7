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