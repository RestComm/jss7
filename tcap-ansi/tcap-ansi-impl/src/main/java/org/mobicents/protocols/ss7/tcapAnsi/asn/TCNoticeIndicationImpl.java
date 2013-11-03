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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TCNoticeIndicationImpl implements TCNoticeIndication {

    private SccpAddress localAddress;
    private SccpAddress remoteAddress;
    private ReturnCauseValue reportCause;
    private Dialog dialog;

    public SccpAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(SccpAddress val) {
        localAddress = val;
    }

    public SccpAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(SccpAddress val) {
        remoteAddress = val;
    }

    public ReturnCauseValue getReportCause() {
        return reportCause;
    }

    public void setReportCause(ReturnCauseValue val) {
        reportCause = val;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog val) {
        dialog = val;
    }
}
