/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.EnumMap;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 * encapsule l'équipement d'un personnage, pour l'instant très simple mais
 * pourra s'étoffer à terme
 *
 * @author iconoctopus
 */
public class Inventaire
{//TODO gérer la latéralisation avec la notion de main principale et secondaire et les bonus afférents (en lien avec combat à deux armes) pour l'instant une seule à la fois

    /**
     * le diagramme contenant les pièces d'armures et armes portées par le
     * personnage
     */
    private EnumMap<ZoneEmplacement, Emplacement> m_diagrammeEmplacement;

    /**
     * main considérée comme portant l'arme principale
     */
    private Lateralisation m_cotePrincipal;

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

	m_cotePrincipal = Lateralisation.DROITE;
    }

    /**
     * méthode ajoutant une arme dans l'une des deux mains
     *
     * @param p_arme
     * @param p_cote
     */
    public void addArme(Arme p_arme, Lateralisation p_cote)
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
	if (!mainPrincipale.isOccupeArmeBouclier())
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
	    mainPrincipale.setArme(p_arme);//ajout effectif de l'arme
	}
	else
	{
	    //exception main principale pas libre
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_pas_libre"));
	}
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
	    mainPrincipale.removeArmeBouclier();
	}
	else
	{
	    //exception main principale non occupée
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	}
    }

    /**
     * méthode ajoutant une piece d'armure dans un des emplacements. Si la pièce
     * est un bouclier alors l'affectation est particulière
     *
     * @param p_piece
     * @param p_zone
     */
    public void addPieceArmure(PieceArmure p_piece, ZoneEmplacement p_zone)
    {
	if (!p_piece.isBouclier())
	{
	    Emplacement emplacement = m_diagrammeEmplacement.get(p_zone);
	    if (!emplacement.isOccupeArmure())
	    {
		emplacement.setPiece(p_piece);
	    }
	    else
	    {
		//exception emplacement non libre
		ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_pas_libre"));
	    }
	}
	else
	{

	}
    }

    /**
     * ajoute un bouclier dans la main indiquée
     *
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
     * supprme la piece d'armure de la zone désignée avec traitement particulier
     * pour les boucliers
     *
     * @param p_zone
     */
    public void removePieceArmure(ZoneEmplacement p_zone)
    {
	Emplacement emplacement = m_diagrammeEmplacement.get(p_zone);
	if (emplacement.isOccupeArmure())
	{
	    emplacement.removePiece();
	}
	else
	{
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	}
    }

    /**
     * retire un bouclier de la main indiquée
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
	    main.removeArmeBouclier();
	}
	else
	{
	    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("emplacement_non_occupe"));
	}
    }

    /**
     *
     * @return une armure calculée dynamiquement en fonction des effets portés
     */
    public Armure getArmure()
    {
	Armure result = new Armure();
	for (Emplacement e : m_diagrammeEmplacement.values())
	{
	    if (e.isOccupeArmure())
	    {
		result.addPiece(e.getPiece());
	    }
	    if (e.isMain())
	    {
		if (((EmplacementMain) e).isOccupeArmeBouclier())
		{
		    PieceArmure bouclier = ((EmplacementMain) e).getBouclier();
		    if (bouclier != null)
		    {
			result.addPiece(bouclier);
		    }
		}
	    }
	}
	return (result);
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
     * pas de vérification le retour peut très bin être nul
     *
     * @param p_zone
     * @return
     */
    public PieceArmure getPieceArmure(ZoneEmplacement p_zone)
    {
	return m_diagrammeEmplacement.get(p_zone).getPiece();
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
	Emplacement(int p_localisation)
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
     * ou un bouclier
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
	 * si une arme ou un bouclier occupe l'emplacement
	 */
	private boolean m_occupeArmeBouclier;
	/**
	 * si l'autre main (supposée principale) tient une arme à 2 mains
	 */
	private boolean m_armeDeuxMainsDansMainPrincipale;

	/**
	 * construit la main initialement vide
	 */
	EmplacementMain(int p_localisation)
	{
	    super(p_localisation);
	    m_occupeArmeBouclier = false;
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
		m_occupeArmeBouclier = true;
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
		    m_occupeArmeBouclier = true;
		}
		else
		{
		    ErrorHandler.mauvaiseMethode(PropertiesHandler.getInstance("libupsystem").getErrorMessage("pas_bouclier"));
		}
	    }
	}

	/**
	 * enlève l'arme ou le bouclier de la main et marque l'emplacement comme
	 * libre
	 */
	private void removeArmeBouclier()
	{
	    if (isOccupeArmeBouclier())
	    {
		m_arme = null;
		m_bouclier = null;
		m_occupeArmeBouclier = false;
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
	boolean isOccupeArmeBouclier()
	{
	    return m_occupeArmeBouclier || m_armeDeuxMainsDansMainPrincipale;
	}

	@Override
	boolean isMain()
	{
	    return true;
	}
    }
}
