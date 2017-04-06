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

import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ykonoclast
 */
public class IntegCaracTest
{

    Perso persoRM1;
    Perso persoRM3;
    Perso persoNonRM;

    @Before
    public void setUp()
    {
	persoRM1 = new Perso(1);
	persoRM3 = new Perso(3);

	ArbreDomaines arbreTest = new ArbreDomaines();
	arbreTest.setRangDomaine(2, 2);
	arbreTest.setRangComp(2, 2, 1);
	arbreTest.addSpecialite(2, 2, "élevage de stylos bic en liberté");

	GroupeTraits traits = new GroupeTraits(1, 2, 3, 4, 5);

	persoNonRM = new Perso(traits, arbreTest);
    }

    @Test
    public void testCaracsSecondaires()
    {
	Assert.assertEquals(0, persoRM1.getEtatVital().getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getEtatVital().getBlessuresLegeres());
	Assert.assertEquals(0, persoRM1.getEtatVital().getBlessuresLegeresMentales());
	Assert.assertEquals(4, persoRM1.getEtatVital().getFatigue());
	Assert.assertEquals(4, persoRM1.getEtatVital().getSante());
	Assert.assertEquals(0, persoRM1.getEtatVital().getPtsFatigue());
	Assert.assertEquals(2, persoRM1.getEtatVital().getPtChocFatigue());
	Assert.assertEquals(2, persoRM1.getEtatVital().getPtChocSante());
	Assert.assertEquals(2, persoRM1.getEtatVital().getInitMax());
	Assert.assertEquals(2, persoRM1.getEtatVital().getInitActu());
	Assert.assertEquals(2, persoRM1.getEtatVital().getForceDAmeActu());
	Assert.assertEquals(2, persoRM1.getEtatVital().getForceDAmeMax());
	Assert.assertEquals(15, persoRM1.getDefense(0, 0));
	Assert.assertEquals(5, persoRM1.getDefense(0, 6));
	Assert.assertFalse(persoRM1.getEtatVital().isSonne());
	Assert.assertFalse(persoRM1.getEtatVital().isInconscient());
	Assert.assertFalse(persoRM1.getEtatVital().isElimine());

	Assert.assertEquals(0, persoRM3.getEtatVital().getBlessuresGraves());
	Assert.assertEquals(0, persoRM3.getEtatVital().getBlessuresLegeres());
	Assert.assertEquals(0, persoRM3.getEtatVital().getBlessuresLegeresMentales());
	Assert.assertEquals(5, persoRM3.getEtatVital().getFatigue());
	Assert.assertEquals(5, persoRM3.getEtatVital().getSante());
	Assert.assertEquals(0, persoRM3.getEtatVital().getPtsFatigue());
	Assert.assertEquals(2, persoRM3.getEtatVital().getPtChocFatigue());
	Assert.assertEquals(3, persoRM3.getEtatVital().getPtChocSante());
	Assert.assertEquals(3, persoRM3.getEtatVital().getInitMax());
	Assert.assertEquals(3, persoRM3.getEtatVital().getInitActu());
	Assert.assertEquals(2, persoRM3.getEtatVital().getForceDAmeActu());
	Assert.assertEquals(2, persoRM3.getEtatVital().getForceDAmeMax());
	Assert.assertEquals(25, persoRM3.getDefense(0, 0));
	Assert.assertEquals(5, persoRM3.getDefense(0, 52));
	Assert.assertFalse(persoRM3.getEtatVital().isSonne());
	Assert.assertFalse(persoRM3.getEtatVital().isInconscient());
	Assert.assertFalse(persoRM3.getEtatVital().isElimine());

	Assert.assertEquals(0, persoNonRM.getEtatVital().getBlessuresGraves());
	Assert.assertEquals(0, persoNonRM.getEtatVital().getBlessuresLegeres());
	Assert.assertEquals(0, persoNonRM.getEtatVital().getBlessuresLegeresMentales());
	Assert.assertEquals(5, persoNonRM.getEtatVital().getFatigue());
	Assert.assertEquals(5, persoNonRM.getEtatVital().getSante());
	Assert.assertEquals(0, persoNonRM.getEtatVital().getPtsFatigue());
	Assert.assertEquals(4, persoNonRM.getEtatVital().getPtChocFatigue());
	Assert.assertEquals(1, persoNonRM.getEtatVital().getPtChocSante());
	Assert.assertEquals(2, persoNonRM.getEtatVital().getInitMax());
	Assert.assertEquals(2, persoNonRM.getEtatVital().getInitActu());
	Assert.assertEquals(1, persoNonRM.getEtatVital().getForceDAmeActu());
	Assert.assertEquals(1, persoNonRM.getEtatVital().getForceDAmeMax());
	Assert.assertEquals(15, persoNonRM.getDefense(0, 0));
	Assert.assertEquals(7, persoNonRM.getDefense(0, 4));
	Assert.assertFalse(persoNonRM.getEtatVital().isSonne());
	Assert.assertFalse(persoNonRM.getEtatVital().isInconscient());
	Assert.assertFalse(persoNonRM.getEtatVital().isElimine());
    }

    @Test
    public void testTraits()
    {
	persoRM3.setTrait(Trait.PHYSIQUE, 5);
	persoRM3.setTrait(Trait.MENTAL, 6);
	Assert.assertEquals(5, persoRM3.getTrait(Trait.PHYSIQUE));
	Assert.assertEquals(6, persoRM3.getTrait(Trait.MENTAL));
    }

    @Test
    public void testDomComp()
    {
	Assert.assertEquals(4, persoRM3.getRangComp(3, 0));//Corps à corps
	Assert.assertEquals(4, persoRM3.getRangDomaine(4));//distance

	Assert.assertEquals(1, persoNonRM.getRangComp(2, 2));
	Assert.assertEquals(2, persoNonRM.getRangDomaine(2));
	Assert.assertEquals("élevage de stylos bic en liberté", persoNonRM.getSpecialites(2, 2).get(0));
    }
}
