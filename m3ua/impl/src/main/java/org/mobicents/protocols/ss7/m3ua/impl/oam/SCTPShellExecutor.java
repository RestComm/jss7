package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.Server;
import org.mobicents.ss7.management.console.ShellExecutor;

public class SCTPShellExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(SCTPShellExecutor.class);

    protected Management sctpManagement = null;

    public Management getSctpManagement() {
        return sctpManagement;
    }

    public void setSctpManagement(Management sctpManagement) {
        this.sctpManagement = sctpManagement;
    }

    private String showServers() {
        List<Server> servers = this.sctpManagement.getServers();
        if (servers.size() == 0) {
            return SCTPOAMMessages.NO_SERVER_DEFINED_YET;
        }

        StringBuffer sb = new StringBuffer();
        for (Server server : servers) {
            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append("SERVER ").append(server.getIpChannelType().getType()).append(" name=").append(server.getName())
                    .append(" started=").append(server.isStarted()).append(" hostIp=").append(server.getHostAddress())
                    .append(" hostPort=").append(server.getHostport());

            String[] secondaryHostAdd = server.getExtraHostAddresses();

            if (secondaryHostAdd != null && secondaryHostAdd.length > 0) {
                sb.append(" secondaryHost=");
                for (int i = 0; i < secondaryHostAdd.length; i++) {
                    sb.append(secondaryHostAdd[i]).append(" ");
                }
            }

            List<String> associations = server.getAssociations();
            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append("Associations:");
            for (String s : associations) {
                sb.append(M3UAOAMMessages.TAB);
                sb.append(s).append(M3UAOAMMessages.NEW_LINE);
            }
            sb.append(M3UAOAMMessages.NEW_LINE);
        }
        return sb.toString();
    }

    private String showAssociations() {
        Map<String, Association> associations = this.sctpManagement.getAssociations();
        if (associations.size() == 0) {
            return SCTPOAMMessages.NO_ASSOCIATION_DEFINED_YET;
        }

        StringBuffer sb = new StringBuffer();
        for (String s : associations.keySet()) {
            Association asso = associations.get(s);
            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append("ASSOCIATION ").append(asso.getIpChannelType().getType()).append(" name=").append(asso.getName())
                    .append(" started=").append(asso.isStarted()).append(" peerIp=").append(asso.getPeerAddress())
                    .append(" peerPort=").append(asso.getPeerPort());
            if (asso.getAssociationType() == AssociationType.CLIENT) {
                sb.append(" hostIp=").append(asso.getHostAddress()).append(" hostPort=").append(asso.getHostPort());
            } else {
                sb.append(" server=").append(asso.getServerName());
            }

            sb.append(" type=").append(asso.getAssociationType());

            String[] secondaryHostAdd = asso.getExtraHostAddresses();

            if (secondaryHostAdd != null && secondaryHostAdd.length > 0) {
                sb.append(" secondaryHost=");
                for (int i = 0; i < secondaryHostAdd.length; i++) {
                    sb.append(secondaryHostAdd[i]).append(" ");
                }
            }

            sb.append(M3UAOAMMessages.NEW_LINE);
        }

        return sb.toString();
    }

    @Override
    public String execute(String[] args) {
        try {
            if (args.length < 3 || args.length > 11) {
                // any command will have atleast 3 args
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1] == null) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1].equals("server")) {
                String command = args[2];

                if (command == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                } else if (command.equals("create")) {

                    if (args.length < 6 || args.length > 7) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String hostAddress = args[4];
                    if (hostAddress == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String[] hostAddresses = hostAddress.split(",");
                    String primaryAddress = hostAddresses[0];
                    String[] secondaryAddresses = null;

                    if (hostAddresses.length > 1) {
                        secondaryAddresses = new String[(hostAddresses.length - 1)];
                        for (int i = 0; i < secondaryAddresses.length; i++) {
                            secondaryAddresses[i] = hostAddresses[(i + 1)];
                        }
                    }

                    int hostPort = Integer.parseInt(args[5]);

                    IpChannelType ipChnnelType = null;

                    if (args.length > 6) {
                        ipChnnelType = IpChannelType.getInstance(args[6]);
                    }

                    if (ipChnnelType == null) {
                        ipChnnelType = IpChannelType.SCTP;
                    }

                    this.sctpManagement.addServer(serverName, primaryAddress, hostPort, ipChnnelType, secondaryAddresses);

                    return String.format(SCTPOAMMessages.ADD_SERVER_SUCCESS, serverName);

                } else if (command.equals("destroy")) {
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sctpManagement.removeServer(serverName);
                    return String.format(SCTPOAMMessages.REMOVE_SERVER_SUCCESS, serverName);

                } else if (command.equals("start")) {
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sctpManagement.startServer(serverName);
                    return String.format(SCTPOAMMessages.START_SERVER_SUCCESS, serverName);
                } else if (command.equals("stop")) {
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String serverName = args[3];
                    if (serverName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sctpManagement.stopServer(serverName);
                    return String.format(SCTPOAMMessages.STOP_SERVER_SUCCESS, serverName);
                } else if (command.equals("show")) {
                    return this.showServers();
                }

                return M3UAOAMMessages.INVALID_COMMAND;

            } else if (args[1].equals("association")) {
                String command = args[2];

                if (command == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                } else if (command.equals("create")) {
                    if (args.length < 8 || args.length > 10) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String assocName = args[3];
                    if (assocName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String type = args[4];
                    if (type == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    } else if (type.equals("CLIENT")) {
                        if (args.length < 9) {
                            return M3UAOAMMessages.INVALID_COMMAND;
                        }

                        String peerIp = args[5];
                        int peerPort = Integer.parseInt(args[6]);

                        String hostIp = args[7];

                        String[] hostAddresses = hostIp.split(",");
                        String primaryAddress = hostAddresses[0];
                        String[] secondaryAddresses = null;

                        if (hostAddresses.length > 1) {
                            secondaryAddresses = new String[(hostAddresses.length - 1)];
                            for (int i = 0; i < secondaryAddresses.length; i++) {
                                secondaryAddresses[i] = hostAddresses[(i + 1)];
                            }
                        }

                        int hostPort = Integer.parseInt(args[8]);

                        IpChannelType ipChnnelType = null;

                        if (args.length > 9) {
                            ipChnnelType = IpChannelType.getInstance(args[9]);
                        }

                        if (ipChnnelType == null) {
                            ipChnnelType = IpChannelType.SCTP;
                        }

                        this.sctpManagement.addAssociation(primaryAddress, hostPort, peerIp, peerPort, assocName, ipChnnelType,
                                secondaryAddresses);

                        return String.format(SCTPOAMMessages.ADD_CLIENT_ASSOCIATION_SUCCESS, assocName);
                    } else if (type.equals("SERVER")) {
                        String serverName = args[5];

                        String peerIp = args[6];
                        int peerPort = Integer.parseInt(args[7]);

                        IpChannelType ipChnnelType = null;

                        if (args.length > 8) {
                            ipChnnelType = IpChannelType.getInstance(args[8]);
                        }

                        if (ipChnnelType == null) {
                            ipChnnelType = IpChannelType.SCTP;
                        }

                        this.sctpManagement.addServerAssociation(peerIp, peerPort, serverName, assocName, ipChnnelType);
                        return String.format(SCTPOAMMessages.ADD_SERVER_ASSOCIATION_SUCCESS, assocName);
                    }

                    return M3UAOAMMessages.INVALID_COMMAND;

                } else if (command.equals("destroy")) {

                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String assocName = args[3];
                    if (assocName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sctpManagement.removeAssociation(assocName);
                    return String.format(SCTPOAMMessages.REMOVE_ASSOCIATION_SUCCESS, assocName);

                } else if (command.equals("show")) {
                    return this.showAssociations();
                }

                return M3UAOAMMessages.INVALID_COMMAND;
            }

            return M3UAOAMMessages.INVALID_COMMAND;
        } catch (Exception e) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), e);
            return e.toString();
        } catch (Throwable t) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), t);
            return t.toString();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang. String)
     */
    @Override
    public boolean handles(String command) {
        return command.startsWith("sctp");
    }

}
