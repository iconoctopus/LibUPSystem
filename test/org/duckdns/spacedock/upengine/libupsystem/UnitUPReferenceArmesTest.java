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
import java.util.EnumMap;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ykonoclast
 */
public class UnitUPReferenceArmesTest
{

    private static UPReferenceArmes m_reference;

    @BeforeClass
    public static void setUpClass()
    {
	m_reference = UPReferenceArmes.getInstance();
    }

    @Test
    public void testGetNbLancesArme()
    {
	Assert.assertEquals(4, m_reference.getNbLancesArme(0));
	Assert.assertEquals(2, m_reference.getNbLancesArme(20));
	Assert.assertEquals(4, m_reference.getNbLancesArme(23));
	Assert.assertEquals(5, m_reference.getNbLancesArme(42));
    }

    @Test
    public void testGetNbGardesArme()
    {
	Assert.assertEquals(3, m_reference.getNbGardesArme(0));
	Assert.assertEquals(2, m_reference.getNbGardesArme(20));
	Assert.assertEquals(2, m_reference.getNbGardesArme(23));
	Assert.assertEquals(4, m_reference.getNbGardesArme(42));
    }

    @Test
    public void testGetBonusInitArme()
    {
	Assert.assertEquals(0, m_reference.getBonusInitArme(0));
	Assert.assertEquals(2, m_reference.getBonusInitArme(20));
	Assert.assertEquals(2, m_reference.getBonusInitArme(23));
	Assert.assertEquals(1, m_reference.getBonusInitArme(42));
    }

    @Test
    public void testGetMalusAttaqueArme()
    {
	Assert.assertEquals(1, m_reference.getMalusAttaqueArme(0));
	Assert.assertEquals(0, m_reference.getMalusAttaqueArme(20));
	Assert.assertEquals(1, m_reference.getMalusAttaqueArme(23));
	Assert.assertEquals(0, m_reference.getMalusAttaqueArme(42));
    }

    @Test
    public void testGetPhysMinArme()
    {
	Assert.assertEquals(2, m_reference.getPhysMinArme(0));
	Assert.assertEquals(0, m_reference.getPhysMinArme(20));
	Assert.assertEquals(0, m_reference.getPhysMinArme(23));
	Assert.assertEquals(0, m_reference.getPhysMinArme(42));
    }

    @Test
    public void testGetCategorieArme()
    {
	Assert.assertEquals(1, m_reference.getCategorieArme(0));
	Assert.assertEquals(3, m_reference.getCategorieArme(8));
	Assert.assertEquals(5, m_reference.getCategorieArme(20));
	Assert.assertEquals(5, m_reference.getCategorieArme(23));
	Assert.assertEquals(2, m_reference.getCategorieArme(42));
	Assert.assertEquals(5, m_reference.getCategorieArme(49));
    }

    @Test
    public void testGetTypeArme()
    {
	Assert.assertEquals(0, m_reference.getTypeArme(0));
	Assert.assertEquals(0, m_reference.getTypeArme(20));
	Assert.assertEquals(0, m_reference.getTypeArme(23));
	Assert.assertEquals(1, m_reference.getTypeArme(42));
	Assert.assertEquals(1, m_reference.getTypeArme(38));
    }

    @Test
    public void testGetModArme()
    {
	Assert.assertEquals(0, m_reference.getModArme(0));
	Assert.assertEquals(1, m_reference.getModArme(39));
    }

    @Test
    public void testGetNbMainsArme()
    {
	Assert.assertEquals(1, m_reference.getNbMainsArme(4));
	Assert.assertEquals(2, m_reference.getNbMainsArme(14));
    }

    @Test
    public void testGetLblModArme()
    {
	Assert.assertEquals("corps à corps", m_reference.getLblModeArme(0));
	Assert.assertEquals("distance", m_reference.getLblModeArme(1));
    }

    @Test
    public void testGetLblCatArmeCaC()
    {
	ArrayList<String> cat = new ArrayList<>();

	cat.add("mains nues");
	cat.add("armes d'escrime");
	cat.add("armes à lame longue");
	cat.add("haches");
	cat.add("armes contondantes");
	cat.add("petites armes");
	cat.add("armes d'hast");
	cat.add("armes spéciales");
	cat.add("armes énergétiques");
	cat.add("armes lourdes");

	Assert.assertEquals(cat, m_reference.getListCatArmeCaC());
    }

    @Test
    public void testGetLblCatArmeDist()
    {

	ArrayList<String> cat = new ArrayList<>();

	cat.add("archerie");
	cat.add("armes de jet");
	cat.add("armes à feu");
	cat.add("armes à énergie");
	cat.add("armes automatiques");
	cat.add("armes lourdes");
	cat.add("armes de trait");
	cat.add("lanceurs");
	Assert.assertEquals(cat, m_reference.getListCatArmeDist());
    }

    @Test
    public void testGetMalusCourtArme()
    {
	Assert.assertEquals(5, m_reference.getMalusCourtArme(38));
    }

    @Test
    public void testGetMalusLongArme()
    {
	Assert.assertEquals(10, m_reference.getMalusLongArme(38));
    }

    @Test
    public void testGetPorteeArme()
    {
	Assert.assertEquals(300, m_reference.getPorteeArme(38));
    }

    @Test
    public void testGetNbActionsRechargeArme()
    {
	Assert.assertEquals(1, m_reference.getNbActionsRechargeArme(38));
    }

    @Test
    public void testGetMagasinArme()
    {
	Assert.assertEquals(1, m_reference.getNbActionsRechargeArme(38));
    }

    @Test
    public void testGetListTypeArme()
    {
	Assert.assertEquals("perce-armure", m_reference.getListTypesArmes().get(1));
	Assert.assertEquals("perce-blindage", m_reference.getListTypesArmes().get(3));
    }

    @Test
    public void testGetListArme()
    {
	Assert.assertEquals("bâton ferré", m_reference.getListArmes().get(14));
	Assert.assertEquals("espadon", m_reference.getListArmes().get(7));
    }

    //TODO : tester les méthodes suivantes :
    @Test
    public void testGetLblArme()
    {
	Assert.assertEquals("cimeterre", m_reference.getLblArme(0));
	Assert.assertEquals("rapière", m_reference.getLblArme(3));
    }

    @Test
    public void testGetListEquilibrage()
    {
	EnumMap map = m_reference.getListEquilibrage();
	Assert.assertEquals("mauvais", map.get(Arme.EquilibrageArme.mauvais));
	Assert.assertEquals("normal", map.get(Arme.EquilibrageArme.normal));
	Assert.assertEquals("bon", map.get(Arme.EquilibrageArme.bon));

    }

    @Test
    public void testGetListQualiteArme()
    {
	EnumMap map = m_reference.getListQualiteArme();
	Assert.assertEquals("inférieure", map.get(Arme.QualiteArme.inferieure));
	Assert.assertEquals("de maître", map.get(Arme.QualiteArme.maitre));
	Assert.assertEquals("moyenne", map.get(Arme.QualiteArme.moyenne));
	Assert.assertEquals("supérieure", map.get(Arme.QualiteArme.superieure));
    }

}
