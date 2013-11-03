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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface TestUssdClientManMBean {

    String getMsisdnAddress();

    void setMsisdnAddress(String val);

    AddressNatureType getMsisdnAddressNature();

    String getMsisdnAddressNature_Value();

    void setMsisdnAddressNature(AddressNatureType val);

    NumberingPlanMapType getMsisdnNumberingPlan();

    String getMsisdnNumberingPlan_Value();

    void setMsisdnNumberingPlan(NumberingPlanMapType val);

    int getDataCodingScheme();

    void setDataCodingScheme(int val);

    int getAlertingPattern();

    void setAlertingPattern(int val);

    UssdClientAction getUssdClientAction();

    String getUssdClientAction_Value();

    void setUssdClientAction(UssdClientAction val);

    String getAutoRequestString();

    void setAutoRequestString(String val);

    int getMaxConcurrentDialogs();

    void setMaxConcurrentDialogs(int val);

    boolean isOneNotificationFor100Dialogs();

    void setOneNotificationFor100Dialogs(boolean val);

    String getCurrentRequestDef();

    String performProcessUnstructuredRequest(String msg);

    String performUnstructuredResponse(String msg);

    String closeCurrentDialog();

    void putMsisdnAddressNature(String val);

    void putMsisdnNumberingPlan(String val);

    void putUssdClientAction(String val);

}
