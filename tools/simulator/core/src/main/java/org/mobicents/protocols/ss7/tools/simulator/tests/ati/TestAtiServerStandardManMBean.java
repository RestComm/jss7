/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

package org.mobicents.protocols.ss7.tools.simulator.tests.ati;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
*
* @author sergey vetyutnev
*
*/
public class TestAtiServerStandardManMBean extends StandardMBean {

    public TestAtiServerStandardManMBean(TestAtiServerMan impl, Class<TestAtiServerManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("ATIReaction", ATIReaction.class.getName(), "ATI response type", true, true, false),
                new MBeanAttributeInfo("ATIReaction_Value", String.class.getName(), "ATI response type", true, false, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

//        MBeanParameterInfo[] performATIParam = new MBeanParameterInfo[] { new MBeanParameterInfo("msg", String.class.getName(), "Message text"),
//                new MBeanParameterInfo("address", String.class.getName(), "SubscriberIdentity: IMSI or MSISDN"), };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
//                new MBeanOperationInfo("performAtiRequest", "Send ATI request", performATIParam, String.class.getName(), MBeanOperationInfo.ACTION),
//                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putATIReaction", "ATI response type: "
                        + "1:ReturnSuccess,2:ReturnSuccessSubscriberState,3:ReturnSystemFailureError,4:ReturnDataMissingError,5:ReturnUnknownSubscriberError",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                };

        return new MBeanInfo(TestAtiServerMan.class.getName(), "AtiServer test parameters management", attributes, null, operations, null);
    }

}
