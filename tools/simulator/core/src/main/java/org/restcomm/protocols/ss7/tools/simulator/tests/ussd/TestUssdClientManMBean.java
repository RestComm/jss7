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

package org.restcomm.protocols.ss7.tools.simulator.tests.ussd;

import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.restcomm.protocols.ss7.tools.simulator.tests.sms.SRIReaction;

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

    String getSRIResponseImsi();

    void setSRIResponseImsi(String val);

    String getSRIResponseVlr();

    void setSRIResponseVlr(String val);

    SRIReaction getSRIReaction();

    String getSRIReaction_Value();

    void setSRIReaction(SRIReaction val);

    boolean isReturn20PersDeliveryErrors();

    void setReturn20PersDeliveryErrors(boolean val);


    String getCurrentRequestDef();

    String performProcessUnstructuredRequest(String msg);

    String performUnstructuredResponse(String msg);

    String sendUssdBusyResponse();

    String closeCurrentDialog();


    void putMsisdnAddressNature(String val);

    void putMsisdnNumberingPlan(String val);

    void putUssdClientAction(String val);

    void putSRIReaction(String val);

    void setAutoResponseString(String text);

    void setAutoResponseOnUnstructuredSSRequests(boolean selected);

    String getAutoResponseString();

    boolean isAutoResponseOnUnstructuredSSRequests();

}
