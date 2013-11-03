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

/**
 * Start time:13:39:30 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.GVNSUserGroup;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingParticipatingServiceProvider;
import org.mobicents.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;

/**
 * Start time:13:39:30 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ForwardGVNSImpl extends AbstractISUPParameter implements ForwardGVNS {

    // FIXME: we must add in numbers below max digits check - in case of max octets - only odd digits number is valid
    private OriginatingParticipatingServiceProviderImpl opServiceProvider = null;
    private GVNSUserGroupImpl gvnsUserGroup = null;
    private TerminatingNetworkRoutingNumberImpl tnRoutingNumber = null;

    public ForwardGVNSImpl(OriginatingParticipatingServiceProviderImpl opServiceProvider, GVNSUserGroupImpl gvnsUserGroup,
            TerminatingNetworkRoutingNumberImpl tnRoutingNumber) {
        super();
        this.opServiceProvider = opServiceProvider;
        this.gvnsUserGroup = gvnsUserGroup;
        this.tnRoutingNumber = tnRoutingNumber;
    }

    public ForwardGVNSImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public ForwardGVNSImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        // Add kength ? || b.length != xxx
        if (b == null) {
            throw new ParameterException("byte[] must  not be null");
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        this.opServiceProvider = new OriginatingParticipatingServiceProviderImpl();
        this.gvnsUserGroup = new GVNSUserGroupImpl();
        this.tnRoutingNumber = new TerminatingNetworkRoutingNumberImpl();

        int count = 0;
        count += this.opServiceProvider.decode(bis);
        count += this.gvnsUserGroup.decode(bis);
        count += this.tnRoutingNumber.decode(bis);

        return count;
    }

    public byte[] encode() throws ParameterException {

        if (this.opServiceProvider == null) {
            throw new IllegalArgumentException("OriginatingParticipatingServiceProvider must not be null.");
        }
        if (this.gvnsUserGroup == null) {
            throw new IllegalArgumentException("GVNSUserGruop must not be null.");
        }
        if (this.tnRoutingNumber == null) {
            throw new IllegalArgumentException("TerminatingNetworkRoutingNumber must not be null.");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(this.opServiceProvider.encode());
            bos.write(this.gvnsUserGroup.encode());
            bos.write(this.tnRoutingNumber.encode());
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return bos.toByteArray();
    }

    public OriginatingParticipatingServiceProvider getOpServiceProvider() {
        return opServiceProvider;
    }

    public void setOpServiceProvider(OriginatingParticipatingServiceProvider opServiceProvider) {
        this.opServiceProvider = (OriginatingParticipatingServiceProviderImpl) opServiceProvider;
    }

    public GVNSUserGroup getGvnsUserGroup() {
        return gvnsUserGroup;
    }

    public void setGvnsUserGroup(GVNSUserGroup gvnsUserGroup) {
        this.gvnsUserGroup = (GVNSUserGroupImpl) gvnsUserGroup;
    }

    public TerminatingNetworkRoutingNumber getTnRoutingNumber() {
        return tnRoutingNumber;
    }

    public void setTnRoutingNumber(TerminatingNetworkRoutingNumber tnRoutingNumber) {
        this.tnRoutingNumber = (TerminatingNetworkRoutingNumberImpl) tnRoutingNumber;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
