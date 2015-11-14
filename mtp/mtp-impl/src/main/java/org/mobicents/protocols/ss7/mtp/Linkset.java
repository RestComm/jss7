/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.mtp;

/**
 * Implements relation between link code and signaling link selection indicator.
 *
 * @author kulikov
 */
public class Linkset {
    /** The list of links. Maximum available 16 links */
    private Mtp2[] links = new Mtp2[16];
    private int count;

    /** The relation between sls and link */
    private int[] map = new int[16];

    /**
     * Adds link to this link set.
     *
     * @param link the link to add
     */
    public void add(Mtp2 link) {
        // add link at the first empty place
        for (int i = 0; i < links.length; i++) {
            if (links[i] == null) {
                links[i] = link;
                break;
            }
        }
        count++;
        remap();
    }

    /**
     * Removes links from linkset.
     *
     * @param link the link to remove.
     */
    public void remove(Mtp2 link) {
        for (int i = 0; i < links.length; i++) {
            if (links[i] == link) {
                links[i] = null;
                break;
            }
        }
        count--;
        remap();
    }

    /**
     * Gets the state of the link.
     *
     * @return true if linkset has at least one active link.
     */
    public boolean isActive() {
        return count > 0;
    }

    /**
     * Selects the link using specified link selection indicator.
     *
     * @param sls signaling link selection indicator.
     * @return
     */
    public Mtp2 select(byte sls) {
        return links[map[sls]];
    }

    /**
     * This method is called each time when number of links has changed to reestablish relation between link selection indicator
     * and link
     */
    private void remap() {
        int k = -1;
        for (int i = 0; i < map.length; i++) {
            boolean found = false;

            for (int j = k + 1; j < links.length; j++) {
                if (links[j] != null) {
                    found = true;
                    k = j;
                    map[i] = k;
                    break;
                }
            }

            if (found) {
                continue;
            }

            for (int j = 0; j < k; j++) {
                if (links[j] != null) {
                    found = true;
                    k = j;
                    map[i] = k;
                    break;
                }
            }

            if (!found) {
                map[i] = 0;
            }
        }
    }
}
