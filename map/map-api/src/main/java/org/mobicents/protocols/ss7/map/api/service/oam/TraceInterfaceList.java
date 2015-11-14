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

package org.mobicents.protocols.ss7.map.api.service.oam;

import java.io.Serializable;

/**
 *
<code>
TraceInterfaceList ::= SEQUENCE {
  msc-s-List      [0] MSC-S-InterfaceList OPTIONAL,
  mgw-List        [1] MGW-InterfaceList OPTIONAL,
  sgsn-List       [2] SGSN-InterfaceList OPTIONAL,
  ggsn-List       [3] GGSN-InterfaceList OPTIONAL,
  rnc-List        [4] RNC-InterfaceList OPTIONAL,
  bmsc-List       [5] BMSC-InterfaceList OPTIONAL,
  ...,
  mme-List        [6] MME-InterfaceList OPTIONAL,
  sgw-List        [7] SGW-InterfaceList OPTIONAL,
  pgw-List        [8] PGW-InterfaceList OPTIONAL,
  eNB-List        [9] ENB-InterfaceList OPTIONAL
}
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TraceInterfaceList extends Serializable {

    MSCSInterfaceList getMscSList();

    MGWInterfaceList getMgwList();

    SGSNInterfaceList getSgsnList();

    GGSNInterfaceList getGgsnList();

    RNCInterfaceList getRncList();

    BMSCInterfaceList getBmscList();

    MMEInterfaceList getMmeList();

    SGWInterfaceList getSgwList();

    PGWInterfaceList getPgwList();

    ENBInterfaceList getEnbList();

}
