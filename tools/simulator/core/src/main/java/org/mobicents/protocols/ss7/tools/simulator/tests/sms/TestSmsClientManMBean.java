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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.MapProtocolVersion;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface TestSmsClientManMBean {

	public AddressNature getServiceCenterAddressAddressNature();

	public void setServiceCenterAddressAddressNature(AddressNature val);

	public NumberingPlan getServiceCenterAddressNumberingPlan();

	public void setServiceCenterAddressNumberingPlan(NumberingPlan val);	

	public String getServiceCenterAddressAddress();

	public void setServiceCenterAddressAddress(String val);	

	public MapProtocolVersion getMapProtocolVersion();

	public String getMapProtocolVersion_Value();

	public void setMapProtocolVersion(MapProtocolVersion val);	


	public void putMapProtocolVersion(String val);

	public String performMoForwardSM(String msg, String origIsdnNumber, String targetIsdnNumber);

	public String closeCurrentDialog();

}
