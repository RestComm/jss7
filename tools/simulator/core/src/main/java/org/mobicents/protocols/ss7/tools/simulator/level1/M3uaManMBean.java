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

package org.mobicents.protocols.ss7.tools.simulator.level1;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface M3uaManMBean {

    boolean getStorePcapTrace();

    void setStorePcapTrace(boolean val);

    String getSctpLocalHost();

    void setSctpLocalHost(String val);

    String getSctpLocalHost2();

    void setSctpLocalHost2(String val);

    int getSctpLocalPort();

    void setSctpLocalPort(int val);

    int getSctpLocalPort2();

    void setSctpLocalPort2(int val);

    String getSctpRemoteHost();

    void setSctpRemoteHost(String val);

    String getSctpRemoteHost2();

    void setSctpRemoteHost2(String val);

    int getSctpRemotePort();

    void setSctpRemotePort(int val);

    int getSctpRemotePort2();

    void setSctpRemotePort2(int val);

    String getSctpExtraHostAddresses();

    void setSctpExtraHostAddresses(String val);

    boolean isSctpIsServer();

    void setSctpIsServer(boolean val);

    BIpChannelType getSctpIPChannelType();

    void setSctpIPChannelType(BIpChannelType val);

    String getSctpIPChannelType_Value();

    M3uaFunctionality getM3uaFunctionality();

    void setM3uaFunctionality(M3uaFunctionality val);

    String getM3uaFunctionality_Value();

    M3uaIPSPType getM3uaIPSPType();

    void setM3uaIPSPType(M3uaIPSPType val);

    String getM3uaIPSPType_Value();

    M3uaExchangeType getM3uaExchangeType();

    void setM3uaExchangeType(M3uaExchangeType val);

    String getM3uaExchangeType_Value();

    int getM3uaDpc();

    void setM3uaDpc(int val);

    int getM3uaDpc2();

    void setM3uaDpc2(int val);

    int getM3uaOpc();

    void setM3uaOpc(int val);

    int getM3uaOpc2();

    void setM3uaOpc2(int val);

    int getM3uaSi();

    void setM3uaSi(int val);

    long getM3uaRoutingContext();

    void setM3uaRoutingContext(long val);

    long getM3uaNetworkAppearance();

    void setM3uaNetworkAppearance(long val);

    void putSctpIPChannelType(String val);

    void putM3uaFunctionality(String val);

    void putM3uaIPSPType(String val);

    void putM3uaExchangeType(String val);

}
