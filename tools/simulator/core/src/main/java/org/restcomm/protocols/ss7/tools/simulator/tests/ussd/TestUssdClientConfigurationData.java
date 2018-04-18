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

package org.restcomm.protocols.ss7.tools.simulator.tests.ussd;

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.tools.simulator.tests.sms.SRIReaction;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdClientConfigurationData {

    protected static final String MSISDN_ADDRESS = "msisdnAddress";
    protected static final String MSISDN_ADDRESS_NATURE = "msisdnAddressNature";
    protected static final String MSISDN_NUMBERING_PLAN = "msisdnNumberingPlan";
    protected static final String DATA_CODING_SCHEME = "dataCodingScheme";
    protected static final String ALERTING_PATTERN = "alertingPattern";
    protected static final String USSD_CLIENT_ACTION = "ussdClientAction";
    protected static final String AUTO_REQUEST_STRING = "autoRequestString";
    protected static final String AUTO_RESPONSE_STRING = "autoResponseString";
    protected static final String MAX_CONCURENT_DIALOGS = "maxConcurrentDialogs";
    protected static final String ONE_NOTIFICATION_FOR_100_DIALOGS = "oneNotificationFor100Dialogs";
    protected static final String AUTO_RESPONSE_ON_UNSTRUCTURED_SS_REQUESTS = "autoResponseOnUnstructuredSSRequests";
    protected static final String SRI_RESPONSE_IMSI = "sriResponseImsi";
    protected static final String SRI_RESPONSE_VLR = "sriResponseVlr";
    protected static final String SRI_REACTION = "sriReaction";
    protected static final String RETURN_20_PERS_DELIVERY_ERRORS = "return20PersDeliveryErrors";

    protected String msisdnAddress = "";
    protected AddressNature msisdnAddressNature = AddressNature.international_number;
    protected NumberingPlan msisdnNumberingPlan = NumberingPlan.ISDN;
    protected int dataCodingScheme = 0x0F;
    protected int alertingPattern = -1;
    protected String sriResponseImsi = "";
    protected String sriResponseVlr = "";
    protected SRIReaction sriReaction = new SRIReaction(SRIReaction.VAL_RETURN_SUCCESS);
    protected boolean return20PersDeliveryErrors = false;

    protected UssdClientAction ussdClientAction = new UssdClientAction(UssdClientAction.VAL_MANUAL_OPERATION);
    protected String autoRequestString = "???";
    protected String autoResponseString = "";
    protected int maxConcurrentDialogs = 10;
    protected boolean oneNotificationFor100Dialogs = false;
    protected boolean autoResponseOnUnstructuredSSRequests = false;

    public String getMsisdnAddress() {
        return msisdnAddress;
    }

    public void setMsisdnAddress(String msisdnAddress) {
        this.msisdnAddress = msisdnAddress;
    }

    public AddressNature getMsisdnAddressNature() {
        return msisdnAddressNature;
    }

    public void setMsisdnAddressNature(AddressNature msisdnAddressNature) {
        this.msisdnAddressNature = msisdnAddressNature;
    }

    public NumberingPlan getMsisdnNumberingPlan() {
        return msisdnNumberingPlan;
    }

    public void setMsisdnNumberingPlan(NumberingPlan msisdnNumberingPlan) {
        this.msisdnNumberingPlan = msisdnNumberingPlan;
    }

    public int getDataCodingScheme() {
        return dataCodingScheme;
    }

    public void setDataCodingScheme(int dataCodingScheme) {
        this.dataCodingScheme = dataCodingScheme;
    }

    public int getAlertingPattern() {
        return alertingPattern;
    }

    public void setAlertingPattern(int alertingPattern) {
        this.alertingPattern = alertingPattern;
    }

    public UssdClientAction getUssdClientAction() {
        return ussdClientAction;
    }

    public void setUssdClientAction(UssdClientAction ussdClientAction) {
        this.ussdClientAction = ussdClientAction;
    }

    public String getAutoRequestString() {
        return autoRequestString;
    }

    public void setAutoRequestString(String autoRequestString) {
        this.autoRequestString = autoRequestString;
    }

    public String getAutoResponseString() {
        return autoResponseString;
    }

    public void setAutoResponseString(String autoResponseString) {
        this.autoResponseString = autoResponseString;
    }

    public int getMaxConcurrentDialogs() {
        return maxConcurrentDialogs;
    }

    public void setMaxConcurrentDialogs(int maxConcurrentDialogs) {
        this.maxConcurrentDialogs = maxConcurrentDialogs;
    }

    public boolean isOneNotificationFor100Dialogs() {
        return oneNotificationFor100Dialogs;
    }

    public void setOneNotificationFor100Dialogs(boolean oneNotificationFor100Dialogs) {
        this.oneNotificationFor100Dialogs = oneNotificationFor100Dialogs;
    }

    public boolean isAutoResponseOnUnstructuredSSRequests() {
        return autoResponseOnUnstructuredSSRequests;
    }

    public void setAutoResponseOnUnstructuredSSRequests(boolean autoResponseOnUnstructuredSSRequests) {
        this.autoResponseOnUnstructuredSSRequests = autoResponseOnUnstructuredSSRequests;
    }

    public String getSriResponseImsi() {
        return sriResponseImsi;
    }

    public void setSriResponseImsi(String sriResponseImsi) {
        this.sriResponseImsi = sriResponseImsi;
    }

    public String getSriResponseVlr() {
        return sriResponseVlr;
    }

    public void setSriResponseVlr(String sriResponseVlr) {
        this.sriResponseVlr = sriResponseVlr;
    }

    public SRIReaction getSRIReaction() {
        return sriReaction;
    }

    public void setSRIReaction(SRIReaction val) {
        sriReaction = val;
    }

    public boolean isReturn20PersDeliveryErrors() {
        return return20PersDeliveryErrors;
    }

    public void setReturn20PersDeliveryErrors(boolean val) {
        this.return20PersDeliveryErrors = val;
    }

    protected static final XMLFormat<TestUssdClientConfigurationData> XML = new XMLFormat<TestUssdClientConfigurationData>(
            TestUssdClientConfigurationData.class) {

        public void write(TestUssdClientConfigurationData clt, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(DATA_CODING_SCHEME, clt.dataCodingScheme);
            xml.setAttribute(ALERTING_PATTERN, clt.alertingPattern);
            xml.setAttribute(MAX_CONCURENT_DIALOGS, clt.maxConcurrentDialogs);
            xml.setAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS, clt.oneNotificationFor100Dialogs);
            xml.setAttribute(AUTO_RESPONSE_ON_UNSTRUCTURED_SS_REQUESTS, clt.autoResponseOnUnstructuredSSRequests);
            xml.setAttribute(RETURN_20_PERS_DELIVERY_ERRORS, clt.return20PersDeliveryErrors);

            xml.add(clt.msisdnAddress, MSISDN_ADDRESS, String.class);
            xml.add(clt.msisdnAddressNature.toString(), MSISDN_ADDRESS_NATURE, String.class);
            xml.add(clt.msisdnNumberingPlan.toString(), MSISDN_NUMBERING_PLAN, String.class);
            xml.add(clt.sriResponseImsi, SRI_RESPONSE_IMSI, String.class);
            xml.add(clt.sriResponseVlr, SRI_RESPONSE_VLR, String.class);
            xml.add(clt.sriReaction.toString(), SRI_REACTION, String.class);

            xml.add(clt.ussdClientAction.toString(), USSD_CLIENT_ACTION, String.class);
            xml.add(clt.autoRequestString, AUTO_REQUEST_STRING, String.class);
            xml.add(clt.autoResponseString, AUTO_RESPONSE_STRING, String.class);
        }

        public void read(InputElement xml, TestUssdClientConfigurationData clt) throws XMLStreamException {
            clt.dataCodingScheme = xml.getAttribute(DATA_CODING_SCHEME).toInt();
            clt.alertingPattern = xml.getAttribute(ALERTING_PATTERN).toInt();
            clt.maxConcurrentDialogs = xml.getAttribute(MAX_CONCURENT_DIALOGS).toInt();
            clt.oneNotificationFor100Dialogs = xml.getAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS).toBoolean();
            CharArray chArr = xml.getAttribute(AUTO_RESPONSE_ON_UNSTRUCTURED_SS_REQUESTS);
            if (chArr != null)
                clt.autoResponseOnUnstructuredSSRequests = chArr.toBoolean();
            chArr = xml.getAttribute(RETURN_20_PERS_DELIVERY_ERRORS);
            if (chArr != null)
                clt.return20PersDeliveryErrors = chArr.toBoolean();

            clt.msisdnAddress = (String) xml.get(MSISDN_ADDRESS, String.class);
            String an = (String) xml.get(MSISDN_ADDRESS_NATURE, String.class);
            clt.msisdnAddressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(MSISDN_NUMBERING_PLAN, String.class);
            clt.msisdnNumberingPlan = NumberingPlan.valueOf(np);
            clt.sriResponseImsi = (String) xml.get(SRI_RESPONSE_IMSI, String.class);
            clt.sriResponseVlr = (String) xml.get(SRI_RESPONSE_VLR, String.class);
            String sriR = (String) xml.get(SRI_REACTION, String.class);
            clt.sriReaction = SRIReaction.createInstance(sriR);

            String uca = (String) xml.get(USSD_CLIENT_ACTION, String.class);
            clt.ussdClientAction = UssdClientAction.createInstance(uca);
            clt.autoRequestString = (String) xml.get(AUTO_REQUEST_STRING, String.class);
            clt.autoResponseString = (String) xml.get(AUTO_RESPONSE_STRING, String.class);
        }
    };

}
