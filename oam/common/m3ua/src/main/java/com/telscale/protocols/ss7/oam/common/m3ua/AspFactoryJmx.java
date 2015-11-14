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
package com.telscale.protocols.ss7.oam.common.m3ua;

import java.util.List;

import javolution.util.FastList;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;

/**
 * @author amit bhayani
 *
 */
public class AspFactoryJmx implements AspFactoryJmxMBean {

    private final AspFactory wrappedAspFactory;
    private FastList<Asp> appServerProcs = new FastList<Asp>();

    public AspFactoryJmx(AspFactory wrappedAspFactory) {
        this.wrappedAspFactory = wrappedAspFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.AspFactory#getAspList()
     */
    @Override
    public List<Asp> getAspList() {
        return this.appServerProcs.unmodifiable();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.AspFactory#getAssociation()
     */
    @Override
    public Association getAssociation() {
        return this.wrappedAspFactory.getAssociation();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.AspFactory#getFunctionality()
     */
    @Override
    public Functionality getFunctionality() {
        return this.wrappedAspFactory.getFunctionality();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.AspFactory#getIpspType()
     */
    @Override
    public IPSPType getIpspType() {
        return this.wrappedAspFactory.getIpspType();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.AspFactory#getName()
     */
    @Override
    public String getName() {
        return this.wrappedAspFactory.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.AspFactory#getStatus()
     */
    @Override
    public boolean getStatus() {
        return this.wrappedAspFactory.getStatus();
    }

    protected void addAppServerProcess(AspJmx aspJmx) {
        this.appServerProcs.add(aspJmx);
    }

    protected void removeAppServerProcess(AspJmx aspJmx) {
        this.appServerProcs.remove(aspJmx);
    }

    @Override
    public ASPIdentifier getAspid() {
        return this.wrappedAspFactory.getAspid();
    }

    @Override
    public boolean isHeartBeatEnabled() {
        return this.wrappedAspFactory.isHeartBeatEnabled();
    }

}
