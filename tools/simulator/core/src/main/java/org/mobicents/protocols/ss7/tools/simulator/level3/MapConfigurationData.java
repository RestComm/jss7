/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tools.simulator.level3;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MapConfigurationData {

    protected static final String LOCAL_SSN = "localSsn";
    protected static final String REMOTE_SSN = "remoteSsn";
    protected static final String REMOTE_ADDRESS_DIGITS = "remoteAddressDigits";
    protected static final String ORIG_REFERENCE = "origReference";
    protected static final String ORIG_REFERENCE_ADDRESS_NATURE = "origReferenceAddressNature";
    protected static final String ORIG_REFERENCE_NUMBERING_PLAN = "origReferenceNumberingPlan";
    protected static final String DEST_REFERENCE = "destReference";
    protected static final String DEST_REFERENCE_ADDRESS_NATURE = "destReferenceAddressNature";
    protected static final String DEST_REFERENCE_NUMBERING_PLAN = "destReferenceNumberingPlan";

    // private int localSsn;
    // private int remoteSsn;
    private String remoteAddressDigits;

    private String origReference;
    private AddressNature origReferenceAddressNature = AddressNature.international_number;
    private NumberingPlan origReferenceNumberingPlan = NumberingPlan.ISDN;
    private String destReference;
    private AddressNature destReferenceAddressNature = AddressNature.international_number;
    private NumberingPlan destReferenceNumberingPlan = NumberingPlan.ISDN;

    // public int getLocalSsn() {
    // return localSsn;
    // }
    //
    // public void setLocalSsn(int localSsn) {
    // this.localSsn = localSsn;
    // }
    //
    // public int getRemoteSsn() {
    // return remoteSsn;
    // }
    //
    // public void setRemoteSsn(int remoteSsn) {
    // this.remoteSsn = remoteSsn;
    // }

    public String getRemoteAddressDigits() {
        return remoteAddressDigits;
    }

    public void setRemoteAddressDigits(String remoteAddressDigits) {
        this.remoteAddressDigits = remoteAddressDigits;
    }

    public String getOrigReference() {
        return origReference;
    }

    public void setOrigReference(String origReference) {
        this.origReference = origReference;
    }

    public AddressNature getOrigReferenceAddressNature() {
        return origReferenceAddressNature;
    }

    public void setOrigReferenceAddressNature(AddressNature origReferenceAddressNature) {
        this.origReferenceAddressNature = origReferenceAddressNature;
    }

    public NumberingPlan getOrigReferenceNumberingPlan() {
        return origReferenceNumberingPlan;
    }

    public void setOrigReferenceNumberingPlan(NumberingPlan origReferenceNumberingPlan) {
        this.origReferenceNumberingPlan = origReferenceNumberingPlan;
    }

    public String getDestReference() {
        return destReference;
    }

    public void setDestReference(String destReference) {
        this.destReference = destReference;
    }

    public AddressNature getDestReferenceAddressNature() {
        return destReferenceAddressNature;
    }

    public void setDestReferenceAddressNature(AddressNature destReferenceAddressNature) {
        this.destReferenceAddressNature = destReferenceAddressNature;
    }

    public NumberingPlan getDestReferenceNumberingPlan() {
        return destReferenceNumberingPlan;
    }

    public void setDestReferenceNumberingPlan(NumberingPlan destReferenceNumberingPlan) {
        this.destReferenceNumberingPlan = destReferenceNumberingPlan;
    }

    protected static final XMLFormat<MapConfigurationData> XML = new XMLFormat<MapConfigurationData>(MapConfigurationData.class) {

        public void write(MapConfigurationData map, OutputElement xml) throws XMLStreamException {
            // xml.setAttribute(LOCAL_SSN, map.getLocalSsn());
            // xml.setAttribute(REMOTE_SSN, map.getRemoteSsn());

            xml.add(map.getRemoteAddressDigits(), REMOTE_ADDRESS_DIGITS, String.class);
            xml.add(map.getOrigReference(), ORIG_REFERENCE, String.class);
            xml.add(map.getDestReference(), DEST_REFERENCE, String.class);

            xml.add(map.getOrigReferenceAddressNature().toString(), ORIG_REFERENCE_ADDRESS_NATURE, String.class);
            xml.add(map.getOrigReferenceNumberingPlan().toString(), ORIG_REFERENCE_NUMBERING_PLAN, String.class);
            xml.add(map.getDestReferenceAddressNature().toString(), DEST_REFERENCE_ADDRESS_NATURE, String.class);
            xml.add(map.getDestReferenceNumberingPlan().toString(), DEST_REFERENCE_NUMBERING_PLAN, String.class);
        }

        public void read(InputElement xml, MapConfigurationData map) throws XMLStreamException {
            // map.setLocalSsn(xml.getAttribute(LOCAL_SSN).toInt());
            // map.setRemoteSsn(xml.getAttribute(REMOTE_SSN).toInt());

            map.setRemoteAddressDigits((String) xml.get(REMOTE_ADDRESS_DIGITS, String.class));
            map.setOrigReference((String) xml.get(ORIG_REFERENCE, String.class));
            map.setDestReference((String) xml.get(DEST_REFERENCE, String.class));

            String an = (String) xml.get(ORIG_REFERENCE_ADDRESS_NATURE, String.class);
            map.setOrigReferenceAddressNature(AddressNature.valueOf(an));
            String np = (String) xml.get(ORIG_REFERENCE_NUMBERING_PLAN, String.class);
            map.setOrigReferenceNumberingPlan(NumberingPlan.valueOf(np));
            an = (String) xml.get(DEST_REFERENCE_ADDRESS_NATURE, String.class);
            map.setDestReferenceAddressNature(AddressNature.valueOf(an));
            np = (String) xml.get(DEST_REFERENCE_NUMBERING_PLAN, String.class);
            map.setDestReferenceNumberingPlan(NumberingPlan.valueOf(np));
        }
    };

}
