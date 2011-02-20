/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ASPIdentifierImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.AffectedPointCodeImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ConcernedDPCImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.CongestedIndicationImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.CorrelationIdImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.DeregistrationResultImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.DeregistrationStatusImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.DestinationPointCodeImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.InfoStringImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.LocalRKIdentifierImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.NetworkAppearanceImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.OPCListImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RegistrationResultImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RegistrationStatusImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RoutingContextImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RoutingKeyImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ServiceIndicatorsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.StatusImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.TrafficModeTypeImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.UserCauseImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class ParameterTest {

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();
    private ByteBuffer out = null;

    public ParameterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        out = ByteBuffer.allocate(8192);
    }

    @After
    public void tearDown() {
    }

    private short getTag(byte[] data) {
        return (short) ((data[0] & 0xff) << 8 | (data[1] & 0xff));
    }

    private short getLen(byte[] data) {
        return (short) ((data[2] & 0xff) << 8 | (data[3] & 0xff));
    }

    private byte[] getValue(byte[] data) {
        // reduce 4 for Tag + length bytes
        short length = (short) (getLen(data) - 4);
        byte[] value = new byte[length];
        System.arraycopy(data, 4, value, 0, length);
        return value;
    }

    /**
     * Test of getOpc method, of class ProtocolDataImpl.
     */
    @Test
    public void testProtocolData1() throws IOException {
        ProtocolDataImpl p1 = factory.createProtocolData(1408, 14150, 1, 1, 0,
                0, new byte[] { 1, 2, 3, 4 });
        p1.write(out);

        byte[] data = out.array();

        ProtocolDataImpl p2 = (ProtocolDataImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());

        boolean isDataCorrect = Arrays.equals(p1.getData(), p2.getData());
        assertTrue("Data mismatch", isDataCorrect);
    }

    @Test
    public void testNetworkAppearance() throws IOException {

        NetworkAppearanceImpl np = (NetworkAppearanceImpl) factory
                .createNetworkAppearance(123);
        np.write(out);

        byte[] data = out.array();

        NetworkAppearanceImpl np2 = (NetworkAppearanceImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(123, (int) np2.getNetApp());
    }

    @Test
    public void testRoutingContext() throws IOException {
        RoutingContextImpl rc = (RoutingContextImpl) factory
                .createRoutingContext(new long[] { 4294967295l });
        rc.write(out);

        byte[] data = out.array();

        RoutingContextImpl rc2 = (RoutingContextImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertTrue(Arrays.equals(new long[] { 4294967295l }, rc2
                .getRoutingContexts()));
    }

    @Test
    public void testRoutingContexts() throws IOException {
        RoutingContextImpl rc = (RoutingContextImpl) factory
                .createRoutingContext(new long[] { 123l, 4294967295l });
        rc.write(out);

        byte[] data = out.array();

        RoutingContextImpl rc2 = (RoutingContextImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertTrue(Arrays.equals(new long[] { 123l, 4294967295l }, rc2
                .getRoutingContexts()));
    }

    @Test
    public void testCorrelationId() throws IOException {
        CorrelationIdImpl crrId = (CorrelationIdImpl) factory
                .createCorrelationId(4294967295l);
        crrId.write(out);

        byte[] data = out.array();

        CorrelationIdImpl crrId2 = (CorrelationIdImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertEquals(4294967295l, crrId2.getCorrelationId());
    }

    @Test
    public void testAffectedPointCode() throws IOException {

        AffectedPointCodeImpl affectedPc = (AffectedPointCodeImpl) factory
                .createAffectedPointCode(new int[] { 123 }, new short[] { 0 });
        affectedPc.write(out);

        byte[] data = out.array();

        AffectedPointCodeImpl affectedPc2 = (AffectedPointCodeImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertTrue(Arrays
                .equals(new int[] { 123 }, affectedPc2.getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0 }, affectedPc2.getMasks()));
    }

    @Test
    public void testAffectedPointCodes() throws IOException {
        AffectedPointCodeImpl affectedPc = (AffectedPointCodeImpl) factory
                .createAffectedPointCode(new int[] { 123, 456 }, new short[] {
                        0, 1 });
        affectedPc.write(out);

        byte[] data = out.array();

        AffectedPointCodeImpl affectedPc2 = (AffectedPointCodeImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123, 456 }, affectedPc2
                .getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0, 1 }, affectedPc2.getMasks()));
    }

    @Test
    public void testInfoString() throws IOException {
        InfoStringImpl infoStr = (InfoStringImpl) factory
                .createInfoString("Hello World");
        infoStr.write(out);

        byte[] data = out.array();

        InfoStringImpl infoStr2 = (InfoStringImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertEquals("Hello World", infoStr2.getString());

    }

    @Test
    public void testConcernedDPC() throws IOException {
        ConcernedDPCImpl concernedDPC = (ConcernedDPCImpl) factory
                .createConcernedDPC(123);
        concernedDPC.write(out);

        byte[] data = out.array();

        ConcernedDPCImpl concernedDPC2 = (ConcernedDPCImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(concernedDPC.getPointCode(), concernedDPC2.getPointCode());

    }

    @Test
    public void testCongestedIndication() throws IOException {
        CongestedIndicationImpl congIndImpl = (CongestedIndicationImpl) factory
                .createCongestedIndication(CongestionLevel.LEVEL2);
        congIndImpl.write(out);

        byte[] data = out.array();

        CongestedIndicationImpl congIndImpl2 = (CongestedIndicationImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(congIndImpl.getCongestionLevel(), congIndImpl2
                .getCongestionLevel());

    }

    @Test
    public void testUserCause() throws IOException {
        UserCauseImpl usrCa = (UserCauseImpl) factory.createUserCause(5, 0);
        usrCa.write(out);

        byte[] data = out.array();

        UserCauseImpl usrCa2 = (UserCauseImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertEquals(usrCa.getUser(), usrCa2.getUser());

        assertEquals(usrCa.getCause(), usrCa2.getCause());

    }

    @Test
    public void testASPIdentifier() throws IOException {
        ASPIdentifierImpl rc = (ASPIdentifierImpl) factory
                .createASPIdentifier(12234445);
        rc.write(out);

        byte[] data = out.array();

        ASPIdentifierImpl rc2 = (ASPIdentifierImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertEquals(12234445l, rc2.getAspId());
    }

    @Test
    public void testDestinationPointCode() throws IOException {

        DestinationPointCodeImpl affectedPc = (DestinationPointCodeImpl) factory
                .createDestinationPointCode(123, (short) 0);
        affectedPc.write(out);

        byte[] data = out.array();

        DestinationPointCodeImpl affectedPc2 = (DestinationPointCodeImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(123, affectedPc2.getPointCode());
        assertEquals((short) 0, affectedPc2.getMask());
    }

    @Test
    public void testLocalRKIdentifier() throws IOException {
        LocalRKIdentifierImpl crrId = (LocalRKIdentifierImpl) factory
                .createLocalRKIdentifier(4294967295l);
        crrId.write(out);

        byte[] data = out.array();

        LocalRKIdentifierImpl crrId2 = (LocalRKIdentifierImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(4294967295l, crrId2.getId());
    }

    @Test
    public void testOPCList() throws IOException {
        OPCListImpl opcList = (OPCListImpl) factory.createOPCList(new int[] {
                123, 456 }, new short[] { 0, 1 });
        opcList.write(out);

        byte[] data = out.array();

        OPCListImpl opcList1 = (OPCListImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertTrue(Arrays.equals(new int[] { 123, 456 }, opcList1
                .getPointCodes()));
        assertTrue(Arrays.equals(new short[] { 0, 1 }, opcList1.getMasks()));
    }

    @Test
    public void testServiceIndicators() throws IOException {
        ServiceIndicatorsImpl opcList = (ServiceIndicatorsImpl) factory
                .createServiceIndicators(new short[] { 1, 2, 3, 4 });
        opcList.write(out);

        byte[] data = out.array();

        ServiceIndicatorsImpl opcList1 = (ServiceIndicatorsImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(new short[] { 1, 2, 3, 4 }, opcList1
                .getIndicators()));
    }

    @Test
    public void testTrafficModeType() throws IOException {
        TrafficModeTypeImpl rc = (TrafficModeTypeImpl) factory
                .createTrafficModeType(1);
        rc.write(out);

        byte[] data = out.array();

        TrafficModeTypeImpl rc2 = (TrafficModeTypeImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(1, rc2.getMode());
    }

    @Test
    public void testRoutingKey() throws IOException {
        LocalRKIdentifier localRkId = factory.createLocalRKIdentifier(12);
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        TrafficModeType trafMdTy = factory.createTrafficModeType(1);
        NetworkAppearance netApp = factory.createNetworkAppearance(1);
        DestinationPointCode[] dpc = new DestinationPointCode[] {
                factory.createDestinationPointCode(123, (short) 0),
                factory.createDestinationPointCode(456, (short) 1) };
        ServiceIndicators[] servInds = new ServiceIndicators[] {
                factory.createServiceIndicators(new short[] { 1, 2 }),
                factory.createServiceIndicators(new short[] { 1, 2 }) };
        OPCList[] opcList = new OPCList[] {
                factory.createOPCList(new int[] { 1, 2, 3 }, new short[] { 0,
                        0, 0 }),
                factory.createOPCList(new int[] { 4, 5, 6 }, new short[] { 0,
                        0, 0 }) };

        RoutingKeyImpl routKey = (RoutingKeyImpl) factory.createRoutingKey(
                localRkId, rc, trafMdTy, netApp, dpc, servInds, opcList);
        routKey.write(out);

        byte[] data = out.array();

        RoutingKeyImpl rc2 = (RoutingKeyImpl) factory.createParameter(
                getTag(data), getValue(data));

        assertEquals(localRkId.getId(), rc2.getLocalRKIdentifier().getId());
        assertTrue(Arrays.equals(routKey.getRoutingContext()
                .getRoutingContexts(), rc2.getRoutingContext()
                .getRoutingContexts()));
    }

    @Test
    public void testRegistrationStatus() throws IOException {
        RegistrationStatusImpl crrId = (RegistrationStatusImpl) factory
                .createRegistrationStatus(11);
        crrId.write(out);

        byte[] data = out.array();

        RegistrationStatusImpl crrId2 = (RegistrationStatusImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(11, crrId2.getStatus());
    }

    @Test
    public void testRegistrationResult() throws IOException {
        LocalRKIdentifier localRkId = factory.createLocalRKIdentifier(12);
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        RegistrationStatus status = factory.createRegistrationStatus(0);

        RegistrationResultImpl routKey = (RegistrationResultImpl) factory
                .createRegistrationResult(localRkId, status, rc);
        routKey.write(out);

        byte[] data = out.array();

        RegistrationResultImpl rc2 = (RegistrationResultImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(localRkId.getId(), rc2.getLocalRKIdentifier().getId());
        assertTrue(Arrays.equals(routKey.getRoutingContext()
                .getRoutingContexts(), rc2.getRoutingContext()
                .getRoutingContexts()));
        assertEquals(status.getStatus(), rc2.getRegistrationStatus()
                .getStatus());
    }

    @Test
    public void testDeregistrationStatus() throws IOException {
        DeregistrationStatusImpl crrId = (DeregistrationStatusImpl) factory
                .createDeregistrationStatus(5);
        crrId.write(out);

        byte[] data = out.array();

        DeregistrationStatusImpl crrId2 = (DeregistrationStatusImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertEquals(5, crrId2.getStatus());
    }

    @Test
    public void testDeregistrationResult() throws IOException {
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        DeregistrationStatus status = factory.createDeregistrationStatus(0);

        DeregistrationResultImpl routKey = (DeregistrationResultImpl) factory
                .createDeregistrationResult(rc, status);
        routKey.write(out);

        byte[] data = out.array();

        DeregistrationResultImpl rc2 = (DeregistrationResultImpl) factory
                .createParameter(getTag(data), getValue(data));

        assertTrue(Arrays.equals(routKey.getRoutingContext()
                .getRoutingContexts(), rc2.getRoutingContext()
                .getRoutingContexts()));
        assertEquals(status.getStatus(), rc2.getDeregistrationStatus()
                .getStatus());
    }

    @Test
    public void testStatus() throws IOException {

        Status routKey = (Status) factory.createStatus(1, 4);
        ((StatusImpl) routKey).write(out);

        byte[] data = out.array();

        StatusImpl rc2 = (StatusImpl) factory.createParameter(getTag(data),
                getValue(data));

        assertEquals(routKey.getType(), rc2.getType());
        assertEquals(routKey.getInfo(), rc2.getInfo());
    }
}