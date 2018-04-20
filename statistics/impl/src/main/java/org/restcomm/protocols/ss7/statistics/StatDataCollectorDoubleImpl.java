/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.statistics;

import java.util.Date;

import javolution.util.FastMap;

import org.restcomm.protocols.ss7.statistics.api.DoubleValue;
import org.restcomm.protocols.ss7.statistics.api.LongValue;
import org.restcomm.protocols.ss7.statistics.api.StatResult;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public abstract class StatDataCollectorDoubleImpl extends StatDataCollectorAbstractImpl {

  protected double val;

  public StatDataCollectorDoubleImpl(String campaignName) {
    super(campaignName);
  }

  public StatResult restartAndGet() {
    StatResultDouble res = new StatResultDouble(val);
    this.sessionStartTime = new Date();
    this.reset();
    return res;
  }

  public class StatResultDouble implements StatResult {

    private double val;

    public StatResultDouble(double val) {
      this.val = val;
    }

    @Override
    public long getLongValue() {
      long longVal = (long) val;
      return longVal;
    }

    @Override
    public FastMap<String, LongValue> getStringLongValue() {
      return null;
    }

    @Override
    public double getDoubleValue() {
      return val;
    }

    @Override
    public FastMap<String, DoubleValue> getStringDoubleValue() {
      return null;
    }
  }

}
