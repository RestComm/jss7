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

package org.mobicents.protocols.ss7.map.api;

import java.util.EnumSet;


/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public enum MAPApplicationContextName {
	/**
	 * Look at http://www.oid-info.com/get/0.4.0.0.1.0.19.2
	 */

	// -- Mobility Services
	networkLocUpContext(1),
	equipmentMngtContext(13),
	infoRetrievalContext(14),
	anyTimeEnquiryContext(29),

	// -- USSD
	networkUnstructuredSsContext(19),
	
	// -- SMS
	shortMsgAlertContext(23), shortMsgMORelayContext(21), shortMsgMTRelayContext(25), shortMsgMTVgcsRelayContext(41), 
	shortMsgGatewayContext(20), mwdMngtContext(24),

	// -- Location Service (lms)
	locationSvcEnquiryContext(38),
	locationSvcGatewayContext(37),
	
	// -- Call Handling Services
	locationInfoRetrievalContext(5);
	
	
	private int code;

	private MAPApplicationContextName(int code) {
		this.code = code;
	}

	public int getApplicationContextCode() {
		return this.code;
	}

	public static MAPApplicationContextName getInstance(Long code) {
		
		EnumSet<MAPApplicationContextName> lst = EnumSet.allOf(MAPApplicationContextName.class);
		for(MAPApplicationContextName el : lst) {
			if(el.code==code)
				return el;
		}
		
		return null;
	}

}
