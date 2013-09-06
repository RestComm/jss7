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

package org.mobicents.protocols.ss7.statistics;

import java.util.Date;

/**
*
* @author sergey vetyutnev
*
*/
public abstract class StatDataCollectorAbstractImpl implements StatDataCollector {

    private String campaignName;
    protected Date sessionStartTime = new Date();

    public StatDataCollectorAbstractImpl(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public Date getSessionStartTime() {
        return sessionStartTime;
    }

    public abstract StatResult restartAndGet();

    protected abstract void reset();

    public abstract void updateData(long newVal);

    public abstract void updateData(String newVal);

    public abstract StatDataCollectorType getStatDataCollectorType();

}
