/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:13:53:58 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:53:58 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RedirectForwardInformation extends ISUPParameter {
    // FIXME: fill this!
    int _PARAMETER_CODE = 0x8C;

    int INFORMATION_RETURN_TO_INVOKING_EXCHANGE_POSSIBLE = 0x01;
    int INFORMATION_RETURN_TO_INVOKING_EXCHANGE_CALL_ID = 0x02;
    int INFORMATION_PERFORMING_REDIRECT_INDICATOR = 0x03;
    int INFORMATION_INVOKING_REDIRECT_REASON = 0x04;

    void setReturnToInvokingExchangePossible(ReturnToInvokingExchangePossible... duration);
    ReturnToInvokingExchangePossible[] getReturnToInvokingExchangePossible();

    void setReturnToInvokingExchangeCallIdentifier(ReturnToInvokingExchangeCallIdentifier... cid);
    ReturnToInvokingExchangeCallIdentifier[] getReturnToInvokingExchangeCallIdentifier();

    void setPerformingRedirectIndicator(PerformingRedirectIndicator... reason);
    PerformingRedirectIndicator[] getPerformingRedirectIndicator();

    void setInvokingRedirectReason(InvokingRedirectReason... reason);
    InvokingRedirectReason[] getInvokingRedirectReason();
}
