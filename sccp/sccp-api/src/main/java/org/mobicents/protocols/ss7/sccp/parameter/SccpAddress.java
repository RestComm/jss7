/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

import org.mobicents.protocols.ss7.indicator.AddressIndicator;

/**
 * @author baranowb
 *
 */
public interface SccpAddress extends Parameter {
    // calling party address
    int CGA_PARAMETER_CODE = 0x4;
    // called party address
    int CDA_PARAMETER_CODE = 0x3;

    boolean isTranslated();

    void setTranslated(boolean translated);

    AddressIndicator getAddressIndicator();

    int getSignalingPointCode();

    int getSubsystemNumber();

    GlobalTitle getGlobalTitle();
}
