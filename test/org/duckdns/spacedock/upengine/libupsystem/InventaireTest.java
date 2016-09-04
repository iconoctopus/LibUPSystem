/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iconoctopus
 */
public class InventaireTest
{

    @Test
    public void testAddRemoveArme()
    {
	Inventaire inventaire = new Inventaire();
	Arme arme1 = new ArmeCaC(7, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);//rapière
	Arme arme2 = new ArmeCaC(22, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon);//hache d'arme
	Arme arme3 = new ArmeCaC(14, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);//épée à deux mains
	Arme arme4 = new ArmeCaC(1, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);//cimeterre

	//erreur : on ne peut pas retirer quelque chose qui n'est pas là
	try
	{
	    inventaire.removeArme(Inventaire.Lateralisation.DROITE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:l'emplacement n'est pas actuellement occupé", e.getMessage());
	}

	//ajout d'arme principale correct
	inventaire.addArme(arme1, Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme1, inventaire.getArmeCourante());

	//ajout d'arme côté secondaire, dans cette version sans combat à deux armes l'arme principale qui répond reste celle de l'autre côté, à terme ce test évoluera
	inventaire.addArme(arme2, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(arme1, inventaire.getArmeCourante());

	//passer le côté secondaire en côté principal et vérifier que le switch a bien été fait
	inventaire.setCotePrincipal(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(arme2, inventaire.getArmeCourante());

	//cas d'erreur : emplacement occupé
	try
	{
	    inventaire.addArme(arme4, Inventaire.Lateralisation.DROITE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:l'emplacement n'est pas libre", e.getMessage());
	}

	//on libère le côté droit, on réaffecte et vérifie que le côté principal est toujours à gauche
	inventaire.removeArme(Inventaire.Lateralisation.DROITE);
	inventaire.addArme(arme4, Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme2, inventaire.getArmeCourante());

	//on repasse le côté droit en principal et vérifie l'ajout précédent
	inventaire.setCotePrincipal(Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme4, inventaire.getArmeCourante());

	//on libère le côté gauche et tente d'ajouter une arme à deux mains sans pour autant libérer le côté droit
	inventaire.removeArme(Inventaire.Lateralisation.GAUCHE);
	try
	{
	    inventaire.addArme(arme3, Inventaire.Lateralisation.GAUCHE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:arme à deux mains mais main secondaire occupée", e.getMessage());
	}

	//on libère le côté droit et on ajoute à nouveau l'arme à deux mains à gauche, comme le côté principal est à gauche il ne doit répondre aucune arme comme arme principale pour l'instant
	inventaire.removeArme(Inventaire.Lateralisation.DROITE);
	inventaire.addArme(arme3, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(null, inventaire.getArmeCourante());

	//on repasse le côté principal à gauche et on vérifie que l'arme à deux mains est bien l'arme principale
	inventaire.setCotePrincipal(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(arme3, inventaire.getArmeCourante());

	//on supprime l'arme à deux mains et on passe une arme à gauche puis à droite et on interroge à nouveau les armes courantes
	inventaire.removeArme(Inventaire.Lateralisation.GAUCHE);
	inventaire.addArme(arme1, Inventaire.Lateralisation.DROITE);
	inventaire.addArme(arme2, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(arme2, inventaire.getArmeCourante());
	inventaire.setCotePrincipal(Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme1, inventaire.getArmeCourante());
    }

    @Test
    public void testAddRemoveGetPieceArmureEtBouclier()
    {
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

	//suppression de l'une des pièce d'armure et vérification qu'elle est effective
	inventaire.removePieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE);
	Assert.assertEquals(null, inventaire.getPieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE));

	//récupération de l'armure totale (avec le trou dans la liste au niveau du pied gauche)
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(1));
	Assert.assertEquals(10, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(0));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(6, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(15, inventaire.getArmure().getMalusEsquive());

	//Erreur : ajout sur zone occupée
	try
	{
	    inventaire.addPieceArmure(casque, Inventaire.ZoneEmplacement.TETE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:l'emplacement n'est pas libre", e.getMessage());
	}

	//Erreur : retrait de zone libre
	try
	{
	    inventaire.removePieceArmure(Inventaire.ZoneEmplacement.PIEDGAUCHE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:l'emplacement n'est pas actuellement occupé", e.getMessage());
	}

	//Erreur : localisation incorrecte
	try
	{
	    inventaire.addPieceArmure(casque, Inventaire.ZoneEmplacement.PIEDGAUCHE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:localisation incorrecte", e.getMessage());
	}

	//test de l'ajout d'un bouclier
	PieceArmure bouclier = new PieceArmure(0, 2, 0, true);
	inventaire.addBouclier(bouclier, Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(bouclier, inventaire.getBouclier(Inventaire.Lateralisation.GAUCHE));

	Assert.assertEquals(6, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(15, inventaire.getArmure().getMalusEsquive());
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(2));//le bouclier doit avoir fait augmenter le type général de l'armure en plus de lui avoir fait passer un rang
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(3));

	//on retire le bouclier, tout doit redevenir comme avant
	inventaire.removeBouclier(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(10, inventaire.getArmure().getBonusND(1));
	Assert.assertEquals(10, inventaire.getArmure().getRedDegats(1));
	Assert.assertEquals(15, inventaire.getArmure().getBonusND(0));
	Assert.assertEquals(5, inventaire.getArmure().getRedDegats(2));
	Assert.assertEquals(6, inventaire.getArmure().getMalusParade());
	Assert.assertEquals(15, inventaire.getArmure().getMalusEsquive());

	//on remet le bouclier, on ne peut pas ajouter d'arme dans la même main
	Arme arme1Main = new ArmeCaC(7, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais);
	inventaire.addBouclier(bouclier, Inventaire.Lateralisation.GAUCHE);
	try
	{
	    inventaire.addArme(arme1Main, Inventaire.Lateralisation.GAUCHE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:l'emplacement n'est pas libre", e.getMessage());
	}

	//le bouclier toujours présent, on ajoute une arme à deux mains dans l'autre main
	Arme arme2Mains = new ArmeCaC(14, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal);
	try
	{
	    inventaire.addArme(arme2Mains, Inventaire.Lateralisation.DROITE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:arme à deux mains mais main secondaire occupée", e.getMessage());
	}

	//ajout d'une autre arme dans l'autre main, l'inventaire la reconnait comme arme principale car elle est à droite et c'est le côté par défaut
	inventaire.addArme(arme1Main, Inventaire.Lateralisation.DROITE);
	Assert.assertEquals(arme1Main, inventaire.getArmeCourante());

	//changement de latéralisation : le bouclier n'est pas une arme donc la méthode doit renvoyer null
	inventaire.setCotePrincipal(Inventaire.Lateralisation.GAUCHE);
	Assert.assertEquals(null, inventaire.getArmeCourante());

	//erreur : ajout d'un bouclier dans la main portant l'arme
	try
	{
	    inventaire.addBouclier(bouclier, Inventaire.Lateralisation.DROITE);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:l'emplacement n'est pas libre", e.getMessage());
	}
    }

    @Test
    public void testArmureVide()
    {
	//tout doit valoir 0
	Assert.assertEquals(0, new Inventaire().getArmure().getBonusND(1));
	Assert.assertEquals(0, new Inventaire().getArmure().getRedDegats(1));
	Assert.assertEquals(0, new Inventaire().getArmure().getBonusND(0));
	Assert.assertEquals(0, new Inventaire().getArmure().getRedDegats(2));
	Assert.assertEquals(0, new Inventaire().getArmure().getMalusParade());
	Assert.assertEquals(0, new Inventaire().getArmure().getMalusEsquive());
    }
}
