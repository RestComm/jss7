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

package org.mobicents.ss7.hardware.dahdi;

import java.io.IOException;

import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;

/**
 *
 * @author amit bhayani
 *
 */
public class DahdiMtp3UserPart implements Mtp3UserPart {

    @Override
    public void addMtp3UserPartListener(Mtp3UserPartListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMaxUserDataLength(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void removeMtp3UserPartListener(Mtp3UserPartListener arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(Mtp3TransferPrimitive arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp3UserPart#getMtp3TransferPrimitiveFactory()
     */
    @Override
    public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp3UserPart#getRoutingLabelFormat()
     */
    @Override
    public RoutingLabelFormat getRoutingLabelFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.mtp.Mtp3UserPart#setRoutingLabelFormat(org.mobicents.protocols.ss7.mtp.RoutingLabelFormat)
     */
    @Override
    public void setRoutingLabelFormat(RoutingLabelFormat arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isUseLsbForLinksetSelection() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setUseLsbForLinksetSelection(boolean arg0) {
        // TODO Auto-generated method stub

    }

}
