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

package org.mobicents.ss7.hardware.dialogic;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class DialogicMtp3UserPart extends Mtp3UserPartBaseImpl {

    private static final Logger logger = Logger.getLogger(DialogicMtp3UserPart.class);

    private static final int MTP3_PAUSE = 3;
    private static final int MTP3_RESUME = 4;
    private static final int MTP3_STATUS = 5;

    private int sourceModuleId;
    private int destinationModuleId;

    private InterProcessCommunicator ipc = null;
    private MtpStreamHandler streamHandler;

    protected ExecutorService layer3exec;

    public DialogicMtp3UserPart() {
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

        ipc = new InterProcessCommunicator(sourceModuleId, destinationModuleId);

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
            this.ipc.write(buf);
        }
    }

    private class MtpStreamHandler implements Runnable {

        private boolean handlerIsStarted = true;

        public void run() {

            // Execute only till state is Running
            while (this.handlerIsStarted) {
                try {
                    byte[] buf = ipc.read();

                    if (!this.handlerIsStarted)
                        return;

                    if (buf == null) {

                        // TODO: test if this event is regular we should remove the following warning
                        logger.warn("No data received while reading data from the Dialogic card");

                        // return from GCT_receive() with no message - may be the error case:
                        // make the delay to escape processor overloading
                        Thread.sleep(10);
                    } else {

                        if (buf.length >= 6) {
                            if (buf[0] == 0) {
                                this.parseMtp3Msg(buf);
                            } else {
                                Mtp3TransferPrimitive msg = getMtp3TransferPrimitiveFactory().createMtp3TransferPrimitive(buf);
                                sendTransferMessageToLocalUser(msg, msg.getSls());
                            }
                        } else {
                            logger.error("Error while reading data from the Dialogic card: received the message with length less then 6 bytes");
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error while reading data from the Dialogic card", e);
                }
            }
        }

        public void stop() {
            this.handlerIsStarted = false;
        }

        /**
         *
         * Parsing and delivering MTP-PAUSE, MTP-RESUME, MTP-STATUS primitives
         *
         * The structure of <i>PAUSE</i> is SI=0 (byte), type=3 (byte), affected dpc = int(4 bytes)</li> <li>
         * The structure of <i>RESUME</i> is SI=0 (byte), type=4 (byte), affected dpc = int(4 bytes)</li> <li>
         * The structure of <i>STATUS</i> is SI=0 (byte), type=5 (byte), status=1 or 2 (byte) where 1 = Remote User Unavailable
         * and 2 = Signaling Network Congestion, affected dpc = int(4 bytes), congestion status = 2 bytes in range of 0 to 3
         * where 0 means no congestion and 3 means maximum congestion, Unavailabilty cause = 2 bytes (if status = Remote User
         * Unavailable(1)). The unavailabilty cause may be one of the following: 0 = Unknown 1 = Unequipped User 2 =
         * Inaccessible User
         *
         * @param buf
         */
        private void parseMtp3Msg(byte[] buf) {

            try {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(buf));
                in.readUnsignedByte(); // sio zero byte

                int type = in.readUnsignedByte(); //
                switch (type) {
                    case MTP3_PAUSE:
                        Mtp3PausePrimitive msgPause = new Mtp3PausePrimitive(in.readInt());
                        sendPauseMessageToLocalUser(msgPause);
                        break;

                    case MTP3_RESUME:
                        Mtp3ResumePrimitive msgResume = new Mtp3ResumePrimitive(in.readInt());
                        sendResumeMessageToLocalUser(msgResume);
                        break;

                    case MTP3_STATUS:
                        if (buf.length < 9) {
                            logger.error("Error while parsing data from the Dialogic card: unsufficient data length for message MTP3_STATUS");
                            return;
                        }

                        int status = in.readUnsignedByte();
                        int affectedDpc = in.readInt();
                        int congestionLevel = in.readShort();
                        int unavailabiltyCause = in.readShort();
                        Mtp3StatusCause cause;
                        if (status == 1) { // 1 = Remote User Unavailable
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
                            cause = Mtp3StatusCause.SignallingNetworkCongested;
                            congestionLevel = 0;
                        }
                        Mtp3StatusPrimitive msgStatus = new Mtp3StatusPrimitive(affectedDpc, cause, congestionLevel);
                        sendStatusMessageToLocalUser(msgStatus);
                        break;

                    default:
                        logger.error("Error while parsing system messages from the Dialogic card: unknown primitive type: "
                                + type);
                }

            } catch (IOException e) {
                logger.error("IOException while parsing system messages from the Dialogic card: " + e.getMessage(), e);
            }
        }
    }
}
