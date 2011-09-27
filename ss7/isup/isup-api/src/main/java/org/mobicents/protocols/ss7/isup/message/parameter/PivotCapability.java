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
 * Start time:13:44:52 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:44:52 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PivotCapability extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x7B;

	//FIXME: add C defs
	/**
	 * See Q.763 3.84 Pivot possible indicator : no indication
	 */
	public static final int _PPI_NO_INDICATION = 0;

	/**
	 * See Q.763 3.84 Pivot possible indicator : pivot routing possible before
	 * ACM
	 */
	public static final int _PPI_PRPB_ACM = 1;

	/**
	 * See Q.763 3.84 Pivot possible indicator : pivot routing possible before
	 * ANM
	 */
	public static final int _PPI_PRPB_ANM = 2;

	/**
	 * See Q.763 3.84 Pivot possible indicator : pivot routing possible any time
	 * during the call
	 */
	public static final int _PPI_PRPB_ANY = 3;

	/**
	 * See Q.763 3.84 Interworking to redirection indicator (national use)
	 */
	public static final boolean _ITRI_ALLOWED = false;

	/**
	 * See Q.763 3.84 Interworking to redirection indicator (national use)
	 */
	public static final boolean _ITRI_NOT_ALLOWED = true;
	
	
	public byte[] getPivotCapabilities();

	public void setPivotCapabilities(byte[] pivotCapabilities);

	public byte createPivotCapabilityByte(boolean itriNotAllowed, int pivotPossibility);

	public boolean getITRINotAllowed(byte b);

	public int getPivotCapability(byte b);
}
