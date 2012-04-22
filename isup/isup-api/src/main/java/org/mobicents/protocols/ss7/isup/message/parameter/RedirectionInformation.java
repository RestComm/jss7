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
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface RedirectionInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x13;
	/**
	 * See Q.763 3.45 Redirecting indicator no redirection (national use)
	 */
	public static final int _RI_NO_REDIRECTION = 0;

	/**
	 * See Q.763 3.45 Redirecting indicator no redirection (national use)
	 */
	public static final int _RI_CALL_REROUTED = 1;

	/**
	 * See Q.763 3.45 Redirecting indicator call rerouted, all redirection
	 * information presentation restricted (national use)
	 */
	public static final int _RI_CALL_R_RID = 2;

	/**
	 * See Q.763 3.45 Redirecting indicator call diverted
	 */
	public static final int _RI_CALL_D = 3;

	/**
	 * See Q.763 3.45 Redirecting indicator call diverted, all redirection
	 * information presentation restricted
	 */
	public static final int _RI_CALL_D_RIR = 4;

	/**
	 * See Q.763 3.45 Redirecting indicator call rerouted, redirection number
	 * presentation restricted (national use)
	 */
	public static final int _RI_CALL_R_RNPR = 5;

	/**
	 * See Q.763 3.45 Redirecting indicator call diversion, redirection number
	 * presentation restricted (national use)
	 */
	public static final int _RI_CALL_D_RNPR = 6;

	/**
	 * See Q.763 3.45 Original redirection reason unknown/not available
	 */
	public static final int _ORR_UNA = 0;
	/**
	 * See Q.763 3.45 Original redirection reason user busy
	 */
	public static final int _ORR_USER_BUSY = 1;

	/**
	 * See Q.763 3.45 Original redirection reason no reply
	 */
	public static final int _ORR_NO_REPLY = 2;
	/**
	 * See Q.763 3.45 Original redirection reason unconditional
	 */
	public static final int _ORR_UNCONDITIONAL = 3;

	/**
	 * See Q.763 3.45 Redirecting reason unknown/not available
	 */
	public static final int _RR_UNA = 0;
	/**
	 * See Q.763 3.45 Redirecting reason user busy
	 */
	public static final int _RR_USER_BUSY = 1;

	/**
	 * See Q.763 3.45 Redirecting reason no reply
	 */
	public static final int _RR_NO_REPLY = 2;
	/**
	 * See Q.763 3.45 Redirecting reason unconditional
	 */
	public static final int _RR_UNCONDITIONAL = 3;

	/**
	 * See Q.763 3.45 Redirecting reason deflection during alerting
	 */
	public static final int _RR_DEFLECTION_DA = 4;

	/**
	 * See Q.763 3.45 Redirecting reason deflection immediate response
	 */
	public static final int _RR_DEFLECTION_IE = 5;

	/**
	 * See Q.763 3.45 Redirecting reason mobile subscriber not reachable
	 */
	public static final int _RR_MOBILE_SNR = 6;
	
	
	public int getRedirectingIndicator() ;

	public void setRedirectingIndicator(int redirectingIndicator) ;

	public int getOriginalRedirectionReason() ;

	public void setOriginalRedirectionReason(int originalRedirectionReason) ;

	public int getRedirectionCounter() ;

	public void setRedirectionCounter(int redirectionCounter) throws IllegalArgumentException ;

	public int getRedirectionReason() ;

	public void setRedirectionReason(int redirectionReason) ;
	
}
