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

import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterCampaign;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterMediator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ConfigurationTest {

    @BeforeMethod
    public void setUp() throws Exception {
        MBeanHostProxy mBeanHostProxy = new MBeanHostProxy();
        CounterProviderProxy counterProvider = new CounterProviderProxy(mBeanHostProxy);
        counterProvider.setName("Test");
        counterProvider.start();

        counterProvider.getCounterCampaignMap().clear();
        counterProvider.store();

        counterProvider.stop();
    }

    @Test(groups = { "confuguration" })
    public void storingCampaignsTest() throws Exception {

        MBeanHostProxy mBeanHostProxy = new MBeanHostProxy();
        CounterProviderProxy counterProvider = new CounterProviderProxy(mBeanHostProxy);
        counterProvider.setName("Test");
        counterProvider.start();

        CounterMediator cm = new CounterMediatorProxy();
        counterProvider.registerCounterMediator(cm);

        counterProvider.createCampaign("camp1", "counterSet1", 5, 1);
        counterProvider.createCampaign("camp2", "counterSet2", 60, 1);
        try {
            counterProvider.createCampaign("camp1", "counterSet3", 60, 1);
            fail("Same campaignName must bring Exception");
        } catch (Exception e) {
        }
        try {
            counterProvider.createCampaign("camp3", "counterSet1", 59, 1);
            fail("Wrong duration must bring Exception");
        } catch (Exception e) {
        }

        assertEquals(counterProvider.getCounterCampaignMap().size(), 2);

        counterProvider.stop();


        counterProvider = new CounterProviderProxy(mBeanHostProxy);
        counterProvider.setName("Test");
        counterProvider.start();

        assertEquals(counterProvider.getCampaignsList().length, 2);

        CounterCampaign camp = counterProvider.getCampaign("camp1");
        assertEquals(camp.getName(), "camp1");
        assertEquals(camp.getCounterSetName(), "counterSet1");
        assertEquals(camp.getDuration(), 5);

        camp = counterProvider.getCampaign("camp2");
        assertEquals(camp.getName(), "camp2");
        assertEquals(camp.getCounterSetName(), "counterSet2");
        assertEquals(camp.getDuration(), 60);

        counterProvider.destroyCampaign("camp1");
        assertEquals(counterProvider.getCampaignsList().length, 1);

        counterProvider.stop();
    }

}
