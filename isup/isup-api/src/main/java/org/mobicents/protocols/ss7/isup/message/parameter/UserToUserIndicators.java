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
 * Start time:14:23:10 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:23:10 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface UserToUserIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x2A;

	//FIXME: Add C defs
	
	/**
	 * Service 1,2,3 request : no info
	 */
	public static final int _REQ_Sx_NO_INFO = 0;
	/**
	 * Service 1,2,3 request : not essential
	 */
	public static final int _REQ_Sx_RNE = 2;
	/**
	 * Service 1,2,3 request : essential
	 */
	public static final int _REQ_Sx_RE = 3;

	/**
	 * Service 1,2,3 request : no info
	 */
	public static final int _RESP_Sx_NO_INFO = 0;
	/**
	 * Service 1,2,3 request : not provided
	 */
	public static final int _RESP_Sx_NOT_PROVIDED = 1;

	/**
	 * Service 1,2,3 request : provided
	 */
	public static final int _RESP_Sx_PROVIDED = 2;

	/**
	 * See Q.763 3.60 Network discard indicator : no information
	 */
	public static final boolean _NDI_NO_INFO = false;

	/**
	 * See Q.763 3.60 Network discard indicator : user-to-user information
	 * discarded by the network
	 */
	public static final boolean _NDI_UTUIDBTN = true;
	
	public boolean isResponse();

	public void setResponse(boolean response);

	public int getServiceOne();

	public void setServiceOne(int serviceOne);

	public int getServiceTwo();

	public void setServiceTwo(int serviceTwo);

	public int getServiceThree();

	public void setServiceThree(int serviceThree);

	public boolean isNetworkDiscardIndicator();

	public void setNetworkDiscardIndicator(boolean networkDiscardIndicator);

}
