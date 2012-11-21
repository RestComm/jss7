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
	public void addRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
			boolean markProhibitedWhenSpcResuming) throws Exception;

	public void modifyRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
			boolean markProhibitedWhenSpcResuming) throws Exception;

	public void removeRemoteSsn(int remoteSsnid) throws Exception;

	public RemoteSubSystem getRemoteSsn(int remoteSsnid);

	public RemoteSubSystem getRemoteSsn(int spc, int remoteSsn);

	public Map<Integer, RemoteSubSystem> getRemoteSsns();

	public void addRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception;

	public void modifyRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception;

	public void removeRemoteSpc(int remoteSpcId) throws Exception;

	public RemoteSignalingPointCode getRemoteSpc(int remoteSpcId);

	public RemoteSignalingPointCode getRemoteSpcByPC(int remotePC);

	public Map<Integer, RemoteSignalingPointCode> getRemoteSpcs();

	public void addConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception;

	public void removeConcernedSpc(int concernedSpcId) throws Exception;
	
	public void modifyConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception;

	public ConcernedSignalingPointCode getConcernedSpc(int concernedSpcId);

	public ConcernedSignalingPointCode getConcernedSpcByPC(int remotePC);

	public Map<Integer, ConcernedSignalingPointCode> getConcernedSpcs();

}
