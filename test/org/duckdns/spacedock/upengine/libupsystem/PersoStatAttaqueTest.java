/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class PersoStatAttaqueTest
{

    @Test
    public void testAttaquer()//sert aussi pour tester tout le système de jets de compétence : il se trouve qu'attaquer est la forme la plus complexe du jet de compétence, une fois tous les bonus et malus pris en compte
    {
	Perso persoRM1 = new Perso(1);
	Perso persoRM3 = new Perso(3);
	Perso persoRM5 = new Perso(5);

	//cas du physique minimal insuffisant avec une hache et un physique de 1
	persoRM1.getInventaire().addArme(new ArmeCaC(10, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal), Inventaire.Lateralisation.DROITE);
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM1, 1, 0, 0));

	//attaque à mains nues avec RM3
	Assert.assertTrue(StatUtils.reussiteStatistiqueAttaque(persoRM3, 30, 0, 0));
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM3, 35, 0, 0));

	//attaque en prenant en compte le malus à l'attaque du sabre et RM3
	persoRM3.getInventaire().addArme(new ArmeCaC(8, Arme.QualiteArme.inferieure, Arme.EquilibrageArme.bon), Inventaire.Lateralisation.DROITE);//la qualité ne devrait pas influer, l'équilibrage non plus, ni ici ni dans les autres tests de cette méthode
	Assert.assertTrue(StatUtils.reussiteStatistiqueAttaque(persoRM3, 27, 0, 0));
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM3, 32, 0, 0));

	//attaque à distance au fusil d'assaut coup par coup avec RM5 et portée courte
	persoRM5.getInventaire().addArme(new ArmeDist(44, Arme.QualiteArme.superieure, Arme.EquilibrageArme.mauvais), Inventaire.Lateralisation.DROITE);
	((ArmeDist) persoRM5.getInventaire().getArmeCourante()).recharger(30);
	Assert.assertTrue(StatUtils.reussiteStatistiqueAttaque(persoRM5, 42, 150, 1));//pile la portée, donc courte
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM5, 46, 150, 2));//deux balles ne devraient rien changer, on n'est pas au seuil de rafale courte

	//attaque à distance au fusil d'assaut en rafale courte avec RM3 et portée courte
	persoRM3.getInventaire().removeArme(Inventaire.Lateralisation.DROITE);
	persoRM3.getInventaire().addArme(new ArmeDist(44, Arme.QualiteArme.moyenne, Arme.EquilibrageArme.normal), Inventaire.Lateralisation.DROITE);
	((ArmeDist) persoRM3.getInventaire().getArmeCourante()).recharger(30);
	Assert.assertTrue(StatUtils.reussiteStatistiqueAttaque(persoRM3, 27, 100, 3));
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM3, 32, 20, 3));

	//attaque à distance au fusil d'assaut en rafale moyenne avec RM3 et portée longue
	persoRM3.getInventaire().removeArme(Inventaire.Lateralisation.DROITE);
	persoRM3.getInventaire().addArme(new ArmeDist(44, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais), Inventaire.Lateralisation.DROITE);
	((ArmeDist) persoRM3.getInventaire().getArmeCourante()).recharger(30);
	Assert.assertTrue(StatUtils.reussiteStatistiqueAttaque(persoRM3, 24, 200, 8));//donc deux groupes entiers de trois balles
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM3, 28, 270, 8));

	//attaque à distance au fusil d'assaut en rafale longue avec RM5 et portée courte
	persoRM5.getInventaire().removeArme(Inventaire.Lateralisation.DROITE);
	persoRM5.getInventaire().addArme(new ArmeDist(44, Arme.QualiteArme.maitre, Arme.EquilibrageArme.bon), Inventaire.Lateralisation.DROITE);
	((ArmeDist) persoRM5.getInventaire().getArmeCourante()).recharger(30);
	Assert.assertTrue(StatUtils.reussiteStatistiqueAttaque(persoRM5, 56, 120, 13));//deux groupes entiers de 5 balles
	Assert.assertFalse(StatUtils.reussiteStatistiqueAttaque(persoRM5, 60, 40, 13));
    }
}
