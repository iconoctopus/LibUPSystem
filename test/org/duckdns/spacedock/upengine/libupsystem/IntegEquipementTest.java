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
    {//TODO tester les libellés
	Inventaire inventaire = persoRM3.getInventaire();

	PieceArmure casque = new PieceArmure(0, 1, 0, false);
	PieceArmure cuirasse = new PieceArmure(7, 0, 0, false);
	PieceArmure jambiereGauche = new PieceArmure(5, 0, 0, false);
	PieceArmure jambiereDroite = new PieceArmure(5, 0, 0, false);
	PieceArmure brassiereGauche = new PieceArmure(4, 0, 0, false);
	PieceArmure brassiereDroite = new PieceArmure(4, 0, 0, false);
	PieceArmure botteGauche = new PieceArmure(6, 0, 0, false);
	PieceArmure botteDroite = new PieceArmure(6, 0, 0, false);
	PieceArmure ganteletGauche = new PieceArmure(3, 0, 2, false);
	PieceArmure ganteletDroit = new PieceArmure(3, 0, 2, false);

	//On ajoute un gantelet à un perso il n'y pas assez de points pour changer le ND et la réduction des dégâts
	inventaire.addPieceArmure(ganteletGauche, Inventaire.ZoneEmplacement.MAINGAUCHE);
	Assert.assertEquals(ganteletGauche, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.MAINGAUCHE));
	Assert.assertEquals(25, persoRM3.getNDPassif(0, 0, false));
	Assert.assertEquals(0, inventaire.getArmure().getRedDegats(0));

	//vérification du cas nominal de l'ajout, pour toutes les zones
	inventaire.addPieceArmure(casque, Inventaire.ZoneEmplacement.TETE);
	Assert.assertEquals(casque, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.TETE));
	inventaire.addPieceArmure(cuirasse, Inventaire.ZoneEmplacement.CORPS);
	Assert.assertEquals(cuirasse, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.CORPS));
	inventaire.addPieceArmure(jambiereDroite, Inventaire.ZoneEmplacement.JAMBEDROITE);
	Assert.assertEquals(jambiereDroite, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.JAMBEDROITE));
	inventaire.addPieceArmure(jambiereGauche, Inventaire.ZoneEmplacement.JAMBEGAUCHE);
	Assert.assertEquals(jambiereGauche, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.JAMBEGAUCHE));
	inventaire.addPieceArmure(brassiereDroite, Inventaire.ZoneEmplacement.BRASDROIT);
	Assert.assertEquals(brassiereDroite, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.BRASDROIT));
	inventaire.addPieceArmure(brassiereGauche, Inventaire.ZoneEmplacement.BRASGAUCHE);
	Assert.assertEquals(brassiereGauche, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.BRASGAUCHE));
	inventaire.addPieceArmure(botteDroite, Inventaire.ZoneEmplacement.PIEDDROIT);
	Assert.assertEquals(botteDroite, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.PIEDDROIT));
	inventaire.addPieceArmure(botteGauche, Inventaire.ZoneEmplacement.PIEDGAUCHE);
	Assert.assertEquals(botteGauche, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE));
	inventaire.addPieceArmure(ganteletDroit, Inventaire.ZoneEmplacement.MAINDROITE);
	Assert.assertEquals(ganteletDroit, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.MAINDROITE));

	//récupération de l'armure totale (avec les deux bottes)
	Armure armure = inventaire.getArmure();
	Assert.assertEquals(40, persoRM3.getNDPassif(1, 0, false));
	Assert.assertEquals(15, armure.getRedDegats(1));
	Assert.assertEquals(35, persoRM3.getNDPassif(2, 0, false));
	Assert.assertEquals(5, armure.getRedDegats(2));
	Assert.assertEquals(0, armure.getMalusParade());
	Assert.assertEquals(12, armure.getMalusEsquive());

	//suppression de l'une des pièce d'armure et vérification qu'elle est effective
	inventaire.removePieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE);
	Assert.assertEquals(null, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE));

	//récupération de l'armure totale (avec le trou dans la liste au niveau du pied gauche)
	armure = inventaire.getArmure();
	Assert.assertEquals(35, persoRM3.getNDPassif(1, 0, false));
	Assert.assertEquals(15, armure.getRedDegats(1));
	Assert.assertEquals(40, persoRM3.getNDPassif(0, 0, false));
	Assert.assertEquals(15, armure.getRedDegats(0));
	Assert.assertEquals(35, persoRM3.getNDPassif(2, 0, false));
	Assert.assertEquals(5, armure.getRedDegats(2));
	Assert.assertEquals(0, armure.getMalusParade());
	Assert.assertEquals(11, armure.getMalusEsquive());

	//test de l'ajout d'un bouclier
	PieceArmure bouclier = new PieceArmure(0, 2, 0, true);
	inventaire.addBouclier(bouclier, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(bouclier, inventaire.getBouclier(Inventaire.Lateralisation.GAUCHE));

	Assert.assertEquals(0, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(11, inventaire.getArmure().getMalusEsquive());
	Assert.assertEquals(40, persoRM3.getNDPassif(2, 0, false));//le bouclier doit avoir fait augmenter le type général de l'armure en plus de lui avoir fait passer un rang
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(35, persoRM3.getNDPassif(3, 0, false));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(3));
	Assert.assertEquals(30, persoRM3.getNDPassif(4, 0, false));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(4));

	//on retire le bouclier, tout doit redevenir comme avant
	inventaire.removeBouclier(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(35, persoRM3.getNDPassif(1, 0, false));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(40, persoRM3.getNDPassif(0, 0, false));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(0));
	Assert.assertEquals(35, persoRM3.getNDPassif(2, 0, false));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(0, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(11, inventaire.getArmure().getMalusEsquive());
    }

    @Test
    public void testIntegArmes()
    {//TODO tester les libellés
	Arme arme = new ArmeCaC(0, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais);
	persoRM3.getInventaire().addArme(arme, Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme, persoRM3.getInventaire().getArmeCourante());
    }
}
