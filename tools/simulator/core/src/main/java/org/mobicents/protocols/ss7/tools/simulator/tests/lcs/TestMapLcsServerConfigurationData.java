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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;


/**
 *
 * @author falonso@csc.com
 *
 */
public class TestMapLcsServerConfigurationData {

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


    private String networkNodeNumberAddress = "5555544444";
    private String naESRDAddress = "11114444";
    private AddressNature addressNature = AddressNature.international_number;
    private NumberingPlan numberingPlanType = NumberingPlan.ISDN;
    //private String numberingPlan = "11112222";
    private String imsi = "5555544444";
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

    public void setNetworkNodeNumberAddress(String data) {
        this.networkNodeNumberAddress=data;
    }

    public String getNetworkNodeNumberAddress() {
        return networkNodeNumberAddress;
    }


    public AddressNature getAddressNature() {
        return addressNature;
    }

    public NumberingPlan getNumberingPlanType() {
        return numberingPlanType;
    }

    public void setAddressNature(AddressNature addressNature) {
        this.addressNature = addressNature;
    }

    public void setNumberingPlanType(NumberingPlan numberingPlan) {
        this.numberingPlanType = numberingPlan;
    }

        public String getMSISDN() {
        return msisdn;
    }

    public void setMSISDN(String msisdn) {
        this.msisdn = msisdn;
    }

    //public void setNumberingPlan(String numberingPlan) {
    //    this.numberingPlan=numberingPlan;
    //}

    //public String getNumberingPlan() {
    //    return numberingPlan;
    //}

    public void setIMSI(String data) {
        this.imsi=data;
    }

    public String getIMSI() {
        return imsi;
    }

    protected static final XMLFormat<TestMapLcsServerConfigurationData> XML = new XMLFormat<TestMapLcsServerConfigurationData>(
            TestMapLcsServerConfigurationData.class) {

        public void write(TestMapLcsServerConfigurationData srv, OutputElement xml) throws XMLStreamException {
            //xml.setAttribute(NETWORK_NODE_NUMBER_ADDRESS, srv.networkNodeNumberAddress);
            xml.add(srv.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(srv.numberingPlanType.toString(), NUMBERING_PLAN_TYPE, String.class);
            xml.add(srv.naESRDAddress.toString(), NA_ESRD_ADDRESS, String.class);
            //xml.add(srv.numberingPlan.toString(), NUMBERING_PLAN, String.class);
            xml.add(srv.imsi.toString(), IMSI, String.class);
            xml.add(srv.networkNodeNumberAddress.toString(), NETWORK_NODE_NUMBER_ADDRESS, String.class);
            xml.add(srv.ageOfLocationEstimate, AGE_OF_LOCATION_ESTIMATE, Integer.class);
            xml.add(srv.cellId, CELL_ID, Integer.class);
            xml.add(srv.lac, LAC, Integer.class);
            xml.add(srv.mnc, MNC, Integer.class);
            xml.add(srv.mcc, MCC, Integer.class);
            xml.add(srv.lcsReferenceNumber, LCS_REFERENCE_NUMBER, Integer.class);
            xml.add(srv.hgmlcAddress, HGMLC_ADDRESS, String.class);
            xml.add(srv.imei, IMEI, String.class);
            xml.add(srv.msisdn, MSISDN, String.class);
            xml.add(srv.lcsEvent.toString(), LCS_EVENT, String.class);
        }

        public void read(InputElement xml, TestMapLcsServerConfigurationData srv) throws XMLStreamException {
            //srv.networkNodeNumberAddress = xml.getAttribute(NETWORK_NODE_NUMBER_ADDRESS).toString();
            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            srv.addressNature = AddressNature.valueOf(an);
            String npt = (String) xml.get(NUMBERING_PLAN_TYPE, String.class);
            srv.numberingPlanType = NumberingPlan.valueOf(npt);
            String naESRDAddress = (String) xml.get(NA_ESRD_ADDRESS, String.class);
            srv.naESRDAddress = naESRDAddress;
            //String np = (String) xml.get(NUMBERING_PLAN, String.class);
            //srv.numberingPlan = np;
            String imsi = (String) xml.get(IMSI, String.class);
            srv.imsi = imsi;
            String networkNodeNumberAddress = (String) xml.get(NETWORK_NODE_NUMBER_ADDRESS, String.class);
            srv.networkNodeNumberAddress = networkNodeNumberAddress;
            Integer ageOfLocationEstimate = (Integer) xml.get(AGE_OF_LOCATION_ESTIMATE, Integer.class);
            srv.ageOfLocationEstimate = ageOfLocationEstimate;
            Integer cellId = (Integer) xml.get(CELL_ID, Integer.class);
            srv.cellId = cellId;
            Integer lac = (Integer) xml.get(LAC, Integer.class);
            srv.lac = lac;
            Integer mnc = (Integer) xml.get(MNC, Integer.class);
            srv.mnc = mnc;
            Integer mcc = (Integer) xml.get(MCC, Integer.class);
            srv.mcc = mcc;
            Integer lcsReferenceNumber = (Integer) xml.get(LCS_REFERENCE_NUMBER, Integer.class);
            srv.lcsReferenceNumber = lcsReferenceNumber;
            String gsnAddress = (String) xml.get(HGMLC_ADDRESS, String.class);
            srv.hgmlcAddress = gsnAddress;
            String imei = (String) xml.get(IMEI, String.class);
            srv.imei = imei;
            String msisdn = (String) xml.get(MSISDN, String.class);
            srv.msisdn = msisdn;
            String lcsEvent = (String) xml.get(LCS_EVENT, String.class);
            srv.lcsEvent = LCSEvent.valueOf(lcsEvent);
        }
    };

}
