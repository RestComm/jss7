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

package org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Encodable;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public interface TCQueryMessage extends Encodable {

    int _TAG_QUERY_WITH_PERM = 2;
    int _TAG_QUERY_WITHOUT_PERM = 3;
    int _TAG_TRANSACTION_ID = 7;
    int _TAG_COMPONENT_SEQUENCE = 8;


    boolean getDialogTermitationPermission();

    void setDialogTermitationPermission(boolean perm);

    byte[] getOriginatingTransactionId();

    void setOriginatingTransactionId(byte[] t);

    DialogPortion getDialogPortion();

    void setDialogPortion(DialogPortion dp);

    Component[] getComponent();

    void setComponent(Component[] c);

}
