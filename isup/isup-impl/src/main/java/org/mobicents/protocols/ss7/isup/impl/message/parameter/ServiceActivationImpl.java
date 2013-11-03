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
 * Start time:17:25:24 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;

/**
 * Start time:17:25:24 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ServiceActivationImpl extends AbstractISUPParameter implements ServiceActivation {

    // FIXME: this is again simple container
    /**
     * See Q.763 3.49
     */
    public static final byte _FEATURE_CODE_CALL_TRANSFER = 1;

    private byte[] featureCodes;

    public ServiceActivationImpl() {
        super();

    }

    public ServiceActivationImpl(byte[] featureCodes) {
        super();
        this.featureCodes = featureCodes;
    }

    public int decode(byte[] b) throws ParameterException {
        this.featureCodes = b;
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        return this.featureCodes;
    }

    public byte[] getFeatureCodes() {
        return featureCodes;
    }

    public void setFeatureCodes(byte[] featureCodes) {
        this.featureCodes = featureCodes;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
