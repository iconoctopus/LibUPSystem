package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import java.util.Iterator;

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
     * liste des pièces incorporées dans l'armure
     */
    private final ArrayList<PieceArmure> m_listPieces = new ArrayList<>();

    /**
     * constructeur d'armure
     *
     * @param p_listPieces la liste des pièces initiales de cette armure
     */
    Armure(ArrayList<PieceArmure> p_listPieces)
    {
	Iterator iterator = p_listPieces.iterator();
	while (iterator.hasNext())
	{
	    addPiece((PieceArmure) iterator.next());
	}
    }

    /**
     * ajouter une pièce à l'armure qui effectue un contrôle sur le nombre
     * maximal de pièces d'un certain type
     *
     * @param p_piece
     */
    final void addPiece(PieceArmure p_piece)
    {
	int nbmax = p_piece.getnbMax();
	int type = p_piece.getType();
	int id = p_piece.getIdPiece();
	int dejaPortees = 0;

	for (Iterator i = m_listPieces.iterator(); i.hasNext();)//on parcourt la liste des pièces courantes pour vérifier combien de pièces du même type sont déjà portées
	{
	    PieceArmure pieceCourante = (PieceArmure) i.next();
	    if (pieceCourante.getIdPiece() == id)
	    {
		++dejaPortees;
	    }
	}
	if (dejaPortees < nbmax)//on peut encore ajouter une pièce de ce type
	{
	    m_listPieces.add(p_piece);
	    m_points += p_piece.getNbpoints();
	    m_malusEsquive += p_piece.getMalusEsquive();
	    m_malusParade += p_piece.getMalusParade();
	    if (m_type < type)
	    {
		m_type = type;//on considère toujours le meilleur type porté, cela évoluera avec la localisation
	    }
	}
	else
	{
	    ErrorHandler.ajoutPieceArmure(p_piece.toString());
	}
    }

    /**
     * retire la pièce possédant l'indice passé en paramétre dans la liste des
     * pièces de cette armure
     *
     * @param p_indice
     */
    final void removePiece(int p_indice)
    {
	PieceArmure piece = m_listPieces.get(p_indice);

	m_points -= piece.getNbpoints();
	m_malusEsquive -= piece.getMalusEsquive();
	m_malusParade -= piece.getMalusParade();
	m_listPieces.remove(p_indice);
	if (m_type == piece.getType())//le type de l'armure provenait de la pièce portée, il faut donc le réévaluer
	{
	    int typeCourant = 0;
	    for (Iterator i = m_listPieces.iterator(); i.hasNext();)//on parcourt la liste des pièces courantes pour trouver le type max
	    {

		PieceArmure pieceCourante = (PieceArmure) i.next();

		if (pieceCourante.getType() > typeCourant)
		{
		    typeCourant = pieceCourante.getType();
		}
	    }
	    m_type = typeCourant;
	}
    }

    /**
     *
     * @return une copie de la liste des pièces de cette armure
     */
    ArrayList<PieceArmure> getListPieces()
    {
	return new ArrayList<>(m_listPieces);
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

    /**
     * classe interne représentant une pièce individuelle d'une armure
     */
    static class PieceArmure
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
	 * le libellé de cette pièce, construit à partir de son matériau et de
	 * sa nature
	 */
	private final String m_libelle;
	/**
	 * le nombre de points de cette pièce
	 */
	private final int m_nbpoints;
	/**
	 * le nombre max de pièces de ce type pouvant être portées
	 */
	private final int m_nbmax;
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
	 */
	PieceArmure(int p_idPiece, int p_type, int p_materiau, boolean p_isBouclier)
	{//TODO si le type n'est pas "ancienne" alors il faut ignorer le matériau est utiliser celui par défaut tout en créant un nouveau matériau "technologique" (pour les libellés éventuels) et créer le libellé de la pièce avec le bon type ("casque blindé") par dérogation
	    m_idPiece = p_idPiece;
	    m_type = p_type;
	    m_materiau = p_materiau;
	    m_isBouclier = p_isBouclier;
	    UPReference reference = UPReference.getInstance();

	    m_nbpoints = p_isBouclier ? reference.getPointsBouclier(p_idPiece, p_materiau) : reference.getPtsArmure(m_idPiece, m_materiau);
	    m_libelle = p_isBouclier ? reference.getLblBouclier(m_idPiece) + " " + reference.libelles.interArme + " " + reference.getLblMateriauBouclier(m_materiau) : reference.getLblPiece(m_idPiece) + " " + reference.libelles.interArme + " " + reference.getLblMateriauArmure(m_materiau);
	    m_malusEsquive = p_isBouclier ? 0 : reference.getMalusEsquive(m_idPiece, m_materiau);
	    m_malusParade = p_isBouclier ? 0 : reference.getMalusParade(m_idPiece, m_materiau);
	    m_nbmax = p_isBouclier ? 1 : reference.getNbMaxPieces(m_idPiece);
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
	 * @return the m_idPiece
	 */
	int getnbMax()
	{
	    return m_nbmax;
	}

	/**
	 * -	* @return the m_libelle -
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
}
