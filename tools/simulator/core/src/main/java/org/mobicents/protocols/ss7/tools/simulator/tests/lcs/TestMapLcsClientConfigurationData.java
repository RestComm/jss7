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
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;

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
    protected static final String LOCATIONESTIMATETYPE = "locationEstimateType";
    protected static final String LCSSERVICETYPEID = "lcsServiceTypeID";
    protected static final String MOLRSHORTCIRCUITINDICATOR = "moLrShortCircuitIndicator";
    protected static final String LCSCLIENTTYPE = "lcsClientType";
    protected static final String CODEWORDUSSDSTRING = "codeWordUSSDString";
    protected static final String CALLSESSIONUNRELATED = "callSessionUnrelated";
    protected static final String CALLSESSIONRELATED = "callSessionRelated";
    protected static final String AREATYPE = "areaType";
    protected static final String REPORTINGAMMOUNT = "reportingAmmount";
    protected static final String REPORTINGINTERVAL = "reportingInterval";
    protected static final String DATACODINGSCHEME = "dataCodingScheme";

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

    public LCSClientType getLcsClientType() {
        return lcsClientType;
    }

    public void setLcsClientType(LCSClientType val){
        this.lcsClientType = val;
    }

    public void setLocationEstimateType(LocationEstimateType locType){
        this.locationEstimateType=locType;
    }

    public LocationEstimateType getLocationEstimateType() {
        return locationEstimateType;
    }

    public Integer getLcsServiceTypeID(){
         return lcsServiceTypeID;
    }

    public void setLcsServiceTypeID(Integer lcsServiceTypeID){
        this.lcsServiceTypeID = lcsServiceTypeID;
    }

    public boolean getMoLrShortCircuitIndicator(){
        return this.moLrShortCircuitIndicator;
    }

    public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator){
        this.moLrShortCircuitIndicator= moLrShortCircuitIndicator;
    }

    public void setCallSessionUnrelated(PrivacyCheckRelatedAction val){
        this.callSessionUnrelated=val;
    }

    public PrivacyCheckRelatedAction getCallSessionUnrelated(){
        return callSessionUnrelated;
    }

    public void setCallSessionRelated(PrivacyCheckRelatedAction val){
        this.callSessionRelated=val;
    }

    public PrivacyCheckRelatedAction getCallSessionRelated(){
        return callSessionRelated;
    }

    public void setAreaType(AreaType areaType){
        this.areaType=areaType;
    }

    public AreaType getAreaType(){
        return areaType;
    }

    public void setReportingAmmount(Integer val){
        this.reportingAmmount=val;
    }

    public Integer getReportingAmmount(){
        return reportingAmmount;
    }
    public void setReportingInterval(Integer val){
        this.reportingInterval=val;
    }

    public Integer getReportingInterval(){
        return reportingInterval;
    }

    public void setDataCodingScheme(Integer val){
        this.dataCodingScheme=val;
    }

    public Integer getDataCodingScheme(){
        return dataCodingScheme;
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
    private LocationEstimateType locationEstimateType = LocationEstimateType.currentLocation;
    private Integer lcsServiceTypeID = 0;
    private boolean moLrShortCircuitIndicator = false;
    private LCSClientType lcsClientType = LCSClientType.emergencyServices;
    private String codeWordUSSDString = "CODE";
    private PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
    private PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
    private AreaType areaType = AreaType.countryCode;
    private Integer reportingAmmount = 10;
    private Integer reportingInterval = 10;
    private Integer dataCodingScheme = 15;

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

    public void setCodeWordUSSDString(String codeWordUSSDString){
        this.codeWordUSSDString =codeWordUSSDString;
    }

    public String getCodeWordUSSDString(){
        return this.codeWordUSSDString;
    }



    protected static final XMLFormat<TestMapLcsClientConfigurationData> XML = new XMLFormat<TestMapLcsClientConfigurationData>(TestMapLcsClientConfigurationData.class) {

        public void write(TestMapLcsClientConfigurationData clt, OutputElement xml) throws XMLStreamException {

            xml.setAttribute(REPORTINGAMMOUNT, clt.reportingAmmount);

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
            xml.add(clt.locationEstimateType.toString(),LOCATIONESTIMATETYPE,String.class);
            xml.add(clt.lcsServiceTypeID,LCSSERVICETYPEID,Integer.class);
            xml.add(Boolean.toString(clt.moLrShortCircuitIndicator),MOLRSHORTCIRCUITINDICATOR,String.class);
            xml.add(clt.lcsClientType.toString(),LCSCLIENTTYPE,String.class);
            xml.add(clt.codeWordUSSDString.toString(),CODEWORDUSSDSTRING,String.class);
            xml.add(clt.callSessionUnrelated.toString(),CALLSESSIONUNRELATED,String.class);
            xml.add(clt.callSessionRelated.toString(),CALLSESSIONRELATED,String.class);
            xml.add(clt.areaType.toString(),AREATYPE,String.class);
            xml.add(clt.reportingInterval,REPORTINGINTERVAL,Integer.class);
            xml.add(clt.dataCodingScheme,DATACODINGSCHEME,Integer.class);
        }

        public void read(InputElement xml, TestMapLcsClientConfigurationData clt) throws XMLStreamException {


            //return;
            clt.reportingAmmount = xml.getAttribute(REPORTINGAMMOUNT).toInt();


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
            String locationEstimateTypeTmp = (String) xml.get(LOCATIONESTIMATETYPE,String.class);
            clt.locationEstimateType = LocationEstimateType.valueOf(locationEstimateTypeTmp);
            Integer lcsServiceTypeID = (Integer) xml.get(LCSSERVICETYPEID,Integer.class);
            clt.lcsServiceTypeID = lcsServiceTypeID;
            String molr_tmp = (String) xml.get(MOLRSHORTCIRCUITINDICATOR,String.class);
            clt.moLrShortCircuitIndicator = Boolean.parseBoolean(molr_tmp);
            String lcsClientType = (String) xml.get(LCSCLIENTTYPE,String.class);
            clt.lcsClientType = LCSClientType.valueOf(lcsClientType);
            String codeWordUSSDString = (String) xml.get(CODEWORDUSSDSTRING,String.class);
            clt.codeWordUSSDString=codeWordUSSDString;
            String callSessionUnrelated = (String) xml.get(CALLSESSIONUNRELATED,String.class);
            clt.callSessionUnrelated=PrivacyCheckRelatedAction.valueOf(callSessionUnrelated);
            String callSessionRelated = (String) xml.get(CALLSESSIONRELATED,String.class);
            clt.callSessionRelated=PrivacyCheckRelatedAction.valueOf(callSessionRelated);
            String areaType = (String) xml.get(AREATYPE,String.class);
            clt.areaType=AreaType.valueOf(areaType);
            Integer reportingInterval = (Integer)xml.get(REPORTINGINTERVAL,Integer.class);
            clt.reportingInterval=reportingInterval;
            Integer dataCodingScheme = (Integer)xml.get(DATACODINGSCHEME,Integer.class);
            clt.dataCodingScheme=dataCodingScheme;

        }
    };

}
