/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;


/**
 * Additional-Number ::= CHOICE {
 *		msc-Number [0] ISDN-AddressString,
 *		sgsn-Number [1] ISDN-AddressString}
 *		-- additional-number can be either msc-number or sgsn-number
 *		-- if received networkNode-number is msc-number then the
 *		-- additional number is sgsn-number
 * 		-- if received networkNode-number is sgsn-number then the
 * 		-- additional number is msc-number
 * 
 * @author amit bhayani
 *
 */
public interface AdditionalNumber extends MAPPrimitive {
	
	public ISDNAddressString getMSCNumber();
	public ISDNAddressString getSGSNNumber();
	
}
