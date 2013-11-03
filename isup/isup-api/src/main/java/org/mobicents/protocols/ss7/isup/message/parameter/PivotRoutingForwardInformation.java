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

/**
 * Start time:13:48:01 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:48:01 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PivotRoutingForwardInformation extends ISUPParameter {
    // FIXME: fill this!
    int _PARAMETER_CODE = 0x88;

    int INFORMATION_RETURN_TO_INVOKING_EXCHANGE_POSSIBLE = 0x01;
    int INFORMATION_RETURN_TO_INVOKING_EXCHANGE_CALL_ID = 0x02;
    int INFORMATION_PERFORMING_PIVOT_INDICATOR = 0x03;
    int INFORMATION_INVOKING_PIVOT_REASON = 0x04;
    void setReturnToInvokingExchangePossible(ReturnToInvokingExchangePossible... duration);
    ReturnToInvokingExchangePossible[] getReturnToInvokingExchangePossible();

    void setReturnToInvokingExchangeCallIdentifier(ReturnToInvokingExchangeCallIdentifier... cid);
    ReturnToInvokingExchangeCallIdentifier[] getReturnToInvokingExchangeCallIdentifier();

    void setPerformingPivotIndicator(PerformingPivotIndicator... reason);
    PerformingPivotIndicator[] getPerformingPivotIndicator();

    void setInvokingPivotReason(InvokingPivotReason... reason);
    InvokingPivotReason[] getInvokingPivotReason();
}
