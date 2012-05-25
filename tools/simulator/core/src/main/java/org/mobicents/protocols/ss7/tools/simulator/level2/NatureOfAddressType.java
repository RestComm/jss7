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

import java.util.Hashtable;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class NatureOfAddressType extends EnumeratedBase {

	private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
	private static Hashtable<Integer,String> intMap = new Hashtable<Integer,String>();

	static {
		intMap.put(NatureOfAddress.UNKNOWN.getValue(), NatureOfAddress.UNKNOWN.toString());
		intMap.put(NatureOfAddress.SUBSCRIBER.getValue(), NatureOfAddress.SUBSCRIBER.toString());
		intMap.put(NatureOfAddress.NATIONAL.getValue(), NatureOfAddress.NATIONAL.toString());
		intMap.put(NatureOfAddress.INTERNATIONAL.getValue(), NatureOfAddress.INTERNATIONAL.toString());

		stringMap.put(NatureOfAddress.UNKNOWN.toString(), NatureOfAddress.UNKNOWN.getValue());
		stringMap.put(NatureOfAddress.SUBSCRIBER.toString(), NatureOfAddress.SUBSCRIBER.getValue());
		stringMap.put(NatureOfAddress.NATIONAL.toString(), NatureOfAddress.NATIONAL.getValue());
		stringMap.put(NatureOfAddress.INTERNATIONAL.toString(), NatureOfAddress.INTERNATIONAL.getValue());
	}

}
