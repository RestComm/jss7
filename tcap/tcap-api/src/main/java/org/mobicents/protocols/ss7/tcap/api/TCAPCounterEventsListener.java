/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

public interface TCAPCounterEventsListener {

    void updateTcUniReceivedCount(Dialog dialog);

    void updateTcUniSentCount(Dialog dialog);

    void updateTcBeginReceivedCount(Dialog dialog);

    void updateTcBeginSentCount(Dialog dialog);

    void updateTcContinueReceivedCount(Dialog dialog);

    void updateTcContinueSentCount(Dialog dialog);

    void updateTcEndReceivedCount(Dialog dialog);

    void updateTcEndSentCount(Dialog dialog);

    void updateTcPAbortReceivedCount(Dialog dialog, PAbortCauseType cause);

    void updateTcPAbortSentCount(byte[] originatingTransactionId, PAbortCauseType cause);

    void updateTcUserAbortReceivedCount(Dialog dialog);

    void updateTcUserAbortSentCount(Dialog dialog);

    void updateInvokeReceivedCount(Dialog dialog, Invoke invoke);

    void updateInvokeSentCount(Dialog dialog, Invoke invoke);

    void updateReturnResultReceivedCount(Dialog dialog);

    void updateReturnResultSentCount(Dialog dialog);

    void updateReturnResultLastReceivedCount(Dialog dialog);

    void updateReturnResultLastSentCount(Dialog dialog);

    void updateReturnErrorReceivedCount(Dialog dialog);

    void updateReturnErrorSentCount(Dialog dialog);

    void updateRejectReceivedCount(Dialog dialog);

    void updateRejectSentCount(Dialog dialog);

    void updateDialogTimeoutCount(Dialog dialog);

    void updateDialogReleaseCount(Dialog dialog);
}
