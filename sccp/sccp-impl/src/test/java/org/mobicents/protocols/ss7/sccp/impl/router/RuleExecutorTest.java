package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author amit bhayani
 *
 */
public class RuleExecutorTest {

    RuleExecutor ruleExecutor = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        RouterImpl router = new RouterImpl();
        try {
            router.start();
            router.deleteRule("Rule1");
            router.deleteRule("Rule2");
            router.deleteRule("Rule3");
            router.stop();
        } catch (Exception e) {
            // ignore
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateRule() throws Exception {
        ruleExecutor = new RuleExecutor();
        RouterImpl router = new RouterImpl();
        router.start();
        ruleExecutor.setRouter(router);

        String message = ruleExecutor
                .execute("sccprule create Rule1 pattern tt -1 np -1 noa 3 digits 99604 ssn -1 translation tt -1 np 3 noa 4 digits 77865 ssn -1"
                        .split(" "));
        assertEquals(1, router.list().size());

        message = ruleExecutor
                .execute("sccprule create Rule2 pattern tt -1 np 3 noa 4 digits 99604 ssn -1 mtpinfo name name1 opc 12 dpc 56 sls 1"
                        .split(" "));
        assertEquals(2, router.list().size());

        message = ruleExecutor
                .execute("sccprule create Rule3 pattern tt -1 np 3 noa 4 digits 99604 ssn -1 translation tt -1 np 3 noa 4 digits 77865 ssn -1 mtpinfo name name1 opc 12 dpc 56 sls 1"
                        .split(" "));
        assertEquals(3, router.list().size());
        router.stop();
    }

}
