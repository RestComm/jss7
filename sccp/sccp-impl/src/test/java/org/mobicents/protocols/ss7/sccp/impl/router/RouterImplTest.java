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
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class RouterImplTest {
    private final static String RULE1 = "1; #ISDN_MOBILE#NATIONAL#9023629581# ; #ISDN_MOBILE#INTERNATIONAL#79023629581# ;linkset#14083#14155#0\n";
    private final static String RULE2 = "2; #ISDN_MOBILE#INTERNATIONAL#9023629581# ; #ISDN_MOBILE#NATIONAL#9023629581# ;linkset#14083#14155#0\n";
    
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
        rule1 = Rule.getInstance(RULE1);
        rule2 = Rule.getInstance(RULE2);
        
        //cleans config file
        RouterImpl router = new RouterImpl();
        router.clean();
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
        router.add(rule1);
        assertEquals(1, router.list().size());
        router.add(rule2);
        assertEquals(2, router.list().size());
        
        router.remove(1);
        Rule rule = router.list().iterator().next();
        assertEquals(NatureOfAddress.NATIONAL, rule.getPattern().getNatureOfAddress());
    }


}