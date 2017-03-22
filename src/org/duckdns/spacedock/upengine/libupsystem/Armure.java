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
 * classe représentant une armure et encapsulant les traitements des effets de
 * celle-ci sur les attaques entrantes
 *
 * @author ykonoclast
 */
final class Armure
{

    /**
     * le malus d'esquive de l'armure
     */
    private final int m_malusEsquive;
    /**
     * le malus de parade de l'armure
     */
    private final int m_malusParade;
    /**
     * les points d'armure
     */
    private final int m_points;
    /**
     * le type d'armure
     */
    private final int m_type;

    /**
     * constructeur d'armure
     */
    Armure(int p_points, int p_type, int p_malusEsquive, int p_malusParade)
    {
	m_type = p_type;
	m_points = p_points;
	m_malusEsquive = p_malusEsquive;
	m_malusParade = p_malusParade;
    }

    /**
     *
     * @param p_typArm
     * @return renvoie le bonus au ND effectif offert par cette armure contre un
     * type d'arme donné
     */
    int getBonusND(int p_typArm)
    {
	return UPReference.getInstance().getArmureBonusND(m_points, p_typArm, m_type);
    }

    /**
     * @return the m_malusEsquive
     */
    int getMalusEsquive()
    {
	return m_malusEsquive;
    }

    /**
     * @return the m_malusParade
     */
    int getMalusParade()
    {
	return m_malusParade;
    }

    /**
     *
     * @param p_typArm
     * @return renvoie la réduction effective des dégâts offerte par cette
     * armure contre un type d'arme donné
     */
    int getRedDegats(int p_typArm)
    {
	return UPReference.getInstance().getArmureRedDegats(m_points, p_typArm, m_type);
    }
}
