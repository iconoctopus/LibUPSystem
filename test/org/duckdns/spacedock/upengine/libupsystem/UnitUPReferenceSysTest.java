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
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.MENTAL;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PHYSIQUE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PRESENCE;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ykonoclast
 */
public final class UnitUPReferenceSysTest
{

    private static UPReferenceSysteme m_reference;

    @BeforeClass
    public static void setUpClass()
    {
	m_reference = UPReferenceSysteme.getInstance();
    }

    @Test
    public void testGetInitModCoord()
    {
	Assert.assertEquals(5, m_reference.getInitModCoord(6));
	Assert.assertEquals(3, m_reference.getInitModCoord(3));
	Assert.assertEquals(2, m_reference.getInitModCoord(2));
	Assert.assertEquals(6, m_reference.getInitModCoord(7));
    }

    @Test
    public void testGetInitModMental()
    {
	Assert.assertEquals(2, m_reference.getInitModMental(6));
	Assert.assertEquals(0, m_reference.getInitModMental(3));
	Assert.assertEquals(0, m_reference.getInitModMental(2));
	Assert.assertEquals(1, m_reference.getInitModMental(5));
	Assert.assertEquals(2, m_reference.getInitModMental(7));
    }

    @Test
    public void testGetLibelleTrait()
    {
	Assert.assertEquals("physique", m_reference.getLibelleTrait(PHYSIQUE));
	Assert.assertEquals("mental", m_reference.getLibelleTrait(MENTAL));
	Assert.assertEquals("présence", m_reference.getLibelleTrait(PRESENCE));
    }

    @Test
    public void testGetListComp()
    {
	ArrayList<String> analyse = new ArrayList<>();
	ArrayList<String> cac = new ArrayList<>();
	ArrayList<String> dist = new ArrayList<>();
	ArrayList<String> social = new ArrayList<>();

	analyse.add("empathie");
	analyse.add("investigation");
	analyse.add("politique");

	cac.add("attaque mains nues");
	cac.add("parade mains nues");
	cac.add("attaque armes d'escrime");
	cac.add("parade armes d'escrime");
	cac.add("attaque armes à lame longue");
	cac.add("parade armes à lame longue");
	cac.add("attaque haches");
	cac.add("parade haches");
	cac.add("attaque armes contondantes");
	cac.add("parade armes contondantes");
	cac.add("attaque petites armes");
	cac.add("parade petites armes");
	cac.add("attaque armes d'hast");
	cac.add("parade armes d'hast");
	cac.add("attaque armes spéciales");
	cac.add("parade armes spéciales");
	cac.add("attaque armes énergétiques");
	cac.add("parade armes énergétiques");
	cac.add("attaque armes lourdes");
	cac.add("parade armes lourdes");

	dist.add("armes de trait");
	dist.add("armes de jet");
	dist.add("armes à feu");
	dist.add("armes à énergie");
	dist.add("armes automatiques");
	dist.add("armes lourdes");
	dist.add("lanceurs");

	social.add("art oratoire");
	social.add("étiquette");
	social.add("séduction");
	social.add("subterfuge");

	Assert.assertEquals(analyse, m_reference.getListComp(0));
	Assert.assertEquals(cac, m_reference.getListComp(3));
	Assert.assertEquals(dist, m_reference.getListComp(4));
	Assert.assertEquals(social, m_reference.getListComp(8));
    }

    @Test
    public void testGetListDomaine()
    {
	Assert.assertEquals("analyse", m_reference.getListDomaines().get(0));
	Assert.assertEquals("social", m_reference.getListDomaines().get(8));
	Assert.assertEquals(9, m_reference.getListDomaines().size());
    }

    @Test
    public void testLibelles()
    {
	Assert.assertEquals("et", m_reference.getCollectionLibelles().addition);
	Assert.assertEquals("équilibrage", m_reference.getCollectionLibelles().equilibrage);
	Assert.assertEquals("parade", m_reference.getCollectionLibelles().parade);
    }
}
