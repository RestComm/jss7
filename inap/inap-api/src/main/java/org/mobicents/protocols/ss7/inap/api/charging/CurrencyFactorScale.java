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

package org.mobicents.protocols.ss7.inap.api.charging;

import java.io.Serializable;

/**
*
<code>
CurrencyFactorScale ::= SEQUENCE {
  currencyFactor [00] CurrencyFactor DEFAULT noCharge ,
  currencyScale  [01] CurrencyScale DEFAULT noScale
}

CurrencyFactor ::= INTEGER (0..999999)
-- Value 0 indicates "no charge".

CurrencyScale ::= INTEGER (-7..3)
-- The actual value for currency scale is given by 10x, where x is the value of the CurrencyScale.
--
-- the coding of CurrencyScale is as follows, all other values are spare:
-- -7 (249): 0,0000001
-- -6 (250): 0,000001
-- -5 (251): 0,00001
-- -4 (252): 0,0001
-- -3 (253): 0,001
-- -2 (254): 0,01
-- -1 (255): 0,1
-- 0 : 1
-- 1 : 10
-- 2 : 100
-- 3 : 1000

-- The charge amount is indicated by the currency factor multiplied with the currency scale.
-- "no charge" indicates CurrencyFactorScale has the value 0.
</code>
*
* @author sergey vetyutnev
*
*/
public interface CurrencyFactorScale extends Serializable {

    Integer getCurrencyFactor();

    Integer getCurrencyScale();

}
