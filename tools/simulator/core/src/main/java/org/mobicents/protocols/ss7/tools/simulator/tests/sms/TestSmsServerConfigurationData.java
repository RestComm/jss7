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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsServerConfigurationData {

    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN = "numberingPlan";
    protected static final String SERVICE_CENTER_ADDRESS = "serviceCenterAddress";
    protected static final String MAP_PROTOCOL_VERSION = "mapProtocolVersion";
    protected static final String HLR_SSN = "hlrSsn";
    protected static final String VLR_SSN = "vlrSsn";
    protected static final String TYPE_OF_NUMBER = "typeOfNumber";
    protected static final String NUMBERING_PLAN_IDENTIFICATION = "numberingPlanIdentification";
    protected static final String SMS_CODING_TYPE = "smsCodingType";
    protected static final String SEND_SRSMDS_IF_ERROR = "sendSrsmdsIfError";
    protected static final String GPRS_SUPPORT_INDICATOR = "gprsSupportIndicator";

    protected AddressNature addressNature = AddressNature.international_number;
    protected NumberingPlan numberingPlan = NumberingPlan.ISDN;
    protected String serviceCenterAddress = "";
    protected MapProtocolVersion mapProtocolVersion = new MapProtocolVersion(MapProtocolVersion.VAL_MAP_V3);
    protected int hlrSsn = 6;
    protected int vlrSsn = 8;
    protected TypeOfNumber typeOfNumber = TypeOfNumber.InternationalNumber;
    protected NumberingPlanIdentification numberingPlanIdentification = NumberingPlanIdentification.ISDNTelephoneNumberingPlan;
    protected SmsCodingType smsCodingType = new SmsCodingType(SmsCodingType.VAL_GSM7);
    protected boolean sendSrsmdsIfError = false;
    protected boolean gprsSupportIndicator = false;

    public AddressNature getAddressNature() {
        return addressNature;
    }

    public void setAddressNature(AddressNature addressNature) {
        this.addressNature = addressNature;
    }

    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    public String getServiceCenterAddress() {
        return serviceCenterAddress;
    }

    public void setServiceCenterAddress(String serviceCenterAddress) {
        this.serviceCenterAddress = serviceCenterAddress;
    }

    public MapProtocolVersion getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    public void setMapProtocolVersion(MapProtocolVersion mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public int getHlrSsn() {
        return hlrSsn;
    }

    public void setHlrSsn(int hlrSsn) {
        this.hlrSsn = hlrSsn;
    }

    public int getVlrSsn() {
        return vlrSsn;
    }

    public void setVlrSsn(int vlrSsn) {
        this.vlrSsn = vlrSsn;
    }

    public TypeOfNumber getTypeOfNumber() {
        return typeOfNumber;
    }

    public void setTypeOfNumber(TypeOfNumber typeOfNumber) {
        this.typeOfNumber = typeOfNumber;
    }

    public NumberingPlanIdentification getNumberingPlanIdentification() {
        return numberingPlanIdentification;
    }

    public void setNumberingPlanIdentification(NumberingPlanIdentification numberingPlanIdentification) {
        this.numberingPlanIdentification = numberingPlanIdentification;
    }

    public SmsCodingType getSmsCodingType() {
        return smsCodingType;
    }

    public void setSmsCodingType(SmsCodingType smsCodingType) {
        this.smsCodingType = smsCodingType;
    }

    public boolean isSendSrsmdsIfError() {
        return sendSrsmdsIfError;
    }

    public void setSendSrsmdsIfError(boolean val) {
        sendSrsmdsIfError = val;

    }

    public boolean isGprsSupportIndicator() {
        return gprsSupportIndicator;
    }

    public void setGprsSupportIndicator(boolean val) {
        gprsSupportIndicator = val;

    }

    protected static final XMLFormat<TestSmsServerConfigurationData> XML = new XMLFormat<TestSmsServerConfigurationData>(
            TestSmsServerConfigurationData.class) {

        public void write(TestSmsServerConfigurationData srv, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(HLR_SSN, srv.hlrSsn);
            xml.setAttribute(VLR_SSN, srv.vlrSsn);
            xml.setAttribute(SEND_SRSMDS_IF_ERROR, srv.sendSrsmdsIfError);
            xml.setAttribute(GPRS_SUPPORT_INDICATOR, srv.gprsSupportIndicator);

            xml.add(srv.serviceCenterAddress, SERVICE_CENTER_ADDRESS, String.class);

            xml.add(srv.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(srv.numberingPlan.toString(), NUMBERING_PLAN, String.class);
            xml.add(srv.mapProtocolVersion.toString(), MAP_PROTOCOL_VERSION, String.class);
            xml.add(srv.typeOfNumber.toString(), TYPE_OF_NUMBER, String.class);
            xml.add(srv.numberingPlanIdentification.toString(), NUMBERING_PLAN_IDENTIFICATION, String.class);
            xml.add(srv.smsCodingType.toString(), SMS_CODING_TYPE, String.class);
        }

        public void read(InputElement xml, TestSmsServerConfigurationData srv) throws XMLStreamException {
            srv.hlrSsn = xml.getAttribute(HLR_SSN).toInt();
            srv.vlrSsn = xml.getAttribute(VLR_SSN).toInt();
            srv.sendSrsmdsIfError = xml.getAttribute(SEND_SRSMDS_IF_ERROR).toBoolean();
            CharArray car = xml.getAttribute(GPRS_SUPPORT_INDICATOR);
            if (car != null)
                srv.gprsSupportIndicator = car.toBoolean();

            srv.serviceCenterAddress = (String) xml.get(SERVICE_CENTER_ADDRESS, String.class);

            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            srv.addressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            srv.numberingPlan = NumberingPlan.valueOf(np);
            String mpv = (String) xml.get(MAP_PROTOCOL_VERSION, String.class);
            srv.mapProtocolVersion = MapProtocolVersion.createInstance(mpv);
            String ton = (String) xml.get(TYPE_OF_NUMBER, String.class);
            srv.typeOfNumber = TypeOfNumber.valueOf(ton);
            String npi = (String) xml.get(NUMBERING_PLAN_IDENTIFICATION, String.class);
            srv.numberingPlanIdentification = NumberingPlanIdentification.valueOf(npi);
            String sct = (String) xml.get(SMS_CODING_TYPE, String.class);
            srv.smsCodingType = SmsCodingType.createInstance(sct);
        }
    };

}
