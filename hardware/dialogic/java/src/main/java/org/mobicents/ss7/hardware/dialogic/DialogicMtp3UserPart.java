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

package org.mobicents.ss7.hardware.dialogic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

import com.dialogic.signaling.gct.BBUtil;
import com.dialogic.signaling.gct.GctException;
import com.dialogic.signaling.gct.GctLib;
import com.dialogic.signaling.gct.GctMsg;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class DialogicMtp3UserPart extends Mtp3UserPartBaseImpl {

    private static final Logger logger = Logger.getLogger(DialogicMtp3UserPart.class);

    // Header Type of message received (from Dialogic)
    private static final int DIALOGIC_MESSAGE_TYPE_API_MSG_TX_REQ = 0xcf00;
    private static final int DIALOGIC_MESSAGE_TYPE_API_MSG_RX_IND = 0x8f01;
    private static final int DIALOGIC_MESSAGE_TYPE_MTP_MSG_PAUSE = 0x8403;
    private static final int DIALOGIC_MESSAGE_TYPE_MTP_MSG_RESUME = 0x8404;
    private static final int DIALOGIC_MESSAGE_TYPE_MTP_MSG_STATUS = 0x8405;

    private int sourceModuleId;
    private int destinationModuleId;

    private MtpStreamHandler streamHandler;

    protected ExecutorService layer3exec;

    public DialogicMtp3UserPart(String productName) {
        super(productName);
    }

    public int getSourceModuleId() {
        return sourceModuleId;
    }

    public void setSourceModuleId(int sourceModuleId) {
        this.sourceModuleId = sourceModuleId;
    }

    public int getDestinationModuleId() {
        return destinationModuleId;
    }

    public void setDestinationModuleId(int destinationModuleId) {
        this.destinationModuleId = destinationModuleId;
    }

    @Override
    public void start() throws Exception {

        // ipc = new InterProcessCommunicator(sourceModuleId, destinationModuleId);

        layer3exec = Executors.newFixedThreadPool(1);
        this.streamHandler = new MtpStreamHandler();
        layer3exec.execute(this.streamHandler);

        super.start();
    }

    @Override
    public void stop() throws Exception {

        super.stop();

        this.streamHandler.stop();
        layer3exec.shutdown();
    }

    @Override
    public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {

        if (this.isStarted) {
            byte[] buf = msg.encodeMtp3();
            try {
                GctMsg txMsg = GctLib.getm(buf.length);
                txMsg.setDst((short)this.destinationModuleId);
                txMsg.setSrc((short)this.sourceModuleId);
                txMsg.setType(DIALOGIC_MESSAGE_TYPE_API_MSG_TX_REQ);
                ByteBuffer bytebuffer = txMsg.getParam();
                bytebuffer.put(buf);
                bytebuffer.flip();
                GctLib.send(txMsg);
            } catch (GctException e) {
               throw new IOException(e);
            }
        }
    }

    private class MtpStreamHandler implements Runnable {

        private boolean handlerIsStarted = true;

        public void run() {

            // Execute only till state is Running
            while (this.handlerIsStarted) {
                try {
                    GctMsg gctmsg = GctLib.receive((short) sourceModuleId);
                    ByteBuffer byteBuffer = gctmsg.getParam();

                    /*Parsing and delivering MTP-PAUSE, MTP-RESUME, MTP-STATUS primitives
                    *
                    * The structure of <i>PAUSE</i> is SI=0 (byte), type=3 (byte), affected dpc = int(4 bytes)</li> <li>
                    * The structure of <i>RESUME</i> is SI=0 (byte), type=4 (byte), affected dpc = int(4 bytes)</li> <li>
                    * The structure of <i>STATUS</i> is SI=0 (byte), type=5 (byte), status=1 or 2 (byte) where 1 = Remote User Unavailable
                    * and 2 = Signaling Network Congestion, affected dpc = int(4 bytes), congestion status = 2 bytes in range of 0 to 3
                    * where 0 means no congestion and 3 means maximum congestion, Unavailabilty cause = 2 bytes (if status = Remote User
                    * Unavailable(1)). The unavailabilty cause may be one of the following: 0 = Unknown 1 = Unequipped User 2 =
                    * Inaccessible User
                    */
                    switch (gctmsg.getType()) {
                        case DIALOGIC_MESSAGE_TYPE_API_MSG_RX_IND:
                            byte[] b = new byte[byteBuffer.remaining()];
                            byteBuffer.get(b);
                            Mtp3TransferPrimitive msg = getMtp3TransferPrimitiveFactory().createMtp3TransferPrimitive(b);
                            sendTransferMessageToLocalUser(msg, msg.getSls());

                            // we release a message now
                            GctLib.relm(gctmsg);
                            break;
                        case DIALOGIC_MESSAGE_TYPE_MTP_MSG_PAUSE:
                            Mtp3PausePrimitive msgPause = new Mtp3PausePrimitive((int) BBUtil.getU32(byteBuffer));
                            sendPauseMessageToLocalUser(msgPause);
                            break;
                        case DIALOGIC_MESSAGE_TYPE_MTP_MSG_RESUME:
                            Mtp3ResumePrimitive msgResume = new Mtp3ResumePrimitive((int) BBUtil.getU32(byteBuffer));
                            sendResumeMessageToLocalUser(msgResume);
                            break;
                        case DIALOGIC_MESSAGE_TYPE_MTP_MSG_STATUS:
                            int status = gctmsg.getStatus();

                            int affectedDpc = (int) BBUtil.getU32(byteBuffer);
                            int congestionLevel = 0;

                            Mtp3StatusCause cause;

                            int par2 = BBUtil.getU16(byteBuffer);
                            int par3 = BBUtil.getU16(byteBuffer);
                            if (status == 1) { // 1 = Remote User Unavailable
                                int unavailabiltyCause = par3;
                                // int unavailabiltyCause = BBUtil.getU16(byteBuffer);
                                switch (unavailabiltyCause) {
                                    case 1:
                                        cause = Mtp3StatusCause.UserPartUnavailability_UnequippedRemoteUser;
                                        break;
                                    case 2:
                                        cause = Mtp3StatusCause.UserPartUnavailability_InaccessibleRemoteUser;
                                        break;
                                    default:
                                        cause = Mtp3StatusCause.UserPartUnavailability_Unknown;
                                        break;
                                }
                                congestionLevel = 0;
                            } else { // 2 = Signaling Network Congestion
                                congestionLevel = par2;
                                // congestionLevel = BBUtil.getU16(byteBuffer);
                                cause = Mtp3StatusCause.SignallingNetworkCongested;
                            }

                            Mtp3StatusPrimitive msgStatus = new Mtp3StatusPrimitive(affectedDpc, cause, congestionLevel, 0);
                            sendStatusMessageToLocalUser(msgStatus);
                            break;
                        default:
                            logger.error("Received unrecognized message type from Dialogic board \n M-t"
                                    + String.format("%04x", gctmsg.getType()) + " -i" + String.format("%04x", gctmsg.getId())
                                    + " -f" + String.format("%02x", gctmsg.getSrc()) + " -d"
                                    + String.format("%02x", gctmsg.getDst()) + " -s"
                                    + String.format("%02x", gctmsg.getStatus()));

                            if (byteBuffer.hasRemaining()) {
                                StringBuffer sb = new StringBuffer();
                                sb.append("-p");
                                while (byteBuffer.hasRemaining()) {
                                    sb.append(String.format("%02x", BBUtil.getU8(byteBuffer)));
                                }
                                logger.error(sb.toString());
                            }

                            break;
                    }
                } catch (Exception e) {
                    logger.error("Error while reading data from the Dialogic card", e);
                }
            }
        }

        public void stop() {
            this.handlerIsStarted = false;
        }

    }
}
