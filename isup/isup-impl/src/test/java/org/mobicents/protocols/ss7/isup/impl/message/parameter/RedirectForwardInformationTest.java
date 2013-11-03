/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InvokingRedirectReason;
import org.mobicents.protocols.ss7.isup.message.parameter.PerformingRedirectIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectReason;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangePossible;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * Class to test BCI
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */

public class RedirectForwardInformationTest {
//This one does not use harness, since this param has multiple levels of nesting ....
    public RedirectForwardInformationTest() {
        super();

    }

    private byte[] getBody1() {

        byte[] body = new byte[] {
                //3.99.1 ReturnToInvokingExchangePossible
                0x01,
                    //len
                    0x00,
                0x01,
                    //len
                    0x00,
                //3.99.2
                0x02,
                    //len
                    0x05,
                    //body
                    (byte)0xAA,
                    0,
                    (byte)0xAA,
                    0x55,
                    0x15,
                //3.99.3
                0x03,
                    //len
                    0x06,
                    //body
                    //pri1
                    (byte)(0x80|0x12),
                    //pri2
                    0x12,
                    0x05,
                    //pri3
                    0x11,
                    0x04,
                    //pri4
                    (byte)(0x80|0x2),
                //3.99.4
                0x04,
                   //len
                   0x03,
                   //body
                   0x01,
                   0x02,
                   (byte)(0x80| 0x03)
                
        };
        return body;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        RedirectForwardInformationImpl rfi = new RedirectForwardInformationImpl(getBody1());
        ReturnToInvokingExchangePossible[] rtiep = rfi.getReturnToInvokingExchangePossible();
        Assert.assertNotNull(rtiep);
        Assert.assertEquals(rtiep.length,2);
        Assert.assertNotNull(rtiep[0]);
        Assert.assertNotNull(rtiep[1]);

        ReturnToInvokingExchangeCallIdentifier[] callIds = rfi.getReturnToInvokingExchangeCallIdentifier();
        Assert.assertNotNull(callIds);
        Assert.assertEquals(callIds.length,1);
        ReturnToInvokingExchangeCallIdentifier id = callIds[0];
        Assert.assertNotNull(id);
        Assert.assertEquals(id.getCallIdentity(), 0xAA00AA);
        Assert.assertEquals(id.getSignalingPointCode(), 0x1555);
        
        PerformingRedirectIndicator[] pris = rfi.getPerformingRedirectIndicator();
        Assert.assertNotNull(pris);
        Assert.assertEquals(pris.length,1);
        PerformingRedirectIndicator ri = pris[0];
        Assert.assertNotNull(ri);
        RedirectReason[] rrs = ri.getReason();
        Assert.assertNotNull(rrs);
        Assert.assertEquals(rrs.length,4);
        Assert.assertNotNull(rrs[0]);
        Assert.assertNotNull(rrs[1]);
        Assert.assertNotNull(rrs[2]);
        Assert.assertNotNull(rrs[3]);

        Assert.assertEquals(rrs[0].getRedirectReason(), 18);
        Assert.assertEquals(rrs[0].getRedirectPossibleAtPerformingExchange(), 0);

        Assert.assertEquals(rrs[1].getRedirectReason(), 18);
        Assert.assertEquals(rrs[1].getRedirectPossibleAtPerformingExchange(), 5);

        Assert.assertEquals(rrs[2].getRedirectReason(), 17);
        Assert.assertEquals(rrs[2].getRedirectPossibleAtPerformingExchange(), 4);

        Assert.assertEquals(rrs[3].getRedirectReason(), 2);
        Assert.assertEquals(rrs[3].getRedirectPossibleAtPerformingExchange(), 0);
        
        InvokingRedirectReason[] inrs = rfi.getInvokingRedirectReason();
        Assert.assertNotNull(inrs);
        Assert.assertEquals(inrs.length,1);
        Assert.assertNotNull(inrs[0]);
        InvokingRedirectReason inr = inrs[0];
        RedirectReason[] rrs2 = inr.getReason();
        Assert.assertNotNull(rrs2);
        Assert.assertEquals(rrs2.length,3);
        Assert.assertNotNull(rrs2[0]);
        Assert.assertEquals(rrs2[0].getRedirectReason(), 1);
        Assert.assertNotNull(rrs2[1]);
        Assert.assertEquals(rrs2[1].getRedirectReason(), 2);
        Assert.assertNotNull(rrs2[2]);
        Assert.assertEquals(rrs2[2].getRedirectReason(), 3);
        
    }

    
    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testSetAndGet() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        RedirectForwardInformationImpl rfi = new RedirectForwardInformationImpl();

        rfi.setReturnToInvokingExchangePossible(new ReturnToInvokingExchangePossibleImpl());

        ReturnToInvokingExchangeCallIdentifierImpl callId = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId.setCallIdentity(0XBB00BC);
        ReturnToInvokingExchangeCallIdentifierImpl callId2 = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId2.setCallIdentity(0XCBF0BC);
        callId2.setSignalingPointCode(1);
        rfi.setReturnToInvokingExchangeCallIdentifier(callId,callId2);

        PerformingRedirectIndicatorImpl pri = new PerformingRedirectIndicatorImpl();
        RedirectReasonImpl rr1 = new RedirectReasonImpl();
        rr1.setRedirectReason((byte) 1);
        RedirectReasonImpl rr2 = new RedirectReasonImpl();
        rr2.setRedirectReason((byte) 1);
        rr2.setRedirectPossibleAtPerformingExchange((byte) 2);
        pri.setReason(rr1,rr2);
        rfi.setPerformingRedirectIndicator(pri);
        
        InvokingRedirectReasonImpl irr = new InvokingRedirectReasonImpl();
        //this differs across some params...
        irr.setTag(RedirectForwardInformation.INFORMATION_INVOKING_REDIRECT_REASON);
        irr.setReason(rr1,rr2);
        rfi.setInvokingRedirectReason(irr);

        byte[] data = rfi.encode();
        rfi = new RedirectForwardInformationImpl();
        rfi.decode(data);


        Assert.assertNotNull(rfi.getReturnToInvokingExchangePossible());
        Assert.assertEquals(rfi.getReturnToInvokingExchangePossible().length,1);

        Assert.assertNotNull(rfi.getReturnToInvokingExchangeCallIdentifier());
        Assert.assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier().length,2);
        Assert.assertNotNull(rfi.getReturnToInvokingExchangeCallIdentifier()[0]);
        Assert.assertNotNull(rfi.getReturnToInvokingExchangeCallIdentifier()[1]);
        
        Assert.assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier()[0].getCallIdentity(),0XBB00BC);
        Assert.assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier()[1].getCallIdentity(),0XCBF0BC);
        Assert.assertEquals(rfi.getReturnToInvokingExchangeCallIdentifier()[1].getSignalingPointCode(),1);
     
        Assert.assertNotNull(rfi.getPerformingRedirectIndicator());
        Assert.assertEquals(rfi.getPerformingRedirectIndicator().length,1);

        Assert.assertNotNull(rfi.getPerformingRedirectIndicator()[0].getReason());
        Assert.assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason().length,2);
        Assert.assertNotNull(rfi.getPerformingRedirectIndicator()[0].getReason()[0]);
        Assert.assertNotNull(rfi.getPerformingRedirectIndicator()[0].getReason()[1]);
        Assert.assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason()[0].getRedirectReason(),1);
        Assert.assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason()[1].getRedirectReason(),1);
        Assert.assertEquals(rfi.getPerformingRedirectIndicator()[0].getReason()[1].getRedirectPossibleAtPerformingExchange(),2);

        Assert.assertNotNull(rfi.getInvokingRedirectReason()[0].getReason());
        Assert.assertEquals(rfi.getInvokingRedirectReason()[0].getReason().length,2);
        Assert.assertNotNull(rfi.getInvokingRedirectReason()[0].getReason()[0]);
        Assert.assertNotNull(rfi.getInvokingRedirectReason()[0].getReason()[1]);
        Assert.assertEquals(rfi.getInvokingRedirectReason()[0].getReason()[0].getRedirectReason(),1);
        Assert.assertEquals(rfi.getInvokingRedirectReason()[0].getReason()[1].getRedirectReason(),1);
        //0 casuse this one does not have it.
        Assert.assertEquals(rfi.getInvokingRedirectReason()[0].getReason()[1].getRedirectPossibleAtPerformingExchange(),0);
    }

}
