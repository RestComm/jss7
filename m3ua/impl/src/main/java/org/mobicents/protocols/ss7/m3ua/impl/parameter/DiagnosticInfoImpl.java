/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class DiagnosticInfoImpl extends ParameterImpl implements DiagnosticInfo {

    private String info;

    public DiagnosticInfoImpl(String info) {
        this.info = info;
        this.tag = Parameter.Diagnostic_Information;
    }

    public DiagnosticInfoImpl(byte[] value) {
        this.tag = Parameter.Diagnostic_Information;
        this.info = new String(value);
    }

    @Override
    protected byte[] getValue() {
        return this.info.getBytes();
    }

    public String getInfo() {
        return this.info;
    }

}
