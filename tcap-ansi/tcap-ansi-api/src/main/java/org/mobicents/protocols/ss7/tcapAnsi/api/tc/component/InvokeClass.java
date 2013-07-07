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

/**
 *
 */

package org.mobicents.protocols.ss7.tcapAnsi.api.tc.component;

/**
 * Class of invoke type, ref Q.771 2.3.1.3.
 * <ul>
 * <li>Class 1 � Both success and failure are reported.</li>
 * <li>Class 2 � Only failure is reported.</li>
 * <li>Class 3 � Only success is reported.</li>
 * <li>Class 4 � Neither success, nor failure is reported.</li>
 * <ul>
 *
 * @author baranowb
 *
 */
public enum InvokeClass {

    Class1, Class2, Class3, Class4;
}
