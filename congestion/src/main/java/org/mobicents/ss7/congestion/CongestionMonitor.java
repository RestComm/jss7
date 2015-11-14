/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.ss7.congestion;

/**
 * The user who is interested in monitoring the underlying source should implement this interface.
 *
 * @author amit bhayani
 *
 */
public interface CongestionMonitor {

    /**
     * The user should call this method periodically to monitor underlying resource
     */
    void monitor();

    /**
     * Add {@link CongestionListener}
     *
     * @param listener
     */
    void addCongestionListener(CongestionListener listener);

    /**
     * Remove {@link CongestionListener}
     *
     * @param listener
     */
    void removeCongestionListener(CongestionListener listener);

    /**
     * Get the name of underlying source
     *
     * @return
     */
    String getSource();

}
