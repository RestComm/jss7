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
package org.mobicents.protocols.ss7.m3ua;

/**
 * @author amit bhayani
 *
 */
public interface M3UAManagementEventListener {
	
	public void onAsCreated(As as);
	
	public void onAsDestroyed(As as);
	
	public void onAspFactoryCreated(AspFactory aspFactory);
	
	public void onAspFactoryDestroyed(AspFactory aspFactory);
	
	public void onAspAssignedToAs(As as, Asp asp);
	
	public void onAspUnassignedFromAs(As as, Asp asp);

	public void onRemoveAllResources();
	
	public void onAspFactoryStarted(AspFactory aspFactory);
	
	public void onAspFactoryStopped(AspFactory aspFactory);
	
	public void onAspActive(Asp asp);
	
	public void onAspInactive(Asp asp);
	
	public void onAspDown(Asp asp);
	
	public void onAsActive(As as);
	
	public void onAsPending(As as);
	
	public void onAsInactive(As as);
	
	public void onAsDown(As as);
}
