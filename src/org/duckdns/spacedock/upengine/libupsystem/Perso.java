package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import java.util.Collections;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;
import org.duckdns.spacedock.upengine.libupsystem.Arme.Degats;
import org.duckdns.spacedock.upengine.libupsystem.RollUtils.RollResult;

public class Perso
{

    /**
     * les actions du personnage dans ce tour sous la forme de la phase dans
     * laquelle l'action se déroule dans l'ordre des phases (donc tableau de la
     * taille de l'init) ,celles consommées sont fixées à 11
     */
    private ArrayList<Integer> m_actions;
    /**
     * l'indice de l'action courantes dans le tableau des actions
     */
    private int m_actionCourante;
    /**
     * le nom du personage
     */
    private String m_libellePerso;
    /**
     * non finale pour augment à l'xp et définition hors constructeur
     */
    private CoupleJauge m_jaugeSanteInit;
    /**
     * non finale pour augment à l'xp et définition hors constructeur
     */
    private CoupleJauge m_jaugeFatigueForceDAme;
    /**
     * rassemble armes et armures du personnage ainsi que quelques
     * fonctionalités utiles notamment les armes et armures courantes
     */
    private final Inventaire m_inventaire = new Inventaire();
    /**
     * dans l'ordre : 0:Physique ; 1:Coordination ; 2:Mental ; 3:Volonté ;
     * 4:Sociabilité
     */
    private final int[] m_traits;
    /**
     * arbre des domaines/compétences du personnage
     */
    private final ArbreDomaines m_arbreDomaines;

    /**
     * Constructeur de Perso prenant des caractéristiques en paramétres. Il est
     * possible de le modifier par la suite on peut l'initialiser avec presque
     * rien, c'est le constructeur du cas général
     *
     * @param p_traits
     * @param p_arbre
     */
    public Perso(int[] p_traits, ArbreDomaines p_arbre)
    {
	if (p_traits.length != 5)
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("nbtraits") + ":" + p_traits.length);
	}
	for (int i = 0; i < p_traits.length; ++i)
	{
	    if (p_traits[i] < 0)
	    {
		ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("trait") + ":" + p_traits[i]);
	    }
	}
	m_traits = p_traits;
	m_arbreDomaines = p_arbre;
    }

    /**
     * constructeur produisant des PNJ générés par rang de menace (RM)
     *
     * @param p_RM
     */
    public Perso(int p_RM)
    {
	if (p_RM < 1)
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("rang") + ":" + p_RM);
	}
	//configuration des traits
	m_traits = new int[5];
	m_traits[0] = p_RM;//physique
	m_traits[1] = p_RM;//coordination
	m_traits[2] = p_RM - 1;//mental
	m_traits[3] = p_RM - 1;//volonté
	m_traits[4] = p_RM - 1;//sociabilité

	//configuration automatique des autres caractéristiques maintenant possible car les traits sont générés
	initPerso();

	//configuration des caractéristiques de combat une fois que l'arbre des domaines est généré
	//configuration du domaine corps à corps
	m_arbreDomaines = new ArbreDomaines();
	m_arbreDomaines.setRangDomaine(3, p_RM);
	for (int i = 0; i < UPReference.getInstance().getListComp(3).size(); i++)
	{
	    m_arbreDomaines.setRangComp(3, i, p_RM);
	}

	//idem pour tout le domaine combat à distance
	m_arbreDomaines.setRangDomaine(4, p_RM);
	for (int i = 0; i < UPReference.getInstance().getListComp(4).size(); i++)
	{
	    m_arbreDomaines.setRangComp(4, i, p_RM);
	}

	//on ajoute des rangs en esquive
	m_arbreDomaines.setRangDomaine(2, p_RM);
	m_arbreDomaines.setRangComp(2, 0, p_RM);

	m_libellePerso = PropertiesHandler.getInstance("libupsystem").getString("lbl_perso_std") + p_RM;
    }

    /**
     * initialise les caractéristiques hors traits du personnage
     */
    private void initPerso()
    {
	initJauges();
	genInit();
    }

    /**
     * initialise les jauges du personnage avec le tableau de ses traits, doit
     * donc être appelé par le onstructeur après cette initialisation
     */
    private void initJauges()
    {
	int traitMin = m_traits[0];
	for (int i = 1; i < m_traits.length; ++i)
	{
	    if (m_traits[i] < traitMin)
	    {
		traitMin = m_traits[i];
	    }
	}
	m_jaugeFatigueForceDAme = new CoupleJauge(m_traits[0], m_traits[3], traitMin);
	m_jaugeSanteInit = new CoupleJauge(m_traits[0], m_traits[3], m_traits[2], m_traits[1]);
    }

    /**
     * génère l'initiative du personnage, devrait être appelée dans le
     * constructeur mais par la suite contrôlée de l'extérieur
     */
    public final void genInit()
    {
	int initiative = m_jaugeSanteInit.getRemplissage_externe();
	m_actionCourante = 0;
	ArrayList<Integer> tabResult = new ArrayList<>();
	if (initiative > 0)
	{
	    for (int i = 0; i < initiative; i++)
	    {
		tabResult.add(RollUtils.lancer(1, 1, true));
	    }
	}
	Collections.sort(tabResult);
	m_actions = tabResult;
    }

    /**
     * fait effectuer au personnage un jet de l'une de ses compétences. Appelé
     * en interne par les méthodes d'attaque qui effectuent les pré-traitements
     * pour aboutir aux caractéristiques finales du jet.
     *
     * @param p_ND
     * @param p_comp
     * @param p_domaine
     * @param p_modifNbLances
     * @param p_modifNbGardes
     * @param p_modifScore
     * @param p_trait
     * @return le résultat du jet
     */
    public final RollUtils.RollResult effectuerJetComp(int p_ND, int p_comp, int p_domaine, int p_modifNbLances, int p_modifNbGardes, int p_modifScore, int p_trait)
    {
	return m_arbreDomaines.effectuerJetComp(p_domaine, p_comp, p_trait, p_ND, p_modifNbLances, p_modifNbGardes, p_modifScore, isSonne());
    }

    /**
     * fait effectuer au personnage un jet avec l'un de ses traits purs.
     *
     * @param p_ND
     * @param p_modifNbLances
     * @param p_modifNbGardes
     * @param p_modifScore
     * @param p_trait
     * @return le résultat du jet
     */
    public final RollUtils.RollResult effectuerJetTrait(int p_ND, int p_modifNbLances, int p_modifNbGardes, int p_modifScore, int p_trait)
    {
	return RollUtils.extraireIncrements(RollUtils.lancer(m_traits[p_trait], m_traits[p_trait], isSonne()), p_ND);
    }

    /**
     * Fait effectuer une attaque au corps à corps au personnage. Le personnage
     * attaquera systématiquement avec l'arme courante, il faut donc la
     * configurer avant. Cette méthode vérifie que l'action est possible dans la
     * phase courante en fonction de l'init du perso, elle est donc conçue pour
     * le combat uniquement. Si l'on veut utiliser une autre arme il faut
     * d'abord la configurer comme arme courante. Une exception est levée si
     * l'on essaye d'attaquer en corps à corps avec une arme à distance (rien
     * n'interdit d'entrer une distance 0 dans l'autre méthode adaptée). Si
     * aucune arme n'est équipée on utilise par défaut les mains nues.
     *
     * Il est important de garder la génération des dégâts séparée et déclenchée
     * depuis l'extérieur afin que le contrôleur puisse choisir d'utiliser les
     * incréments pour autre chose que des dégâs (ciblage, autoriser parade...).
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @return
     */
    public final RollUtils.RollResult attaquerCaC(int p_phaseActuelle, int p_ND)
    {
	Arme arme = m_inventaire.getArmeCourante();
	int catArm = 0;//mains nues par défaut
	if (arme != null)//une arme est équipée
	{
	    if (arme.getMode() == 0)//vérification qu'il s'agit bien d'une arme de corps à corps
	    {
		catArm = arme.getCategorie();
	    }
	    else
	    {
		UPErrorHandler.mauvaisModeAttaque();//TODO ce cas d'erreur n'est pas testé
	    }
	}
	return effectuerAttaque(p_phaseActuelle, p_ND, catArm * 2, 3, 0, 0, 0);//par convention les comp d'attaque de CaC sont à cat*2, les parades sont à Cat*2+1
    }

    /**
     * Fait effectuer une attaque à distance au personnage. Cette méthode
     * vérifie que l'action est possible dans la phase courante en fonction de
     * l'init du perso, elle est donc conçue pour le combat uniquement. On
     * utilise l'arme courante ou les mains nues. Si l'on veut utiliser une
     * autre arme il faut d'abord la configurer comme arme courante. Une
     * exception est levée si l'on essaye d'attaquer à distance avec une arme de
     * corps à corps (rien n'interdit d'entrer une distance 0 dans l'autre
     * méthode adaptée). Aucune vérification n'est effectuée sur le magasin
     * actuel de l'arme. Si celui-ci n'est pas suffisant l'arme lèvera une
     * exception.
     *
     * Il est important de garder la génération des dégâts séparée et déclenchée
     * depuis l'extérieur afin que le contrôleur puisse choisir d'utiliser les
     * incréments pour autre chose que des dégâs (ciblage, autoriser parade...).
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @param p_distance la distance de la cible
     * @param p_nbCoups nombre de tirs effectués (pour la règle
     * @return
     */
    public RollResult attaquerDist(int p_phaseActuelle, int p_ND, int p_distance, int p_nbCoups)
    {
	RollResult result = new RollResult(0, false, 0);//raté par défaut
	ArmeDist arme = (ArmeDist) m_inventaire.getArmeCourante();
	int modDist = 0;
	if (arme.getMode() == 1)//vérification qu'il s'agit bien d'une arme à distance
	{
	    if (p_distance >= 0 && p_nbCoups > 0 && p_nbCoups <= 20)
	    {
		arme.consommerMun(p_nbCoups);//on consomme les coups, une exception sera levée si il n'y a pas assez de munitions, le code appelant devrait vérifier systématiquement cela
		if (p_distance <= arme.getPortee())//échec auto si distance > portée
		{
		    if (p_distance <= (int) Math.round((double) arme.getPortee() / (double) 2))
		    {//portée courte
			modDist -= arme.getMalusCourt();
		    }
		    else
		    {//portée longue
			modDist -= arme.getMalusLong();
		    }
		    //tir effectif, maintenant il faut calculer l'éventuel bonus de rafale
		    int bonusDesLancesRafale = 0;
		    int bonusDesGardesRafale = 0;

		    if (p_nbCoups > 1)
		    {
			if (arme.getCategorie() == 3)//rafales acceptées, sinon lever une exception
			{
			    if (p_nbCoups >= 3)//les bonus commmencent à partir de 3 balles
			    {
				if (p_nbCoups < 4)//rafale courte
				{
				    bonusDesLancesRafale = 2;
				}
				else
				{
				    if (p_nbCoups < 10)//rafale moyenne
				    {
					int preResult = (p_nbCoups / 3);//division entre int donc troncature
					bonusDesLancesRafale = preResult * 2;
				    }
				    else//rafale longue
				    {
					bonusDesLancesRafale = bonusDesGardesRafale = (p_nbCoups / 5);//division entre int donc troncature
				    }
				}
			    }
			}
			else
			{
			    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("nbCoups") + ":" + p_nbCoups);
			}

		    }
		    result = effectuerAttaque(p_phaseActuelle, p_ND, arme.getCategorie(), 4, bonusDesLancesRafale, bonusDesGardesRafale, modDist);
		}
	    }
	    else
	    {
		ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("distance") + ":" + p_distance + " " + PropertiesHandler.getInstance("libupsystem").getString("nbCoups") + ":" + p_nbCoups);
	    }
	}
	else
	{
	    UPErrorHandler.mauvaisModeAttaque();//TODO ce cas d'erreur n'est jamais testé
	}
	return result;
    }

    /**
     * Méthode où les éléments communs d'attaque se déroulent : les méthodes
     * précédentes ont calculé les bonus/malus et diverses conditions de
     * l'attaque spécifiques à leur situation (distance ou CaC), celle-ci va
     * prendre en compte tous les éléments communs et aire exécuter le jet à la
     * méthode afférente
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @param p_comp
     * @param p_domaine
     * @param p_modifNbLances
     * @param p_modifNbGardes
     * @param p_modifScore
     * @return
     */
    private RollResult effectuerAttaque(int p_phaseActuelle, int p_ND, int p_comp, int p_domaine, int p_modifNbLances, int p_modifNbGardes, int p_modifScore)
    {
	RollResult result = null;
	if (agirEnCombat(p_phaseActuelle))
	{
	    int modDesLances = 0 + p_modifNbLances;
	    int modDesGardes = 0 + p_modifNbGardes;
	    int modFinal = 0 + p_modifScore;
	    int ecartPhyMin = 0;

	    Arme arme = m_inventaire.getArmeCourante();

	    if (p_comp != 0)//on utilise une arme, il faut prendre en compte ses éventuels malus
	    {
		{
		    if (arme.getphysMin() > m_traits[0])
		    {
			ecartPhyMin += m_traits[0] - arme.getphysMin();
		    }
		}
		modDesLances -= arme.getMalusAttaque();
	    }
	    modFinal += (ecartPhyMin * 10);
	    result = effectuerJetComp(p_ND, p_comp, p_domaine, modDesLances, modDesGardes, modFinal, m_traits[1]);
	}
	return result;
    }

    /**
     * fait dépenser une action au personnage dans la phase en cours si c'est
     * possible
     *
     * @param p_phaseActuelle
     * @return un booléen indiquant si il est possible d'agir dans la phase en
     * cours
     */
    public boolean agirEnCombat(int p_phaseActuelle)
    {
	boolean result = false;
	if (p_phaseActuelle > 0 && p_phaseActuelle < 11)
	{

	    if ((m_actions.size() - m_actionCourante) > 0 && p_phaseActuelle == m_actions.get(m_actionCourante))
	    {
		m_actions.set(m_actionCourante, 11);
		m_actionCourante++;
		result = true;
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("phase") + ":" + p_phaseActuelle);
	}
	return result;
    }

    /**
     * génère des dégâts avec l'arme courante (distance ou corps à corps),
     * séparée de l'attaque pour que le contrôleur puisse utiliser les
     * incréments pour autre chose (comme cibler ou permettre une défense)
     *
     * @param p_increments
     * @return
     */
    public Degats genererDegats(int p_increments)
    {
	Degats result = new Degats(RollUtils.lancer(m_traits[0] + p_increments, 1, isSonne()), 0); //mains nues par défaut

	if (p_increments >= 0)
	{
	    Arme arme = m_inventaire.getArmeCourante();

	    if (arme != null)//armes équipées
	    {
		if (arme.getMode() == 0)//arme de corps à corps employée
		{
		    result = ((ArmeCaC) arme).genererDegats(p_increments, m_traits[0], isSonne());
		}
		else//arme à distance employée
		{
		    result = ((ArmeDist) arme).genererDegats(p_increments, isSonne());
		}
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("increments") + ":" + p_increments);
	}
	return result;
    }

    /**
     * inflige des dégâts, via la jauge de Santé après avoir appliqué les effets
     * d'm_armure
     *
     * @param p_degats
     */
    public void etreBlesse(Degats p_degats)
    {
	if (p_degats.getQuantite() >= 0)
	{
	    Armure armure = m_inventaire.getArmure();
	    int redDegats = armure.getRedDegats(p_degats.getTypeArme());
	    int degatsEffectifs = p_degats.getQuantite() - redDegats;

	    if (degatsEffectifs > 0)
	    {
		m_jaugeSanteInit.recevoirDegats(degatsEffectifs, this);
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("degats") + ":" + p_degats.getQuantite());
	}
    }

    /**
     *
     * @param p_typeArme
     * @param p_catArme catégorie d'arme à employer en parade, ignoré si
     * esquive, attnetion ce n'est pas le numéro index de comp mais bien la
     * catégorie d'arme
     * @param p_esquive : si l'esquive doit être employée, sinon c'est une
     * parade qui est effectuée
     * @return le ND passif calculé à partir des comps et de l'armure
     */
    public int getNDPassif(int p_typeArme, int p_catArme, boolean p_esquive)
    {
	int ND = 5;

	int rang = 0;
	int effetArmure = 0;
	Armure armure = m_inventaire.getArmure();

	if (!p_esquive)
	{
	    //calcul de la valeur issue de la compétence parade
	    rang = m_arbreDomaines.getRangComp(3, p_catArme * 2 + 1); // la comp de parade est par convention à cat*2+1 là où attaque est à cat*2

	    //ajout des bonus  et malus d'armure
	    effetArmure += armure.getBonusND(p_typeArme);
	    effetArmure -= armure.getMalusParade();
	}
	else
	{
	    //calcul de la valeur issue de la compétence esquive
	    rang = m_arbreDomaines.getRangComp(2, 0);
	    //ajout des bonus  et malus d'armure
	    effetArmure += armure.getBonusND(p_typeArme);
	    effetArmure -= armure.getMalusEsquive();

	}

	if (rang > 0)
	{
	    ND = rang * 5 + 5;
	    if (rang >= 3)
	    {
		ND += 5;
	    }
	}
	else
	{
	    if (!p_esquive)
	    {
		rang = m_arbreDomaines.getRangDomaine(3);
	    }
	    else
	    {
		rang = m_arbreDomaines.getRangDomaine(2);
	    }
	    ND = rang * 5 - 5;
	    if (ND < 5)
	    {
		ND = 5;
	    }
	}
	ND += effetArmure;
	return ND;
    }

    /**
     * renvoie vrai si le personnage a une action dans la phase active
     * comportement indéfini si demande pour pĥase ultérieure ou antérieure
     *
     * @param p_phaseActuelle
     * @return
     */
    public boolean isActif(int p_phaseActuelle)
    {
	if (p_phaseActuelle <= 0 || p_phaseActuelle > 10)
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("phase") + ":" + p_phaseActuelle);
	}
	return ((m_actions.size() - m_actionCourante) > 0 && p_phaseActuelle == m_actions.get(m_actionCourante));
    }

    /**
     *
     * @return l'initiative totale du personnage en comptant le bonus de l'arme
     */
    public int getInitTotale()
    {
	int result = 0;

	//traitement de la partie dûe aux dés d'action
	for (int i = 0; i < m_actions.size(); ++i)
	{
	    if (m_actions.get(i) < 11)//si l'action considérée est toujours disponible
	    {
		result += m_actions.get(i);
	    }
	}

	//traitement du bonus dû à l'arme
	Arme arme = m_inventaire.getArmeCourante();
	if (arme != null)
	{
	    result += arme.getBonusInit() * 5;
	}
	return result;
    }

    @Override
    public String toString()
    {
	return m_libellePerso;
    }

    /**
     *
     * @param libellePerso
     */
    public void setLibellePerso(String libellePerso)
    {
	this.m_libellePerso = libellePerso;
    }

    /**
     *
     * @param p_domaine
     * @param p_rang
     */
    public void setRangDomaine(int p_domaine, int p_rang)
    {
	m_arbreDomaines.setRangDomaine(p_domaine, p_rang);
    }

    /**
     *
     * @param p_domaine
     * @return
     */
    public int getRangDomaine(int p_domaine)
    {
	return m_arbreDomaines.getRangDomaine(p_domaine);
    }

    /**
     *
     * @param p_domaine
     * @param p_comp
     * @param p_rang
     */
    public void setRangComp(int p_domaine, int p_comp, int p_rang)
    {
	m_arbreDomaines.setRangComp(p_domaine, p_comp, p_rang);
    }

    /**
     *
     * @param p_domaine
     * @param p_comp
     * @return
     */
    public int getRangComp(int p_domaine, int p_comp)
    {
	return m_arbreDomaines.getRangComp(p_domaine, p_comp);
    }

    /**
     *
     * @param p_domaine
     * @param p_comp
     * @return
     */
    public ArrayList<String> getSpecialites(int p_domaine, int p_comp)
    {
	return m_arbreDomaines.getSpecialites(p_domaine, p_comp);
    }

    /**
     *
     * @param p_domaine
     * @param p_comp
     * @param p_specialite
     */
    public void addSpecialite(int p_domaine, int p_comp, String p_specialite)
    {
	m_arbreDomaines.addSpecialite(p_domaine, p_comp, p_specialite);
    }

    /**
     *
     * @param p_domaine
     * @param p_comp
     * @param p_indiceSpe
     */
    public void removeSpecialite(int p_domaine, int p_comp, int p_indiceSpe)
    {
	m_arbreDomaines.removeSpecialite(p_domaine, p_comp, p_indiceSpe);
    }

    public Inventaire getInventaire()
    {
	return m_inventaire;
    }

    /**
     * les 2 jauges sont prises en compte
     *
     * @return
     */
    public boolean isInconscient()
    {
	return m_jaugeSanteInit.isInconscient() || m_jaugeFatigueForceDAme.isInconscient();
    }

    /**
     * les 2 jauges sont prises en compte
     *
     * @return
     */
    public boolean isSonne()
    {
	return (m_jaugeSanteInit.isSonne() || m_jaugeFatigueForceDAme.isSonne());
    }

    /**
     * les 2 jauges sont prises en compte
     *
     * @return
     */
    public boolean isElimine()
    {
	return (m_jaugeFatigueForceDAme.isElimine() || m_jaugeSanteInit.isElimine());
    }

    public int getBlessuresGraves()
    {
	return m_jaugeSanteInit.getRemplissage_interne();
    }

    public int getBlessuresLegeres()
    {
	return m_jaugeSanteInit.getBlessuresLegeres();
    }

    public int getPointsDeFatigue()
    {
	return m_jaugeFatigueForceDAme.getRemplissage_interne();
    }

    public int getBlessuresLegeresMentales()
    {
	return m_jaugeFatigueForceDAme.getBlessuresLegeres();
    }

    /**
     *
     * @return une copie : la liste n'est pas modifiable de l'extérieur
     */
    public ArrayList<Integer> getActions()
    {
	return new ArrayList<>(m_actions);
    }

    /**
     *
     * @param p_indice
     * @param p_valeur
     */
    public void setTrait(int p_indice, int p_valeur)
    {
	m_traits[p_indice] = p_valeur;
    }

    /**
     *
     * @param p_indice
     * @return la valeur du trait
     */
    public int getTrait(int p_indice)
    {
	return m_traits[p_indice];
    }
}
