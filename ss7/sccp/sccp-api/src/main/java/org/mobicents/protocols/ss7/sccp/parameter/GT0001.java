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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class GT0001 extends GlobalTitle {
	private static final GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
	private NatureOfAddress nai;
	private String digits;
	private boolean odd = false;

	public GT0001() {
		digits = "";
	}

	public GT0001(NatureOfAddress nai, String digits) {
		this.nai = nai;
		this.digits = digits;
	}

	public NatureOfAddress getNoA() {
		return this.nai;
	}

	public String getDigits() {
		return digits;
	}

	public GlobalTitleIndicator getIndicator() {
		return this.gti;
	}

	public boolean equals(Object other) {
		if (!(other instanceof GlobalTitle)) {
			return false;
		}

		GlobalTitle gt = (GlobalTitle) other;
		if (gt.getIndicator() != gti) {
			return false;
		}

		GT0001 gt1 = (GT0001) gt;
		return gt1.nai == nai && gt1.digits.equals(digits);
	}

	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (this.gti != null ? this.gti.hashCode() : 0);
		hash = 53 * hash + (this.digits != null ? this.digits.hashCode() : 0);
		return hash;
	}

	// default XML representation.
	protected static final XMLFormat<GT0001> XML = new XMLFormat<GT0001>(GT0001.class) {

		public void write(GT0001 ai, OutputElement xml) throws XMLStreamException {
			//xml.setAttribute(GLOBALTITLE_INDICATOR, ai.gti.getValue());
			xml.setAttribute(NATURE_OF_ADDRESS_INDICATOR, ai.nai.getValue());
			xml.setAttribute(DIGITS, ai.digits);
		}

		public void read(InputElement xml, GT0001 ai) throws XMLStreamException {
			//ai.gti = GlobalTitleIndicator.valueOf(xml.getAttribute(GLOBALTITLE_INDICATOR).toInt());
			ai.nai = NatureOfAddress.valueOf(xml.getAttribute(NATURE_OF_ADDRESS_INDICATOR).toInt());
			ai.digits = xml.getAttribute(DIGITS).toString();
		}
	};
}
