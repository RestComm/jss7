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
 * Start time:13:01:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:01:51 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TerminatingNetworkRoutingNumber extends Number, ISUPParameter {
	public static final int _PARAMETER_CODE = 0;

	// FIXME: Add C defs

	/**
	 * see Q.763 3.66 c4 : subscriber number (national use)
	 */
	public static final int _NAI_SN = 1;
	/**
	 * see Q.763 3.66 c4 : unknown (national use)
	 */
	public static final int _NAI_UNKNOWN = 2;
	/**
	 * see Q.763 3.66 c4 : national (significant) number
	 */
	public static final int _NAI_NATIONAL_SN = 3;
	/**
	 * see Q.763 3.66 c4 : international number
	 */
	public static final int _NAI_IN = 4;
	/**
	 * see Q.763 3.66 c4 : network specific number
	 */
	public static final int _NAI_NETWORK_SN = 5;

	/**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
	public final static int _NPI_ISDN = 1;
	/**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
	public final static int _NPI_DATA = 3;
	/**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
	public final static int _NPI_TELEX = 4;

	public int getNumberingPlanIndicator();

	public void setNumberingPlanIndicator(int numberingPlanIndicator);

	public int getNatureOfAddressIndicator();

	public void setNatureOfAddressIndicator(int natureOfAddressIndicator);

	public int getTnrnLengthIndicator();
}
