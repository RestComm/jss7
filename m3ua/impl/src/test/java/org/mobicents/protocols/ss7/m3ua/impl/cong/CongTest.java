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

package org.mobicents.protocols.ss7.m3ua.impl.cong;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.mobicents.ss7.congestion.BaseCongestionMonitor;
import org.mobicents.ss7.congestion.CongestionListener;
import org.mobicents.ss7.congestion.CongestionTicket;
import org.mobicents.ss7.congestion.CongestionTicketImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CongTest {

    @Test
    public void MemoryCongestionMonitorTest() throws Exception {
//        CongestionTicketImpl tik = new CongestionTicketImpl("QQQQ", 3);
//        tik.setAttribute("a1", "vv1");
//        tik.setAttribute("a2", null);


        TestCongestionMonitor monitor = new TestCongestionMonitor();

        CongestionListenerProxy listener = new CongestionListenerProxy();
        monitor.addCongestionListener(listener);

        monitor.monitor();
        CongestionTicket[] a1 = monitor.getCongestionTicketsList();
        assertNull(a1);
        assertEquals(listener.lstCong.size(), 0);
        assertEquals(listener.lstFinish.size(), 0);

        monitor.testValue = 3;
        monitor.monitor();
        a1 = monitor.getCongestionTicketsList();
        assertNull(a1);
        assertEquals(listener.lstCong.size(), 0);
        assertEquals(listener.lstFinish.size(), 0);

        monitor.testValue = 5.5;
        monitor.monitor();
        a1 = monitor.getCongestionTicketsList();
        assertNotNull(a1);
        assertEquals(a1[0].getLevel(), 1);
        assertEquals(listener.lstCong.size(), 1);
        assertEquals(listener.lstFinish.size(), 0);

        monitor.testValue = 3;
        monitor.monitor();
        a1 = monitor.getCongestionTicketsList();
        assertNotNull(a1);
        assertEquals(a1[0].getLevel(), 1);
        assertEquals(listener.lstCong.size(), 1);
        assertEquals(listener.lstFinish.size(), 0);

        monitor.testValue = 0;
        monitor.monitor();
        a1 = monitor.getCongestionTicketsList();
        assertNull(a1);
        assertEquals(listener.lstCong.size(), 1);
        assertEquals(listener.lstFinish.size(), 1);

        monitor.testValue = 600;
        monitor.monitor();
        a1 = monitor.getCongestionTicketsList();
        assertNotNull(a1);
        assertEquals(a1[0].getLevel(), 3);
        assertEquals(listener.lstCong.size(), 2);
        assertEquals(listener.lstFinish.size(), 1);

        monitor.testValue = 3;
        monitor.monitor();
        a1 = monitor.getCongestionTicketsList();
        assertNotNull(a1);
        assertEquals(a1[0].getLevel(), 1);
        assertEquals(listener.lstCong.size(), 2);
        assertEquals(listener.lstFinish.size(), 2);

        listener.lstCong.clear();
        listener.lstFinish.clear();
        monitor.removeCongestionListener(listener);
        monitor.addCongestionListener(listener);
        assertEquals(listener.lstCong.size(), 1);
        assertEquals(listener.lstFinish.size(), 0);
    }

    class TestCongestionMonitor extends BaseCongestionMonitor {
        protected double[] alarmThreshold = new double[] { 5, 50, 500 };
        protected double[] backToNormalMemoryThreshold = new double[] { 2, 20, 200 };
        protected double testValue = 0;
        protected int alarmLevel = 0;

        @Override
        public void monitor() {
            super.applyNewValue(alarmLevel, testValue, alarmThreshold, backToNormalMemoryThreshold, true);
        }

        @Override
        public String getSource() {
            return "TestMonitor";
        }

        @Override
        protected CongestionTicketImpl generateTicket() {
            return new CongestionTicketImpl("TestMon", alarmLevel);
        }

        @Override
        protected int getAlarmLevel() {
            return alarmLevel;
        }

        @Override
        protected void setAlarmLevel(int val) {
            alarmLevel = val;
        }

        @Override
        protected String getAlarmDescription() {
            return "Test error description";
        }
    }

    class CongestionListenerProxy implements CongestionListener {

        public ArrayList<CongestionTicket> lstCong = new ArrayList<CongestionTicket>();
        public ArrayList<CongestionTicket> lstFinish = new ArrayList<CongestionTicket>();

        @Override
        public void onCongestionStart(CongestionTicket ticket) {
            lstCong.add(ticket);
        }

        @Override
        public void onCongestionFinish(CongestionTicket ticket) {
            lstFinish.add(ticket);
        }

    }

}
