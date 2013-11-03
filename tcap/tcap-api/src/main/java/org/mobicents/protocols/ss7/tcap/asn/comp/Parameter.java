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
 *
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * This class embeds Parameter/Parameters. It can be either primtiive or constructed. It supports two types of encoding -
 * content as byte[] - either simple or complex - depends on how TC-User sets parameters, or content as Parameter[] - in this
 * case encoding is done as constructed element. It supports following cases:
 * <ul>
 * <li><br>
 * byte[] as primitive -
 *
 * <pre>
 * Parameter p = ....
 *  p.setTag(0x01);
 *  p.setTagClass({@link Tag.CLASS_APPLICATION});
 *  p.setData(new byte[]{ENCODED});
 *  p.setPrimitive(true);
 * </pre>
 *
 * </li>
 * <li><br>
 * byte[] as constructed
 *
 * <pre>
 * Parameter p = ....
 *  p.setTag(0x01);
 *  p.setTagClass({@link Tag.CLASS_APPLICATION});
 *  p.setData(new byte[]{ENCODED});
 *  p.setPrimitive(false);
 * </pre>
 *
 * </li>
 * <li><br>
 * As constructed, with Parameter[] -
 *
 * <pre>
 * Parameter[] params = ....;
 *  Parameter p = ....
 *  p.setTag(0x01);
 *  p.setTagClass({@link Tag.CLASS_APPLICATION});
 *  //This sets primitive explicitly to false, it must be constructed type.
 *  p.setParameters(params);
 * </pre>
 *
 * </li>
 * </ul>
 * Note that on read only byte[] is filled! In case TC-USER makes call to {@link #getParameters()} method - it triggers parsing
 * to array, so it is perfectly legal to obtain byte[], rather than Parameter[].
 *
 * @author baranowb
 *
 */
public interface Parameter extends Encodable {
    // public static final int _TAG = 0x02;
    // public static final boolean _TAG_PRIMITIVE = true;

    byte[] getData();

    /**
     * Sets content as raw byte[]. invocation parameter must be encoded by user.
     *
     * @param b
     */
    void setData(byte[] b);

    /**
     * Return the length value that was encoded in ASN.1 stream Usually this value ==getData().length and we can use
     * "getData().length" as the length value Checking this field makes sense when special cases when
     * "encoded length value"!=getData().length
     *
     * @return
     */
    int getEncodingLength();

    /**
     * Set a length that will be encoded when ASN.1 parameter encoding Usually this length is equal getData().length and we need
     * not set this You can use this only when "encoded length value"!=getData().length If setEncodingLength() has not been
     * invoked the value for length encoding will be ==getData().length
     *
     * @param val
     */
    void setEncodingLength(int val);

    /**
     * Determine if this parameter is of primitive type - not constructed.
     *
     * @return
     */
    boolean isPrimitive();

    void setPrimitive(boolean b);

    /**
     * @return the tag
     */
    int getTag();

    /**
     * @param tag the tag to set
     */
    void setTag(int tag);

    /**
     * @return the tagClass
     */
    int getTagClass();

    /**
     * @param tagClass the tagClass to set
     */
    void setTagClass(int tagClass);

    /**
     * Return decoded raw byte[] data.
     *
     * @return
     */
    Parameter[] getParameters();

    /**
     * Sets Parameter[]. Automaticly marks this object as constructed - {@link #isPrimitive()} will return false.
     *
     * @param paramss
     */
    void setParameters(Parameter[] paramss);

    void setSingleParameterInAsn();

}
