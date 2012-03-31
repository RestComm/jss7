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

package org.mobicents.protocols.ss7.sccp;

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
	 * Exposes SCCP provider object to SCCP user.
	 * 
	 * @return SCCP provider object.
	 */
	public SccpProvider getSccpProvider();

//	/**
//	 * <p>
//	 * Set the local signaling point for this SCCP instance. The local signaling
//	 * point will be added as OPC for outgoing MTP3 MSU.
//	 * </p>
//	 * <p>
//	 * For incoming MSU, after translation the point code of the Called Party
//	 * Address (SCCP Address) will compared with localSpc, if it matches MSU
//	 * will be consumed
//	 * </p>
//	 * 
//	 * @param localSpc
//	 */
//	public void setLocalSpc(int localSpc);
//
//	/**
//	 * Get the local signaling point set for this SCCP instance
//	 * 
//	 * @return
//	 */
//	public int getLocalSpc();
//
//	/**
//	 * Set the Network Indicator value. This value will be set in Service
//	 * Information Octet (SIO) for outgoing MTP3 MSU
//	 * 
//	 * @param ni
//	 */
//	public void setNi(int ni);
//
//	/**
//	 * Get the Network Indicator value.
//	 * 
//	 * @return
//	 */
//	public int getNi();

	/**
	 * If set, the signaling point code from SCCP called/calling address will be
	 * removed if corresponding routing is based on GT
	 * 
	 * @param removeSpc
	 */
	public void setRemoveSpc(boolean removeSpc);

	/**
	 * Get the remove siganling point code flag
	 * @return
	 */
	public boolean isRemoveSpc();

}
