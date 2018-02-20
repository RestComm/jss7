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

package org.restcomm.protocols.ss7.tools.simulator.tests.ati;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
*
* @author sergey vetyutnev
*
*/
public class TestAtiClientConfigurationData {

    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN = "numberingPlan";
    protected static final String SUBSCRIBER_IDENTITY_TYPE = "subscriberIdentityType";
    protected static final String GET_LOCATION_INFORMATION = "getLocationInformation";
    protected static final String GET_SUBSCRIBER_STATE = "getSubscriberState";
    protected static final String GET_CURRENT_LOCATION = "getCurrentLocation";
    protected static final String GET_REQUESTED_DOMAIN = "getRequestedDomain";
    protected static final String GET_IMEI = "getImei";
    protected static final String GET_MS_CLASSMARK = "getMsClassmark";
    protected static final String GET_MNP_REQUESTED_INFO = "getMnpRequestedInfo";
    protected static final String GSM_SCF_ADDRESS = "gsmScfAddress";

    private AddressNature addressNature = AddressNature.international_number;
    private NumberingPlan numberingPlan = NumberingPlan.ISDN;
    private boolean subscriberIdentityTypeIsImsi = false;
    private boolean getLocationInformation = false;
    private boolean getSubscriberState = false;
    private boolean getCurrentLocation = false;
    private DomainType getRequestedDomain = null;
    private boolean getImei = false;
    private boolean getMsClassmark = false;
    private boolean getMnpRequestedInfo = false;
    private String gsmScfAddress = "000";

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

    public boolean isSubscriberIdentityTypeIsImsi() {
        return subscriberIdentityTypeIsImsi;
    }

    public void setSubscriberIdentityTypeIsImsi(boolean subscriberIdentityTypeIsImsi) {
        this.subscriberIdentityTypeIsImsi = subscriberIdentityTypeIsImsi;
    }

    public boolean isGetLocationInformation() {
        return getLocationInformation;
    }

    public void setGetLocationInformation(boolean getLocationInformation) {
        this.getLocationInformation = getLocationInformation;
    }

    public boolean isGetSubscriberState() {
        return getSubscriberState;
    }

    public void setGetSubscriberState(boolean getSubscriberState) {
        this.getSubscriberState = getSubscriberState;
    }

    public boolean isGetCurrentLocation() {
        return getCurrentLocation;
    }

    public void setGetCurrentLocation(boolean getCurrentLocation) {
        this.getCurrentLocation = getCurrentLocation;
    }

    public DomainType getGetRequestedDomain() {
        return getRequestedDomain;
    }

    public void setGetRequestedDomain(DomainType getRequestedDomain) {
        this.getRequestedDomain = getRequestedDomain;
    }

    public boolean isGetImei() {
        return getImei;
    }

    public void setGetImei(boolean getImei) {
        this.getImei = getImei;
    }

    public boolean isGetMsClassmark() {
        return getMsClassmark;
    }

    public void setGetMsClassmark(boolean getMsClassmark) {
        this.getMsClassmark = getMsClassmark;
    }

    public boolean isGetMnpRequestedInfo() {
        return getMnpRequestedInfo;
    }

    public void setGetMnpRequestedInfo(boolean getMnpRequestedInfo) {
        this.getMnpRequestedInfo = getMnpRequestedInfo;
    }

    public String getGsmScfAddress() {
        return gsmScfAddress;
    }

    public void setGsmScfAddress(String gsmScfAddress) {
        this.gsmScfAddress = gsmScfAddress;
    }

    protected static final XMLFormat<TestAtiClientConfigurationData> XML = new XMLFormat<TestAtiClientConfigurationData>(TestAtiClientConfigurationData.class) {

        public void write(TestAtiClientConfigurationData clt, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(SUBSCRIBER_IDENTITY_TYPE, clt.subscriberIdentityTypeIsImsi);

            if (clt.getLocationInformation)
                xml.setAttribute(GET_LOCATION_INFORMATION, clt.getLocationInformation);
            if (clt.getSubscriberState)
                xml.setAttribute(GET_SUBSCRIBER_STATE, clt.getSubscriberState);
            if (clt.getCurrentLocation)
                xml.setAttribute(GET_CURRENT_LOCATION, clt.getCurrentLocation);
            if (clt.getRequestedDomain != null)
                xml.setAttribute(GET_REQUESTED_DOMAIN, clt.getRequestedDomain);
            if (clt.getImei)
                xml.setAttribute(GET_IMEI, clt.getImei);
            if (clt.getMsClassmark)
                xml.setAttribute(GET_MS_CLASSMARK, clt.getMsClassmark);
            if (clt.getMnpRequestedInfo)
                xml.setAttribute(GET_MNP_REQUESTED_INFO, clt.getMnpRequestedInfo);

            xml.add(clt.addressNature.toString(), ADDRESS_NATURE, String.class);
            xml.add(clt.numberingPlan.toString(), NUMBERING_PLAN, String.class);
            xml.add(clt.gsmScfAddress, GSM_SCF_ADDRESS, String.class);
        }

        public void read(InputElement xml, TestAtiClientConfigurationData clt) throws XMLStreamException {
            CharArray chArr = xml.getAttribute(SUBSCRIBER_IDENTITY_TYPE);
            if (chArr != null)
                clt.subscriberIdentityTypeIsImsi = chArr.toBoolean();

            chArr = xml.getAttribute(GET_LOCATION_INFORMATION);
            if (chArr != null)
                clt.getLocationInformation = chArr.toBoolean();
            chArr = xml.getAttribute(GET_SUBSCRIBER_STATE);
            if (chArr != null)
                clt.getSubscriberState = chArr.toBoolean();
            chArr = xml.getAttribute(GET_CURRENT_LOCATION);
            if (chArr != null)
                clt.getCurrentLocation = chArr.toBoolean();
            chArr = xml.getAttribute(GET_REQUESTED_DOMAIN);
            if (chArr != null) {
                clt.getRequestedDomain = DomainType.valueOf(chArr.toString());
            }
            chArr = xml.getAttribute(GET_IMEI);
            if (chArr != null)
                clt.getImei = chArr.toBoolean();
            chArr = xml.getAttribute(GET_MS_CLASSMARK);
            if (chArr != null)
                clt.getMsClassmark = chArr.toBoolean();
            chArr = xml.getAttribute(GET_MNP_REQUESTED_INFO);
            if (chArr != null)
                clt.getMnpRequestedInfo = chArr.toBoolean();

            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            clt.addressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            clt.numberingPlan = NumberingPlan.valueOf(np);
            clt.gsmScfAddress = (String) xml.get(GSM_SCF_ADDRESS, String.class);
        }
    };

}
