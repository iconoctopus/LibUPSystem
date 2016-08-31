package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import java.util.Iterator;
import org.duckdns.spacedock.upengine.libupsystem.Inventaire.Lateralisation;

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
    private final ArrayList<ArrayList<PieceArmure>> m_diagrammeOccupation;

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
     * retire la pièce localisée et latéralisée comme spécifié
     *
     * @param p_localisation
     * @param p_cote
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
}
