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
 *
 * @author kulikov
 */
public class GT0010  extends GlobalTitle {
    private final static GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
    /** Translation type */
    private int tt;
    /** address digits */
    private String digits;
    
    public GT0010() {
        digits = "";
    }
    
    public GT0010(int tt, String digits) {
        this.tt = tt;
        this.digits = digits;
    }

    public int getTranslationType() {
        return tt;
    }

    public String getDigits() {
        return digits;
    }

    public GlobalTitleIndicator getIndicator() {
        return gti;
    }
    
    
    public boolean equals(Object other) {
        if (!(other instanceof GlobalTitle)) {
            return false;
        }
        
        GlobalTitle gt = (GlobalTitle) other;
        if (gt.getIndicator() != gti) {
            return false;
        }
        
        GT0010 gt1 = (GT0010)gt;
        return gt1.digits.equals(digits);
    }

    
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }
    
	public String toString() {
		return "GT0010{tt=" + tt + ", digits=" + digits + "}";
	}
    
	// default XML representation.
	protected static final XMLFormat<GT0010> XML = new XMLFormat<GT0010>(GT0010.class) {

		public void write(GT0010 ai, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(TRANSLATION_TYPE, ai.tt);
			xml.setAttribute(DIGITS, ai.digits);
		}

		public void read(InputElement xml, GT0010 ai) throws XMLStreamException {
			ai.tt = xml.getAttribute(TRANSLATION_TYPE).toInt();			
			ai.digits = xml.getAttribute(DIGITS).toString();
		}
	};
    
}
