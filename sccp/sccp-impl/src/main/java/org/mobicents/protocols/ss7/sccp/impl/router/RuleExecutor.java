package org.mobicents.protocols.ss7.sccp.impl.router;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;

/**
 * 
 * @author amit bhayani
 * 
 */
public class RuleExecutor {

    private static final Logger logger = Logger.getLogger(RuleExecutor.class);

    private RouterImpl router = null;

    public RuleExecutor() {

    }

    public RouterImpl getRouter() {
        return router;
    }

    public void setRouter(RouterImpl router) {
        this.router = router;
    }

    public String execute(String[] options) {
        if (this.router == null) {
            logger.warn("Router not set. Command will not be executed ");
            return RuleOAMMessage.SERVER_ERROR;
        }

        // Atleast 1 option is passed?
        if (options == null || options.length < 2) {
            return RuleOAMMessage.INVALID_COMMAND;
        }

        String firstOption = options[1];

        if (firstOption == null) {
            return LinkOAMMessages.INVALID_COMMAND;
        }

        try {
            if (firstOption.compareTo("show") == 0) {
                // Show
                return this.router.showRules();
            } else if (firstOption.compareTo("create") == 0) {
                // Create Rule
                return this.createRule(options);
            } else if (firstOption.compareTo("delete") == 0) {
                // Delete Rule
                return this.deleteRule(options);
            }
        } catch (Exception e) {
            logger.error("Error while executing command ", e);
            return e.toString();
        } catch (Throwable t) {
            return t.toString();
        }

        return RuleOAMMessage.INVALID_COMMAND;
    }

    /**
     * <p>
     * Command to create new rule.
     * </p>
     * <p>
     * The valid combination for a command are
     * <ul>
     * <li>
     * <p>
     * <i>pattern</i> and <i>translation</i>
     * </p>
     * </li>
     * <li>
     * <p>
     * <i>pattern</i>, <i>translation</i> and <i>mtpinfo</i>
     * </p>
     * </li>
     * <li>
     * <p>
     * <i>pattern</i> and <i>mtpinfo</i>
     * </p>
     * </li>
     * </ul>
     * </p>
     * <p>
     * To know more about these options look at
     * {@link org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl}
     * </p>
     * 
     * @param options
     * @return
     * @throws Exception
     */
    private String createRule(String[] options) throws Exception {
        // Minimum is 23
        if (options.length < 23) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        if (options[1].compareTo("create") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        String name = options[2];
        AddressInformation translation = null;
        AddressInformation pattern = null;
        MTPInfo mtpInfo = null;

        // Pattern parsing
        if (options[3].compareTo("pattern") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        pattern = getAddressInformation(options, 4);

        // Translation parsing
        if (options[14].compareTo("translation") == 0) {

            // For translation minimum length is 25
            if (options.length < 25) {
                throw new Exception(RuleOAMMessage.INVALID_COMMAND);
            }

            translation = getAddressInformation(options, 15);

            if (options.length > 25) {
                // We have MtpInfo also
                if (options.length < 34) {
                    // Hmmm, we don't have everything to parse mtpInfo
                    throw new Exception(RuleOAMMessage.INVALID_COMMAND);
                }

                if (options[25].compareTo("mtpinfo") != 0) {
                    throw new Exception(RuleOAMMessage.INVALID_COMMAND);
                }
                // Lets parse mtpInfo
                mtpInfo = this.getMTPInfo(options, 26);
            }

        } else if (options[14].compareTo("mtpinfo") == 0) {
            // Mtp Info Parsing
            mtpInfo = this.getMTPInfo(options, 15);
        } else {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        Rule rule = new Rule(name, pattern, translation, mtpInfo);

        return this.router.addRule(rule);

    }

    private AddressInformation getAddressInformation(String[] options,
            int offset) throws Exception {
        if (options[offset++].compareTo("tt") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        int tt = Integer.parseInt(options[offset++]);

        if (options[offset++].compareTo("np") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        NumberingPlan np = NumberingPlan.valueOf(Integer
                .parseInt(options[offset++]));

        if (options[offset++].compareTo("noa") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        NatureOfAddress noa = NatureOfAddress.valueOf(Integer
                .parseInt(options[offset++]));

        if (options[offset++].compareTo("digits") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        String digits = options[offset++];

        if (options[offset++].compareTo("ssn") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        int ssn = Integer.parseInt(options[offset++]);

        return new AddressInformation(tt, np, noa, digits, ssn);
    }

    private MTPInfo getMTPInfo(String[] options, int offSet) throws Exception {
        if (options[offSet++].compareTo("name") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        String mtpName = options[offSet++];

        if (options[offSet++].compareTo("opc") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        int opc = Integer.parseInt(options[offSet++]);

        if (options[offSet++].compareTo("apc") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        int dpc = Integer.parseInt(options[offSet++]);

        if (options[offSet++].compareTo("sls") != 0) {
            throw new Exception(RuleOAMMessage.INVALID_COMMAND);
        }

        int sls = Integer.parseInt(options[offSet++]);

        return new MTPInfo(mtpName, opc, dpc, sls);
    }

    /**
     * Delete the existing Rule. Command is sccprule delete <rule-name>. For
     * example sccprule delete Rule1
     * 
     * @param options
     * @return
     * @throws Exception
     */
    private String deleteRule(String[] options) throws Exception {

        if (options.length < 3) {
            return RuleOAMMessage.INVALID_COMMAND;
        }

        String name = options[2];

        if (name == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        return this.router.deleteRule(name);
    }
}
