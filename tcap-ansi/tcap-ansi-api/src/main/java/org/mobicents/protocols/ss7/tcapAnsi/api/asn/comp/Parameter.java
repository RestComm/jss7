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

/**
 *
 */

package org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Encodable;

/**
 *
 * @author baranowb
 *
 */
public interface Parameter extends Encodable {
    int _TAG_SEQUENCE = 16;
    int _TAG_SET = 18;

    byte[] getData();

    /**
     * Sets content as raw byte[]. invocation parameter must be encoded by user.
     *
     * @param b
     */
    void setData(byte[] b);

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

    Parameter[] getParameters();

    void setParameters(Parameter[] paramss);

}
