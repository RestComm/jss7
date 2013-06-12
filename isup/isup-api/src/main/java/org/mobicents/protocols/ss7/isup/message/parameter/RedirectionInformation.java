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

/**
 * Start time:16:05:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:16:05:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface RedirectionInformation extends ISUPParameter {
    int _PARAMETER_CODE = 0x13;
    /**
     * See Q.763 3.45 Redirecting indicator no redirection (national use)
     */
    int _RI_NO_REDIRECTION = 0;

    /**
     * See Q.763 3.45 Redirecting indicator no redirection (national use)
     */
    int _RI_CALL_REROUTED = 1;

    /**
     * See Q.763 3.45 Redirecting indicator call rerouted, all redirection information presentation restricted (national use)
     */
    int _RI_CALL_R_RID = 2;

    /**
     * See Q.763 3.45 Redirecting indicator call diverted
     */
    int _RI_CALL_D = 3;

    /**
     * See Q.763 3.45 Redirecting indicator call diverted, all redirection information presentation restricted
     */
    int _RI_CALL_D_RIR = 4;

    /**
     * See Q.763 3.45 Redirecting indicator call rerouted, redirection number presentation restricted (national use)
     */
    int _RI_CALL_R_RNPR = 5;

    /**
     * See Q.763 3.45 Redirecting indicator call diversion, redirection number presentation restricted (national use)
     */
    int _RI_CALL_D_RNPR = 6;

    /**
     * See Q.763 3.45 Original redirection reason unknown/not available
     */
    int _ORR_UNA = 0;
    /**
     * See Q.763 3.45 Original redirection reason user busy
     */
    int _ORR_USER_BUSY = 1;

    /**
     * See Q.763 3.45 Original redirection reason no reply
     */
    int _ORR_NO_REPLY = 2;
    /**
     * See Q.763 3.45 Original redirection reason unconditional
     */
    int _ORR_UNCONDITIONAL = 3;

    /**
     * See Q.763 3.45 Redirecting reason unknown/not available
     */
    int _RR_UNA = 0;
    /**
     * See Q.763 3.45 Redirecting reason user busy
     */
    int _RR_USER_BUSY = 1;

    /**
     * See Q.763 3.45 Redirecting reason no reply
     */
    int _RR_NO_REPLY = 2;
    /**
     * See Q.763 3.45 Redirecting reason unconditional
     */
    int _RR_UNCONDITIONAL = 3;

    /**
     * See Q.763 3.45 Redirecting reason deflection during alerting
     */
    int _RR_DEFLECTION_DA = 4;

    /**
     * See Q.763 3.45 Redirecting reason deflection immediate response
     */
    int _RR_DEFLECTION_IE = 5;

    /**
     * See Q.763 3.45 Redirecting reason mobile subscriber not reachable
     */
    int _RR_MOBILE_SNR = 6;

    int getRedirectingIndicator();

    void setRedirectingIndicator(int redirectingIndicator);

    int getOriginalRedirectionReason();

    void setOriginalRedirectionReason(int originalRedirectionReason);

    int getRedirectionCounter();

    void setRedirectionCounter(int redirectionCounter) throws IllegalArgumentException;

    int getRedirectionReason();

    void setRedirectionReason(int redirectionReason);

}
