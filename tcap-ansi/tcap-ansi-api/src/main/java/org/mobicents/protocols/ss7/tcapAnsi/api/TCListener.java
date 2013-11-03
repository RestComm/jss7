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

package org.mobicents.protocols.ss7.tcapAnsi.api;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface TCListener {

    void onTCUni(TCUniIndication ind);

    void onTCQuery(TCQueryIndication ind);

    void onTCConversation(TCConversationIndication ind);

    void onTCResponse(TCResponseIndication ind);

    void onTCUserAbort(TCUserAbortIndication ind);

    void onTCPAbort(TCPAbortIndication ind);

    void onTCNotice(TCNoticeIndication ind);

    /**
     * Called once dialog is released. It is invoked once primitives are delivered. Indicates that stack has no reference, and
     * dialog object is considered invalid.
     *
     * @param d
     */
    void onDialogReleased(Dialog d);

    /**
     *
     * @param tcInvokeRequest
     */
    void onInvokeTimeout(Invoke tcInvokeRequest);

    /**
     * Called once dialog times out. Once this method is called, dialog cant be used anymore.
     *
     * @param d
     */
    void onDialogTimeout(Dialog d);

}
