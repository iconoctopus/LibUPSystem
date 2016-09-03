package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;

/**
 * classe représentant une armure et encapsulant les traitements des effets de
 * celle-ci sur les attaques entrantes
 *
 * @author iconoctopus
 */
public class Armure
{

    /**
     * le type d'armure, c'est par défaut le type le plus élevé parmi les pièces
     * portées (hors localisation)
     */
    private int m_type;
    /**
     * les points d'armure, viennent des ajouts de pièces
     */
    private int m_points;
    /**
     * le malus d'esquive de l'armure, provient de la somme de ceux des pièces
     */
    private int m_malusEsquive;
    /**
     * le malus de parade de l'armure, provient de la somme de ceux des pièces
     */
    private int m_malusParade;

    /**
     * liste des localisations et leurs occupations, c'est un tableau de
     * tableaux car il ya des localisations doubles
     */
    private final ArrayList<PieceArmure> m_listPieces;

    /**
     * constructeur d'armure
     */
    Armure()
    {
	//initialisation de l'armure à vide
	m_listPieces = new ArrayList<>();
	m_type = 0;
	m_points = 0;
	m_malusEsquive = 0;
	m_malusParade = 0;
    }

    /**
     * ajouter une pièce à l'armure. Aucun contrôle n'est effectué sur la
     * légalité de l'ajout. Tout est géré au niveau de l'inventaire. Ne pas
     * utiliser indépendamment cette méthode hors de l'inventaire.
     *
     * @param p_piece ignoré si pièce unique
     */
    final void addPiece(PieceArmure p_piece)
    {
	int type = p_piece.getType();

	m_points += p_piece.getNbpoints();
	m_malusEsquive += p_piece.getMalusEsquive();
	m_malusParade += p_piece.getMalusParade();
	if (m_type < type)
	{
	    m_type = type;//on considère toujours le meilleur type porté pour les coups non-ciblés
	}
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
}
