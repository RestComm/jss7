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
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class GTCodec {
	private GTCodec getCodec(GlobalTitle gt) {
		switch (gt.getIndicator()) {
		case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
			return new GT0001Codec((GT0001) gt);
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
			return new GT0010Codec((GT0010) gt);
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
			return new GT0011Codec((GT0011) gt);
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
			return new GT0100Codec((GT0100) gt);
		default:
			return null;
		}
	}

	public void encode(GlobalTitle gt, OutputStream out) throws IOException {
		GTCodec codec = this.getCodec(gt);
		codec.encode(out);
	}

	public GlobalTitle decode(GlobalTitleIndicator gti, InputStream in) throws IOException {
		GTCodec codec = null;
		switch (gti) {
		case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
			codec = new GT0001Codec();
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
			codec = new GT0010Codec();
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
			codec = new GT0011Codec();
			break;
		case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
			codec = new GT0100Codec();
			break;
		case NO_GLOBAL_TITLE_INCLUDED:
			return null;
		}
		return codec.decode(in);
	}

	protected void encode(OutputStream out) throws IOException {
	}

	protected GlobalTitle decode(InputStream in) throws IOException {
		return null;
	}
}
