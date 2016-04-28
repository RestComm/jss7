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

package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.IOException;

import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;

/**
*
* @author sergey vetyutnev
*
*/
public class Mtp3UserPartProxy implements Mtp3UserPart {

    @Override
    public void addMtp3UserPartListener(Mtp3UserPartListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeMtp3UserPartListener(Mtp3UserPartListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public RoutingLabelFormat getRoutingLabelFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) {
        // TODO Auto-generated method stub

    }

    @Override
    public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxUserDataLength(int dpc) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isUseLsbForLinksetSelection() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getDeliveryMessageThreadCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) {
        // TODO Auto-generated method stub

    }

    @Override
    public ExecutorCongestionMonitor getExecutorCongestionMonitor() {
        // TODO Auto-generated method stub
        return null;
    }

}
