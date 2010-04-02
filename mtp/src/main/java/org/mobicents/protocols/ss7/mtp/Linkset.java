/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
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
        //add link at the first empty place
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
     * This method is called each time when number of links has changed
     * to reestablish relation between link selection indicator and link
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
