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