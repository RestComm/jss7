/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

	public SmsCommandTpdu createSmsCommandTpdu(boolean statusReportRequest, int messageReference, ProtocolIdentifier protocolIdentifier,
			CommandType commandType, int messageNumber, AddressField destinationAddress, CommandData commandData);
	public SmsDeliverReportTpdu createSmsDeliverReportTpdu(FailureCause failureCause, ProtocolIdentifier protocolIdentifier, UserData userData);
	public SmsDeliverTpdu createSmsDeliverTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned, boolean replyPathExists, boolean statusReportIndication,
			AddressField originatingAddress, ProtocolIdentifier protocolIdentifier, AbsoluteTimeStamp serviceCentreTimeStamp, UserData userData);
	public SmsStatusReportTpdu createSmsStatusReportTpdu(boolean moreMessagesToSend, boolean forwardedOrSpawned, StatusReportQualifier statusReportQualifier,
			int messageReference, AddressField recipientAddress, AbsoluteTimeStamp serviceCentreTimeStamp, AbsoluteTimeStamp dischargeTime, Status status,
			ProtocolIdentifier protocolIdentifier, UserData userData);
	public SmsSubmitReportTpdu createSmsSubmitReportTpdu(FailureCause failureCause, AbsoluteTimeStamp serviceCentreTimeStamp,
			ProtocolIdentifier protocolIdentifier, UserData userData);
	public SmsSubmitTpdu createSmsSubmitTpdu(boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest, int messageReference,
			AddressField destinationAddress, ProtocolIdentifier protocolIdentifier, ValidityPeriod validityPeriod, UserData userData);

	public AbsoluteTimeStamp createAbsoluteTimeStamp(int year, int month, int day, int hour, int minute, int second, int timeZone);
	public AddressField createAddressField(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification, String addressValue);
	public CommandType createCommandType(int code);
	public CommandType createCommandType(CommandTypeValue value);
	public DataCodingScheme createDataCodingScheme(int code);
	public DataCodingScheme createDataCodingScheme(DataCodingGroup dataCodingGroup, DataCodingSchemaMessageClass messageClass,
			DataCodingSchemaIndicationType dataCodingSchemaIndicationType, Boolean setIndicationActive, CharacterSet characterSet, boolean isCompressed);
	public FailureCause createFailureCause(int code);
	public ParameterIndicator createParameterIndicator(boolean TP_UDLPresence, boolean getTP_DCSPresence, boolean getTP_PIDPresence);
	public ProtocolIdentifier createProtocolIdentifier(int code);
	public Status createStatus(int code);
	public ValidityEnhancedFormatData createValidityEnhancedFormatData(byte[] data);
	public ValidityPeriod createValidityPeriod(int relativeFormatValue);
	public ValidityPeriod createValidityPeriod(AbsoluteTimeStamp absoluteFormatValue);
	public ValidityPeriod createValidityPeriod(ValidityEnhancedFormatData enhancedFormatValue);

	public UserDataHeader createUserDataHeader();
	public UserData createUserData(byte[] encodedData, DataCodingScheme dataCodingScheme, int encodedUserDataLength, boolean encodedUserDataHeaderIndicator,
			Charset gsm8Charset);
	public UserData createUserData(String decodedMessage, DataCodingScheme dataCodingScheme, UserDataHeader decodedUserDataHeader, Charset gsm8Charset);
	public CommandData createCommandData(byte[] data);
	public CommandData createCommandData(String decodedMessage);

	public ConcatenatedShortMessagesIdentifier createConcatenatedShortMessagesIdentifier(boolean referenceIs16bit, int reference, int mesageSegmentCount,
			int mesageSegmentNumber);
	public NationalLanguageLockingShiftIdentifier createNationalLanguageLockingShiftIdentifier(NationalLanguageIdentifier nationalLanguageCode);
	public NationalLanguageSingleShiftIdentifier createNationalLanguageSingleShiftIdentifier(NationalLanguageIdentifier nationalLanguageCode);
}


