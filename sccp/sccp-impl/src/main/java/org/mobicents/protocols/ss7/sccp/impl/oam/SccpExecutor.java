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

package org.mobicents.protocols.ss7.sccp.impl.oam;

import javolution.util.FastMap;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.management.console.ShellExecutor;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author amit bhayani
 *
 */
public class SccpExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(SccpExecutor.class);

    private FastMap<String, SccpStackImpl> sccpStacks = new FastMap<String, SccpStackImpl>();
    private SccpStackImpl sccpStack = null;

    public SccpExecutor() {

    }

    public void setSccpStacks(Map<String, SccpStackImpl> sccpStacksTemp) {
        if (sccpStacksTemp != null) {
            synchronized (this) {
                FastMap<String, SccpStackImpl> newSccpStacks = new FastMap<String, SccpStackImpl>();
                newSccpStacks.putAll(sccpStacksTemp);
                this.sccpStacks = newSccpStacks;
            }
        }
    }

    private void setDefaultValue() {
        if (this.sccpStack == null) {
            Map.Entry<String, SccpStackImpl> sccpStacksTmp = this.sccpStacks.entrySet().iterator().next();
            this.sccpStack = sccpStacksTmp.getValue();
        }
    }

    /**
     * @return the sccpStacks
     */
    public Map<String, SccpStackImpl> getSccpStacks() {
        return sccpStacks;
    }

    public String execute(String[] options) {
        if (this.sccpStacks.size() == 0) {
            logger.warn("SCCP stack not set. Command will not be executed ");
            return SccpOAMMessage.SERVER_ERROR;
        }

        // Atleast 1 option is passed?
        if (options == null || options.length < 2) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String firstOption = options[1];

        if (firstOption == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        try {
            if (firstOption.equals("rule")) {
                return this.manageRule(options);
            } else if (firstOption.equals("address")) {
                return this.manageAddress(options);
            } else if (firstOption.equals("rsp")) {
                return this.manageRsp(options);
            } else if (firstOption.equals("rss")) {
                return this.manageRss(options);
            } else if (firstOption.equals("lmr")) {
                return this.manageLmr(options);
            } else if (firstOption.equals("sap")) {
                return this.manageSap(options);
            } else if (firstOption.equals("dest")) {
                return this.manageDest(options);
            } else if (firstOption.equals("csp")) {
                return this.manageConcernedSpc(options);
            } else if (firstOption.equals("set")) {
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

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageRss(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            // sccp rss create <id> <remote-spc> <remote-ssn> <rss-flag> prohibitedwhenspcresuming
            // <mark-prohibited-when-spc-resuming> stackname <stack-name>

            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int remoteSsId = Integer.parseInt(options[3]);

            int remoteSpc = Integer.parseInt(options[4]);

            int remoteSs = Integer.parseInt(options[5]);
            int remoteSsFlag = Integer.parseInt(options[6]);

            boolean markProhibitedWhenSpcResuming = false;

            int count = 7;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("prohibitedwhenspcresuming")) {
                    markProhibitedWhenSpcResuming = Boolean.parseBoolean(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().addRemoteSsn(remoteSsId, remoteSpc, remoteSs, remoteSsFlag,
                    markProhibitedWhenSpcResuming);
            return String.format(SccpOAMMessage.RSS_SUCCESSFULLY_ADDED, this.sccpStack.getName());
        } else if (command.equals("modify")) {
            // sccp rss modify <id> <remote-spc> <remote-ssn> <rss-flag> prohibitedwhenspcresuming
            // <mark-prohibited-when-spc-resuming> stackname <stack-name>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int remoteSsId = Integer.parseInt(options[3]);
            int remoteSpc = Integer.parseInt(options[4]);
            int remoteSs = Integer.parseInt(options[5]);
            int remoteSsFlag = Integer.parseInt(options[6]);
            boolean markProhibitedWhenSpcResuming = false;

            int count = 7;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("prohibitedwhenspcresuming")) {
                    markProhibitedWhenSpcResuming = Boolean.parseBoolean(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();
            this.sccpStack.getSccpResource().modifyRemoteSsn(remoteSsId, remoteSpc, remoteSs, remoteSsFlag,
                    markProhibitedWhenSpcResuming);

            return String.format(SccpOAMMessage.RSS_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp rss delete <id> stackname <stack-name>
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int remoteSsId = Integer.parseInt(options[3]);

            int count = 4;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().removeRemoteSsn(remoteSsId);
            return String.format(SccpOAMMessage.RSS_SUCCESSFULLY_DELETED, this.sccpStack.getName());
        } else if (command.equals("show")) {
            // sccp rss show id <id> stackname <stack-name>
            int count = 3;
            int remoteSsId = -1;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    remoteSsId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (remoteSsId != -1) {
                RemoteSubSystem rss = this.sccpStack.getSccpResource().getRemoteSsn(remoteSsId);
                if (rss == null) {
                    return String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.sccpStack.getName());
                }
                return rss.toString();
            }

            if (this.sccpStack.getSccpResource().getRemoteSsns().size() == 0) {
                return String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.sccpStack.getName());
            }

            Map<Integer, RemoteSubSystem> idVsRemoteSsn = this.sccpStack.getSccpResource().getRemoteSsns();

            StringBuffer sb = new StringBuffer();
            for (Integer e : idVsRemoteSsn.keySet()) {
                RemoteSubSystem rss = idVsRemoteSsn.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(rss);
                sb.append("\n");
            }
            return sb.toString();
        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageRsp(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            // sccp rsp create <id> <remote-spc> <rspc-flag> <mask> stackname <stack-name>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int remoteSpcId = Integer.parseInt(options[3]);
            int remoteSpc = Integer.parseInt(options[4]);
            int remoteSpcFlag = Integer.parseInt(options[5]);
            int mask = Integer.parseInt(options[6]);

            int count = 7;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().addRemoteSpc(remoteSpcId, remoteSpc, remoteSpcFlag, mask);

            return String.format(SccpOAMMessage.RSPC_SUCCESSFULLY_ADDED, this.sccpStack.getName());
        } else if (command.equals("modify")) {
            // sccp rsp modify <id> <remote-spc> <rspc-flag> <mask> stackname <stack-name>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int remoteSpcId = Integer.parseInt(options[3]);

            int remoteSpc = Integer.parseInt(options[4]);
            int remoteSpcFlag = Integer.parseInt(options[5]);
            int mask = Integer.parseInt(options[6]);

            int count = 7;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().modifyRemoteSpc(remoteSpcId, remoteSpc, remoteSpcFlag, mask);

            return String.format(SccpOAMMessage.RSPC_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp rsp delete <id> stackname <stack-name>
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int remoteSpcId = Integer.parseInt(options[3]);

            int count = 4;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().removeRemoteSpc(remoteSpcId);
            return String.format(SccpOAMMessage.RSPC_SUCCESSFULLY_DELETED, this.sccpStack.getName());

        } else if (command.equals("show")) {
            // sccp rsp show id <id> stackname <stack-name>

            int count = 3;
            int remoteSpcId = -1;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    remoteSpcId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (remoteSpcId != -1) {
                RemoteSignalingPointCode rspc = this.sccpStack.getSccpResource().getRemoteSpc(remoteSpcId);
                if (rspc == null) {
                    return String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.sccpStack.getName());
                }

                return rspc.toString();
            }

            if (this.sccpStack.getSccpResource().getRemoteSpcs().size() == 0) {
                return String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.sccpStack.getName());
            }

            StringBuffer sb = new StringBuffer();
            Map<Integer, RemoteSignalingPointCode> idVsRspc = this.sccpStack.getSccpResource().getRemoteSpcs();
            Set<Integer> rspIds = idVsRspc.keySet();
            for (Integer e : rspIds) {
                RemoteSignalingPointCode rsp = idVsRspc.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(rsp);
                sb.append("\n");
            }
            return sb.toString();

        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageAddress(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int addressId = Integer.parseInt(options[3]);

            int count = 11;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();
            SccpAddress address = this.createAddress(options, 4, false);

            this.sccpStack.getRouter().addRoutingAddress(addressId, address);
            return String.format(SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED, this.sccpStack.getName());
        } else if (command.equals("modify")) {
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int addressId = Integer.parseInt(options[3]);

            int count = 11;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            SccpAddress address = this.createAddress(options, 4, false);

            this.sccpStack.getRouter().modifyRoutingAddress(addressId, address);
            return String.format(SccpOAMMessage.ADDRESS_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp address delete <id> stackname <stack-name>

            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int addressId = Integer.parseInt(options[3]);

            int count = 4;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().removeRoutingAddress(addressId);
            return String.format(SccpOAMMessage.ADDRESS_SUCCESSFULLY_DELETED, this.sccpStack.getName());

        } else if (command.equals("show")) {
            // sccp address show id <id> stackname <stack-name>

            int count = 3;
            int addressId = -1;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    addressId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (addressId != -1) {
                SccpAddress pa = this.sccpStack.getRouter().getRoutingAddress(addressId);
                if (pa == null) {
                    return String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, this.sccpStack.getName());
                }
                return pa.toString();
            }

            if (this.sccpStack.getRouter().getRoutingAddresses().size() == 0) {
                return String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, this.sccpStack.getName());
            }

            StringBuffer sb = new StringBuffer();

            Map<Integer, SccpAddress> idVsPrimAdd = this.sccpStack.getRouter().getRoutingAddresses();
            for (Integer e : idVsPrimAdd.keySet()) {
                SccpAddress address = idVsPrimAdd.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(address);
                sb.append("\n");
            }
            return sb.toString();
        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageRule(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            return this.createRule(options);
        } else if (command.equals("modify")) {
            return this.modifyRule(options);
        } else if (command.equals("delete")) {
            return this.deleteRule(options);
        } else if (command.equals("show")) {
            return this.showRule(options);
        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    /**
     * <p>
     * Command to create new rule.
     *
     * sccp rule create <id> <mask> <address-indicator> <point-code> <subsystem-number> <translation-type> <numbering-plan>
     * <nature-of-address-indicator> <digits> <ruleType> <primary-address-id> backup-addressid <backup-address-id>
     * loadsharing-algo <loadsharing-algorithm> newcgparty-addressid <new-callingPartyAddress-id> origination-type
     * <originationType> networkid <network-id> calling-ai <address-indicator> calling-pc <point-code> calling-ssn <calling-subsystem-number> calling-tt <calling-translation-type> calling-np <calling-numbering-plan>
     * calling-nai <calling-nature-of-address-indicator> calling-digits-pattern <calling-digits-pattern> stackname <stack-name>
     * </p>
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String createRule(String[] options) throws Exception {
        // Minimum is 13
        if (options.length < 14 || options.length > 40) {
            return SccpOAMMessage.INVALID_COMMAND;
        }
        int ruleId = Integer.parseInt(options[3]);
        String mask = options[4];
        if (mask == null) {
            return SccpOAMMessage.INVALID_MASK;
        }

        RuleType ruleType;
        String s1 = options[12].toLowerCase();
        if (s1.equalsIgnoreCase(RuleType.SOLITARY.getValue())) {
            ruleType = RuleType.SOLITARY;
        } else if (s1.equalsIgnoreCase(RuleType.DOMINANT.getValue())) {
            ruleType = RuleType.DOMINANT;
        } else if (s1.equalsIgnoreCase(RuleType.LOADSHARED.getValue())) {
            ruleType = RuleType.LOADSHARED;
        } else if (s1.equals("broadcast")) {
            ruleType = RuleType.BROADCAST;
        } else {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        int pAddressId = Integer.parseInt(options[13]);

        int count = 14;
        int sAddressId = -1;
        Integer newcgpartyAddressId = null;
        LoadSharingAlgorithm algo = LoadSharingAlgorithm.Undefined;
        OriginationType originationType = OriginationType.ALL;
        int networkId = 0;

        // Calling Address fields with default values
        int callingAI = -1;
        int callingPC = -1;
        int callingSSN = -1;
        int callingTT = -1;
        int callingNP = -1;
        int callingNAI = -1;
        String callingDigits = null; // having it default as * means everythign matches

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("loadsharing-algo")) {
                algo = LoadSharingAlgorithm.getInstance(options[count++]);
            } else if (key.equals("backup-addressid")) {
                sAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("newcgparty-addressid")) {
                newcgpartyAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("origination-type")) {
                originationType = OriginationType.getInstance(options[count++]);
            } else if (key.equals("stackname")) {
                String m3uaStackName = options[count++];

                SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                if (sccpStaclImpl == null) {
                    return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                }

                this.sccpStack = sccpStaclImpl;
            } else if (key.equals("networkid")) {
                String networkIdS = options[ count++ ];
                networkId = Integer.parseInt( networkIdS );
            } else if (key.equals( "calling-ai" )) {
                callingAI = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-pc" )) {
                callingPC = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-ssn" )) {
                callingSSN = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-tt" )) {
                callingTT = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-np" )) {
                callingNP = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-nai" )) {
                callingNAI = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-digits-pattern" )) {
                callingDigits = options[count++];
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }
        }

        this.setDefaultValue();
        SccpAddress pattern = this.createAddress(options, 5, true);
        SccpAddress callingPattern  = null;
        if ( callingDigits != null && !callingDigits.isEmpty()) {
            callingPattern = this.createAddress( callingAI, callingPC, callingSSN, callingTT, callingNP, callingNAI, callingDigits, true );
        }

        this.sccpStack.getRouter().addRule( ruleId, ruleType, algo, originationType, pattern, mask, pAddressId, sAddressId,
                newcgpartyAddressId, networkId, callingPattern);

        return String.format(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, this.sccpStack.getName());
    }

    private String modifyRule(String[] options) throws Exception {
        // Minimum is 13
        if (options.length < 14 || options.length > 40) {
            return SccpOAMMessage.INVALID_COMMAND;
        }
        int ruleId = Integer.parseInt(options[3]);

        String mask = options[4];

        if (mask == null) {
            return SccpOAMMessage.INVALID_MASK;
        }

        RuleType ruleType;
        String s1 = options[12].toLowerCase();
        if (s1.equals("solitary")) {
            ruleType = RuleType.SOLITARY;
        } else if (s1.equals("dominant")) {
            ruleType = RuleType.DOMINANT;
        } else if (s1.equals("loadshared")) {
            ruleType = RuleType.LOADSHARED;
        } else if (s1.equals("broadcast")) {
            ruleType = RuleType.BROADCAST;
        } else {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        int pAddressId = Integer.parseInt(options[13]);

        int count = 14;
        int sAddressId = -1;
        Integer newcgpartyAddressId = null;
        LoadSharingAlgorithm algo = LoadSharingAlgorithm.Undefined;
        OriginationType originationType = OriginationType.ALL;
        int networkId = 0;

        // Calling Address fields with default values
        int callingAI = -1;
        int callingPC = -1;
        int callingSSN = -1;
        int callingTT = -1;
        int callingNP = -1;
        int callingNAI = -1;
        String callingDigits = null; // having it default as null means no matching on callingPattern
        // TODO: Validate the AI/TT/NP/NAI in case callingDigits are provided

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("loadsharing-algo")) {
                algo = LoadSharingAlgorithm.getInstance(options[count++]);
            } else if (key.equals("backup-addressid")) {
                sAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("newcgparty-addressid")) {
                newcgpartyAddressId = Integer.parseInt(options[count++]);
            } else if (key.equals("origination-type")) {
                originationType = OriginationType.getInstance(options[count++]);
            } else if (key.equals("stackname")) {
                String m3uaStackName = options[count++];

                SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                if (sccpStaclImpl == null) {
                    return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                }

                this.sccpStack = sccpStaclImpl;
            } else if (key.equals("networkid")) {
                String networkIdS = options[count++];
                networkId = Integer.parseInt(networkIdS);
            } else if (key.equals( "calling-ai" )) {
                callingAI = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-pc" )) {
                callingPC = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-ssn" )) {
                callingSSN = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-tt" )) {
                callingTT = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-np" )) {
                callingNP = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-nai" )) {
                callingNAI = Integer.parseInt( options[count++] );
            } else if (key.equals( "calling-digits-pattern" )) {
                callingDigits = options[count++];
            }  else {
                return SccpOAMMessage.INVALID_COMMAND;
            }
        }

        this.setDefaultValue();
        SccpAddress pattern = this.createAddress(options, 5, true);
        SccpAddress callingPattern  = null;
        if ( callingDigits != null && !callingDigits.isEmpty()) {
            callingPattern = this.createAddress( callingAI, callingPC, callingSSN, callingTT, callingNP, callingNAI, callingDigits, true );
        }

        this.sccpStack.getRouter().modifyRule(ruleId, ruleType, algo, originationType, pattern, mask, pAddressId, sAddressId,
                newcgpartyAddressId, networkId, callingPattern);
        return String.format(SccpOAMMessage.RULE_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
    }

    /**
     * Command is "sccp rule delete <id> stackname <stack-name>"
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String deleteRule(String[] options) throws Exception {
        // Minimum is 4
        if (options.length < 4) {
            return SccpOAMMessage.INVALID_COMMAND;
        }
        int ruleId;
        ruleId = Integer.parseInt(options[3]);

        int count = 4;

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("stackname")) {
                String m3uaStackName = options[count++];

                SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                if (sccpStaclImpl == null) {
                    return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                }

                this.sccpStack = sccpStaclImpl;
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }

        }

        this.setDefaultValue();

        this.sccpStack.getRouter().removeRule(ruleId);
        return String.format(SccpOAMMessage.RULE_SUCCESSFULLY_REMOVED, this.sccpStack.getName());
    }

    /**
     * Command is "sccp rule show id <id> stackname <stack-name>" where id is optional. If id is not passed, all rules
     * configured are shown
     *
     * @param options
     * @return
     * @throws Exception
     */
    private String showRule(String[] options) throws Exception {
        // Minimum is 4
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        int count = 3;
        int ruleId = -1;
        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("stackname")) {
                String m3uaStackName = options[count++];

                SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                if (sccpStaclImpl == null) {
                    return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                }

                this.sccpStack = sccpStaclImpl;
            } else if (key.equals("id")) {
                ruleId = Integer.parseInt(options[count++]);
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }

        }

        this.setDefaultValue();

        if (ruleId != -1) {
            Rule rule = this.sccpStack.getRouter().getRule(ruleId);
            if (rule == null) {
                return String.format(SccpOAMMessage.RULE_DOESNT_EXIST, this.sccpStack.getName());
            }
            return rule.toString();
        }

        if (this.sccpStack.getRouter().getRules().size() == 0) {
            return String.format(SccpOAMMessage.RULE_DOESNT_EXIST, this.sccpStack.getName());
        }

        Map<Integer, Rule> idVsRule = this.sccpStack.getRouter().getRules();

        StringBuffer sb = new StringBuffer();
        for (Integer e : idVsRule.keySet()) {
            Rule rule = idVsRule.get(e);
            sb.append("key=");
            sb.append(e);
            sb.append("  Rule=");
            sb.append(rule);
            sb.append("\n");
        }

        return sb.toString();
    }

    private SccpAddress createAddress(String[] options, int index, boolean isRule) throws Exception {
        // TODO: add encoding scheme

        // sccp address create <id> <address-indicator> <point-code> <subsystemnumber> <translation-type> <numbering-plan>
        // <nature-of-address-indicator> <digits>
        return createAddress(Integer.parseInt(options[index++]), Integer.parseInt(options[index++]), Integer.parseInt(options[index++]),
                Integer.parseInt(options[index++]), Integer.parseInt(options[index++]), Integer.parseInt(options[index++]),
                options[index++], isRule);
    }

    private SccpAddress createAddress(int ai, int pc, int ssn, int tt, int npValue, int naiValue, String digits, boolean isRule) throws Exception {
        SccpAddress sccpAddress = null;

        AddressIndicator aiObj = new AddressIndicator((byte) ai, SccpProtocolVersion.ITU);

        if (!isRule && pc <= 0) {
            throw new Exception(String.format("Point code parameter is mandatory and must be > 0"));
        }
        if (aiObj.getGlobalTitleIndicator() == null) {
            throw new Exception(String.format("GlobalTitle type is not recognizes, possible bad AddressIndicator value"));
        }

        NumberingPlan np = NumberingPlan.valueOf(npValue);
        NatureOfAddress nai = NatureOfAddress.valueOf(naiValue);

        GlobalTitle gt = null;

        switch (aiObj.getGlobalTitleIndicator()) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(digits, nai);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(digits, tt);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(digits, tt, np, null);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(digits, tt, np, null, nai);
                break;

            case NO_GLOBAL_TITLE_INCLUDED:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(digits);
                break;
        }

        sccpAddress = new SccpAddressImpl(aiObj.getRoutingIndicator(), gt, pc, ssn);

        return sccpAddress;
    }

    private String manageLmr(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            // sccp lmr create <id> <first-spc> <last-spc> <long-message-rule-type> stackname <stack-name>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int lmrId = Integer.parseInt(options[3]);
            int firstSpc = Integer.parseInt(options[4]);
            int lastSpc = Integer.parseInt(options[5]);

            LongMessageRuleType ruleType;
            String s1 = options[6].toLowerCase();
            if (s1.equals("udt")) {
                ruleType = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
            } else if (s1.equals("xudt")) {
                ruleType = LongMessageRuleType.XUDT_ENABLED;
            } else if (s1.equals("ludt")) {
                ruleType = LongMessageRuleType.LUDT_ENABLED;
            } else if (s1.equals("ludt_segm")) {
                ruleType = LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION;
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int count = 7;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().addLongMessageRule(lmrId, firstSpc, lastSpc, ruleType);
            return String.format(SccpOAMMessage.LMR_SUCCESSFULLY_ADDED, this.sccpStack.getName());
        } else if (command.equals("modify")) {
            // sccp lmr modify <id> <first-spc> <last-spc> <long-message-rule-type> stackname <stack-name>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int lmrId = Integer.parseInt(options[3]);
            int firstSpc = Integer.parseInt(options[4]);
            int lastSpc = Integer.parseInt(options[5]);

            LongMessageRuleType ruleType;
            String s1 = options[6].toLowerCase();
            if (s1.equals("udt")) {
                ruleType = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
            } else if (s1.equals("xudt")) {
                ruleType = LongMessageRuleType.XUDT_ENABLED;
            } else if (s1.equals("ludt")) {
                ruleType = LongMessageRuleType.LUDT_ENABLED;
            } else if (s1.equals("ludt_segm")) {
                ruleType = LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION;
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int count = 7;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().modifyLongMessageRule(lmrId, firstSpc, lastSpc, ruleType);
            return String.format(SccpOAMMessage.LMR_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp lmr delete <id> stackname <stack-name>

            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int lmrId = Integer.parseInt(options[3]);
            int count = 4;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().removeLongMessageRule(lmrId);

            return String.format(SccpOAMMessage.LMR_SUCCESSFULLY_DELETED, this.sccpStack.getName());
        } else if (command.equals("show")) {

            int count = 3;
            int lmrId = -1;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    lmrId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (lmrId != -1) {
                LongMessageRule lmr = this.sccpStack.getRouter().getLongMessageRule(lmrId);
                if (lmr == null) {
                    return String.format(SccpOAMMessage.LMR_DOESNT_EXIST, this.sccpStack.getName());
                }
                return lmr.toString();
            }

            if (this.sccpStack.getRouter().getLongMessageRules().size() == 0) {
                return String.format(SccpOAMMessage.LMR_DOESNT_EXIST, this.sccpStack.getName());
            }

            Map<Integer, LongMessageRule> idVsLngmsgRule = this.sccpStack.getRouter().getLongMessageRules();

            StringBuffer sb = new StringBuffer();
            for (Integer e : idVsLngmsgRule.keySet()) {
                LongMessageRule lmr = idVsLngmsgRule.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(lmr);
                sb.append("\n");
            }
            return sb.toString();
        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageSap(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            // sccp sap create <id> <mtp3-id> <opc> <ni> stackname <stack-name> networkid <networkId> localgtdigits <localGtDigits>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int sapId = Integer.parseInt(options[3]);

            int mtp3Id = Integer.parseInt(options[4]);
            int opc = Integer.parseInt(options[5]);
            int ni = Integer.parseInt(options[6]);

            int count = 7;
            int networkId = 0;
            String localGtDigits = "";

            while (count < options.length - 1) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("networkid")) {
                    String networkIdS = options[count++];
                    networkId = Integer.parseInt(networkIdS);
                } else if (key.equals("localgtdigits")) {
                    localGtDigits = options[count++];
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().addMtp3ServiceAccessPoint(sapId, mtp3Id, opc, ni, networkId, localGtDigits);

            return String.format(SccpOAMMessage.SAP_SUCCESSFULLY_ADDED, this.sccpStack.getName());
        } else if (command.equals("modify")) {
            // sccp sap modify <id> <mtp3-id> <opc> <ni> stackname <stack-named> networkid <networkId> localgtdigits <localGtDigits>
            if (options.length < 7) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int sapId = Integer.parseInt(options[3]);

            int mtp3Id = Integer.parseInt(options[4]);
            int opc = Integer.parseInt(options[5]);
            int ni = Integer.parseInt(options[6]);

            int count = 7;
            int networkId = 0;
            String localGtDigits = "";

            while (count < options.length - 1) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("networkid")) {
                    String networkIdS = options[count++];
                    networkId = Integer.parseInt(networkIdS);
                } else if (key.equals("localgtdigits")) {
                    localGtDigits = options[count++];
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().modifyMtp3ServiceAccessPoint(sapId, mtp3Id, opc, ni, networkId, localGtDigits);

            return String.format(SccpOAMMessage.SAP_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp sap delete <id> stackname <stack-name>
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int sapId = Integer.parseInt(options[3]);

            int count = 4;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().removeMtp3ServiceAccessPoint(sapId);

            return String.format(SccpOAMMessage.SAP_SUCCESSFULLY_DELETED, this.sccpStack.getName());
        } else if (command.equals("show")) {
            // sccp sap show id <id> stackname <stack-name>

            int count = 3;
            int sapId = -1;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    sapId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (sapId != -1) {
                Mtp3ServiceAccessPoint sap = this.sccpStack.getRouter().getMtp3ServiceAccessPoint(sapId);
                if (sap == null) {
                    return String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName());
                }
                return sap.toString();
            }

            if (this.sccpStack.getRouter().getMtp3ServiceAccessPoints().size() == 0) {
                return String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName());
            }

            Map<Integer, Mtp3ServiceAccessPoint> idVsMtp3SerAccPt = this.sccpStack.getRouter().getMtp3ServiceAccessPoints();
            StringBuffer sb = new StringBuffer();
            for (Integer e : idVsMtp3SerAccPt.keySet()) {
                Mtp3ServiceAccessPoint sap = idVsMtp3SerAccPt.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(sap);
                sb.append("\n");
            }
            return sb.toString();
        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageDest(String[] options) throws Exception {
        // Minimum 4 needed. Show
        if (options.length < 4) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            // sccp dest create <sap-id> <id> <first-dpc> <last-dpc> <first-sls> <lastsls> <sls-mask> stackname <stack-name>
            if (options.length < 10) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int sapId = Integer.parseInt(options[3]);

            int destId = Integer.parseInt(options[4]);

            int firstDpc = Integer.parseInt(options[5]);
            int lastDpc = Integer.parseInt(options[6]);
            int firstSls = Integer.parseInt(options[7]);
            int lastSls = Integer.parseInt(options[8]);
            int slsMask = Integer.parseInt(options[9]);

            int count = 10;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().addMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
            return String.format(SccpOAMMessage.DEST_SUCCESSFULLY_ADDED, this.sccpStack.getName());
        } else if (command.equals("modify")) {
            // sccp dest modify <sap-id> <id> <first-dpc> <last-dpc> <first-sls> <lastsls> <sls-mask> stackname <stack-name>
            if (options.length < 10) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int sapId = Integer.parseInt(options[3]);
            int destId = Integer.parseInt(options[4]);
            int firstDpc = Integer.parseInt(options[5]);
            int lastDpc = Integer.parseInt(options[6]);
            int firstSls = Integer.parseInt(options[7]);
            int lastSls = Integer.parseInt(options[8]);
            int slsMask = Integer.parseInt(options[9]);

            int count = 10;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().modifyMtp3Destination(sapId, destId, firstDpc, lastDpc, firstSls, lastSls, slsMask);
            return String.format(SccpOAMMessage.DEST_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp dest delete <sap-id> <id> stackname <stack-name>
            if (options.length < 5) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int sapId = Integer.parseInt(options[3]);
            int destId = Integer.parseInt(options[4]);

            int count = 5;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getRouter().removeMtp3Destination(sapId, destId);

            return String.format(SccpOAMMessage.DEST_SUCCESSFULLY_DELETED, this.sccpStack.getName());
        } else if (command.equals("show")) {
            // sccp dest show <sap-id> id <id> stackname <stack-name>

            int sapId = Integer.parseInt(options[3]);
            int destId = -1;
            int count = 4;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    destId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (destId != -1) {
                Mtp3ServiceAccessPoint sap = this.sccpStack.getRouter().getMtp3ServiceAccessPoint(sapId);
                if (sap == null) {
                    return String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName());
                }
                Mtp3Destination dest = sap.getMtp3Destination(destId);
                if (dest == null) {
                    return String.format(SccpOAMMessage.DEST_DOESNT_EXIST, this.sccpStack.getName());
                }

                return dest.toString();
            }

            Mtp3ServiceAccessPoint sap = this.sccpStack.getRouter().getMtp3ServiceAccessPoint(sapId);
            if (sap == null) {
                return String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName());
            }
            return sap.toString();

        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageConcernedSpc(String[] options) throws Exception {
        // Minimum 3 needed. Show
        if (options.length < 3) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        String command = options[2];

        if (command == null) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (command.equals("create")) {
            // sccp csp create <id> <spc> stackname <stack-name>
            if (options.length < 5) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int concernedSpcId = Integer.parseInt(options[3]);
            int conSpc = Integer.parseInt(options[4]);

            int count = 5;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().addConcernedSpc(concernedSpcId, conSpc);

            return String.format(SccpOAMMessage.CS_SUCCESSFULLY_ADDED, this.sccpStack.getName());
            // sccp csp modify <id> <spc> stackanme <stack-name>
        } else if (command.equals("modify")) {
            if (options.length < 5) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            int concernedSpcId = Integer.parseInt(options[3]);
            if (this.sccpStack.getSccpResource().getConcernedSpc(concernedSpcId) == null) {
                return String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName());
            }

            int conSpc = Integer.parseInt(options[4]);

            int count = 5;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().modifyConcernedSpc(concernedSpcId, conSpc);

            return String.format(SccpOAMMessage.CS_SUCCESSFULLY_MODIFIED, this.sccpStack.getName());
        } else if (command.equals("delete")) {
            // sccp csp delete <id> stackname <stack-name>
            if (options.length < 4) {
                return SccpOAMMessage.INVALID_COMMAND;
            }
            int concernedSpcId = Integer.parseInt(options[3]);

            int count = 4;

            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            this.sccpStack.getSccpResource().removeConcernedSpc(concernedSpcId);
            return String.format(SccpOAMMessage.CS_SUCCESSFULLY_DELETED, this.sccpStack.getName());
        } else if (command.equals("show")) {
            // sccp csp show id <id> stackname <stack-name>

            int count = 3;
            int concernedSpcId = -1;
            while (count < options.length) {
                String key = options[count++];
                if (key == null) {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

                if (key.equals("stackname")) {
                    String m3uaStackName = options[count++];

                    SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                    if (sccpStaclImpl == null) {
                        return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                    }

                    this.sccpStack = sccpStaclImpl;
                } else if (key.equals("id")) {
                    concernedSpcId = Integer.parseInt(options[count++]);
                } else {
                    return SccpOAMMessage.INVALID_COMMAND;
                }

            }

            this.setDefaultValue();

            if (concernedSpcId != -1) {
                ConcernedSignalingPointCode conSpc = this.sccpStack.getSccpResource().getConcernedSpc(concernedSpcId);
                if (conSpc == null) {
                    return String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName());
                }
                return conSpc.toString();
            }

            if (this.sccpStack.getSccpResource().getConcernedSpcs().size() == 0) {
                return String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName());
            }

            StringBuffer sb = new StringBuffer();

            Map<Integer, ConcernedSignalingPointCode> idvsCsp = this.sccpStack.getSccpResource().getConcernedSpcs();
            for (Integer e : idvsCsp.keySet()) {
                ConcernedSignalingPointCode ConcSpc = idvsCsp.get(e);
                sb.append("key=");
                sb.append(e);
                sb.append("  ");
                sb.append(ConcSpc);
                sb.append("\n");
            }
            return sb.toString();
        }

        return SccpOAMMessage.INVALID_COMMAND;
    }

    private String manageSet(String[] options) throws Exception {
        // Minimum 4 needed. Show

        // sccp set <command> <value> stackname <stack-name>
        if (options.length < 4) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        int count = 4;

        while (count < options.length) {
            String key = options[count++];
            if (key == null) {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            if (key.equals("stackname")) {
                String m3uaStackName = options[count++];

                SccpStackImpl sccpStaclImpl = this.sccpStacks.get(m3uaStackName);

                if (sccpStaclImpl == null) {
                    return String.format(SccpOAMMessage.NO_SCCP_MANAGEMENT_BEAN_FOR_NAME, m3uaStackName);
                }

                this.sccpStack = sccpStaclImpl;
            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }

        }

        this.setDefaultValue();

        String parName = options[2].toLowerCase();

        if (parName.equals("zmarginxudtmessage")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setZMarginXudtMessage(val);
        } else if (parName.equals("reassemblytimerdelay")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setReassemblyTimerDelay(val);
        } else if (parName.equals("maxdatamessage")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setMaxDataMessage(val);
        } else if (parName.equals("periodoflogging")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setPeriodOfLogging(val);
        } else if (parName.equals("removespc")) {
            boolean val = Boolean.parseBoolean(options[3]);
            this.sccpStack.setRemoveSpc(val);
        } else if (parName.equals("previewmode")) {
            boolean val = Boolean.parseBoolean(options[3]);
            this.sccpStack.setPreviewMode(val);
        } else if (parName.equals("ssttimerduration_min")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setSstTimerDuration_Min(val);
        } else if (parName.equals("ssttimerduration_max")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setSstTimerDuration_Max(val);
        } else if (parName.equals("ssttimerduration_increasefactor")) {
            double val = Double.parseDouble(options[3]);
            this.sccpStack.setSstTimerDuration_IncreaseFactor(val);
        } else if (parName.equals("sccpprotocolversion")) {
            SccpProtocolVersion spv = Enum.valueOf(SccpProtocolVersion.class, options[3]);
            if (spv != null)
                this.sccpStack.setSccpProtocolVersion(spv);

        } else if (parName.equals("cc_timer_a")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setCongControlTIMER_A(val);
        } else if (parName.equals("cc_timer_d")) {
            int val = Integer.parseInt(options[3]);
            this.sccpStack.setCongControlTIMER_D(val);
        } else if (parName.equals("cc_algo")) {
            String vals = options[3];
            SccpCongestionControlAlgo algo = Enum.valueOf(SccpCongestionControlAlgo.class, vals);
            this.sccpStack.setCongControl_Algo(algo);
        } else if (parName.equals("cc_blockingoutgoungsccpmessages")) {
            boolean valb = Boolean.parseBoolean(options[3]);
            this.sccpStack.setCongControl_blockingOutgoungSccpMessages(valb);

        } else {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        return String.format(SccpOAMMessage.PARAMETER_SUCCESSFULLY_SET, this.sccpStack.getName());
    }

    private String manageGet(String[] options) throws Exception {
        // Minimum 2 needed. Show
        if (options.length < 2) {
            return SccpOAMMessage.INVALID_COMMAND;
        }

        if (options.length == 3) {
            this.setDefaultValue();
            String parName = options[2].toLowerCase();

            StringBuilder sb = new StringBuilder();
            sb.append(options[2]);
            sb.append(" = ");
            if (parName.equals("zmarginxudtmessage")) {
                sb.append(this.sccpStack.getZMarginXudtMessage());
            } else if (parName.equals("reassemblytimerdelay")) {
                sb.append(this.sccpStack.getReassemblyTimerDelay());
            } else if (parName.equals("maxdatamessage")) {
                sb.append(this.sccpStack.getMaxDataMessage());
            } else if (parName.equals("periodoflogging")) {
                sb.append(this.sccpStack.getPeriodOfLogging());
            } else if (parName.equals("removespc")) {
                sb.append(this.sccpStack.isRemoveSpc());
            } else if (parName.equals("previewmode")) {
                sb.append(this.sccpStack.isPreviewMode());
            } else if (parName.equals("ssttimerduration_min")) {
                sb.append(this.sccpStack.getSstTimerDuration_Min());
            } else if (parName.equals("ssttimerduration_max")) {
                sb.append(this.sccpStack.getSstTimerDuration_Max());
            } else if (parName.equals("ssttimerduration_increasefactor")) {
                sb.append(this.sccpStack.getSstTimerDuration_IncreaseFactor());
            } else if (parName.equals("sccpprotocolversion")) {
                sb.append(this.sccpStack.getSccpProtocolVersion());

            } else if (parName.equals("cc_timer_a")) {
                sb.append(this.sccpStack.getCongControlTIMER_A());
            } else if (parName.equals("cc_timer_d")) {
                sb.append(this.sccpStack.getCongControlTIMER_D());
            } else if (parName.equals("cc_algo")) {
                sb.append(this.sccpStack.getCongControl_Algo());
            } else if (parName.equals("cc_blockingoutgoungsccpmessages")) {
                sb.append(this.sccpStack.isCongControl_blockingOutgoungSccpMessages());

            } else {
                return SccpOAMMessage.INVALID_COMMAND;
            }

            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            for (FastMap.Entry<String, SccpStackImpl> e = this.sccpStacks.head(), end = this.sccpStacks.tail(); (e = e
                    .getNext()) != end;) {

                SccpStackImpl managementImplTmp = e.getValue();
                String stackname = e.getKey();

                sb.append("Properties for ");
                sb.append(stackname);
                sb.append("\n");
                sb.append("*******************");
                sb.append("\n");

                sb.append("zMarginXudtMessage = ");
                sb.append(managementImplTmp.getZMarginXudtMessage());
                sb.append("\n");

                sb.append("reassemblyTimerDelay = ");
                sb.append(managementImplTmp.getReassemblyTimerDelay());
                sb.append("\n");

                sb.append("maxDataMessage = ");
                sb.append(managementImplTmp.getMaxDataMessage());
                sb.append("\n");

                sb.append("periodOfLogging = ");
                sb.append(managementImplTmp.getPeriodOfLogging());
                sb.append("\n");

                sb.append("removeSpc = ");
                sb.append(managementImplTmp.isRemoveSpc());
                sb.append("\n");

                sb.append("previewMode = ");
                sb.append(managementImplTmp.isPreviewMode());
                sb.append("\n");

                sb.append("sstTimerDuration_Min = ");
                sb.append(managementImplTmp.getSstTimerDuration_Min());
                sb.append("\n");

                sb.append("sstTimerDuration_Max = ");
                sb.append(managementImplTmp.getSstTimerDuration_Max());
                sb.append("\n");

                sb.append("sstTimerDuration_IncreaseFactor = ");
                sb.append(managementImplTmp.getSstTimerDuration_IncreaseFactor());
                sb.append("\n");

                sb.append("sccpprotocolversion = ");
                sb.append(managementImplTmp.getSccpProtocolVersion());
                sb.append("\n");

                sb.append("cc_timer_a = ");
                sb.append(managementImplTmp.getCongControlTIMER_A());
                sb.append("\n");

                sb.append("cc_timer_d = ");
                sb.append(managementImplTmp.getCongControlTIMER_D());
                sb.append("\n");

                sb.append("cc_algo = ");
                sb.append(managementImplTmp.getCongControl_Algo());
                sb.append("\n");

                sb.append("cc_blockingoutgoungsccpmessages = ");
                sb.append(managementImplTmp.isCongControl_blockingOutgoungSccpMessages());
                sb.append("\n");

                sb.append("*******************");
                sb.append("\n");
                sb.append("\n");
            }

            return sb.toString();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang. String)
     */
    @Override
    public boolean handles(String command) {
        return command.startsWith("sccp");
    }
}
