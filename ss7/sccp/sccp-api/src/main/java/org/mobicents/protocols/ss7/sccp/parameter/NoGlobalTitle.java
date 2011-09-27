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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;

/**
 * @author amit bhayani
 * 
 */
public class NoGlobalTitle extends GlobalTitle {

	private String digits;
	
	public NoGlobalTitle(){
		
	}

	public NoGlobalTitle(String digits) {
		this.digits = digits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle#getIndicator()
	 */
	@Override
	public GlobalTitleIndicator getIndicator() {
		return GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle#getDigits()
	 */
	@Override
	public String getDigits() {
		return this.digits;
	}

	// default XML representation.
	protected static final XMLFormat<NoGlobalTitle> XML = new XMLFormat<NoGlobalTitle>(NoGlobalTitle.class) {

		public void write(NoGlobalTitle ai, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(DIGITS, ai.digits);
		}

		public void read(InputElement xml, NoGlobalTitle ai) throws XMLStreamException {
			ai.digits = xml.getAttribute(DIGITS).toString();
		}
	};
}
