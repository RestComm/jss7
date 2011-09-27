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
 * Start time:13:36:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:36:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface OptionalBackwardCallIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x29;

	/**
	 * See Q.763 3.37 In-band information indicator
	 */
	public final static boolean _IBII_NO_INDICATION = false;
	/**
	 * See Q.763 3.37 In-band information indicator
	 */
	public final static boolean _IBII_AVAILABLE = true;

	/**
	 * See Q.763 3.37 Call diversion may occur indicator
	 */
	public final static boolean _CDI_NO_INDICATION = false;

	/**
	 * See Q.763 3.37 Call diversion may occur indicator
	 */
	public final static boolean _CDI_MAY_OCCUR = true;

	/**
	 * See Q.763 3.37 Simple segmentation indicator
	 */
	public final static boolean _SSIR_NO_ADDITIONAL_INFO = false;

	/**
	 * See Q.763 3.37 Simple segmentation indicator
	 */
	public final static boolean _SSIR_ADDITIONAL_INFO = true;

	/**
	 * See Q.763 3.37 MLPP user indicator
	 */
	public final static boolean _MLLPUI_NO_INDICATION = false;

	/**
	 * See Q.763 3.37 MLPP user indicator
	 */
	public final static boolean _MLLPUI_USER = true;
	
	
	public boolean isInbandInformationIndicator();

	public void setInbandInformationIndicator(boolean inbandInformationIndicator);

	public boolean isCallDiversionMayOccurIndicator();

	public void setCallDiversionMayOccurIndicator(boolean callDiversionMayOccurIndicator);

	public boolean isSimpleSegmentationIndicator();

	public void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator);

	public boolean isMllpUserIndicator();

	public void setMllpUserIndicator(boolean mllpUserIndicator);
}
