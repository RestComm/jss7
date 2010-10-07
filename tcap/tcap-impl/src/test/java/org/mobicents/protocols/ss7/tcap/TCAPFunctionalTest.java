package org.mobicents.protocols.ss7.tcap;

import junit.framework.TestCase;

import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Test for call flow.
 * @author baranowb
 *
 */
public class TCAPFunctionalTest extends TestCase {

    private static final int _WAIT_TIMEOUT = 90000;
    public static final long[] _ACN_ = new long[]{0, 4, 0, 0, 1, 0, 19, 2};
    private SccpStackImpl sccpStack = new SccpStackImpl();
    private SccpProvider provider;
    private TCAPStackImpl stack1;
    private TCAPStackImpl stack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sccpStack.start();

        this.provider = sccpStack.getSccpProvider();
        
        GlobalTitle gt1 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "123");
        GlobalTitle gt2 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "321");
        
        peer1Address = new SccpAddress(gt1, 0);
        peer2Address = new SccpAddress(gt2, 0);
        
        this.stack1 = new TCAPStackImpl(provider, peer1Address);
        this.stack2 = new TCAPStackImpl(provider, peer2Address);
        
        this.stack1.start();
        this.stack2.start();
        //create test classes
        this.client = new Client(this.stack1, this, peer1Address, peer2Address);
        this.server = new Server(this.stack2, this, peer2Address, peer1Address);

    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */

    @Override
    protected void tearDown() throws Exception {
        sccpStack.stop();
        this.stack1.stop();
        this.stack2.stop();
        super.tearDown();

    }

    @Test
    public void testSimpleTCWithDialog() throws Exception {
        client.start();
        waitForEnd();
        assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
        assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());
    }

    private void waitForEnd() {
        try {
            Thread.currentThread().sleep(_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            fail("Interrupted on wait!");
        }
    }
}
