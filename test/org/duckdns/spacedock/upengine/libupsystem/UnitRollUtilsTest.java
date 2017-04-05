/*
 * Copyright (C) 2017 ykonoclast
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class UnitRollUtilsTest
{
//cas d'erreur sur paramétres

    @Test
    public void testLancerErreur()
    {
	try
	{
	    RollUtils.lancer(-1, 2, true);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de dés lancés:-1 nombre de dés gardés:2", e.getMessage());
	}

	try
	{
	    RollUtils.lancer(2, -1, true);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de dés lancés:2 nombre de dés gardés:-1", e.getMessage());
	}
    }

    @Test
    public void testExtraireIncrements()
    {
	//Cas d'échec
	Assert.assertFalse(RollUtils.extraireIncrements(12, 25).isJetReussi());
	Assert.assertEquals(12, RollUtils.extraireIncrements(12, 25).getScoreBrut());
	Assert.assertEquals(0, RollUtils.extraireIncrements(12, 25).getNbIncrements());

	//Pile le ND
	Assert.assertTrue(RollUtils.extraireIncrements(25, 25).isJetReussi());
	Assert.assertEquals(25, RollUtils.extraireIncrements(25, 25).getScoreBrut());
	Assert.assertEquals(0, RollUtils.extraireIncrements(25, 25).getNbIncrements());

	//Tranche non entière de 5
	Assert.assertTrue(RollUtils.extraireIncrements(29, 25).isJetReussi());
	Assert.assertEquals(29, RollUtils.extraireIncrements(29, 25).getScoreBrut());
	Assert.assertEquals(0, RollUtils.extraireIncrements(29, 25).getNbIncrements());

	Assert.assertTrue(RollUtils.extraireIncrements(34, 25).isJetReussi());
	Assert.assertEquals(34, RollUtils.extraireIncrements(34, 25).getScoreBrut());
	Assert.assertEquals(1, RollUtils.extraireIncrements(34, 25).getNbIncrements());

	//Tranches entières de 5
	Assert.assertTrue(RollUtils.extraireIncrements(35, 25).isJetReussi());
	Assert.assertEquals(35, RollUtils.extraireIncrements(35, 25).getScoreBrut());
	Assert.assertEquals(2, RollUtils.extraireIncrements(35, 25).getNbIncrements());
    }
}
