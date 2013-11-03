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
     * Get the remove signaling point code flag
     *
     * @return
     */
    boolean isRemoveSpc();

    /**
     * Stack only listens for incoming messages and does not send anything out of stack. Messages are silently dropped.
     *
     * @param previewMode
     */
    void setPreviewMode(boolean previewMode);

    /**
     * Returns if previeMode is enabled
     *
     * @return
     */
    boolean isPreviewMode();

    /**
     * Get reference to {@link SccpResource}
     *
     * @return
     */
    SccpResource getSccpResource();

    /**
     * Returns min (starting) SST sending interval (in milliseconds) set
     *
     * @return
     */
    int getSstTimerDuration_Min();

    /**
     * Set's min (starting) SST sending interval (in milliseconds). Value can be from 5000 to 10000.
     * If value passed is less than 5000, it sets to 5000 and if value passed is greater than 10000, its set to 10000
     *
     * @param sstTimerDuration_Min
     */
    void setSstTimerDuration_Min(int sstTimerDuration_Min);

    /**
     * Returns Max (after increasing) SST sending interval.
     *
     * @return
     */
    int getSstTimerDuration_Max();

    /**
     * Sets Max (after increasing) SST sending interval (in 600000 - 1200000 milliseconds). Value can be from 600000 to 1200000
     * If value passed is less than 600000, it sets to 600000 and if value passed is greater than 1200000, it sets to 1200000
     *
     * @param sstTimerDuration_Max
     */
    void setSstTimerDuration_Max(int sstTimerDuration_Max);

    /**
     * Returns Multiplicator of SST sending interval
     *
     * @return
     */
    double getSstTimerDuration_IncreaseFactor();

    /**
     * Set multiplicator of SST sending interval (next interval will be greater then the current by sstTimerDuration_IncreaseFactor).
     * Range is 1 to 4. If passed value is less than 1, it sets to 1 and if passed value is greater than 4, it sets to 4
     *
     * @param sstTimerDuration_IncreaseFactor
     */
    void setSstTimerDuration_IncreaseFactor(double sstTimerDuration_IncreaseFactor);

    /**
     * Returns the segmentation length
     *
     * @return
     */
    int getZMarginXudtMessage();

    /**
     * Sets segmentation length. If the XUDT message data length greater this value,
     * segmentation is processed. Otherwise no segmentation.
     * Range is 160 to 255. If passed value is less than 160, it sets to 160 and
     * if passed value is greater than 255, it sets to 255.
     *
     * @param zMarginXudtMessage
     */
    void setZMarginXudtMessage(int zMarginXudtMessage);

    /**
     * Reurns Max available SCCP message data for all message types
     *
     * @return
     */
    int getMaxDataMessage();

    /**
     * Sets Max available SCCP message data for all message types. Range is 2560 to 3952.
     * If passed value is less than 2560, it sets to 2560 and if passed value is greater
     * than 3952, it sets to 3952.
     *
     * @param maxDataMessage
     */
    void setMaxDataMessage(int maxDataMessage);

    /**
     * Returns SCCP segmented message reassembling timeout (in milliseconds).
     *
     * @return
     */
    int getReassemblyTimerDelay();

    /**
     * Sets SCCP segmented message reassembling timeout (in milliseconds).
     * Range is 10000 to 20000. If passed value is less than 10000, it sets to 10000 and
     * if passed value is greater than 20000, it sets to 20000
     *
     * @param reassemblyTimerDelay
     */
    void setReassemblyTimerDelay(int reassemblyTimerDelay);

    /**
     * Set the underlying MTP3 layer
     *
     * @param mtp3UserPartsTemp
     */
    void setMtp3UserParts(Map<Integer, Mtp3UserPart> mtp3UserPartsTemp);

    /**
     * Retrieve the underlying MTP3 layer
     *
     * @return
     */
    Map<Integer, Mtp3UserPart> getMtp3UserParts();

    /**
     * Retrieve specific underlying MTP3 layer
     *
     * @param id
     * @return
     */
    Mtp3UserPart getMtp3UserPart(int id);

    /**
     * Returns handle to {@link Router}
     *
     * @return
     */
    Router getRouter();

}
