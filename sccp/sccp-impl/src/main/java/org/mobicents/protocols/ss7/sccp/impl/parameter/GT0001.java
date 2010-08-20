/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class GT0001 implements GlobalTitle {
    private int nai;
    private String digits;
    
    public GT0001(int nai, String digits) {
        this.nai = nai;
        this.digits = digits;
    }

    public void decode(InputStream in) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void encode(OutputStream in) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getTranslationType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTranslationType(int translationType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNumberingPlan() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNumberingPlan(int numberingPlan) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEncodingScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEncodingScheme(int encodingScheme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNatureOfAddress() {
        return nai;
    }

    public void setNatureOfAddress(int natureOfAddress) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }
}
