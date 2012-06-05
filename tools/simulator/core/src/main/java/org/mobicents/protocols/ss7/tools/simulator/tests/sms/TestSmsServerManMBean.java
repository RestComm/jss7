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

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface TestSmsServerManMBean {

	public AddressNatureType getAddressNature();

	public String getAddressNature_Value();

	public void setAddressNature(AddressNatureType val);

	public NumberingPlanType getNumberingPlan();

	public String getNumberingPlan_Value();

	public void setNumberingPlan(NumberingPlanType val);

	// ServiceCenterAddress Address is getting from a SCCP CallingPartyAddress

	public MapProtocolVersion getMapProtocolVersion();

	public String getMapProtocolVersion_Value();

	public void setMapProtocolVersion(MapProtocolVersion val);

	public int getHlrSsn();

	public void setHlrSsn(int val);

	public int getVlrSsn();

	public void setVlrSsn(int val);

	public TypeOfNumberType getTypeOfNumber();

	public String getTypeOfNumber_Value();

	public void setTypeOfNumber(TypeOfNumberType val);

	public NumberingPlanIdentificationType getNumberingPlanIdentification();

	public String getNumberingPlanIdentification_Value();

	public void setNumberingPlanIdentification(NumberingPlanIdentificationType val);


	public void putAddressNature(String val);

	public void putNumberingPlan(String val);

	public void putMapProtocolVersion(String val);

	public void putTypeOfNumber(String val);

	public void putNumberingPlanIdentification(String val);


	public String getCurrentRequestDef();

	
	public String performSRIForSM(String destIsdnNumber);

	public String performSRIForSM_MtForwardSM(String msg, String destIsdnNumber, String origIsdnNumber);

	public String performMtForwardSM(String msg, String destImsi, String vlrNumber, String origIsdnNumber);

	public String closeCurrentDialog();

}
