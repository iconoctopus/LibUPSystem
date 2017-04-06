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
 * classe représentant une pièce individuelle d'une armure
 */
public class PieceArmure
{

    /**
     * si la pièce est un boucliere
     */
    private final boolean m_isBouclier;
    /**
     * le libellé de cette pièce, construit à partir de son matériau et de sa
     * nature
     */
    private final String m_libelle;
    /**
     * la localisation de la pièce
     */
    private final int m_localisation;
    /**
     * le malus d'esquive infligé par cette piècé
     */
    private final int m_malusEsquive;
    /**
     * le malus de parade infligé par cette pièce
     */
    private final int m_malusParade;
    /**
     * le nombre de points de cette pièce
     */
    private final int m_nbpoints;
    /**
     * le type de la pièce
     */
    private final int m_type;

    /**
     * costructeur de pièces d'armure
     *
     * @param p_idPiece
     * @param p_type
     * @param p_materiau
     * @param p_isBouclier car ils ont leurs propres règles
     */
    public PieceArmure(int p_idPiece, int p_type, int p_materiau, boolean p_isBouclier)
    {
	m_type = p_type;
	UPReferenceArmures referenceArmures = UPReferenceArmures.getInstance();
	UPReferenceSysteme referenceSys = UPReferenceSysteme.getInstance();

	//construction du nom
	if (p_type == 0)
	{
	    m_libelle = referenceArmures.getLblPiece(p_idPiece, p_isBouclier) + " " + referenceSys.getCollectionLibelles().interArmure + " " + referenceArmures.getLblMateriauArmure(p_materiau, p_isBouclier);
	}
	else
	{
	    m_libelle = referenceArmures.getLblPiece(p_idPiece, p_isBouclier) + " " + referenceArmures.getLblTypeArmure(p_type);
	}

	m_isBouclier = p_isBouclier;
	m_nbpoints = referenceArmures.getPtsArmure(p_idPiece, p_materiau, p_isBouclier);
	m_malusEsquive = referenceArmures.getMalusEsquive(p_idPiece, p_materiau, p_isBouclier);
	m_malusParade = referenceArmures.getMalusParade(p_idPiece, p_materiau, p_isBouclier);
	m_localisation = referenceArmures.getLocalisation(p_idPiece, p_isBouclier);
    }

    /**
     * @return the m_libelle -
     */
    @Override

    public String toString()
    {
	return m_libelle;
    }

    /**
     * @return the m_localisation
     */
    int getLocalisation()
    {
	return m_localisation;
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
     * @return the m_nbpoints
     */
    int getNbpoints()
    {
	return m_nbpoints;
    }

    /**
     * @return the m_type
     */
    int getType()
    {
	return m_type;
    }

    /**
     * @return the m_isBouclier
     */
    boolean isBouclier()
    {
	return m_isBouclier;
    }
}
