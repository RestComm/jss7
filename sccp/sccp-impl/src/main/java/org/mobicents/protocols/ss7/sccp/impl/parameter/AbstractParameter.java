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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.parameter.Parameter;

/**
 *
 * @author baranowb
 */
public abstract class AbstractParameter implements Parameter { // implement
                                                               // marker
                                                               // interface.

    public abstract void decode(InputStream in) throws IOException;

    public abstract void encode(OutputStream os) throws IOException;

    /**
     * Accepts only body for decoding operation, that is leng and tag must be processed.
     *
     * @param b
     * @throws IOException
     */
    public abstract void decode(byte[] b) throws IOException;

    /**
     * Encodes only body of parameter, tag and len must be encoded.
     *
     * @return
     * @throws IOException
     */
    public abstract byte[] encode() throws IOException;
}
