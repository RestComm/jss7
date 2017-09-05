package org.mobicents.protocols.ss7.sccp.impl.message;

import java.util.ArrayList;
import java.util.List;

public abstract class SccpConnSegmentableMessageImpl extends SccpConnReferencedMessageImpl {
    protected byte[] userData;
    private List<byte[]> buffer = new ArrayList<>();
    private boolean isFullyReceived;

    protected SccpConnSegmentableMessageImpl(int maxDataLen, int type, int sls, int localSsn) {
        super(maxDataLen, type, sls, localSsn);
    }

    protected SccpConnSegmentableMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public byte[] getUserData() {
        return userData;
    }

    public void setUserData(byte[] userData) {
        this.userData = userData;
    }

    public boolean isFullyRecieved() {
        return this.isFullyReceived;
    }

    public abstract boolean isMoreData();

    public abstract void setMoreData(boolean moreData);

    public void setReceivedNextSegment(SccpConnSegmentableMessageImpl nextSegment) {
        if (this.buffer.isEmpty()) {
            this.buffer.add(userData);
        }
        this.buffer.add(nextSegment.userData);

        if (!nextSegment.isMoreData()) {

            int totalLength = 0;
            for (int i = 0; i < buffer.size(); i++) {
                totalLength += buffer.get(i).length;
            }
            byte[] allData = new byte[totalLength];
            int pos = 0;
            for (int i = 0; i < buffer.size(); i++) {
                System.arraycopy(buffer.get(i),0, allData,pos         , buffer.get(i).length);
                pos += buffer.get(i).length;
            }

            userData = allData;
            this.buffer.clear();
            this.isFullyReceived = true;
        }
    }

    public void cancelSegmentation() {
        this.isFullyReceived = false;
    }
}
