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

//import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
//import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
//import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestImpl;
//import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapSsfConfigurationData {

    private static final String CAP_APPLICATION_CONTEXT = "capApplicationContext";

    /** InitialDPRequest parameters **/
    private static final String SERVICE_KEY = "serviceKey";
    private static final String IDP_EVENT_TYPE_BCSM = "idpEventTypeBCSM";
    private static final String USE_CLD_INSTEAD_OF_CLDBCD_NUMBER = "useCldInsteadOfCldBCDNumber";
    private static final String CALLING_PARTY_NUMBER = "callingPartyNumber";
    private static final String CALLING_PARTY_NUMBER_ADDRES = CALLING_PARTY_NUMBER + "Address";
    private static final String CALLING_PARTY_NUMBER_NAI = CALLING_PARTY_NUMBER + "NAI";
    private static final String CALLING_PARTY_NUMBER_NPI = CALLING_PARTY_NUMBER + "NPI";
    private static final String CALLED_PARTY_BCD_NUMBER = "calledPartyBCDNumber";
    private static final String CALLED_PARTY_BCD_NUMBER_ADDRES = CALLED_PARTY_BCD_NUMBER + "Address";
    private static final String CALLED_PARTY_BCD_NUMBER_NPI = CALLED_PARTY_BCD_NUMBER + "NPI";
    private static final String CALLED_PARTY_BCD_NUMBER_TON = CALLED_PARTY_BCD_NUMBER + "TON";
    private static final String CALLED_PARTY_NUMBER = "calledPartyNumber";
    private static final String CALLED_PARTY_NUMBER_ADDRES = CALLED_PARTY_NUMBER + "Address";
    private static final String CALLED_PARTY_NUMBER_NAI = CALLED_PARTY_NUMBER + "NAI";
    private static final String CALLED_PARTY_NUMBER_NPI = CALLED_PARTY_NUMBER + "NPI";
    private static final String MSC_ADDRESS = "mscAddress";
    private static final String MSC_ADDRESS_ADDRES = MSC_ADDRESS + "Address";
    private static final String MSC_ADDRESS_NA = MSC_ADDRESS + "NA";
    private static final String MSC_ADDRESS_NPI = MSC_ADDRESS + "NPI";

    private CapApplicationContextSsf capApplicationContext = new CapApplicationContextSsf(
            CapApplicationContextSsf.VAL_CAP_V1_gsmSSF_to_gsmSCF);

    private int serviceKey = 10;
    private EventTypeBCSM idpEventTypeBCSM = EventTypeBCSM.collectedInfo;

    private boolean useCldInsteadOfCldBCDNumber= false;

    private String callingPartyNumberAddress = "11111111";
    private IsupNatureOfAddressIndicator callingPartyNumberNatureOfAddress = IsupNatureOfAddressIndicator.getInstance(CallingPartyNumber._NAI_INTERNATIONAL_NUMBER);
    private IsupNumberingPlanIndicator callingPartyNumberNumberingPlan = IsupNumberingPlanIndicator.getInstance(CallingPartyNumber._NPI_ISDN);

    private String calledPartyBCDNumberAddress = "22222222";
    private AddressNature calledPartyBCDNumberAddressNature = AddressNature.international_number;
    private NumberingPlan calledPartyBCDNumberNumberingPlan = NumberingPlan.ISDN;

    private String calledPartyNumberAddress = "33333333";
    private IsupNatureOfAddressIndicator calledPartyNumberNatureOfAddress =  IsupNatureOfAddressIndicator.getInstance(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
    private IsupNumberingPlanIndicator calledPartyNumberNumberingPlan = IsupNumberingPlanIndicator.getInstance(CalledPartyNumber._NPI_ISDN);

    private String mscAddressAddress = "55555555";
    private AddressNature mscAddressNatureOfAddress = AddressNature.international_number;
    private NumberingPlan mscAddressNumberingPlan = NumberingPlan.ISDN;

    /*int serviceKey = 13;
    CalledPartyNumber calledPartyNumberIsup = capProvider.getISUPParameterFactory().createCalledPartyNumber();
    calledPartyNumberIsup.setAddress("111222");
    calledPartyNumberIsup.setInternalNetworkNumberIndicator(calledPartyNumberIsup._INN_ROUTING_ALLOWED);
    calledPartyNumberIsup.setNatureOfAddresIndicator(calledPartyNumberIsup._NAI_INTERNATIONAL_NUMBER);
    calledPartyNumberIsup.setNumberingPlanIndicator(calledPartyNumberIsup._NPI_ISDN);
    CalledPartyNumberCap calledPartyNumber = capProvider.getCAPParameterFactory().createCalledPartyNumberCap(
            calledPartyNumberIsup)

    private InitialDPRequestImpl initialDPReq = new InitialDPRequestImpl(serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory, CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions, highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, cugOutgoingAccess, imsi, subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress, calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension, isCAPVersion3orLater)
;*/
    public CapApplicationContextSsf getCapApplicationContext() {
        return capApplicationContext;
    }

    public void setCapApplicationContext(CapApplicationContextSsf capApplicationContext) {
        this.capApplicationContext = capApplicationContext;
    }


    public Integer getServiceKey() {
        return serviceKey;
    }
    public void setServiceKey(Integer serviceKey) {
        this.serviceKey = serviceKey;
    }
    public EventTypeBCSM getIdpEventTypeBCSM() {
        return idpEventTypeBCSM;
    }
    public void setIdpEventTypeBCSM(EventTypeBCSM eventTypeBCSM) {
        this.idpEventTypeBCSM = eventTypeBCSM;
    }

    public boolean isUseCldInsteadOfCldBCDNumber() {
        return useCldInsteadOfCldBCDNumber;
    }
    public void setUseCldInsteadOfCldBCDNumber(boolean useCldInsteadOfCldBCDNumber) {
        this.useCldInsteadOfCldBCDNumber = useCldInsteadOfCldBCDNumber;
    }
    public String getCallingPartyNumberAddress() {
        return callingPartyNumberAddress;
    }
    public void setCallingPartyNumberAddress(String callingPartyNumberAddress) {
        this.callingPartyNumberAddress = callingPartyNumberAddress;
    }
    public IsupNatureOfAddressIndicator getCallingPartyNumberNatureOfAddress() {
        return callingPartyNumberNatureOfAddress;
    }
    public void setCallingPartyNumberNatureOfAddress(IsupNatureOfAddressIndicator callingPartyNumberNature) {
        this.callingPartyNumberNatureOfAddress = callingPartyNumberNature;
    }
    public IsupNumberingPlanIndicator getCallingPartyNumberNumberingPlan() {
        return callingPartyNumberNumberingPlan;
    }
    public void setCallingPartyNumberNumberingPlan(IsupNumberingPlanIndicator callingPartyNumberPlan) {
        this.callingPartyNumberNumberingPlan = callingPartyNumberPlan;
    }
    public String getCalledPartyBCDNumberAddress() {
        return calledPartyBCDNumberAddress;
    }
    public void setCalledPartyBCDNumberAddress(String calledPartyBCDNumberAddress) {
        this.calledPartyBCDNumberAddress = calledPartyBCDNumberAddress;
    }
    public AddressNature getCalledPartyBCDNumberAddressNature() {
        return calledPartyBCDNumberAddressNature;
    }
    public void setCalledPartyBCDNumberAddressNature(AddressNature calledPartyBCDNumberAddressNature) {
        this.calledPartyBCDNumberAddressNature = calledPartyBCDNumberAddressNature;
    }
    public NumberingPlan getCalledPartyBCDNumberNumberingPlan() {
        return calledPartyBCDNumberNumberingPlan;
    }
    public void setCalledPartyBCDNumberNumberingPlan(NumberingPlan calledPartyBCDNumberNumberingPlan) {
        this.calledPartyBCDNumberNumberingPlan = calledPartyBCDNumberNumberingPlan;
    }
    public String getCalledPartyNumberAddress() {
        return calledPartyNumberAddress;
    }
    public void setCalledPartyNumberAddress(String calledPartyNumberAddress) {
        this.calledPartyNumberAddress = calledPartyNumberAddress;
    }
    public IsupNatureOfAddressIndicator getCalledPartyNumberNatureOfAddress() {
        return calledPartyNumberNatureOfAddress;
    }
    public void setCalledPartyNumberNatureOfAddress(IsupNatureOfAddressIndicator calledPartyNumberNatureOfAddress) {
        this.calledPartyNumberNatureOfAddress = calledPartyNumberNatureOfAddress;
    }
    public IsupNumberingPlanIndicator getCalledPartyNumberNumberingPlan() {
        return calledPartyNumberNumberingPlan;
    }
    public void setCalledPartyNumberNumberingPlan(IsupNumberingPlanIndicator calledPartyNumberNumberingPlan) {
        this.calledPartyNumberNumberingPlan = calledPartyNumberNumberingPlan;
    }
    public String getMscAddressAddress() {
        return mscAddressAddress;
    }
    public void setMscAddressAddress(String mscAddressAddress) {
        this.mscAddressAddress = mscAddressAddress;
    }
    public AddressNature getMscAddressNatureOfAddress() {
        return mscAddressNatureOfAddress;
    }
    public void setMscAddressNatureOfAddress(AddressNature mscAddressNature) {
        this.mscAddressNatureOfAddress = mscAddressNature;
    }
    public NumberingPlan getMscAddressNumberingPlan() {
        return mscAddressNumberingPlan;
    }
    public void setMscAddressNumberingPlan(NumberingPlan mscAddressNumberingPlan) {
        this.mscAddressNumberingPlan = mscAddressNumberingPlan;
    }


    protected static final XMLFormat<TestCapSsfConfigurationData> XML = new XMLFormat<TestCapSsfConfigurationData>(
            TestCapSsfConfigurationData.class) {

        public void write(TestCapSsfConfigurationData ssf, OutputElement xml) throws XMLStreamException {
            xml.add(ssf.capApplicationContext.toString(), CAP_APPLICATION_CONTEXT, String.class);

            xml.add(ssf.serviceKey, SERVICE_KEY, Integer.class);
            xml.add(""+ssf.idpEventTypeBCSM, IDP_EVENT_TYPE_BCSM, String.class);
            xml.add(ssf.useCldInsteadOfCldBCDNumber, USE_CLD_INSTEAD_OF_CLDBCD_NUMBER, Boolean.class);

            xml.add(ssf.callingPartyNumberAddress, CALLING_PARTY_NUMBER_ADDRES, String.class);
            xml.add(""+ssf.callingPartyNumberNatureOfAddress, CALLING_PARTY_NUMBER_NAI, String.class);
            xml.add(""+ssf.callingPartyNumberNumberingPlan, CALLING_PARTY_NUMBER_NPI, String.class);

            xml.add(ssf.calledPartyBCDNumberAddress, CALLED_PARTY_BCD_NUMBER_ADDRES, String.class);
            xml.add(""+ssf.calledPartyBCDNumberAddressNature, CALLED_PARTY_BCD_NUMBER_TON, String.class);
            xml.add(""+ssf.calledPartyBCDNumberNumberingPlan, CALLED_PARTY_BCD_NUMBER_NPI, String.class);

            xml.add(ssf.calledPartyNumberAddress, CALLED_PARTY_NUMBER_ADDRES, String.class);
            xml.add(""+ssf.calledPartyNumberNatureOfAddress, CALLED_PARTY_NUMBER_NAI, String.class);
            xml.add(""+ssf.calledPartyNumberNumberingPlan, CALLED_PARTY_NUMBER_NPI, String.class);


            xml.add(ssf.mscAddressAddress, MSC_ADDRESS_ADDRES, String.class);
            xml.add(""+ssf.mscAddressNatureOfAddress, MSC_ADDRESS_NA, String.class);
            xml.add(""+ssf.mscAddressNumberingPlan, MSC_ADDRESS_NPI, String.class);

            //xml.add((InitialDPRequestImpl) capMessage, CAPMessageType.initialDP_Request.name(), InitialDPRequestImpl.class);
        }

        public void read(InputElement xml, TestCapSsfConfigurationData ssf) throws XMLStreamException {
            String cpv = (String) xml.get(CAP_APPLICATION_CONTEXT, String.class);
            ssf.capApplicationContext = CapApplicationContextSsf.createInstance(cpv);

            Integer vali = (Integer) xml.get(SERVICE_KEY, Integer.class);
            if (vali != null)
                ssf.serviceKey = vali;
            String eventTypeBCSM = (String) xml.get(IDP_EVENT_TYPE_BCSM, String.class);
            if (eventTypeBCSM != null)
                ssf.idpEventTypeBCSM = EventTypeBCSM.valueOf(eventTypeBCSM);

            Boolean valb = (Boolean) xml.get(USE_CLD_INSTEAD_OF_CLDBCD_NUMBER, Boolean.class);
            if (valb != null)
                ssf.useCldInsteadOfCldBCDNumber = valb;

            String vals = (String) xml.get(CALLING_PARTY_NUMBER_ADDRES, String.class);
            if (vals != null)
                ssf.callingPartyNumberAddress = vals;
            String clgNai = (String) xml.get(CALLING_PARTY_NUMBER_NAI, String.class);
            if (clgNai != null)
                ssf.callingPartyNumberNatureOfAddress = IsupNatureOfAddressIndicator.valueOf(clgNai);
            String clgNPI = (String) xml.get(CALLING_PARTY_NUMBER_NPI, String.class);
            if (clgNPI != null)
                ssf.callingPartyNumberNumberingPlan = IsupNumberingPlanIndicator.valueOf(clgNPI);

            vals = (String) xml.get(CALLED_PARTY_BCD_NUMBER_ADDRES, String.class);
            if (vals != null)
                ssf.calledPartyBCDNumberAddress = vals;
            String calledPartyBCDNumberAddressNature = (String) xml.get(CALLED_PARTY_BCD_NUMBER_TON, String.class);
            if (calledPartyBCDNumberAddressNature != null)
                ssf.calledPartyBCDNumberAddressNature = AddressNature.valueOf(calledPartyBCDNumberAddressNature);
            String calledPartyBCDNumberNP = (String) xml.get(CALLED_PARTY_BCD_NUMBER_NPI, String.class);
            if (calledPartyBCDNumberNP != null)
                ssf.calledPartyBCDNumberNumberingPlan = NumberingPlan.valueOf(calledPartyBCDNumberNP);

            vals = (String) xml.get(CALLED_PARTY_NUMBER_ADDRES, String.class);
            if (vals != null)
                ssf.calledPartyNumberAddress = vals;
            String cldNAI = (String) xml.get(CALLED_PARTY_NUMBER_NAI, String.class);
            if (cldNAI != null)
                ssf.calledPartyNumberNatureOfAddress = IsupNatureOfAddressIndicator.valueOf(cldNAI);
            String cldNPI = (String) xml.get(CALLED_PARTY_NUMBER_NPI, String.class);
            if (cldNPI != null)
                ssf.calledPartyNumberNumberingPlan = IsupNumberingPlanIndicator.valueOf(cldNPI);

            vals = (String) xml.get(MSC_ADDRESS_ADDRES, String.class);
            if (vals != null)
                ssf.mscAddressAddress = vals;
            String mscAddressNature = (String) xml.get(MSC_ADDRESS_NA, String.class);
            if (mscAddressNature != null)
                ssf.mscAddressNatureOfAddress = AddressNature.valueOf(mscAddressNature);
            String mscAddrNP = (String) xml.get(MSC_ADDRESS_NPI, String.class);
            if (mscAddrNP != null)
                ssf.mscAddressNumberingPlan = NumberingPlan.valueOf(mscAddrNP);

        }
    };

}
