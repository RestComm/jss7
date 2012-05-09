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
public class MapManStandardMBean extends StandardMBean {

	public MapManStandardMBean(MapMan impl, Class<MapManMBean> intf) throws NotCompliantMBeanException {
		super(impl, intf);
	}

	@Override
	public MBeanInfo getMBeanInfo() {

		MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] { 
				new MBeanAttributeInfo("LocalPc", int.class.getName(), "Local PC", true, true, false),
				new MBeanAttributeInfo("RemotePc", int.class.getName(), "Remote PC", true, true, false),
				new MBeanAttributeInfo("LocalSsn", int.class.getName(), "Local Ssn", true, true, false),
				new MBeanAttributeInfo("RemoteSsn", int.class.getName(), "Remote Ssn", true, true, false),
				new MBeanAttributeInfo("OrigReference", String.class.getName(), "Origin reference string", true, true, false),
				new MBeanAttributeInfo("DestReference", String.class.getName(), "Destination reference string", true, true, false),
		};

		MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

		MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
//				new MBeanOperationInfo("putSctpIPChannelType", "IP channel type: SCTP or TCP: 1:TCP,2:SCTP", signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
//
//				new MBeanOperationInfo("putM3uaFunctionality", "M3ua functionality type: 1:IPSP,2:AS,3:SGW", signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
//				new MBeanOperationInfo("putM3uaIPSPType", "M3ua IPSP type: 1:CLIENT,2:SERVER", signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
//				new MBeanOperationInfo("putM3uaExchangeType", "M3ua exchange type: 1:SE,2:DE", signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
		};

		return new MBeanInfo(MapMan.class.getName(), "Map Management", attributes, null, operations, null);
	}
}

