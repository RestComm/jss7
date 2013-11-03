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
 * Start time:17:24:08 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.Invoke;
import org.mobicents.protocols.ss7.isup.message.parameter.Reject;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperation;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnError;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnResult;

/**
 * Start time:17:24:08 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RemoteOperationsImpl extends AbstractISUPParameter implements RemoteOperations {

    private List<RemoteOperation> remoteOperations = new ArrayList<RemoteOperation>();
    private byte protocol = RemoteOperations.PROTOCOL_REMOTE_OPERATIONS;

    // FIXME: XXX
    // Q.763 3.48, requires a lot of hacks
    public RemoteOperationsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public RemoteOperationsImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        if (b.length < 1) {
            throw new ParameterException();
        }
        this.protocol = (byte) (b[0] & 0x1F);
        if ((b[0] & 0x80) > 0) {
            if (b.length > 1) {
                throw new ParameterException();
            }
            return 1;
        }

        try {
            AsnInputStream asnInputeStream = new AsnInputStream(b);
            // skip first since its protocol + ext. bit
            asnInputeStream.skip(1);
            while (asnInputeStream.available() > 0) {
                final int tag = asnInputeStream.readTag();
                AbstractRemoteOperation aro = null;
                switch (tag) {
                    case Invoke._TAG:
                        aro = new InvokeImpl();
                        break;
                    case ReturnResult._TAG:
                        aro = new ReturnResultImpl();
                        break;
                    case ReturnError._TAG:
                        aro = new ReturnErrorImpl();
                        break;
                    case Reject._TAG:
                        aro = new RejectImpl();
                        break;
                    default:
                        throw new ParameterException("Unknown tag: " + tag);
                }
                aro.decode(asnInputeStream);
                this.remoteOperations.add(aro);
                aro = null;
            }
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        if (remoteOperations.size() == 0) {
            return new byte[]{(byte) (0x80 | this.protocol)};
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //ext. bit set to zero since more will come
            baos.write(protocol);
            AsnOutputStream aos = new AsnOutputStream();
            for(RemoteOperation ro:this.remoteOperations){
                //TODO: should this do more?
                AbstractRemoteOperation aro = (AbstractRemoteOperation) ro;
                aro.encode(aos);
            }
            try {
                baos.write(aos.toByteArray());
            } catch (IOException e) {
                throw new ParameterException(e);
            }
            return baos.toByteArray();
        }
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public void setProtocol(byte protocol) {
        this.protocol = this.protocol;
    }

    @Override
    public byte getProtocol() {
        return this.protocol;
    }

    @Override
    public void setOperations(RemoteOperation... operations) {
        this.remoteOperations.clear();
        for (RemoteOperation ro : operations) {
            if (ro != null) {
                this.remoteOperations.add(ro);
            }
        }
    }

    @Override
    public RemoteOperation[] getOperations() {
        return this.remoteOperations.toArray(new RemoteOperation[this.remoteOperations.size()]);
    }

}
