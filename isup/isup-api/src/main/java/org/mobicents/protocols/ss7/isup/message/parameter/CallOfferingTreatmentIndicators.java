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
 * Start time:11:59:38 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:59:38 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallOfferingTreatmentIndicators extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x70;

	/**
	 * See Q.763 3.74 Call to be offered indicator : no indication
	 */
	public static final int _CTBOI_NO_INDICATION = 0;

	/**
	 * See Q.763 3.74 Call to be offered indicator : call offering not allowed
	 */
	public static final int _CTBOI_CONA = 1;

	/**
	 * See Q.763 3.74 Call to be offered indicator : call offering allowed
	 */
	public static final int _CTBOI_COA = 2;

	public byte[] getCallOfferingTreatmentIndicators();

	public void setCallOfferingTreatmentIndicators(byte[] callOfferingTreatmentIndicators);

	public int getCallOfferingIndicator(byte b);

}
