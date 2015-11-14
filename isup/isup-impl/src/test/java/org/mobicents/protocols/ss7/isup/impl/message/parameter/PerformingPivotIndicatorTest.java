/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag. 
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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.message.parameter.PivotReason;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author baranowb
 * 
 */
public class PerformingPivotIndicatorTest {

    /**
     * 
     */
    public PerformingPivotIndicatorTest() {
        // TODO Auto-generated constructor stub
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testSetEncodeDecodeGet() throws Exception {
        PerformingPivotIndicatorImpl ppi = new PerformingPivotIndicatorImpl();
        PivotReasonImpl pr1 = new PivotReasonImpl();
        pr1.setPivotReason((byte) 1);
        pr1.setPivotPossibleAtPerformingExchange((byte) 5);
        PivotReasonImpl pr2 = new PivotReasonImpl();
        pr2.setPivotReason((byte) 21);
        pr2.setPivotPossibleAtPerformingExchange((byte) 3);
        ppi.setReason(pr1, pr2);
        byte[] b = ppi.encode();
        ppi = new PerformingPivotIndicatorImpl();
        ppi.decode(b);
        PivotReason[] reasons = ppi.getReason();
        Assert.assertNotNull(reasons);
        Assert.assertEquals(reasons.length, 2);
        Assert.assertNotNull(reasons[0]);
        Assert.assertNotNull(reasons[1]);
        Assert.assertEquals(reasons[0].getPivotReason(), (byte) 1);
        Assert.assertEquals(reasons[0].getPivotPossibleAtPerformingExchange(), (byte) 5);
        Assert.assertEquals(reasons[1].getPivotReason(), (byte) 21);
        Assert.assertEquals(reasons[1].getPivotPossibleAtPerformingExchange(), (byte) 3);
    }
}