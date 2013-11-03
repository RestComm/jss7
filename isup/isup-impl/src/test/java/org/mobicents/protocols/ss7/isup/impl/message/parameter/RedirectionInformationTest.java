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
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Start time:20:07:45 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class RedirectionInformationTest extends ParameterHarness {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private byte[] getData() {
        return new byte[] { 35, 20 };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        RedirectionInformationImpl prim = new RedirectionInformationImpl();
        prim.decode(getData());

        assertEquals(prim.getRedirectingIndicator(), RedirectionInformation._RI_CALL_D);
        assertEquals(prim.getOriginalRedirectionReason(), RedirectionInformation._ORR_NO_REPLY);
        assertEquals(prim.getRedirectionCounter(), 4);
        assertEquals(prim.getRedirectionReason(), RedirectionInformation._RI_CALL_REROUTED);
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        RedirectionInformationImpl prim = new RedirectionInformationImpl(RedirectionInformation._RI_CALL_D,
                RedirectionInformation._ORR_NO_REPLY, 4, RedirectionInformation._RI_CALL_REROUTED);
        // int redirectingIndicator, int originalRedirectionReason, int redirectionCounter, int redirectionReason

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        RedirectionInformationImpl original = new RedirectionInformationImpl(RedirectionInformation._RI_CALL_D,
                RedirectionInformation._ORR_NO_REPLY, 4, RedirectionInformation._RI_CALL_REROUTED);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "redirectionInformation", RedirectionInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        RedirectionInformationImpl copy = reader.read("redirectionInformation", RedirectionInformationImpl.class);

        assertEquals(copy.getRedirectingIndicator(), original.getRedirectingIndicator());
        assertEquals(copy.getOriginalRedirectionReason(), original.getOriginalRedirectionReason());
        assertEquals(copy.getRedirectionCounter(), original.getRedirectionCounter());
        assertEquals(copy.getRedirectionReason(), original.getRedirectionReason());
    }

    public RedirectionInformationTest() {
        super();
        super.goodBodies.add(new byte[] { (byte) 0xC5, (byte) 0x03 });

        super.badBodies.add(new byte[] { (byte) 0xC5, (byte) 0x0F });
        super.badBodies.add(new byte[1]);
        super.badBodies.add(new byte[3]);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        RedirectionInformationImpl bci = new RedirectionInformationImpl(getBody(RedirectionInformationImpl._RI_CALL_D_RNPR,
                RedirectionInformationImpl._ORR_UNA, 1, RedirectionInformationImpl._RR_DEFLECTION_IE));

        String[] methodNames = { "getRedirectingIndicator", "getOriginalRedirectionReason", "getRedirectionCounter",
                "getRedirectionReason" };
        Object[] expectedValues = { RedirectionInformationImpl._RI_CALL_D_RNPR, RedirectionInformationImpl._ORR_UNA, 1,
                RedirectionInformationImpl._RR_DEFLECTION_IE };
        super.testValues(bci, methodNames, expectedValues);
    }

    private byte[] getBody(int riCallDRnpr, int orrUna, int counter, int rrDeflectionIe) {
        byte[] b = new byte[2];

        b[0] = (byte) riCallDRnpr;
        b[0] |= orrUna << 4;

        b[1] = (byte) counter;
        b[1] |= rrDeflectionIe << 4;
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent()
     */

    public AbstractISUPParameter getTestedComponent() throws IllegalArgumentException, ParameterException {
        return new RedirectionInformationImpl(new byte[] { 0, 1 });
    }

}
