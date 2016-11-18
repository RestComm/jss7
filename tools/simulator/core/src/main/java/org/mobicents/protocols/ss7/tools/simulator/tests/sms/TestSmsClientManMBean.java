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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface TestSmsClientManMBean {

    AddressNatureType getAddressNature();

    String getAddressNature_Value();

    void setAddressNature(AddressNatureType val);

    NumberingPlanMapType getNumberingPlan();

    String getNumberingPlan_Value();

    void setNumberingPlan(NumberingPlanMapType val);

    String getServiceCenterAddress();

    void setServiceCenterAddress(String val);

    MapProtocolVersion getMapProtocolVersion();

    String getMapProtocolVersion_Value();

    void setMapProtocolVersion(MapProtocolVersion val);

    SRIReaction getSRIReaction();

    String getSRIReaction_Value();

    void setSRIReaction(SRIReaction val);

    SRIInformServiceCenter getSRIInformServiceCenter();

    String getSRIInformServiceCenter_Value();

    void setSRIInformServiceCenter(SRIInformServiceCenter val);

    boolean isSRIScAddressNotIncluded();

    void setSRIScAddressNotIncluded(boolean val);

    MtFSMReaction getMtFSMReaction();

    String getMtFSMReaction_Value();

    void setMtFSMReaction(MtFSMReaction val);

    ReportSMDeliveryStatusReaction getReportSMDeliveryStatusReaction();

    String getReportSMDeliveryStatusReaction_Value();

    void setReportSMDeliveryStatusReaction(ReportSMDeliveryStatusReaction val);

    String getSRIResponseImsi();

    void setSRIResponseImsi(String val);

    String getSRIResponseVlr();

    void setSRIResponseVlr(String val);

    int getSmscSsn();

    void setSmscSsn(int val);

    int getNationalLanguageCode();

    void setNationalLanguageCode(int val);

    boolean isStatusReportRequest();

    void setStatusReportRequest(boolean val);

    TypeOfNumberType getTypeOfNumber();

    String getTypeOfNumber_Value();

    void setTypeOfNumber(TypeOfNumberType val);

    NumberingPlanIdentificationType getNumberingPlanIdentification();

    String getNumberingPlanIdentification_Value();

    void setNumberingPlanIdentification(NumberingPlanIdentificationType val);

    SmsCodingType getSmsCodingType();

    String getSmsCodingType_Value();

    void setSmsCodingType(SmsCodingType val);

    boolean isOneNotificationFor100Dialogs();

    void setOneNotificationFor100Dialogs(boolean val);

    boolean isReturn20PersDeliveryErrors();

    void setReturn20PersDeliveryErrors(boolean val);

    boolean isContinueDialog();

    void setContinueDialog(boolean val);


    void putAddressNature(String val);

    void putNumberingPlan(String val);

    void putMapProtocolVersion(String val);

    void putSRIReaction(String val);

    void putSRIInformServiceCenter(String val);

    void putMtFSMReaction(String val);

    void putReportSMDeliveryStatusReaction(String val);

    void putTypeOfNumber(String val);

    void putNumberingPlanIdentification(String val);

    void putSmsCodingType(String val);

    String getCurrentRequestDef();

    String performMoForwardSM(String msg, String destIsdnNumber, String origIsdnNumber);

    String performMoForwardSMPartial(String msg, String destIsdnNumber, String origIsdnNumber, int msgRef, int segmCnt, int segmNum);

    String performAlertServiceCentre(String destIsdnNumber);

    String closeCurrentDialog();

}
