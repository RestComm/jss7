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

package org.restcomm.protocols.ss7.tools.simulator.tests.ati;

import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
*
* @author sergey vetyutnev
*
*/
public interface TestAtiClientManMBean {

    AddressNatureType getAddressNature();

    String getAddressNature_Value();

    void setAddressNature(AddressNatureType val);

    NumberingPlanMapType getNumberingPlan();

    String getNumberingPlan_Value();

    void setNumberingPlan(NumberingPlanMapType val);

    SubscriberIdentityType getSubscriberIdentityType();

    String getSubscriberIdentityType_Value();

    void setSubscriberIdentityType(SubscriberIdentityType val);

    boolean isGetLocationInformation();

    void setGetLocationInformation(boolean val);

    boolean isGetSubscriberState();

    void setGetSubscriberState(boolean val);

    boolean isGetCurrentLocation();

    void setGetCurrentLocation(boolean val);

    AtiDomainType getGetRequestedDomain();

    String getGetRequestedDomain_Value();

    void setGetRequestedDomain(AtiDomainType val);

    boolean isGetImei();

    void setGetImei(boolean val);

    boolean isGetMsClassmark();

    void setGetMsClassmark(boolean val);

    boolean isGetMnpRequestedInfo();

    void setGetMnpRequestedInfo(boolean val);

    String getGsmSCFAddress();

    void setGsmSCFAddress(String val);


    void putAddressNature(String val);

    void putNumberingPlan(String val);

    void putSubscriberIdentityType(String val);

    void putGetRequestedDomain(String val);


    String performAtiRequest(String address);

    String getCurrentRequestDef();

    String closeCurrentDialog();

}
