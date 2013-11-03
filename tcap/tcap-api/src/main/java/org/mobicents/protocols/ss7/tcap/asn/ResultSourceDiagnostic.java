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

package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface ResultSourceDiagnostic extends Encodable {

    // Annoying... TL[CHOICE[TL[TLV]]

    int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    boolean _TAG_PC_PRIMITIVE = false; // constructed....
    int _TAG = 0x03;

    // membersL CHOICE
    int _TAG_U_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    boolean _TAG_U_PC_PRIMITIVE = false; // constructed....
    int _TAG_U = 0x01;

    int _TAG_P_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    boolean _TAG_P_PC_PRIMITIVE = false; // constructed....
    int _TAG_P = 0x02;

    void setDialogServiceProviderType(DialogServiceProviderType t);

    DialogServiceProviderType getDialogServiceProviderType();

    void setDialogServiceUserType(DialogServiceUserType t);

    DialogServiceUserType getDialogServiceUserType();

}
