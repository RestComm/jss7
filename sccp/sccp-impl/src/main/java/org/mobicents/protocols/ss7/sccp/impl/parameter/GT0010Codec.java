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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class GT0010Codec extends GTCodec {

	private GT0010 gt;

	public GT0010Codec() {
	}

	public GT0010Codec(GT0010 gt) {
		this.gt = gt;
	}

	public GlobalTitle decode(InputStream in) throws IOException {
		int b = in.read() & 0xff;
		int tt = b;

		String digits = "";
		while (in.available() > 0) {
			b = in.read() & 0xff;
			digits += Integer.toHexString(b & 0x0f) + Integer.toHexString((b & 0xf0) >> 4);
		}

		return new GT0010(tt, digits);
	}

	public void encode(OutputStream in) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
