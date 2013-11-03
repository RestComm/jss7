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

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class CalledPartyNumberTest extends ParameterHarness {

    /**
     * @throws IOException
     */
    public CalledPartyNumberTest() throws IOException {
        super.badBodies.add(new byte[1]);

        super.goodBodies.add(getBody1());
        super.goodBodies.add(getBody2());
    }

    private byte[] getBody1() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // we will use odd number of digits, so we leave zero as MSB

        bos.write(CalledPartyNumber._NAI_SUBSCRIBER_NUMBER);
        int v = CalledPartyNumberImpl._INN_ROUTING_ALLOWED << 7;
        v |= CalledPartyNumberImpl._NPI_ISDN << 4;
        bos.write(v);
        bos.write(super.getSixDigits());
        return bos.toByteArray();
    }

    private byte[] getBody2() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(CalledPartyNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7));
        int v = CalledPartyNumberImpl._INN_ROUTING_NOT_ALLOWED << 7;
        v |= CalledPartyNumberImpl._NPI_ISDN << 4;
        bos.write(v);
        bos.write(super.getFiveDigits());
        return bos.toByteArray();
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CalledPartyNumberImpl bci = new CalledPartyNumberImpl(getBody1());

        String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator",
                "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { CalledPartyNumberImpl._NPI_ISDN, CalledPartyNumberImpl._INN_ROUTING_ALLOWED,
                CalledPartyNumber._NAI_SUBSCRIBER_NUMBER, false, super.getSixDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        CalledPartyNumberImpl bci = new CalledPartyNumberImpl(getBody2());

        String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator",
                "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
        Object[] expectedValues = { CalledPartyNumberImpl._NPI_ISDN, CalledPartyNumberImpl._INN_ROUTING_NOT_ALLOWED,
                CalledPartyNumber._NAI_SUBSCRIBER_NUMBER, true, super.getFiveDigitsString() };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        CalledPartyNumberImpl original = new CalledPartyNumberImpl(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER, "664422",
                CalledPartyNumber._NPI_ISDN, CalledPartyNumber._NAI_NRNINNF);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "calledPartyNumber", CalledPartyNumberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CalledPartyNumberImpl copy = reader.read("calledPartyNumber", CalledPartyNumberImpl.class);

        assertEquals(copy.getNatureOfAddressIndicator(), original.getNatureOfAddressIndicator());
        assertEquals(copy.getAddress(), original.getAddress());
        assertEquals(copy.getNumberingPlanIndicator(), original.getNumberingPlanIndicator());
        assertEquals(copy.getInternalNetworkNumberIndicator(), original.getInternalNetworkNumberIndicator());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() {
        return new CalledPartyNumberImpl(0, "1", 1, 1);
    }

}
