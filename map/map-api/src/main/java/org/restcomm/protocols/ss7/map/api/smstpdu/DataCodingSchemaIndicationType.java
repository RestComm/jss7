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

package org.restcomm.protocols.ss7.map.api.smstpdu;

/**
 *
 Bit 1 Bit 0 Indication Type: 0 0 Voicemail MessageWaiting 0 1 Fax MessageWaiting 1 0 Electronic Mail MessageWaiting 1 1 Other
 * MessageWaiting* Mobile manufacturers may implement the "Other MessageWaiting" indication as an additional indication without
 * specifying the meaning.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum DataCodingSchemaIndicationType {
    VoicemailMessageWaiting(0), FaxMessageWaiting(1), ElectronicMailMessageWaiting(2), OtherMessageWaiting(3);

    private int code;

    private DataCodingSchemaIndicationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static DataCodingSchemaIndicationType getInstance(int code) {
        switch (code) {
            case 0:
                return VoicemailMessageWaiting;
            case 1:
                return FaxMessageWaiting;
            case 2:
                return ElectronicMailMessageWaiting;
            default:
                return OtherMessageWaiting;
        }
    }
}