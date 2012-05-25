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

package org.mobicents.protocols.ss7.tools.simulator.level2;

import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SccpManMBean {

	public int getRemoteSpc();

	public void setRemoteSpc(int val);

	public int getLocalSpc();

	public void setLocalSpc(int val);

	public int getNi();

	public void setNi(int val);

	public int getRemoteSsn();

	public void setRemoteSsn(int val);

	public int getLocalSsn();

	public void setLocalSsn(int val);

	public GlobalTitleType getGlobalTitleType();

	public String getGlobalTitleType_Value();

	public void setGlobalTitleType(GlobalTitleType val);

	public AddressNatureType getAddressNature();

	public String getAddressNature_Value();

	public void setAddressNature(AddressNatureType val);

	public NumberingPlanType getNumberingPlan();

	public String getNumberingPlan_Value();

	public void setNumberingPlan(NumberingPlanType val);

	public int getTranslationType();

	public void setTranslationType(int val);


	public void putGlobalTitleType(String val);

	public void putAddressNature(String val);

	public void putNumberingPlan(String val);

}

