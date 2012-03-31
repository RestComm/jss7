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

package org.mobicents.protocols.ss7.sccp.parameter;

/**
 * Protocol class (contains class data (0-3) and "return message on error" option for connectionless classes)
 * 
 * The "protocol class" parameter field is a one-octet parameter and is structured as follows:
 * Bits 1-4 indicating protocol class are coded as follows:
 * 4321
 * 0000 class 0
 * 0001 class 1
 * 0010 class 2
 * 0011 class 3
 * 
 * @author baranowb
 * @author kulikov
 */
public interface ProtocolClass extends Parameter{

	public static final int PARAMETER_CODE = 0x05;

	public static final int HANDLING_RET_ERR = 0x08;
    /**
     * The value of protocol class.
     * 
     * @return protocol class code
     */
    public int getProtocolClass();

    /**
     * Gets a "return message on error" flag
     * 
     * @return
     */
    public boolean getReturnMessageOnError();
}
