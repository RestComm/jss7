package org.restcomm.protocols.ss7.m3ua;

import java.util.Map;
import java.util.UUID;

import org.restcomm.protocols.ss7.statistics.api.LongValue;

public interface M3UACounterProvider {

    /**
     * Return a unique sessionId.
     * After m3uaManagement/counters restart this value will be changed,
     * all counters will be set to zero and all active campaigns will be removed
     */
    UUID getSessionId();
    /**
     * return a number of data packets transmitted through association.
     */
    Map<String,LongValue> getPacketsPerAssTx(String compainName);
    /**
     * return a number of ASP UP messages transmitted through association
     */
    Map<String,LongValue> getAspUpPerAssTx(String compainName);
    /**
     * return a number of ASP UP ACK messages transmitted through association
     */
    Map<String,LongValue> getAspUpAckPerAssTx(String compainName);
    /**
     * return a number of ASP DOWN messages transmitted through association
     */
    Map<String,LongValue> getAspDownPerAssTx(String compainName);
    /**
     * return a number of ASP DOWN ACK messages transmitted through association
     */
    Map<String,LongValue> getAspDownAckPerAssTx(String compainName);
    /**
     * return a number of ASP ACTIVE messages transmitted through association
     */
    Map<String,LongValue> getAspActivePerAssTx(String compainName);
    /**
     * return a number of ASP ACTIVE ACK messages transmitted through association
     */
    Map<String,LongValue> getAspActiveAckPerAssTx(String compainName);
    /**
     * return a number of ASP INACTIVE messages transmitted through association
     */
    Map<String,LongValue> getAspInactivePerAssTx(String compainName);
    /**
     * return a number of ASP INACTIVE ACK messages transmitted through association
     */
    Map<String,LongValue> getAspInactiveAckPerAssTx(String compainName);
    /**
     * return a number of ERROR messages transmitted through association
     */
    Map<String,LongValue> getErrorPerAssTx(String compainName);
    /**
     * return a number of NOTIFY messages transmitted through association
     */
    Map<String,LongValue> getNotifyPerAssTx(String compainName);
    /**
     * return a number of DUNA messages transmitted through association
     */
    Map<String,LongValue> getDunaPerAssTx(String compainName);
    /**
     * return a number of DAVA messages transmitted through association
     */
    Map<String,LongValue> getDavaPerAssTx(String compainName);
    /**
     * return a number of DAUD messages transmitted through association
     */
    Map<String,LongValue> getDaudPerAssTx(String compainName);
    /**
     * return a number of SCON messages transmitted through association
     */
    Map<String,LongValue> getSconPerAssTx(String compainName);
    /**
     * return a number of DUPU messages transmitted through association
     */
    Map<String,LongValue> getDupuPerAssTx(String compainName);
    /**
     * return a number of DRST messages transmitted through association
     */
    Map<String,LongValue> getDrstPerAssTx(String compainName);
    /**
     * return a number of BEAT messages transmitted through association
     */
    Map<String,LongValue> getBeatPerAssTx(String compainName);
    /**
     * return a number of BEAT ACK messages transmitted through association
     */
    Map<String,LongValue> getBeatAckPerAssTx(String compainName);
    /**
     * return a number of data packets received through association
     */
    Map<String,LongValue> getPacketsPerAssRx(String compainName);
    /**
     * return a number of ASP UP messages received through association
     */
    Map<String,LongValue> getAspUpPerAssRx(String compainName);
    /**
     * return a number of ASP UP ACK messages received through association
     */
    Map<String,LongValue> getAspUpAckPerAssRx(String compainName);
    /**
     * return a number of ASP DOWN messages received through association
     */
    Map<String,LongValue> getAspDownPerAssRx(String compainName);
    /**
     * return a number of ASP DOWN ACK messages received through association
     */
    Map<String,LongValue> getAspDownAckPerAssRx(String compainName);
    /**
     * return a number of ASP ACTIVE messages received through association
     */
    Map<String,LongValue> getAspActivePerAssRx(String compainName);
    /**
     * return a number of ASP ACTIVE ACK messages received through association
     */
    Map<String,LongValue> getAspActiveAckPerAssRx(String compainName);
    /**
     * return a number of ASP ACTIVE ACK messages received through association
     */
    Map<String,LongValue> getAspInactivePerAssRx(String compainName);
    /**
     * return a number of ASP INACTIVE ACK messages received through association
     */
    Map<String,LongValue> getAspInactiveAckPerAssRx(String compainName);
    /**
     * return a number of ERROR messages received through association
     */
    Map<String,LongValue> getErrorPerAssRx(String compainName);
    /**
     * return a number of NOTIFY messages received through association
     */
    Map<String,LongValue> getNotifyPerAssRx(String compainName);
    /**
     * return a number of DUNA messages received through association
     */
    Map<String,LongValue> getDunaPerAssRx(String compainName);
    /**
     * return a number of DAVA messages received through association
     */
    Map<String,LongValue> getDavaPerAssRx(String compainName);
    /**
     * return a number of DAUD messages received through association
     */
    Map<String,LongValue> getDaudPerAssRx(String compainName);
    /**
     * return a number of SCON messages received through association
     */
    Map<String,LongValue> getSconPerAssRx(String compainName);
    /**
     * return a number of DUPU messages received through association
     */
    Map<String,LongValue> getDupuPerAssRx(String compainName);
    /**
     * return a number of DRST messages received through association
     */
    Map<String,LongValue> getDrstPerAssRx(String compainName);
    /**
     * return a number of BEAT messages received through association
     */
    Map<String,LongValue> getBeatPerAssRx(String compainName);
    /**
     * return a number of BEAT ACK messages received through association
     */
    Map<String,LongValue> getBeatAckPerAssRx(String compainName);

}
