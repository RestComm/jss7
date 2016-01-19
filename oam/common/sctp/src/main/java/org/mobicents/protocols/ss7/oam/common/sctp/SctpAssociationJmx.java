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
package org.mobicents.protocols.ss7.oam.common.sctp;

import io.netty.buffer.ByteBufAllocator;

import javax.naming.OperationNotSupportedException;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.PayloadData;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SctpAssociationJmx implements SctpAssociationJmxMBean {

    private final Association wrappedAssociation;
    private final SctpManagementJmx sctpManagement;

    SctpAssociationJmx(SctpManagementJmx sctpManagement, Association association) {
        this.sctpManagement = sctpManagement;
        this.wrappedAssociation = association;
    }

    @Override
    public void acceptAnonymousAssociation(AssociationListener arg0) throws Exception {
        throw new OperationNotSupportedException();
    }

    @Override
    public AssociationListener getAssociationListener() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssociationType getAssociationType() {
        return this.wrappedAssociation.getAssociationType();
    }

    @Override
    public String[] getExtraHostAddresses() {
        return this.wrappedAssociation.getExtraHostAddresses();
    }

    @Override
    public String getHostAddress() {
        return this.wrappedAssociation.getHostAddress();
    }

    @Override
    public int getHostPort() {
        return this.wrappedAssociation.getHostPort();
    }

    @Override
    public IpChannelType getIpChannelType() {
        return this.wrappedAssociation.getIpChannelType();
    }

    @Override
    public String getName() {
        return this.wrappedAssociation.getName();
    }

    @Override
    public String getPeerAddress() {
        return this.wrappedAssociation.getPeerAddress();
    }

    @Override
    public int getPeerPort() {
        return this.wrappedAssociation.getPeerPort();
    }

    @Override
    public String getServerName() {
        return this.wrappedAssociation.getServerName();
    }

    @Override
    public boolean isConnected() {
        return this.wrappedAssociation.isConnected();
    }

    @Override
    public boolean isStarted() {
        return this.wrappedAssociation.isStarted();
    }

    @Override
    public boolean isUp() {
        return this.wrappedAssociation.isUp();
    }

    @Override
    public int getCongestionLevel() {
        return this.wrappedAssociation.getCongestionLevel();
    }

    @Override
    public void rejectAnonymousAssociation() {
    }

    @Override
    public void send(PayloadData arg0) throws Exception {
        throw new OperationNotSupportedException();
    }

    @Override
    public ByteBufAllocator getByteBufAllocator() throws Exception {
        return null;
    }

    @Override
    public void setAssociationListener(AssociationListener arg0) {
    }

    @Override
    public void stopAnonymousAssociation() throws Exception {
    }

}
