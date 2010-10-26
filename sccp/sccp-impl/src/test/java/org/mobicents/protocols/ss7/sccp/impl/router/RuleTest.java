/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class RuleTest {
    private final static String RULE = "1; #ISDN_MOBILE#NATIONAL#9023629581# ; #ISDN_MOBILE#INTERNATIONAL#79023629581# ;linkset#14083#14155#0\n";
    
    public RuleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class Rule.
     */
    @Test
    public void testGetInstance() {
        Rule rule = Rule.getInstance(RULE);
        assertEquals(1, rule.getNo());
        assertEquals(NumberingPlan.ISDN_MOBILE, rule.getPattern().getNumberingPlan());
        assertEquals(NatureOfAddress.NATIONAL, rule.getPattern().getNatureOfAddress());
        assertEquals("9023629581", rule.getPattern().getDigits());
        
        assertEquals(NumberingPlan.ISDN_MOBILE, rule.getTranslation().getNumberingPlan());
        assertEquals(NatureOfAddress.INTERNATIONAL, rule.getTranslation().getNatureOfAddress());
        assertEquals("79023629581", rule.getTranslation().getDigits());
        
        assertEquals("linkset", rule.getMTPInfo().getName());
        assertEquals(14083, rule.getMTPInfo().getOpc());
        assertEquals(14155, rule.getMTPInfo().getDpc());
        assertEquals(0, rule.getMTPInfo().getSls());
    }

    /**
     * Test of toString method, of class Rule.
     */
    @Test
    public void testToString() {
        AddressInformation ai = new AddressInformation(-1, NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL, "9023629581", -1);
        AddressInformation tr = new AddressInformation(-1, NumberingPlan.ISDN_MOBILE, NatureOfAddress.INTERNATIONAL, "79023629581", -1);
        MTPInfo mtpInfo = new MTPInfo("linkset", 14083, 14155, 0);
        
        Rule rule = new Rule(1, ai, tr, mtpInfo);
        assertEquals(RULE, rule.toString());
    }

}