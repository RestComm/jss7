package org.mobicents.protocols.ss7.sccp.impl;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class ComparisonTest {
    @Test
    public void testRangeContains() throws Exception {
        assertTrue(FlowControlWindows.Range.rangeFromTo(125, 3).contains(0));
        assertFalse(FlowControlWindows.Range.rangeFromTo(125, 3).contains(4));

        assertTrue(FlowControlWindows.Range.rangeFromTo(4, 10).contains(6));
        assertFalse(FlowControlWindows.Range.rangeFromTo(4, 10).contains(12));
    }
}
