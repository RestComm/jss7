package org.mobicents.ss7;

import org.apache.log4j.Logger;
import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.LinksetManager;
import org.mobicents.ss7.management.console.LinksetListener;

/**
 * 
 * @author amit bhayani
 * 
 */
public class LinksetListenerImpl implements LinksetListener {

    private static final Logger logger = Logger
            .getLogger(LinksetListenerImpl.class);

    private LinksetManager linksetManager = null;

    protected LinksetListenerImpl(LinksetManager linksetManager) {
        this.linksetManager = linksetManager;
    }

    public String execute(String[] options) {

        try {
            // Atleast 1 option is passed?
            if (options == null || options.length < 2) {
                return LinkOAMMessages.INVALID_COMMAND;
            }

            String firstOption = options[1];

            if (firstOption == null) {
                return LinkOAMMessages.INVALID_COMMAND;
            }

            if (firstOption.compareTo("create") == 0) {
                // Create Linkset
                return this.linksetManager.createLinkset(options);
            } else if (firstOption.compareTo("delete") == 0) {
                // Delete Linkset
                return this.linksetManager.deleteLinkset(options);

            } else if (firstOption.compareTo("activate") == 0) {
                // noshutdown Linkset
                return this.linksetManager.activateLinkset(options);

            } else if (firstOption.compareTo("deactivate") == 0) {
                // shutdown Linkset
                return this.linksetManager.deactivateLinkset(options);

            } else if (firstOption.compareTo("link") == 0) {
                // Operation for Link

                // Check do we have more options
                if (options.length < 3) {
                    return LinkOAMMessages.INVALID_COMMAND;
                }

                // Check if its create, delete, shutdown or noshutdown
                firstOption = options[2];
                if (firstOption == null) {
                    return LinkOAMMessages.INVALID_COMMAND;
                }

                if (firstOption.compareTo("create") == 0) {
                    return this.linksetManager.createLink(options);
                } else if (firstOption.compareTo("delete") == 0) {
                    return this.linksetManager.deleteLink(options);
                } else if (firstOption.compareTo("deactivate") == 0) {
                    return this.linksetManager.deactivateLink(options);
                } else if (firstOption.compareTo("activate") == 0) {
                    return this.linksetManager.activateLink(options);
                }
            }
        } catch (Exception e) {
            logger.error("Error while executing command ", e);
            return e.toString();
        } catch (Throwable t){
            return t.toString();
        }
        return LinkOAMMessages.INVALID_COMMAND;
    }
}
