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

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MapManMBean {

//	public int getLocalSsn();
//
//	public void setLocalSsn(int val);
//
//	public int getRemoteSsn();
//
//	public void setRemoteSsn(int val);

	public String getRemoteAddressDigits();

	public void setRemoteAddressDigits(String val);


	public String getOrigReference();

	public void setOrigReference(String val);

	public AddressNatureType getOrigReferenceAddressNature();

	public String getOrigReferenceAddressNature_Value();

	public void setOrigReferenceAddressNature(AddressNatureType val);

	public NumberingPlanMapType getOrigReferenceNumberingPlan();

	public String getOrigReferenceNumberingPlan_Value();

	public void setOrigReferenceNumberingPlan(NumberingPlanMapType val);

	public String getDestReference();

	public void setDestReference(String val);

	public AddressNatureType getDestReferenceAddressNature();

	public String getDestReferenceAddressNature_Value();

	public void setDestReferenceAddressNature(AddressNatureType val);

	public NumberingPlanMapType getDestReferenceNumberingPlan();

	public String getDestReferenceNumberingPlan_Value();

	public void setDestReferenceNumberingPlan(NumberingPlanMapType val);


	public void putOrigReferenceAddressNature(String val);

	public void putOrigReferenceNumberingPlan(String val);

	public void putDestReferenceAddressNature(String val);

	public void putDestReferenceNumberingPlan(String val);

}

