package org.mobicents.protocols.ss7.m3ua.impl.oam;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.SigGateway;
import org.mobicents.ss7.management.console.ShellExecutor;

public class M3UAShellExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(M3UAShellExecutor.class);

    private SigGateway sigGatewayImpl;
    private SigGateway remSigGatewayImpl;

    public M3UAShellExecutor() {

    }

    public SigGateway getSigGatewayImpl() {
        return sigGatewayImpl;
    }

    public void setSigGatewayImpl(SigGateway sigGatewayImpl) {
        this.sigGatewayImpl = sigGatewayImpl;
    }

    public SigGateway getRemSigGatewayImpl() {
        return remSigGatewayImpl;
    }

    public void setRemSigGatewayImpl(SigGateway remSigGatewayImpl) {
        this.remSigGatewayImpl = remSigGatewayImpl;
    }

    public String execute(String[] args) {
        try {
            if (args.length < 5) {
                // any command will have atleast 5 args
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
                    As as = this.sigGatewayImpl.createAppServer(args);
                    return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
                } else if (rasCmd.compareTo("add") == 0) {
                    // Add Rem ASP to Rem AS
                    if (args[3] == null || args[4] == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.sigGatewayImpl.assignAspToAs(args[3], args[4]);
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
                    AspFactory factory = this.sigGatewayImpl.createAspFactory(args);
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
                    As as = this.remSigGatewayImpl.createAppServer(args);
                    return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, as.getName());
                } else if (rasCmd.compareTo("add") == 0) {
                    // Add Rem ASP to Rem AS
                    if (args[3] == null || args[4] == null) {
                        return M3UAOAMMessages.INVALID_COMMAND;
                    }

                    this.remSigGatewayImpl.assignAspToAs(args[3], args[4]);
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
                    // Create new Rem ASP
                    AspFactory factory = this.remSigGatewayImpl.createAspFactory(args);
                    return String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, factory.getName());
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
