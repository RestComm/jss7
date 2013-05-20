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

    int getSctpLocalPort();

    void setSctpLocalPort(int val);

    String getSctpRemoteHost();

    void setSctpRemoteHost(String val);

    int getSctpRemotePort();

    void setSctpRemotePort(int val);

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

    int getM3uaOpc();

    void setM3uaOpc(int val);

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
