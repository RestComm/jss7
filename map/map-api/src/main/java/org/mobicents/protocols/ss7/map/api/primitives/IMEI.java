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

package org.mobicents.protocols.ss7.map.api.primitives;


/**
 * 
 * IMEI ::= TBCD-STRING (SIZE (8))
 *	--	Refers to International Mobile Station Equipment Identity
 *	--	and Software Version Number (SVN) defined in TS 3GPP TS 23.003 [17].
 *	--	If the SVN is not present the last octet shall contain the
 *	--	digit 0 and a filler.
 *	--	If present the SVN shall be included in the last octet.
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public interface IMEI extends MAPPrimitive {

	public String getIMEI();
	
}


