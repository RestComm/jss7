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
 * The listener interface for receiving congestion state. The class that is interested in listening for congestion state should
 * implement this interface and register itself to {@link CongestionMonitor}
 *
 * @author amit bhayani
 *
 */
public interface CongestionListener {

    /**
     * As soon as congestion starts in the underlying source, it calls this method to notify about it. Notification is only
     * one-time till the congestion abates in which case {@link CongestionListener#onCongestionFinish(String)} is called
     *
     * @param source The underlying source which is facing congestion
     */
    void onCongestionStart(String source);

    /**
     * As soon as congestion abates in the underlying source, it calls this method to notify about it. Notification is only
     * one-time till the congestion starts agaain in which case {@link CongestionListener#onCongestionStart(String)} is called
     *
     * @param source The underlying source
     */
    void onCongestionFinish(String source);

}
