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
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectReason;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * Class to test BCI
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */

public class RedirectBackwardInformationTest {
//This one does not use harness, since this param has multiple levels of nesting ....
    public RedirectBackwardInformationTest() {
        super();

    }

    private byte[] getBody1() {

        byte[] body = new byte[] {
          //3.100.1 duration
          0x01,
              //len
              0x02,
              (byte) 0xAA,
              (byte) 0xBB,
          //3.100.2
              0x02,
                  //len
                  0x05,
                  //body
                  (byte) 0xAA,
                  0,
                  (byte) 0xAA,
                  0x55,
                  0x15,
           //3.100.3
              0x03,
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
        RedirectBackwardInformationImpl parameter = new RedirectBackwardInformationImpl(getBody1());
        
        ReturnToInvokingExchangeDuration[] rtied = parameter.getReturnToInvokingExchangeDuration();
        Assert.assertNotNull(rtied);
        Assert.assertEquals(rtied.length,1);
        Assert.assertNotNull(rtied[0]);
        Assert.assertEquals(rtied[0].getDuration(),0xBBAA);

        ReturnToInvokingExchangeCallIdentifier[] callIds = parameter.getReturnToInvokingExchangeCallIdentifier();
        Assert.assertNotNull(callIds);
        Assert.assertEquals(callIds.length,1);
        ReturnToInvokingExchangeCallIdentifier id = callIds[0];
        Assert.assertNotNull(id);
        Assert.assertEquals(id.getCallIdentity(), 0xAA00AA);
        Assert.assertEquals(id.getSignalingPointCode(), 0x1555);

        InvokingRedirectReason[] inrs = parameter.getInvokingRedirectReason();
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
        RedirectBackwardInformationImpl parameter = new RedirectBackwardInformationImpl();

       ReturnToInvokingExchangeDurationImpl duration  = new ReturnToInvokingExchangeDurationImpl();
       duration.setDuration(0xAA0C);
       parameter.setReturnToInvokingExchangeDuration(duration);

        ReturnToInvokingExchangeCallIdentifierImpl callId = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId.setCallIdentity(0XBB00BC);
        ReturnToInvokingExchangeCallIdentifierImpl callId2 = new ReturnToInvokingExchangeCallIdentifierImpl();
        callId2.setCallIdentity(0XCBF0BC);
        callId2.setSignalingPointCode(1);
        parameter.setReturnToInvokingExchangeCallIdentifier(callId,callId2);

        InvokingRedirectReasonImpl irr = new InvokingRedirectReasonImpl();
        //this differs across some params...
        irr.setTag(RedirectBackwardInformation.INFORMATION_INVOKING_REDIRECT_REASON);
        RedirectReasonImpl rr1 = new RedirectReasonImpl();
        rr1.setRedirectReason((byte) 1);
        RedirectReasonImpl rr2 = new RedirectReasonImpl();
        rr2.setRedirectReason((byte) 1);
        rr2.setRedirectPossibleAtPerformingExchange((byte) 2);
        irr.setReason(rr1,rr2);
        parameter.setInvokingRedirectReason(irr);

        byte[] data = parameter.encode();
        parameter = new RedirectBackwardInformationImpl();
        parameter.decode(data);

        Assert.assertNotNull(parameter.getReturnToInvokingExchangeDuration());
        Assert.assertEquals(parameter.getReturnToInvokingExchangeDuration().length,1);
        Assert.assertNotNull(parameter.getReturnToInvokingExchangeDuration()[0]);
        
        Assert.assertEquals(parameter.getReturnToInvokingExchangeDuration()[0].getDuration(),0xAA0C);

        Assert.assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier());
        Assert.assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier().length,2);
        Assert.assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier()[0]);
        Assert.assertNotNull(parameter.getReturnToInvokingExchangeCallIdentifier()[1]);
        
        Assert.assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[0].getCallIdentity(),0XBB00BC);
        Assert.assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[1].getCallIdentity(),0XCBF0BC);
        Assert.assertEquals(parameter.getReturnToInvokingExchangeCallIdentifier()[1].getSignalingPointCode(),1);


        Assert.assertNotNull(parameter.getInvokingRedirectReason()[0].getReason());
        Assert.assertEquals(parameter.getInvokingRedirectReason()[0].getReason().length,2);
        Assert.assertNotNull(parameter.getInvokingRedirectReason()[0].getReason()[0]);
        Assert.assertNotNull(parameter.getInvokingRedirectReason()[0].getReason()[1]);
        Assert.assertEquals(parameter.getInvokingRedirectReason()[0].getReason()[0].getRedirectReason(),1);
        Assert.assertEquals(parameter.getInvokingRedirectReason()[0].getReason()[1].getRedirectReason(),1);
        //0 casuse this one does not have it.
        Assert.assertEquals(parameter.getInvokingRedirectReason()[0].getReason()[1].getRedirectPossibleAtPerformingExchange(),0);
    }

}
