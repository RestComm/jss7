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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface TestCapScfManMBean {

	public CapApplicationContextScf getCapApplicationContext();

	public String getCapApplicationContext_Value();

	public void setCapApplicationContext(CapApplicationContextScf val);	


	public void putCapApplicationContext(String val);


	public String getCurrentRequestDef();

	
	public String closeCurrentDialog();

	public String performAssistRequestInstructions(String msg);

}
