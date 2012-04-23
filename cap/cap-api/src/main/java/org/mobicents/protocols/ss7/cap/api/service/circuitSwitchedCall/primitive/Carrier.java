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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

/**
*
ISUP Carrier wrapper

Carrier {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( 
 bound.&minCarrierLength .. bound.&maxCarrierLength)) 
-- This parameter is used for North America (na) only. 
-- It contains the carrier selection field (first octet) followed by Carrier ID 
-- information (North America (na)). 
 
-- The Carrier selection is one octet and is encoded as: 
-- 00000000  No indication 
-- 00000001  Selected carrier identification code (CIC) pre subscribed and not 
--    input by calling party 
-- 00000010  Selected carrier identification code (CIC) pre subscribed and input by 
--    calling party 
-- 00000011  Selected carrier identification code (CIC) pre subscribed, no 
--    indication of whether input by calling party (undetermined) 
-- 00000100  Selected carrier identification code (CIC) not pre subscribed and 
--    input by calling party 
-- 00000101 
-- to   Spare 
-- 11111110 
-- 11111111  Reserved 
 
-- Refer to ANSI T1.113-1995 [92] for encoding of na carrier ID information (3 octets). 

minCarrierLength ::= 4
maxCarrierLength ::= 4

* 
* @author sergey vetyutnev
* 
*/
public interface Carrier {

	public byte[] getData();

}
