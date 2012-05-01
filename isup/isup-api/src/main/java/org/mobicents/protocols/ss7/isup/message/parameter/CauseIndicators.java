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
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:11:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CauseIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x12;

	
	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_ITUT = 0;

	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_IOS_IEC = 1;

	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_NATIONAL = 2;
	/**
	 * See Q.850
	 */
	public static final int _CODING_STANDARD_SPECIFIC = 3;
	

	/**
	 * See Q.850
	 */
	public static final int _LOCATION_USER = 0;

	/**
	 * See Q.850 private network serving the local user (LPN)
	 */
	public static final int _LOCATION_PRIVATE_NSLU = 1;

	/**
	 * See Q.850 public network serving the local user (LN)
	 */
	public static final int _LOCATION_PUBLIC_NSLU = 2;

	/**
	 * See Q.850 transit network (TN)
	 */
	public static final int _LOCATION_TRANSIT_NETWORK = 3;

	/**
	 * See Q.850 private network serving the remote user (RPN)
	 */
	public static final int _LOCATION_PRIVATE_NSRU = 5;

	/**
	 * See Q.850 public network serving the remote user (RLN)
	 */
	public static final int _LOCATION_PUBLIC_NSRU = 4;
	/**
	 * See Q.850
	 */
	public static final int _LOCATION_INTERNATIONAL_NETWORK = 7;

	/**
	 * See Q.850 network beyond interworking point (BI)
	 */
	public static final int _LOCATION_NETWORK_BEYOND_IP = 10;

	
	
	public int getCodingStandard();

	public void setCodingStandard(int codingStandard);

	public int getLocation();

	public void setLocation(int location);

	public int getCauseValue();

	public void setCauseValue(int causeValue);

	public byte[] getDiagnostics();

	public void setDiagnostics(byte[] diagnostics);

}
