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

package org.mobicents.protocols.ss7.isup;

import java.util.EventObject;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Event indicating timeout of some sort, ie. T7
 * 
 * @author baranowb
 * 
 */
public class ISUPTimeoutEvent extends EventObject {

	protected final ISUPMessage message;
	protected final int timerId;

	/**
	 * @param message
	 * @param circuit
	 */
	public ISUPTimeoutEvent(Object source, ISUPMessage message, int timerId) {
		super(source);
		this.message = message;
		this.timerId = timerId;
		new Exception();
	}

	public ISUPMessage getMessage() {
		return message;
	}

	public int getTimerId() {
		return timerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (message != null) {
			result = prime * result + message.getMessageType().getCode();
			result = prime * result + message.getCircuitIdentificationCode().getCIC();
		}

		result = prime * result + timerId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ISUPTimeoutEvent other = (ISUPTimeoutEvent) obj;
		if (this.message != null && other.message == null) {
			return false;
		}

		if (this.message == null && other.message != null) {
			return false;
		}

		if (this.message.getCircuitIdentificationCode().getCIC() != other.message.getCircuitIdentificationCode().getCIC())
			return false;

		if (this.message.getMessageType().getCode() != other.message.getMessageType().getCode())
			return false;
		if (timerId != other.timerId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ISUPTimeoutEvent [message=" + message + ", timerId=" + timerId + "]";
	}

	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T1_DEFAULT}.</li>
	 * <li>Time span: 4-15 sec</li>
	 * <li>Started: When release message is sent</li>
	 * <li>Terminated: At the receipt of RLC</li>
	 * <li>On expire: Re-transmirt REL and restart T1</li>
	 * </ul>
	 */
	public static final int T1 = 1;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T1_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T2_DEFAULT}.</li>
	 * <li>Time span: 3 min</li>
	 * <li>Started: When controlling exchange receives suspend (user) message.</li>
	 * <li>Terminated: At receipt of resume (user) message at controlling
	 * exchange.</li>
	 * <li>On expire: Initiate release procedure.</li>
	 * </ul>
	 */
	public static final int T2 = 2;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T2_DEFAULT = 3 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T3_DEFAULT}.</li>
	 * <li>Time span: 2 min</li>
	 * <li>Started: At receipt of overload message.</li>
	 * <li>Terminated: Only by expire.</li>
	 * <li>On expire: Initiate release procedure.</li>
	 * </ul>
	 */
	public static final int T3 = 3;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T3_DEFAULT = 2 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T4_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: At receipt of MTP-STATUS primitive with the cause
	 * "inaccessible remote user".</li>
	 * <li>Terminated: On expire or on receipt of UPA message( or any other).</li>
	 * <li>On expire: Send User Part Test and start T4</li>
	 * </ul>
	 */
	public static final int T4 = 4;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T4_DEFAULT = 6 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T5_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial REL message is sent.</li>
	 * <li>Terminated: At receipt of RLC.</li>
	 * <li>On expire: Send Reset Circuit message, alert personel, stopT1,start
	 * T17. Run in loop...</li>
	 * </ul>
	 */
	public static final int T5 = 5;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T5_DEFAULT = 6 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T6_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T6 = 6;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T6_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T7_DEFAULT}.</li>
	 * <li>Time span: 20-30 sec</li>
	 * <li>Started: Started after each address message is sent IAM/SAM</li>
	 * <li>Terminated: On receipt of ACM/CON</li>
	 * <li>On expire: Send RLC message.</li>
	 * </ul>
	 */
	public static final int T7 = 7;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T7_DEFAULT = 20 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T8_DEFAULT}.</li>
	 * <li>Time span: 10-15 sec</li>
	 * <li>Started: When exchange receives IAM which requires continuity check
	 * on its circuit or indicates that continuity has been performed on a
	 * previous circuit.</li>
	 * <li>Terminated: At receipt of continuity message(CON).</li>
	 * <li>On expire:</li>
	 * </ul>
	 */
	public static final int T8 = 8;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T8_DEFAULT = 10 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T9_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T9 = 9;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T9_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T10_DEFAULT}.</li>
	 * <li>Time span: 4-6 sec</li>
	 * <li>Started: When last digit is received in interworking situations.</li>
	 * <li>Terminated: At receipt of fresh information.</li>
	 * <li>On expire: Send ACM.</li>
	 * </ul>
	 */
	public static final int T10 = 10;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T10_DEFAULT = 4 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T11_DEFAULT}.</li>
	 * <li>Time span: 15-20 sec</li>
	 * <li>Started: When latest address message is received in interworking
	 * situations.</li>
	 * <li>Terminated: When ACM is sent.</li>
	 * <li>On expire: Send ACM.</li>
	 * </ul>
	 */
	public static final int T11 = 11;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T11_DEFAULT = 20 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T12_DEFAULT}.</li>
	 * <li>Time span: 15-60 sec</li>
	 * <li>Started: When BLO is sent.</li>
	 * <li>Terminated: At receipt of BLA.</li>
	 * <li>On expire: Retransmit BLO and start T12.</li>
	 * </ul>
	 */
	public static final int T12 = 12;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T12_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T13_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial BLO is sent.</li>
	 * <li>Terminated: At receipt of BLA.</li>
	 * <li>On expire: Send BLO, alert personel, stop T12, start T13. Run in
	 * loop.</li>
	 * </ul>
	 */
	public static final int T13 = 13;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T13_DEFAULT = 5 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T14_DEFAULT}.</li>
	 * <li>Time span: 15-60 sec</li>
	 * <li>Started: When UBL is sent.</li>
	 * <li>Terminated: On receipt of UBA.</li>
	 * <li>On expire: Send UBL and restart T14</li>
	 * </ul>
	 */
	public static final int T14 = 14;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T14_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T15_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial UBL is sent.</li>
	 * <li>Terminated: On receipt of UBA</li>
	 * <li>On expire: Retransmit UBL, stop T14, start T15 and run in loop.</li>
	 * </ul>
	 */
	public static final int T15 = 15;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T15_DEFAULT = 5 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T16_DEFAULT}.</li>
	 * <li>Time span: 15-60 sec</li>
	 * <li>Started: When Reset Circuit is sent not due to T5.</li>
	 * <li>Terminated: At the receipt of RLC.</li>
	 * <li>On expire: Retransmit Reset Circuit and start T16.</li>
	 * </ul>
	 */
	public static final int T16 = 16;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T16_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T17_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial Reset Circuit is sent.</li>
	 * <li>Terminated: At receipt of Reset Circuit ack.</li>
	 * <li>On expire:</li>
	 * </ul>
	 */
	public static final int T17 = 17;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T17_DEFAULT = 5 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T18_DEFAULT}.</li>
	 * <li>Time span: 15-60 sec</li>
	 * <li>Started: When GBL is sent.</li>
	 * <li>Terminated: At receipt of GBA.</li>
	 * <li>On expire: Retransmit GBL and start T18.</li>
	 * </ul>
	 */
	public static final int T18 = 18;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T18_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T19_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial GBL is sent</li>
	 * <li>Terminated: At receipt of GBA.</li>
	 * <li>On expire: Retransmit GBL, stop T18, start T19, alert staff, run in
	 * loop.</li>
	 * </ul>
	 */
	public static final int T19 = 19;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T19_DEFAULT = 5 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T20_DEFAULT}.</li>
	 * <li>Time span: 15-60 sec</li>
	 * <li>Started: When CGU is sent.</li>
	 * <li>Terminated: At receipt of CGUA.</li>
	 * <li>On expire: Retransmit CGU, start T20.</li>
	 * </ul>
	 */
	public static final int T20 = 20;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T20_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T21_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial CGU is sent.</li>
	 * <li>Terminated: At receipt of CGUA.</li>
	 * <li>On expire: Retransmit CGU, stop T20, start T21, alert staff, run in
	 * loop.</li>
	 * </ul>
	 */
	public static final int T21 = 21;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T21_DEFAULT = 5 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T22_DEFAULT}.</li>
	 * <li>Time span: 15-60 sec</li>
	 * <li>Started: When GRS is sent.</li>
	 * <li>Terminated: At receipt of GRA.</li>
	 * <li>On expire: Retransmit GRS, start T22.</li>
	 * </ul>
	 */
	public static final int T22 = 22;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T22_DEFAULT = 15 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T23_DEFAULT}.</li>
	 * <li>Time span: 5-15 min</li>
	 * <li>Started: When initial GRs is sent.</li>
	 * <li>Terminated: At receipt of GRA.</li>
	 * <li>On expire: Retransmit GRS, stop T22, start T23, alert staff, run in
	 * loop.</li>
	 * </ul>
	 */
	public static final int T23 = 23;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T23_DEFAULT = 5 * 60 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T24_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T24 = 24;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T24_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T25_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T25 = 25;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T25_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T26_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T26 = 26;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T26_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T27_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T27 = 27;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T27_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T28_DEFAULT}.</li>
	 * <li>Time span: 10s</li>
	 * <li>Started: When CQM is sent. </li>
	 * <li>Terminated:On receipt of CQR. </li>
	 * <li>On expire: Alert staff</li>
	 * </ul>
	 */
	public static final int T28 = 28;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T28_DEFAULT = 10 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T29_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T29 = 29;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T29_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T30_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T30 = 30;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T30_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T31_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T31 = 31;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T31_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T32_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T32 = 32;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T32_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T33_DEFAULT}.</li>
	 * <li>Time span: 12-15 sec</li>
	 * <li>Started: When INR is sent.</li>
	 * <li>Terminated: On receipt of INF.</li>
	 * <li>On expire: Release call, alert staff.</li>
	 * </ul>
	 */
	public static final int T33 = 33;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T33_DEFAULT = 12 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T34_DEFAULT}.</li>
	 * <li>Time span: 2-4 sec</li>
	 * <li>Started: When segmentation indication is received in IAM/ACM/CPG/ANM
	 * or CON.</li>
	 * <li>Terminated: At receipt of SEG.</li>
	 * <li>On expire: Proceed with call.</li>
	 * </ul>
	 */
	public static final int T34 = 34;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T34_DEFAULT = 2 * 1000;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T35_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T35 = 35;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T35_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T36_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T36 = 36;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T36_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T37_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T37 = 37;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T37_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T38_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T38 = 38;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T38_DEFAULT = -1;
	/**
	 * Timer ID used as argument in TimeoutEvent:
	 * <ul>
	 * <li>Default value: {@link #T39_DEFAULT}.</li>
	 * <li>Time span: n/a</li>
	 * <li>Started: n/a</li>
	 * <li>Terminated: n/a</li>
	 * <li>On expire: n/a</li>
	 * </ul>
	 */
	public static final int T39 = 39;
	/**
	 * Default miliseconds for timers.
	 */
	public static final int T39_DEFAULT = -1;
}
