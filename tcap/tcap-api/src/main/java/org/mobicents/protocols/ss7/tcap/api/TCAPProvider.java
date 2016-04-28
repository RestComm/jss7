/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tcap.api;

import java.io.Serializable;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author baranowb
 *
 */
public interface TCAPProvider extends Serializable {

    /**
     * Create new structured dialog.
     *
     * @param localAddress - desired local address
     * @param remoteAddress - initial remote address, it can change after first TCContinue.
     * @return
     */
    Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException;

    /**
     * Create new structured dialog with predefined local TransactionId.
     * We do not normally invoke this method. Use it only when you need this and only this local TransactionId
     * (for example if we need of recreating a Dialog for which a peer already has in memory)
     * If a Dialog with local TransactionId is already present there will be TCAPException
     *
     * @param localAddress - desired local address
     * @param remoteAddress - initial remote address, it can change after first TCContinue.
     * @param localTrId - predefined local TransactionId
     * @return
     */
    Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long localTrId) throws TCAPException;

    /**
     * Create new unstructured dialog.
     *
     * @param localAddress
     * @param remoteAddress
     * @return
     * @throws TCAPException
     */
    Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException;

    // /////////////
    // Factories //
    // /////////////

    DialogPrimitiveFactory getDialogPrimitiveFactory();

    ComponentPrimitiveFactory getComponentPrimitiveFactory();

    // /////////////
    // Listeners //
    // /////////////

    void addTCListener(TCListener lst);

    void removeTCListener(TCListener lst);

    boolean getPreviewMode();

    /**
     * The collection of netwokIds that are marked as prohibited or congested.
     *
     * @return The collection of pairs: netwokId value - NetworkIdState (prohibited / congested state)
     */
    FastMap<Integer, NetworkIdState> getNetworkIdStateList();

    /**
     * Returns the state of availability / congestion for a networkId subnetwork. Returns null if there is no info (we need to
     * treat it as available)
     *
     * @param networkId
     * @return
     */
    NetworkIdState getNetworkIdState(int networkId);

    /**
     * Setting of a congestion level for a TCAP user "congObject"
     *
     * @param congObject a String with the name of an object
     * @param level a congestion level for this object
     */
    void setUserPartCongestionLevel(String congObject, int level);

    /**
     * Returns a congestion level of a Memory congestion monitor
     *
     * @return
     */
    int getMemoryCongestionLevel();

    /**
     * Returns a congestion level of thread Executors for processing of incoming messages
     *
     * @return
     */
    int getExecutorCongestionLevel();

    /**
     * Returns a max congestion level for UserPartCongestion, MemoryCongestion and ExecutorCongestionLevel
     *
     * @return
     */
    int getCumulativeCongestionLevel();

}
