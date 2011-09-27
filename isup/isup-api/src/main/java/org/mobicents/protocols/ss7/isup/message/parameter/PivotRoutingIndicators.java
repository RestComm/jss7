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
 * Start time:13:49:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:49:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface PivotRoutingIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x7C;
	
	//FIXME: add C defs
	
	/**
	 * See Q.763 3.85 Pivot routing indicators : no indication
	 */
	public static final int _PRI_NO_INDICATION = 0;
	/**
	 * See Q.763 3.85 Pivot routing indicators : pivote request
	 */
	public static final int _PRI_PIVOT_REQUEST = 1;
	/**
	 * See Q.763 3.85 Pivot routing indicators : cancel pivot request
	 */
	public static final int _PRI_C_PR = 2;
	/**
	 * See Q.763 3.85 Pivot routing indicators : pivot request failure
	 */
	public static final int _PRI_PRF = 3;
	/**
	 * See Q.763 3.85 Pivot routing indicators : interworking to redirection
	 * prohibited (backward) (national use)
	 */
	public static final int _PRI_ITRP = 4;
	
	
	public byte[] getPivotRoutingIndicators() ;

	public void setPivotRoutingIndicators(byte[] pivotRoutingIndicators) ;
}
