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

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;

import org.restcomm.protocols.ss7.map.service.lsm.ExtGeographicalInformationImpl;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */
public class TestLcsServerConfigurationData {

  protected static final String SRIforLCS_REACTION = "sriForLCSReaction";
  protected SRIforLCSReaction sriForLCSReaction = new SRIforLCSReaction(SRIforLCSReaction.VAL_RETURN_SUCCESS);
  protected static final String PSL_REACTION = "pslReaction";
  protected PSLReaction pslReaction = new PSLReaction(PSLReaction.VAL_RETURN_SUCCESS);
  protected static final String SLR_REACTION = "slrReaction";
  protected SLRReaction slrReaction = new SLRReaction(SLRReaction.VAL_RETURN_SUCCESS);

  protected static final String ADDRESS_NATURE = "addressNature";
  protected static final String NUMBERING_PLAN_TYPE = "numberingPlanType";
  protected static final String NUMBERING_PLAN = "numberingPlan";
  protected static final String MLC_NUMBER = "mlcNumber";
  protected static final String IMSI = "imsi";
  private static final String LMSI = "lmsi";
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
  protected static final String OCCURRENCE_INFO = "occurrenceInfo";
  protected static final String INTERVAL_TIME = "intervalTime";
  protected static final String MSISDN = "msisdn";
  protected static final String LOCATION_ESTIMATE = "locationEstimate";
  protected static final String LATITUDE = "latitude";
  protected static final String LONGITUDE = "longitude";
  protected static final String TYPE_OF_SHAPE = "typeOfShape";
  protected static final String LOCATIONESTIMATETYPE = "locationEstimateType";
  protected static final String LCSSERVICETYPEID = "lcsServiceTypeID";
  protected static final String MOLRSHORTCIRCUITINDICATOR = "moLrShortCircuitIndicator";
  protected static final String LCSCLIENTTYPE = "lcsClientType";
  protected static final String CODEWORDUSSDSTRING = "codeWordUSSDString";
  protected static final String CALLSESSIONUNRELATED = "callSessionUnrelated";
  protected static final String CALLSESSIONRELATED = "callSessionRelated";
  protected static final String AREATYPE = "areaType";
  protected static final String REPORTINGAMOUNT = "reportingAmount";
  protected static final String REPORTINGINTERVAL = "reportingInterval";
  protected static final String DATACODINGSCHEME = "dataCodingScheme";
  protected static final String NA_ESRD_ADDRESS = "na_esrd_address";

  private MAPParameterFactory mapParameterFactory;
  private AddressNature addressNature = AddressNature.international_number;
  private NumberingPlan numberingPlanType = NumberingPlan.ISDN;
  private String numberingPlan = "1";
  //private ISDNAddressString mlcNumber =  mapParameterFactory.createISDNAddressString(addressNature,numberingPlanType, "5980482910");
  private String mlcNumber = "5980482910";
  private String imsi = "748010192837465";
  private String lmsi = "09876543";
  private String networkNodeNumber = "598048";
  private String msisdn = "59899077937";
  private String imei = "354449063537030";
  private String hgmlcAddress = "200.10.0.1";
  private Integer mcc = 748;
  private Integer mnc = 01;
  private Integer lac = 79010;
  private Integer cellId = 222;
  private Integer lcsReferenceNumber = 921;
  private LCSEvent lcsEvent = LCSEvent.emergencyCallOrigination;
  private TypeOfShape typeOfShape = TypeOfShape.EllipsoidArc;
  private TypeOfShapeEnumerated typeOfShapeEnumerated = TypeOfShapeEnumerated.createInstance("EllipsoidArc");
  private Double latitude = 34.789123;
  private Double longitude = -124.902033;
  private Double uncertainty = 50.0;
  private Double uncertaintySemiMajorAxis = 40.0;
  private Double uncertaintySemiMinorAxis = 20.0;
  private Double angleOfMajorAxis = 30.0;
  private int confidence = 0;
  private int altitude = 1500;
  private Double uncertaintyAltitude = 500.0;
  private int innerRadius = 5;
  private Double uncertaintyRadius = 1.50;
  private Double offsetAngle = 20.0;
  private Double includedAngle = 20.0;
  private ExtGeographicalInformation locationEstimate;
  private Integer ageOfLocationEstimate = 1;

  {
    try {
      locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis,
          uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
    } catch (MAPException e) {
      e.printStackTrace();
    }
  }

  private LocationEstimateType locationEstimateType = LocationEstimateType.currentLocation;
  private Integer lcsServiceTypeID = 0;
  private boolean moLrShortCircuitIndicator = false;
  private LCSClientType lcsClientType = LCSClientType.emergencyServices;
  private String codeWordUSSDString = "*911#";
  private PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
  private PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
  private AreaType areaType = AreaType.countryCode;
  private OccurrenceInfo occurrenceInfo = OccurrenceInfo.oneTimeEvent;
  private Integer intervalTime = 60;
  private Integer reportingAmount = 10;
  private Integer reportingInterval = 10;
  private Integer dataCodingScheme = 15;
  private String naESRDAddress = "11114444";

  public SRIforLCSReaction getSriForLCSReaction() {
    return sriForLCSReaction;
  }

  public void setSriForLCSReaction(SRIforLCSReaction sriForLCSReaction) {
    this.sriForLCSReaction = sriForLCSReaction;
  }

  public PSLReaction getPslReaction() {
    return pslReaction;
  }

  public void setPslReaction(PSLReaction pslReaction) {
    this.pslReaction = pslReaction;
  }

  public SLRReaction getSlrReaction() {
    return slrReaction;
  }

  public void setSlrReaction(SLRReaction slrReaction) {
    this.slrReaction = slrReaction;
  }

  public String getMlcNumber() {
    return mlcNumber;
  }

  public void setMlcNumber(String mlcNumber) {
    this.mlcNumber = mlcNumber;
  }

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

  public void setLcsClientType(LCSClientType val) {
    this.lcsClientType = val;
  }

  public ExtGeographicalInformation getLocationEstimate() {
    return this.locationEstimate;
  }

  public void setLocationEstimate(ExtGeographicalInformation extGeographicalInformation) {
    this.locationEstimate = extGeographicalInformation;
  }

  public void setLocationEstimateType(LocationEstimateType locType) {
    this.locationEstimateType = locType;
  }

  public LocationEstimateType getLocationEstimateType() {
    return locationEstimateType;
  }

  public Integer getLcsServiceTypeID() {
    return lcsServiceTypeID;
  }

  public void setLcsServiceTypeID(Integer lcsServiceTypeID) {
    this.lcsServiceTypeID = lcsServiceTypeID;
  }

  public boolean getMoLrShortCircuitIndicator() {
    return this.moLrShortCircuitIndicator;
  }

  public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator) {
    this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
  }

  public void setCallSessionUnrelated(PrivacyCheckRelatedAction val) {
    this.callSessionUnrelated = val;
  }

  public PrivacyCheckRelatedAction getCallSessionUnrelated() {
    return callSessionUnrelated;
  }

  public void setCallSessionRelated(PrivacyCheckRelatedAction val) {
    this.callSessionRelated = val;
  }

  public PrivacyCheckRelatedAction getCallSessionRelated() {
    return callSessionRelated;
  }

  public void setAreaType(AreaType areaType) {
    this.areaType = areaType;
  }

  public AreaType getAreaType() {
    return areaType;
  }

  public OccurrenceInfo getOccurrenceInfo() {
    return this.occurrenceInfo;
  }

  public void setOccurrenceInfo(OccurrenceInfo occurrenceInfo) {
    this.occurrenceInfo = occurrenceInfo;
  }

  public Integer getIntervalTime() {
    return this.intervalTime;
  }

  public void setIntervalTime(Integer intervalTime) {
    this.intervalTime = intervalTime;
  }

  public void setReportingAmount(Integer val) {
    this.reportingAmount = val;
  }

  public Integer getReportingAmount() {
    return reportingAmount;
  }

  public void setReportingInterval(Integer val) {
    this.reportingInterval = val;
  }

  public Integer getReportingInterval() {
    return reportingInterval;
  }

  public void setDataCodingScheme(Integer val) {
    this.dataCodingScheme = val;
  }

  public Integer getDataCodingScheme() {
    return dataCodingScheme;
  }

  public String getLMSI() {
    return this.lmsi;
  }

  public void setLMSI(String lmsi) {
    this.lmsi = lmsi;
  }

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
    this.numberingPlan = numberingPlan;
  }

  public String getNumberingPlan() {
    return numberingPlan;
  }

  public void setIMSI(String data) {
    this.imsi = data;
  }

  public String getIMSI() {
    return imsi;
  }

  public void setNetworkNodeNumber(String data) {
    this.networkNodeNumber = data;
  }

  public String getNetworkNodeNumber() {
    return networkNodeNumber;
  }

  public String getImsi() {
    return imsi;
  }

  public void setImsi(String imsi) {
    this.imsi = imsi;
  }

  public String getLmsi() {
    return lmsi;
  }

  public void setLmsi(String lmsi) {
    this.lmsi = lmsi;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public String getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public String getHgmlcAddress() {
    return hgmlcAddress;
  }

  public void setHgmlcAddress(String hgmlcAddress) {
    this.hgmlcAddress = hgmlcAddress;
  }

  public Integer getMcc() {
    return mcc;
  }

  public void setMcc(Integer mcc) {
    this.mcc = mcc;
  }

  public Integer getMnc() {
    return mnc;
  }

  public void setMnc(Integer mnc) {
    this.mnc = mnc;
  }

  public Integer getLac() {
    return lac;
  }

  public void setLac(Integer lac) {
    this.lac = lac;
  }

  public Integer getLcsReferenceNumber() {
    return lcsReferenceNumber;
  }

  public void setLcsReferenceNumber(Integer lcsReferenceNumber) {
    this.lcsReferenceNumber = lcsReferenceNumber;
  }

  public LCSEvent getLcsEvent() {
    return lcsEvent;
  }

  public void setLcsEvent(LCSEvent lcsEvent) {
    this.lcsEvent = lcsEvent;
  }

  public TypeOfShape getTypeOfShape() {
    return typeOfShape;
  }

  public void setTypeOfShape(TypeOfShape typeOfShape) {
    this.typeOfShape = typeOfShape;
  }

  public TypeOfShapeEnumerated getTypeOfShapeEnumerated() {
    return typeOfShapeEnumerated;
  }

  public void setTypeOfShapeEnumerated(TypeOfShapeEnumerated typeOfShapeEnumerated) {
    this.typeOfShapeEnumerated = typeOfShapeEnumerated;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getUncertainty() {
    return uncertainty;
  }

  public void setUncertainty(Double uncertainty) {
    this.uncertainty = uncertainty;
  }

  public Double getUncertaintySemiMajorAxis() {
    return uncertaintySemiMajorAxis;
  }

  public void setUncertaintySemiMajorAxis(Double uncertaintySemiMajorAxis) {
    this.uncertaintySemiMajorAxis = uncertaintySemiMajorAxis;
  }

  public Double getUncertaintySemiMinorAxis() {
    return uncertaintySemiMinorAxis;
  }

  public void setUncertaintySemiMinorAxis(Double uncertaintySemiMinorAxis) {
    this.uncertaintySemiMinorAxis = uncertaintySemiMinorAxis;
  }

  public Double getAngleOfMajorAxis() {
    return angleOfMajorAxis;
  }

  public void setAngleOfMajorAxis(Double angleOfMajorAxis) {
    this.angleOfMajorAxis = angleOfMajorAxis;
  }

  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }

  public int getAltitude() {
    return altitude;
  }

  public void setAltitude(int altitude) {
    this.altitude = altitude;
  }

  public Double getUncertaintyAltitude() {
    return uncertaintyAltitude;
  }

  public void setUncertaintyAltitude(Double uncertaintyAltitude) {
    this.uncertaintyAltitude = uncertaintyAltitude;
  }

  public int getInnerRadius() {
    return innerRadius;
  }

  public void setInnerRadius(int innerRadius) {
    this.innerRadius = innerRadius;
  }

  public Double getUncertaintyRadius() {
    return uncertaintyRadius;
  }

  public void setUncertaintyRadius(Double uncertaintyRadius) {
    this.uncertaintyRadius = uncertaintyRadius;
  }

  public Double getOffsetAngle() {
    return offsetAngle;
  }

  public void setOffsetAngle(Double offsetAngle) {
    this.offsetAngle = offsetAngle;
  }

  public Double getIncludedAngle() {
    return includedAngle;
  }

  public void setIncludedAngle(Double includedAngle) {
    this.includedAngle = includedAngle;
  }

  public boolean isMoLrShortCircuitIndicator() {
    return moLrShortCircuitIndicator;
  }

  public void setCodeWordUSSDString(String codeWordUSSDString) {
    this.codeWordUSSDString = codeWordUSSDString;
  }

  public String getCodeWordUSSDString() {
    return this.codeWordUSSDString;
  }

  protected static final XMLFormat<TestLcsServerConfigurationData> XML = new XMLFormat<TestLcsServerConfigurationData>(TestLcsServerConfigurationData.class) {

    public void write(TestLcsServerConfigurationData clt, OutputElement xml) throws XMLStreamException {

      xml.setAttribute(REPORTINGAMOUNT, clt.reportingAmount);

      xml.add(clt.addressNature.toString(), ADDRESS_NATURE, String.class);
      xml.add(clt.numberingPlanType.toString(), NUMBERING_PLAN_TYPE, String.class);
      xml.add(clt.numberingPlan, NUMBERING_PLAN, String.class);

      xml.add(clt.mlcNumber, MLC_NUMBER, String.class);

      xml.add(clt.msisdn, MSISDN, String.class);
      xml.add(clt.imsi, IMSI, String.class);
      xml.add(clt.lmsi, LMSI, String.class);

      xml.add(clt.hgmlcAddress, HGMLC_ADDRESS, String.class);

      xml.add(clt.networkNodeNumber, NETWORK_NODE_NUMBER_ADDRESS, String.class);

      xml.add(clt.latitude.toString(), LATITUDE, String.class);
      xml.add(clt.longitude.toString(), LONGITUDE, String.class);
      xml.add(clt.ageOfLocationEstimate, AGE_OF_LOCATION_ESTIMATE, Integer.class);

      xml.add(clt.imei, IMEI, String.class);

      xml.add(clt.lcsReferenceNumber, LCS_REFERENCE_NUMBER, Integer.class);

      xml.add(clt.lcsServiceTypeID, LCSSERVICETYPEID, Integer.class);

      xml.add(clt.mcc, MCC, Integer.class);
      xml.add(clt.mnc, MNC, Integer.class);
      xml.add(clt.lac, LAC, Integer.class);
      xml.add(clt.cellId, CELL_ID, Integer.class);

      xml.add(clt.reportingInterval, REPORTINGINTERVAL, Integer.class);

      xml.add(clt.dataCodingScheme, DATACODINGSCHEME, Integer.class);

      xml.add(clt.naESRDAddress, NA_ESRD_ADDRESS, String.class);

      /*xml.add(clt.areaType.toString(), AREATYPE, String.class);
      xml.add(clt.occurrenceInfo.toString(), OCCURRENCE_INFO, String.class);
      xml.add(clt.intervalTime.toString(), INTERVAL_TIME, String.class);
      xml.add(clt.lcsClientType.toString(), LCSCLIENTTYPE, String.class);
      xml.add(clt.locationEstimate.toString(), LOCATION_ESTIMATE, String.class);
      xml.add(clt.lcsEvent.toString(), LCS_EVENT, String.class);
      xml.add(clt.locationEstimateType.toString(), LOCATIONESTIMATETYPE, String.class);
      xml.add(Boolean.toString(clt.moLrShortCircuitIndicator), MOLRSHORTCIRCUITINDICATOR, String.class);
      xml.add(clt.codeWordUSSDString.toString(), CODEWORDUSSDSTRING, String.class);
      xml.add(clt.callSessionUnrelated.toString(), CALLSESSIONUNRELATED, String.class);
      xml.add(clt.callSessionRelated.toString(), CALLSESSIONRELATED, String.class);*/

    }

    public void read(InputElement xml, TestLcsServerConfigurationData clt) throws XMLStreamException {

      //return;
      String an = xml.get(ADDRESS_NATURE, String.class);
      clt.addressNature = AddressNature.valueOf(an);
      String npt = xml.get(NUMBERING_PLAN_TYPE, String.class);
      clt.numberingPlanType = NumberingPlan.valueOf(npt);
      String np = xml.get(NUMBERING_PLAN, String.class);
      clt.numberingPlan = np;

      String mlcNum = xml.get(MLC_NUMBER, String.class);
      clt.mlcNumber = mlcNum;

      String msisdn = xml.get(MSISDN, String.class);
      clt.msisdn = msisdn;
      String imsi = xml.get(IMSI, String.class);
      clt.imsi = imsi;
      String lmsi = xml.get(LMSI, String.class);
      clt.lmsi = lmsi;

      String hGmlcAddress = xml.get(HGMLC_ADDRESS, String.class);
      clt.hgmlcAddress = hGmlcAddress;

      String networkNodeNumberAddress = xml.get(NETWORK_NODE_NUMBER_ADDRESS, String.class);
      clt.networkNodeNumber = networkNodeNumberAddress;

      Double latitude = xml.get(LATITUDE, Double.class);
      clt.latitude = latitude;
      Double longitude = xml.get(LONGITUDE, Double.class);
      clt.longitude = longitude;

      Integer ageOfLocationEstimate = xml.get(AGE_OF_LOCATION_ESTIMATE, Integer.class);
      clt.ageOfLocationEstimate = ageOfLocationEstimate;

      String imei = xml.get(IMEI, String.class);
      clt.imei = imei;

      Integer lcsReferenceNumber = xml.get(LCS_REFERENCE_NUMBER, Integer.class);
      clt.lcsReferenceNumber = lcsReferenceNumber;

      Integer lcsServiceTypeID = xml.get(LCSSERVICETYPEID, Integer.class);
      clt.lcsServiceTypeID = lcsServiceTypeID;

      Integer mcc = xml.get(MCC, Integer.class);
      clt.mcc = mcc;
      Integer mnc = xml.get(MNC, Integer.class);
      clt.mnc = mnc;
      Integer lac = xml.get(LAC, Integer.class);
      clt.lac = lac;
      Integer cellId = xml.get(CELL_ID, Integer.class);
      clt.cellId = cellId;

      String molr_tmp = xml.get(MOLRSHORTCIRCUITINDICATOR, String.class);
      clt.moLrShortCircuitIndicator = Boolean.parseBoolean(molr_tmp);

      String codeWordUSSDString = xml.get(CODEWORDUSSDSTRING, String.class);
      clt.codeWordUSSDString = codeWordUSSDString;

      Integer reportingInterval = xml.get(REPORTINGINTERVAL, Integer.class);
      clt.reportingInterval = reportingInterval;

      Integer dataCodingScheme = xml.get(DATACODINGSCHEME, Integer.class);
      clt.dataCodingScheme = dataCodingScheme;

      Integer reportingAmount = xml.get(REPORTINGAMOUNT, Integer.class);
      clt.reportingAmount = reportingAmount;

      String naESRDAddress = xml.get(NA_ESRD_ADDRESS, String.class);
      clt.naESRDAddress = naESRDAddress;


/*
      String typeOfShape = (String) xml.get(TYPE_OF_SHAPE, String.class);
      clt.typeOfShape = TypeOfShape.valueOf(typeOfShape);
      String lcsEvent = (String) xml.get(LCS_EVENT, String.class);
      clt.lcsEvent = LCSEvent.valueOf(lcsEvent);
      String locationEstimateTypeTmp = (String) xml.get(LOCATIONESTIMATETYPE, String.class);
      clt.locationEstimateType = LocationEstimateType.valueOf(locationEstimateTypeTmp);
      String lcsClientType = (String) xml.get(LCSCLIENTTYPE, String.class);
      clt.lcsClientType = LCSClientType.valueOf(lcsClientType);
      String callSessionUnrelated = (String) xml.get(CALLSESSIONUNRELATED, String.class);
      clt.callSessionUnrelated = PrivacyCheckRelatedAction.valueOf(callSessionUnrelated);
      String callSessionRelated = (String) xml.get(CALLSESSIONRELATED, String.class);
      clt.callSessionRelated = PrivacyCheckRelatedAction.valueOf(callSessionRelated);
      String areaType = (String) xml.get(AREATYPE, String.class);
      clt.areaType = AreaType.valueOf(areaType);
      String occurrenceInfo = (String) xml.get(OCCURRENCE_INFO, String.class);
      clt.occurrenceInfo = OccurrenceInfo.valueOf(occurrenceInfo);
      Integer intervalTime = (Integer) xml.get(INTERVAL_TIME, Integer.class);
      clt.intervalTime = intervalTime;
*/

    }
  };

}