/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.smstpdu;

import org.restcomm.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.Gsm7NationalLanguageIdentifier;

/**
 *
 * @author sergey vetyutnev
 *
 */
public abstract class Gsm7NationalLanguageIdentifierImpl implements Gsm7NationalLanguageIdentifier {

    private NationalLanguageIdentifier nationalLanguageCode;

    public Gsm7NationalLanguageIdentifierImpl(NationalLanguageIdentifier nationalLanguageCode) {
        this.nationalLanguageCode = nationalLanguageCode;
    }

    public Gsm7NationalLanguageIdentifierImpl(byte[] encodedInformationElementData) {
        if (encodedInformationElementData != null && encodedInformationElementData.length > 0)
            this.nationalLanguageCode = NationalLanguageIdentifier.getInstance(encodedInformationElementData[0] & 0xFF);
    }

    public NationalLanguageIdentifier getNationalLanguageIdentifier() {
        return nationalLanguageCode;
    }

    public byte[] getEncodedInformationElementData() {
        return new byte[] { (byte) nationalLanguageCode.getCode() };
    }
}
