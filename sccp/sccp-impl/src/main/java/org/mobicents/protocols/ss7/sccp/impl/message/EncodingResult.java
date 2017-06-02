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

package org.mobicents.protocols.ss7.sccp.impl.message;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum EncodingResult {

    // Successed encoding, data available for transfer
    Success,
    // Sccp error, returnCause should be returned or message discarded
    ReturnFailure,
    // Exception: data==null or has zero length
    DataMissed,
    // Exception: data length exceeds maximum possible safe length (2560) or corresponding max data size for used message type
    DataMaxLengthExceeded,
    // Exception: CalledPartyAddress is missed
    CalledPartyAddressMissing,
    // Exception: CallingPartyAddress is missed
    CallingPartyAddressMissing,
    // Exception: ProtocolClass is missed
    ProtocolClassMissing,
    // Exception: SourceLocalReferenceNumber is missing
    SourceLocalReferenceNumberMissing,
    // Exception: MessageTypeMissing
    MessageTypeMissing,
    // Exception: DestinationLocalReferenceNumber is missing
    DestinationLocalReferenceNumberMissing,
    // Exception: ReleaseCause is missing
    ReleaseCauseMissing,
    // Exception: RefusalCause is missing
    RefusalCauseMissing,
    // Exception: SegmentingReassembling is missing
    SegmentingReassemblingMissing,
    // Exception: ReceiveSequenceNumber is missing
    ReceiveSequenceNumberMissing,
    // Exception: Credit is missing
    CreditMissing,
    // Exception: ResetCause is missing
    ResetCauseMissing,
    // Exception: ErrorCause is missing
    ErrorCauseMissing,
    // Exception: SequencingSegmenting is missing
    SequencingSegmentingMissing,
    SegmentationNotSupported
}
