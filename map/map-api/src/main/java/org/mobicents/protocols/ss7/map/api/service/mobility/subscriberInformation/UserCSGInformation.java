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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
*
UserCSGInformation ::= SEQUENCE {
	csg-Id	 	[0] CSG-Id,
	extensionContainer	[1] ExtensionContainer		OPTIONAL,
	...,
	accessMode	[2] OCTET STRING (SIZE(1))		OPTIONAL,
	cmi			[3] OCTET STRING (SIZE(1))		OPTIONAL }
-- The encoding of the accessMode and cmi parameters are as defined in 3GPP TS 29.060 [105].

* 
* @author sergey vetyutnev
* 
*/
public interface UserCSGInformation {

	public CSGId getCSGId();

	public MAPExtensionContainer getExtensionContainer();

	public byte[] getAccessMode();

	public byte[] getCmi();

}
