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

package org.mobicents.protocols.ss7.sccp.impl.congestion;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCodeImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0100Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Sergey Vetyutnev
 *
 */
public class CongestionLevelTest {

    private int TIMER_A = 100;
    private int TIMER_D = 200;
    private int N = 8;
    private int M = 4;
    private int EXTRA_DELAY = 10;

    private SccpStackImpl sccpStack;
    private RouterImpl router;
    private SccpListenerProxy listenerProxy;

    @BeforeMethod
    public void setUpClass() throws Exception {
        // BasicConfigurator.configure();
        Properties properties = new Properties();
//        properties.setProperty("log4j.threshold", "DEBUG");
        properties.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
        properties.setProperty("log4j.appender.A1.Target", "System.out");
        properties.setProperty("log4j.appender.A1.Threshold", "DEBUG");
        properties.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.A1.layout.ConversionPattern", "=%-4r %-5p %c{2} %M.%L %x - %m\n");
        properties.setProperty("log4j.rootLogger", "DEBUG, A1");
        properties.setProperty("log4j.logger.org.mobicents", "DEBUG");
        PropertyConfigurator.configure(properties);

        sccpStack = new SccpStackImpl("TestSccp");
        sccpStack.start();
        sccpStack.removeAllResourses();
        router = (RouterImpl) sccpStack.getRouter();

        GlobalTitle gt = new GlobalTitle0100Impl("*", 0, new BCDEvenEncodingScheme(), NumberingPlan.ISDN_TELEPHONY,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 8);
        GlobalTitle gt1 = new GlobalTitle0100Impl("-", 0, new BCDEvenEncodingScheme(), NumberingPlan.ISDN_TELEPHONY,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress sccpAddress1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 101, 8);
        router.addRoutingAddress(1, sccpAddress1);
        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.ALL, pattern, "K", 1, -1, null, 1);

        listenerProxy = new SccpListenerProxy();
        sccpStack.getSccpProvider().registerSccpListener(8, listenerProxy);

        sccpStack.setCongControlTIMER_A(TIMER_A);
        sccpStack.setCongControlTIMER_D(TIMER_D);
        sccpStack.setCongControlN(N);
        sccpStack.setCongControlM(M);
    }

    @Test
    public void testCongestionLevel1() throws Exception {
        int affectedDpc = 101;
        Mtp3StatusPrimitive msg = new Mtp3StatusPrimitive(affectedDpc, Mtp3StatusCause.SignallingNetworkCongested, 1, 3);

        sccpStack.getSccpResource().addRemoteSpc(1, 101, 0, 0);
        RemoteSignalingPointCodeImpl rspc1 = (RemoteSignalingPointCodeImpl) sccpStack.getSccpResource().getRemoteSpc(1);

        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 1);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 2);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 3);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 1);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);

        Thread.sleep(TIMER_D + EXTRA_DELAY);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 3);

        Thread.sleep(TIMER_D);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 2);

        Thread.sleep(TIMER_D);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 1);

        Thread.sleep(TIMER_D);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);

        Thread.sleep(TIMER_D);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);
    }

    @Test
    public void testCongestionLevel2() throws Exception {
        int affectedDpc = 101;
        Mtp3StatusPrimitive msg = new Mtp3StatusPrimitive(affectedDpc, Mtp3StatusCause.SignallingNetworkCongested, 1, 3);

        sccpStack.getSccpResource().addRemoteSpc(1, 101, 0, 0);
        RemoteSignalingPointCodeImpl rspc1 = (RemoteSignalingPointCodeImpl) sccpStack.getSccpResource().getRemoteSpc(1);

        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);
        assertEquals(listenerProxy.getNI_NISList().size(), 0);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 1);
        assertEquals(listenerProxy.getNI_NISList().size(), 0);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 2);
        assertEquals(listenerProxy.getNI_NISList().size(), 0);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 3);
        assertEquals(listenerProxy.getNI_NISList().size(), 0);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 1);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);
        assertEquals(listenerProxy.getNI_NISList().size(), 1);
        assertTrue(listenerProxy.getNI_NISList().get(0).networkIdState.isAvailavle());
        assertEquals(listenerProxy.getNI_NISList().get(0).networkIdState.getCongLevel(), 0);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 1);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 1);
        assertEquals(listenerProxy.getNI_NISList().size(), 1);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 1);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 2);
        assertEquals(listenerProxy.getNI_NISList().size(), 1);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 1);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 3);
        assertEquals(listenerProxy.getNI_NISList().size(), 1);

        Thread.sleep(TIMER_A + EXTRA_DELAY);

        sccpStack.onMtp3StatusMessage(msg);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 2);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);
        assertEquals(listenerProxy.getNI_NISList().size(), 2);
        assertTrue(listenerProxy.getNI_NISList().get(1).networkIdState.isAvailavle());
        assertEquals(listenerProxy.getNI_NISList().get(1).networkIdState.getCongLevel(), 1);

        // decreasing of level
        Thread.sleep(TIMER_D * 4 + EXTRA_DELAY);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 1);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);
        assertEquals(listenerProxy.getNI_NISList().size(), 3);
        assertTrue(listenerProxy.getNI_NISList().get(2).networkIdState.isAvailavle());
        assertEquals(listenerProxy.getNI_NISList().get(2).networkIdState.getCongLevel(), 0);

        Thread.sleep(TIMER_D * 4);
        assertEquals(rspc1.getCurrentRestrictionLevel(), 0);
        assertEquals(rspc1.getCurrentRestrictionSubLevel(), 0);
        assertEquals(listenerProxy.getNI_NISList().size(), 4);
        assertTrue(listenerProxy.getNI_NISList().get(3).networkIdState.isAvailavle());
        assertEquals(listenerProxy.getNI_NISList().get(3).networkIdState.getCongLevel(), 0);
    }

    private class SccpListenerProxy implements SccpListener {

        private ArrayList<NI_NIS> lst = new ArrayList<NI_NIS>();

        @Override
        public void onMessage(SccpDataMessage message) {
            // TODO Auto-generated method stub
            
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
            lst.add(new NI_NIS(networkId, networkIdState));
        }

        public ArrayList<NI_NIS> getNI_NISList() {
            return this.lst;
        }
    }

    private class NI_NIS {
        public NI_NIS(int networkId, NetworkIdState networkIdState) {
            this.networkId = networkId;
            this.networkIdState = networkIdState;
        }

        public int networkId;
        public NetworkIdState networkIdState;
    }
}
