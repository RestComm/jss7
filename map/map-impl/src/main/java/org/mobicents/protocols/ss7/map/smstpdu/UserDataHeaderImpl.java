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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mobicents.protocols.ss7.map.api.smstpdu.ConcatenatedShortMessagesIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageLockingShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageSingleShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeaderElement;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserDataHeaderImpl implements UserDataHeader {

    private Map<Integer, byte[]> data = new HashMap<Integer, byte[]>();

    public UserDataHeaderImpl() {
    }

    public UserDataHeaderImpl(byte[] encodedData) {
        if (encodedData == null || encodedData.length < 1)
            return;
        int udhl = encodedData[0] & 0xFF;
        if (udhl > encodedData.length)
            udhl = encodedData.length;
        int ind = 1;
        while (ind <= udhl - 2) {
            int id = encodedData[ind++];
            int len = encodedData[ind++];
            if (len <= udhl + 1 - ind) {
                byte[] buf = new byte[len];
                System.arraycopy(encodedData, ind, buf, 0, len);
                ind += len;

                data.put(id, buf);
            }
        }
    }

    public byte[] getEncodedData() {

        if (data.size() == 0)
            return null;

        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        stm.write(0);
        for (int id : data.keySet()) {
            byte[] buf = data.get(id);

            stm.write(id);
            if (buf == null)
                stm.write(0);
            else {
                stm.write(buf.length);
                try {
                    stm.write(buf);
                } catch (IOException e) {
                    // This should never occur
                }
            }
        }

        byte[] res = stm.toByteArray();
        res[0] = (byte) (res.length - 1);
        return res;
    }

    public Map<Integer, byte[]> getAllData() {
        return data;
    }

    public void addInformationElement(int informationElementIdentifier, byte[] encodedData) {
        this.data.put(informationElementIdentifier, encodedData);
    }

    public void addInformationElement(UserDataHeaderElement informationElement) {
        this.data.put(informationElement.getEncodedInformationElementIdentifier(),
                informationElement.getEncodedInformationElementData());
    }

    public byte[] getInformationElementData(int informationElementIdentifier) {
        return this.data.get(informationElementIdentifier);
    }

    public NationalLanguageLockingShiftIdentifier getNationalLanguageLockingShift() {
        byte[] buf = this.data.get(_InformationElementIdentifier_NationalLanguageLockingShift);
        if (buf != null && buf.length == 1)
            return new NationalLanguageLockingShiftIdentifierImpl(buf);
        else
            return null;
    }

    public NationalLanguageSingleShiftIdentifier getNationalLanguageSingleShift() {
        byte[] buf = this.data.get(_InformationElementIdentifier_NationalLanguageSingleShift);
        if (buf != null && buf.length == 1)
            return new NationalLanguageSingleShiftIdentifierImpl(buf);
        else
            return null;
    }

    public ConcatenatedShortMessagesIdentifier getConcatenatedShortMessagesIdentifier() {
        byte[] buf = this.data.get(_InformationElementIdentifier_ConcatenatedShortMessages16bit);
        if (buf != null && buf.length == 4)
            return new ConcatenatedShortMessagesIdentifierImpl(buf);
        else {
            buf = this.data.get(_InformationElementIdentifier_ConcatenatedShortMessages8bit);
            if (buf != null && buf.length == 3)
                return new ConcatenatedShortMessagesIdentifierImpl(buf);
            else
                return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserDataHeader [");
        boolean isFirst = true;
        for (int id : data.keySet()) {
            byte[] buf = data.get(id);

            if (isFirst)
                isFirst = false;
            else
                sb.append("\n\t");
            sb.append(id);
            sb.append(" = ");
            sb.append(printDataArr(buf));
        }
        sb.append("]");

        return sb.toString();
    }

    private String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }
}
