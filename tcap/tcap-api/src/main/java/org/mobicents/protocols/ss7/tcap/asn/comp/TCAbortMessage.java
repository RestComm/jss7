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

package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * This message represents Abort messages (P and U). According to Q.773:<br>
 *
 * <pre>
 * Abort ::= SEQUENCE {
 * dtid DestTransactionID,
 * reason CHOICE
 * { p-abortCause P-AbortCause,
 * u-abortCause DialoguePortion } OPTIONAL
 * }
 * </pre>
 *
 * Cryptic ASN say:
 * <ul>
 * <li><b>TC-U-Abort</b> - if abort APDU is present</li>
 * <li><b>TC-P-Abort</b> - if P-Abort-Cause is present</li>
 * </ul>
 *
 * @author baranowb
 *
 */
public interface TCAbortMessage extends Encodable {

    int _TAG = 0x07;
    boolean _TAG_PC_PRIMITIVE = false;
    int _TAG_CLASS = Tag.CLASS_APPLICATION;

    int _TAG_P = 0x0A;
    boolean _TAG_P_PC_PRIMITIVE = true;
    int _TAG_CLASS_P = Tag.CLASS_APPLICATION;

    int _TAG_DTX = 0x09;
    boolean _TAG_DTX_PC_PRIMITIVE = true;
    int _TAG_CLASS_DTX = Tag.CLASS_APPLICATION;

    // mandatory
    byte[] getDestinationTransactionId();

    void setDestinationTransactionId(byte[] t);

    // optionals
    PAbortCauseType getPAbortCause();

    void setPAbortCause(PAbortCauseType t);

    DialogPortion getDialogPortion();

    void setDialogPortion(DialogPortion dp);

    // External getUserDefinedReason();
    //
    // void setUserDefinedReason(External dp);
}
