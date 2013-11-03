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
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserServiceInformationTest {
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
        return new byte[] { (byte) 184, (byte) 209 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 184, (byte) 216, (byte) 147 };
    }

    private byte[] getData3() {
        return new byte[] { -128, -112, -95, -62, -30 };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        UserServiceInformationImpl prim = new UserServiceInformationImpl();
        prim.decode(getData());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_INTERNATIONAL);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_VIDEO);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_PACKET);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_64x2);

        assertEquals(prim.getCustomInformationTransferRate(), 0);
        assertEquals(prim.getL1UserInformation(), 0);
        assertEquals(prim.getL2UserInformation(), 0);
        assertEquals(prim.getL3UserInformation(), 0);
        assertEquals(prim.getSyncMode(), 0);
        assertEquals(prim.getNegotiation(), 0);
        assertEquals(prim.getUserRate(), 0);
        assertEquals(prim.getIntermediateRate(), 0);
        assertEquals(prim.getNicOnTx(), 0);
        assertEquals(prim.getNicOnRx(), 0);
        assertEquals(prim.getFlowControlOnTx(), 0);
        assertEquals(prim.getFlowControlOnRx(), 0);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), 0);
        assertEquals(prim.getDataBits(), 0);
        assertEquals(prim.getParity(), 0);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);

        prim = new UserServiceInformationImpl();
        prim.decode(getData2());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_INTERNATIONAL);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_VIDEO);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_PACKET);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_MULTIRATE);

        assertEquals(prim.getCustomInformationTransferRate(), 19);
        assertEquals(prim.getL1UserInformation(), 0);
        assertEquals(prim.getL2UserInformation(), 0);
        assertEquals(prim.getL3UserInformation(), 0);
        assertEquals(prim.getSyncMode(), 0);
        assertEquals(prim.getNegotiation(), 0);
        assertEquals(prim.getUserRate(), 0);
        assertEquals(prim.getIntermediateRate(), 0);
        assertEquals(prim.getNicOnTx(), 0);
        assertEquals(prim.getNicOnRx(), 0);
        assertEquals(prim.getFlowControlOnTx(), 0);
        assertEquals(prim.getFlowControlOnRx(), 0);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), 0);
        assertEquals(prim.getDataBits(), 0);
        assertEquals(prim.getParity(), 0);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);

        prim = new UserServiceInformationImpl();
        prim.decode(getData3());

        assertEquals(prim.getCodingStandart(), UserServiceInformation._CS_CCITT);
        assertEquals(prim.getInformationTransferCapability(), UserServiceInformation._ITS_SPEECH);
        assertEquals(prim.getTransferMode(), UserServiceInformation._TM_CIRCUIT);
        assertEquals(prim.getInformationTransferRate(), UserServiceInformation._ITR_64);

        assertEquals(prim.getCustomInformationTransferRate(), 0);
        assertEquals(prim.getL1UserInformation(), UserServiceInformation._L1_ITUT_110);
        assertEquals(prim.getL2UserInformation(), UserServiceInformation._L2_Q921);
        assertEquals(prim.getL3UserInformation(), UserServiceInformation._L3_Q931);
        assertEquals(prim.getSyncMode(), 0);
        assertEquals(prim.getNegotiation(), 0);
        assertEquals(prim.getUserRate(), 0);
        assertEquals(prim.getIntermediateRate(), 0);
        assertEquals(prim.getNicOnTx(), 0);
        assertEquals(prim.getNicOnRx(), 0);
        assertEquals(prim.getFlowControlOnTx(), 0);
        assertEquals(prim.getFlowControlOnRx(), 0);
        assertEquals(prim.getHDR(), 0);
        assertEquals(prim.getMultiframe(), 0);
        assertEquals(prim.getMode(), 0);
        assertEquals(prim.getLLINegotiation(), 0);
        assertEquals(prim.getAssignor(), 0);
        assertEquals(prim.getInBandNegotiation(), 0);
        assertEquals(prim.getStopBits(), 0);
        assertEquals(prim.getDataBits(), 0);
        assertEquals(prim.getParity(), 0);
        assertEquals(prim.getDuplexMode(), 0);
        assertEquals(prim.getModemType(), 0);
        assertEquals(prim.getL3Protocol(), 0);

        // TODO: now tested only for a case l1UserInformation = 0 && l2UserInformation == 0 && l2UserInformation == 0
        // we need to test other case encoding/decoding
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        UserServiceInformationImpl prim = new UserServiceInformationImpl();
        prim.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        prim.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        prim.setTransferMode(UserServiceInformation._TM_PACKET);
        prim.setInformationTransferRate(UserServiceInformation._ITR_64x2);

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new UserServiceInformationImpl();
        prim.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        prim.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        prim.setTransferMode(UserServiceInformation._TM_PACKET);
        prim.setInformationTransferRate(UserServiceInformation._ITR_MULTIRATE);
        prim.setCustomInformationTransferRate(19);

        data = getData2();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new UserServiceInformationImpl();
        prim.setCodingStandart(UserServiceInformation._CS_CCITT);
        prim.setInformationTransferCapability(UserServiceInformation._ITS_SPEECH);
        prim.setTransferMode(UserServiceInformation._TM_CIRCUIT);
        prim.setInformationTransferRate(UserServiceInformation._ITR_64);
        prim.setL1UserInformation(UserServiceInformation._L1_ITUT_110);
        prim.setL2UserInformation(UserServiceInformation._L2_Q921);
        prim.setL3UserInformation(UserServiceInformation._L3_Q931);

        data = getData3();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        // TODO: now tested only for a case l1UserInformation = 0 && l2UserInformation == 0 && l2UserInformation == 0
        // we need to test other case encoding/decoding
    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        UserServiceInformationImpl original = new UserServiceInformationImpl();
        original.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        original.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        original.setTransferMode(UserServiceInformation._TM_PACKET);
        original.setInformationTransferRate(UserServiceInformation._ITR_64x2);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "userServiceInformation", UserServiceInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        UserServiceInformationImpl copy = reader.read("userServiceInformation", UserServiceInformationImpl.class);

        assertEquals(copy.getCodingStandart(), original.getCodingStandart());
        assertEquals(copy.getInformationTransferCapability(), original.getInformationTransferCapability());
        assertEquals(copy.getTransferMode(), original.getTransferMode());
        assertEquals(copy.getInformationTransferRate(), original.getInformationTransferRate());
        assertEquals(copy.getCustomInformationTransferRate(), original.getCustomInformationTransferRate());
        assertEquals(copy.getL1UserInformation(), original.getL1UserInformation());
        assertEquals(copy.getL2UserInformation(), original.getL2UserInformation());
        assertEquals(copy.getL3UserInformation(), original.getL3UserInformation());
        assertEquals(copy.getSyncMode(), original.getSyncMode());
        assertEquals(copy.getNegotiation(), original.getNegotiation());
        assertEquals(copy.getUserRate(), original.getUserRate());
        assertEquals(copy.getIntermediateRate(), original.getIntermediateRate());
        assertEquals(copy.getNicOnTx(), original.getNicOnTx());
        assertEquals(copy.getNicOnRx(), original.getNicOnRx());
        assertEquals(copy.getFlowControlOnTx(), original.getFlowControlOnTx());
        assertEquals(copy.getFlowControlOnRx(), original.getFlowControlOnRx());
        assertEquals(copy.getHDR(), original.getHDR());
        assertEquals(copy.getMultiframe(), original.getMultiframe());
        assertEquals(copy.getMode(), original.getMode());
        assertEquals(copy.getLLINegotiation(), original.getLLINegotiation());
        assertEquals(copy.getAssignor(), original.getAssignor());
        assertEquals(copy.getInBandNegotiation(), original.getInBandNegotiation());
        assertEquals(copy.getStopBits(), original.getStopBits());
        assertEquals(copy.getDataBits(), original.getDataBits());
        assertEquals(copy.getParity(), original.getParity());
        assertEquals(copy.getDuplexMode(), original.getDuplexMode());
        assertEquals(copy.getModemType(), original.getModemType());
        assertEquals(copy.getL3Protocol(), original.getL3Protocol());

        original = new UserServiceInformationImpl();
        original.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        original.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        original.setTransferMode(UserServiceInformation._TM_PACKET);
        original.setInformationTransferRate(UserServiceInformation._ITR_MULTIRATE);
        original.setCustomInformationTransferRate(60);

        original.setL1UserInformation(UserServiceInformation._L1_ITUT_X31);
        original.setL2UserInformation(UserServiceInformation._L2_LAN_LLC);
        original.setL3UserInformation(UserServiceInformation._L3_ISO_9577);
        original.setSyncMode(31);
        original.setNegotiation(32);
        original.setUserRate(UserServiceInformation._UR_0_075_ON_1_2);
        original.setIntermediateRate(UserServiceInformation._IR_32_0);
        original.setNicOnTx(33);
        original.setNicOnRx(3);
        original.setFlowControlOnTx(35);
        original.setFlowControlOnRx(36);
        original.setHDR(37);
        original.setMultiframe(38);
        original.setMode(39);
        original.setLLINegotiation(40);
        original.setAssignor(41);
        original.setInBandNegotiation(42);
        original.setStopBits(43);
        original.setDataBits(44);
        original.setParity(UserServiceInformation._PAR_FORCED_1);
        original.setDuplexMode(45);
        original.setModemType(UserServiceInformation._MODEM_V27);
        original.setL3Protocol(UserServiceInformation._L3_PROT_P2P);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "userServiceInformation", UserServiceInformationImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("userServiceInformation", UserServiceInformationImpl.class);

        assertEquals(copy.getCodingStandart(), original.getCodingStandart());
        assertEquals(copy.getInformationTransferCapability(), original.getInformationTransferCapability());
        assertEquals(copy.getTransferMode(), original.getTransferMode());
        assertEquals(copy.getInformationTransferRate(), original.getInformationTransferRate());
        assertEquals(copy.getCustomInformationTransferRate(), original.getCustomInformationTransferRate());
        assertEquals(copy.getL1UserInformation(), original.getL1UserInformation());
        assertEquals(copy.getL2UserInformation(), original.getL2UserInformation());
        assertEquals(copy.getL3UserInformation(), original.getL3UserInformation());
        assertEquals(copy.getSyncMode(), original.getSyncMode());
        assertEquals(copy.getNegotiation(), original.getNegotiation());
        assertEquals(copy.getUserRate(), original.getUserRate());
        assertEquals(copy.getIntermediateRate(), original.getIntermediateRate());
        assertEquals(copy.getNicOnTx(), original.getNicOnTx());
        assertEquals(copy.getNicOnRx(), original.getNicOnRx());
        assertEquals(copy.getFlowControlOnTx(), original.getFlowControlOnTx());
        assertEquals(copy.getFlowControlOnRx(), original.getFlowControlOnRx());
        assertEquals(copy.getHDR(), original.getHDR());
        assertEquals(copy.getMultiframe(), original.getMultiframe());
        assertEquals(copy.getMode(), original.getMode());
        assertEquals(copy.getLLINegotiation(), original.getLLINegotiation());
        assertEquals(copy.getAssignor(), original.getAssignor());
        assertEquals(copy.getInBandNegotiation(), original.getInBandNegotiation());
        assertEquals(copy.getStopBits(), original.getStopBits());
        assertEquals(copy.getDataBits(), original.getDataBits());
        assertEquals(copy.getParity(), original.getParity());
        assertEquals(copy.getDuplexMode(), original.getDuplexMode());
        assertEquals(copy.getModemType(), original.getModemType());
        assertEquals(copy.getL3Protocol(), original.getL3Protocol());

    }

}
