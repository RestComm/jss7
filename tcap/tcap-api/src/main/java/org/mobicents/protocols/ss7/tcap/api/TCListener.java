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

package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface TCListener {

    // dialog handlers
    /**
     * Invoked for TC_UNI. See Q.771 3.1.2.2.2.1
     */
    void onTCUni(TCUniIndication ind);

    /**
     * Invoked for TC_BEGIN. See Q.771 3.1.2.2.2.1
     */
    void onTCBegin(TCBeginIndication ind);

    /**
     * Invoked for TC_CONTINUE dialog primitive. See Q.771 3.1.2.2.2.2/3.1.2.2.2.3
     *
     * @param ind
     */
    void onTCContinue(TCContinueIndication ind);

    /**
     * Invoked for TC_END dialog primitive. See Q.771 3.1.2.2.2.4
     *
     * @param ind
     */
    void onTCEnd(TCEndIndication ind);

    /**
     * Invoked for TC-U-Abort primitive(P-Abort-Cause is present.). See Q.771 3.1.2.2.2.4
     *
     * @param ind
     */
    void onTCUserAbort(TCUserAbortIndication ind);

    /**
     * Invoked TC-P-Abort (when dialog has been terminated by some unpredicatable environment cause). See Q.771 3.1.4.2
     *
     * @param ind
     */
    void onTCPAbort(TCPAbortIndication ind);

    /**
     * Invoked when TC-Notice primitive has been received. A TC-NOTICE indication primitive is only passed to the TC-user if the
     * requested service (i.e. transfer of components) cannot be provided (the network layer cannot deliver the embedded message
     * to the remote node) and the TC-user requested the return option in the Quality of Service parameter of the dialogue
     * handling request primitive.
     *
     * @param ind
     */
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
