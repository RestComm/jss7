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
package org.mobicents.protocols.ss7.sccp;

import java.util.Map;

/**
 * 
 * @author Amit Bhayani
 *
 */
public interface Mtp3ServiceAccessPoint {

	public int getMtp3Id();

	public int getOpc();

	public int getNi();

	public Mtp3Destination getMtp3Destination(int destId);

	public Map<Integer, Mtp3Destination> getMtp3Destinations();

	public void addMtp3Destination(int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
			throws Exception;

	public void modifyMtp3Destination(int destId, int firstDpc, int lastDpc, int firstSls, int lastSls, int slsMask)
			throws Exception;

	public void removeMtp3Destination(int destId) throws Exception;

	public boolean matches(int dpc, int sls);

}
