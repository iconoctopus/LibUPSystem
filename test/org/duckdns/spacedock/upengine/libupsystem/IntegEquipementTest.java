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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class IntegEquipementTest
{

    /*persoRM1.getInventaire().addPieceArmure(new PieceArmure(0, 0, 0, false), Inventaire.ZoneEmplacement.TETE);
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, false));
	Assert.assertEquals(10, persoRM1.getNDPassif(0, 1, true));
	persoRM1.getInventaire().addPieceArmure(new PieceArmure(7, 0, 0, false), Inventaire.ZoneEmplacement.CORPS);
	Assert.assertEquals(15, persoRM1.getNDPassif(0, 1, false));
	Assert.assertEquals(5, persoRM1.getNDPassif(0, 1, true));
	Assert.assertEquals(0, persoRM1.getPointsDeFatigue());



	persoRM3.getInventaire().addPieceArmure(new PieceArmure(0, 3, 0, false), Inventaire.ZoneEmplacement.TETE);
	Assert.assertEquals(35, persoRM3.getNDPassif(0, 1, false));
	Assert.assertEquals(35, persoRM3.getNDPassif(0, 1, true));
	Assert.assertEquals(0, persoRM3.getPointsDeFatigue());*/

    @Before
    public void setUp()
    {
    }

    @Test
    public void testAjoutRetraitArmure()
    {
	//TODO tester l'augmentation/diminution du type d'armure et les effets de seuils dans les rangs d'armure

	Inventaire inventaire = new Inventaire();

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
	inventaire.addPieceArmure(ganteletGauche, Inventaire.ZoneEmplacement.MAINGAUCHE);
	Assert.assertEquals(ganteletGauche, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.MAINGAUCHE));

	//récupération de l'armure totale (avec les deux bottes)
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(1));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(2));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(0, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(12, inventaire.getArmure().getMalusEsquive());

	//suppression de l'une des pièce d'armure et vérification qu'elle est effective
	inventaire.removePieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE);
	Assert.assertEquals(null, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE));

	//récupération de l'armure totale (avec le trou dans la liste au niveau du pied gauche)
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(1));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(0));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(0));
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(2));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(0, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(11, inventaire.getArmure().getMalusEsquive());

	//test de l'ajout d'un bouclier
	PieceArmure bouclier = new PieceArmure(0, 2, 0, true);
	inventaire.addBouclier(bouclier, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(bouclier, inventaire.getBouclier(Inventaire.Lateralisation.GAUCHE));

	Assert.assertEquals(0, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(11, inventaire.getArmure().getMalusEsquive());
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(2));//le bouclier doit avoir fait augmenter le type général de l'armure en plus de lui avoir fait passer un rang
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(3));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(3));
	Assert.assertEquals(5, inventaire.getArmure().getBonusND(4));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(4));

	//on retire le bouclier, tout doit redevenir comme avant
	inventaire.removeBouclier(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(1));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(0));
	Assert.assertEquals(15, inventaire.getArmure().getRedDegats(0));
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(2));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(0, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(11, inventaire.getArmure().getMalusEsquive());
    }

    @Test
    public void testIntegArmes()
    {

    }
}
