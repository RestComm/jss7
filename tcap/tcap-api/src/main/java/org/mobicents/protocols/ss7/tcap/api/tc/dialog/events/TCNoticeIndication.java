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

package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import java.io.Serializable;

import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface TCNoticeIndication extends Serializable {

    SccpAddress getLocalAddress();

    void setLocalAddress(SccpAddress val);

    SccpAddress getRemoteAddress();

    void setRemoteAddress(SccpAddress val);

    ReturnCauseValue getReportCause();

    void setReportCause(ReturnCauseValue val);

    /*
     * This value can be equal null if TCAP can not match any existant Dialog to the incoming data
     */
    Dialog getDialog();

    void setDialog(Dialog val);

}
