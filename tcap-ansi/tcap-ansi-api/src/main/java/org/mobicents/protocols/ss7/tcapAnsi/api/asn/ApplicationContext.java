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

package org.mobicents.protocols.ss7.tcapAnsi.api.asn;

/**
 * @author baranowb
 * @author sergey vetyutnev

ApplicationContext  CHOICE {
        integerApplicationId    IntegerApplicationContext,
        objectApplicationId ObjectIDApplicationContext
 } OPTIONAL,

IntegerApplicationContext ::= [PRIVATE 27] IMPLICIT INTEGER

ObjectIDApplicationContext ::= [PRIVATE 28] IMPLICIT OBJECT IDENTIFIER

 *
 */
public interface ApplicationContext extends Encodable {

    // its type of OID

    int _TAG_INTEGER = 27;
    int _TAG_OBJECT_ID = 28;

    boolean isInteger();

    boolean isObjectID();

    long[] getOid();

    void setOid(long[] val);

    long getInteger();

    void setInteger(long val);

}
