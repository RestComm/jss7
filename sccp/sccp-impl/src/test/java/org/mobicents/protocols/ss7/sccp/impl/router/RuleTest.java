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
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class RuleTest {
    private final static String RULE = "1; # #NATIONAL#9023629581# ; # #INTERNATIONAL#79023629581# ;linkset#14083#14155#0\n";
    
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

    @Test
    public void testMatches() {
        Rule rule = Rule.getInstance(RULE);
        SccpAddress address = new SccpAddress(GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "9023629581"), 0);
        assertTrue(rule.matches(address));
    }
    
    @Test
    public void testTranslation() {
        SccpAddress a1 = new SccpAddress(GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "9023629581"), 0);
        Rule rule = Rule.getInstance(RULE);
        
        SccpAddress a2 = rule.translate(a1);
        assertEquals(a2.getGlobalTitle().getIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY);
        assertEquals(NatureOfAddress.INTERNATIONAL, ((GT0001)a2.getGlobalTitle()).getNoA());
        assertEquals("79023629581", a2.getGlobalTitle().getDigits());
    }
    /**
     * Test of getInstance method, of class Rule.
     */
    @Test
    public void testGetInstance() {
        Rule rule = Rule.getInstance(RULE);
        assertEquals(1, rule.getNo());
        assertEquals(null, rule.getPattern().getNumberingPlan());
        assertEquals(NatureOfAddress.NATIONAL, rule.getPattern().getNatureOfAddress());
        assertEquals("9023629581", rule.getPattern().getDigits());
        
        assertEquals(null, rule.getTranslation().getNumberingPlan());
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
        AddressInformation ai = new AddressInformation(-1, null, NatureOfAddress.NATIONAL, "9023629581", -1);
        AddressInformation tr = new AddressInformation(-1, null, NatureOfAddress.INTERNATIONAL, "79023629581", -1);
        MTPInfo mtpInfo = new MTPInfo("linkset", 14083, 14155, 0);
        
        Rule rule = new Rule(1, ai, tr, mtpInfo);
        assertEquals(RULE, rule.toString());
    }

}