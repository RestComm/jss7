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

package org.mobicents.protocols.ss7.tcap.oam;

import java.util.Arrays;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 * @author Amit Bhayani
 *
 */
public class TCAPExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(TCAPExecutor.class);

    private FastMap<String, TCAPStackImpl> tcapStacks = new FastMap<String, TCAPStackImpl>();

    private TCAPStackImpl tcapStack = null;

    /**
     *
     */
    public TCAPExecutor() {
        // TODO Auto-generated constructor stub
    }

    public Map<String, TCAPStackImpl> getTcapStacks() {
        return tcapStacks;
    }

    public void setTcapStacks(Map<String, TCAPStackImpl> mtp3UserPartsTemp) {
        if (mtp3UserPartsTemp != null) {
            synchronized (this) {
                FastMap<String, TCAPStackImpl> newMtp3UserPart = new FastMap<String, TCAPStackImpl>();
                newMtp3UserPart.putAll(mtp3UserPartsTemp);
                this.tcapStacks = newMtp3UserPart;
            }
        }
    }

    private void setDefaultValue() {
        if (this.tcapStack == null) {
            Map.Entry<String, TCAPStackImpl> sccpStacksTmp = this.tcapStacks.entrySet().iterator().next();
            this.tcapStack = sccpStacksTmp.getValue();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#execute(java.lang.String[])
     */
    @Override
    public String execute(String[] options) {

        if (this.tcapStacks == null || this.tcapStacks.size() == 0) {
            logger.warn("TCAPStackImpl not set. Command will not be executed ");
            return TCAPOAMMessage.SERVER_ERROR;
        }

        // Atleast 1 option is passed?
        if (options == null || options.length < 2) {
            return TCAPOAMMessage.INVALID_COMMAND;
        }

        String firstOption = options[1];

        if (firstOption == null) {
            return TCAPOAMMessage.INVALID_COMMAND;
        }

        try {
            if (firstOption.equals("set")) {
                return this.manageSet(options);
            } else if (firstOption.equals("get")) {
                return this.manageGet(options);
            }
            // } catch (Exception e) {
            // logger.error(String.format("Error while executing comand %s", Arrays.toString(options)), e);
            // return e.getMessage();
        } catch (Throwable t) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(options)), t);
            return t.getMessage();
        }

        return TCAPOAMMessage.INVALID_COMMAND;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang.String)
     */
    @Override
    public boolean handles(String command) {
        return command.startsWith("tcap");
    }

    /**
     * Command is tcap set <param-name> <name of stack> <value>
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String manageSet(String[] options) throws Exception {
        // sccp set <command> <value> stackname <stack-name>
        if (options.length < 4) {
            return TCAPOAMMessage.INVALID_COMMAND;
        }

        int count = 4;

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return TCAPOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("stackname")) {
                String m3uaStackName = options[count++];

                TCAPStackImpl sccpStaclImpl = this.tcapStacks.get(m3uaStackName);

                if (sccpStaclImpl == null) {
                    return String.format(TCAPOAMMessage.NO_TCAP_STCAK_CONFIGURE, m3uaStackName);
                }

                this.tcapStack = sccpStaclImpl;
            } else {
                return TCAPOAMMessage.INVALID_COMMAND;
            }

        }

        this.setDefaultValue();

        String parName = options[2].toLowerCase();

        if (parName.equals("dialogidletimeout")) {
            long val = Long.parseLong(options[3]);
            this.tcapStack.setDialogIdleTimeout(val);
        } else if (parName.equals("invoketimeout")) {
            long val = Long.parseLong(options[3]);
            this.tcapStack.setInvokeTimeout(val);
        } else if (parName.equals("maxdialogs")) {
            int val = Integer.parseInt(options[3]);
            this.tcapStack.setMaxDialogs(val);
        } else if (parName.equals("dialogidrangestart")) {
            long val = Long.parseLong(options[3]);
            this.tcapStack.setDialogIdRangeStart(val);
        } else if (parName.equals("dialogidrangeend")) {
            long val = Long.parseLong(options[3]);
            this.tcapStack.setDialogIdRangeEnd(val);
//        } else if (parName.equals("previewmode")) {
//            boolean val = Boolean.parseBoolean(options[3]);
//            this.tcapStack.setPreviewMode(val);
        } else if (parName.equals("donotsendprotocolversion")) {
            boolean val = Boolean.parseBoolean(options[3]);
            this.tcapStack.setDoNotSendProtocolVersion(val);
        } else if (parName.equals("statisticsenabled")) {
            boolean val = Boolean.parseBoolean(options[3]);
            this.tcapStack.setStatisticsEnabled(val);

        } else if (parName.equals("executordelaythreshold_1")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_ExecutorDelayThreshold_1(val);
        } else if (parName.equals("executordelaythreshold_2")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_ExecutorDelayThreshold_2(val);
        } else if (parName.equals("executordelaythreshold_3")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_ExecutorDelayThreshold_3(val);

        } else if (parName.equals("executorbacktonormaldelaythreshold_1")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_ExecutorBackToNormalDelayThreshold_1(val);
        } else if (parName.equals("executorbacktonormaldelaythreshold_2")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_ExecutorBackToNormalDelayThreshold_2(val);
        } else if (parName.equals("executorbacktonormaldelaythreshold_3")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_ExecutorBackToNormalDelayThreshold_3(val);

        } else if (parName.equals("memorythreshold_1")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_MemoryThreshold_1(val);
        } else if (parName.equals("memorythreshold_2")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_MemoryThreshold_2(val);
        } else if (parName.equals("memorythreshold_3")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_MemoryThreshold_3(val);

        } else if (parName.equals("backtonormalmemorythreshold_1")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_BackToNormalMemoryThreshold_1(val);
        } else if (parName.equals("backtonormalmemorythreshold_2")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_BackToNormalMemoryThreshold_2(val);
        } else if (parName.equals("backtonormalmemorythreshold_3")) {
            double val = Double.parseDouble(options[3]);
            this.tcapStack.setCongControl_BackToNormalMemoryThreshold_3(val);

        } else if (parName.equals("blockingincomingtcapmessages")) {
            boolean val = Boolean.parseBoolean(options[3]);
            this.tcapStack.setCongControl_blockingIncomingTcapMessages(val);

        } else {
            return TCAPOAMMessage.INVALID_COMMAND;
        }

        return String.format(TCAPOAMMessage.PARAMETER_SUCCESSFULLY_SET, this.tcapStack.getName());
    }

    /**
     * Command is tcap get <param-name> <name of stack>
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String manageGet(String[] options) throws Exception {
        // Minimum 2 needed. Show
        if (options.length < 2) {
            return TCAPOAMMessage.INVALID_COMMAND;
        }

        if (options.length == 3) {

            this.setDefaultValue();

            String parName = options[2].toLowerCase();
            StringBuilder sb = new StringBuilder();
            sb.append(options[2]);
            sb.append(" = ");
            if (parName.equals("dialogidletimeout")) {
                sb.append(this.tcapStack.getDialogIdleTimeout());
            } else if (parName.equals("invoketimeout")) {
                sb.append(this.tcapStack.getInvokeTimeout());
            } else if (parName.equals("maxdialogs")) {
                sb.append(this.tcapStack.getMaxDialogs());
            } else if (parName.equals("dialogidrangestart")) {
                sb.append(this.tcapStack.getDialogIdRangeStart());
            } else if (parName.equals("dialogidrangeend")) {
                sb.append(this.tcapStack.getDialogIdRangeEnd());
            } else if (parName.equals("previewmode")) {
                sb.append(this.tcapStack.getPreviewMode());
            } else if (parName.equals("donotsendprotocolversion")) {
                sb.append(this.tcapStack.getDoNotSendProtocolVersion());
            } else if (parName.equals("statisticsenabled")) {
                sb.append(this.tcapStack.getStatisticsEnabled());
            } else if (parName.equals("ssn")) {
                sb.append(this.tcapStack.getSubSystemNumber());

            } else if (parName.equals("executordelaythreshold_1")) {
                sb.append(this.tcapStack.getCongControl_ExecutorDelayThreshold_1());
            } else if (parName.equals("executordelaythreshold_2")) {
                sb.append(this.tcapStack.getCongControl_ExecutorDelayThreshold_2());
            } else if (parName.equals("executordelaythreshold_3")) {
                sb.append(this.tcapStack.getCongControl_ExecutorDelayThreshold_3());

            } else if (parName.equals("executorbacktonormaldelaythreshold_1")) {
                sb.append(this.tcapStack.getCongControl_ExecutorBackToNormalDelayThreshold_1());
            } else if (parName.equals("executorbacktonormaldelaythreshold_2")) {
                sb.append(this.tcapStack.getCongControl_ExecutorBackToNormalDelayThreshold_2());
            } else if (parName.equals("executorbacktonormaldelaythreshold_3")) {
                sb.append(this.tcapStack.getCongControl_ExecutorBackToNormalDelayThreshold_3());

            } else if (parName.equals("memorythreshold_1")) {
                sb.append(this.tcapStack.getCongControl_MemoryThreshold_1());
            } else if (parName.equals("memorythreshold_2")) {
                sb.append(this.tcapStack.getCongControl_MemoryThreshold_2());
            } else if (parName.equals("memorythreshold_3")) {
                sb.append(this.tcapStack.getCongControl_MemoryThreshold_3());

            } else if (parName.equals("backtonormalmemorythreshold_1")) {
                sb.append(this.tcapStack.getCongControl_BackToNormalMemoryThreshold_1());
            } else if (parName.equals("backtonormalmemorythreshold_2")) {
                sb.append(this.tcapStack.getCongControl_BackToNormalMemoryThreshold_2());
            } else if (parName.equals("backtonormalmemorythreshold_3")) {
                sb.append(this.tcapStack.getCongControl_BackToNormalMemoryThreshold_3());

            } else if (parName.equals("blockingincomingtcapmessages")) {
                sb.append(this.tcapStack.isCongControl_blockingIncomingTcapMessages());

            } else {
                return TCAPOAMMessage.INVALID_COMMAND;
            }

            return sb.toString();
        } else {

            StringBuilder sb = new StringBuilder();

            for (FastMap.Entry<String, TCAPStackImpl> e = this.tcapStacks.head(), end = this.tcapStacks.tail(); (e = e
                    .getNext()) != end;) {
                TCAPStackImpl tcapStackImpl = e.getValue();
                String tcapStackname = e.getKey();

                sb.append("Properties for ");
                sb.append(tcapStackname);
                sb.append("\n");
                sb.append("*******************\n");

                sb.append("dialogidletimeout = ");
                sb.append(tcapStackImpl.getDialogIdleTimeout());
                sb.append("\n");

                sb.append("invoketimeout = ");
                sb.append(tcapStackImpl.getInvokeTimeout());
                sb.append("\n");

                sb.append("maxdialogs = ");
                sb.append(tcapStackImpl.getMaxDialogs());
                sb.append("\n");

                sb.append("dialogidrangestart = ");
                sb.append(tcapStackImpl.getDialogIdRangeStart());
                sb.append("\n");

                sb.append("dialogidrangeend = ");
                sb.append(tcapStackImpl.getDialogIdRangeEnd());
                sb.append("\n");

                sb.append("previewmode = ");
                sb.append(tcapStackImpl.getPreviewMode());
                sb.append("\n");

                sb.append("donotsendprotocolversion = ");
                sb.append(tcapStackImpl.getDoNotSendProtocolVersion());
                sb.append("\n");

                sb.append("statisticsenabled = ");
                sb.append(tcapStackImpl.getStatisticsEnabled());
                sb.append("\n");

                sb.append("subsystemnumber = ");
                sb.append(tcapStackImpl.getSubSystemNumber());
                sb.append("\n");

                sb.append("executordelaythreshold_1 = ");
                sb.append(tcapStackImpl.getCongControl_ExecutorDelayThreshold_1());
                sb.append("\n");

                sb.append("executordelaythreshold_2 = ");
                sb.append(tcapStackImpl.getCongControl_ExecutorDelayThreshold_2());
                sb.append("\n");

                sb.append("executordelaythreshold_3 = ");
                sb.append(tcapStackImpl.getCongControl_ExecutorDelayThreshold_3());
                sb.append("\n");

                sb.append("executorbacktonormaldelaythreshold_1 = ");
                sb.append(tcapStackImpl.getCongControl_ExecutorBackToNormalDelayThreshold_1());
                sb.append("\n");

                sb.append("executorbacktonormaldelaythreshold_2 = ");
                sb.append(tcapStackImpl.getCongControl_ExecutorBackToNormalDelayThreshold_2());
                sb.append("\n");

                sb.append("executorbacktonormaldelaythreshold_3 = ");
                sb.append(tcapStackImpl.getCongControl_ExecutorBackToNormalDelayThreshold_3());
                sb.append("\n");

                sb.append("memorythreshold_1 = ");
                sb.append(tcapStackImpl.getCongControl_MemoryThreshold_1());
                sb.append("\n");

                sb.append("memorythreshold_2 = ");
                sb.append(tcapStackImpl.getCongControl_MemoryThreshold_2());
                sb.append("\n");

                sb.append("memorythreshold_3 = ");
                sb.append(tcapStackImpl.getCongControl_MemoryThreshold_3());
                sb.append("\n");

                sb.append("backtonormalmemorythreshold_1 = ");
                sb.append(tcapStackImpl.getCongControl_BackToNormalMemoryThreshold_1());
                sb.append("\n");

                sb.append("backtonormalmemorythreshold_2 = ");
                sb.append(tcapStackImpl.getCongControl_BackToNormalMemoryThreshold_2());
                sb.append("\n");

                sb.append("backtonormalmemorythreshold_3 = ");
                sb.append(tcapStackImpl.getCongControl_BackToNormalMemoryThreshold_3());
                sb.append("\n");

                sb.append("blockingincomingtcapmessages = ");
                sb.append(tcapStackImpl.isCongControl_blockingIncomingTcapMessages());
                sb.append("\n");

                sb.append("*******************");
                sb.append("\n");
                sb.append("\n");

            }

            return sb.toString();
        }
    }

}
