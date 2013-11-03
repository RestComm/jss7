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

    // String getExtraLocalAddressDigits();
    //
    // void setExtraLocalAddressDigits(String val);

    void putGlobalTitleType(String val);

    void putNatureOfAddress(String val);

    void putNumberingPlan(String val);

}
