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

package org.restcomm.protocols.ss7.statistics;

import javolution.util.FastMap;

import org.restcomm.protocols.ss7.statistics.api.DoubleValue;
import org.restcomm.protocols.ss7.statistics.api.LongValue;
import org.restcomm.protocols.ss7.statistics.api.StatDataCollectorType;
import org.restcomm.protocols.ss7.statistics.api.StatResult;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class StringDoubleMap extends StatDataCollectorAbstractImpl {

  private FastMap<String, DoubleValue> data = new FastMap<String, DoubleValue>();

  public StringDoubleMap(String campaignName) {
    super(campaignName);
  }

  public StatResult restartAndGet() {
    synchronized (this) {
      StatResultStringDoubleMap res = new StatResultStringDoubleMap(this.data);
      this.data = new FastMap<String, DoubleValue>();
      this.reset();
      return res;
    }
  }

  protected void reset() {
    synchronized (this) {
      this.data.clear();
    }
  }

  @Override
  public void updateData(String name, double value) {
    synchronized (this) {
      DoubleValue val = data.get(name);
      if (val == null) {
        val = new DoubleValue();
        data.put(name, val);
      }
      val.updateDoubleValue(value);
    }
  }

  @Override
  public void updateData(String name) {
    synchronized (this) {
      DoubleValue val = data.get(name);
      if (val == null) {
        val = new DoubleValue();
        data.put(name, val);
      }
      val.updateDoubleValue(val.getDoubleValue());
    }
  }

  @Override
  public void updateData(long newVal) {
  }

  @Override
  public StatDataCollectorType getStatDataCollectorType() {
    return StatDataCollectorType.StringLongMap;
  }

  public class StatResultStringDoubleMap implements StatResult {

    private FastMap<String, DoubleValue> doubleData;

    private FastMap<String, LongValue> longData;

    public StatResultStringDoubleMap(FastMap<String, DoubleValue> data) {
      this.doubleData = data;
    }

    @Override
    public long getLongValue() {
      return 0;
    }

    @Override
    public double getDoubleValue() {
      return 0.00;
    }

    @Override
    public FastMap<String, DoubleValue> getStringDoubleValue() {
      return doubleData;
    }

    @Override
    public FastMap<String, LongValue> getStringLongValue() {
      return longData;
    }

  }

}
