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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class BIpChannelType extends EnumeratedBase {

	private static final long serialVersionUID = 1309893222268779973L;
	public static final int VAL_TCP = 1;
	public static final int VAL_SCTP = 2;

	private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
	private static Hashtable<Integer,String> intMap = new Hashtable<Integer,String>();

	static {
		intMap.put(VAL_TCP, "TCP");
		intMap.put(VAL_SCTP, "SCTP");

		stringMap.put("TCP", VAL_TCP);
		stringMap.put("SCTP", VAL_SCTP);
	}

	public BIpChannelType() {
	}

	public BIpChannelType(int val) throws java.lang.IllegalArgumentException {
		super(val);
	}

	public BIpChannelType(Integer val) throws java.lang.IllegalArgumentException {
		super(val);
	}

	public BIpChannelType(String val) throws java.lang.IllegalArgumentException {
		super(val);
	}

	public static BIpChannelType createInstance(String s) {
		Integer i1 = doCreateInstance(s, stringMap, intMap);
		if (i1 == null)
			return null;
		else
			return new BIpChannelType(i1);
	}

	@Override
	protected Hashtable<Integer,String> getIntTable() {
		return intMap;
	}

	@Override
	protected Hashtable<String, Integer> getStringTable() {
		return stringMap;
	}
}

