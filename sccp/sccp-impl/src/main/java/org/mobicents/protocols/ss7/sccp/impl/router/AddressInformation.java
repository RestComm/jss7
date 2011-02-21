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

package org.mobicents.protocols.ss7.sccp.impl.router;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 * Defines information required for compare and constrcut SCCP address.
 * 
 * @author kulikov
 */
public class AddressInformation implements XMLSerializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4270636264251165348L;

    private static final String TRANSLATION_TYPE = "tt";
    private static final String NUMBERING_PLAN = "np";
    private static final String NATURE_OF_ADDRESS = "noa";
    private static final String DIGITS = "digits";
    private static final String SUBSYSTEM_NUMBER = "ssn";

    private final static String SEPARATOR = "#";
    private final static String NULL = " ";

    /** translation type */
    private int tt;
    /** numbering plan */
    private NumberingPlan np;
    /** nature of address */
    private NatureOfAddress noa;
    /** digits */
    private String digits;
    /** subsytem number */
    private int ssn;

    public AddressInformation() {

    }

    /**
     * Creates new address information object.
     * 
     * @param tt
     *            translation type
     * @param np
     *            numbering plan
     * @param noa
     *            nature of address
     * @param digits
     *            global title mask
     * @param ssn
     *            subsytem number.
     */
    public AddressInformation(int tt, NumberingPlan np, NatureOfAddress noa,
            String digits, int ssn) {
        this.tt = tt;
        this.np = np;
        this.noa = noa;
        this.digits = digits;
        this.ssn = ssn;
    }

    /**
     * Gets the translation type.
     * 
     * @return translation type indcator.
     */
    public int getTranslationType() {
        return tt;
    }

    /**
     * Gets the numbering plan indicator.
     * 
     * @return numbering plan
     */
    public NumberingPlan getNumberingPlan() {
        return np;
    }

    /**
     * Gets the nature of address indicator.
     * 
     * @return the nature of address
     */
    public NatureOfAddress getNatureOfAddress() {
        return noa;
    }

    /**
     * Global title mask
     * 
     * @return the global title mask
     */
    public String getDigits() {
        return digits;
    }

    /**
     * Gets the subsystem number
     * 
     * @return the subsystem number
     */
    public int getSubsystem() {
        return ssn;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        if (tt != -1) {
            buff.append(tt);
        } else {
            buff.append(NULL);
        }

        buff.append(SEPARATOR);

        if (np != null) {
            buff.append(np.toString());
        } else {
            buff.append(NULL);
        }
        buff.append(SEPARATOR);

        if (noa != null) {
            buff.append(noa.toString());
        } else {
            buff.append(NULL);
        }
        buff.append(SEPARATOR);

        if (digits != null) {
            buff.append(digits);
        } else {
            buff.append(NULL);
        }
        buff.append(SEPARATOR);

        if (ssn != -1) {
            buff.append(ssn);
        } else {
            buff.append(NULL);
        }

        return buff.toString();
    }

    // default XML representation.
    protected static final XMLFormat<AddressInformation> XML = new XMLFormat<AddressInformation>(
            AddressInformation.class) {

        public void write(AddressInformation ai, OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(TRANSLATION_TYPE, ai.tt);
            xml.setAttribute(NUMBERING_PLAN, ai.np == null ? -1 : ai.np
                    .getValue());
            xml.setAttribute(NATURE_OF_ADDRESS, ai.noa == null ? -1 : ai.noa
                    .getValue());
            xml.setAttribute(DIGITS, ai.digits);
            xml.setAttribute(SUBSYSTEM_NUMBER, ai.ssn);
        }

        public void read(InputElement xml, AddressInformation ai)
                throws XMLStreamException {
            ai.tt = xml.getAttribute(TRANSLATION_TYPE).toInt();
            ai.np = NumberingPlan.valueOf(xml.getAttribute(NUMBERING_PLAN)
                    .toInt());
            ai.noa = NatureOfAddress.valueOf(xml
                    .getAttribute(NATURE_OF_ADDRESS).toInt());
            ai.digits = xml.getAttribute(DIGITS).toString();
            ai.ssn = xml.getAttribute(SUBSYSTEM_NUMBER).toInt();
        }
    };
}
