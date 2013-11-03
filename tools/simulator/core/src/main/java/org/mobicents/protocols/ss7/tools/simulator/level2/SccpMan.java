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

package org.mobicents.protocols.ss7.tools.simulator.level2;

import java.io.IOException;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpMan implements SccpManMBean, Stoppable {

    public static String SOURCE_NAME = "SCCP";

    private final String name;
    private TesterHost testerHost;

    private Mtp3UserPart mtp3UserPart;

    private SccpStackImpl sccpStack;
    private SccpProvider sccpProvider;
    private SccpResource resource;
    private Router router;
    private boolean isRspcUp = true;
    private boolean isRssUp = true;

    public SccpMan() {
        this.name = "???";
    }

    public SccpMan(String name) {
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    public void setMtp3UserPart(Mtp3UserPart val) {
        this.mtp3UserPart = val;
    }

    public SccpStack getSccpStack() {
        return this.sccpStack;
    }

    @Override
    public boolean isRouteOnGtMode() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().isRouteOnGtMode();
    }

    @Override
    public void setRouteOnGtMode(boolean val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setRouteOnGtMode(val);
        this.testerHost.markStore();
    }

    @Override
    public int getRemoteSpc() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSpc();
    }

    @Override
    public void setRemoteSpc(int val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setRemoteSpc(val);
        this.testerHost.markStore();
    }

    @Override
    public int getLocalSpc() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getLocalSpc();
    }

    @Override
    public void setLocalSpc(int val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setLocalSpc(val);
        this.testerHost.markStore();
    }

    @Override
    public int getNi() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getNi();
    }

    @Override
    public void setNi(int val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setNi(val);
        this.testerHost.markStore();
    }

    @Override
    public int getRemoteSsn() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSsn();
    }

    @Override
    public void setRemoteSsn(int val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setRemoteSsn(val);
        this.testerHost.markStore();
    }

    @Override
    public int getLocalSsn() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getLocalSsn();
    }

    @Override
    public void setLocalSsn(int val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setLocalSsn(val);
        this.testerHost.markStore();
    }

    @Override
    public GlobalTitleType getGlobalTitleType() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getGlobalTitleType();
    }

    @Override
    public String getGlobalTitleType_Value() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getGlobalTitleType().toString();
    }

    @Override
    public void setGlobalTitleType(GlobalTitleType val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setGlobalTitleType(val);
        this.testerHost.markStore();
    }

    @Override
    public NatureOfAddressType getNatureOfAddress() {
        return new NatureOfAddressType(this.testerHost.getConfigurationData().getSccpConfigurationData().getNatureOfAddress()
                .getValue());
    }

    @Override
    public String getNatureOfAddress_Value() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getNatureOfAddress().toString();
    }

    @Override
    public void setNatureOfAddress(NatureOfAddressType val) {
        try {
            this.testerHost.getConfigurationData().getSccpConfigurationData()
                    .setNatureOfAddress(NatureOfAddress.valueOf(val.intValue()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanSccpType getNumberingPlan() {
        return new NumberingPlanSccpType(this.testerHost.getConfigurationData().getSccpConfigurationData().getNumberingPlan()
                .getValue());
    }

    @Override
    public String getNumberingPlan_Value() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getNumberingPlan().toString();
    }

    @Override
    public void setNumberingPlan(NumberingPlanSccpType val) {
        try {
            this.testerHost.getConfigurationData().getSccpConfigurationData()
                    .setNumberingPlan(NumberingPlan.valueOf(val.intValue()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.testerHost.markStore();
    }

    @Override
    public int getTranslationType() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getTranslationType();
    }

    @Override
    public void setTranslationType(int val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setTranslationType(val);
        this.testerHost.markStore();
    }

    @Override
    public String getCallingPartyAddressDigits() {
        return this.testerHost.getConfigurationData().getSccpConfigurationData().getCallingPartyAddressDigits();
    }

    @Override
    public void setCallingPartyAddressDigits(String val) {
        this.testerHost.getConfigurationData().getSccpConfigurationData().setCallingPartyAddressDigits(val);
        this.testerHost.markStore();
    }

    @Override
    public void putGlobalTitleType(String val) {
        GlobalTitleType x = GlobalTitleType.createInstance(val);
        if (x != null)
            this.setGlobalTitleType(x);
    }

    @Override
    public void putNatureOfAddress(String val) {
        NatureOfAddressType x = NatureOfAddressType.createInstance(val);
        if (x != null)
            this.setNatureOfAddress(x);
    }

    @Override
    public void putNumberingPlan(String val) {
        NumberingPlanSccpType x = NumberingPlanSccpType.createInstance(val);
        if (x != null)
            this.setNumberingPlan(x);
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("SCCP: Rspc: ");
        sb.append(this.isRspcUp ? "Enabled" : "Disabled");
        sb.append("  Rss: ");
        sb.append(this.isRssUp ? "Enabled" : "Disabled");
        return sb.toString();
    }

    public boolean start() {
        try {
            this.isRspcUp = true;
            this.isRssUp = true;
            this.initSccp(this.mtp3UserPart, this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSsn(),
                    this.testerHost.getConfigurationData().getSccpConfigurationData().getLocalSsn(), this.testerHost
                            .getConfigurationData().getSccpConfigurationData().getRemoteSpc(), this.testerHost
                            .getConfigurationData().getSccpConfigurationData().getLocalSpc(), this.testerHost
                            .getConfigurationData().getSccpConfigurationData().getNi(), this.testerHost.getConfigurationData()
                            .getSccpConfigurationData().getCallingPartyAddressDigits(), this.testerHost.getPersistDir());
            this.testerHost.sendNotif(SOURCE_NAME, "SCCP has been started", "", Level.INFO);
            return true;
        } catch (Throwable e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting SccpMan", e, Level.ERROR);
            return false;
        }
    }

    @Override
    public void stop() {
        try {
            this.stopSccp();
            this.testerHost.sendNotif(SOURCE_NAME, "SCCP has been stopped", "", Level.INFO);
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping SccpMan", e, Level.ERROR);
        }
    }

    @Override
    public void execute() {
        if (this.resource != null) {
            RemoteSignalingPointCode rspc = this.resource.getRemoteSpc(1);
            RemoteSubSystem rss = this.resource.getRemoteSsn(1);
            if (rspc != null) {
                boolean conn = !rspc.isRemoteSpcProhibited();
                if (this.isRspcUp != conn) {
                    this.isRspcUp = conn;
                    this.testerHost.sendNotif(SOURCE_NAME, "SCCP RemoteSignalingPoint is " + (conn ? "enabled" : "disabled"),
                            "Dpc=" + this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSpc(),
                            Level.INFO);
                }
            }
            if (rss != null) {
                boolean conn = !rss.isRemoteSsnProhibited();
                if (this.isRssUp != conn) {
                    this.isRssUp = conn;
                    this.testerHost.sendNotif(SOURCE_NAME, "SCCP RemoteSubSystem is " + (conn ? "enabled" : "disabled"), "Dpc="
                            + this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSpc() + " Ssn="
                            + this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSsn(), Level.INFO);
                }
            }
        }
    }

    private void initSccp(Mtp3UserPart mtp3UserPart, int remoteSsn, int localSsn, int dpc, int opc, int ni, String callingPartyAddressDigits, String persistDir)
            throws Exception {

        this.sccpStack = new SccpStackImpl("TestingSccp");
        this.sccpStack.setPersistDir(persistDir);

        this.sccpStack.setMtp3UserPart(1, mtp3UserPart);
        this.sccpStack.start();
        this.sccpStack.removeAllResourses();

        this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, opc, ni);
        this.sccpStack.getRouter().addMtp3Destination(1, 1, dpc, dpc, 0, 255, 255);

        this.sccpProvider = this.sccpStack.getSccpProvider();

        // router1 = sccpStack1.getRouter();

        this.resource = this.sccpStack.getSccpResource();

        this.resource.addRemoteSpc(1, dpc, 0, 0);
        this.resource.addRemoteSsn(1, dpc, remoteSsn, 0, false);

        if (this.testerHost.getConfigurationData().getSccpConfigurationData().isRouteOnGtMode()) {
            this.router = this.sccpStack.getRouter();

            this.router.addRoutingAddress(1,
                    new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, dpc, this.createGlobalTitle(""), 0));
            this.router.addRoutingAddress(2,
                    new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, opc, this.createGlobalTitle(""), localSsn));

            SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                    this.createGlobalTitle("*"), 0);
            String mask = "K";
            ((RouterImpl) this.router).addRule(1, RuleType.Solitary, null, OriginationType.LocalOriginated, pattern, mask, 1,
                    -1, null);
            pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, this.createGlobalTitle("*"), 0);
            mask = "R";
            ((RouterImpl) this.router).addRule(2, RuleType.Solitary, null, OriginationType.RemoteOriginated, pattern, mask, 2,
                    -1, null);
        }
    }

    private void stopSccp() {

        this.sccpStack.removeAllResourses();
        this.sccpStack.stop();
    }

    public SccpAddress createCallingPartyAddress() {
        if (this.testerHost.getConfigurationData().getSccpConfigurationData().isRouteOnGtMode()) {
            return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, createGlobalTitle(this.testerHost
                    .getConfigurationData().getSccpConfigurationData().getCallingPartyAddressDigits()), this.testerHost
                    .getConfigurationData().getSccpConfigurationData().getLocalSsn());
        } else {
            return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, this.testerHost.getConfigurationData()
                    .getSccpConfigurationData().getLocalSpc(), null, this.testerHost.getConfigurationData()
                    .getSccpConfigurationData().getLocalSsn());
        }
    }

    public SccpAddress createCalledPartyAddress() {
        return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, this.testerHost.getConfigurationData()
                .getSccpConfigurationData().getRemoteSpc(), null, this.testerHost.getConfigurationData()
                .getSccpConfigurationData().getRemoteSsn());
    }

    public SccpAddress createCalledPartyAddress(String address, int ssn) {
        if (this.testerHost.getConfigurationData().getSccpConfigurationData().isRouteOnGtMode()) {
            return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, createGlobalTitle(address),
                    (ssn >= 0 ? ssn : this.testerHost.getConfigurationData().getSccpConfigurationData().getRemoteSsn()));
        } else {
            return createCalledPartyAddress();
        }
    }

    public GlobalTitle createGlobalTitle(String address) {
        GlobalTitle gt = null;
        switch (this.testerHost.getConfigurationData().getSccpConfigurationData().getGlobalTitleType().intValue()) {
            case GlobalTitleType.VAL_NOA_ONLY:
                gt = GlobalTitle.getInstance(this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getNatureOfAddress(), address);
                break;
            case GlobalTitleType.VAL_TT_ONLY:
                gt = GlobalTitle.getInstance(this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getTranslationType(), address);
                break;
            case GlobalTitleType.VAL_TT_NP_ES:
                gt = GlobalTitle.getInstance(this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getTranslationType(), this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getNumberingPlan(), address);
                break;
            case GlobalTitleType.VAL_TT_NP_ES_NOA:
                gt = GlobalTitle.getInstance(this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getTranslationType(), this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getNumberingPlan(), this.testerHost.getConfigurationData().getSccpConfigurationData()
                        .getNatureOfAddress(), address);
                break;
        }
        return gt;
    }

    // public SccpAddress createDestAddress() {
    // return new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, this.remotePc, null, this.remoteSsn);
    // }
}
