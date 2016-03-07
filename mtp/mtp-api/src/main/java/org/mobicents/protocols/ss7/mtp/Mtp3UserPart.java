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

package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;

import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface Mtp3UserPart {

    /**
     * Add {@link Mtp3UserPartListener}
     *
     * @param listener
     */
    void addMtp3UserPartListener(Mtp3UserPartListener listener);

    /**
     * Remove {@link Mtp3UserPartListener}
     *
     * @param listener
     */
    void removeMtp3UserPartListener(Mtp3UserPartListener listener);

    /**
     * Get the Mtp3TransferPrimitiveFactory
     *
     * @return
     */
    Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory();

    /**
     * Return the maximum data field length of the MTP-TRANSFER message to the DPC
     *
     * @param dpc
     * @return
     */
    int getMaxUserDataLength(int dpc);

    /**
     * If message delivering failed: MTP-PAUSE or MTR-STATUS indication will be sent
     *
     * @param msg
     *
     */
    void sendMessage(Mtp3TransferPrimitive msg) throws IOException;


    /**
     * return PointCodeFormat
     *
     * @return
     */
    RoutingLabelFormat getRoutingLabelFormat();

    /**
     * Set PointCodeFormat
     *
     * @param length
     */
    void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) throws Exception;

    /**
     * Returns true if lowest bit of SLS is used for loadbalancing between Linkset else returns false
     *
     * @return
     */
    boolean isUseLsbForLinksetSelection();

    /**
     * If set to true, lowest bit of SLS is used for loadbalancing between Linkset else highest bit of SLS is used.
     *
     * @param useLsbForLinksetSelection
     */
    void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) throws Exception;

    /**
     * @return
     */
    int getDeliveryMessageThreadCount();

    /**
     * @param deliveryMessageThreadCount
     */
    void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) throws Exception;

    /**
     * @return ExecutorCongestionMonitor that is responsible for measuring of congestion of the thread Executor that processes
     *         incoming messages (may be null if mtp3 is not started)
     */
    ExecutorCongestionMonitor getExecutorCongestionMonitor();

}
