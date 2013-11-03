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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.Number;

/**
 * Start time:18:44:10 2009-03-27<br>
 * Project: mobicents-isup-stack<br>
 * This class is super classes for all message parameters with form of:
 *
 * <pre>
 *    ________
 *   | Header |
 *   |________|
 *   |  Body  |
 *   |________|
 *   | Digits |
 *   |________|
 *
 *
 * </pre>
 *
 * Where Header has 1+ bytes, body 1+ byte, and digits part contains pairs of digits encoded from right to left from most
 * significant digits in number. Examples parameters are(from Q763): 3.16 Connected Number,3.9 Called party number, 3.10 Calling
 * party number, 3.26 Generic number, 3.30 Location number. Also (3.39,) Implemetnation class must fill tag variable with proper
 * information. Length part of information component is computed from length of this element. See section (B1, B2 and B3 of
 * Q.763)
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AbstractNumber extends AbstractISUPParameter implements Number {

    private static final String ADDRESS = "address";

    private static final String DEFAULT_ADDRESS = "";

    protected Logger logger = Logger.getLogger(this.getClass());
    /**
     * Holds odd flag, it can have either value: 10000000(x80) or 00000000. For each it takes value 1 and 0;
     */
    protected int oddFlag;

    /**
     * indicates odd flag value (0x80) as integer (1). This is achieved when odd flag in parameter is moved to the right by
     * seven possitions ( >> 7)
     */
    public static final int _FLAG_ODD = 1;

    /**
     * Holds digits(in specs: "signal"). digits[0] holds most siginificant digit. Also length of this table contains information
     * about Odd/even flag. However there is distinct flag used in process of decoding from byte[]. This is becuse in case of
     * decoding we dont know if last digit is filler or digit.
     */
    protected String address;

    public AbstractNumber() {
        super();

    }

    public AbstractNumber(byte[] representation) throws ParameterException {
        super();

        decode(representation);

    }

    public AbstractNumber(ByteArrayInputStream bis) throws ParameterException {
        super();

        this.decode(bis);
    }

    public AbstractNumber(String address) {
        super();
        this.setAddress(address);
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
        count += decodeBody(bis);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding body, read count: " + count);
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding digits");
        }
        count += decodeDigits(bis);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding digits, read count: " + count);
        }
        return count;
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
        count += encodeBody(bos);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding body, write count: " + count);
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits");
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
        count += encodeBody(bos);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding body, write count: " + count);
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits");
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

    /**
     * This method is used in constructor that takes byte[] or ByteArrayInputStream as parameter. Decodes header part (its 1 or
     * 2 bytes usually.) Default implemetnation decodes header of one byte - where most significant bit is O/E indicator and
     * bits 7-1 are NAI. This method should be over
     *
     * @param bis
     * @return - number of bytes reads
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public int decodeHeader(ByteArrayInputStream bis) throws ParameterException {
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = bis.read() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;

        return 1;
    }

    /**
     * This method is used in constructor that takes byte[] or ByteArrayInputStream as parameter. Decodes body part (its 1 byte
     * usually.) However in different "numbers" it has different meaning. Each subclass should provide implementation
     *
     * @param bis
     * @return - number of bytes reads throws IllegalArgumentException - thrown if read error is encountered.
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public abstract int decodeBody(ByteArrayInputStream bis) throws ParameterException;

    /**
     * This method is used in constructor that takes byte[] or ByteArrayInputStream as parameter. Decodes digits part.
     *
     * @param bis
     * @return - number of bytes reads
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public int decodeDigits(ByteArrayInputStream bis) throws ParameterException {
        if(skipDigits()){
            return 0;
        }
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }

        // FIXME: we could spare time by passing length arg - or getting it from
        // bis??
        int count = 0;
        try {
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

            if (oddFlag != _FLAG_ODD) {
                address += Integer.toHexString((b & 0xf0) >> 4);
            }
            this.setAddress(address);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return count;
    }

    /**
     * Decodes digits part. It reads exactly octetsCount number of octets.
     *
     * @param bis
     * @param octetsCount - number iof octets to read from stream
     * @return
     * @throws IllegalArgumentException
     */
    public int decodeDigits(ByteArrayInputStream bis, int octetsCount) throws ParameterException {
        if(skipDigits()){
            return 0;
        }
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int count = 0;
        try {
            address = "";
            int b = 0;
            // while (octetsCount != count - 1 && bis.available() - 1 > 0) {
            while (octetsCount - 1 != count && bis.available() - 1 > 0) {
                b = (byte) bis.read();
                count++;

                int d1 = b & 0x0f;
                int d2 = (b & 0xf0) >> 4;

                address += Integer.toHexString(d1) + Integer.toHexString(d2);

            }

            b = bis.read() & 0xff;
            count++;
            address += Integer.toHexString((b & 0x0f));

            if (oddFlag != _FLAG_ODD) {
                address += Integer.toHexString((b & 0xf0) >> 4);
            }
            this.setAddress(address);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        if (octetsCount != count) {
            throw new ParameterException("Failed to read [" + octetsCount + "], encountered only [" + count + "]");
        }
        return count;
    }

    /**
     * This method is used in encode method. It encodes header part (1 or 2 bytes usually.)
     *
     * @param bis
     * @return - number of bytes encoded.
     */
    public int encodeHeader(ByteArrayOutputStream bos) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        bos.write(b);

        return 1;
    }

    /**
     * This methods is used in encode method. It encodes body. Each subclass shoudl provide implementetaion.
     *
     * @param bis
     * @return - number of bytes reads
     *
     */
    public abstract int encodeBody(ByteArrayOutputStream bos);

    /**
     * This method is used in encode. Encodes digits part. This is because
     *
     * @param bos - where digits will be encoded
     * @return - number of bytes encoded
     *
     */
    public int encodeDigits(ByteArrayOutputStream bos) {
        if(skipDigits()){
            return 0;
        }
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

    public boolean isOddFlag() {
        return oddFlag == _FLAG_ODD;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        if(address!=null){
            // lets compute odd flag
            oddFlag = this.address.length() % 2;
        } else {
            oddFlag = 0;
        }
    }

    protected boolean skipDigits(){
        return false;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<AbstractNumber> ISUP_ABSTRACT_NUMBER_XML = new XMLFormat<AbstractNumber>(
            AbstractNumber.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AbstractNumber abstractNumber) throws XMLStreamException {
            abstractNumber.setAddress(xml.getAttribute(ADDRESS, DEFAULT_ADDRESS));
        }

        @Override
        public void write(AbstractNumber abstractNumber, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(ADDRESS, abstractNumber.address);
        }
    };

}
