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

package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.*;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.impl.SccpRoutingControl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.EncodingResultData;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class NetworkIdTest implements SccpListener {

    private SccpAddress primaryAddr1_L, primaryAddr1_R;
    private SccpAddress primaryAddr2_L, primaryAddr2_R;

    private int dpc1_L, dpc1_R;
    private int dpc2_L, dpc2_R;

    private RouterImpl router = null;

    private SccpStackImpl testSccpStackImpl = null;
    private ParameterFactory factory = null;
    private MessageFactoryImpl messageFactory = null;

    private int localTerm_1;
    private int localTerm_2;
    private int remTerm_1;
    private int remTerm_2;

    public NetworkIdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        testSccpStackImpl = new SccpStackImpl("Test");
        testSccpStackImpl.start();
        factory = new ParameterFactoryImpl();
        messageFactory = new MessageFactoryImpl(testSccpStackImpl);

        dpc1_L = 11;
        dpc1_R = 111;
        dpc2_L = 22;
        dpc2_R = 222;
        GlobalTitle gt_1L = factory.createGlobalTitle("1111", 0, NumberingPlan.ISDN_TELEPHONY, BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt_1R = factory.createGlobalTitle("1119", 0, NumberingPlan.ISDN_TELEPHONY, BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt_2L = factory.createGlobalTitle("2229", 0, NumberingPlan.ISDN_TELEPHONY, BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt_2R = factory.createGlobalTitle("2229", 0, NumberingPlan.ISDN_TELEPHONY, BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        primaryAddr1_L = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt_1L, 11, 0);
        primaryAddr1_R = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt_1R, 111, 0);
        primaryAddr2_L = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt_2L, 22, 0);
        primaryAddr2_R = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt_2R, 222, 0);

        Mtp3UserPartProxy mtp3UserPart = new Mtp3UserPartProxy();
        testSccpStackImpl.setMtp3UserPart(1, mtp3UserPart);

        // cleans config file
        this.router = (RouterImpl) this.testSccpStackImpl.getRouter();
        this.testSccpStackImpl.removeAllResourses();
    }

    @AfterMethod
    public void tearDown() {
        router.removeAllResourses();
        router.stop();
    }

    /**
     * Test of add method, of class RouterImpl.
     */
    @Test(groups = { "router", "functional" })
    public void testNetworkId() throws Exception {

        this.testSccpStackImpl.getSccpProvider().registerSccpListener(8, this);
        
        router.addMtp3ServiceAccessPoint(1, 1, dpc1_L, 2, 1);
        router.addMtp3ServiceAccessPoint(2, 1, dpc2_L, 2, 2);
        // int id, int mtp3Id, int opc, int ni, int networkId
        router.addMtp3Destination(1, 1, dpc1_R, dpc1_R, 0, 255, 255);
        router.addMtp3Destination(2, 1, dpc2_R, dpc2_R, 0, 255, 255);
        // sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask

        router.addRoutingAddress(1, primaryAddr1_R);
        router.addRoutingAddress(2, primaryAddr2_R);
        router.addRoutingAddress(3, primaryAddr1_L);
        router.addRoutingAddress(4, primaryAddr2_L);

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL, pattern, "K", 1, 1, null, 1);
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL, pattern, "K", 2, 2, null, 2);
        router.addRule(3, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.REMOTE, pattern, "K", 3, 3, null, 1);
        router.addRule(4, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.REMOTE, pattern, "K", 4, 4, null, 2);
        // int id, RuleType ruleType, LoadSharingAlgorithm algo, OriginationType
        // originationType, SccpAddress pattern, String mask, int pAddressId,
        // int sAddressId, Integer newCallingPartyAddressAddressId, int networkId

        this.testSccpStackImpl.getSccpResource().addRemoteSpc(1, dpc1_R, 0, 0);
        this.testSccpStackImpl.getSccpResource().addRemoteSpc(2, dpc2_R, 0, 0);
        // remoteSpcId, remoteSpc, remoteSpcFlag, mask

        // ***** remote orig - network=1
        Mtp3TransferPrimitiveFactory mtp3TransferPrimitiveFactory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        byte[] data = new byte[] { 1, 2, 3 };
        GlobalTitle gt1 = factory.createGlobalTitle("3333", 1);
        GlobalTitle gt2 = factory.createGlobalTitle("0000", 1);
        SccpAddress calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 0, 8);
        SccpAddress callingParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 0, 8);
        HopCounter hc = new HopCounterImpl(3);
        Importance imp = new ImportanceImpl((byte) 0);
        SccpDataMessageImpl msg1 = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledParty, callingParty, data, 0, 0, false, hc, imp);
        // calledParty, callingParty, data, sls, localSsn, returnMessageOnError,
        // hopCounter, importance
        Logger logger = Logger.getLogger(SccpRoutingControl.class);
        EncodingResultData erd = msg1.encode(this.testSccpStackImpl, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 1000, logger, true, SccpProtocolVersion.ITU);
        // longMessageRuleType, maxMtp3UserDataLength, logger, removeSPC,
        // sccpProtocolVersion

        Mtp3TransferPrimitive mtp3Msg = mtp3TransferPrimitiveFactory.createMtp3TransferPrimitive(3, 2, 0, dpc1_R, dpc1_L, 0, erd.getSolidData());
        // int si, int ni, int mp, int opc, int dpc, int sls, byte[] data,
        // RoutingLabelFormat pointCodeFormat
        this.testSccpStackImpl.onMtp3TransferMessage(mtp3Msg);

        assertEquals(this.localTerm_1, 1);
        assertEquals(this.localTerm_2, 0);
        assertEquals(this.remTerm_1, 0);
        assertEquals(this.remTerm_2, 0);


        // ***** remote orig - network=2
        mtp3Msg = mtp3TransferPrimitiveFactory.createMtp3TransferPrimitive(3, 2, 0, dpc2_R, dpc2_L, 0, erd.getSolidData());
        // int si, int ni, int mp, int opc, int dpc, int sls, byte[] data,
        // RoutingLabelFormat pointCodeFormat
        this.testSccpStackImpl.onMtp3TransferMessage(mtp3Msg);

        assertEquals(this.localTerm_1, 1);
        assertEquals(this.localTerm_2, 1);
        assertEquals(this.remTerm_1, 0);
        assertEquals(this.remTerm_2, 0);


        // ***** local orig - network=1
        gt1 = factory.createGlobalTitle("0000", 1);
        gt2 = factory.createGlobalTitle("3333", 1);
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 0, 8);
        callingParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 0, 8);
        SccpDataMessageImpl msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledParty, callingParty, data, 0, 8, false, hc, imp);
        msg.setNetworkId(1);
        this.testSccpStackImpl.getSccpProvider().send(msg);

        assertEquals(this.localTerm_1, 1);
        assertEquals(this.localTerm_2, 1);
        assertEquals(this.remTerm_1, 1);
        assertEquals(this.remTerm_2, 0);


        // ***** local orig - network=2
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledParty, callingParty, data, 0, 8, false, hc, imp);
        msg.setNetworkId(2);
        this.testSccpStackImpl.getSccpProvider().send(msg);

        assertEquals(this.localTerm_1, 1);
        assertEquals(this.localTerm_2, 1);
        assertEquals(this.remTerm_1, 1);
        assertEquals(this.remTerm_2, 1);
    }

    private class Mtp3UserPartProxy implements Mtp3UserPart {

        @Override
        public void addMtp3UserPartListener(Mtp3UserPartListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeMtp3UserPartListener(Mtp3UserPartListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public RoutingLabelFormat getRoutingLabelFormat() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) {
            // TODO Auto-generated method stub

        }

        @Override
        public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
            return new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        }

        @Override
        public int getMaxUserDataLength(int dpc) {
            return 1000;
        }

        @Override
        public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {
            int dpc = msg.getDpc();

            if (dpc == dpc1_R)
                remTerm_1++;
            if (dpc == dpc2_R)
                remTerm_2++;
        }

        @Override
        public void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isUseLsbForLinksetSelection() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getDeliveryMessageThreadCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public ExecutorCongestionMonitor getExecutorCongestionMonitor() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public void onMessage(SccpDataMessage message) {
        int dpc = message.getCalledPartyAddress().getSignalingPointCode();

        if (dpc == dpc1_L)
            localTerm_1++;
        if (dpc == dpc2_L)
            localTerm_2++;
    }

    @Override
    public void onNotice(SccpNoticeMessage message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCoordResponse(int ssn, int multiplicityIndicator) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
            RemoteSccpStatus remoteSccpStatus) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {
        // TODO Auto-generated method stub
        
    }
}
