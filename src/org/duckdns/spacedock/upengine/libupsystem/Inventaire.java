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

import java.util.EnumMap;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 * encapsule l'équipement d'un personnage
 *
 * @author ykonoclast
 */
public class Inventaire
{

    /**
     * main considérée comme portant l'arme principale
     */
    private Lateralisation m_cotePrincipal;
    /**
     * le diagramme contenant les pièces d'armures et armes portées par le
     * personnage
     */
    private final EnumMap<ZoneEmplacement, Emplacement> m_diagrammeEmplacement;
    /**
     * le malus d'esquive de l'armure, provient de la somme de ceux des pièces
     */
    private int m_malusEsquive;
    /**
     * le malus de parade de l'armure, provient de la somme de ceux des pièces
     */
    private int m_malusParade;
    /**
     * les points d'armure, viennent des ajouts de pièces
     */
    private int m_pointsArmure;
    /**
     * le type d'armure, c'est par défaut le type le plus élevé parmi les pièces
     * portées (hors localisation)
     */
    private int m_typeArmure;

    /**
     * constructeur sans éléments initiaux : on initialise quand même pour
     * assurer que l'ajout sera possible
     */
    public Inventaire()
    {
	m_diagrammeEmplacement = new EnumMap<ZoneEmplacement, Emplacement>(ZoneEmplacement.class);
	m_diagrammeEmplacement.put(ZoneEmplacement.BRASDROIT, new Emplacement(2));
	m_diagrammeEmplacement.put(ZoneEmplacement.BRASGAUCHE, new Emplacement(2));
	m_diagrammeEmplacement.put(ZoneEmplacement.TETE, new Emplacement(0));
	m_diagrammeEmplacement.put(ZoneEmplacement.CORPS, new Emplacement(1));
	m_diagrammeEmplacement.put(ZoneEmplacement.MAINDROITE, new EmplacementMain(3));
	m_diagrammeEmplacement.put(ZoneEmplacement.MAINGAUCHE, new EmplacementMain(3));
	m_diagrammeEmplacement.put(ZoneEmplacement.JAMBEDROITE, new Emplacement(4));
	m_diagrammeEmplacement.put(ZoneEmplacement.JAMBEGAUCHE, new Emplacement(4));
	m_diagrammeEmplacement.put(ZoneEmplacement.PIEDDROIT, new Emplacement(5));
	m_diagrammeEmplacement.put(ZoneEmplacement.PIEDGAUCHE, new Emplacement(5));

	m_typeArmure = 0;
	m_malusEsquive = 0;
	m_malusParade = 0;
	m_pointsArmure = 0;

	m_cotePrincipal = Lateralisation.DROITE;//par défaut
    }

    /**
     * méthode ajoutant une arme dans l'une des deux mains
     *
     * @param p_arme
     * @param p_cote
     */
    public void addArme(Arme p_arme, Lateralisation p_cote)
    {
	EmplacementMain mainCible;
	EmplacementMain autreMain;

	//identification de la main porteuse et de l'arme opposée
	if (p_cote == Lateralisation.DROITE)
	{
	    mainCible = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	    autreMain = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	}
	else
	{
	    mainCible = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	    autreMain = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	}

	//vérification de l'occupation des mains
	if (!mainCible.isOccupeArmeBouclier())
	{
	    if (p_arme.getNbMainsArme() == 2)
	    {
		if (!autreMain.isOccupeArmeBouclier())
		{
		    autreMain.setArmeDeuxMainsDansMainPrincipaleTrue();
		}
		else
		{
		    //exception autre main pas libre mais arme à deux mains
		    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("autre_main_pas_libre"));
		}
	    }
	    mainCible.setArme(p_arme);//ajout effectif de l'arme
	}
	else
	{
	    //exception main principale pas libre
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_pas_libre"));
	}
    }

    /**
     * ajoute un bouclier dans la main indiquée (méthode spéciale pour boucliers
     * car c'est le gantelet qui est dans la main)
     *
     * @param p_bouclier
     * @param p_cote
     */
    public void addBouclier(PieceArmure p_bouclier, Lateralisation p_cote)
    {
	if (p_bouclier.isBouclier())
	{
	    EmplacementMain main;

	    //identification de la main
	    if (p_cote == Lateralisation.DROITE)
	    {
		main = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	    }
	    else
	    {
		main = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	    }

	    if (!main.isOccupeArmeBouclier())
	    {
		main.setBouclier(p_bouclier);
		reScanArmure();
	    }
	    else
	    {
		//exception emplacement non libre
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_pas_libre"));
	    }
	}
	else
	{
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("pas_bouclier"));
	}
    }

    /**
     * méthode ajoutant une piece d'armure dans un des emplacements. Ne peut
     * être employée avec un bouclier (c'est le gantelet qui est dans la main)
     *
     * @param p_piece
     * @param p_zone
     */
    public void addPieceArmure(PieceArmure p_piece, ZoneEmplacement p_zone)
    {
	if (!p_piece.isBouclier())//pièce d'armure classique
	{
	    Emplacement emplacement = m_diagrammeEmplacement.get(p_zone);
	    if (!emplacement.isOccupeArmure())
	    {
		emplacement.setPiece(p_piece);
		reScanArmure();
	    }
	    else
	    {
		//exception emplacement non libre
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_pas_libre"));
	    }
	}
	else
	{
	    //cette méthode ne s'applique pas aux boucliers
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("est_bouclier"));
	}
    }

    /**
     *
     * @return l'arme couramment tenue
     */
    public Arme getArmeCourante()
    {
	EmplacementMain mainPrincipale;
	if (m_cotePrincipal == Lateralisation.DROITE)
	{
	    mainPrincipale = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	}
	else
	{
	    mainPrincipale = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	}
	return mainPrincipale.getArme();
    }

    /**
     * pas de vérification, le retour peut très bien être nul
     *
     * @param p_cote
     * @return
     */
    public PieceArmure getBouclier(Lateralisation p_cote)
    {
	EmplacementMain main;
	if (p_cote == Lateralisation.DROITE)
	{
	    main = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	}
	else
	{
	    main = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	}
	return main.getBouclier();
    }

    /**
     * indique quelle main sera utilisée par défaut
     *
     * @param p_cote
     */
    public void setCotePrincipal(Lateralisation p_cote)
    {
	m_cotePrincipal = p_cote;
    }

    /**
     * pas de vérification le retour peut très bien être nul
     *
     * @param p_zone
     * @return
     */
    public PieceArmure getPieceArmure(ZoneEmplacement p_zone)
    {
	return m_diagrammeEmplacement.get(p_zone).getPiece();
    }

    /**
     *
     * @param p_cote
     */
    public void removeArme(Lateralisation p_cote)
    {
	EmplacementMain mainPrincipale;
	EmplacementMain autreMain;

	//identification de la main porteuse et de l'arme opposée
	if (p_cote == Lateralisation.DROITE)
	{
	    mainPrincipale = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	    autreMain = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	}
	else
	{
	    mainPrincipale = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	    autreMain = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	}

	//vérification de l'occupation des mains
	if (mainPrincipale.isOccupeArmeBouclier())
	{
	    if (mainPrincipale.getArme().getNbMainsArme() == 2)
	    {
		autreMain.setArmeDeuxMainsDansMainPrincipaleFalse();
	    }
	    mainPrincipale.removeArme();
	}
	else
	{
	    //exception main principale non occupée
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	}
    }

    /**
     * retire un bouclier de la main indiquée (méthode spéciale pour boucliers
     * car c'est le gantelet qui est dans la main)
     *
     * @param p_cote
     */
    public void removeBouclier(Lateralisation p_cote)
    {
	EmplacementMain main;

	//identification de la main
	if (p_cote == Lateralisation.DROITE)
	{
	    main = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINDROITE);
	}
	else
	{
	    main = (EmplacementMain) m_diagrammeEmplacement.get(ZoneEmplacement.MAINGAUCHE);
	}

	//retrait du bouclier
	if (main.isOccupeArmeBouclier())
	{
	    main.removeBouclier();
	    reScanArmure();
	}
	else
	{
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	}
    }

    /**
     * supprime la piece d'armure de la zone désignée, ne s'applique pas aux
     * boucliers (c'est le gantelet qui est dans la main)
     *
     * @param p_zone
     */
    public void removePieceArmure(ZoneEmplacement p_zone)
    {
	Emplacement emplacement = m_diagrammeEmplacement.get(p_zone);
	if (emplacement.isOccupeArmure())
	{
	    emplacement.removePiece();
	    reScanArmure();
	}
	else
	{
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	}
    }

    /**
     * @return l'armure actuellement portée
     */
    Armure getArmure()
    {
	return new Armure(m_pointsArmure, m_typeArmure, m_malusEsquive, m_malusParade);
    }

    /**
     *
     * recalcule les caracs de l'armure courante
     */
    private void reScanArmure()
    {
	//on commence par réinitialiser l'armure
	m_typeArmure = 0;
	m_malusEsquive = 0;
	m_malusParade = 0;
	m_pointsArmure = 0;

	//l'armure remise à 0, on peut la faire remonter
	for (Emplacement e : m_diagrammeEmplacement.values())
	{
	    if (e.isOccupeArmure())
	    {
		scanPiece(e.getPiece());
	    }
	    if (e.isMain())
	    {
		if (((EmplacementMain) e).isOccupeArmeBouclier())
		{
		    PieceArmure bouclier = ((EmplacementMain) e).getBouclier();
		    if (bouclier != null)
		    {
			scanPiece(bouclier);
		    }
		}
	    }
	}
    }

    /**
     * analyse une pièce d'armure et la prend en compte dans les caracs d'armure
     * courante
     *
     * @param p_piece
     */
    private void scanPiece(PieceArmure p_piece)
    {
	int type = p_piece.getType();
	if (m_typeArmure < type)
	{
	    m_typeArmure = type;//on considère toujours le meilleur type porté pour les coups non-ciblés
	}

	m_pointsArmure += p_piece.getNbpoints();
	m_malusEsquive += p_piece.getMalusEsquive();
	m_malusParade += p_piece.getMalusParade();
    }

    /**
     * classe représentant le contenu d'un emplacement d'inventaire
     */
    private static class Emplacement
    {

	/**
	 * la pièce d'armure de l'emplacement
	 */
	private PieceArmure m_piece;
	/**
	 * si une pice d'armure occupe l'emplacement
	 */
	private boolean m_occupeArmure;
	/**
	 * la localisation que peuvent avoir les éléments affectés dans ce
	 * contenu
	 */
	private final int m_localisation;

	/**
	 *
	 * @param p_localisation la seule localisation qu'acceptera ce contenu
	 */
	private Emplacement(int p_localisation)
	{
	    m_occupeArmure = false;
	    m_localisation = p_localisation;
	}

	boolean isMain()
	{
	    return false;
	}

	/**
	 * @return la pièce de l'emplacement
	 */
	private PieceArmure getPiece()
	{
	    return m_piece;
	}

	/**
	 * place une pièce d'armure dans l'emplacement
	 *
	 * @param p_piece
	 */
	private void setPiece(PieceArmure p_piece)
	{
	    if (isOccupeArmure())//si une pièce est déjà présente
	    {
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emp_occupe"));
	    }
	    else
	    {
		if (p_piece.getLocalisation() == m_localisation)
		{
		    m_piece = p_piece;
		    m_occupeArmure = true;//l'emplacement est maintenant occupé
		}
		else
		{
		    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("mauv_loca"));
		}
	    }
	}

	/**
	 * enlève une pièce et indique l'emplacement comme non rempli
	 */
	private void removePiece()
	{
	    if (isOccupeArmure())
	    {
		m_piece = null;
		m_occupeArmure = false;
	    }
	    else
	    {
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	    }
	}

	/**
	 *
	 * @return si l'emplacement est occupé par une pièce d'armure
	 */
	boolean isOccupeArmure()
	{
	    return m_occupeArmure;
	}
    }

    /**
     * classe dérivée spéciale pour les mains afin de contenir en plus une arme
     * ou un bouclier (en plus du gantelet)
     */
    private static class EmplacementMain extends Emplacement
    {

	/**
	 * l'arme occupant l'emplacement
	 */
	private Arme m_arme;
	/**
	 * le bouclier occupant l'emplacement
	 */
	private PieceArmure m_bouclier;
	/**
	 * si une arme occupe la main
	 */
	private boolean m_occupeArme;
	/**
	 * si un bouclier occupe la main
	 */
	private boolean m_occupeBouclier;
	/**
	 * si l'autre main (supposée principale) tient une arme à 2 mains
	 */
	private boolean m_armeDeuxMainsDansMainPrincipale;

	/**
	 * construit la main initialement vide
	 */
	private EmplacementMain(int p_localisation)
	{
	    super(p_localisation);
	    m_occupeBouclier = false;
	    m_occupeArme = false;
	    m_armeDeuxMainsDansMainPrincipale = false;
	}

	/**
	 * place une arme dans l'emplacement et le rend occupé
	 *
	 * @param p_arme
	 */
	private void setArme(Arme p_arme)
	{
	    if (isOccupeArmeBouclier())
	    {
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emp_occupe"));
	    }
	    else
	    {
		m_arme = p_arme;
		m_occupeArme = true;
	    }
	}

	/**
	 *
	 * @return l'arme actuellement dans l'emplacement, éventuellement null
	 */
	private Arme getArme()
	{
	    return m_arme;
	}

	private PieceArmure getBouclier()
	{
	    return m_bouclier;
	}

	/**
	 * place un bouclier dans l'emplacement
	 *
	 * @param p_bouclier
	 */
	private void setBouclier(PieceArmure p_bouclier)
	{
	    if (isOccupeArmeBouclier())
	    {
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emp_occupe"));
	    }
	    else
	    {
		if (p_bouclier.isBouclier())
		{
		    m_bouclier = p_bouclier;
		    m_occupeBouclier = true;
		}
		else
		{
		    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("pas_bouclier"));
		}
	    }
	}

	/**
	 * enlève l'arme de la main et marque l'emplacement comme libre
	 */
	private void removeArme()
	{
	    if (m_occupeArme)
	    {
		m_arme = null;
		m_occupeArme = false;
	    }
	    else
	    {
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	    }
	}

	/**
	 * enlève le bouclier de la main et marque l'emplacement comme libre
	 */
	private void removeBouclier()
	{
	    if (m_occupeBouclier)
	    {
		m_bouclier = null;
		m_occupeBouclier = false;
	    }
	    else
	    {
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	    }
	}

	/**
	 * vient flagger cet emplacement occupé car l'autre main brandit une
	 * arme à deux mains
	 */
	private void setArmeDeuxMainsDansMainPrincipaleTrue()
	{
	    m_armeDeuxMainsDansMainPrincipale = true;
	}

	/**
	 * libère cette main car l'autre main lâche son arme à deux mains
	 */
	private void setArmeDeuxMainsDansMainPrincipaleFalse()
	{
	    m_armeDeuxMainsDansMainPrincipale = false;
	}

	/**
	 * si l'emplacement est occupé
	 *
	 * @return
	 */
	private boolean isOccupeArmeBouclier()
	{
	    return m_occupeArme || m_occupeBouclier || m_armeDeuxMainsDansMainPrincipale;
	}

	@Override
	boolean isMain()
	{
	    return true;
	}
    }

    /**
     * enum représentant les différentes localisation du corps d'un personnage à
     * fins d'inventaire. Il manque les yeux par exemple pour le combat.
     */
    public enum ZoneEmplacement
    {
	TETE, CORPS, JAMBEGAUCHE, JAMBEDROITE, PIEDGAUCHE, PIEDDROIT, BRASGAUCHE, BRASDROIT, MAINGAUCHE, MAINDROITE
    }

    /**
     * représente l'un des deux côtés
     */
    public enum Lateralisation
    {
	GAUCHE, DROITE
    }
}
