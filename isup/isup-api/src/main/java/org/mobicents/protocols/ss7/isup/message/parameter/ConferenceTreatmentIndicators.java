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
 * Start time:12:28:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:28:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ConferenceTreatmentIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x72;

	
	/**
	 * See Q.763 3.76 Conference acceptance indicator (Note) : no indication
	 */
	public static final int _CAI_NO_INDICATION = 0;

	/**
	 * See Q.763 3.76 Conference acceptance indicator (Note) : accept conference
	 * request
	 */
	public static final int _CAI_ACR = 1;

	/**
	 * See Q.763 3.76 Conference acceptance indicator (Note) : reject conference
	 * request
	 */
	public static final int _CAI_RCR = 2;
	
	public byte[] getConferenceAcceptance();

	public void setConferenceAcceptance(byte[] conferenceAcceptance);

	public int getConferenceTreatmentIndicator(byte b);
}
