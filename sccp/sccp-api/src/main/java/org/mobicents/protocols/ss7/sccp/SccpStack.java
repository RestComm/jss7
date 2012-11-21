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

import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;

/**
 * @author amit bhayani
 * @author baranowb
 * @author kulikov
 */
public interface SccpStack {
	public final static int UDT_ONLY = 1;
	public final static int XUDT_ONLY = 2;

	/**
	 * Starts SCCP stack.
	 * 
	 * @throws java.lang.IllegalStateException
	 */
	public void start() throws IllegalStateException;

	/**
	 * Terminates SCCP stack.
	 * 
	 * @throws java.lang.IllegalStateException
	 * @throws org.mobicents.protocols.StartFailedException
	 */
	public void stop();
	
	/**
	 * Returns the name of this stack
	 * @return
	 */
	public String getName();

	/**
	 * Exposes SCCP provider object to SCCP user.
	 * 
	 * @return SCCP provider object.
	 */
	public SccpProvider getSccpProvider();

	/**
	 * Set the persist directory to store the xml files
	 * 
	 * @return
	 */
	public String getPersistDir();

	/**
	 * Get the persist directory from which to read the xml files
	 * 
	 * @param persistDir
	 */
	public void setPersistDir(String persistDir);

	/**
	 * If set, the signaling point code from SCCP called/calling address will be
	 * removed if corresponding routing is based on GT
	 * 
	 * @param removeSpc
	 */
	public void setRemoveSpc(boolean removeSpc);

	/**
	 * Get the remove siganling point code flag
	 * 
	 * @return
	 */
	public boolean isRemoveSpc();

	public SccpResource getSccpResource();

	public int getSstTimerDuration_Min();
	
	public int getSstTimerDuration_Max();
	
	public double getSstTimerDuration_IncreaseFactor();
	
	public int getZMarginXudtMessage();
	
	public int getMaxDataMessage();
	
	public int getReassemblyTimerDelay();
	
	public Map<Integer, Mtp3UserPart> getMtp3UserParts();

	public Mtp3UserPart getMtp3UserPart(int id);
	
	public Router getRouter();

}
