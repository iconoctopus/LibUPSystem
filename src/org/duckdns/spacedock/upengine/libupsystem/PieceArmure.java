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
public class PieceArmure implements Iprotection
{

    /**
     * le libellé de cette pièce, construit à partir de son matériau et de sa
     * nature
     */
    private final String m_libelle;
    /**
     * le nombre de points de cette pièce
     */
    private final int m_nbpoints;
    /**
     * le type de cette pièce
     */
    private final int m_type;
    /**
     * la localisation de la pièce
     */
    private final int m_localisation;

    /**
     * costructeur de pièces d'armure
     *
     * @param p_idPiece
     * @param p_type
     * @param p_materiau
     */
    public PieceArmure(int p_idPiece, int p_type, int p_materiau)
    {
	m_type = p_type;
	UPReferenceArmures referenceArmures = UPReferenceArmures.getInstance();
	UPReferenceSysteme referenceSys = UPReferenceSysteme.getInstance();

	//construction du nom
	if (p_type == 0)
	{//armure ancienne
	    m_libelle = referenceArmures.getLblPiece(p_idPiece) + " " + referenceSys.getCollectionLibelles().interArmure + " " + referenceArmures.getLblMateriauArmureAncienne(p_materiau);
	}
	else
	{//armure moderne
	    m_libelle = referenceArmures.getLblPiece(p_idPiece) + " " + referenceArmures.getLblTypeArmure(p_type) + " " + referenceSys.getCollectionLibelles().liaison + " " + referenceSys.getCollectionLibelles().facture + " " + referenceArmures.getLblMateriauArmure(p_materiau);
	}
	m_nbpoints = referenceArmures.getPtsPiece(p_idPiece, p_materiau);
	m_localisation = referenceArmures.getLocalisation(p_idPiece);
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
    public int getLocalisation()
    {
	return m_localisation;
    }

    /**
     * @return the m_nbpoints
     */
    @Override
    public int getNbPoints()
    {
	return m_nbpoints;
    }

    /**
     * @return the m_nbpoints
     */
    @Override
    public int getType()
    {
	return m_type;
    }
}
