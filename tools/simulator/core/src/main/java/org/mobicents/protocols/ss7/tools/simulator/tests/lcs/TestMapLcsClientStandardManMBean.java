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

package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;


/**
*
* @author sergey vetyutnev
*
*/
public class TestMapLcsClientStandardManMBean extends StandardMBean {

    public TestMapLcsClientStandardManMBean(TestMapLcsClientMan impl, Class<TestMapLcsClientManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        // TODO: Put real attributes and operations

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("AddressNature", AddressNatureType.class.getName(),
                                       "AddressNature parameter for mlcNumber creating",
                                       true, true, false),
                new MBeanAttributeInfo("NumberingPlanType", NumberingPlanMapType.class.getName(),
                                       "NumberingPlanType parameter for mlcNumber creating",
                                       true, true, false),
                new MBeanAttributeInfo("NumberingPlan", String.class.getName(),
                                       "NumberingPlan parameter for mlcNumber creating",
                                       true, true, false),

                new MBeanAttributeInfo("IMSI", String.class.getName(),
                                       "IMSI parameter for targetMS creating",
                                       true, true, false), };

        MBeanParameterInfo[] performSRIRequestParam = new MBeanParameterInfo[] { };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("performSendRoutingInfoForLCSRequest", "Send Routing Information for LCS request",
                                       performSRIRequestParam, String.class.getName(), MBeanOperationInfo.ACTION),
                 };

        return new MBeanInfo(TestMapLcsClientMan.class.getName(), "MapLcsClient test parameters management", attributes, null, operations, null);
    }

}
