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
package org.mobicents.protocols.ss7.sccp;

import java.util.Map;

/**
 *
 * @author Amit Bhayani
 *
 */
public interface SccpResource {

    /**
     * Add remote sub system number.
     *
     * @param remoteSsnid
     * @param remoteSpc
     * @param remoteSsn
     * @param remoteSsnFlag
     * @param markProhibitedWhenSpcResuming
     * @throws Exception
     */
    void addRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
            boolean markProhibitedWhenSpcResuming) throws Exception;

    void modifyRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
            boolean markProhibitedWhenSpcResuming) throws Exception;

    void removeRemoteSsn(int remoteSsnid) throws Exception;

    RemoteSubSystem getRemoteSsn(int remoteSsnid);

    RemoteSubSystem getRemoteSsn(int spc, int remoteSsn);

    Map<Integer, RemoteSubSystem> getRemoteSsns();

    void addRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception;

    void modifyRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception;

    void removeRemoteSpc(int remoteSpcId) throws Exception;

    RemoteSignalingPointCode getRemoteSpc(int remoteSpcId);

    RemoteSignalingPointCode getRemoteSpcByPC(int remotePC);

    Map<Integer, RemoteSignalingPointCode> getRemoteSpcs();

    void addConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception;

    void removeConcernedSpc(int concernedSpcId) throws Exception;

    void modifyConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception;

    ConcernedSignalingPointCode getConcernedSpc(int concernedSpcId);

    ConcernedSignalingPointCode getConcernedSpcByPC(int remotePC);

    Map<Integer, ConcernedSignalingPointCode> getConcernedSpcs();

}
