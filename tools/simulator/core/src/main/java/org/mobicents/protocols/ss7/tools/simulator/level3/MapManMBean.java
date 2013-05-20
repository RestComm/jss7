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
