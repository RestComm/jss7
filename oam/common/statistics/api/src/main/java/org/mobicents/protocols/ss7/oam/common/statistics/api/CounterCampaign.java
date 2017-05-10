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

package org.mobicents.protocols.ss7.oam.common.statistics.api;

import java.io.Serializable;


/**
*
* Counter campaign definition.
*
* @author sergey vetyutnev
*
*/
public interface CounterCampaign extends Serializable {

    /**
     * campaign name
     * @return
     */
    String getName();

    /**
     * counterSet name (string value)
     * Concrete counterSet may not be registered at campaign creation time.
     * Counters collection will be started when counterSet will be registered
     * @return
     */
    String getCounterSetName();

    /**
     * duration in minutes (for normal campaigns) or in seconds (for short
     * campaigns) (5, 10, 15, 20, 30 and 60 are possible values)
     *
     * @return
     */
    int getDuration();

    /**
     * statistics output format
     * @return
     */
    CounterOutputFormat getOutputFormat();

    /**
     * statistics output format
     * @return
     */
    int getOutputFormatInt();

    /**
     * Returns counters definitions
     * @return
     */
    CounterDefSet getCounterSet();

    /**
     * Returns counters values
     * @return
     */
    CounterValueSet getLastCounterValueSet();

    /**
     * Returns if this campaign normal (false) or short (true)
     * @return
     */
    boolean isShortCampaign();

}
