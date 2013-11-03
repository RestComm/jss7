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
package org.mobicents.protocols.ss7.map.primitives;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.NAEACIC;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkIdentificationPlanValue;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkIdentificationTypeValue;

/**
 *
 *
 ___________________________________________________________ | | 8 | 7 | 6 | 5 | 4 | 3 | 2 | 1 |
 * |___________|_____|_____|_____|_____|_____|_____|_____|_____| | Octet 1: |Spare| Type of net idn | network idn plan |
 * |___________|_____|_________________|_______________________| | Octet 2: | DIGIT 2 | DIGIT 1 | |
 * __________|_______________________|_______________________| | Octet 3: | DIGIT 4(or 0000) | DIGIT 3 |
 * |___________|_______________________|_______________________|
 *
 *
 * @author Lasith Waruna Perera
 *
 */
public class NAEACICImpl extends OctetStringBase implements NAEACIC {

    protected static final int NETWORK_IND_PLAN_MASK = 0x0F;
    protected static final int NETWORK_IND_TYPE_MASK = 0x70;
    protected static final int THREE_OCTET_CARRIER_CODE_MASK = 0x0F;

    public NAEACICImpl() {
        super(3, 3, "NAEACIC");
    }

    public NAEACICImpl(byte[] data) {
        super(3, 3, "NAEACIC", data);
    }

    public NAEACICImpl(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException {
        super(3, 3, "NAEACIC");
        setParameters(carrierCode, networkIdentificationPlanValue, networkIdentificationTypeValue);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public String getCarrierCode() {

        if (this.data == null || this.data.length == 0)
            return null;

        try {
            ByteArrayInputStream stm = new ByteArrayInputStream(this.data);
            stm.read();
            String address = TbcdString.decodeString(stm, this.data.length - 1);
            if (address.length() == 4
                    && this.getNetworkIdentificationPlanValue().equals(
                            NetworkIdentificationPlanValue.threeDigitCarrierIdentification)) {
                return address.substring(0, 3);
            }
            return address;
        } catch (MAPParsingComponentException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public NetworkIdentificationPlanValue getNetworkIdentificationPlanValue() {
        if (this.data == null || this.data.length == 0)
            return null;

        int planValue = this.data[0];
        return NetworkIdentificationPlanValue.getInstance(planValue & NETWORK_IND_PLAN_MASK);
    }

    public NetworkIdentificationTypeValue getNetworkIdentificationTypeValue() {

        if (this.data == null || this.data.length == 0)
            return null;

        int typeValue = this.data[0];
        typeValue = ((typeValue & NETWORK_IND_TYPE_MASK) >> 4);
        return NetworkIdentificationTypeValue.getInstance(typeValue);
    }

    private void setParameters(String carrierCode, NetworkIdentificationPlanValue networkIdentificationPlanValue,
            NetworkIdentificationTypeValue networkIdentificationTypeValue) throws MAPException {

        if (carrierCode == null || networkIdentificationPlanValue == null || networkIdentificationTypeValue == null)
            throw new MAPException("Error when encoding " + _PrimitiveName
                    + ": carrierCode, networkIdentificationPlanValue or networkIdentificationTypeValue is empty");

        if (!(carrierCode.length() == 3 || carrierCode.length() == 4))
            throw new MAPException("Error when encoding " + _PrimitiveName + ": carrierCode lenght should be 3 or 4");

        ByteArrayOutputStream stm = new ByteArrayOutputStream();

        int octOne = 0;
        octOne = octOne | (networkIdentificationTypeValue.getCode() << 4);
        octOne = octOne | networkIdentificationPlanValue.getCode();

        stm.write(octOne);

        try {
            TbcdString.encodeString(stm, carrierCode);
        } catch (MAPException e) {
            throw new MAPException(e);
        }

        this.data = stm.toByteArray();

        if (carrierCode.length() == 3) {
            this.data[2] = (byte) (this.data[2] & THREE_OCTET_CARRIER_CODE_MASK);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.getNetworkIdentificationPlanValue() != null) {
            sb.append("NetworkIdentificationPlanValue=");
            sb.append(this.getNetworkIdentificationPlanValue());
        }
        if (this.getNetworkIdentificationTypeValue() != null) {
            sb.append(", NetworkIdentificationTypeValue=");
            sb.append(this.getNetworkIdentificationTypeValue());
        }
        if (this.getCarrierCode() != null) {
            sb.append(", CarrierCode=");
            sb.append(this.getCarrierCode());
        }
        sb.append("]");

        return sb.toString();
    }

}
