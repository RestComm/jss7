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
 * Start time:13:29:53 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:29:53 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface MLPPPrecedence extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x3A;
	
	/**
	 * See Q.763 3.34 LFB (Look ahead for busy) : LFB allowed
	 */
	public static final int _LFB_INDICATOR_ALLOWED = 0;
	/**
	 * See Q.763 3.34 LFB (Look ahead for busy) : path reserved (national use)
	 */
	public static final int _LFB_INDICATOR_PATH_RESERVED = 1;
	/**
	 * See Q.763 3.34 LFB (Look ahead for busy) : LFB not allowed
	 */
	public static final int _LFB_INDICATOR_NOT_ALLOWED = 2;

	/**
	 * See Q.763 3.34 Precedence level : flash override
	 */
	public static final int _PLI_FLASH_OVERRIDE = 0;

	/**
	 * See Q.763 3.34 Precedence level : flash
	 */
	public static final int _PLI_FLASH = 1;
	/**
	 * See Q.763 3.34 Precedence level : immediate
	 */
	public static final int _PLI_IMMEDIATE = 2;
	/**
	 * See Q.763 3.34 Precedence level : priority
	 */
	public static final int _PLI_PRIORITY = 3;

	/**
	 * See Q.763 3.34 Precedence level : routine
	 */
	public static final int _PLI_ROUTINE = 4;
	
	public byte getLfb();

	public void setLfb(byte lfb);

	public byte getPrecedenceLevel() ;

	public void setPrecedenceLevel(byte precedenceLevel) ;

	public int getMllpServiceDomain() ;

	public void setMllpServiceDomain(int mllpServiceDomain) ;

	public byte[] getNiDigits();

	public void setNiDigits(byte[] niDigits) ;
}
