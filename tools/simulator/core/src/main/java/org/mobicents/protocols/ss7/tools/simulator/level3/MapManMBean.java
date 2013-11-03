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

package org.mobicents.protocols.ss7.tools.simulator.level3;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MapManMBean {

    // int getLocalSsn();
    //
    // void setLocalSsn(int val);
    //
    // int getRemoteSsn();
    //
    // void setRemoteSsn(int val);

    String getRemoteAddressDigits();

    void setRemoteAddressDigits(String val);

    String getOrigReference();

    void setOrigReference(String val);

    AddressNatureType getOrigReferenceAddressNature();

    String getOrigReferenceAddressNature_Value();

    void setOrigReferenceAddressNature(AddressNatureType val);

    NumberingPlanMapType getOrigReferenceNumberingPlan();

    String getOrigReferenceNumberingPlan_Value();

    void setOrigReferenceNumberingPlan(NumberingPlanMapType val);

    String getDestReference();

    void setDestReference(String val);

    AddressNatureType getDestReferenceAddressNature();

    String getDestReferenceAddressNature_Value();

    void setDestReferenceAddressNature(AddressNatureType val);

    NumberingPlanMapType getDestReferenceNumberingPlan();

    String getDestReferenceNumberingPlan_Value();

    void setDestReferenceNumberingPlan(NumberingPlanMapType val);

    void putOrigReferenceAddressNature(String val);

    void putOrigReferenceNumberingPlan(String val);

    void putDestReferenceAddressNature(String val);

    void putDestReferenceNumberingPlan(String val);

}
