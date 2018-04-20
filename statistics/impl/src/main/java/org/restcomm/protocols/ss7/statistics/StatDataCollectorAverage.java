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

import org.restcomm.protocols.ss7.statistics.api.StatDataCollectorType;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class StatDataCollectorAverage extends StatDataCollectorDoubleImpl {

  public StatDataCollectorAverage(String name) {
    super(name);
  }

  @Override
  protected void reset() {
    val = Double.MIN_VALUE;
  }

  @Override
  public void updateData(long newVal) {
    if (val < newVal)
      val = newVal;
  }

  @Override
  public StatDataCollectorType getStatDataCollectorType() {
    return StatDataCollectorType.AVERAGE;
  }

  @Override
  public void updateData(String newVal) {
  }

  @Override
  public void updateData(String newVal, double d) {
  }

}
