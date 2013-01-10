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
 * <p>
 * The listener interface for receiving status update on M3UA resources. The
 * class that is interested in processing status update must implements this
 * interface and the object created with that class is registered with
 * {@link M3UAManagement} using <code>addM3UAManagementEventListener</code>
 * method.
 * </p>
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public interface M3UAManagementEventListener {

	/**
	 * Invoked when M3UA stack is started
	 */
	public void onServiceStarted();

	/**
	 * Invoked when M3UA stack is stopped
	 */
	public void onServiceStopped();

	/**
	 * Invoked when all the M3UA resources are successfully removed by calling
	 * {@link M3UAManagement#removeAllResourses()}
	 */
	public void onRemoveAllResources();

	/**
	 * Invoked when new {@link As} is successfully created
	 * 
	 * @param as
	 *            newly created As
	 */
	public void onAsCreated(As as);

	/**
	 * Invoked when existing {@link As} is successfully destroyed
	 * 
	 * @param as
	 *            destroyed As
	 */
	public void onAsDestroyed(As as);

	/**
	 * Invoked when new {@link AspFactory} is successfully created
	 * 
	 * @param aspFactory
	 *            newly created AspFactory
	 */
	public void onAspFactoryCreated(AspFactory aspFactory);

	/**
	 * Invoked when existing {@link AspFactory} is destroyed
	 * 
	 * @param aspFactory
	 *            destroyed {@link AspFactory}
	 */
	public void onAspFactoryDestroyed(AspFactory aspFactory);

	/**
	 * Invoked when {@link Asp} is successfully assigned to {@link As}
	 * 
	 * @param as
	 *            As to which Asp is assigned
	 * @param asp
	 *            Asp which is assigned to As
	 */
	public void onAspAssignedToAs(As as, Asp asp);

	/**
	 * Invoked when {@link Asp} is successfully unassigned from {@link As}
	 * 
	 * @param as
	 *            As from which Asp is unassigned
	 * @param asp
	 *            Asp which is unassigned from As
	 */
	public void onAspUnassignedFromAs(As as, Asp asp);

	/**
	 * Invoked when {@link AspFactory} is successfully started
	 * 
	 * @param aspFactory
	 *            AspFactory that's started
	 */
	public void onAspFactoryStarted(AspFactory aspFactory);

	/**
	 * Invoked when {@link AspFactory} is successfully stopped
	 * 
	 * @param aspFactory
	 *            AspFactory that's stopped
	 */
	public void onAspFactoryStopped(AspFactory aspFactory);

	/**
	 * Invoked when {@link Asp} becomes ACTIVE
	 * 
	 * @param asp
	 *            Asp that changed state to ACTIVE
	 * @param oldState
	 *            the old state of Asp from which it changed to ACTIVE
	 */
	public void onAspActive(Asp asp, State oldState);

	/**
	 * Invoked when {@link Asp} becomes INACTIVE
	 * 
	 * @param asp
	 *            Asp that changed state to INACTIVE
	 * @param oldState
	 *            the old state of Asp from which it changed to INACTIVE
	 */
	public void onAspInactive(Asp asp, State oldState);

	/**
	 * Invoked when {@link Asp} becomes DOWN
	 * 
	 * @param asp
	 *            Asp that changed state to DOWN
	 * @param oldState
	 *            the old state of Asp from which it changed to DOWN
	 */
	public void onAspDown(Asp asp, State oldState);

	/**
	 * Invoked when {@link As} becomes ACTIVE
	 * 
	 * @param as
	 *            As that changed state to ACTIVE
	 * @param oldState
	 *            the old state of As from which it changed to ACTIVE
	 */
	public void onAsActive(As as, State oldState);

	/**
	 * Invoked when {@link As} becomes PENDING
	 * 
	 * @param as
	 *            As that changed state to PENDING
	 * @param oldState
	 *            the old state of As from which it changed to PENDING
	 */
	public void onAsPending(As as, State oldState);

	/**
	 * Invoked when {@link As} becomes INACTIVE
	 * 
	 * @param as
	 *            As that changed state to INACTIVE
	 * @param oldState
	 *            the old state of As from which it changed to INACTIVE
	 */
	public void onAsInactive(As as, State oldState);

	/**
	 * Invoked when {@link As} becomes DOWN
	 * 
	 * @param as
	 *            As that changed state to DOWN
	 * @param oldState
	 *            the old state of As from which it changed to DOWN
	 */
	public void onAsDown(As as, State oldState);

}
