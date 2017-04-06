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

/**
 *
 * @author ykonoclast
 */
public class IntegEquipementTest
{

    Perso persoRM3;

    @Before
    public void setUp()
    {
	persoRM3 = new Perso(3);
    }

    @Test
    public void testAjoutIntegArmure() throws Exception
    {
	Inventaire inventaire = persoRM3.getInventaire();

	PieceArmure casque = new PieceArmure(0, 1, 0);
	PieceArmure cuirasse = new PieceArmure(7, 0, 0);
	PieceArmure jambiereGauche = new PieceArmure(5, 0, 0);
	PieceArmure jambiereDroite = new PieceArmure(5, 0, 0);
	PieceArmure brassiereGauche = new PieceArmure(4, 0, 0);
	PieceArmure brassiereDroite = new PieceArmure(4, 0, 0);
	PieceArmure botteGauche = new PieceArmure(6, 0, 0);
	PieceArmure botteDroite = new PieceArmure(6, 0, 0);
	PieceArmure ganteletGauche = new PieceArmure(3, 0, 2);
	PieceArmure ganteletDroit = new PieceArmure(3, 0, 2);

	//On ajoute un gantelet à un perso il n'y pas assez de points pour changer le ND et la réduction des dégâts
	inventaire.addPieceArmure(ganteletGauche, Inventaire.PartieCorps.MAINGAUCHE);
	Assert.assertEquals(ganteletGauche, inventaire.getPieceArmure(Inventaire.PartieCorps.MAINGAUCHE));
	Assert.assertEquals("gantelet en cuir clouté", ganteletGauche.toString());
	Assert.assertEquals(25, persoRM3.getDefense(0, 0));
	Assert.assertEquals(0, inventaire.getArmure().getRedDegats(0));

	//vérification du cas nominal de l'ajout, pour toutes les zones
	inventaire.addPieceArmure(casque, Inventaire.PartieCorps.TETE);
	Assert.assertEquals(casque, inventaire.getPieceArmure(Inventaire.PartieCorps.TETE));
	Assert.assertEquals("casque complet moderne de facture très lourde", casque.toString());
	inventaire.addPieceArmure(cuirasse, Inventaire.PartieCorps.CORPS);
	Assert.assertEquals(cuirasse, inventaire.getPieceArmure(Inventaire.PartieCorps.CORPS));
	Assert.assertEquals("cuirasse en plates", cuirasse.toString());
	inventaire.addPieceArmure(jambiereDroite, Inventaire.PartieCorps.JAMBEDROITE);
	Assert.assertEquals(jambiereDroite, inventaire.getPieceArmure(Inventaire.PartieCorps.JAMBEDROITE));
	Assert.assertEquals("jambière en plates", jambiereDroite.toString());
	inventaire.addPieceArmure(jambiereGauche, Inventaire.PartieCorps.JAMBEGAUCHE);
	Assert.assertEquals(jambiereGauche, inventaire.getPieceArmure(Inventaire.PartieCorps.JAMBEGAUCHE));
	Assert.assertEquals("jambière en plates", jambiereGauche.toString());
	inventaire.addPieceArmure(brassiereDroite, Inventaire.PartieCorps.BRASDROIT);
	Assert.assertEquals(brassiereDroite, inventaire.getPieceArmure(Inventaire.PartieCorps.BRASDROIT));
	Assert.assertEquals("brassière en plates", brassiereDroite.toString());
	inventaire.addPieceArmure(brassiereGauche, Inventaire.PartieCorps.BRASGAUCHE);
	Assert.assertEquals(brassiereGauche, inventaire.getPieceArmure(Inventaire.PartieCorps.BRASGAUCHE));
	Assert.assertEquals("brassière en plates", brassiereGauche.toString());
	inventaire.addPieceArmure(botteDroite, Inventaire.PartieCorps.PIEDDROIT);
	Assert.assertEquals(botteDroite, inventaire.getPieceArmure(Inventaire.PartieCorps.PIEDDROIT));
	Assert.assertEquals("botte en plates", botteDroite.toString());
	inventaire.addPieceArmure(botteGauche, Inventaire.PartieCorps.PIEDGAUCHE);
	Assert.assertEquals(botteGauche, inventaire.getPieceArmure(Inventaire.PartieCorps.PIEDGAUCHE));
	Assert.assertEquals("botte en plates", botteGauche.toString());
	inventaire.addPieceArmure(ganteletDroit, Inventaire.PartieCorps.MAINDROITE);
	Assert.assertEquals(ganteletDroit, inventaire.getPieceArmure(Inventaire.PartieCorps.MAINDROITE));
	Assert.assertEquals("gantelet en cuir clouté", ganteletGauche.toString());

	//récupération de l'armure totale (avec les deux bottes)
	Armure armure = inventaire.getArmure();
	Assert.assertEquals(40, persoRM3.getDefense(1, 0));
	Assert.assertEquals(15, armure.getRedDegats(1));
	Assert.assertEquals(35, persoRM3.getDefense(2, 0));
	Assert.assertEquals(5, armure.getRedDegats(2));
	Assert.assertEquals(5, armure.getMalusArmure());

	//suppression de l'une des pièce d'armure et vérification qu'elle est effective
	inventaire.removePieceArmure(Inventaire.PartieCorps.PIEDGAUCHE);
	Assert.assertEquals(null, inventaire.getPieceArmure(Inventaire.PartieCorps.PIEDGAUCHE));

	//récupération de l'armure totale (avec le trou dans la liste au niveau du pied gauche)
	armure = inventaire.getArmure();
	Assert.assertEquals(35, persoRM3.getDefense(1, 0));
	Assert.assertEquals(15, armure.getRedDegats(1));
	Assert.assertEquals(40, persoRM3.getDefense(0, 0));
	Assert.assertEquals(15, armure.getRedDegats(0));
	Assert.assertEquals(35, persoRM3.getDefense(2, 0));
	Assert.assertEquals(5, armure.getRedDegats(2));
	Assert.assertEquals(4, armure.getMalusArmure());

	//test de l'ajout d'un bouclier
	Bouclier bouclier = new Bouclier(0, 2);
	Assert.assertEquals("targe avec blindage", bouclier.toString());
	inventaire.addBouclier(bouclier, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(bouclier, inventaire.getBouclier(Inventaire.Lateralisation.GAUCHE));
	Assert.assertEquals(5, inventaire.getArmure().getMalusArmure());
	Assert.assertEquals(40, persoRM3.getDefense(2, 0));//le bouclier doit avoir fait augmenter le type général de l'armure en plus de lui avoir fait passer un rang
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(35, persoRM3.getDefense(3, 0));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(3));
	Assert.assertEquals(30, persoRM3.getDefense(4, 0));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(4));

	//on retire le bouclier, tout doit redevenir comme avant
	inventaire.removeBouclier(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(35, persoRM3.getDefense(1, 0));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(40, persoRM3.getDefense(0, 0));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(0));
	Assert.assertEquals(35, persoRM3.getDefense(2, 0));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(4, inventaire.getArmure().getMalusArmure());
    }

    @Test
    public void testIntegArmes()
    {
	Arme arme = new ArmeCaC(0, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais);
	Assert.assertEquals("cimeterre de maître", arme.toString());
	persoRM3.getInventaire().addArme(arme, Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme, persoRM3.getInventaire().getArmeCourante());

	Arme arme2 = new ArmeCaC(2, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);
	Assert.assertEquals("épée courte de qualité supérieure et équilibrage mauvais", arme2.toString());
	Arme arme3 = new ArmeCaC(1, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon);
	Assert.assertEquals("coutelas de qualité inférieure et équilibrage bon", arme3.toString());
	Arme arme4 = new ArmeDist(42, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
	Assert.assertEquals("pistolet lourd de qualité moyenne et équilibrage normal", arme4.toString());
    }
}
