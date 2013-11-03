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

package org.mobicents.protocols.ss7.map.api;

import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.mobicents.protocols.ss7.map.api.smstpdu.CommandData;
import org.mobicents.protocols.ss7.map.api.smstpdu.CommandType;
import org.mobicents.protocols.ss7.map.api.smstpdu.CommandTypeValue;
import org.mobicents.protocols.ss7.map.api.smstpdu.ConcatenatedShortMessagesIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingGroup;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingSchemaIndicationType;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.FailureCause;
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageLockingShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageSingleShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsCommandTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsStatusReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.Status;
import org.mobicents.protocols.ss7.map.api.smstpdu.StatusReportQualifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityEnhancedFormatData;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPSmsTpduParameterFactory {

    SmsCommandTpdu createSmsCommandTpdu(boolean statusReportRequest, int messageReference,
            ProtocolIdentifier protocolIdentifier, CommandType commandType, int messageNumber, AddressField destinationAddress,
            CommandData commandData);

    SmsDeliverReportTpdu createSmsDeliverReportTpdu(FailureCause failureCause, ProtocolIdentifier protocolIdentifier,
            UserData userData);

    SmsDeliverTpdu createSmsDeliverTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists,
            boolean statusReportIndication, AddressField originatingAddress, ProtocolIdentifier protocolIdentifier,
            AbsoluteTimeStamp serviceCentreTimeStamp, UserData userData);

    SmsStatusReportTpdu createSmsStatusReportTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned,
            StatusReportQualifier statusReportQualifier, int messageReference, AddressField recipientAddress,
            AbsoluteTimeStamp serviceCentreTimeStamp, AbsoluteTimeStamp dischargeTime, Status status,
            ProtocolIdentifier protocolIdentifier, UserData userData);

    SmsSubmitReportTpdu createSmsSubmitReportTpdu(FailureCause failureCause, AbsoluteTimeStamp serviceCentreTimeStamp,
            ProtocolIdentifier protocolIdentifier, UserData userData);

    SmsSubmitTpdu createSmsSubmitTpdu(boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest,
            int messageReference, AddressField destinationAddress, ProtocolIdentifier protocolIdentifier,
            ValidityPeriod validityPeriod, UserData userData);

    AbsoluteTimeStamp createAbsoluteTimeStamp(int year, int month, int day, int hour, int minute, int second,
            int timeZone);

    AddressField createAddressField(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification,
            String addressValue);

    CommandType createCommandType(int code);

    CommandType createCommandType(CommandTypeValue value);

    DataCodingScheme createDataCodingScheme(int code);

    DataCodingScheme createDataCodingScheme(DataCodingGroup dataCodingGroup, DataCodingSchemaMessageClass messageClass,
            DataCodingSchemaIndicationType dataCodingSchemaIndicationType, Boolean setIndicationActive,
            CharacterSet characterSet, boolean isCompressed);

    FailureCause createFailureCause(int code);

    ParameterIndicator createParameterIndicator(boolean TP_UDLPresence, boolean getTP_DCSPresence,
            boolean getTP_PIDPresence);

    ProtocolIdentifier createProtocolIdentifier(int code);

    Status createStatus(int code);

    ValidityEnhancedFormatData createValidityEnhancedFormatData(byte[] data);

    ValidityPeriod createValidityPeriod(int relativeFormatValue);

    ValidityPeriod createValidityPeriod(AbsoluteTimeStamp absoluteFormatValue);

    ValidityPeriod createValidityPeriod(ValidityEnhancedFormatData enhancedFormatValue);

    UserDataHeader createUserDataHeader();

    UserData createUserData(byte[] encodedData, DataCodingScheme dataCodingScheme, int encodedUserDataLength,
            boolean encodedUserDataHeaderIndicator, Charset gsm8Charset);

    UserData createUserData(String decodedMessage, DataCodingScheme dataCodingScheme,
            UserDataHeader decodedUserDataHeader, Charset gsm8Charset);

    CommandData createCommandData(byte[] data);

    CommandData createCommandData(String decodedMessage);

    ConcatenatedShortMessagesIdentifier createConcatenatedShortMessagesIdentifier(boolean referenceIs16bit,
            int reference, int mesageSegmentCount, int mesageSegmentNumber);

    NationalLanguageLockingShiftIdentifier createNationalLanguageLockingShiftIdentifier(
            NationalLanguageIdentifier nationalLanguageCode);

    NationalLanguageSingleShiftIdentifier createNationalLanguageSingleShiftIdentifier(
            NationalLanguageIdentifier nationalLanguageCode);
}
