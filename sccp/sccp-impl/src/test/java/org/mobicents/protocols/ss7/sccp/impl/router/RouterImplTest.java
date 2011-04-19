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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

import static org.junit.Assert.*;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class RouterImplTest {

    private Rule rule1, rule2;

    public RouterImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        rule1 = new Rule("Rule1", new AddressInformation(-1,
                NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL,
                "9023629581", -1), new AddressInformation(-1,
                NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL,
                "79023629581", -1), new MTPInfo("linkset", 14083, 14155, 0));
        rule2 = new Rule("Rule2", new AddressInformation(-1,
                NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL,
                "9023629581", -1), new AddressInformation(-1,
                NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL,
                "9023629581", -1), null);

        // cleans config file
        RouterImpl router = new RouterImpl();
        try {
            router.start();
            router.deleteRule("Rule1");
            router.deleteRule("Rule2");
            router.deleteRule("Rule3");
            router.deleteRule("Rule4");
            router.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class RouterImpl.
     */
    @Test
    public void testRouter() throws Exception {
        RouterImpl router = new RouterImpl();
        router.start();
        router.addRule(rule1);
        assertEquals(1, router.list().size());
        router.addRule(rule2);
        assertEquals(2, router.list().size());

        router.deleteRule("Rule2");
        Rule rule = router.list().iterator().next();
        assertEquals(NatureOfAddress.NATIONAL, rule.getPattern()
                .getNatureOfAddress());
        router.stop();
    }

}