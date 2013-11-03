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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ExtensionContainer ::= SEQUENCE { privateExtensionList [0]PrivateExtensionList OPTIONAL, pcs-Extensions [1]PCS-Extensions
 * OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 */
public interface MAPExtensionContainer extends Serializable {
    /**
     * Get the PrivateExtension list
     *
     * @return
     */
    ArrayList<MAPPrivateExtension> getPrivateExtensionList();

    /**
     * Set the PrivateExtension list
     *
     * @param privateExtensionList
     */
    void setPrivateExtensionList(ArrayList<MAPPrivateExtension> privateExtensionList);

    /**
     * Get the Pcs-Extensions - ASN.1 encoded byte array
     *
     * @return
     */
    byte[] getPcsExtensions();

    /**
     * Set the Pcs-Extensions - ASN.1 encoded byte array
     *
     * @param pcsExtensions
     */
    void setPcsExtensions(byte[] pcsExtensions);

}
