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

import java.util.ArrayList;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author ykonoclast
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(//pour les méthodes statiques c'est la classe appelante qui doit apparaître ici, pour les classes final c'est la classe appelée (donc UPReference n'apparaît ici que pour son caractère final et pas pour sa méthode getInstance()

	{
	    Perso.class, UPReference.class
	})
public class UnitPersoTest
{

    private UPReference referenceMock;
    private ArbreDomaines arbreMock;
    private CoupleJauge santeInitRM1;
    private CoupleJauge santeInitRM3;
    private Perso persoRM1;
    private Perso persoRM3;
    private static ArrayList<String> listComp = new ArrayList<>();
    private static ArrayList<String> listDom = new ArrayList<>();

    @BeforeClass
    public static void setupClass()
    {
	//on construit une liste de compétences retournée par la référence mockée, ce seront les mêmes pour tous les domaines
	listComp.add("comp1");
	listComp.add("comp2");
	listComp.add("comp3");

	//on construit une liste de domaines retournée par la référence mockée, quatre suffiront
	listDom.add("dom1");
	listDom.add("dom2");
	listDom.add("dom3");
	listDom.add("dom4");
	listDom.add("dom5");
    }

    @Before
    public void setup() throws Exception
    {
	//on mocke la référence, les jauges, l'arbredomaines et l'inventaire pour deux persos
	referenceMock = PowerMockito.mock(UPReference.class);
	PowerMockito.mockStatic(UPReference.class);
	when(UPReference.getInstance()).thenReturn(referenceMock);

	arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	santeInitRM1 = PowerMockito.mock(CoupleJauge.class);
	santeInitRM3 = PowerMockito.mock(CoupleJauge.class);
	whenNew(CoupleJauge.class).withArguments(1, 0, 0, 1).thenReturn(santeInitRM1);
	whenNew(CoupleJauge.class).withArguments(3, 2, 2, 3).thenReturn(santeInitRM3);

	//La référence mockée renvoie les listes de domaines et compétence prédéfinies
	when(referenceMock.getListDomaines()).thenReturn(listDom);
	when(referenceMock.getListComp(2)).thenReturn(listComp);
	when(referenceMock.getListComp(3)).thenReturn(listComp);
	when(referenceMock.getListComp(4)).thenReturn(listComp);

	//les jauges mockées renvoient des valeurs d'init
	when(santeInitRM1.getRemplissage_externe()).thenReturn(1);
	when(santeInitRM3.getRemplissage_externe()).thenReturn(3);

	//les persos de test sont créés, ils utiliseront les jauges de test
	persoRM1 = new Perso(1);
	persoRM3 = new Perso(3);
    }

    @Test
    public void testPersoParCaracs()
    {
	//cas d'erreur : pas assez de traits
	try
	{
	    int[] traits =
	    {
		1, 1
	    };
	    new Perso(traits, arbreMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de traits:2", e.getMessage());
	}

	//cas d'erreur : trop de traits
	try
	{
	    int[] traits =
	    {
		1, 1, 1, 1, 1, 1
	    };
	    new Perso(traits, arbreMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:nombre de traits:6", e.getMessage());
	}

	//cas d'erreur : trait négatif
	try
	{
	    int[] traits =
	    {
		0, 1, -12, 1, 1
	    };
	    new Perso(traits, arbreMock);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:trait:-12", e.getMessage());
	}
    }

    @Test
    public void testPersoParRM() throws Exception
    {
	reset(arbreMock);//on réinitialise arbreMock pour que ses interractions dans setup() ne perturbent pas le test ici

	//Cas d'erreur : RM négatif
	try
	{
	    new Perso(-11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:-11", e.getMessage());
	}

	//Cas d'erreur : RM nul
	try
	{
	    new Perso(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:rang:0", e.getMessage());
	}

	//Cas nominal RM3 : vérification des appels d'ArbreDomaines et de coupleJauge
	new Perso(3);

	verify(arbreMock).setRangDomaine(3, 3);
	verify(arbreMock).setRangComp(3, 2, 3);//la liste mock contient à ce moment 3 items
	verify(arbreMock, never()).setRangComp(3, 3, 3);//et donc on ne va pas jusqu'à un éventuel quatrième
	verify(arbreMock).setRangDomaine(4, 3);
	verify(arbreMock).setRangDomaine(2, 3);
	verify(arbreMock).setRangComp(2, 0, 3);
	verify(arbreMock, never()).setRangComp(2, 1, 3);//seule esquive est montée dans ce domaine
    }

    @Test
    public void testAddSpecialite()
    {
	Perso perso = new Perso(3);

	//test appel effectif des méthodes de l'ArbreDomaines
	perso.addSpecialite(3, 1, "gneuh");
	verify(arbreMock).addSpecialite(3, 1, "gneuh");
    }

    @Test
    public void testAgirEnCombat() throws Exception
    {

	//On teste la "consommation" des actions pour des persos ayant des valeurs d'initiative différentes
	persoRM1.agirEnCombat(persoRM1.getActions().get(0));
	Assert.assertEquals(11, persoRM1.getActions().get(0).intValue());

	persoRM3.agirEnCombat(persoRM3.getActions().get(0));
	Assert.assertEquals(11, persoRM3.getActions().get(0).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(1));
	Assert.assertEquals(11, persoRM3.getActions().get(1).intValue());
	persoRM3.agirEnCombat(persoRM3.getActions().get(2));
	Assert.assertEquals(11, persoRM3.getActions().get(2).intValue());

	try
	{
	    persoRM1.agirEnCombat(0);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:0", e.getMessage());
	}

	try
	{
	    persoRM1.agirEnCombat(11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    Assert.assertEquals("paramétre aberrant:phase:11", e.getMessage());
	}
    }

    @Test
    public void testAttaquerCaC()
    {
	//cas mains nues et arme
	/*m_inventaire.getArmeCourante()


	arme.getCategorie()
		getphysMin()

			arme.getMalusAttaque();


			m_arbreDomaines.effectuerJetComp //tester le jugement de la réussite par le perso*/


 /*
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




	 */
    }

    //TODO décommenter la méthode ci-dessous et l'achever
    /*@Test
    public void testEtreBlesse() throws Exception
    {
	Inventaire inventaireMock = PowerMockito.mock(Inventaire.class);
	whenNew(Inventaire.class).withNoArguments().thenReturn(inventaireMock);
	when(inventaireMock.getArmure()).thenReturn(new Armure(1, 1, 0, 0));

	Perso perso = new Perso(3);
	perso.etreBlesse(new Arme.Degats(40, 0));
    }*/
}
