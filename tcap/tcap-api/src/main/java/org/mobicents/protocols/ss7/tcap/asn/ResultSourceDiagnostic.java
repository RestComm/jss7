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

package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface ResultSourceDiagnostic extends Encodable {

	//Annoying... TL[CHOICE[TL[TLV]]
	
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG = 0x03;
	
	
	//membersL CHOICE
	public static final int _TAG_U_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_U_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG_U = 0x01;
	
	public static final int _TAG_P_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_P_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG_P = 0x02;
	
	public void setDialogServiceProviderType(DialogServiceProviderType t);
	public DialogServiceProviderType getDialogServiceProviderType();
	
	public void setDialogServiceUserType(DialogServiceUserType t);
	public DialogServiceUserType getDialogServiceUserType();
	
}
