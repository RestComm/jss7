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
 * @author baranowb
 *
 */
public interface TCBeginMessage extends Encodable {

    int _TAG = 0x02;
    boolean _TAG_PC_PRIMITIVE = false;
    int _TAG_CLASS = Tag.CLASS_APPLICATION;

    int _TAG_OTX = 0x08;
    boolean _TAG_OTX_PC_PRIMITIVE = true;
    int _TAG_CLASS_OTX = Tag.CLASS_APPLICATION;

    // mandatory
    byte[] getOriginatingTransactionId();

    void setOriginatingTransactionId(byte[] t);

    // opt FIXME: make this External?
    DialogPortion getDialogPortion();

    void setDialogPortion(DialogPortion dp);

    // opt
    Component[] getComponent();

    void setComponent(Component[] c);

}
