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

package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.AsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 *
 * @author amit bhayani
 *
 */
public class M3UAShellExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(M3UAShellExecutor.class);

    private M3UAManagementImpl m3uaManagement;

    protected ParameterFactory parameterFactory = new ParameterFactoryImpl();

    public M3UAShellExecutor() {

    }

    public M3UAManagementImpl getM3uaManagement() {
        return m3uaManagement;
    }

    public void setM3uaManagement(M3UAManagementImpl m3uaManagement) {
        this.m3uaManagement = m3uaManagement;
    }

    /**
     * m3ua as create <as-name> <AS | SGW | IPSP> mode <SE | DE> ipspType < client | server > rc <routing-context> traffic-mode
     * <traffic mode> min-asp <minimum asp active for TrafficModeType.Loadshare> network-appearance <network appearance>
     *
     * @param args
     * @return
     */
    private String createAs(String[] args) throws Exception {
        if (args.length < 5 || args.length > 17) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        // Create new Rem AS
        String asName = args[3];
        if (asName == null) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        Functionality functionlaity = Functionality.getFunctionality(args[4]);
        ExchangeType exchangeType = null;
        IPSPType ipspType = null;
        RoutingContext rc = null;
        TrafficModeType trafficModeType = null;
        NetworkAppearance na = null;

        if (functionlaity == null) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        int count = 5;
        int minAspActiveForLoadbalance = 1;

        while (count < args.length) {
            String key = args[count++];
            if (key == null) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (key.equals("mode")) {
                exchangeType = ExchangeType.getExchangeType(args[count++]);
                if (exchangeType == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }
            } else if (key.equals("ipspType")) {
                ipspType = IPSPType.getIPSPType(args[count++]);
            } else if (key.equals("rc")) {
                long rcLong = Long.parseLong(args[count++]);
                rc = parameterFactory.createRoutingContext(new long[] { rcLong });
            } else if (key.equals("traffic-mode")) {
                trafficModeType = getTrafficModeType(args[count++]);
            } else if (key.equals("network-appearance")) {
                na = parameterFactory.createNetworkAppearance(Long.parseLong(args[count++]));
            } else if (key.equals("min-asp")) {
                minAspActiveForLoadbalance = Integer.parseInt(args[count++]);
            } else {
                return M3UAOAMMessages.INVALID_COMMAND;
            }
        }

        As asImpl = this.m3uaManagement.createAs(asName, functionlaity, exchangeType, ipspType, rc, trafficModeType,
                minAspActiveForLoadbalance, na);
        return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, asImpl.getName());
    }

    /**
     * m3ua as destroy <as-name>
     *
     * @param args
     * @return
     * @throws Exception
     */
    private String destroyAs(String[] args) throws Exception {
        if (args.length < 4) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        String asName = args[3];
        if (asName == null) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        AsImpl asImpl = this.m3uaManagement.destroyAs(asName);

        return String.format(M3UAOAMMessages.DESTROY_AS_SUCESSFULL, asName);
    }

    /**
     * m3ua as add <as-name> <asp-name>
     *
     * @param args
     * @return
     * @throws Exception
     */
    private String addAspToAs(String[] args) throws Exception {
        if (args.length < 5) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        // Add Rem ASP to Rem AS
        if (args[3] == null || args[4] == null) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        this.m3uaManagement.assignAspToAs(args[3], args[4]);
        return String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, args[4], args[3]);
    }

    /**
     * m3ua as remove <as-name> <asp-name>
     *
     * @param args
     * @return
     * @throws Exception
     */
    private String removeAspFromAs(String[] args) throws Exception {
        if (args.length < 5) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        // Add Rem ASP to Rem AS
        if (args[3] == null || args[4] == null) {
            return M3UAOAMMessages.INVALID_COMMAND;
        }

        this.m3uaManagement.unassignAspFromAs(args[3], args[4]);
        return String.format(M3UAOAMMessages.REMOVE_ASP_FROM_AS_SUCESSFULL, args[4], args[3]);
    }

    private TrafficModeType getTrafficModeType(String mode) {
        int iMode = -1;
        if (mode == null) {
            return null;
        } else if (mode.equals("loadshare")) {
            iMode = TrafficModeType.Loadshare;
        } else if (mode.equals("override")) {
            iMode = TrafficModeType.Override;
        } else if (mode.equals("broadcast")) {
            iMode = TrafficModeType.Broadcast;
        } else {
            return null;
        }

        return this.parameterFactory.createTrafficModeType(iMode);
    }

    private String showAspFactories() {
        List<AspFactory> aspfactories = this.m3uaManagement.getAspfactories();
        if (aspfactories.size() == 0) {
            return M3UAOAMMessages.NO_ASP_DEFINED_YET;
        }
        StringBuffer sb = new StringBuffer();
        for (AspFactory aspFactory : aspfactories) {
            AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) aspFactory;
            sb.append(M3UAOAMMessages.NEW_LINE);
            aspFactoryImpl.show(sb);
            sb.append(M3UAOAMMessages.NEW_LINE);
        }
        return sb.toString();
    }

    private String showRoutes() {
        Map<String, As[]> route = this.m3uaManagement.getRoute();

        if (route.size() == 0) {
            return M3UAOAMMessages.NO_ROUTE_DEFINED_YET;
        }
        StringBuffer sb = new StringBuffer();

        Set<String> keys = route.keySet();
        for (String key : keys) {
            As[] asList = route.get(key);

            sb.append(M3UAOAMMessages.NEW_LINE);
            sb.append(key);
            sb.append(M3UAOAMMessages.TAB);
            for (int i = 0; i < asList.length; i++) {
                As asImpl = asList[i];
                if (asImpl != null) {
                    sb.append(asImpl.getName());
                    sb.append(M3UAOAMMessages.COMMA);
                }
            }
            sb.append(M3UAOAMMessages.NEW_LINE);
        }
        return sb.toString();
    }

    private String showAs() {
        List<As> appServers = this.m3uaManagement.getAppServers();
        if (appServers.size() == 0) {
            return M3UAOAMMessages.NO_AS_DEFINED_YET;
        }
        StringBuffer sb = new StringBuffer();
        for (As as : appServers) {
            AsImpl asImpl = (AsImpl) as;
            sb.append(M3UAOAMMessages.NEW_LINE);
            asImpl.show(sb);
            sb.append(M3UAOAMMessages.NEW_LINE);
        }
        return sb.toString();
    }

    private String executeM3UA(String[] args) {
        try {
            if (args.length < 3 || args.length > 15) {
                // any command will have atleast 3 args
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1] == null) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1].equals("as")) {
                // related to rem AS for SigGatewayImpl
                String rasCmd = args[2];
                if (rasCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                if (rasCmd.equals("create")) {
                    return this.createAs(args);
                } else if (rasCmd.equals("destroy")) {
                    return this.destroyAs(args);
                } else if (rasCmd.equals("add")) {
                    return this.addAspToAs(args);
                } else if (rasCmd.equals("remove")) {
                    return this.removeAspFromAs(args);
                } else if (rasCmd.equals("show")) {
                    return this.showAs();
                }
                return M3UAOAMMessages.INVALID_COMMAND;
            } else if (args[1].equals("asp")) {

                if (args.length < 3 || args.length > 9) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                // related to rem AS for SigGatewayImpl
                String raspCmd = args[2];

                if (raspCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                } else if (raspCmd.equals("create")) {
                    // Create new ASP
                    if (args.length < 5) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String aspname = args[3];
                    String assocName = args[4];

                    if (aspname == null || assocName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    AspFactory factory = null;
                    if (args.length == 5) {
                        factory = this.m3uaManagement.createAspFactory(aspname, assocName, false);
                    } else {
                        int count = 5;
                        long aspid = -1;
                        boolean isHeartBeatEnabled = false;
                        while (count < args.length) {
                            String key = args[count++];
                            if (key == null) {
                                return M3UAOAMMessages.INVALID_COMMAND;
                            }

                            if (key.equals("aspid")) {
                                aspid = Long.parseLong(args[count++]);
                            } else if (key.equals("heartbeat")) {
                                isHeartBeatEnabled = Boolean.parseBoolean(args[count++]);
                            } else {
                                return M3UAOAMMessages.INVALID_COMMAND;
                            }
                        }
                        if(aspid == -1){
                            factory = this.m3uaManagement.createAspFactory(aspname, assocName, isHeartBeatEnabled);
                        } else {
                            factory = this.m3uaManagement.createAspFactory(aspname, assocName, aspid, isHeartBeatEnabled);
                        }
                    }
                    return String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, factory.getName());
                } else if (raspCmd.equals("destroy")) {
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String aspName = args[3];
                    this.m3uaManagement.destroyAspFactory(aspName);
                    return String.format(M3UAOAMMessages.DESTROY_ASP_SUCESSFULL, aspName);

                } else if (raspCmd.equals("show")) {
                    return this.showAspFactories();

                } else if (raspCmd.equals("start")) {
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String aspName = args[3];
                    this.m3uaManagement.startAsp(aspName);
                    return String.format(M3UAOAMMessages.ASP_START_SUCESSFULL, aspName);
                } else if (raspCmd.equals("stop")) {
                    if (args.length < 4) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    String aspName = args[3];
                    this.m3uaManagement.stopAsp(aspName);
                    return String.format(M3UAOAMMessages.ASP_STOP_SUCESSFULL, aspName);
                }

                return M3UAOAMMessages.INVALID_COMMAND;
            } else if (args[1].equals("route")) {

                String routeCmd = args[2];

                if (routeCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                if (routeCmd.equals("add")) {

                    if (args.length < 5 || args.length > 7) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    int count = 3;
                    String asName = args[count++];
                    int dpc = -1;
                    int opc = -1;
                    int si = -1;

                    if (asName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    dpc = Integer.parseInt(args[count++]);

                    while (count < args.length) {
                        opc = Integer.parseInt(args[count++]);
                        si = Integer.parseInt(args[count++]);
                    }

                    this.m3uaManagement.addRoute(dpc, opc, si, asName);

                    return String.format(M3UAOAMMessages.ADD_ROUTE_AS_FOR_DPC_SUCCESSFULL, asName, dpc);
                }

                if (routeCmd.equals("remove")) {

                    if (args.length < 5 || args.length > 7) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    int count = 3;
                    String asName = args[count++];
                    int dpc = -1;
                    int opc = -1;
                    int si = -1;

                    if (asName == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    dpc = Integer.parseInt(args[count++]);

                    while (count < args.length) {
                        opc = Integer.parseInt(args[count++]);
                        si = Integer.parseInt(args[count++]);
                    }

                    this.m3uaManagement.removeRoute(dpc, opc, si, asName);
                    return String.format(M3UAOAMMessages.REMOVE_AS_ROUTE_FOR_DPC_SUCCESSFULL, asName, dpc);
                }

                if (routeCmd.equals("show")) {
                    return this.showRoutes();

                }
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

    public String execute(String[] args) {
        if (args[0].equals("m3ua")) {
            return this.executeM3UA(args);
        }
        return M3UAOAMMessages.INVALID_COMMAND;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang. String)
     */
    @Override
    public boolean handles(String command) {
        return (command.startsWith("m3ua"));
    }

}
