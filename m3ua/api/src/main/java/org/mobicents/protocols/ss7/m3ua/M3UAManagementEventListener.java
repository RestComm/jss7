/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.mobicents.protocols.ss7.m3ua;

/**
 * <p>
 * The listener interface for receiving status update on M3UA resources. The class that is interested in processing status
 * update must implements this interface and the object created with that class is registered with {@link M3UAManagement} using
 * <code>addM3UAManagementEventListener</code> method.
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
    void onServiceStarted();

    /**
     * Invoked when M3UA stack is stopped
     */
    void onServiceStopped();

    /**
     * Invoked when all the M3UA resources are successfully removed by calling {@link M3UAManagement#removeAllResourses()}
     */
    void onRemoveAllResources();

    /**
     * Invoked when new {@link As} is successfully created
     *
     * @param as newly created As
     */
    void onAsCreated(As as);

    /**
     * Invoked when existing {@link As} is successfully destroyed
     *
     * @param as destroyed As
     */
    void onAsDestroyed(As as);

    /**
     * Invoked when new {@link AspFactory} is successfully created
     *
     * @param aspFactory newly created AspFactory
     */
    void onAspFactoryCreated(AspFactory aspFactory);

    /**
     * Invoked when existing {@link AspFactory} is destroyed
     *
     * @param aspFactory destroyed {@link AspFactory}
     */
    void onAspFactoryDestroyed(AspFactory aspFactory);

    /**
     * Invoked when {@link Asp} is successfully assigned to {@link As}
     *
     * @param as As to which Asp is assigned
     * @param asp Asp which is assigned to As
     */
    void onAspAssignedToAs(As as, Asp asp);

    /**
     * Invoked when {@link Asp} is successfully unassigned from {@link As}
     *
     * @param as As from which Asp is unassigned
     * @param asp Asp which is unassigned from As
     */
    void onAspUnassignedFromAs(As as, Asp asp);

    /**
     * Invoked when {@link AspFactory} is successfully started
     *
     * @param aspFactory AspFactory that's started
     */
    void onAspFactoryStarted(AspFactory aspFactory);

    /**
     * Invoked when {@link AspFactory} is successfully stopped
     *
     * @param aspFactory AspFactory that's stopped
     */
    void onAspFactoryStopped(AspFactory aspFactory);

    /**
     * Invoked when {@link Asp} becomes ACTIVE
     *
     * @param asp Asp that changed state to ACTIVE
     * @param oldState the old state of Asp from which it changed to ACTIVE
     */
    void onAspActive(Asp asp, State oldState);

    /**
     * Invoked when {@link Asp} becomes INACTIVE
     *
     * @param asp Asp that changed state to INACTIVE
     * @param oldState the old state of Asp from which it changed to INACTIVE
     */
    void onAspInactive(Asp asp, State oldState);

    /**
     * Invoked when {@link Asp} becomes DOWN
     *
     * @param asp Asp that changed state to DOWN
     * @param oldState the old state of Asp from which it changed to DOWN
     */
    void onAspDown(Asp asp, State oldState);

    /**
     * Invoked when {@link As} becomes ACTIVE
     *
     * @param as As that changed state to ACTIVE
     * @param oldState the old state of As from which it changed to ACTIVE
     */
    void onAsActive(As as, State oldState);

    /**
     * Invoked when {@link As} becomes PENDING
     *
     * @param as As that changed state to PENDING
     * @param oldState the old state of As from which it changed to PENDING
     */
    void onAsPending(As as, State oldState);

    /**
     * Invoked when {@link As} becomes INACTIVE
     *
     * @param as As that changed state to INACTIVE
     * @param oldState the old state of As from which it changed to INACTIVE
     */
    void onAsInactive(As as, State oldState);

    /**
     * Invoked when {@link As} becomes DOWN
     *
     * @param as As that changed state to DOWN
     * @param oldState the old state of As from which it changed to DOWN
     */
    void onAsDown(As as, State oldState);

}
