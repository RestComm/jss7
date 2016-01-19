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
package org.mobicents.protocols.ss7.oam.common.m3ua;

import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.State;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;

/**
 * @author amit bhayani
 *
 */
public class AspJmx implements AspJmxMBean {

    private final Asp wrappedAsp;
    private final AsJmx asJmx;
    private AspFactoryJmx aspFactoryJmx;


    public AspJmx(Asp wrappedAsp, AsJmx asJmx, AspFactoryJmx aspFactoryJmx) {
        this.wrappedAsp = wrappedAsp;
        this.asJmx = asJmx;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.Asp#getASPIdentifier()
     */
    @Override
    public ASPIdentifier getASPIdentifier() {
        return this.wrappedAsp.getASPIdentifier();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.Asp#getAs()
     */
    @Override
    public As getAs() {
        return this.asJmx;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.Asp#getName()
     */
    @Override
    public String getName() {
        return this.wrappedAsp.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.Asp#getState()
     */
    @Override
    public State getState() {
        return this.wrappedAsp.getState();
    }

    @Override
    public AspFactory getAspFactory() {
        return this.aspFactoryJmx;
    }

    @Override
    public boolean isConnected() {
        return this.wrappedAsp.isConnected();
    }

    @Override
    public boolean isStarted() {
        return this.wrappedAsp.isStarted();
    }

    @Override
    public boolean isUp() {
        return this.wrappedAsp.isUp();
    }

}
