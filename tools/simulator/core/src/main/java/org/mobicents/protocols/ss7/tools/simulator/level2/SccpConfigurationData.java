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

package org.mobicents.protocols.ss7.tools.simulator.level2;

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpConfigurationData {

    protected static final String REMOTE_ON_GT_MODE = "routeOnGtMode";
    protected static final String REMOTE_SPC = "remoteSpc";
    protected static final String LOCAL_SPC = "localSpc";
    protected static final String NI = "ni";
    protected static final String REMOTE_SSN = "remoteSsn";
    protected static final String LOCAL_SSN = "localSsn";
    protected static final String LOCAL_SSN2 = "localSsn2";
    protected static final String GLOBAL_TITLE_TYPE = "globalTitleType";
    protected static final String ADDRESS_NATURE = "addressNature";
    protected static final String NUMBERING_PLAN = "numberingPlan";
    protected static final String TRANSLATION_TYTE = "translationType";
    protected static final String CALLING_PARTY_ADDRESS_DIGITS = "callingPartyAddressDigits";
    protected static final String SCCP_PROTOCOL_VERSION = "sccpProtocolVersion";

    private boolean routeOnGtMode;
    private int remoteSpc = 0;
    private int localSpc = 0;
    private int localSsn;
    private int localSsn2;
    private int remoteSsn;
    private int ni = 0;
    private GlobalTitleType globalTitleType = new GlobalTitleType(GlobalTitleType.VAL_TT_NP_ES_NOA);
    private NatureOfAddress natureOfAddress = NatureOfAddress.INTERNATIONAL;
    private NumberingPlan numberingPlan = NumberingPlan.ISDN_MOBILE;
    private int translationType = 0;
    private String callingPartyAddressDigits = "";
    private SccpProtocolVersion sccpProtocolVersion = SccpProtocolVersion.ITU;

    public boolean isRouteOnGtMode() {
        return routeOnGtMode;
    }

    public void setRouteOnGtMode(boolean routeOnGtMode) {
        this.routeOnGtMode = routeOnGtMode;
    }

    public int getRemoteSpc() {
        return remoteSpc;
    }

    public void setRemoteSpc(int remoteSpc) {
        this.remoteSpc = remoteSpc;
    }

    public int getLocalSpc() {
        return localSpc;
    }

    public void setLocalSpc(int localSpc) {
        this.localSpc = localSpc;
    }

    public int getLocalSsn() {
        return localSsn;
    }

    public void setLocalSsn(int localSsn) {
        this.localSsn = localSsn;
    }

    public int getLocalSsn2() {
        return localSsn2;
    }

    public void setLocalSsn2(int localSsn2) {
        this.localSsn2 = localSsn2;
    }

    public int getRemoteSsn() {
        return remoteSsn;
    }

    public void setRemoteSsn(int remoteSsn) {
        this.remoteSsn = remoteSsn;
    }

    public int getNi() {
        return ni;
    }

    public void setNi(int ni) {
        this.ni = ni;
    }

    public GlobalTitleType getGlobalTitleType() {
        return globalTitleType;
    }

    public void setGlobalTitleType(GlobalTitleType globalTitleType) {
        this.globalTitleType = globalTitleType;
    }

    public NatureOfAddress getNatureOfAddress() {
        return natureOfAddress;
    }

    public void setNatureOfAddress(NatureOfAddress natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    public NumberingPlan getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(NumberingPlan numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    public int getTranslationType() {
        return translationType;
    }

    public void setTranslationType(int translationType) {
        this.translationType = translationType;
    }

    public String getCallingPartyAddressDigits() {
        return callingPartyAddressDigits;
    }

    public void setCallingPartyAddressDigits(String callingPartyAddressDigits) {
        this.callingPartyAddressDigits = callingPartyAddressDigits;
    }

    public SccpProtocolVersion getSccpProtocolVersion() {
        return sccpProtocolVersion;
    }

    public void setSccpProtocolVersion(SccpProtocolVersion sccpProtocolVersion) {
        this.sccpProtocolVersion = sccpProtocolVersion;
    }

    protected static final XMLFormat<SccpConfigurationData> XML = new XMLFormat<SccpConfigurationData>(
            SccpConfigurationData.class) {

        public void write(SccpConfigurationData sccp, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(REMOTE_ON_GT_MODE, sccp.isRouteOnGtMode());
            xml.setAttribute(REMOTE_SPC, sccp.getRemoteSpc());
            xml.setAttribute(LOCAL_SPC, sccp.getLocalSpc());
            xml.setAttribute(NI, sccp.getNi());
            xml.setAttribute(REMOTE_SSN, sccp.getRemoteSsn());
            xml.setAttribute(LOCAL_SSN, sccp.getLocalSsn());
            xml.setAttribute(LOCAL_SSN2, sccp.getLocalSsn2());
            xml.setAttribute(TRANSLATION_TYTE, sccp.getTranslationType());

            xml.add(sccp.getGlobalTitleType().toString(), GLOBAL_TITLE_TYPE, String.class);
            xml.add(sccp.getNatureOfAddress().toString(), ADDRESS_NATURE, String.class);
            xml.add(sccp.getNumberingPlan().toString(), NUMBERING_PLAN, String.class);
            xml.add(sccp.getSccpProtocolVersion().toString(), SCCP_PROTOCOL_VERSION, String.class);
            xml.add(sccp.getCallingPartyAddressDigits(), CALLING_PARTY_ADDRESS_DIGITS, String.class);
        }

        public void read(InputElement xml, SccpConfigurationData sccp) throws XMLStreamException {
            sccp.setRouteOnGtMode(xml.getAttribute(REMOTE_ON_GT_MODE).toBoolean());
            sccp.setRemoteSpc(xml.getAttribute(REMOTE_SPC).toInt());
            sccp.setLocalSpc(xml.getAttribute(LOCAL_SPC).toInt());
            sccp.setNi(xml.getAttribute(NI).toInt());
            sccp.setRemoteSsn(xml.getAttribute(REMOTE_SSN).toInt());
            sccp.setLocalSsn(xml.getAttribute(LOCAL_SSN).toInt());
            CharArray ca = xml.getAttribute(LOCAL_SSN2);
            if (ca != null)
                sccp.setLocalSsn2(ca.toInt());
            sccp.setTranslationType(xml.getAttribute(TRANSLATION_TYTE).toInt());

            String gtt = (String) xml.get(GLOBAL_TITLE_TYPE, String.class);
            sccp.setGlobalTitleType(GlobalTitleType.createInstance(gtt));
            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            sccp.setNatureOfAddress(NatureOfAddress.valueOf(an));
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            sccp.setNumberingPlan(NumberingPlan.valueOf(np));
            String spv = (String) xml.get(SCCP_PROTOCOL_VERSION, String.class);
            if (spv != null)
                sccp.setSccpProtocolVersion(SccpProtocolVersion.valueOf(spv));
            sccp.setCallingPartyAddressDigits((String) xml.get(CALLING_PARTY_ADDRESS_DIGITS, String.class));
        }
    };

}
