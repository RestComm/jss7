/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.Parameter;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 */
@SuppressWarnings("serial")
public abstract class AbstractParameter implements Parameter {
    //NOTE: decode methods take ParameterFactory to allow custom EncodingScheme

    public abstract void decode(InputStream in, ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    public abstract void encode(OutputStream os, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    /**
     * Accepts only body for decoding operation, the len and tag must be processed.
     *
     * @param b
     * @throws IOException
     */
    public abstract void decode(byte[] b, ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    /**
     * Encodes only body of parameter, tag and len must be encoded.
     *
     * @return
     * @throws IOException
     */
    public abstract byte[] encode(boolean removeSPC, final SccpProtocolVersion sccpProtocolVersion) throws ParseException;

}
