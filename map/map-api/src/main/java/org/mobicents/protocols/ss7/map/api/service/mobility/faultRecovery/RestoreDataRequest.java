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

package org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;

/**
 * <p>
 * The VLR uses MAP RD when it needs to reload its subscription data. The RD procedure is not triggered by subscriber action,
 * such as registration in MSC. The handling of the RD procedure is similar to the handling of the location update procedure.
 * That is, the VLR indicates to HLR which CAMEL capability is supported in the VLR, so the HLR can decide whether it can send
 * CAMEL data to the VLR for that subscriber
 * </p>
 *
 * MAP V2-3:
 *
 * MAP V3: restoreData OPERATION ::= { --Timer m ARGUMENT RestoreDataArg RESULT RestoreDataRes ERRORS { systemFailure |
 * dataMissing | unexpectedDataValue | unknownSubscriber} CODE local:57 }
 *
 * MAP V2: RestoreData ::= OPERATION--Timer m ARGUMENT restoreDataArg RestoreDataArg RESULT restoreDataRes RestoreDataRes ERRORS
 * { SystemFailure, DataMissing, UnexpectedDataValue, UnknownSubscriber}
 *
 * MAP V3: RestoreDataArg ::= SEQUENCE { imsi IMSI, lmsi LMSI OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ... ,
 * vlr-Capability [6] VLR-Capability OPTIONAL, restorationIndicator [7] NULL OPTIONAL }
 *
 * MAP V2: RestoreDataArg ::= SEQUENCE { imsi IMSI, lmsi LMSI OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RestoreDataRequest extends MobilityMessage {

    IMSI getImsi();

    LMSI getLmsi();

    VLRCapability getVLRCapability();

    MAPExtensionContainer getExtensionContainer();

    boolean getRestorationIndicator();

}
