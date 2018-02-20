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

package org.restcomm.protocols.ss7.oam.common.statistics.api;


/**
* This interface must provide the class who want to export statistic data - for example TCAPStackStat
*
* @author sergey vetyutnev
*
*/
public interface CounterMediator {

    /**
     * Must return the unique name inside the system
     * @return
     */
    String getCounterMediatorName();

    /**
     * Must return names of one or several CounterDefSet that this class provides
     * @return
     */
    String[] getCounterDefSetList();

    /**
     * Must return CounterDefSet with defined name or null if absent
     * @param counterDefSetName
     * @return
     */
    CounterDefSet getCounterDefSet(String counterDefSetName);

    /**
     * This method will provide "current value" - the value for current moment
     * for both source counter types (current values and cumulative values).
     * Aggregation task is the task for a class that implements
     * CounterCampaignProvider interface (and provides Campaigns set).
     * CounterCampaignProvider will invoke this method for each campaign at the
     * time of campaign start / end time
     *
     * @return
     */
    SourceValueSet getSourceValueSet(String counterDefSetName, String campaignName, int durationInSeconds);

}
