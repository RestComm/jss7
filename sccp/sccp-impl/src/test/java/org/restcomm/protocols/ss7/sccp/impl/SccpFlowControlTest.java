package org.restcomm.protocols.ss7.sccp.impl;

import org.restcomm.protocols.ss7.sccp.impl.SccpFlowControl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SccpFlowControlTest {

    @Test
    public void testUpperEdge() throws Exception {
        SccpFlowControl.SccpFlowControlWindow window = new SccpFlowControl.SccpFlowControlWindow(new SequenceNumberImpl(127), 2);
        assertEquals(window.getUpperEdge().getValue(), 0);
    }
}
