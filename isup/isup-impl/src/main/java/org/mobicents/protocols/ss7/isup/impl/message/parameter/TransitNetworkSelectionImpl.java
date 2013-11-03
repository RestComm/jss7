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

/**
 * Start time:17:05:31 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.TransitNetworkSelection;

/**
 * Start time:17:05:31 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TransitNetworkSelectionImpl extends AbstractISUPParameter implements TransitNetworkSelection {

    protected static final Logger logger = Logger.getLogger(TransitNetworkSelectionImpl.class);

    // FIXME: Oleg is this correct?
    private String address;
    private int typeOfNetworkIdentification;
    private int networkIdentificationPlan;
    /**
     * Holds odd flag, it can have either value: 10000000(x80) or 00000000. For each it takes value 1 and 0;
     */
    protected int oddFlag;

    /**
     * indicates odd flag value (0x80) as integer (1). This is achieved when odd flag in parameter is moved to the right by
     * sever possitions ( >> 7)
     */
    public static final int _FLAG_ODD = 1;

    public TransitNetworkSelectionImpl(String address, int typeOfNetworkIdentification, int networkIdentificationPlan) {
        super();
        this.setAddress(address);
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
        this.networkIdentificationPlan = networkIdentificationPlan;
    }

    public TransitNetworkSelectionImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public TransitNetworkSelectionImpl() {
        super();

    }

    public byte[] encode() throws ParameterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding header");
        }
        int count = encodeHeader(bos);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding header, write count: " + count);
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding body");
        }

        count += encodeDigits(bos);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits, write count: " + count);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // out.write(tag);
        // Util.encodeLength(count, out);
        try {
            out.write(bos.toByteArray());
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return out.toByteArray();
    }

    public int encode(ByteArrayOutputStream out) throws ParameterException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding header");
        }
        int count = encodeHeader(bos);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding header, write count: " + count);
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding body");
        }

        count += encodeDigits(bos);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits, write count: " + count);
        }

        // count += tag.length;
        // out.write(tag);
        // count += Util.encodeLength(count, out);
        try {
            out.write(bos.toByteArray());
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return count;
    }

    public int decode(byte[] b) throws ParameterException {
        ByteArrayInputStream bis = new ByteArrayInputStream(b);

        return this.decode(bis);
    }

    protected int decode(ByteArrayInputStream bis) throws ParameterException {
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding header");
        }

        int count = decodeHeader(bis);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding header, read count: " + count);
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding body");
        }

        count += decodeDigits(bis);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding digits, read count: " + count);
        }
        return count;
    }

    /**
     * This method is used in encode. Encodes digits part. This is because
     *
     * @param bos - where digits will be encoded
     * @return - number of bytes encoded
     *
     */
    public int encodeDigits(ByteArrayOutputStream bos) {
        boolean isOdd = this.oddFlag == _FLAG_ODD;

        byte b = 0;
        int count = (!isOdd) ? address.length() : address.length() - 1;
        int bytesCount = 0;
        for (int i = 0; i < count - 1; i += 2) {
            String ds1 = address.substring(i, i + 1);
            String ds2 = address.substring(i + 1, i + 2);

            int d1 = Integer.parseInt(ds1, 16);
            int d2 = Integer.parseInt(ds2, 16);

            b = (byte) (d2 << 4 | d1);
            bos.write(b);
            bytesCount++;
        }

        if (isOdd) {
            String ds1 = address.substring(count, count + 1);
            int d = Integer.parseInt(ds1);

            b = (byte) (d & 0x0f);
            bos.write(b);
            bytesCount++;
        }

        return bytesCount;
    }

    /**
     * This method is used in constructor that takes byte[] or ByteArrayInputStream as parameter. Decodes digits part. Stores
     * result in digits field, where digits[0] holds most significant digit. This is because
     *
     * @param bis
     * @return - number of bytes reads throws IllegalArgumentException - thrown if read error is encountered.
     * @throws ParameterException - thrown if read error is encountered.
     */
    public int decodeDigits(ByteArrayInputStream bis) throws ParameterException {
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }
        // FIXME: we could spare time by passing length arg - or getting it from
        // bis??
        int count = 0;
        address = "";
        int b = 0;
        while (bis.available() - 1 > 0) {
            b = (byte) bis.read();

            int d1 = b & 0x0f;
            int d2 = (b & 0xf0) >> 4;

            address += Integer.toHexString(d1) + Integer.toHexString(d2);

        }

        b = bis.read() & 0xff;
        address += Integer.toHexString((b & 0x0f));

        if (oddFlag != 1) {
            address += Integer.toHexString((b & 0xf0) >> 4);
        }
        return count;
    }

    /**
     * This method is used in constructor that takes byte[] or ByteArrayInputStream as parameter. Decodes header part (its 1 or
     * 2 bytes usually.) Default implemetnation decodes header of one byte - where most significant bit is O/E indicator and
     * bits 7-1 are NAI. This method should be over
     *
     * @param bis
     * @return - number of bytes reads
     * @throws ParameterException - thrown if read error is encountered.
     */
    public int decodeHeader(ByteArrayInputStream bis) throws ParameterException {
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = bis.read() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.setTypeOfNetworkIdentification((b >> 4));
        this.setNetworkIdentificationPlan(b);
        return 1;
    }

    /**
     * This method is used in encode method. It encodes header part (1 or 2 bytes usually.)
     *
     * @param bis
     * @return - number of bytes encoded.
     */
    public int encodeHeader(ByteArrayOutputStream bos) {
        int b = this.networkIdentificationPlan & 0x0F;
        b |= (this.typeOfNetworkIdentification & 0x07) << 4;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        bos.write(b);

        return 1;
    }

    public int getTypeOfNetworkIdentification() {
        return typeOfNetworkIdentification;
    }

    public void setTypeOfNetworkIdentification(int typeOfNetworkIdentification) {
        this.typeOfNetworkIdentification = typeOfNetworkIdentification & 0x07;
    }

    public int getNetworkIdentificationPlan() {
        return networkIdentificationPlan;
    }

    public void setNetworkIdentificationPlan(int networkIdentificationPlan) {
        this.networkIdentificationPlan = networkIdentificationPlan & 0x0F;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        oddFlag = this.address.length() % 2;
    }

    public boolean isOddFlag() {
        return oddFlag == _FLAG_ODD;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
