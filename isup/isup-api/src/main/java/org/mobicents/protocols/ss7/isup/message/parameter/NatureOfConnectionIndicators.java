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
 * Start time:13:31:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:31:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface NatureOfConnectionIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x06;

	/**
	 * See Q.763 3.35 Echo control device indicator : outgoing echo control
	 * device included
	 */
	public static final boolean _ECDI_INCLUDED = true;

	/**
	 * See Q.763 3.35 Echo control device indicator : outgoing echo control
	 * device not included
	 */
	public static final boolean _ECDI_NOT_INCLUDED = false;

	/**
	 * See Q.763 3.35 Satellite indicator : no satellite circuit in the
	 * connection
	 */
	public static final int _SI_NO_SATELLITE = 0;

	/**
	 * See Q.763 3.35 Satellite indicator : one satellite circuit in the
	 * connection
	 */
	public static final int _SI_ONE_SATELLITE = 1;

	/**
	 * See Q.763 3.35 Satellite indicator : two satellite circuits in the
	 * connection
	 */
	public static final int _SI_TWO_SATELLITE = 2;

	/**
	 * See Q.763 3.35 Continuity check indicator
	 */
	public static final int _CCI_NOT_REQUIRED = 0;
	/**
	 * See Q.763 3.35 Continuity check indicator
	 */
	public static final int _CCI_REQUIRED_ON_THIS_CIRCUIT = 1;

	/**
	 * See Q.763 3.35 Continuity check indicator
	 */
	public static final int _CCI_PERFORMED_ON_PREVIOUS_CIRCUIT = 2;

	public int getSatelliteIndicator();

	public void setSatelliteIndicator(int satelliteIndicator);

	public int getContinuityCheckIndicator();

	public void setContinuityCheckIndicator(int continuityCheckIndicator);

	public boolean isEchoControlDeviceIndicator();

	public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator);
}
