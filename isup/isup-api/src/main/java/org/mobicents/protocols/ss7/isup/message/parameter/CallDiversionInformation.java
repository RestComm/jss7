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
 * Start time:11:06:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:06:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallDiversionInformation extends ISUPParameter {

    int _PARAMETER_CODE = 0x36;

    /**
     * see Q.763 3.6 Notification subscription options Unknown
     */
    int _NSO_UNKNOWN = 0;

    /**
     * see Q.763 3.6 Notification subscription options presentation not allowed
     */
    int _NSO_P_NOT_ALLOWED = 1;

    /**
     * see Q.763 3.6 Notification subscription options presentation allowed with redirection number
     */
    int _NSO_P_A_WITH_RN = 2;

    /**
     * see Q.763 3.6 Notification subscription options presentation allowed without redirection number
     */
    int _NSO_P_A_WITHOUT_RN = 3;

    /**
     * see Q.763 3.6 Notification subscription options Unknown
     */
    int _REDIRECTING_REASON_UNKNOWN = 0;

    /**
     * see Q.763 3.6 Redirecting reason User busy
     */
    int _REDIRECTING_REASON_USER_BUSY = 1;

    /**
     * see Q.763 3.6 Redirecting reason no reply
     */
    int _REDIRECTING_REASON_NO_REPLY = 2;

    /**
     * see Q.763 3.6 Redirecting reason unconditional
     */
    int _REDIRECTING_REASON_UNCONDITIONAL = 3;

    /**
     * see Q.763 3.6 Redirecting reason deflection during alerting
     */
    int _REDIRECTING_REASON_DDA = 4;
    /**
     * see Q.763 3.6 Redirecting reason deflection immediate response
     */
    int _REDIRECTING_REASON_DIR = 5;

    /**
     * see Q.763 3.6 Redirecting reason mobile subscriber not reachable
     */
    int _REDIRECTING_REASON_MSNR = 6;

    int getNotificationSubscriptionOptions();

    void setNotificationSubscriptionOptions(int notificationSubscriptionOptions);

    int getRedirectingReason();

    void setRedirectingReason(int redirectingReason);
}
