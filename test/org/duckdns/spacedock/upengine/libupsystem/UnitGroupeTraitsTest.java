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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class UnitGroupeTraitsTest
{

    @Test
    public void testSetTraitErreur()
    {
	try
	{
	    new GroupeTraits(3, 2, 0, 4, 1).setTrait(GroupeTraits.Trait.MENTAL, -1);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    assertEquals("paramétre aberrant:trait:-1", e.getMessage());
	}

	try
	{
	    new GroupeTraits(3, 2, 0, 4, 1).setTrait(GroupeTraits.Trait.VOLONTE, 11);
	    fail();
	}
	catch (IllegalArgumentException e)
	{
	    assertEquals("paramétre aberrant:trait:11", e.getMessage());
	}
    }

    @Test
    public void testSetTraitNominal()
    {
	GroupeTraits traits = new GroupeTraits(4, 3, 7, 1, 2);
	traits.setTrait(GroupeTraits.Trait.COORDINATION, 10);
	assertEquals(10, traits.getTrait(GroupeTraits.Trait.COORDINATION));
	traits.setTrait(GroupeTraits.Trait.COORDINATION, 0);//petit cas limite
	assertEquals(0, traits.getTrait(GroupeTraits.Trait.COORDINATION));
    }

    @Test
    public void testGetTraitEtCreationReussie()
    {
	GroupeTraits traits = new GroupeTraits(1, 2, 8, 0, 6);
	assertEquals(1, traits.getTrait(GroupeTraits.Trait.PHYSIQUE));
	assertEquals(2, traits.getTrait(GroupeTraits.Trait.COORDINATION));
	assertEquals(8, traits.getTrait(GroupeTraits.Trait.MENTAL));
	assertEquals(0, traits.getTrait(GroupeTraits.Trait.VOLONTE));
	assertEquals(6, traits.getTrait(GroupeTraits.Trait.PRESENCE));
    }
}
