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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import java.util.List;

import org.apache.log4j.Level;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.AsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.AspImpl;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class M3uaMan implements M3uaManMBean, Stoppable {

    public static String SOURCE_NAME = "M3UA";

    private final String name;
    private TesterHost testerHost;
    private NettySctpManagementImpl sctpManagement;
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();
    private M3UAManagementProxyImpl m3uaMgmt;
    private boolean isSctpConnectionUp = false;
    private boolean isSctpConnectionUp2 = false;
    private boolean isM3uaConnectionActive = false;
    private boolean isM3uaConnectionActive2 = false;
    private Association assoc;
    private Association assoc2;

    private As localAs;
    private As localAs2;
    private AspFactory localAspFactory;
    private AspFactory localAspFactory2;
    private Asp localAsp;
    private Asp localAsp2;

    public M3uaMan() {
        this.name = "???";
    }

    public M3uaMan(String name) {
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    @Override
    public String getSctpLocalHost() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalHost();
    }

    @Override
    public void setSctpLocalHost(String val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setLocalHost(val);
        this.testerHost.markStore();
    }

    @Override
    public String getSctpLocalHost2() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalHost2();
    }

    @Override
    public void setSctpLocalHost2(String val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setLocalHost2(val);
        this.testerHost.markStore();
    }

    @Override
    public int getSctpLocalPort() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalPort();
    }

    @Override
    public void setSctpLocalPort(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setLocalPort(val);
        this.testerHost.markStore();
    }

    @Override
    public int getSctpLocalPort2() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalPort2();
    }

    @Override
    public void setSctpLocalPort2(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setLocalPort2(val);
        this.testerHost.markStore();
    }

    @Override
    public String getSctpRemoteHost() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemoteHost();
    }

    @Override
    public void setSctpRemoteHost(String val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setRemoteHost(val);
        this.testerHost.markStore();
    }

    @Override
    public String getSctpRemoteHost2() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemoteHost2();
    }

    @Override
    public void setSctpRemoteHost2(String val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setRemoteHost2(val);
        this.testerHost.markStore();
    }

    @Override
    public int getSctpRemotePort() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemotePort();
    }

    @Override
    public void setSctpRemotePort(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setRemotePort(val);
        this.testerHost.markStore();
    }

    @Override
    public int getSctpRemotePort2() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemotePort2();
    }

    @Override
    public void setSctpRemotePort2(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setRemotePort2(val);
        this.testerHost.markStore();
    }

    @Override
    public String getSctpExtraHostAddresses() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getSctpExtraHostAddresses();
    }

    @Override
    public void setSctpExtraHostAddresses(String val) {
        this.doSetExtraHostAddresses(val);
        this.testerHost.markStore();
    }

    public void doSetExtraHostAddresses(String val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setSctpExtraHostAddresses(val);
    }

    @Override
    public boolean isSctpIsServer() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getIsSctpServer();
    }

    @Override
    public void setSctpIsServer(boolean val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setIsSctpServer(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean getStorePcapTrace() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getStorePcapTrace();
    }

    @Override
    public void setStorePcapTrace(boolean val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setStorePcapTrace(val);
        this.testerHost.markStore();
    }

    @Override
    public BIpChannelType getSctpIPChannelType() {
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getIpChannelType() == IpChannelType.TCP)
            return new BIpChannelType(BIpChannelType.VAL_TCP);
        else
            return new BIpChannelType(BIpChannelType.VAL_SCTP);
    }

    @Override
    public void setSctpIPChannelType(BIpChannelType val) {
        if (val.intValue() == BIpChannelType.VAL_TCP)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setIpChannelType(IpChannelType.TCP);
        else
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setIpChannelType(IpChannelType.SCTP);
        this.testerHost.markStore();
    }

    @Override
    public String getSctpIPChannelType_Value() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getIpChannelType().toString();
    }

    @Override
    public M3uaFunctionality getM3uaFunctionality() {
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaFunctionality() == Functionality.IPSP) {
            return new M3uaFunctionality(M3uaFunctionality.VAL_IPSP);
        } else if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaFunctionality() == Functionality.AS) {
            return new M3uaFunctionality(M3uaFunctionality.VAL_AS);
        } else {
            return new M3uaFunctionality(M3uaFunctionality.VAL_SGW);
        }
    }

    @Override
    public void setM3uaFunctionality(M3uaFunctionality val) {
        if (val.intValue() == M3uaFunctionality.VAL_IPSP)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaFunctionality(Functionality.IPSP);
        else if (val.intValue() == M3uaFunctionality.VAL_AS)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaFunctionality(Functionality.AS);
        else
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaFunctionality(Functionality.SGW);
        this.testerHost.markStore();
    }

    @Override
    public String getM3uaFunctionality_Value() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaFunctionality().toString();
    }

    @Override
    public M3uaIPSPType getM3uaIPSPType() {
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaIPSPType() == IPSPType.CLIENT) {
            return new M3uaIPSPType(M3uaIPSPType.VAL_CLIENT);
        } else {
            return new M3uaIPSPType(M3uaIPSPType.VAL_SERVER);
        }
    }

    @Override
    public void setM3uaIPSPType(M3uaIPSPType val) {
        if (val.intValue() == M3uaIPSPType.VAL_CLIENT)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaIPSPType(IPSPType.CLIENT);
        else
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaIPSPType(IPSPType.SERVER);
        this.testerHost.markStore();
    }

    @Override
    public String getM3uaIPSPType_Value() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaIPSPType().toString();
    }

    @Override
    public M3uaRoutingLabelFormat getRoutingLabelFormat() {
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingLabelFormat() == RoutingLabelFormat.ITU) {
            return new M3uaRoutingLabelFormat(M3uaRoutingLabelFormat.VAL_ITU);
        } else if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingLabelFormat() == RoutingLabelFormat.ANSI_Sls8Bit) {
            return new M3uaRoutingLabelFormat(M3uaRoutingLabelFormat.VAL_ANSI_Sls8Bit);
        } else if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingLabelFormat() == RoutingLabelFormat.ANSI_Sls5Bit) {
            return new M3uaRoutingLabelFormat(M3uaRoutingLabelFormat.VAL_ANSI_Sls5Bit);
        } else if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingLabelFormat() == RoutingLabelFormat.Japan_TTC_DDI) {
            return new M3uaRoutingLabelFormat(M3uaRoutingLabelFormat.VAL_Japan_TTC_DDI);
        } else if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingLabelFormat() == RoutingLabelFormat.Japan_NTT) {
            return new M3uaRoutingLabelFormat(M3uaRoutingLabelFormat.VAL_Japan_NTT);
        } else {
            return new M3uaRoutingLabelFormat(M3uaRoutingLabelFormat.VAL_China);
        }
    }

    @Override
    public void setRoutingLabelFormat(M3uaRoutingLabelFormat val) {
        if (val.intValue() == M3uaRoutingLabelFormat.VAL_ITU)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingLabelFormat(RoutingLabelFormat.ITU);
        else if (val.intValue() == M3uaRoutingLabelFormat.VAL_ANSI_Sls8Bit)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingLabelFormat(RoutingLabelFormat.ANSI_Sls8Bit);
        else if (val.intValue() == M3uaRoutingLabelFormat.VAL_ANSI_Sls5Bit)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingLabelFormat(RoutingLabelFormat.ANSI_Sls5Bit);
        else if (val.intValue() == M3uaRoutingLabelFormat.VAL_Japan_TTC_DDI)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingLabelFormat(RoutingLabelFormat.Japan_TTC_DDI);
        else if (val.intValue() == M3uaRoutingLabelFormat.VAL_Japan_NTT)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingLabelFormat(RoutingLabelFormat.Japan_NTT);
        else
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingLabelFormat(RoutingLabelFormat.China);
        this.testerHost.markStore();
    }

    @Override
    public String getRoutingLabelFormat_Value() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingLabelFormat().toString();
    }

    @Override
    public M3uaExchangeType getM3uaExchangeType() {
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaExchangeType() == ExchangeType.SE) {
            return new M3uaExchangeType(M3uaExchangeType.VAL_SE);
        } else {
            return new M3uaExchangeType(M3uaExchangeType.VAL_DE);
        }
    }

    @Override
    public void setM3uaExchangeType(M3uaExchangeType val) {
        if (val.intValue() == M3uaExchangeType.VAL_SE)
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaExchangeType(ExchangeType.SE);
        else
            this.testerHost.getConfigurationData().getM3uaConfigurationData().setM3uaExchangeType(ExchangeType.DE);
        this.testerHost.markStore();
    }

    @Override
    public String getM3uaExchangeType_Value() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaExchangeType().toString();
    }

    @Override
    public int getM3uaDpc() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getDpc();
    }

    @Override
    public void setM3uaDpc(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setDpc(val);
        this.testerHost.markStore();
    }

    @Override
    public int getM3uaDpc2() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getDpc2();
    }

    @Override
    public void setM3uaDpc2(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setDpc2(val);
        this.testerHost.markStore();
    }

    @Override
    public int getM3uaOpc() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getOpc();
    }

    @Override
    public void setM3uaOpc(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setOpc(val);
        this.testerHost.markStore();
    }

    @Override
    public int getM3uaOpc2() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getOpc2();
    }

    @Override
    public void setM3uaOpc2(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setOpc2(val);
        this.testerHost.markStore();
    }

    @Override
    public int getM3uaSi() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getSi();
    }

    @Override
    public void setM3uaSi(int val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setSi(val);
        this.testerHost.markStore();
    }

    @Override
    public long getM3uaRoutingContext() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getRoutingContext();
    }

    @Override
    public void setM3uaRoutingContext(long val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setRoutingContext(val);
        this.testerHost.markStore();
    }

    @Override
    public long getM3uaNetworkAppearance() {
        return this.testerHost.getConfigurationData().getM3uaConfigurationData().getNetworkAppearance();
    }

    @Override
    public void setM3uaNetworkAppearance(long val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setNetworkAppearance(val);
        this.testerHost.markStore();
    }

    @Override
    public M3uaTrafficModeType getM3uaTrafficModeType() {
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getTrafficModeType() == M3uaTrafficModeType.VAL_LOADSHARE) {
            return new M3uaTrafficModeType(M3uaTrafficModeType.VAL_LOADSHARE);
        } else if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getTrafficModeType() == M3uaTrafficModeType.VAL_OVERRIDE) {
            return new M3uaTrafficModeType(M3uaTrafficModeType.VAL_OVERRIDE);
        }

        return new M3uaTrafficModeType(M3uaTrafficModeType.VAL_BROADCAST);
    }

    @Override
    public void setM3uaTrafficModeType(M3uaTrafficModeType val) {
        this.testerHost.getConfigurationData().getM3uaConfigurationData().setTrafficModeType(val.intValue());
        this.testerHost.markStore();
    }

    @Override
    public String getM3uaTrafficModeType_Value() {
        TrafficModeType trafficModeType = factory.createTrafficModeType(this.testerHost.getConfigurationData().getM3uaConfigurationData().getTrafficModeType());
        return trafficModeType.toString();
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("SCTP: ");
        sb.append(this.isSctpConnectionUp ? "Connected" : "Disconnected");
        if (this.assoc2 != null)
            sb.append(this.isSctpConnectionUp2 ? "/Connected" : "/Disconnected");
        sb.append("  M3UA:");

        this.m3uaMgmt.getAppServers();
        List<As> lstAs = this.m3uaMgmt.getAppServers();
        for (As as : lstAs) {
            if (as.getName().equals("testas")) {
                AsImpl asImpl = (AsImpl) as;
                FSM lFsm = asImpl.getLocalFSM();
                FSM pFsm = asImpl.getPeerFSM();
                FSM lFsmP = null;
                FSM pFsmP = null;

                List<Asp> lstAsp = as.getAspList();
                for (Asp asp : lstAsp) {
                    // we take only the first ASP (it should be a single)
                    AspImpl aspImpl = (AspImpl) asp;
                    lFsmP = aspImpl.getLocalFSM();
                    pFsmP = aspImpl.getPeerFSM();
                    break;
                }

                if (lFsm != null) {
                    sb.append(" lFsm:");
                    sb.append(lFsm.getState().toString());
                }
                if (pFsm != null) {
                    sb.append(" pFsm:");
                    sb.append(pFsm.getState().toString());
                }
                if (lFsmP != null) {
                    sb.append(" lFsmP:");
                    sb.append(lFsmP.getState().toString());
                }
                if (pFsmP != null) {
                    sb.append(" pFsmP:");
                    sb.append(pFsmP.getState().toString());
                }

                break;
            }
        }

        return sb.toString();
    }

    @Override
    public void putSctpIPChannelType(String val) {
        BIpChannelType x = BIpChannelType.createInstance(val);
        if (x != null)
            this.setSctpIPChannelType(x);
    }

    @Override
    public void putM3uaFunctionality(String val) {
        M3uaFunctionality x = M3uaFunctionality.createInstance(val);
        if (x != null)
            this.setM3uaFunctionality(x);
    }

    @Override
    public void putM3uaIPSPType(String val) {
        M3uaIPSPType x = M3uaIPSPType.createInstance(val);
        if (x != null)
            this.setM3uaIPSPType(x);
    }

    @Override
    public void putRoutingLabelFormat(String val) {
        M3uaRoutingLabelFormat x = M3uaRoutingLabelFormat.createInstance(val);
        if (x != null)
            this.setRoutingLabelFormat(x);
    }

    @Override
    public void putM3uaExchangeType(String val) {
        M3uaExchangeType x = M3uaExchangeType.createInstance(val);
        if (x != null)
            this.setM3uaExchangeType(x);
    }

    @Override
    public void putM3uaTrafficModeType(String val) {
        M3uaTrafficModeType x = M3uaTrafficModeType.createInstance(val);
        if (x != null)
            this.setM3uaTrafficModeType(x);
    }

    public boolean start() {
        try {
            this.isSctpConnectionUp = false;
            this.isSctpConnectionUp2 = false;
            this.isM3uaConnectionActive = false;
            this.isM3uaConnectionActive2 = false;
            this.initM3ua(this.testerHost.getConfigurationData().getM3uaConfigurationData().getStorePcapTrace(), this.testerHost.getConfigurationData()
                    .getM3uaConfigurationData().getIsSctpServer(), this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalHost(),
                    this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalPort(), this.testerHost.getConfigurationData()
                            .getM3uaConfigurationData().getRemoteHost(), this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemotePort(),
                    this.testerHost.getConfigurationData().getM3uaConfigurationData().getLocalHost2(), this.testerHost.getConfigurationData()
                            .getM3uaConfigurationData().getLocalPort2(), this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemoteHost2(),
                    this.testerHost.getConfigurationData().getM3uaConfigurationData().getRemotePort2(), this.testerHost.getConfigurationData()
                            .getM3uaConfigurationData().getIpChannelType(), this.testerHost.getConfigurationData().getM3uaConfigurationData()
                            .getSctpExtraHostAddressesArray(), this.testerHost.getPersistDir(), this.testerHost.getConfigurationData()
                            .getM3uaConfigurationData().getTrafficModeType(), this.testerHost.getConfigurationData().getM3uaConfigurationData()
                            .getRoutingLabelFormat());
            this.testerHost.sendNotif(SOURCE_NAME, "M3UA has been started", "", Level.INFO);
            return true;
        } catch (Throwable e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting M3uaMan", e, Level.ERROR);
            return false;
        }
    }

    public void stop() {
        try {
            this.stopM3ua();
            this.testerHost.sendNotif(SOURCE_NAME, "M3UA has been stopped", "", Level.INFO);
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping M3uaMan", e, Level.ERROR);
        }
    }

    @Override
    public void execute() {
        if (this.assoc != null) {
            boolean conn = this.assoc.isConnected();
            if (this.isSctpConnectionUp != conn) {
                this.isSctpConnectionUp = conn;
                this.testerHost.sendNotif(SOURCE_NAME, "Sctp connection is " + (conn ? "up" : "down"), this.assoc.getName(),
                        Level.INFO);
            }
        }
        if (this.assoc2 != null) {
            boolean conn = this.assoc2.isConnected();
            if (this.isSctpConnectionUp2 != conn) {
                this.isSctpConnectionUp2 = conn;
                this.testerHost.sendNotif(SOURCE_NAME, "Sctp2 connection is " + (conn ? "up" : "down"), this.assoc2.getName(),
                        Level.INFO);
            }
        }
        if (this.m3uaMgmt != null) {
            boolean active = false;
            List<As> lstAs = this.m3uaMgmt.getAppServers();
            for (As as : lstAs) {
                if (as.getName().equals("testas")) {
                    AsImpl asImpl = (AsImpl) as;
                    FSM lFsm = asImpl.getLocalFSM();
                    FSM pFsm = asImpl.getPeerFSM();
                    if ((lFsm == null || lFsm.getState().getName().equals(AsState.ACTIVE.toString()))
                            && (pFsm == null || pFsm.getState().getName().equals(AsState.ACTIVE.toString()))) {
                        active = true;
                    }
                    break;
                }
            }
            if (this.isM3uaConnectionActive != active) {
                this.isM3uaConnectionActive = active;
                this.testerHost.sendNotif(SOURCE_NAME, "M3ua connection is " + (active ? "active" : "not active"),
                        this.assoc.getName(), Level.INFO);
            }
            if (this.assoc2 != null) {
                if (this.isM3uaConnectionActive2 != active) {
                    this.isM3uaConnectionActive2 = active;
                    this.testerHost.sendNotif(SOURCE_NAME, "M3ua connection2 is " + (active ? "active" : "not active"), this.assoc2.getName(), Level.INFO);
                }
            }
        }
    }

    private void initM3ua(boolean storePcapTrace, boolean isSctpServer, String localHost, int localPort, String remoteHost, int remotePort, String localHost2,
            int localPort2, String remoteHost2, int remotePort2, IpChannelType ipChannelType, String[] extraHostAddresses, String persistDir,
            int trafficModeTypeInt, RoutingLabelFormat routingLabelFormat) throws Exception {

        this.stopM3ua();

        // init SCTP stack
        this.sctpManagement = new NettySctpManagementImpl("SimSCTPServer_" + name);
        // set 8 threads for delivering messages
        this.sctpManagement.setPersistDir(persistDir);
        this.sctpManagement.setWorkerThreads(8);
        this.sctpManagement.setSingleThread(false);

        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResourses();
        Thread.sleep(500); // waiting for freeing ip ports

        // init M3UA stack
        this.m3uaMgmt = new M3UAManagementProxyImpl("SimM3uaServer_" + name);
        this.m3uaMgmt.setPersistDir(persistDir);
        this.m3uaMgmt.setTransportManagement(this.sctpManagement);
        this.m3uaMgmt.setRoutingLabelFormat(routingLabelFormat);

        this.m3uaMgmt.start();
        this.m3uaMgmt.removeAllResourses();

        // starting pcap trace storing if it is configured
        if (storePcapTrace) {
            String s1 = "";
            if (persistDir != null)
                s1 = persistDir + "/";
            this.m3uaMgmt.startPcapTrace(s1 + "MsgLog_" + name + ".pcap");
        }

        // configure SCTP stack
        String SERVER_NAME = "Server_" + name;
        String SERVER_NAME_2 = "Server_" + name + "_2";
        String SERVER_ASSOCIATION_NAME = "ServerAss_" + name;
        String SERVER_ASSOCIATION_NAME_2 = "ServerAss_" + name + "_2";
        String ASSOCIATION_NAME = "Ass_" + name;
        String ASSOCIATION_NAME_2 = "Ass_" + name + "_2";
        String assName;
        String assName2 = null;

        if (isSctpServer) {

            // String localHost2, int localPort2, String remoteHost2, int remotePort2

            // 1. Create SCTP Server
            sctpManagement.addServer(SERVER_NAME, localHost, localPort, ipChannelType, extraHostAddresses);

            // 2. Create SCTP Server Association
            sctpManagement.addServerAssociation(remoteHost, remotePort, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);
            this.assoc = sctpManagement.getAssociation(SERVER_ASSOCIATION_NAME);
            assName = SERVER_ASSOCIATION_NAME;

            // 3. Start Server
            sctpManagement.startServer(SERVER_NAME);

            if (localHost2 != null && !localHost2.equals("") && remoteHost2 != null && !remoteHost2.equals("")) {
                // a second link is defined

                // 1. Create SCTP Server
                sctpManagement.addServer(SERVER_NAME_2, localHost2, localPort2, ipChannelType, null);

                // 2. Create SCTP Server Association
                sctpManagement.addServerAssociation(remoteHost2, remotePort2, SERVER_NAME_2, SERVER_ASSOCIATION_NAME_2, ipChannelType);
                this.assoc2 = sctpManagement.getAssociation(SERVER_ASSOCIATION_NAME_2);
                assName2 = SERVER_ASSOCIATION_NAME_2;

                // 3. Start Server
                sctpManagement.startServer(SERVER_NAME_2);
            }
        } else {

            // 1. Create SCTP Association
            sctpManagement.addAssociation(localHost, localPort, remoteHost, remotePort, ASSOCIATION_NAME, ipChannelType,
                    extraHostAddresses);
            this.assoc = sctpManagement.getAssociation(ASSOCIATION_NAME);
            assName = ASSOCIATION_NAME;

            if (localHost2 != null && !localHost2.equals("") && remoteHost2 != null && !remoteHost2.equals("")) {
                // a second link is defined

                // 1. Create SCTP Association
                sctpManagement.addAssociation(localHost2, localPort2, remoteHost2, remotePort2, ASSOCIATION_NAME_2, ipChannelType, null);
                this.assoc2 = sctpManagement.getAssociation(ASSOCIATION_NAME_2);
                assName2 = ASSOCIATION_NAME_2;
            }
        }

        // configure M3UA stack
        // 1. Create AS
        RoutingContext rc = factory.createRoutingContext(new long[] { this.testerHost.getConfigurationData()
                .getM3uaConfigurationData().getRoutingContext() });
        TrafficModeType trafficModeType = factory.createTrafficModeType(trafficModeTypeInt);
        NetworkAppearance na = null;
        if (this.testerHost.getConfigurationData().getM3uaConfigurationData().getNetworkAppearance() != 0)
            na = factory.createNetworkAppearance(this.testerHost.getConfigurationData().getM3uaConfigurationData().getNetworkAppearance());
        localAs = m3uaMgmt.createAs("testas", this.testerHost.getConfigurationData().getM3uaConfigurationData()
                .getM3uaFunctionality(), this.testerHost.getConfigurationData().getM3uaConfigurationData()
                .getM3uaExchangeType(), this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaIPSPType(),
                rc, trafficModeType, 1, na);

        // 2. Create ASP
        localAspFactory = m3uaMgmt.createAspFactory("testasp", assName);

        // 3. Assign ASP to AS
        localAsp = m3uaMgmt.assignAspToAs("testas", "testasp");

        // 4. Define Route
        // Define Route
        m3uaMgmt.addRoute(this.testerHost.getConfigurationData().getM3uaConfigurationData().getDpc(), this.testerHost
                .getConfigurationData().getM3uaConfigurationData().getOpc(), this.testerHost.getConfigurationData()
                .getM3uaConfigurationData().getSi(), "testas");

        // 2. Start ASP
        m3uaMgmt.startAsp("testasp");

        if(assName2!=null){
            // 1. Create AS
            localAs2 = m3uaMgmt.createAs("testas2", this.testerHost.getConfigurationData().getM3uaConfigurationData().getM3uaFunctionality(), this.testerHost
                    .getConfigurationData().getM3uaConfigurationData().getM3uaExchangeType(), this.testerHost.getConfigurationData().getM3uaConfigurationData()
                    .getM3uaIPSPType(), rc, trafficModeType, 1, na);

            // 2. Create ASP
            localAspFactory2 = m3uaMgmt.createAspFactory("testasp2", assName2);

            // 3. Assign ASP to AS
            localAsp2 = m3uaMgmt.assignAspToAs("testas2", "testasp2");

            // 4. Define Route
            // Define Route
            m3uaMgmt.addRoute(this.testerHost.getConfigurationData().getM3uaConfigurationData().getDpc2(), this.testerHost
                    .getConfigurationData().getM3uaConfigurationData().getOpc2(), this.testerHost.getConfigurationData()
                    .getM3uaConfigurationData().getSi(), "testas2");

            // 2. Start ASP
            m3uaMgmt.startAsp("testasp2");
        }

    }

    private void stopM3ua() throws Exception {
        if (this.m3uaMgmt != null) {
            this.m3uaMgmt.stop();
            this.m3uaMgmt = null;
        }
        if (this.sctpManagement != null) {
            this.sctpManagement.stop();
            this.sctpManagement = null;
        }
    }

    public Mtp3UserPart getMtp3UserPart() {
        return this.m3uaMgmt;
    }
}
