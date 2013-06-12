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
 * Return cause values parameter for connectionless message
 *
 * @author baranowb
 * @author sergey vetyutnev
 */
public interface ReturnCause extends Parameter {

    int PARAMETER_CODE = 0xB;

    // int NO_TRANSLATION_FOR_NATURE = 0x0;
    // int NO_TRANSLATION_FOR_ADDRESS = 0x1;
    // int SUBSYSTEM_CONGESTION = 0x2;
    // int SUBSYSTEM_FAILURE = 0x3;
    // int UNEQUIPED_USER = 0x4;
    // int MTP_FAILURE = 0x5;
    // int NETWORK_CONGESTION = 0x6;
    // int UNQALIFIED = 0x7;
    // int ERR_IN_MSG_TRANSPORT = 0x8;
    // int ERR_IN_LOCAL_PROCESSING = 0x9;
    // int CANNOT_REASEMBLE = 0xA;
    // int SCCP_FAILURE = 0xB;
    // int HOP_COUNTER_VIOLATION = 0xC;
    // int SEG_NOT_SUPPORTED = 0xD;
    // int SEG_FAILURE = 0xE;

    /**
     * Gets the value of this parameter.
     *
     * @return the value of this parameter.
     */
    ReturnCauseValue getValue();
}
