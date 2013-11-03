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

/**
 *
 MAP V3:
 *
 * registerCC-Entry OPERATION ::= { --Timer m ARGUMENT RegisterCC-EntryArg RESULT RegisterCC-EntryRes ERRORS { systemFailure |
 * dataMissing | unexpectedDataValue | callBarred | illegalSS-Operation | ss-ErrorStatus | ss-Incompatibility | shortTermDenial
 * | longTermDenial | facilityNotSupported} CODE local:76 }
 *
 * RegisterCC-EntryArg ::= SEQUENCE { ss-Code [0] SS-Code, ccbs-Data [1] CCBS-Data OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RegisterCCEntryRequest extends SupplementaryMessage {

    SSCode getSsCode();

    CCBSData getCcbsData();

}