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

import javolution.util.FastMap;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCodeImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpRspProxy;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0100Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Sergey Vetyutnev
 *
 */
public class NetworkIdAffectedPCTest {

    private GlobalTitle gt = new GlobalTitle0100Impl("*", 0, new BCDEvenEncodingScheme(), NumberingPlan.ISDN_TELEPHONY,
            NatureOfAddress.INTERNATIONAL);
    private SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 8);

    @Test
    public void testNetworkIdAffectedPC() throws Exception {

        SccpStackImpl sccpStack = new SccpStackImpl("TestSccp");
        sccpStack.start();
        sccpStack.removeAllResourses();
        RouterImpl router = (RouterImpl) sccpStack.getRouter();

        // no rules
        FastMap<Integer, NetworkIdState> map = router.getNetworkIdList(101);
        assertEquals(map.size(), 0);

        // simple case
        GlobalTitle gt1 = new GlobalTitle0100Impl("-", 0, new BCDEvenEncodingScheme(), NumberingPlan.ISDN_TELEPHONY,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress sccpAddress1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 101, 8);
        SccpAddress sccpAddress2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 102, 8);
        SccpAddress sccpAddress3 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 103, 8);
        SccpAddress sccpAddress4 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 104, 8);
        router.addRoutingAddress(1, sccpAddress1);
        router.addRoutingAddress(2, sccpAddress2);
        router.addRoutingAddress(3, sccpAddress3);
        router.addRoutingAddress(4, sccpAddress4);

        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.ALL, pattern, "K", 1, -1, null, 1, pattern);
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 2, -1, null, 2, pattern);
        router.addRule(3, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 3, -1, null, 3, pattern);
        router.addRule(4, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.ALL, pattern, "K", 4, -1, null, 11, pattern);

        map = router.getNetworkIdList(101);
        assertEquals(map.size(), 1);
        NetworkIdState state = map.get(1);
        assertNotNull(state);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        map = router.getNetworkIdList(102);
        assertEquals(map.size(), 1);
        state = map.get(2);
        assertNotNull(state);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        // no routing address
        router.removeRoutingAddress(1);
        router.addRoutingAddress(1, sccpAddress1);

        // isPcLocal()==true
        Mtp3UserPart mtp3UserPart = new M3UAManagementImpl("Test", "Test2");
        sccpStack.setMtp3UserPart(1, mtp3UserPart);
        router.addMtp3ServiceAccessPoint(1, 1, 101, 0, 1, null);
        map = router.getNetworkIdList(101);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertNotNull(state);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);
        router.removeMtp3ServiceAccessPoint(1);

        // RSP - available
        sccpStack.getSccpResource().addRemoteSpc(1, 101, 0, 0);
        sccpStack.getSccpResource().addRemoteSpc(2, 102, 0, 0);
        RemoteSignalingPointCodeImpl rspc1 = (RemoteSignalingPointCodeImpl) sccpStack.getSccpResource().getRemoteSpc(1);
        RemoteSignalingPointCodeImpl rspc2 = (RemoteSignalingPointCodeImpl) sccpStack.getSccpResource().getRemoteSpc(2);

        router.removeRule(2);
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 2, -1, null, 1, pattern);
        map = router.getNetworkIdList(101);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertNotNull(state);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        sccpAddress2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 101, 8);
        router.removeRule(2);
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 1, pattern);
        map = router.getNetworkIdList(101);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertNotNull(state);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        // RSP - unavailable / congested
        sccpStack.removeAllResourses();
        sccpAddress1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 101, 8);
        sccpAddress2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 102, 8);
        router.addRoutingAddress(1, sccpAddress1);
        router.addRoutingAddress(2, sccpAddress2);
        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 1, pattern);
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 2, -1, null, 1, pattern);
        sccpStack.getSccpResource().addRemoteSpc(1, 101, 0, 0);
        sccpStack.getSccpResource().addRemoteSpc(2, 102, 0, 0);
        rspc1 = (RemoteSignalingPointCodeImpl) sccpStack.getSccpResource().getRemoteSpc(1);
        rspc2 = (RemoteSignalingPointCodeImpl) sccpStack.getSccpResource().getRemoteSpc(2);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 1);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 1);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, true);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertFalse(state.isAvailavle());

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, true);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertFalse(state.isAvailavle());

        // Dominant
        router.removeRule(1);
        router.removeRule(2);
        router.addRule(1, RuleType.DOMINANT, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, 2, null, 1, pattern);
        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 2);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, true);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 1);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, true);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, true);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertFalse(state.isAvailavle());

        // Loadsharing
        router.removeRule(1);
        router.addRule(1, RuleType.LOADSHARED, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, 2, null, 1, pattern);
        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 0);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 0);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 2);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 2);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, true);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 1);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, true);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 2);

        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, true);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, true);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 1);
        state = map.get(1);
        assertFalse(state.isAvailavle());

        // two affected networkIDs 
        router.removeRule(1);
        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 1, pattern);
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 2, -1, null, 2, pattern);
        SccpRspProxy.setCurrentRestrictionLevel(rspc1, 4);
        SccpRspProxy.setRemoteSpcProhibited(rspc1, false);
        SccpRspProxy.setCurrentRestrictionLevel(rspc2, 2);
        SccpRspProxy.setRemoteSpcProhibited(rspc2, false);
        map = router.getNetworkIdList(-1);
        assertEquals(map.size(), 2);
        state = map.get(1);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 2);
        state = map.get(2);
        assertTrue(state.isAvailavle());
        assertEquals(state.getCongLevel(), 1);
    
    }

}
