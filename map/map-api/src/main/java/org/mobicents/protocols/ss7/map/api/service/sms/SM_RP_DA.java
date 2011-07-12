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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;

/**
 * SM-RP-DA ::= CHOICE {
	imsi			[0] IMSI,
	lmsi			[1] LMSI,
	serviceCentreAddressDA	[4] AddressString,
	noSM-RP-DA	[5] NULL}

 * Only one method getIMSI(), getLMSI() or getServiceCentreAddressDA() 
 * will return the non null value
 * If all these methods return null - this means noSM-RP-DA value 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SM_RP_DA extends MAPPrimitive {

	public IMSI getIMSI();

	public LMSI getLMSI();

	public AddressString getServiceCentreAddressDA();

}
