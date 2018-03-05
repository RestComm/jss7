/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;

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
