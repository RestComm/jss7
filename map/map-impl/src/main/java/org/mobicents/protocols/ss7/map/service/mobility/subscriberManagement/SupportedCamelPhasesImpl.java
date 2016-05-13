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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SupportedCamelPhasesImpl extends BitStringBase implements SupportedCamelPhases {

    private static final int _INDEX_Phase1 = 0;
    private static final int _INDEX_Phase2 = 1;
    private static final int _INDEX_Phase3 = 2;
    private static final int _INDEX_Phase4 = 3;

    private static final String PHASE1 = "phase1";
    private static final String PHASE2 = "phase2";
    private static final String PHASE3 = "phase3";
    private static final String PHASE4 = "phase4";

    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    public SupportedCamelPhasesImpl() {
        super(1, 16, 4, "SupportedCamelPhases");
    }

    public SupportedCamelPhasesImpl(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        super(1, 16, 4, "SupportedCamelPhases");

        this.setData(phase1, phase2, phase3, phase4);
    }

    protected void setData(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        if (phase1)
            this.bitString.set(_INDEX_Phase1);
        if (phase2)
            this.bitString.set(_INDEX_Phase2);
        if (phase3)
            this.bitString.set(_INDEX_Phase3);
        if (phase4)
            this.bitString.set(_INDEX_Phase4);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase1Supported()
     */
    public boolean getPhase1Supported() {
        return this.bitString.get(_INDEX_Phase1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase2Supported()
     */
    public boolean getPhase2Supported() {
        return this.bitString.get(_INDEX_Phase2);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase3Supported()
     */
    public boolean getPhase3Supported() {
        return this.bitString.get(_INDEX_Phase3);
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<SupportedCamelPhasesImpl> SUPPORTED_CAMEL_PHASES_XML = new XMLFormat<SupportedCamelPhasesImpl>(SupportedCamelPhasesImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, SupportedCamelPhasesImpl supportedCamelPhases) throws XMLStreamException {

            boolean phase1 = false;
            boolean phase2 = false;
            boolean phase3 = false;
            boolean phase4 = false;
            Boolean bval = xml.getAttribute(PHASE1, DEFAULT_BOOLEAN_VALUE);
            if (bval != null) {
                phase1 = bval;
            }
            bval = xml.getAttribute(PHASE2, DEFAULT_BOOLEAN_VALUE);
            if (bval != null) {
                phase2 = bval;
            }
            bval = xml.getAttribute(PHASE3, DEFAULT_BOOLEAN_VALUE);
            if (bval != null) {
                phase3 = bval;
            }
            bval = xml.getAttribute(PHASE4, DEFAULT_BOOLEAN_VALUE);
            if (bval != null) {
                phase4 = bval;
            }

            supportedCamelPhases.setData(phase1, phase2, phase3, phase4);
        }

        @Override
        public void write(SupportedCamelPhasesImpl supportedCamelPhases, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (supportedCamelPhases.getPhase1Supported())
                xml.setAttribute(PHASE1, true);
            if (supportedCamelPhases.getPhase2Supported())
                xml.setAttribute(PHASE2, true);
            if (supportedCamelPhases.getPhase3Supported())
                xml.setAttribute(PHASE3, true);
            if (supportedCamelPhases.getPhase4Supported())
                xml.setAttribute(PHASE4, true);
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase4Supported()
     */
    public boolean getPhase4Supported() {
        return this.bitString.get(_INDEX_Phase4);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedCamelPhases [");

        if (getPhase1Supported())
            sb.append("Phase1Supported, ");
        if (getPhase2Supported())
            sb.append("Phase2Supported, ");
        if (getPhase3Supported())
            sb.append("Phase3Supported, ");
        if (getPhase4Supported())
            sb.append("Phase4Supported, ");

        sb.append("]");

        return sb.toString();
    }
}