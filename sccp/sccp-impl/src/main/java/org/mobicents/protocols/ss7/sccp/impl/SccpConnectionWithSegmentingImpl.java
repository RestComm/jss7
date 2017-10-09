package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt1MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class SccpConnectionWithSegmentingImpl extends SccpConnectionWithTimers {
    private boolean awaitSegments = false;
    private SccpConnSegmentableMessageImpl currentSegmentedMessage;

    public SccpConnectionWithSegmentingImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol, SccpStackImpl stack,
                                            SccpRoutingControl sccpRoutingControl) {
        super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
    }

    protected void receiveMessage(SccpConnMessage message) throws Exception {
        super.receiveMessage(message);

        if (message instanceof SccpConnSegmentableMessageImpl) {
            receiveDataMessage((SccpConnSegmentableMessageImpl) message);
        }
    }

    protected void receiveDataMessage(SccpConnSegmentableMessageImpl msg) throws Exception {
        if (!msg.isMoreData() && !awaitSegments) {
            callListenerOnData(msg.getUserData());
        } else if (msg.isMoreData()) {
            awaitSegments = true;
            if (currentSegmentedMessage == null) {
                currentSegmentedMessage = msg;
            } else {
                currentSegmentedMessage.setReceivedNextSegment(msg);
            }
        } else if (!msg.isMoreData() && awaitSegments) {
            currentSegmentedMessage.setReceivedNextSegment(msg);
            awaitSegments = false;

            if (!currentSegmentedMessage.isFullyRecieved()) {
                logger.error(String.format("Message is expected to be fully received but it isn't: %s", msg));
                throw new IllegalStateException();
            }

            callListenerOnData(currentSegmentedMessage.getUserData());
            currentSegmentedMessage = null;
        }
    }

    public void send(byte[] data) throws Exception {

        if (data.length <= 255) {
            sendDataMessageSegment(data, false);
        } else {
            int chunks = (int) Math.ceil(data.length / 255.0);
            int pos = 0;
            List<byte[]> chunkData = new ArrayList<>();
            for (int i = 0; i < chunks; i++) {
                int copyBytes;
                if (i != chunks - 1) {
                    copyBytes = 255;
                    chunkData.add(Arrays.copyOfRange(data, pos, pos + 255));
                } else {
                    copyBytes = data.length - i * 255;
                    chunkData.add(Arrays.copyOfRange(data, pos, pos + copyBytes));
                }

                pos += copyBytes;
            }
            for (int i = 0; i < chunkData.size(); i++) {
                sendDataMessageSegment(chunkData.get(i), i != chunkData.size() - 1);
            }
        }
    }

    private void sendDataMessageSegment(byte[] data, boolean moreData) throws Exception {
        if (data.length > 255) {
            logger.error("Message data is too lengthy");
            throw new IllegalArgumentException("Message data is too lengthy");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Sending data message to DPC=%d, SSN=%d, DLR=%s", getRemoteDpc(),
                    getRemoteSsn(), getRemoteReference()));
        }

        SccpConnSegmentableMessageImpl dataMessage;

        if (getProtocolClass().getProtocolClass() == 2) {
            dataMessage = new SccpConnDt1MessageImpl(255, getSls(), getLocalSsn());
        } else {
            dataMessage = new SccpConnDt2MessageImpl(255, getSls(), getLocalSsn());
        }

        dataMessage.setDestinationLocalReferenceNumber(getRemoteReference());
        dataMessage.setSourceLocalReferenceNumber(getLocalReference());
        dataMessage.setUserData(data);
        dataMessage.setMoreData(moreData);
        lastMoreDataSent = moreData;

        sendMessage(dataMessage);
    }
}
