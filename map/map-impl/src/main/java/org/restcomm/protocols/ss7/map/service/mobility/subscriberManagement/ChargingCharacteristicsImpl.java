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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ChargingCharacteristicsImpl extends OctetStringBase implements ChargingCharacteristics {

    public static final int _FLAG_NORMAL_CHARGING = 0x08;
    public static final int _FLAG_PREPAID_CHARGING = 0x04;
    public static final int _FLAG_FLAT_RATE_CHARGING_CHARGING = 0x02;
    public static final int _FLAG_CHARGING_BY_HOT_BILLING_CHARGING = 0x01;

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public ChargingCharacteristicsImpl() {
        super(2, 2, "ChargingCharacteristics");
    }

    public ChargingCharacteristicsImpl(byte[] data) {
        super(2, 2, "ChargingCharacteristics", data);
    }

    public ChargingCharacteristicsImpl(boolean isNormalCharging, boolean isPrepaidCharging, boolean isFlatRateChargingCharging,
            boolean isChargingByHotBillingCharging) {
        super(2, 2, "ChargingCharacteristics");

        this.data = new byte[2];

        if (isNormalCharging)
            this.data[0] |= _FLAG_NORMAL_CHARGING;
        if (isPrepaidCharging)
            this.data[0] |= _FLAG_PREPAID_CHARGING;
        if (isFlatRateChargingCharging)
            this.data[0] |= _FLAG_FLAT_RATE_CHARGING_CHARGING;
        if (isChargingByHotBillingCharging)
            this.data[0] |= _FLAG_CHARGING_BY_HOT_BILLING_CHARGING;
    }

    public byte[] getData() {
        return data;
    }

    private boolean isDataGoodFormed() {
        if (this.data != null && this.data.length == 2)
            return true;
        else
            return false;
    }

    @Override
    public boolean isNormalCharging() {
        if (isDataGoodFormed() && (this.data[0] & _FLAG_NORMAL_CHARGING) != 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean isPrepaidCharging() {
        if (isDataGoodFormed() && (this.data[0] & _FLAG_PREPAID_CHARGING) != 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean isFlatRateChargingCharging() {
        if (isDataGoodFormed() && (this.data[0] & _FLAG_FLAT_RATE_CHARGING_CHARGING) != 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean isChargingByHotBillingCharging() {
        if (isDataGoodFormed() && (this.data[0] & _FLAG_CHARGING_BY_HOT_BILLING_CHARGING) != 0)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        if (isDataGoodFormed()) {
            boolean normalCharging = isNormalCharging();
            boolean prepaidCharging = isPrepaidCharging();
            boolean flatRateChargingCharging = isFlatRateChargingCharging();
            boolean chargingByHotBillingCharging = isChargingByHotBillingCharging();

            StringBuilder sb = new StringBuilder();
            sb.append(_PrimitiveName);
            sb.append(" [Data= ");
            sb.append(this.printDataArr());

            if (normalCharging) {
                sb.append(", normalCharging");
            }
            if (prepaidCharging) {
                sb.append(", prepaidCharging");
            }
            if (flatRateChargingCharging) {
                sb.append(", flatRateChargingCharging");
            }
            if (chargingByHotBillingCharging) {
                sb.append(", chargingByHotBillingCharging");
            }

            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ChargingCharacteristicsImpl> CHARGING_CHARACTERISTICS_XML = new XMLFormat<ChargingCharacteristicsImpl>(ChargingCharacteristicsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ChargingCharacteristicsImpl chargingCharacteristics) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                chargingCharacteristics.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(ChargingCharacteristicsImpl chargingCharacteristics, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (chargingCharacteristics.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(chargingCharacteristics.data));
            }
        }
    };
}
