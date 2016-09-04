/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

/**
 * classe interne représentant une pièce individuelle d'une armure
 */
public class PieceArmure
{

    /**
     * l'indice de la pièce
     */
    private final int m_idPiece;
    /**
     * le type de la pièce
     */
    private final int m_type;
    /**
     * le matériau de la pièce
     */
    private final int m_materiau;
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
     * si la pièce est un boucliere
     */
    private final boolean m_isBouclier;

    /**
     * costructeur de pièces d'armure
     *
     * @param p_idPiece
     * @param p_type
     * @param p_materiau
     * @param p_isBouclier car ils ont leurs propres règles
     */
    PieceArmure(int p_idPiece, int p_type, int p_materiau, boolean p_isBouclier)
    {
	m_idPiece = p_idPiece;
	m_type = p_type;
	UPReference reference = UPReference.getInstance();
	m_materiau = p_materiau;
	if (p_type == 0)
	{
	    m_libelle = reference.getLblPiece(m_idPiece, p_isBouclier) + " " + reference.libelles.interArme + " " + reference.getLblMateriauArmure(m_materiau, p_isBouclier);
	}
	else
	{
	    m_libelle = reference.getLblPiece(m_idPiece, p_isBouclier) + " " + reference.getLblTypeArmure(p_type);
	}
	m_isBouclier = p_isBouclier;
	m_nbpoints = reference.getPtsArmure(p_idPiece, p_materiau, p_isBouclier);
	m_malusEsquive = reference.getMalusEsquive(m_idPiece, m_materiau, p_isBouclier);
	m_malusParade = reference.getMalusParade(m_idPiece, m_materiau, p_isBouclier);
	m_localisation = reference.getLocalisation(m_idPiece, p_isBouclier);
    }

    /**
     * @return the m_idPiece
     */
    int getIdPiece()
    {
	return m_idPiece;
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
     * @return the m_localisation
     */
    int getLocalisation()
    {
	return m_localisation;
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
     * @return the m_materiau
     */
    int getMateriau()
    {
	return m_materiau;
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
