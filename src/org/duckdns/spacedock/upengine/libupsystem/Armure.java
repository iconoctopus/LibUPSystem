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
     * liste des localisations et leurs occupations, c'est un tableau de
     * tableaux car il ya des localisations doubles
     */
    private ArrayList<ArrayList<PieceArmure>> m_diagrammeOccupation;

    /**
     * constructeur d'armure
     *
     * @param p_listPieces la liste des pièces initiales de cette armure
     */
    Armure()
    {
	//construction du diagramme d'occupation
	UPReference reference = UPReference.getInstance();
	int locaNumber = reference.getLocaNumber();
	m_diagrammeOccupation = new ArrayList<>();
	for (int i = 0; i < locaNumber; i++)
	{
	    m_diagrammeOccupation.add(new ArrayList<PieceArmure>());
	    m_diagrammeOccupation.get(i).add(null);//initialisé à null car non occupé par défaut
	    if (reference.isLocaDouble(i))//si c'est une localisation double on ajoute une case pour le deuxième exeplaire éventuel
	    {
		m_diagrammeOccupation.get(i).add(null);
	    }
	}
    }

    /**
     * ajouter une pièce à l'armure qui effectue un contrôle sur le nombre
     * maximal de pièces d'un certain type
     *
     * @param p_piece
     * @param p_cote le côté gauche ou droit (enum déclaré dans cette classe)
     * ignoré si pièce unique
     */
    final void addPiece(PieceArmure p_piece, Lateralisation p_cote)
    {
	int loca = p_piece.getLocalisation();
	int type = p_piece.getType();
	int cote;

	if (!UPReference.getInstance().isLocaDouble(p_piece.getLocalisation()))
	{
	    cote = 0;//par défaut, GAUCHE valant 0, on applique cette valeur aux pièce non latéralisées, ignorant le paramétre
	}
	else//la pièce est latéralisée on utilise donc le paramétre
	{
	    cote = (p_cote == Lateralisation.GAUCHE) ? 0 : 1;
	}

	if (m_diagrammeOccupation.get(loca).get(cote) == null)
	{//l'emplacement est libre, on y installe la pièce
	    m_points += p_piece.getNbpoints();
	    m_malusEsquive += p_piece.getMalusEsquive();
	    m_malusParade += p_piece.getMalusParade();
	    if (m_type < type)
	    {
		m_type = type;//on considère toujours le meilleur type porté pour les coups non-ciblés
	    }
	    m_diagrammeOccupation.get(loca).set(cote, p_piece);
	}
	else
	{//erreur : l'emplacement n'est pas libre
	    ErrorHandler.ajoutPieceArmure(p_piece.toString());
	}
    }

    /**
     * retire la pièce possédant l'indice passé en paramétre dans la liste des
     * pièces de cette armure
     *
     * @param p_indice
     */
    final void removePiece(int p_localisation, Lateralisation p_cote)
    {
	int cote = p_cote == Lateralisation.GAUCHE ? 0 : 1;
	PieceArmure piece = m_diagrammeOccupation.get(p_localisation).get(cote);

	m_points -= piece.getNbpoints();
	m_malusEsquive -= piece.getMalusEsquive();
	m_malusParade -= piece.getMalusParade();
	m_diagrammeOccupation.get(p_localisation).set(cote, null);//on supprime la pièce

	if (m_type == piece.getType())//le type de l'armure provenait de la pièce portée, il faut donc le réévaluer
	{
	    int typeCourant = 0;
	    for (Iterator i = m_diagrammeOccupation.iterator(); i.hasNext();)//on parcourt la liste des pièces courantes pour trouver le type max
	    {//parcours des localisations
		ArrayList<PieceArmure> locaCourante = (ArrayList<PieceArmure>) i.next();
		for (Iterator j = locaCourante.iterator(); j.hasNext();)
		{//parcours du côté gauche puis droit
		    PieceArmure pieceCourante = (PieceArmure) j.next();
		    if (pieceCourante.getType() > typeCourant)
		    {//si le type de la pièce considérée est meilleur que le dernier retenu
			typeCourant = pieceCourante.getType();
		    }
		}
	    }
	    m_type = typeCourant;
	}
    }

    /**
     *
     * @return une copie de la liste des pièces de cette armure
     */
    ArrayList<ArrayList<PieceArmure>> getListPieces()
    {
	return new ArrayList<>(m_diagrammeOccupation);
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

    public enum Lateralisation
    {
	GAUCHE, DROITE
    }
}
