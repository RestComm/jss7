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
 * Start time:14:18:57 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:18:57 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface UIDCapabilityIndicators extends ISUPParameter {

	public static final int _PARAMETER_CODE = 0x75;

	//FIXME: add C defs
	/**
	 * See Q.763 3.79 Through-connection instruction indicator : no indication
	 */
	public static final boolean _TCI_NO_INDICATION = false;

	/**
	 * See Q.763 3.79 Through-connection instruction indicator :
	 * through-connection modification possible
	 */
	public static final boolean _TCI_TCMP = true;

	/**
	 * See Q.763 3.79 T9 timer indicator : no indication
	 */
	public static final boolean _T9_TII_NO_INDICATION = false;

	/**
	 * See Q.763 3.79 T9 timer indicator : stopping of T9 timer possible
	 */
	public static final boolean _T9_TI_SOT9P = false;

	
	public byte[] getUIDCapabilityIndicators();

	public void setUIDCapabilityIndicators(byte[] uidCapabilityIndicators);

	public byte createUIDAction(boolean TCI, boolean T9);

	public boolean getT9Indicator(byte b);

	public boolean getTCIndicator(byte b);
}
