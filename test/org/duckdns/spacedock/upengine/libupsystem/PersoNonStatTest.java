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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 *
 * @author ykonoclast
 */
public class PersoNonStatTest
{

    static Perso persoRM1;
    static Perso persoRM3;
    static Perso persoRM5;

    @Before
    public void setUp()
    {
	persoRM1 = new Perso(1);
	persoRM3 = new Perso(3);
	persoRM5 = new Perso(5);
    }

    @Test
    public void testPerso()
    {

	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeresMentales());
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, false));
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, true));
	persoRM1.getInventaire().addPieceArmure(new PieceArmure(0, 0, 0, false), Inventaire.ZoneEmplacement.TETE);
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, false));
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, true));
	persoRM1.getInventaire().addPieceArmure(new PieceArmure(7, 0, 0, false), Inventaire.ZoneEmplacement.CORPS);
	Assert.assertEquals(15, persoRM1.getNDPassif(0, 1, false));
	Assert.assertEquals(5, persoRM1.getNDPassif(0, 1, true));
	Assert.assertEquals(0, persoRM1.getPointsDeFatigue());
	Assert.assertTrue(persoRM1.isSonne());
	Assert.assertFalse(persoRM1.isInconscient());
	Assert.assertFalse(persoRM1.isElimine());

	Assert.assertEquals(0, persoRM3.getBlessuresGraves());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM3.getBlessuresLegeresMentales());
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 1, false));
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 1, true));
	persoRM3.getInventaire().addPieceArmure(new PieceArmure(0, 3, 0, false), Inventaire.ZoneEmplacement.TETE);
	Assert.assertEquals(35, persoRM3.getNDPassif(0, 1, false));
	Assert.assertEquals(35, persoRM3.getNDPassif(0, 1, true));
	Assert.assertEquals(0, persoRM3.getPointsDeFatigue());
	Assert.assertFalse(persoRM3.isSonne());
	Assert.assertFalse(persoRM3.isInconscient());
	Assert.assertFalse(persoRM3.isElimine());

	Assert.assertEquals(0, persoRM5.getBlessuresGraves());
	Assert.assertEquals(0, persoRM5.getBlessuresLegeres());
	Assert.assertEquals(0, persoRM5.getBlessuresLegeresMentales());
	Assert.assertEquals(35, persoRM5.getNDPassif(0, 1, false));
	Assert.assertEquals(35, persoRM5.getNDPassif(0, 1, true));
	Assert.assertEquals(0, persoRM5.getPointsDeFatigue());
	Assert.assertFalse(persoRM5.isSonne());
	Assert.assertFalse(persoRM5.isInconscient());
	Assert.assertFalse(persoRM5.isElimine());
    }

    @Test
    public void testErreurAttaquer()
    {
	//cas d'erreur arme pas assez chargée pour faire feu : rafale trop grosse
	persoRM5.getInventaire().addArme(new ArmeDist(44, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon), Inventaire.Lateralisation.DROITE);
	((ArmeDist) persoRM5.getInventaire().getArmeCourante()).recharger(15);
	persoRM5.genInit();
	try
	{
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 0, 20);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:20 munitions courantes:15", e.getMessage());
	}

	//cas d'erreur arme pas assez chargée pour faire feu : arme vide
	persoRM5.getInventaire().removeArme(Inventaire.Lateralisation.DROITE);
	persoRM5.getInventaire().addArme(new ArmeDist(44, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon), Inventaire.Lateralisation.DROITE);
	persoRM5.genInit();
	try
	{
	    persoRM5.attaquerDist(persoRM5.getActions().get(0), 0, 0, 1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de coups:1 munitions courantes:0", e.getMessage());
	}
    }

    @Test
    public void testGenererDegats()
    {
	//Test en corps à corps avec rapière de maître sans incréments, le perso est sonné
	Arme arme = new ArmeCaC(3, Arme.QualiteArme.maitre, Arme.EquilibrageArme.normal);
	int resultVariant = StatUtils.degatsStatistiques(1, arme, 0);
	Assert.assertTrue(22 == resultVariant || resultVariant == 21);

	//test en corps avec hache de mauvaise qualité et deux incréments
	arme = new ArmeCaC(10, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.normal);
	Assert.assertEquals(31, StatUtils.degatsStatistiques(3, arme, 2));

	//test en corps à corps à mains nues sans incréments
	Assert.assertEquals(11, StatUtils.degatsStatistiques(5, null, 0));

	//test à distance avec arc de bonne qualité et un incrément
	arme = new ArmeDist(34, Arme.QualiteArme.superieure, Arme.EquilibrageArme.normal);
	Assert.assertEquals(21, StatUtils.degatsStatistiques(5, arme, 1));
    }

    @Test
    public void testEtreBlesse()
    {
	//cas limite : les dégâts à 0 pasent sans causer d'effet
	persoRM1.etreBlesse(new Arme.Degats(0, 0));
	Assert.assertEquals(0, persoRM1.getBlessuresGraves());
	Assert.assertEquals(0, persoRM1.getBlessuresLegeres());
    }
}
