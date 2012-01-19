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

import org.mobicents.protocols.ss7.indicator.EncodingScheme;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 *
 * @author kulikov
 */
public class GT0011  extends GlobalTitle {
    private final static GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME;
    private int tt;    
    private NumberingPlan np;
    private EncodingScheme es;
    
    private String digits;
    
    public GT0011() {
        digits = "";
    }
    
    public GT0011(int tt, NumberingPlan np, String digits) {
        this.tt = tt;
        this.np = np;
        this.digits = digits;
        this.es = digits.length() % 2 == 0 ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
    }

    public int getTranslationType() {
        return tt;
    }

    public NumberingPlan getNp() {
        return np;
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
        
        GT0011 gt1 = (GT0011)gt;
        return gt1.tt == tt && gt1.np == np && gt1.digits.equals(digits);
    }

    
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.tt;
        hash = 41 * hash + (this.np != null ? this.np.hashCode() : 0);
        hash = 41 * hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }
    
    
	// default XML representation.
	protected static final XMLFormat<GT0011> XML = new XMLFormat<GT0011>(GT0011.class) {

		public void write(GT0011 ai, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(TRANSLATION_TYPE, ai.tt);
			xml.setAttribute(ENCODING_SCHEME, ai.es.getValue());
			xml.setAttribute(NUMBERING_PLAN, ai.np.getValue());
			xml.setAttribute(DIGITS, ai.digits);
		}

		public void read(InputElement xml, GT0011 ai) throws XMLStreamException {
			ai.tt = xml.getAttribute(TRANSLATION_TYPE).toInt();
			ai.es = EncodingScheme.valueOf(xml.getAttribute(ENCODING_SCHEME).toInt());
			try {
				ai.np = NumberingPlan.valueOf(xml.getAttribute(NUMBERING_PLAN).toInt());
			} catch (IOException e) {
				throw new XMLStreamException(e);
			}
			ai.digits = xml.getAttribute(DIGITS).toString();
		}
	};    
    
}
