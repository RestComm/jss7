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

	public boolean getStorePcapTrace();

	public void setStorePcapTrace(boolean val);

	public String getSctpLocalHost();

	public void setSctpLocalHost(String val);

	public int getSctpLocalPort();

	public void setSctpLocalPort(int val);

	public String getSctpRemoteHost();

	public void setSctpRemoteHost(String val);

	public int getSctpRemotePort();

	public void setSctpRemotePort(int val);

	public String getSctpExtraHostAddresses();

	public void setSctpExtraHostAddresses(String val);

	public boolean isSctpIsServer();

	public void setSctpIsServer(boolean val);

	public BIpChannelType getSctpIPChannelType();

	public void setSctpIPChannelType(BIpChannelType val);

	public String getSctpIPChannelType_Value();


	public M3uaFunctionality getM3uaFunctionality();

	public void setM3uaFunctionality(M3uaFunctionality val);

	public String getM3uaFunctionality_Value();

	public M3uaIPSPType getM3uaIPSPType();

	public void setM3uaIPSPType(M3uaIPSPType val);

	public String getM3uaIPSPType_Value();

	public M3uaExchangeType getM3uaExchangeType();

	public void setM3uaExchangeType(M3uaExchangeType val);

	public String getM3uaExchangeType_Value();

	public int getM3uaDpc();

	public void setM3uaDpc(int val);

	public int getM3uaOpc();

	public void setM3uaOpc(int val);

	public int getM3uaSi();

	public void setM3uaSi(int val);

	public long getM3uaRoutingContext();

	public void setM3uaRoutingContext(long val);

	public long getM3uaNetworkAppearance();

	public void setM3uaNetworkAppearance(long val);


	public void putSctpIPChannelType(String val);

	public void putM3uaFunctionality(String val);

	public void putM3uaIPSPType(String val);

	public void putM3uaExchangeType(String val);

}

