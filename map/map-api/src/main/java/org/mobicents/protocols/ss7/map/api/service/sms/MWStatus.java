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

package org.mobicents.protocols.ss7.map.api.service.sms;

import java.io.Serializable;

/**
 *
<code>
MW-Status ::= BIT STRING {
  sc-AddressNotIncluded (0), mnrf-Set (1), mcef-Set (2) , mnrg-Set (3)
} (SIZE (6..16))
-- exception handling:
-- bits 4 to 15 shall be ignored if received and not understood
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MWStatus extends Serializable {

    boolean getScAddressNotIncluded();

    boolean getMnrfSet();

    boolean getMcefSet();

    boolean getMnrgSet();

}
