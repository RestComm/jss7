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

/**
 * Start time:09:16:42 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:09:16:42 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public abstract class ParameterHarness {

    // 21 10000011 Address....................... 83
    // 22 01100000 Address....................... 60
    // 23 00111000 Address....................... 38
    // NOTE: now see how nice digits swap can come out with conversion, lol
    private static final byte[] sixDigits = new byte[] { (byte) 0x83, 0x60, 0x38 };
    private static final byte[] fiveDigits = new byte[] { (byte) 0x83, 0x60, 0x08 };
    private static final byte[] sevenDigits = new byte[] { (byte) 0x83, 0x60, 0x33, 0x08 };
    private static final byte[] eightDigits = new byte[] { (byte) 0x83, 0x60, 0x33, 0x48 };
    private static final byte[] threeDigits = new byte[] { (byte) 0x83, 0x0 };;
    private static final String sixDigitsString = "380683";
    private static final String fiveDigitsString = "38068";
    private static final String sevenDigitsString = "3806338";
    private static final String eightDigitsString = "38063384";
    private static final String threeDigitsString = "380";

    // FIXME: add code to check values :)

    protected List<byte[]> goodBodies = new ArrayList<byte[]>();

    protected List<byte[]> badBodies = new ArrayList<byte[]>();

    protected String makeCompare(byte[] hardcodedBody, byte[] elementEncoded) {
        int totalLength = 0;
        if (hardcodedBody == null || elementEncoded == null) {
            return "One arg is null";
        }
        if (hardcodedBody.length >= elementEncoded.length) {
            totalLength = hardcodedBody.length;
        } else {
            totalLength = elementEncoded.length;
        }

        String out = "";

        for (int index = 0; index < totalLength; index++) {
            if (hardcodedBody.length > index) {
                out += "hardcodedBody[" + Integer.toHexString(hardcodedBody[index]) + "]";
            } else {
                out += "hardcodedBody[NOP]";
            }

            if (elementEncoded.length > index) {
                out += "elementEncoded[" + Integer.toHexString(elementEncoded[index]) + "]";
            } else {
                out += "elementEncoded[NOP]";
            }
            out += "\n";
        }

        return out;
    }

    public String makeCompare(int[] hardcodedBody, int[] elementEncoded) {

        int totalLength = 0;
        if (hardcodedBody == null || elementEncoded == null) {
            return "One arg is null";
        }
        if (hardcodedBody.length >= elementEncoded.length) {
            totalLength = hardcodedBody.length;
        } else {
            totalLength = elementEncoded.length;
        }

        String out = "";

        for (int index = 0; index < totalLength; index++) {
            if (hardcodedBody.length > index) {
                out += "hardcodedBody[" + Integer.toHexString(hardcodedBody[index]) + "]";
            } else {
                out += "hardcodedBody[NOP]";
            }

            if (elementEncoded.length > index) {
                out += "elementEncoded[" + Integer.toHexString(elementEncoded[index]) + "]";
            } else {
                out += "elementEncoded[NOP]";
            }
            out += "\n";
        }

        return out;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testDecodeEncode() throws IOException, ParameterException {

        for (int index = 0; index < this.goodBodies.size(); index++) {
            byte[] goodBody = this.goodBodies.get(index);
            AbstractISUPParameter component = this.getTestedComponent();
            doTestDecode(goodBody, true, component, index);
            byte[] encodedBody = component.encode();
            boolean equal = Arrays.equals(goodBody, encodedBody);
            assertTrue(equal, "Body index: " + index + "\n" + makeCompare(goodBody, encodedBody));

        }
        for (int index = 0; index < this.badBodies.size(); index++) {

            byte[] badBody = this.badBodies.get(index);
            AbstractISUPParameter component = this.getTestedComponent();
            doTestDecode(badBody, false, component, index);
            byte[] encodedBody = component.encode();
            // TODO: make some tests here?
        }

    }

    public abstract AbstractISUPParameter getTestedComponent() throws ParameterException;

    protected void doTestDecode(byte[] presumableBody, boolean shouldPass, AbstractISUPParameter component, int index)
            throws ParameterException {
        try {
            component.decode(presumableBody);
            if (!shouldPass) {
                fail("Decoded[" + index + "] parameter[" + component.getClass() + "], should not pass. Passed data: "
                        + dumpData(presumableBody));
            }

        } catch (IllegalArgumentException iae) {
            if (shouldPass) {
                fail("Failed to decode[" + index + "] parameter[" + component.getClass() + "], should not happen. " + iae
                        + ".Passed data: " + dumpData(presumableBody));
                iae.printStackTrace();
            }
        } catch (ParameterException iae) {
            if (shouldPass) {
                fail("Failed to decode[" + index + "] parameter[" + component.getClass() + "], should not happen. " + iae
                        + ".Passed data: " + dumpData(presumableBody));
                throw iae;
            }
        } catch (Exception e) {
            fail("Failed to decode[" + index + "] parameter[" + component.getClass() + "]." + e + ". Passed data: "
                    + dumpData(presumableBody));
            e.printStackTrace();
        }
    }

    protected String dumpData(byte[] b) {
        String s = "\n";
        for (byte bb : b) {
            s += Integer.toHexString(bb);
        }

        return s;
    }

    public void testValues(AbstractISUPParameter component, String[] getterMethodNames, Object[] expectedValues) {
        try {
            Class cClass = component.getClass();
            for (int index = 0; index < getterMethodNames.length; index++) {
                Method m = cClass.getMethod(getterMethodNames[index], null);
                // Should not be null by now
                Object v = m.invoke(component, null);
                if (v == null && expectedValues[index] != null) {
                    fail("Failed to validate values in component: " + component.getClass().getName() + ". Value of: "
                            + getterMethodNames[index] + " is null, but test values is not.");
                }
                if (expectedValues[index] != null && expectedValues[index].getClass().isArray()) {
                    assertTrue(Arrays.deepEquals(new Object[] { expectedValues[index] }, new Object[] { v }),
                            "Failed to validate values in component: " + component.getClass().getName() + ". Value of: "
                                    + getterMethodNames[index]);
                } else {
                    assertEquals(v, expectedValues[index], "Failed to validate values in component: "
                            + component.getClass().getName() + ". Value of: " + getterMethodNames[index]);
                }

            }

        } catch (Exception e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(baos);
            e.printStackTrace(pw);
            fail("Failed to check values on component: " + component.getClass().getName() + ", due to: " + new String(baos.toByteArray()));
            e.printStackTrace();
        }
    }

    public static byte[] getSixDigits() {
        return sixDigits;
    }

    public static byte[] getFiveDigits() {
        return fiveDigits;
    }

    public static byte[] getThreeDigits() {
        return threeDigits;
    }

    public static byte[] getSevenDigits() {
        return sevenDigits;
    }

    public static byte[] getEightDigits() {
        return eightDigits;
    }

    public static String getSixDigitsString() {
        return sixDigitsString;
    }

    public static String getFiveDigitsString() {
        return fiveDigitsString;
    }

    public static String getThreeDigitsString() {
        return threeDigitsString;
    }

    public static String getSevenDigitsString() {
        return sevenDigitsString;
    }

    public static String getEightDigitsString() {
        return eightDigitsString;
    }

}
