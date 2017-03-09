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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapScfConfigurationData {

    private static final String CAP_APPLICATION_CONTEXT = "capApplicationContext";
    private static final String CON_DESTINATION_ROUTING_ADDRESS = "destinationRoutingAddress";
    private static final String CON_DESTINATION_ROUTING_ADDRESS_ADDRES = CON_DESTINATION_ROUTING_ADDRESS + "Address";
    private static final String CON_DESTINATION_ROUTING_ADDRESS_NAI = CON_DESTINATION_ROUTING_ADDRESS + "NAI";
    private static final String CON_DESTINATION_ROUTING_ADDRESS_NPI = CON_DESTINATION_ROUTING_ADDRESS + "NPI";
    private static final String REL_CAUSE_VALUE = "releaseCauseValue";
    private static final String REL_CODING_STANDARD_IND = "releaseCauseCodingStandardIndicator";
    private static final String REL_LOCATION_IND = "releaseCauseLocationIndicator";

    private CapApplicationContextScf capApplicationContext = new CapApplicationContextScf(
            CapApplicationContextScf.VAL_CAP_V4_capscf_ssfGeneric);

    private String conDestRouteAddrAddress = "77777777";
    private IsupNatureOfAddressIndicator conDestRouteAddrNatureOfAddress = IsupNatureOfAddressIndicator.getInstance(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
    private IsupNumberingPlanIndicator conDestRouteAddrNumberingPlan = IsupNumberingPlanIndicator.getInstance(CalledPartyNumber._NPI_ISDN);

    private IsupCauseIndicatorCauseValue relCauseValue = IsupCauseIndicatorCauseValue.normalUnspecified;
    private IsupCauseIndicatorCodingStandard relCodingStandardInd = IsupCauseIndicatorCodingStandard.ITUT;
    private IsupCauseIndicatorLocation relLocationInd = IsupCauseIndicatorLocation.internationalNetwork;

    public CapApplicationContextScf getCapApplicationContext() {
        return capApplicationContext;
    }

    public void setCapApplicationContext(CapApplicationContextScf capApplicationContext) {
        this.capApplicationContext = capApplicationContext;
    }

    public String getConDestRouteAddrAddress() {
        return conDestRouteAddrAddress;
    }

    public void setConDestRouteAddrAddress(String conDestRouteAddrAddress) {
        this.conDestRouteAddrAddress = conDestRouteAddrAddress;
    }

    public IsupNatureOfAddressIndicator getConDestRouteAddrNatureOfAddress() {
        return conDestRouteAddrNatureOfAddress;
    }

    public void setConDestRouteAddrNatureOfAddress(IsupNatureOfAddressIndicator conDestRouteAddrNatureOfAddress) {
        this.conDestRouteAddrNatureOfAddress = conDestRouteAddrNatureOfAddress;
    }

    public IsupNumberingPlanIndicator getConDestRouteAddrNumberingPlan() {
        return conDestRouteAddrNumberingPlan;
    }

    public void setConDestRouteAddrNumberingPlan(IsupNumberingPlanIndicator conDestRouteAddrNumberingPlan) {
        this.conDestRouteAddrNumberingPlan = conDestRouteAddrNumberingPlan;
    }

    public IsupCauseIndicatorCauseValue getRelCauseValue() {
        return relCauseValue;
    }

    public void setRelCauseValue(IsupCauseIndicatorCauseValue relCauseValue) {
        this.relCauseValue = relCauseValue;
    }

    public IsupCauseIndicatorCodingStandard getRelCodingStandardInd() {
        return relCodingStandardInd;
    }

    public void setRelCodingStandardInd(IsupCauseIndicatorCodingStandard relCodingStandardInd) {
        this.relCodingStandardInd = relCodingStandardInd;
    }

    public IsupCauseIndicatorLocation getRelLocationInd() {
        return relLocationInd;
    }

    public void setRelLocationInd(IsupCauseIndicatorLocation relLocationInd) {
        this.relLocationInd = relLocationInd;
    }


    protected static final XMLFormat<TestCapScfConfigurationData> XML = new XMLFormat<TestCapScfConfigurationData>(
            TestCapScfConfigurationData.class) {

        public void write(TestCapScfConfigurationData scf, OutputElement xml) throws XMLStreamException {
            xml.add(scf.capApplicationContext.toString(), CAP_APPLICATION_CONTEXT, String.class);
            xml.add(scf.conDestRouteAddrAddress, CON_DESTINATION_ROUTING_ADDRESS_ADDRES, String.class);
            xml.add(""+scf.conDestRouteAddrNatureOfAddress, CON_DESTINATION_ROUTING_ADDRESS_NAI, String.class);
            xml.add(""+scf.conDestRouteAddrNumberingPlan, CON_DESTINATION_ROUTING_ADDRESS_NPI, String.class);
            xml.add(""+scf.relCauseValue, REL_CAUSE_VALUE, String.class);
            xml.add(""+scf.relCodingStandardInd, REL_CODING_STANDARD_IND, String.class);
            xml.add(""+scf.relLocationInd, REL_LOCATION_IND, String.class);
        }

        public void read(InputElement xml, TestCapScfConfigurationData scf) throws XMLStreamException {
            String cpv = (String) xml.get(CAP_APPLICATION_CONTEXT, String.class);
            scf.capApplicationContext = CapApplicationContextScf.createInstance(cpv);

            scf.conDestRouteAddrAddress = (String) xml.get(CON_DESTINATION_ROUTING_ADDRESS_ADDRES, String.class);
            String cldNAI = (String) xml.get(CON_DESTINATION_ROUTING_ADDRESS_NAI, String.class);
            if (cldNAI != null)
                scf.conDestRouteAddrNatureOfAddress = IsupNatureOfAddressIndicator.valueOf(cldNAI);
            String cldNPI = (String) xml.get(CON_DESTINATION_ROUTING_ADDRESS_NPI, String.class);
            if (cldNPI != null)
                scf.conDestRouteAddrNumberingPlan = IsupNumberingPlanIndicator.valueOf(cldNPI);
            String relCauseValue = (String) xml.get(REL_CAUSE_VALUE, String.class);
            if (relCauseValue != null)
                scf.relCauseValue = IsupCauseIndicatorCauseValue.valueOf(relCauseValue);
            String relCodingStandardInd = (String) xml.get(REL_CODING_STANDARD_IND, String.class);
            if (relCodingStandardInd != null)
                scf.relCodingStandardInd = IsupCauseIndicatorCodingStandard.valueOf(relCodingStandardInd);
            String relLocationInd = (String) xml.get(REL_LOCATION_IND, String.class);
            if (relLocationInd != null)
                scf.relLocationInd = IsupCauseIndicatorLocation.valueOf(relLocationInd);
        }
    };
}
