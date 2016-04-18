/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.isup.message.parameter;

import java.io.UnsupportedEncodingException;

/**
 * Start time:12:37:11 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface GenericDigits extends ISUPParameter {
    int _PARAMETER_CODE = 0xC1;

    /**
     * See Q.763 3.24 Encoding scheme : BCD even: (even number of digits)
     */
    int _ENCODING_SCHEME_BCD_EVEN = 0;

    /**
     * See Q.763 3.24 Encoding scheme : BCD odd: (odd number of digits)
     */
    int _ENCODING_SCHEME_BCD_ODD = 1;

    /**
     * See Q.763 3.24 Encoding scheme : IA5 character
     */
    int _ENCODING_SCHEME_IA5 = 2;

    /**
     * See Q.763 3.24 Encoding scheme : binary coded
     */
    int _ENCODING_SCHEME_BINARY = 3;

    /**
     * See Q.763 3.24 Type of digits : reserved for account code
     */
    int _TOD_ACCOUNT_CODE = 0;

    /**
     * See Q.763 3.24 Type of digits : reserved for authorisation code
     */
    int _TOD_AUTHORIZATION_CODE = 1;

    /**
     * See Q.763 3.24 Type of digits : reserved for private networking travelling class mark
     */
    int _TOD_PNTCM = 2;

    /**
     * See Q.763 3.24 Type of digits : reserved for business communication group identity
     */
    int _TOD_BGCI = 3;

    int getEncodingScheme();

    void setEncodingScheme(int encodingScheme);

    int getTypeOfDigits();

    void setTypeOfDigits(int typeOfDigits);

    byte[] getEncodedDigits();

    void setEncodedDigits(byte[] digits);

    String getDecodedDigits() throws UnsupportedEncodingException;

    void setDecodedDigits(int encodingScheme, String digits) throws UnsupportedEncodingException;
}
