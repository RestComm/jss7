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

package org.mobicents.protocols.ss7.map.smstpdu;

import org.mobicents.protocols.ss7.map.api.smstpdu.ConcatenatedShortMessagesIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ConcatenatedShortMessagesIdentifierImpl implements ConcatenatedShortMessagesIdentifier {

    private boolean referenceIs16bit;
    private int reference;
    private int mesageSegmentCount;
    private int mesageSegmentNumber;

    /**
     *
     * @param referenceIs16bit
     * @param reference Concatenated short message reference number. This octet shall contain a modulo 256 counter indicating
     *        the reference number for a particular concatenated short message. This reference number shall remain constant for
     *        every short message which makes up a particular concatenated short message.
     * @param mesageSegmentCount Maximum number of short messages in the concatenated short message. This octet shall contain a
     *        value in the range 0 to 255 indicating the total number of short messages within the concatenated short message.
     *        The value shall start at 1 and remain constant for every short message which makes up the concatenated short
     *        message. If the value is zero then the receiving entity shall ignore the whole Information Element.
     * @param mesageSegmentNumber Sequence number of the current short message. This octet shall contain a value in the range 0
     *        to 255 indicating the sequence number of a particular short message within the concatenated short message. The
     *        value shall start at 1 and increment by one for every short message sent within the concatenated short message. If
     *        the value is zero or the value is greater than the value in octet 2 then the receiving entity shall ignore the
     *        whole Information Element.
     */
    public ConcatenatedShortMessagesIdentifierImpl(boolean referenceIs16bit, int reference, int mesageSegmentCount,
            int mesageSegmentNumber) {
        this.referenceIs16bit = referenceIs16bit;
        this.reference = reference;
        this.mesageSegmentCount = mesageSegmentCount;
        this.mesageSegmentNumber = mesageSegmentNumber;
    }

    public ConcatenatedShortMessagesIdentifierImpl(byte[] encodedInformationElementData) {

        if (encodedInformationElementData == null || encodedInformationElementData.length < 3
                || encodedInformationElementData.length > 4)
            return;

        if (encodedInformationElementData.length == 3) {
            this.referenceIs16bit = false;
            this.reference = encodedInformationElementData[0] & 0xFF;
            this.mesageSegmentCount = encodedInformationElementData[1] & 0xFF;
            this.mesageSegmentNumber = encodedInformationElementData[2] & 0xFF;
        } else {
            this.referenceIs16bit = true;
            this.reference = (encodedInformationElementData[0] & 0xFF) + ((encodedInformationElementData[1] & 0xFF) << 8);
            this.mesageSegmentCount = encodedInformationElementData[2] & 0xFF;
            this.mesageSegmentNumber = encodedInformationElementData[3] & 0xFF;
        }
    }

    public boolean getReferenceIs16bit() {
        return referenceIs16bit;
    }

    public int getReference() {
        return reference;
    }

    public int getMesageSegmentCount() {
        return mesageSegmentCount;
    }

    public int getMesageSegmentNumber() {
        return mesageSegmentNumber;
    }

    public int getEncodedInformationElementIdentifier() {
        if (this.getReferenceIs16bit())
            return UserDataHeader._InformationElementIdentifier_ConcatenatedShortMessages16bit;
        else
            return UserDataHeader._InformationElementIdentifier_ConcatenatedShortMessages8bit;
    }

    public byte[] getEncodedInformationElementData() {
        byte[] res;
        if (this.referenceIs16bit) {
            res = new byte[4];
            res[0] = (byte) (this.reference & 0x00FF);
            res[1] = (byte) ((this.reference & 0xFF00) >> 8);
            res[2] = (byte) (this.mesageSegmentCount & 0xFF);
            res[3] = (byte) (this.mesageSegmentNumber & 0xFF);
        } else {
            res = new byte[3];
            res[0] = (byte) (this.reference & 0x00FF);
            res[1] = (byte) (this.mesageSegmentCount & 0xFF);
            res[2] = (byte) (this.mesageSegmentNumber & 0xFF);
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConcatenatedShortMessagesIdentifier [");
        if (this.referenceIs16bit)
            sb.append("Reference=16bit");
        else
            sb.append("Reference=8bit");
        sb.append(", reference=");
        sb.append(this.reference);
        sb.append(", mesagePartsCount=");
        sb.append(this.mesageSegmentCount);
        sb.append(", mesagePartNumber=");
        sb.append(this.mesageSegmentNumber);
        sb.append("]");

        return sb.toString();
    }
}
