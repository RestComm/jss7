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

package org.mobicents.protocols.ss7.tools.simulator.level3;

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
public class CapManStandardMBean extends StandardMBean {

    public CapManStandardMBean(CapMan impl, Class<CapManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
        // new MBeanAttributeInfo("LocalSsn", int.class.getName(), "Local Ssn", true, true, false),
        // new MBeanAttributeInfo("RemoteSsn", int.class.getName(), "Remote Ssn", true, true, false),
        new MBeanAttributeInfo("RemoteAddressDigits", String.class.getName(),
                "Remote address digits. If empty ROUTING_BASED_ON_DPC_AND_SSN is used for CalledPartyAddress (remoteSpc from SCCP), "
                        + "if not empty ROUTING_BASED_ON_GLOBAL_TITLE is used (address and Ssn from CAP)", true, true, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {};

        return new MBeanInfo(CapMan.class.getName(), "Cap Management", attributes, null, operations, null);
    }

}
