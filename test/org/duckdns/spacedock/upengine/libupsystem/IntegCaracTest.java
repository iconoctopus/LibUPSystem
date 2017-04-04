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

import java.util.EnumMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ykonoclast
 */
public class IntegCaracTest//tester les libellés
{//TODO vérifier que les compétences d'attaque et de parade sont bien positionnées

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

	EnumMap<Perso.Trait, Integer> traits = new EnumMap(Perso.Trait.class);
	traits.put(Perso.Trait.PHYSIQUE, 1);
	traits.put(Perso.Trait.COORDINATION, 2);
	traits.put(Perso.Trait.MENTAL, 3);
	traits.put(Perso.Trait.VOLONTE, 4);
	traits.put(Perso.Trait.PRESENCE, 5);

	persoNonRM = new Perso(traits, arbreTest);
    }

    @Test
    public void testCaracsSecondaires()
    {
	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeresMentales());
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, false));
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, true));
	Assert.assertTrue(persoRM1.isSonne());
	Assert.assertFalse(persoRM1.isInconscient());
	Assert.assertFalse(persoRM1.isElimine());

	Assert.assertEquals(0, persoRM3.getBlessuresGraves());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeresMentales());
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 1, false));
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 1, true));
	Assert.assertFalse(persoRM3.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());

	Assert.assertEquals(0, persoNonRM.getBlessuresGraves());
	Assert.assertEquals(0, persoNonRM.getBlessuresLegeres());
	Assert.assertEquals(0, persoNonRM.getBlessuresLegeresMentales());
	Assert.assertEquals(5, persoNonRM.getNDPassif(0, 1, false));
	Assert.assertEquals(5, persoNonRM.getNDPassif(0, 1, true));
	Assert.assertFalse(persoNonRM.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());
    }

    @Test
    public void testTraits()
    {
	persoRM3.setTrait(Perso.Trait.PHYSIQUE, 5);
	persoRM3.setTrait(Perso.Trait.MENTAL, 6);
	Assert.assertEquals(5, persoRM3.getTrait(Perso.Trait.PHYSIQUE));
	Assert.assertEquals(6, persoRM3.getTrait(Perso.Trait.MENTAL));
    }

    @Test
    public void testDomComp()
    {
	Assert.assertEquals(3, persoRM3.getRangComp(3, 0));//Corps à corps
	Assert.assertEquals(3, persoRM3.getRangDomaine(4));//distance

	Assert.assertEquals(1, persoNonRM.getRangComp(2, 2));
	Assert.assertEquals(2, persoNonRM.getRangDomaine(2));
	Assert.assertEquals("élevage de stylos bic en liberté", persoNonRM.getSpecialites(2, 2).get(0));
    }
}
