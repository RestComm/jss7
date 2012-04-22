/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.isup.impl;

import java.util.HashMap;
import java.util.Iterator;

import org.mobicents.protocols.ss7.isup.CircuitManager;

/**
 * @author baranowb
 *
 */
public class CircuitManagerImpl implements CircuitManager {

	protected HashMap<Integer, Integer> cicMap = new HashMap<Integer, Integer>();
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.CircuitManager#addCircuit(int, int)
	 */
	@Override
	public void addCircuit(int cic, int dpc) {
		cicMap.put(cic, dpc);

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.CircuitManager#removeCircuit(int)
	 */
	@Override
	public void removeCircuit(int cic) {
		this.cicMap.remove(cic);

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.CircuitManager#getCircuits()
	 */
	@Override
	public int[] getCircuits() {
		int[] x = new int[this.cicMap.size()];
		Iterator<Integer> it = this.cicMap.keySet().iterator();
		int index = 0;
		while(it.hasNext())
		{
			x[index++] = it.next();
		}
		return x;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.CircuitManager#getDpc(int)
	 */
	@Override
	public int getDpc(int cic) {
		if(isCircuitPresent(cic))
		{
			return this.cicMap.get(cic);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.isup.CircuitManager#isCircuitPresent(int)
	 */
	@Override
	public boolean isCircuitPresent(int cic) {
		return this.cicMap.containsKey(cic);
	}

}
