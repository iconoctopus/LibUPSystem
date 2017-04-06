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

import java.util.ArrayList;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;
import org.duckdns.spacedock.upengine.libupsystem.EnsembleJauges.EtatVital;
import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;
import org.duckdns.spacedock.upengine.libupsystem.RollUtils.RollResult;

public class Perso
{

    /**
     * arbre des domaines/compétences du personnage
     */
    private final ArbreDomaines m_arbreDomaines;
    /**
     * la structure encapsulant l'état vital du personnage
     */
    private final EnsembleJauges m_jauges;
    /**
     * le nom du personage
     */
    private String m_libellePerso;
    /**
     * les traits du personnage
     */
    private final GroupeTraits m_groupeTraits;

    /**
     * Constructeur de Perso prenant des caractéristiques en paramétres. Il est
     * possible de le modifier par la suite on peut l'initialiser avec presque
     * rien, c'est le constructeur du cas général
     *
     * @param p_traits
     * @param p_arbre
     */
    public Perso(GroupeTraits p_traits, ArbreDomaines p_arbre)
    {
	m_libellePerso = "Perso";

	m_groupeTraits = p_traits;
	m_arbreDomaines = p_arbre;

	m_jauges = new EnsembleJauges(p_traits);
    }

    /**
     * constructeur produisant des PNJ générés par rang de menace (RM)
     *
     * @param p_RM
     */
    public Perso(int p_RM)
    {
	if (p_RM < 1 || p_RM > 9)
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("rang") + ":" + p_RM);
	}

	//configuration des traits
	ArrayList<Integer> listTrait = new ArrayList<>();
	listTrait.add(p_RM);
	listTrait.add(p_RM + 1);
	listTrait.add(p_RM - 2);
	listTrait.add(p_RM - 1);
	listTrait.add(p_RM - 3);

	for (int i = 0; i < 5; ++i)
	{
	    if (listTrait.get(i) < 2)
	    {
		listTrait.set(i, 2);//minimum de 2 aux traits
	    }
	}

	m_groupeTraits = new GroupeTraits(listTrait.get(0), listTrait.get(1), listTrait.get(2), listTrait.get(3), listTrait.get(4));

	m_jauges = new EnsembleJauges(m_groupeTraits);

	//configuration des caractéristiques de combat une fois que l'arbre des domaines est généré
	//configuration du domaine corps à corps
	m_arbreDomaines = new ArbreDomaines();
	m_arbreDomaines.setRangDomaine(3, p_RM + 1);
	for (int i = 0; i < UPReferenceSysteme.getInstance().getListComp(3).size(); i++)
	{
	    m_arbreDomaines.setRangComp(3, i, p_RM + 1);
	}

	//idem pour tout le domaine combat à distance
	m_arbreDomaines.setRangDomaine(4, p_RM + 1);
	for (int i = 0; i < UPReferenceSysteme.getInstance().getListComp(4).size(); i++)
	{
	    m_arbreDomaines.setRangComp(4, i, p_RM + 1);
	}

	m_libellePerso = PropertiesHandler.getInstance("libupsystem").getString("lbl_perso_std") + p_RM;
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
     * Fait effectuer une attaque au corps à corps au personnage. Le personnage
     * attaquera systématiquement avec l'arme courante fournie (mains nues si
     * null) Cette méthode vérifie que l'action est possible dans la phase
     * courante en fonction de l'init du perso, elle est donc conçue pour le
     * combat uniquement (utiliser la méthode générale des jets de compétence
     * sinon)
     *
     * Il est important de garder la génération des dégâts séparée et déclenchée
     * depuis l'extérieur afin que le contrôleur puisse choisir d'utiliser les
     * incréments pour autre chose que des dégâs (ciblage, autoriser parade...).
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @param p_arme
     * @return
     */
    public final RollUtils.RollResult attaquerCaC(int p_phaseActuelle, int p_ND, ArmeCaC p_arme)
    {
	int catArm = 0;//mains nues par défaut
	if (p_arme != null)//une arme est équipée
	{
	    catArm = p_arme.getCategorie();
	}
	return effectuerAttaque(p_phaseActuelle, p_ND, catArm * 2, 3, 0, 0, 0, p_arme);//par convention les comp d'attaque de CaC sont à cat*2, les parades sont à Cat*2+1
    }

    /**
     * Fait effectuer une attaque à distance au personnage. Cette méthode
     * vérifie que l'action est possible dans la phase courante en fonction de
     * l'init du perso, elle est donc conçue pour le combat uniquement (utiliser
     * la méthode pour les jets de compétence généraux sinon). On utilise l'arme
     * courante fournie (mains nues si null). Aucune vérification n'est
     * effectuée sur le magasin actuel de l'arme. Si celui-ci n'est pas
     * suffisant l'arme lèvera une exception.
     *
     * Il est important de garder la génération des dégâts séparée et déclenchée
     * depuis l'extérieur afin que le contrôleur puisse choisir d'utiliser les
     * incréments pour autre chose que des dégâs (ciblage, autoriser parade...).
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @param p_distance la distance de la cible
     * @param p_nbCoups nombre de tirs effectués (pour la règle
     * @param p_arme
     * @return
     */
    public RollResult attaquerDist(int p_phaseActuelle, int p_ND, int p_distance, int p_nbCoups, ArmeDist p_arme)
    {
	RollResult result = new RollResult(0, false, 0);//raté par défaut

	ArmeDist.DistReport report = p_arme.verifPreAttaque(p_distance, p_nbCoups);
	if (!report.isEchecAuto())//on n'est pas en situation d'échec automatique
	{
	    result = effectuerAttaque(p_phaseActuelle, p_ND, p_arme.getCategorie(), 4, report.getModDesLances(), report.getModDesGardes(), report.getModJet(), p_arme);
	}
	return result;
    }

    /**
     * fait effectuer au personnage un jet de l'une de ses compétences. Appelé
     * en interne par les méthodes d'attaque qui effectuent les pré-traitements
     * pour aboutir aux caractéristiques finales du jet.
     *
     * @param p_idTrait id du trait à utiliser (pas la valeur!)
     * @param p_ND
     * @param p_indComp
     * @param p_indDomaine
     * @param p_modifNbLances
     * @param p_modifNbGardes
     * @param p_modifScore
     * @return le résultat du jet
     */
    public final RollUtils.RollResult effectuerJetComp(Trait p_idTrait, int p_indDomaine, int p_indComp, int p_ND, int p_modifNbLances, int p_modifNbGardes, int p_modifScore)
    {
	return m_arbreDomaines.effectuerJetComp(m_groupeTraits.getTrait(p_idTrait), p_indDomaine, p_indComp, p_ND, p_modifNbLances, p_modifNbGardes, p_modifScore, m_jauges.getEtatVital().isSonne());
    }

    /**
     * fait effectuer au personnage un jet avec l'un de ses traits purs.
     *
     * @param p_trait
     * @param p_ND
     * @return le résultat du jet
     */
    public final RollUtils.RollResult effectuerJetTrait(Trait p_trait, int p_ND)
    {
	return m_groupeTraits.effectuerJetTrait(p_trait, p_ND, m_jauges.getEtatVital().isSonne());
    }

    /**
     * inflige des dégâts à ce perso, via la jauge de Santé après avoir appliqué
     * les effets d'armure
     *
     * @param p_degats
     * @param p_armure peut être null
     */
    public void etreBlesse(Degats p_degats, Armure p_armure)
    {

	if (p_degats.getQuantite() >= 0 && p_degats.getTypeArme() >= 0)
	{
	    int degatsEffectifs = p_degats.getQuantite();
	    if (p_armure != null)
	    {
		//application des effets d'armure
		int redDegats = p_armure.getRedDegats(p_degats.getTypeArme());
		degatsEffectifs -= redDegats;
	    }
	    if (degatsEffectifs > 0)
	    {
		m_jauges.recevoirDegatsPhysiques(degatsEffectifs);
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("degats") + ":" + p_degats.getQuantite() + " " + PropertiesHandler.getInstance("libupsystem").getString("type") + ":" + p_degats.getTypeArme());
	}
    }

    /**
     * génère des dégâts avec l'arme courante (distance ou corps à corps),
     * séparée de l'attaque pour que le contrôleur puisse utiliser les
     * incréments pour autre chose (comme cibler ou permettre une défense)
     *
     * @param p_increments
     * @return
     */
    public Degats genererDegats(int p_increments, Arme p_arme)
    {
	int domaine;
	int competence;
	int vd;
	int bonusSup = p_increments * 2;//bonus aux dégâts du aux incréments (pas de ciblage dans cette méthode)
	int typArm;

	Degats result = new Degats(0, 0);

	if (p_increments >= 0)
	{
	    if (p_arme != null)//armes équipées
	    {
		vd = p_arme.getVD();
		typArm = p_arme.getTypeArme();

		if (p_arme.getMode() == 0)//arme de corps à corps employée
		{
		    domaine = 3;//corps à corps
		    competence = p_arme.getCategorie() * 2;//les attaques sont à catégorie *2, les parades à catégorie * 2 +1
		    bonusSup += m_groupeTraits.getTrait(Trait.PHYSIQUE);//le rang de physique vient en bonus au nombre d'incréments
		}
		else//arme à distance employée
		{
		    domaine = 4;//distance
		    competence = p_arme.getCategorie();//compétence d'arme
		    //pas de bonus de physique pour les armes à distance
		}
	    }
	    else//combat à mains nues
	    {
		vd = m_groupeTraits.getTrait(Trait.PHYSIQUE);
		typArm = 0;
		domaine = 3;//corps à corps
		competence = 0;//comp mains nues
		bonusSup += m_groupeTraits.getTrait(Trait.PHYSIQUE);//le rang de physique vient en bonus au nombre d'incréments
	    }
	    return new Degats(vd + m_arbreDomaines.getRangDomaine(domaine) + m_arbreDomaines.getRangComp(domaine, competence) + bonusSup, typArm);
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("increments") + ":" + p_increments);
	}
	return result;
    }

    /**
     * fait dépenser une action au personnage si il lui est possible d'agir dans
     * la phase actuelle
     *
     * @param p_phaseActuelle
     * @return vrai si le perso peut agir
     */
    public boolean agirEnCombat(int p_phaseActuelle)
    {
	return m_jauges.agirEnCombat(p_phaseActuelle);
    }

    /**
     *
     * @param p_phaseActuelle
     * @return vrai si le perso a une action dans la phase actuelle
     */
    public boolean isActif(int p_phaseActuelle)
    {
	return m_jauges.isActif(p_phaseActuelle);
    }

    /**
     *
     * @return une copie : la liste n'est pas modifiable de l'extérieur
     */
    public ArrayList<Integer> getActions()
    {
	return m_jauges.getActions();
    }

    /**
     *
     * @return un rapport complet sur l'état des jauges
     */
    public EtatVital getEtatVital()
    {
	return m_jauges.getEtatVital();
    }

    /**
     *
     * @return l'initiative totale du personnage en comptant le bonus de l'arme
     * @param p_arme
     */
    public int getInitTotale(Arme p_arme)
    {
	return m_jauges.getInitTotale(p_arme);
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
     * @param p_typeArme le type d'arme infligeant potentiellement des dégâts
     * entrants afin de prendre en compte l'armure
     * @param p_nbAdvSup nombre d'adversaires supplémentaires AU DELA DU PREMIER
     * @param p_armure
     * @return la défense, calculée à partir du groupe de traits
     */
    public int getDefense(int p_typeArme, int p_nbAdvSup, Armure p_armure)
    {
	int result = 0;
	if (p_nbAdvSup >= 0)
	{
	    result = m_groupeTraits.getTrait(Trait.COORDINATION) * 5 + 5;//valeur de base
	    if (p_armure != null)
	    {
		result += p_armure.getBonusND(p_typeArme);//effets d'armure
	    }
	    result -= p_nbAdvSup * 2; // malus par adversaire supplémentaire
	    if (m_jauges.getEtatVital().isSonne())//malus si sonné
	    {
		result -= 5;
	    }
	    if (result < 5)//application de la règle du minimum de 5 à la défense
	    {
		result = 5;
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("nb_adv_sup") + ":" + p_nbAdvSup);
	}
	return result;
    }

    /**
     * force le personnage à relancer son initiative
     */
    public void genInit()
    {
	m_jauges.genInit();
    }

    /**
     *
     * @param p_indDomaine l'indice du domaine
     * @param p_indComp l'indice de la comp dans le domaine
     * @return
     */
    public int getRangComp(int p_indDomaine, int p_indComp)
    {
	return m_arbreDomaines.getRangComp(p_indDomaine, p_indComp);
    }

    /**
     *
     * @param p_indDomaine l'indice du domaine
     * @return
     */
    public int getRangDomaine(int p_indDomaine)
    {
	return m_arbreDomaines.getRangDomaine(p_indDomaine);
    }

    /**
     *
     * @param p_indDomaine l'indice du domaine
     * @param p_indComp l'indice de la compétence dans le domaine
     * @return
     */
    public ArrayList<String> getSpecialites(int p_indDomaine, int p_indComp)
    {
	return m_arbreDomaines.getSpecialites(p_indDomaine, p_indComp);
    }

    /**
     *
     * @param p_trait le trait considéré
     * @return la valeur du trait
     */
    public int getTrait(Trait p_trait)
    {
	return m_groupeTraits.getTrait(p_trait);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @param p_indiceSpe
     */
    public void removeSpecialite(int p_indDomaine, int p_indComp, int p_indiceSpe)
    {
	m_arbreDomaines.removeSpecialite(p_indDomaine, p_indComp, p_indiceSpe);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @param p_rang
     */
    public void setRangComp(int p_indDomaine, int p_indComp, int p_rang)
    {
	m_arbreDomaines.setRangComp(p_indDomaine, p_indComp, p_rang);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_rang
     */
    public void setRangDomaine(int p_indDomaine, int p_rang)
    {
	m_arbreDomaines.setRangDomaine(p_indDomaine, p_rang);
    }

    /**
     *
     * @param p_trait le trait considéré
     * @param p_rang
     */
    public void setTrait(Trait p_trait, int p_rang)
    {
	m_groupeTraits.setTrait(p_trait, p_rang);
	m_jauges.initJauges();//TODO : en l'état les jauges sont complètement remplacées : on perd donc les blessures, la force d'âme dépensée etc.
    }

    @Override
    public String toString()
    {
	return m_libellePerso;
    }

    /**
     * Méthode où les éléments communs d'attaque se déroulent : les méthodes
     * précédentes ont calculé les bonus/malus et diverses conditions de
     * l'attaque spécifiques à leur situation (distance ou CaC), celle-ci va
     * prendre en compte tous les éléments communs et faire exécuter le jet
     * d'attaque à la méthode afférente
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @param p_indComp
     * @param p_indDomaine
     * @param p_modifNbLances
     * @param p_modifNbGardes
     * @param p_modifScore
     * @return
     */
    private RollResult effectuerAttaque(int p_phaseActuelle, int p_ND, int p_indComp, int p_indDomaine, int p_modifNbLances, int p_modifNbGardes, int p_modifScore, Arme p_arme)
    {
	RollResult result = null;
	if (m_jauges.agirEnCombat(p_phaseActuelle))
	{//on peut agir
	    int modDesLances = 0 + p_modifNbLances;
	    int modDesGardes = 0 + p_modifNbGardes;
	    int modFinal = 0 + p_modifScore;
	    int ecartPhyMin = 0;

	    if (p_indDomaine != 3 || p_indDomaine == 3 && p_indComp != 0)
	    {//on utilise une arme, il faut prendre en compte ses éventuels malus
		{
		    if (p_arme.getphysMin() > m_groupeTraits.getTrait(Trait.PHYSIQUE))
		    {
			ecartPhyMin += m_groupeTraits.getTrait(Trait.PHYSIQUE) - p_arme.getphysMin();
		    }
		}
		modDesLances -= p_arme.getMalusAttaque();
	    }
	    modFinal += (ecartPhyMin * 10);
	    result = effectuerJetComp(Trait.COORDINATION, p_indDomaine, p_indComp, p_ND, modDesLances, modDesGardes, modFinal);
	}
	return result;
    }

    /**
     * classe utilisée pour encapsuler les résultats d'une attaque réussie ; des
     * dégâts mais aussi le type.
     */
    public static final class Degats
    {

	/**
	 * le total des dégâts infligés
	 */
	private int m_quantite;
	/**
	 * le type d'arme employé
	 */
	private int m_typeArme;

	/**
	 * constructeur de dégâts
	 *
	 * @param p_quantite
	 * @param p_typeArme
	 */
	public Degats(int p_quantite, int p_typeArme)
	{
	    if (p_quantite >= 0 && p_typeArme >= 0)
	    {
		m_quantite = p_quantite;
		m_typeArme = p_typeArme;
	    }
	    else
	    {
		ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("degats") + ":" + p_quantite + " " + PropertiesHandler.getInstance("libupsystem").getString("type") + ":" + p_typeArme);
	    }
	}

	/**
	 * @return the m_quantite
	 */
	public int getQuantite()
	{
	    return m_quantite;
	}

	/**
	 * @return the m_typeArme
	 */
	public int getTypeArme()
	{
	    return m_typeArme;
	}
    }
}
