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

/**
 * classe permettant à un personnage de créer directement ses propres poings
 * comme une "arme" en fonction de ses propres caracs
 *
 * @author ykonoclast
 */
public class ArmeMainsNues extends ArmeCaC
{

    private final int m_physique;

    public ArmeMainsNues(GroupeTraits p_traits)
    {
	super(50, QualiteArme.moyenne, EquilibrageArme.normal);
	m_physique = p_traits.getTrait(GroupeTraits.Trait.PHYSIQUE);
    }

    @Override
    public int getVD()
    {
	return (super.getVD() + m_physique);
    }
}
