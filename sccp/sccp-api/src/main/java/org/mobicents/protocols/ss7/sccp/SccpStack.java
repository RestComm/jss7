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
    int UDT_ONLY = 1;
    int XUDT_ONLY = 2;

    /**
     * Starts SCCP stack.
     *
     * @throws java.lang.IllegalStateException
     */
    void start() throws IllegalStateException;

    /**
     * Terminates SCCP stack.
     *
     * @throws java.lang.IllegalStateException
     * @throws org.mobicents.protocols.StartFailedException
     */
    void stop();

    /**
     * Returns the name of this stack
     *
     * @return
     */
    String getName();

    /**
     * Exposes SCCP provider object to SCCP user.
     *
     * @return SCCP provider object.
     */
    SccpProvider getSccpProvider();

    /**
     * Set the persist directory to store the xml files
     *
     * @return
     */
    String getPersistDir();

    /**
     * Get the persist directory from which to read the xml files
     *
     * @param persistDir
     */
    void setPersistDir(String persistDir);

    /**
     * If set, the signaling point code from SCCP called/calling address will be removed if corresponding routing is based on GT
     *
     * @param removeSpc
     */
    void setRemoveSpc(boolean removeSpc);

    /**
     * Get the remove siganling point code flag
     *
     * @return
     */
    boolean isRemoveSpc();

    SccpResource getSccpResource();

    int getSstTimerDuration_Min();

    int getSstTimerDuration_Max();

    double getSstTimerDuration_IncreaseFactor();

    int getZMarginXudtMessage();

    int getMaxDataMessage();

    int getReassemblyTimerDelay();

    Map<Integer, Mtp3UserPart> getMtp3UserParts();

    Mtp3UserPart getMtp3UserPart(int id);

    Router getRouter();

}
