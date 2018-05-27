/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */
public class TestLcsClientConfigurationData {

    protected static final String NA_ESRD_ADDRESS = "na_esrd_address";
    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN_TYPE = "numberingPlanType";
    protected static final String IMSI = "imsi";
    protected static final String NETWORK_NODE_NUMBER_ADDRESS = "networkNodeNumberAddress";
    protected static final String IMEI = "imei";
    protected static final String HGMLC_ADDRESS = "hgmlcAddress";
    protected static final String MCC = "mcc";
    protected static final String MNC = "mnc";
    protected static final String LAC = "lac";
    protected static final String CELL_ID = "cellId";
    protected static final String LCS_REFERENCE_NUMBER = "lcsReferenceNumber";
    protected static final String AGE_OF_LOCATION_ESTIMATE = "ageOfLocationEstimate";
    protected static final String LCS_EVENT = "lcsEvent";
    protected static final String MSISDN = "msisdn";

    private String networkNodeNumberAddress = "598048";
    private String naESRDAddress = "11114444";
    private String naESRKAddress = "11115555";
    private AddressNature addressNature = AddressNature.international_number;
    private NumberingPlan numberingPlanType = NumberingPlan.ISDN;
    private String imsi = "748010192837465";
    private String msisdn = "59899077937";
    private String imei = "354449063537030";
    private String hgmlcAddress = "200.10.0.1";
    private Integer mcc = 748;
    private Integer mnc = 01;
    private Integer lac = 79010;
    private Integer cellId = 222;
    private Integer lcsReferenceNumber = 111;
    private Integer ageOfLocationEstimate = 1;
    private LCSEvent lcsEvent = LCSEvent.emergencyCallOrigination;

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

    public void setNetworkNodeNumberAddress(String data) {
        this.networkNodeNumberAddress = data;
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

    public void setIMSI(String data) {
        this.imsi = data;
    }

    public String getIMSI() {
        return imsi;
    }

    public String getNaESRDAddress() {
        return naESRDAddress;
    }

    public void setNaESRDAddress(String naESRDAddress) {
        this.naESRKAddress = naESRDAddress;
    }

    public String getNaESRKAddress() {
        return naESRDAddress;
    }

    public void setNaESRKAddress(String naESRKAddress) {
        this.naESRKAddress = naESRDAddress;
    }

    protected static final XMLFormat<TestLcsClientConfigurationData> XML = new XMLFormat<TestLcsClientConfigurationData>(
            TestLcsClientConfigurationData.class) {

        public void write(TestLcsClientConfigurationData srv, OutputElement xml) throws XMLStreamException {
            //xml.setAttribute(NETWORK_NODE_NUMBER_ADDRESS, srv.networkNodeNumberAddress);
            xml.add(srv.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(srv.numberingPlanType.toString(), NUMBERING_PLAN_TYPE, String.class);
            xml.add(srv.msisdn, MSISDN, String.class);
            xml.add(srv.imsi.toString(), IMSI, String.class);
            xml.add(srv.networkNodeNumberAddress.toString(), NETWORK_NODE_NUMBER_ADDRESS, String.class);
            xml.add(srv.ageOfLocationEstimate, AGE_OF_LOCATION_ESTIMATE, Integer.class);
            xml.add(srv.mcc, MCC, Integer.class);
            xml.add(srv.mnc, MNC, Integer.class);
            xml.add(srv.lac, LAC, Integer.class);
            xml.add(srv.cellId, CELL_ID, Integer.class);
            xml.add(srv.lcsReferenceNumber, LCS_REFERENCE_NUMBER, Integer.class);
            xml.add(srv.hgmlcAddress, HGMLC_ADDRESS, String.class);
            xml.add(srv.imei, IMEI, String.class);
            xml.add(srv.naESRDAddress.toString(), NA_ESRD_ADDRESS, String.class);
            //xml.add(srv.lcsEvent.toString(), LCS_EVENT, String.class);
        }

        public void read(InputElement xml, TestLcsClientConfigurationData srv) throws XMLStreamException {
            //srv.networkNodeNumberAddress = xml.getAttribute(NETWORK_NODE_NUMBER_ADDRESS).toString();
            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            srv.addressNature = AddressNature.valueOf(an);
            String npt = (String) xml.get(NUMBERING_PLAN_TYPE, String.class);
            srv.numberingPlanType = NumberingPlan.valueOf(npt);
            String msisdn = (String) xml.get(MSISDN, String.class);
            srv.msisdn = msisdn;
            String imsi = (String) xml.get(IMSI, String.class);
            srv.imsi = imsi;
            String networkNodeNumberAddress = (String) xml.get(NETWORK_NODE_NUMBER_ADDRESS, String.class);
            srv.networkNodeNumberAddress = networkNodeNumberAddress;
            Integer ageOfLocationEstimate = (Integer) xml.get(AGE_OF_LOCATION_ESTIMATE, Integer.class);
            srv.ageOfLocationEstimate = ageOfLocationEstimate;
            Integer mcc = (Integer) xml.get(MCC, Integer.class);
            srv.mcc = mcc;
            Integer mnc = (Integer) xml.get(MNC, Integer.class);
            srv.mnc = mnc;
            Integer lac = (Integer) xml.get(LAC, Integer.class);
            srv.lac = lac;
            Integer cellId = (Integer) xml.get(CELL_ID, Integer.class);
            srv.cellId = cellId;
            Integer lcsReferenceNumber = (Integer) xml.get(LCS_REFERENCE_NUMBER, Integer.class);
            srv.lcsReferenceNumber = lcsReferenceNumber;
            String gsnAddress = (String) xml.get(HGMLC_ADDRESS, String.class);
            srv.hgmlcAddress = gsnAddress;
            String imei = (String) xml.get(IMEI, String.class);
            srv.imei = imei;
            String naESRDAddress = (String) xml.get(NA_ESRD_ADDRESS, String.class);
            srv.naESRDAddress = naESRDAddress;
        }
    };

}
