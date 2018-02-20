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

package org.restcomm.ss7.congestion;

/**
*
* @author sergey vetyutnev
*
*/
public interface MemoryCongestionMonitor {

    String getSource();

    int getAlarmLevel();

    double getMemoryThreshold_1();

    double getMemoryThreshold_2();

    double getMemoryThreshold_3();

    void setMemoryThreshold_1(double value) throws Exception;

    void setMemoryThreshold_2(double value) throws Exception;

    void setMemoryThreshold_3(double value) throws Exception;

    double getBackToNormalMemoryThreshold_1();

    double getBackToNormalMemoryThreshold_2();

    double getBackToNormalMemoryThreshold_3();

    void setBackToNormalMemoryThreshold_1(double value) throws Exception;

    void setBackToNormalMemoryThreshold_2(double value) throws Exception;

    void setBackToNormalMemoryThreshold_3(double value) throws Exception;

}
