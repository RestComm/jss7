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
package org.restcomm.protocols.ss7.sccp;

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
