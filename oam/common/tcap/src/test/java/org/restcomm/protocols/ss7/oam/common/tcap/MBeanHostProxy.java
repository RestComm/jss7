/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.oam.common.tcap;

import javax.management.MBeanServer;

import org.restcomm.protocols.ss7.oam.common.jmx.MBeanHost;
import org.restcomm.protocols.ss7.oam.common.jmx.MBeanLayer;
import org.restcomm.protocols.ss7.oam.common.jmx.MBeanType;

/**
*
* @author sergey vetyutnev
*
*/
public class MBeanHostProxy implements MBeanHost {

    @Override
    public String getAgentId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAgentId(String val) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getDomainName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDomainName(String domainName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getRmiPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRmiPort(int val) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public MBeanServer getMBeanServer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void registerMBean(MBeanLayer layer, MBeanType type, String name, Object bean) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object unregisterMBean(MBeanLayer layer, MBeanType type, String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
