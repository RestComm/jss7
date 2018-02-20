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

package org.restcomm.protocols.ss7.map.api.errors;

/**
 *
 * AdditionalNetworkResource ::= ENUMERATED { sgsn (0), ggsn (1), gmlc (2), gsmSCF (3), nplr (4), auc (5), ...} -- if unknown
 * value is received in AdditionalNetworkResource -- it shall be ignored.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum AdditionalNetworkResource {
    sgsn(0), ggsn(1), gmlc(2), gsmSCF(3), nplr(4), auc(5);

    private int code;

    private AdditionalNetworkResource(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AdditionalNetworkResource getInstance(int code) {
        switch (code) {
            case 0:
                return sgsn;
            case 1:
                return ggsn;
            case 2:
                return gmlc;
            case 3:
                return gsmSCF;
            case 4:
                return nplr;
            case 5:
                return auc;
            default:
                return null;
        }
    }

}
