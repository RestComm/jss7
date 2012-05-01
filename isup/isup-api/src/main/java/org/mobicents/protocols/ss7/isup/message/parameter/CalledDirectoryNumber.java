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
 * Start time:11:12:56 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:12:56 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CalledDirectoryNumber extends ISUPParameter, NAINumber {
	// FIXME: whats the code ?
	public static final int _PARAMETER_CODE = 0x7D;

	
	/**
	 * See Q.763 Numbering plan indicator : ISDN (Telephony) numbering plan
	 * (ITU-T Recommendation E.164)
	 */
	public static final int _NPI_ISDN_NP = 1;

	/**
	 * See Q.763 Internal network number indicator (INN) : reserved
	 */
	public static final int _INNI_RESERVED = 0;

	/**
	 * See Q.763 Internal network number indicator (INN) : routing to internal
	 * network number not allowed
	 */
	public static final int _INNI_RTINNNA = 1;
	
	public int getNumberingPlanIndicator();

	public void setNumberingPlanIndicator(int numberingPlanIndicator);

	public int getInternalNetworkNumberIndicator();

	public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator);
}
