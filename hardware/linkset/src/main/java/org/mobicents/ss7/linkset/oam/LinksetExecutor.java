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

package org.mobicents.ss7.linkset.oam;

import org.apache.log4j.Logger;
import org.mobicents.ss7.management.console.ShellExecutor;

/**
 * Provides executor service for {@link LinksetManager}
 *
 * @author amit bhayani
 *
 */
public class LinksetExecutor implements ShellExecutor {

    private static final Logger logger = Logger.getLogger(LinksetExecutor.class);

    private LinksetManager linksetManager = null;

    public LinksetExecutor() {

    }

    public LinksetManager getLinksetManager() {
        return linksetManager;
    }

    public void setLinksetManager(LinksetManager linksetManager) {
        this.linksetManager = linksetManager;
    }

    public String execute(String[] options) {

        if (this.linksetManager == null) {
            logger.warn("LinksetManager not set. Command will not be executed ");
            return LinkOAMMessages.SERVER_ERROR;
        }

        try {
            // Atleast 1 option is passed?
            if (options == null || options.length < 2) {
                return LinkOAMMessages.INVALID_COMMAND;
            }

            String firstOption = options[1];

            if (firstOption == null) {
                return LinkOAMMessages.INVALID_COMMAND;
            }

            if (firstOption.compareTo("show") == 0) {
                // Show
                return this.linksetManager.showLinkset(options);
            } else if (firstOption.compareTo("create") == 0) {
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
        } catch (Throwable t) {
            return t.toString();
        }
        return LinkOAMMessages.INVALID_COMMAND;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.ShellExecutor#handles(java.lang.String)
     */
    @Override
    public boolean handles(String command) {
        return (command.startsWith("linkset"));
    }

}
