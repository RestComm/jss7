/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.oam.common.tcap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmProvider;
import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Management;
import org.mobicents.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterCampaign;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDefSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterMediator;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPCounterProviderImpl;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class RegistrationTest {

    private SccpStackImpl sccpStack;
    private TCAPStackImpl tcapStack1;
    private Ss7Management ss7Man;
    private CounterProviderProxy counterProvider;
    private AlarmProvider alarmProvider;
    private TcapManagementJmx tcapManagement;

    @BeforeMethod
    public void setUp() throws Exception {

        MBeanHostProxy mBeanHostProxy = new MBeanHostProxy();
        counterProvider = new CounterProviderProxy(mBeanHostProxy);
        counterProvider.setName("Test");
        counterProvider.start();

        counterProvider.getCounterCampaignMap().clear();
        counterProvider.store();
        
        counterProvider.stop();
        counterProvider = null;

        sccpStack = new SccpStackImpl("SccpStack");
        sccpStack.start();
        tcapStack1 = new TCAPStackImpl("Test", sccpStack.getSccpProvider(), 8);
        tcapStack1.start();
        tcapStack1.setStatisticsEnabled(true);

        ss7Man = new Ss7Management();
        ss7Man.setRmiPort(9998);

        counterProvider = new CounterProviderProxy(ss7Man);
        counterProvider.setName("Test");

        alarmProvider = new AlarmProvider(ss7Man, ss7Man);

        tcapManagement = new TcapManagementJmx(ss7Man, tcapStack1);
    }

    @AfterMethod
    public void tearDown() {

        tcapManagement.stop();
        alarmProvider.stop();
        counterProvider.stop();
        ss7Man.stop();

        this.tcapStack1.stop();
        this.sccpStack.stop();

    }

    @Test(groups = { "confuguration" })
    public void counterProviderStartsLastTest() throws Exception {

        ss7Man.start();

        tcapManagement.start();
        alarmProvider.start();
        counterProvider.start();

        String csName = tcapManagement.getCounterDefSetList()[0];
        String cmName = tcapManagement.getCounterMediatorName();
        CounterDefSet cds = tcapManagement.getCounterDefSet(csName);

        assertEquals(counterProvider.getCampaignsList().length, 0);
        String[] ss1 = counterProvider.getCounterDefSetList();
        assertEquals(ss1.length, 1);
        assertEquals(counterProvider.getCounterDefSet(ss1[0]).getName(), csName);
        assertEquals(counterProvider.getCounterMediatorLst().size(), 1);
        CounterMediator cm = counterProvider.getCounterMediatorLst().get(cmName);
        assertEquals(cm, tcapManagement);

        counterProvider.createCampaign("camp_01", csName, 10, 1);
        assertEquals(counterProvider.getCampaignsList().length, 1);
        CounterCampaign cc = counterProvider.getCampaign("camp_01");
        assertEquals(cc.getCounterSetName(), csName);
        assertNotNull(cc.getCounterSet());
        assertEquals(cc.getDuration(), 10);
        assertEquals(cc.getCounterSet(), cds);
        assertFalse(cc.isShortCampaign());

        counterProvider.stop();

        counterProvider.start();
        cds = tcapManagement.getCounterDefSet(csName);

        ss1 = counterProvider.getCounterDefSetList();
        assertEquals(ss1.length, 1);
        assertEquals(counterProvider.getCounterDefSet(ss1[0]).getName(), csName);
        assertEquals(counterProvider.getCounterMediatorLst().size(), 1);
        cm = counterProvider.getCounterMediatorLst().get(cmName);
        assertEquals(cm, tcapManagement);

        assertEquals(counterProvider.getCampaignsList().length, 1);
        cc = counterProvider.getCampaign("camp_01");
        assertEquals(cc.getCounterSetName(), csName);
        assertNotNull(cc.getCounterSet());
        assertEquals(cc.getDuration(), 10);
        assertEquals(cc.getCounterSet(), cds);
        assertFalse(cc.isShortCampaign());

        counterProvider.createShortCampaign("camp_02", csName, 20, 1);
        assertEquals(counterProvider.getCampaignsList().length, 2);
        cc = counterProvider.getCampaign("camp_02");
        assertEquals(cc.getCounterSetName(), csName);
        assertNotNull(cc.getCounterSet());
        assertEquals(cc.getDuration(), 20);
        assertEquals(cc.getCounterSet(), cds);
        assertTrue(cc.isShortCampaign());

    }

    @Test(groups = { "confuguration" })
    public void counterProviderStartsFirstTest() throws Exception {

        ss7Man.start();

        counterProvider.start();
        tcapManagement.start();
        alarmProvider.start();

        String csName = tcapManagement.getCounterDefSetList()[0];
        String cmName = tcapManagement.getCounterMediatorName();
        CounterDefSet cds = tcapManagement.getCounterDefSet(csName);

        assertEquals(counterProvider.getCampaignsList().length, 0);
        String[] ss1 = counterProvider.getCounterDefSetList();
        assertEquals(ss1.length, 1);
        assertEquals(counterProvider.getCounterDefSet(ss1[0]).getName(), csName);
        assertEquals(counterProvider.getCounterMediatorLst().size(), 1);
        CounterMediator cm = counterProvider.getCounterMediatorLst().get(cmName);
        assertEquals(cm, tcapManagement);

        counterProvider.createCampaign("camp_01", csName, 10, 1);
        assertEquals(counterProvider.getCampaignsList().length, 1);
        CounterCampaign cc = counterProvider.getCampaign("camp_01");
        assertEquals(cc.getCounterSetName(), csName);
        assertNotNull(cc.getCounterSet());
        assertEquals(cc.getDuration(), 10);
        assertEquals(cc.getCounterSet(), cds);
        assertFalse(cc.isShortCampaign());

        tcapManagement.stop();
        counterProvider.stop();
        ss7Man.stop();

        ss7Man.start();
        counterProvider.start();
        tcapManagement.start();
        cds = tcapManagement.getCounterDefSet(csName);

        ss1 = counterProvider.getCounterDefSetList();
        assertEquals(ss1.length, 1);
        assertEquals(counterProvider.getCounterDefSet(ss1[0]).getName(), csName);
        assertEquals(counterProvider.getCounterMediatorLst().size(), 1);
        cm = counterProvider.getCounterMediatorLst().get(cmName);
        assertEquals(cm, tcapManagement);

        assertEquals(counterProvider.getCampaignsList().length, 1);
        cc = counterProvider.getCampaign("camp_01");
        assertEquals(cc.getCounterSetName(), csName);
        assertNotNull(cc.getCounterSet());
        assertEquals(cc.getDuration(), 10);
        assertEquals(cc.getCounterSet(), cds);
        assertFalse(cc.isShortCampaign());

    }

    @Test(groups = { "confuguration" })
    public void processingStatDataTest() throws Exception {

        ss7Man.start();

        counterProvider.start();
        tcapManagement.start();
        alarmProvider.start();

        String csName = tcapManagement.getCounterDefSetList()[0];
        String cmName = tcapManagement.getCounterMediatorName();
        CounterDefSet cds = tcapManagement.getCounterDefSet(csName);

        counterProvider.createCampaign("camp_01", csName, 60, 1);
        Date tm1 = new Date(2010, 1, 1, 10, 0, 0);
        Date tm2 = new Date(2010, 1, 1, 11, 0, 0);

        TCAPCounterProviderImpl cp = tcapStack1.getCounterProviderImpl();
        SccpAddress localAddress = sccpStack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 20, 8);
        tcapStack1.getProvider().getNewDialog(localAddress, localAddress);

        Thread.sleep(500);
        CounterValueSet cvs1 = counterProvider.getLastCounterValues("camp_01");
        counterProvider.processCampaign("camp_01", tm1);

        Dialog d1 = tcapStack1.getProvider().getNewDialog(localAddress, localAddress);
        Dialog d2 = tcapStack1.getProvider().getNewDialog(localAddress, localAddress);
        cp.updateTcBeginSentCount(d1);
        cp.updateAllEstablishedDialogsCount();
        cp.updateAllEstablishedDialogsCount();
        cp.updateAllDialogsDuration(1000);
        cp.updateAllDialogsDuration(2000);
        cp.updateDialogReleaseCount(d1);
        cp.updateDialogReleaseCount(d2);
        cp.updateOutgoingDialogsPerApplicatioContextName("20.1");

        Thread.sleep(500);
        CounterValueSet cvs2 = counterProvider.getLastCounterValues("camp_01");
        counterProvider.processCampaign("camp_01", tm2);

        Thread.sleep(500);
        CounterValueSet cvs3 = counterProvider.getLastCounterValues("camp_01");

        assertNull(cvs1);
        assertNull(cvs2);
        assertNotNull(cvs3);
        assertTrue(cvs3.getStartTime().equals(tm1));
        assertTrue(cvs3.getEndTime().equals(tm2));
        for (CounterValue cv : cvs3.getCounterValues()) {
            if (cv.getCounterDef().getCounterName().equals("MinDialogsCount")) {
                assertEquals(cv.getLongValue(), 1);
            } else if (cv.getCounterDef().getCounterName().equals("MaxDialogsCount")) {
                assertEquals(cv.getLongValue(), 3);
            } else if (cv.getCounterDef().getCounterName().equals("AllEstablishedDialogsCount")) {
                assertEquals(cv.getLongValue(), 4);
            } else if (cv.getCounterDef().getCounterName().equals("TcBeginSentCount")) {
                assertEquals(cv.getLongValue(), 1);
            } else if (cv.getCounterDef().getCounterName().equals("AllDialogsDuration")) {
                assertTrue(Math.abs(cv.getDoubleValue() - 3.0) < 0.001);
            } else if (cv.getCounterDef().getCounterName().equals("AverageDialogsDuration")) {
                assertTrue(Math.abs(cv.getDoubleValue() - 3.0 / 2) < 0.001);
            } else if (cv.getCounterDef().getCounterName().equals("OutgoingDialogsPerApplicatioContextName")) {
                assertEquals(cv.getComplexValue().length, 1);
                ComplexValue cvv = cv.getComplexValue()[0];
                assertEquals(cvv.getKey(), "20.1");
                assertEquals(cvv.getValue(), 1);
            } else if (cv.getCounterDef().getCounterName().equals("IncomingDialogsPerApplicatioContextName")) {
                assertEquals(cv.getComplexValue().length, 0);
            }
        }
    }

}
