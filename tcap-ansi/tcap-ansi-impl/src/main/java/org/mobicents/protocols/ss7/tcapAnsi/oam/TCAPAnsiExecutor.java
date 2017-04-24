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

package org.mobicents.protocols.ss7.tcapAnsi.oam;

import java.util.Arrays;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 * @author Amit Bhayani
 * @author sergey vetyutnev
 *
 */
public class TCAPAnsiExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(TCAPAnsiExecutor.class);

    private FastMap<String, TCAPStackImpl> tcapStacks = new FastMap<String, TCAPStackImpl>();

    public TCAPAnsiExecutor() {
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#execute(java.lang.String[])
     */
    @Override
    public String execute(String[] options) {

        if (this.tcapStacks == null || this.tcapStacks.size() == 0) {
            logger.warn("TCAPStackImpl not set. Command will not be executed ");
            return TCAPAnsiOAMMessage.SERVER_ERROR;
        }

        // At least 1 option is passed?
        if (options == null || options.length < 2) {
            return TCAPAnsiOAMMessage.INVALID_COMMAND;
        }

        String firstOption = options[1];

        if (firstOption == null) {
            return TCAPAnsiOAMMessage.INVALID_COMMAND;
        }

        try {
            if (firstOption.equals("set")) {
                return this.manageSet(options);
            } else if (firstOption.equals("get")) {
                return this.manageGet(options);
            }
            // } catch (Exception e) {
            // logger.error(String.format("Error while executing command %s", Arrays.toString(options)), e);
            // return e.getMessage();
        } catch (Throwable t) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(options)), t);
            return t.getMessage();
        }

        return TCAPAnsiOAMMessage.INVALID_COMMAND;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang.String)
     */
    @Override
    public boolean handles(String command) {
        return command.startsWith("tcapansi");
    }

    /**
     * Command is tcapansi set <param-name> <name of stack> <value>
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String manageSet(String[] options) throws Exception {
        // Minimum 4 needed. Show
        if (options.length < 5) {
            return TCAPAnsiOAMMessage.INVALID_COMMAND;
        }

        String parName = options[2].toLowerCase();
        String tcapStackName = options[3].toLowerCase();

        TCAPStackImpl tcapStackImpl = this.tcapStacks.get(tcapStackName);
        if (tcapStackImpl == null) {
            return String.format(TCAPAnsiOAMMessage.NO_TCAP_STCAK_CONFIGURE, tcapStackName);
        }

        if (parName.equals("dialogidletimeout")) {
            long val = Long.parseLong(options[4]);
            tcapStackImpl.setDialogIdleTimeout(val);
        } else if (parName.equals("invoketimeout")) {
            long val = Long.parseLong(options[4]);
            tcapStackImpl.setInvokeTimeout(val);
        } else if (parName.equals("maxdialogs")) {
            int val = Integer.parseInt(options[4]);
            tcapStackImpl.setMaxDialogs(val);
        } else if (parName.equals("dialogidrangestart")) {
            long val = Long.parseLong(options[4]);
            tcapStackImpl.setDialogIdRangeStart(val);
        } else if (parName.equals("dialogidrangeend")) {
            long val = Long.parseLong(options[4]);
            tcapStackImpl.setDialogIdRangeEnd(val);
//        } else if (parName.equals("previewmode")) {
//            boolean val = Boolean.parseBoolean(options[4]);
//            tcapStackImpl.setPreviewMode(val);
        } else if (parName.equals("statisticsenabled")) {
            boolean val = Boolean.parseBoolean(options[4]);
            tcapStackImpl.setStatisticsEnabled(val);
        }else if (parName.equals("slsranger")) {
            String val = String.valueOf(options[3]);
            tcapStackImpl.setSlsRange(val);
        } else {
            return TCAPAnsiOAMMessage.INVALID_COMMAND;
        }

        return TCAPAnsiOAMMessage.PARAMETER_SUCCESSFULLY_SET;
    }

    /**
     * Command is tcapansi get <param-name> <name of stack>
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String manageGet(String[] options) throws Exception {
        // Minimum 2 needed. Show
        if (options.length < 2) {
            return TCAPAnsiOAMMessage.INVALID_COMMAND;
        }

        if (options.length == 4) {
            String parName = options[2].toLowerCase();

            String tcapStackName = options[3].toLowerCase();

            TCAPStackImpl tcapStackImpl = this.tcapStacks.get(tcapStackName);
            if (tcapStackImpl == null) {
                return String.format(TCAPAnsiOAMMessage.NO_TCAP_STCAK_CONFIGURE, tcapStackName);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(options[2]);
            sb.append(" = ");
            if (parName.equals("dialogidletimeout")) {
                sb.append(tcapStackImpl.getDialogIdleTimeout());
            } else if (parName.equals("invoketimeout")) {
                sb.append(tcapStackImpl.getInvokeTimeout());
            } else if (parName.equals("maxdialogs")) {
                sb.append(tcapStackImpl.getMaxDialogs());
            } else if (parName.equals("dialogidrangestart")) {
                sb.append(tcapStackImpl.getDialogIdRangeStart());
            } else if (parName.equals("dialogidrangeend")) {
                sb.append(tcapStackImpl.getDialogIdRangeEnd());
            } else if (parName.equals("previewmode")) {
                sb.append(tcapStackImpl.getPreviewMode());
            } else if (parName.equals("statisticsenabled")) {
                sb.append(tcapStackImpl.getStatisticsEnabled());
            }else if (parName.equals("slsrange")) {
                sb.append(tcapStackImpl.getSlsRange());
            } else {
                return TCAPAnsiOAMMessage.INVALID_COMMAND;
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

                sb.append("statisticsenabled = ");
                sb.append(tcapStackImpl.getStatisticsEnabled());
                sb.append("\n");

                sb.append("slsrange = ");
                sb.append(tcapStackImpl.getSlsRange());
                sb.append("\n");

                sb.append("*******************");
                sb.append("\n");
                sb.append("\n");

            }

            return sb.toString();
        }
    }

}
