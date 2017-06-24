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

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface SccpManMBean {

    boolean isRouteOnGtMode();

    void setRouteOnGtMode(boolean val);

    int getRemoteSpc();

    void setRemoteSpc(int val);

    int getLocalSpc();

    void setLocalSpc(int val);

    int getNi();

    void setNi(int val);

    int getRemoteSsn();

    void setRemoteSsn(int val);

    int getLocalSsn();

    void setLocalSsn(int val);

    int getLocalSsn2();

    void setLocalSsn2(int val);

    GlobalTitleType getGlobalTitleType();

    String getGlobalTitleType_Value();

    void setGlobalTitleType(GlobalTitleType val);

    NatureOfAddressType getNatureOfAddress();

    String getNatureOfAddress_Value();

    void setNatureOfAddress(NatureOfAddressType val);

    NumberingPlanSccpType getNumberingPlan();

    String getNumberingPlan_Value();

    void setNumberingPlan(NumberingPlanSccpType val);

    int getTranslationType();

    void setTranslationType(int val);

    String getCallingPartyAddressDigits();

    void setCallingPartyAddressDigits(String val);

    SccpProtocolVersionType getSccpProtocolVersion();

    String getSccpProtocolVersion_Value();

    void setSccpProtocolVersion(SccpProtocolVersionType val);


    void putGlobalTitleType(String val);

    void putNatureOfAddress(String val);

    void putNumberingPlan(String val);

    void putSccpProtocolVersion(String val);

}
