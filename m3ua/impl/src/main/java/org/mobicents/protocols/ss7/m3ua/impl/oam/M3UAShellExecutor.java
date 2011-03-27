/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.Sgp;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 * 
 * @author amit bhayani
 *
 */
public class M3UAShellExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(M3UAShellExecutor.class);

    private Sgp sgp;

    public M3UAShellExecutor() {

    }

    public Sgp getSgp() {
        return sgp;
    }

    public void setSgp(Sgp sgp) {
        this.sgp = sgp;
    }

    public String execute(String[] args) {
        try {
            if (args.length < 4) {
                // any command will have atleast 4 args
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            if (args[1] == null) {
                return M3UAOAMMessages.INVALID_COMMAND;
            }

            // args[0] is m3ua
            if (args[1].compareTo("ras") == 0) {
                // related to rem AS for SigGatewayImpl
                String rasCmd = args[2];
                if (rasCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                if (rasCmd.compareTo("create") == 0) {
                    // Create new Rem AS
                    As as = this.sgp.createAppServer(args);
                    return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
                } else if (rasCmd.compareTo("add") == 0) {
                    // Add Rem ASP to Rem AS
                    if (args[3] == null || args[4] == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sgp.assignAspToAs(args[3], args[4]);
                    return String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, args[4], args[3]);
                }
                return M3UAOAMMessages.INVALID_COMMAND;
            } else if (args[1].compareTo("rasp") == 0) {
                // related to rem AS for SigGatewayImpl
                String raspCmd = args[2];

                if (raspCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                if (raspCmd.compareTo("create") == 0) {
                    // Create new Rem ASP
                    AspFactory factory = this.sgp.createAspFactory(args);
                    return String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, factory.getName());
                }
                return M3UAOAMMessages.INVALID_COMMAND;
            } else if (args[1].compareTo("as") == 0) {
                // related to rem AS for SigGatewayImpl
                String rasCmd = args[2];
                if (rasCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                if (rasCmd.compareTo("create") == 0) {
                    // Create new Rem AS
                    As as = this.sgp.createAppServer(args);
                    return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
                } else if (rasCmd.compareTo("add") == 0) {
                    // Add Rem ASP to Rem AS
                    if (args[3] == null || args[4] == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sgp.assignAspToAs(args[3], args[4]);
                    return String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, args[4], args[3]);
                }
                return M3UAOAMMessages.INVALID_COMMAND;
            } else if (args[1].compareTo("asp") == 0) {
                // related to rem AS for SigGatewayImpl
                String raspCmd = args[2];

                if (raspCmd == null) {
                    return M3UAOAMMessages.INVALID_COMMAND;
                }

                if (raspCmd.compareTo("create") == 0) {
                    // Create new ASP
                    AspFactory factory = this.sgp.createAspFactory(args);
                    return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, factory.getName());
                }
                
                if (raspCmd.compareTo("start") == 0) {
                    String aspName = args[3];
                    this.sgp.startAsp(aspName);
                    return String.format(M3UAOAMMessages.ASP_START_SUCESSFULL, aspName);
                }
                
                if (raspCmd.compareTo("stop") == 0) {
                    String aspName = args[3];
                    this.sgp.stopAsp(aspName);
                    return String.format(M3UAOAMMessages.ASP_STOP_SUCESSFULL, aspName);
                }                
                
                return M3UAOAMMessages.INVALID_COMMAND;
            }
            return M3UAOAMMessages.INVALID_COMMAND;
        } catch (Exception e) {
            logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), e);
            return e.getMessage();
        }
    }

}
