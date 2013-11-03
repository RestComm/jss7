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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdServerConfigurationData {

    protected static final String MSISDN_ADDRESS = "msisdnAddress";
    protected static final String MSISDN_ADDRESS_NATURE = "msisdnAddressNature";
    protected static final String MSISDN_NUMBERING_PLAN = "msisdnNumberingPlan";
    protected static final String DATA_CODING_SCHEME = "dataCodingScheme";
    protected static final String ALERTING_PATTERN = "alertingPattern";
    protected static final String PROCESS_SS_REQUEST_ACTION = "processSsRequestAction";
    protected static final String AUTO_RESPONSE_STRING = "autoResponseString";
    protected static final String AUTO_UNSTRUCTURED_SS_REQUEST_STRING = "autoUnstructured_SS_RequestString";
    protected static final String ONE_NOTIFICATION_FOR_100_DIALOGS = "oneNotificationFor100Dialogs";

    protected String msisdnAddress = "";
    protected AddressNature msisdnAddressNature = AddressNature.international_number;
    protected NumberingPlan msisdnNumberingPlan = NumberingPlan.ISDN;
    protected int dataCodingScheme = 0x0F;
    protected int alertingPattern = -1;
    protected ProcessSsRequestAction processSsRequestAction = new ProcessSsRequestAction(
            ProcessSsRequestAction.VAL_MANUAL_RESPONSE);
    protected String autoResponseString = "";
    protected String autoUnstructured_SS_RequestString = "";
    protected boolean oneNotificationFor100Dialogs = false;

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

    public ProcessSsRequestAction getProcessSsRequestAction() {
        return processSsRequestAction;
    }

    public void setProcessSsRequestAction(ProcessSsRequestAction processSsRequestAction) {
        this.processSsRequestAction = processSsRequestAction;
    }

    public String getAutoResponseString() {
        return autoResponseString;
    }

    public void setAutoResponseString(String autoResponseString) {
        this.autoResponseString = autoResponseString;
    }

    public String getAutoUnstructured_SS_RequestString() {
        return autoUnstructured_SS_RequestString;
    }

    public void setAutoUnstructured_SS_RequestString(String autoUnstructured_SS_RequestString) {
        this.autoUnstructured_SS_RequestString = autoUnstructured_SS_RequestString;
    }

    public boolean isOneNotificationFor100Dialogs() {
        return oneNotificationFor100Dialogs;
    }

    public void setOneNotificationFor100Dialogs(boolean oneNotificationFor100Dialogs) {
        this.oneNotificationFor100Dialogs = oneNotificationFor100Dialogs;
    }

    protected static final XMLFormat<TestUssdServerConfigurationData> XML = new XMLFormat<TestUssdServerConfigurationData>(
            TestUssdServerConfigurationData.class) {

        public void write(TestUssdServerConfigurationData srv, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(DATA_CODING_SCHEME, srv.dataCodingScheme);
            xml.setAttribute(ALERTING_PATTERN, srv.alertingPattern);
            xml.setAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS, srv.oneNotificationFor100Dialogs);

            xml.add(srv.msisdnAddress, MSISDN_ADDRESS, String.class);
            xml.add(srv.autoResponseString, AUTO_RESPONSE_STRING, String.class);
            xml.add(srv.autoUnstructured_SS_RequestString, AUTO_UNSTRUCTURED_SS_REQUEST_STRING, String.class);

            xml.add(srv.msisdnAddressNature.toString(), MSISDN_ADDRESS_NATURE, String.class);
            xml.add(srv.msisdnNumberingPlan.toString(), MSISDN_NUMBERING_PLAN, String.class);
            xml.add(srv.processSsRequestAction.toString(), PROCESS_SS_REQUEST_ACTION, String.class);
        }

        public void read(InputElement xml, TestUssdServerConfigurationData srv) throws XMLStreamException {
            srv.dataCodingScheme = xml.getAttribute(DATA_CODING_SCHEME).toInt();
            srv.alertingPattern = xml.getAttribute(ALERTING_PATTERN).toInt();
            srv.oneNotificationFor100Dialogs = xml.getAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS).toBoolean();

            srv.msisdnAddress = (String) xml.get(MSISDN_ADDRESS, String.class);
            srv.autoResponseString = (String) xml.get(AUTO_RESPONSE_STRING, String.class);
            srv.autoUnstructured_SS_RequestString = (String) xml.get(AUTO_UNSTRUCTURED_SS_REQUEST_STRING, String.class);

            String an = (String) xml.get(MSISDN_ADDRESS_NATURE, String.class);
            srv.msisdnAddressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(MSISDN_NUMBERING_PLAN, String.class);
            srv.msisdnNumberingPlan = NumberingPlan.valueOf(np);
            String ss_act = (String) xml.get(PROCESS_SS_REQUEST_ACTION, String.class);
            srv.processSsRequestAction = ProcessSsRequestAction.createInstance(ss_act);
        }
    };

}
