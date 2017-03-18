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
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.never;
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

    @Before
    public void setup()
    {
	referenceMock = PowerMockito.mock(UPReference.class);
	PowerMockito.mockStatic(UPReference.class);
	when(UPReference.getInstance()).thenReturn(referenceMock);
    }

    @Test
    public void testPersoParCaracs()
    {
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);

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

	//Cas nominal RM3 : vérification des appels d'ArbreDomaines
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	ArrayList<String> list = new ArrayList<>();//on construit une liste de compétences retournée par la référence mockée
	list.add("comp1");
	list.add("comp2");
	list.add("comp3");

	when(referenceMock.getListComp(3)).thenReturn(list);

	new Perso(3);

	verify(arbreMock).setRangDomaine(3, 3);
	verify(arbreMock).setRangComp(3, 2, 3);//la liste mock contient à ce moment 3 items
	verify(arbreMock, never()).setRangComp(3, 3, 3);//et donc on ne va pas jusqu'à un éventuel quatrième
	verify(arbreMock).setRangDomaine(4, 3);
	verify(arbreMock).setRangDomaine(2, 3);
	verify(arbreMock).setRangComp(2, 0, 3);
	verify(arbreMock, never()).setRangComp(2, 1, 3);//seule esquive est montée dans ce domaine

	list.add("comp4");

	new Perso(3);

	verify(arbreMock).setRangComp(3, 3, 3);//la liste mock contient à ce moment 4 items
	verify(arbreMock, never()).setRangComp(3, 4, 3);//on ne va donc pas jusqu'à un cinquième
    }

    @Test
    public void testAddSpecialite() throws Exception
    {
	ArbreDomaines arbreMock = PowerMockito.mock(ArbreDomaines.class);
	whenNew(ArbreDomaines.class).withNoArguments().thenReturn(arbreMock);

	Perso perso = new Perso(3);

	//test appel effectif des méthodes de l'ArbreDomaines
	perso.addSpecialite(3, 1, "gneuh");
	verify(arbreMock).addSpecialite(3, 1, "gneuh");
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
