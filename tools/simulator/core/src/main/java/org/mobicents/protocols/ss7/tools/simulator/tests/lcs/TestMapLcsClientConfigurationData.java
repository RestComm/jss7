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

import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;

//import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
*
* @author falonso@csc.com
*
*/
public class TestMapLcsClientConfigurationData {

    protected static final String NA_ESRD_ADDRESS = "na_esrd_address";
    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN_TYPE = "numberingPlanType";
    protected static final String NUMBERING_PLAN = "numberingPlan";
    protected static final String IMSI = "imsi";
    protected static final String NETWORK_NODE_NUMBER_ADDRESS = "networkNodeNumberAddress";
    protected static final String IMEI ="imei";
    protected static final String HGMLC_ADDRESS = "hgmlcAddress";
    protected static final String MCC ="mcc";
    protected static final String MNC = "mnc";
    protected static final String LAC ="lac";
    protected static final String CELL_ID="cellId";
    protected static final String LCS_REFERENCE_NUMBER="lcsReferenceNumber";
    protected static final String AGE_OF_LOCATION_ESTIMATE ="ageOfLocationEstimate";
    protected static final String LCS_EVENT = "lcsEvent";
    protected static final String MSISDN = "msisdn";

    public String getIMEI() {
        return imei;
    }

    public void setIMEI(String imei) {
        this.imei = imei;
    }

    public String getHGMLCAddress() {
        return hgmlcAddress;
    }

    public void setHGMLCAddress(String hgmlcAddress) {
        this.hgmlcAddress = hgmlcAddress;
    }

    public Integer getMCC() {
        return mcc;
    }

    public void setMCC(Integer mcc) {
        this.mcc = mcc;
    }

    public Integer getMNC() {
        return mnc;
    }

    public void setMNC(Integer mnc) {
        this.mnc = mnc;
    }

    public Integer getLAC() {
        return lac;
    }

    public void setLAC(Integer lac) {
        this.lac = lac;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Integer getLCSReferenceNumber() {
        return lcsReferenceNumber;
    }

    public void setLCSReferenceNumber(Integer lcsReferenceNumber) {
        this.lcsReferenceNumber = lcsReferenceNumber;
    }

    public Integer getAgeOfLocationEstimate() {
        return ageOfLocationEstimate;
    }

    public void setAgeOfLocationEstimate(Integer ageOfLocationEstimate) {
        this.ageOfLocationEstimate = ageOfLocationEstimate;
    }

    public LCSEvent getLCSEvent() {
        return lcsEvent;
    }

    public void setLCSEvent(LCSEvent lcsEvent) {
        this.lcsEvent = lcsEvent;
    }

    private String naESRDAddress = "11114444";

    private AddressNature addressNature = AddressNature.international_number;
    private NumberingPlan numberingPlanType = NumberingPlan.ISDN;
    private String numberingPlan = "11112222";
    private String imsi = "5555544444";
    private String networkNodeNumberAddress = "4444455555";
    private String imei = "5555544444";
    private String msisdn = "3333344444";
    private String hgmlcAddress = "0.0.0.0";
    private Integer mcc = 250;
    private Integer mnc = 123;
    private Integer lac =1111;
    private Integer cellId =222;
    private Integer lcsReferenceNumber = 111;
    private Integer ageOfLocationEstimate = 100;
    private LCSEvent lcsEvent = LCSEvent.emergencyCallOrigination;

    public String getNaESRDAddress() {
        return naESRDAddress;
    }

    public void setNaESRDAddress(String naESRDAddress) {
        this.naESRDAddress = naESRDAddress;
    }

    public String getMSISDN() {
        return msisdn;
    }

    public void setMSISDN(String msisdn) {
        this.msisdn = msisdn;
    }

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

            xml.add(clt.naESRDAddress.toString(), NA_ESRD_ADDRESS, String.class);
            xml.add(clt.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(clt.numberingPlanType.toString(), NUMBERING_PLAN_TYPE, String.class);
            xml.add(clt.numberingPlan.toString(), NUMBERING_PLAN, String.class);
            xml.add(clt.imsi.toString(), IMSI, String.class);
            xml.add(clt.networkNodeNumberAddress.toString(), NETWORK_NODE_NUMBER_ADDRESS, String.class);
            xml.add(clt.ageOfLocationEstimate, AGE_OF_LOCATION_ESTIMATE, Integer.class);
            xml.add(clt.cellId, CELL_ID, Integer.class);
            xml.add(clt.lac, LAC, Integer.class);
            xml.add(clt.mnc, MNC, Integer.class);
            xml.add(clt.mcc, MCC, Integer.class);
            xml.add(clt.lcsReferenceNumber, LCS_REFERENCE_NUMBER, Integer.class);
            xml.add(clt.hgmlcAddress, HGMLC_ADDRESS, String.class);
            xml.add(clt.imei, IMEI, String.class);
            xml.add(clt.msisdn, MSISDN, String.class);
            xml.add(clt.lcsEvent.toString(), LCS_EVENT, String.class);
        }

        public void read(InputElement xml, TestMapLcsClientConfigurationData clt) throws XMLStreamException {

            String naESRDAddress = (String) xml.get(NA_ESRD_ADDRESS, String.class);
            clt.naESRDAddress = naESRDAddress;
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
            Integer ageOfLocationEstimate = (Integer) xml.get(AGE_OF_LOCATION_ESTIMATE, Integer.class);
            clt.ageOfLocationEstimate = ageOfLocationEstimate;
            Integer cellId = (Integer) xml.get(CELL_ID, Integer.class);
            clt.cellId = cellId;
            Integer lac = (Integer) xml.get(LAC, Integer.class);
            clt.lac = lac;
            Integer mnc = (Integer) xml.get(MNC, Integer.class);
            clt.mnc = mnc;
            Integer mcc = (Integer) xml.get(MCC, Integer.class);
            clt.mcc = mcc;
            Integer lcsReferenceNumber = (Integer) xml.get(LCS_REFERENCE_NUMBER, Integer.class);
            clt.lcsReferenceNumber = lcsReferenceNumber;
            String gsnAddress = (String) xml.get(HGMLC_ADDRESS, String.class);
            clt.hgmlcAddress = gsnAddress;
            String imei = (String) xml.get(IMEI, String.class);
            clt.imei = imei;
            String msisdn = (String) xml.get(MSISDN, String.class);
            clt.msisdn = msisdn;
            String lcsEvent = (String) xml.get(LCS_EVENT, String.class);
            clt.lcsEvent = LCSEvent.valueOf(lcsEvent);
        }
    };

}
