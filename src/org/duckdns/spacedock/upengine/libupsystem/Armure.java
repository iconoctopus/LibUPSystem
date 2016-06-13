package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import java.util.Iterator;
//TODO : gérer les boucliers, la référence est prête, ajouter une autre classe interne, peut être en créant une interface ou une superclasse dont hériteraient bouclier et PieceArmure

public class Armure
{
    //TODO la localisation n'est pas encore gérée

    int getRedDegats(int p_typArm)
    {
//TODO implémenter
    }

    int getAugND(int p_typArm)
    {
//TODO implémenter
    }

    private int m_type;//ancienne, moderne, blindage, energetique, respectivement de 0 à 3
    private int m_points; //nombre de points d'armure
    private int m_malusEsquive;
    private int m_malusParade;
    private ArrayList<PieceArmure> m_listPieces = new ArrayList<>();

    Armure(ArrayList<PieceArmure> p_listPieces)
    {
	Iterator iterator = p_listPieces.iterator();
	while(iterator.hasNext())
	{
	    addPiece((PieceArmure) iterator.next());
	}
    }

    final void addPiece(PieceArmure p_piece)
    {
	int nbmax = p_piece.getnbMax();
	int type = p_piece.getType();
	int id = p_piece.getIdPiece();
	int dejaPortees = 0;
	//Iterator iterator = ;

	for(Iterator i = m_listPieces.iterator(); i.hasNext();)
	{
	    PieceArmure pieceCourante = (PieceArmure) i.next();
	    if(pieceCourante.getIdPiece() == id)
	    {
		++dejaPortees;
	    }
	}
	if(dejaPortees < nbmax)
	{
	    m_listPieces.add(p_piece);
	    m_points += p_piece.getNbpoints();
	    m_malusEsquive += p_piece.getMalusEsquive();
	    m_malusParade += p_piece.getMalusParade();
	    if(m_type < type)
	    {
		m_type = type;//on considère toujours le meilleur type porté, cela évoluera avec la localisation
	    }
	}
    }

    class PieceArmure
    {

	private final int m_idPiece;
	private final int m_type;
	private final int m_materiau;
	private final String m_libelle;
	private final int m_nbpoints;
	private final int m_nbmax;
	private final int m_malusEsquive;
	private final int m_malusParade;

	PieceArmure(int p_idPiece, int p_type, int p_materiau)
	{
	    m_idPiece = p_idPiece;
	    m_type = p_type;
	    m_materiau = p_materiau;
	    UPReference reference = UPReference.getInstance();
	    m_nbpoints = reference.getPtsArmure(m_idPiece, m_type, m_materiau);
	    m_libelle = reference.getLblPiece(m_idPiece) + " " + reference.libelles.interArme + " " + reference.getLblMateriauArmure(m_idPiece);
	    m_malusEsquive = reference.getMalusEsquive(m_idPiece);
	    m_malusParade = reference.getMalusParade(m_idPiece);
	    m_nbmax = reference.getNbMaxPieces(m_idPiece);
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
	 * @return the m_libelle
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

    }
}
