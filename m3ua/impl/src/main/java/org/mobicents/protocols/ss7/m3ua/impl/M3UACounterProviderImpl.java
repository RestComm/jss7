package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UACounterProvider;
import org.mobicents.protocols.ss7.statistics.StatDataCollectionImpl;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;

public class M3UACounterProviderImpl implements M3UACounterProvider{

    private static final Logger logger = Logger.getLogger(M3UACounterProviderImpl.class);

    private UUID sessionId = UUID.randomUUID();

    private StatDataCollection statDataCollection = new StatDataCollectionImpl();
    private M3UAManagementImpl m3uaManagementImpl;

    private static String PACKETS_PER_ASS_TX = "packetsPerAssTx";
    private static String ASP_UP_PER_ASS_TX = "aspUpPerAssTx";
    private static String ASP_UP_ACK_PER_ASS_TX = "aspUpAckPerAssTx";
    private static String ASP_DOWN_PER_ASS_TX = "aspDownPerAssTx";
    private static String ASP_DOWN_ACK_PER_ASS_TX = "aspDownAckPerAssTx";
    private static String ASP_ACTIVE_PER_ASS_TX = "aspActivePerAssTx";
    private static String ASP_ACTIVE_ACK_PER_ASS_TX = "aspActiveAckPerAssTx";
    private static String ASP_INACTIVE_PER_ASS_TX = "aspInactivePerAssTx";
    private static String ASP_INACTIVE_ACK_PER_ASS_TX = "aspInactiveAckPerAssTx";
    private static String ERROR_PER_ASS_TX = "errorPerAssTx";
    private static String NOTIFY_PER_ASS_TX = "notifyPerAssTx";
    private static String DUNA_PER_ASS_TX = "dunaPerAssTx";
    private static String DAVA_PER_ASS_TX = "davaPerAssTx";
    private static String DAUD_PER_ASS_TX = "daudPerAssTx";
    private static String SCON_PER_ASS_TX = "sconPerAssTx";
    private static String DUPU_PER_ASS_TX = "dupuPerAssTx";
    private static String DRST_PER_ASS_TX = "drstPerAssTx";
    private static String BEAT_PER_ASS_TX = "beatPerAssTx";
    private static String BEAT_ACK_PER_ASS_TX = "beatAckPerAssTx";

    private static String PACKETS_PER_ASS_RX = "packetsPerAssRx";
    private static String ASP_UP_PER_ASS_RX = "aspUpPerAssRx";
    private static String ASP_UP_ACK_PER_ASS_RX = "aspUpAckPerAssRx";
    private static String ASP_DOWN_PER_ASS_RX = "aspDownPerAssRx";
    private static String ASP_DOWN_ACK_PER_ASS_RX = "aspDownAckPerAssRx";
    private static String ASP_ACTIVE_PER_ASS_RX = "aspActivePerAssRx";
    private static String ASP_ACTIVE_ACK_PER_ASS_RX = "aspActiveAckPerAssRx";
    private static String ASP_INACTIVE_PER_ASS_RX = "aspInactivePerAssRx";
    private static String ASP_INACTIVE_ACK_PER_ASS_RX = "aspInactiveAckPerAssRx";
    private static String ERROR_PER_ASS_RX = "errorPerAssRx";
    private static String NOTIFY_PER_ASS_RX = "notifyPerAssRx";
    private static String DUNA_PER_ASS_RX = "dunaPerAssRx";
    private static String DAVA_PER_ASS_RX = "davaPerAssRx";
    private static String DAUD_PER_ASS_RX = "daudPerAssRx";
    private static String SCON_PER_ASS_RX = "sconPerAssRx";
    private static String DUPU_PER_ASS_RX = "dupuPerAssRx";
    private static String DRST_PER_ASS_RX = "drstPerAssRx";
    private static String BEAT_PER_ASS_RX = "beatPerAssRx";
    private static String BEAT_ACK_PER_ASS_RX = "beatAckPerAssRx";

    public M3UACounterProviderImpl(M3UAManagementImpl m3uaManagementImpl) {

        this.m3uaManagementImpl = m3uaManagementImpl;

        this.statDataCollection.registerStatCounterCollector(PACKETS_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_UP_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_UP_ACK_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_DOWN_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_DOWN_ACK_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_ACTIVE_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_ACTIVE_ACK_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_INACTIVE_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_INACTIVE_ACK_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ERROR_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(NOTIFY_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DUNA_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DAVA_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DAUD_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(SCON_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DUPU_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DRST_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(BEAT_PER_ASS_TX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(BEAT_ACK_PER_ASS_TX, StatDataCollectorType.StringLongMap);

        this.statDataCollection.registerStatCounterCollector(PACKETS_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_UP_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_UP_ACK_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_DOWN_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_DOWN_ACK_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_ACTIVE_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_ACTIVE_ACK_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_INACTIVE_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ASP_INACTIVE_ACK_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(ERROR_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(NOTIFY_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DUNA_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DAVA_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DAUD_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(SCON_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DUPU_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(DRST_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(BEAT_PER_ASS_RX, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(BEAT_ACK_PER_ASS_RX, StatDataCollectorType.StringLongMap);
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }

    public Map<String, LongValue> getPacketsPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(PACKETS_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(PACKETS_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updatePacketsPerAssTx(String assName) {
        this.statDataCollection.updateData(PACKETS_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspUpPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_UP_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_UP_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspUpPerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_UP_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspUpAckPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_UP_ACK_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_UP_ACK_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspUpAckPerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_UP_ACK_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspDownPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_DOWN_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_DOWN_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspDownPerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_DOWN_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspDownAckPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_DOWN_ACK_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_DOWN_ACK_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspDownAckPerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_DOWN_ACK_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspActivePerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_ACTIVE_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_ACTIVE_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspActivePerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_ACTIVE_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspActiveAckPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_ACTIVE_ACK_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_ACTIVE_ACK_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspActiveAckPerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_ACTIVE_ACK_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspInactivePerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_INACTIVE_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_INACTIVE_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspInactivePerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_INACTIVE_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getAspInactiveAckPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_INACTIVE_ACK_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ASP_INACTIVE_ACK_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspInactiveAckPerAssTx(String assName) {
        this.statDataCollection.updateData(ASP_INACTIVE_ACK_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getErrorPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ERROR_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(ERROR_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateErrorPerAssTx(String assName) {
        this.statDataCollection.updateData(ERROR_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getNotifyPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(NOTIFY_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(NOTIFY_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateNotifyPerAssTx(String assName) {
        this.statDataCollection.updateData(NOTIFY_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getDunaPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DUNA_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(DUNA_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDunaPerAssTx(String assName) {
        this.statDataCollection.updateData(DUNA_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getDavaPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DAVA_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(DAVA_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDavaPerAssTx(String assName) {
        this.statDataCollection.updateData(DAVA_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getDaudPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DAUD_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(DAUD_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDaudPerAssTx(String assName) {
        this.statDataCollection.updateData(DAUD_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getSconPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(SCON_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(SCON_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateSconPerAssTx(String assName) {
        this.statDataCollection.updateData(SCON_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getDupuPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DUPU_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(DUPU_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDupuPerAssTx(String assName) {
        this.statDataCollection.updateData(DUPU_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getDrstPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DRST_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(DRST_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDrstPerAssTx(String assName) {
        this.statDataCollection.updateData(DRST_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getBeatPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(BEAT_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(BEAT_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateBeatPerAssTx(String assName) {
        this.statDataCollection.updateData(BEAT_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getBeatAckPerAssTx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(BEAT_ACK_PER_ASS_TX, compainName);
        this.statDataCollection.updateData(BEAT_ACK_PER_ASS_TX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateBeatAckPerAssTx(String assName) {
        this.statDataCollection.updateData(BEAT_ACK_PER_ASS_TX, assName);
    }
    public Map<String, LongValue> getPacketsPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(PACKETS_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(PACKETS_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updatePacketsPerAssRx(String assName) {
        this.statDataCollection.updateData(PACKETS_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspUpPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_UP_PER_ASS_RX, compainName);
        logger.info("GET STAT RESULT : " + res);
        if(res != null)
            logger.info("RESULT VALUE : " + res.getStringLongValue());
        this.statDataCollection.updateData(ASP_UP_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspUpPerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_UP_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspUpAckPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_UP_ACK_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_UP_ACK_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspUpAckPerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_UP_ACK_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspDownPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_DOWN_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_DOWN_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspDownPerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_DOWN_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspDownAckPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_DOWN_ACK_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_DOWN_ACK_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspDownAckPerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_DOWN_ACK_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspActivePerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_ACTIVE_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_ACTIVE_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspActivePerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_ACTIVE_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspActiveAckPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_ACTIVE_ACK_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_ACTIVE_ACK_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspActiveAckPerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_ACTIVE_ACK_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspInactivePerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_INACTIVE_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_INACTIVE_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspInactivePerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_INACTIVE_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getAspInactiveAckPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ASP_INACTIVE_ACK_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ASP_INACTIVE_ACK_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateAspInactiveAckPerAssRx(String assName) {
        this.statDataCollection.updateData(ASP_INACTIVE_ACK_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getErrorPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(ERROR_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(ERROR_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateErrorPerAssRx(String assName) {
        this.statDataCollection.updateData(ERROR_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getNotifyPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(NOTIFY_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(NOTIFY_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateNotifyPerAssRx(String assName) {
        this.statDataCollection.updateData(NOTIFY_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getDunaPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DUNA_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(DUNA_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDunaPerAssRx(String assName) {
        this.statDataCollection.updateData(DUNA_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getDavaPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DAVA_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(DAVA_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDavaPerAssRx(String assName) {
        this.statDataCollection.updateData(DAVA_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getDaudPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DAUD_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(DAUD_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDaudPerAssRx(String assName) {
        this.statDataCollection.updateData(DAUD_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getSconPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(SCON_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(SCON_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateSconPerAssRx(String assName) {
        this.statDataCollection.updateData(SCON_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getDupuPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DUPU_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(DUPU_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDupuPerAssRx(String assName) {
        this.statDataCollection.updateData(DUPU_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getDrstPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(DRST_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(DRST_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateDrstPerAssRx(String assName) {
        this.statDataCollection.updateData(DRST_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getBeatPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(BEAT_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(BEAT_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateBeatPerAssRx(String assName) {
        this.statDataCollection.updateData(BEAT_PER_ASS_RX, assName);
    }
    public Map<String, LongValue> getBeatAckPerAssRx(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(BEAT_ACK_PER_ASS_RX, compainName);
        this.statDataCollection.updateData(BEAT_ACK_PER_ASS_RX, m3uaManagementImpl.getAspfactories().size());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }
    public void updateBeatAckPerAssRx(String assName) {
        this.statDataCollection.updateData(BEAT_ACK_PER_ASS_RX, assName);
    }

}
