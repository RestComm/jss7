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

package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

//import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
*
* @author falonso@csc.com
*
*/
public class TestMapLcsClientConfigurationData {

    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN_TYPE = "numberingPlanType";
    protected static final String NUMBERING_PLAN = "numberingPlan";
    protected static final String IMSI = "imsi";
    protected static final String NETWORK_NODE_NUMBER_ADDRESS = "networkNodeNumberAddress";

    private AddressNature addressNature = AddressNature.international_number;
    private NumberingPlan numberingPlanType = NumberingPlan.ISDN;
    private String numberingPlan = "11112222";
    private String imsi = "5555544444";
    private String networkNodeNumberAddress = "4444455555";


    public AddressNature getAddressNature() {
        return addressNature;
    }

    public void setAddressNature(AddressNature addressNature) {
        this.addressNature = addressNature;
    }

    public NumberingPlan getNumberingPlanType() {
        return numberingPlanType;
    }

    public void setNumberingPlanType(NumberingPlan numberingPlan) {
        this.numberingPlanType = numberingPlan;
    }

    public void setNumberingPlan(String numberingPlan) {
        this.numberingPlan=numberingPlan;
    }

    public String getNumberingPlan() {
        return numberingPlan;
    }

    public void setIMSI(String data) {
        this.imsi=data;
    }

    public String getIMSI() {
        return imsi;
    }

    public void setNetworkNodeNumberAddress(String data) {
        this.networkNodeNumberAddress=data;
    }

    public String getNetworkNodeNumberAddress() {
        return networkNodeNumberAddress;
    }

    protected static final XMLFormat<TestMapLcsClientConfigurationData> XML = new XMLFormat<TestMapLcsClientConfigurationData>(TestMapLcsClientConfigurationData.class) {

        public void write(TestMapLcsClientConfigurationData clt, OutputElement xml) throws XMLStreamException {

            xml.add(clt.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(clt.numberingPlanType.toString(), NUMBERING_PLAN_TYPE, String.class);
            xml.add(clt.numberingPlan.toString(), NUMBERING_PLAN, String.class);
            xml.add(clt.imsi.toString(), IMSI, String.class);
            xml.add(clt.networkNodeNumberAddress.toString(), NETWORK_NODE_NUMBER_ADDRESS, String.class);
        }

        public void read(InputElement xml, TestMapLcsClientConfigurationData clt) throws XMLStreamException {

            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            clt.addressNature = AddressNature.valueOf(an);
            String npt = (String) xml.get(NUMBERING_PLAN_TYPE, String.class);
            clt.numberingPlanType = NumberingPlan.valueOf(npt);
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            clt.numberingPlan = np;
            String imsi = (String) xml.get(IMSI, String.class);
            clt.imsi = imsi;
            String networkNodeNumberAddress = (String) xml.get(NETWORK_NODE_NUMBER_ADDRESS, String.class);
            clt.networkNodeNumberAddress = networkNodeNumberAddress;
        }
    };

}
