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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.mobicents.protocols.ss7.sccp.parameter.EncodingScheme;
import org.mobicents.protocols.ss7.sccp.parameter.EncodingSchemeType;

/**
 * @author baranowb
 *
 */
public class BCDEvenEncodingScheme extends DefaultEncodingScheme {

    public static final EncodingScheme INSTANCE = new BCDEvenEncodingScheme();
    public static final int SCHEMA_CODE = 2;
    public BCDEvenEncodingScheme() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean isOdd() {
        return false;
    }

    @Override
    public EncodingSchemeType getType() {
        return EncodingSchemeType.BCD_EVEN;
    }

    @Override
    public byte getSchemeCode() {
        return SCHEMA_CODE;
    }

}
