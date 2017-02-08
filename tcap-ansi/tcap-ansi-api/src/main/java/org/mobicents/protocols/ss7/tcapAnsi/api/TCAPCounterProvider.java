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

package org.mobicents.protocols.ss7.tcapAnsi.api;

import java.util.Map;
import java.util.UUID;

import org.mobicents.protocols.ss7.statistics.api.LongValue;

/**
*
* @author servey vetyutnev
*
*/
public interface TCAPCounterProvider {

    /**
     * Return a unique sessionId.
     * After stack/counters restart this value will be changed,
     * all counters will be set to zero and all active campaigns will be removed
     */
    UUID getSessionId();

    /**
     * return a count of received since Stack restart TC-UNI messages
     */
    long getTcUniReceivedCount();

    /**
     * return a count of sent since Stack restart TC-UNI messages
     */
    long getTcUniSentCount();

    /**
     * return a count of received since Stack restart TC-BEGIN messages
     */
    long getTcQueryReceivedCount();

    /**
     * return a count of sent since Stack restart TC-BEGIN messages
     */
    long getTcQuerySentCount();

    /**
     * return a count of received since Stack restart TC-CONTINUE messages
     */
    long getTcConversationReceivedCount();

    /**
     * return a count of sent since Stack restart TC-CONTINUE messages
     */
    long getTcConversationSentCount();

    /**
     * return a count of received since Stack restart TC-END messages
     */
    long getTcResponseReceivedCount();

    /**
     * return a count of sent since Stack restart TC-END messages
     */
    long getTcResponseSentCount();

    /**
     * return a count of received since Stack restart TC-PROVIDER-ABORT messages
     */
    long getTcPAbortReceivedCount();

    /**
     * return a count of sent since Stack restart TC-PROVIDER-ABORT messages
     */
    long getTcPAbortSentCount();

    /**
     * return a count of received since Stack restart TC-USER-ABORT messages
     */
    long getTcUserAbortReceivedCount();

    /**
     * return a count of sent since Stack restart TC-USER-ABORT messages
     */
    long getTcUserAbortSentCount();

    /**
     * return a count of received since Stack restart InvokeNotLast components
     */
    long getInvokeNotLastReceivedCount();

    /**
     * return a count of sent since Stack restart InvokeNotLast components
     */
    long getInvokeNotLastSentCount();

    /**
     * return a count of received since Stack restart InvokeLast components
     */
    long getInvokeLastReceivedCount();

    /**
     * return a count of sent since Stack restart InvokeLast components
     */
    long getInvokeLastSentCount();

    /**
     * return a count of received since Stack restart ReturtResult components
     */
    long getReturnResultNotLastReceivedCount();

    /**
     * return a count of sent since Stack restart ReturtResult components
     */
    long getReturnResultNotLastSentCount();

    /**
     * return a count of received since Stack restart ReturtResultLast components
     */
    long getReturnResultLastReceivedCount();

    /**
     * return a count of sent since Stack restart ReturtResultLast components
     */
    long getReturnResultLastSentCount();

    /**
     * return a count of received since Stack restart ReturnError components
     */
    long getReturnErrorReceivedCount();

    /**
     * return a count of sent since Stack restart ReturnError components
     */
    long getReturnErrorSentCount();

    /**
     * return a count of received since Stack restart Reject components
     */
    long getRejectReceivedCount();

    /**
     * return a count of sent since Stack restart Reject components
     */
    long getRejectSentCount();

    /**
     * return a count of received since Stack restart DialogTimeouts
     */
    long getDialogTimeoutCount();

    /**
     * return a count of received since Stack restart DialogReleases
     */
    long getDialogReleaseCount();


    /**
     * return a current count of established Dialogs
     */
    long getCurrentDialogsCount();

    /**
     * return a count of all established Dialogs since stack start
     */
    long getAllEstablishedDialogsCount();

    /**
     * return a count of all established local originated Dialogs since stack start
     */
    long getAllLocalEstablishedDialogsCount();

    /**
     * return a count of all established remote originated Dialogs since stack start
     */
    long getAllRemoteEstablishedDialogsCount();

    /**
     * return a min count of established Dialogs
     */
    Long getMinDialogsCount(String compainName);

    /**
     * return a max current count of established Dialogs
     */
    Long getMaxDialogsCount(String compainName);

    /**
     * return a total durations of all released Dialogs since stack start (in milliseconds)
     * this value is updated when a dialog is released
     */
    long getAllDialogsDuration();


    /**
     * return an outgoing Dialogs count per ApplicationContextNames (in string form)
     * all MAP V1 operations will be assigned into empty string group ("")
     */
    Map<String,LongValue> getOutgoingDialogsPerApplicatioContextName(String compainName);

    /**
     * return an incoming Dialogs count per ApplicationContextNames (in string form)
     */
    Map<String,LongValue> getIncomingDialogsPerApplicatioContextName(String compainName);

    /**
     * return an outgoing Invokes count per OperationCodes
     */
    Map<String,LongValue> getOutgoingInvokesPerOperationCode(String compainName);

    /**
     * return an incoming Invokes count per OperationCodes
     */
    Map<String,LongValue> getIncomingInvokesPerOperationCode(String compainName);

    /**
     * return an outgoing ReturtError count per ErrorCodes
     */
    Map<String,LongValue> getOutgoingErrorsPerErrorCode(String compainName);

    /**
     * return an incoming ReturtError count per ErrorCodes
     */
    Map<String,LongValue> getIncomingErrorsPerErrorCode(String compainName);

    /**
     * return an outgoing Reject count per Problem
     */
    Map<String,LongValue> getOutgoingRejectPerProblem(String compainName);

    /**
     * return an incoming Reject count per Problem
     */
    Map<String,LongValue> getIncomingRejectPerProblem(String compainName);


    /**
     * return A max count of networkID areas that are not available
     */
    Long getMaxNetworkIdAreasNotAvailable(String compainName);

    /**
     * return A max count of networkID areas that are congested with level at least 1
     */
    Long getMaxNetworkIdAreasCongLevel_1(String compainName);

    /**
     * return A max count of networkID areas that are congested with level at least 2
     */
    Long getMaxNetworkIdAreasCongLevel_2(String compainName);

    /**
     * return A max count of networkID areas that are congested with level at least 3
     */
    Long getMaxNetworkIdAreasCongLevel_3(String compainName);

    /**
     * return A max count of Executors that are congested with level at least 1
     */
    Long getMaxExecutorsCongLevel_1(String compainName);

    /**
     * return A max count of Executors that are congested with level at least 2
     */
    Long getMaxExecutorsCongLevel_2(String compainName);

    /**
     * return A max count of Executors that are congested with level at least 3
     */
    Long getMaxExecutorsCongLevel_3(String compainName);

    /**
     * return A max memory congestion level
     */
    Long getMaxMemoryCongLevel(String compainName);

    /**
     * return Max count of TcapUserParts that are congested with level at least 1
     */
    Long getMaxUserPartsCongLevel_1(String compainName);

    /**
     * return Max count of TcapUserParts that are congested with level at least 2
     */
    Long getMaxUserPartsCongLevel_2(String compainName);

    /**
     * return Max count of TcapUserParts that are congested with level at least 3
     */
    Long getMaxUserPartsCongLevel_3(String compainName);

}
