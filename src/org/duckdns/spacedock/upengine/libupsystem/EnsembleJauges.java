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
import java.util.Collections;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;
import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;

/**
 *
 * @author ykonoclast
 */
public class EnsembleJauges
{

    /**
     * les actions du personnage dans ce tour sous la forme de la phase dans
     * laquelle l'action se déroule dans l'ordre des phases (donc tableau de la
     * taille de l'init)
     */
    private ArrayList<Integer> m_actions;
    /**
     * référence sur les traits du personnage possédant cet ensemble de jauge
     */
    private final GroupeTraits m_groupeTraits;
    /**
     * non finale pour évoluer avec les traits
     */
    private CoupleJauges m_jaugeSanteInit;
    /**
     * non finale pour évoluer avec les traits
     */
    private CoupleJauges m_jaugeFatigueForceDAme;

    public EnsembleJauges(GroupeTraits p_traits)
    {
	m_groupeTraits = p_traits;
	initJauges();
	genInit();
    }

    void initJauges()
    {
	int traitMin = m_groupeTraits.getTrait(Trait.PHYSIQUE);

	for (Trait t : Trait.values())
	{
	    int traitcourant = m_groupeTraits.getTrait(t);
	    if (traitcourant < traitMin)
	    {
		traitMin = traitcourant;
	    }
	}

	m_jaugeFatigueForceDAme = new CoupleJauges(m_groupeTraits.getTrait(Trait.PHYSIQUE), m_groupeTraits.getTrait(Trait.VOLONTE), traitMin);
	m_jaugeSanteInit = new CoupleJauges(m_groupeTraits.getTrait(Trait.PHYSIQUE), m_groupeTraits.getTrait(Trait.VOLONTE), m_groupeTraits.getTrait(Trait.MENTAL), m_groupeTraits.getTrait(Trait.COORDINATION));
    }

    /**
     * inflige des dégâts à la jauge de santé
     *
     * @param p_degats la quantité finale de dégâts
     */
    void recevoirDegatsPhysiques(int p_degats)
    {
	m_jaugeSanteInit.recevoirDegats(p_degats, m_groupeTraits);
    }

    /**
     * inflige des dégâts à la jauge de fatigue
     *
     * @param p_degats la quantité finale de dégâts
     */
    void recevoirDegatsMentaux(int p_degats)
    {
	m_jaugeFatigueForceDAme.recevoirDegats(p_degats, m_groupeTraits);
    }

    /**
     * génère l'initiative du personnage, devrait être appelée dans le
     * constructeur mais par la suite contrôlée de l'extérieur
     */
    public final void genInit()
    {
	int initiative = m_jaugeSanteInit.getRemplissage_externe();
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
	    if (isActif(p_phaseActuelle))
	    {
		m_actions.remove(0);
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
	return (m_actions.size() > 0 && p_phaseActuelle == m_actions.get(0));//si la liste des actions possibles n'est pas vide et que la prochaine action à accomplir est dans la phase actuelle
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
     * @param p_arme l'arme qui va donner son bonus d'initiative (la méthode
     * accepte
     * @return l'initiative totale du personnage en comptant le bonus de l'arme
     */
    public int getInitTotale(Arme p_arme)
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
	if (p_arme != null)
	{
	    result += p_arme.getBonusInit() * 5;
	}
	return result;
    }

    /**
     *
     * @return un descripteur de l'état des jauges au moment de l'interrogation
     */
    EtatVital getEtatVital()
    {
	return new EtatVital();
    }

    /**
     * classe de communication encapsulant un état à l'instant t des jauges,
     * n'est pas statique car se construit en fonction des jauges de l'objet où
     * il est produit
     */
    public class EtatVital
    {

	/**
	 * le nombre de blessures legeres mentales
	 */
	private final int m_nbBlessuresLegeresMentales;
	/**
	 * le nombre de blessures legeres physiques
	 */
	private final int m_nbBlessuresLegeres;
	/**
	 * la taille de la jauge de santé
	 */
	private final int m_santeMax;
	/**
	 * la taille de la jauge d'init
	 */
	private final int m_initMax;
	/**
	 * le remplissage de la jauge de santé
	 */
	private final int m_santeActu;
	/**
	 * le remplissage de la jauge d'init
	 */
	private final int m_initActu;
	/**
	 * la taille de la jauge de fatigue
	 */
	private final int m_fatigueMax;
	/**
	 * la taille de la jauge de force d'âme
	 */
	private final int m_forceDAmeMax;
	/**
	 * le remplissage actuel de la jauge de fatigue
	 */
	private final int m_fatigueActu;
	/**
	 * le remplissage actuel de la jauge de force d'âme
	 */
	private final int m_forceDAmeActu;
	/**
	 * la position du point de choc de la jauge de santé
	 */
	private final int m_ptChocSante;
	/**
	 * la position du point de choc de la jauge de fatigue
	 */
	private final int m_ptChocFatigue;
	/**
	 * si l'une des deux jauges indique sonne
	 */
	private final boolean m_isSonne;
	/**
	 * si l'une des deux jauges a rendu le personnage inconscient
	 */
	private final boolean m_isInconscient;
	/**
	 * si l'une des deux jauges a rendu le personnage mort ou dans le coma
	 */
	private final boolean m_isElimine;

	/**
	 * constructeur sans arguments car lit ses données dans l'instance
	 * enclosante
	 */
	public EtatVital()
	{
	    m_nbBlessuresLegeres = m_jaugeSanteInit.getBlessuresLegeres();
	    m_nbBlessuresLegeresMentales = m_jaugeFatigueForceDAme.getBlessuresLegeres();
	    m_santeMax = m_jaugeSanteInit.getTaille_interne();
	    m_initMax = m_jaugeSanteInit.getTaille_externe();
	    m_santeActu = m_jaugeSanteInit.getRemplissage_interne();
	    m_initActu = m_jaugeSanteInit.getRemplissage_externe();
	    m_fatigueMax = m_jaugeFatigueForceDAme.getTaille_interne();
	    m_forceDAmeMax = m_jaugeFatigueForceDAme.getTaille_externe();
	    m_fatigueActu = m_jaugeFatigueForceDAme.getRemplissage_interne();
	    m_forceDAmeActu = m_jaugeFatigueForceDAme.getRemplissage_externe();
	    m_ptChocSante = m_jaugeSanteInit.getPtChoc();
	    m_ptChocFatigue = m_jaugeFatigueForceDAme.getPtChoc();
	    m_isSonne = m_jaugeFatigueForceDAme.isSonne() || m_jaugeSanteInit.isSonne();
	    m_isInconscient = m_jaugeFatigueForceDAme.isInconscient() || m_jaugeSanteInit.isInconscient();
	    m_isElimine = m_jaugeFatigueForceDAme.isElimine() || m_jaugeSanteInit.isElimine();
	}

	/**
	 *
	 * @return le nombre de blessures legeres physiques
	 */
	int getBlessuresLegeres()
	{
	    return m_nbBlessuresLegeres;
	}

	/**
	 *
	 * @return le nombre de blessures legeres mentales
	 */
	int getBlessuresLegeresMentales()
	{
	    return m_nbBlessuresLegeresMentales;
	}

	/**
	 *
	 * @return la taille de la jauge de santé
	 */
	int getSante()
	{
	    return m_santeMax;
	}

	/**
	 *
	 * @return la taille de la jauge d'init
	 */
	int getInitMax()
	{
	    return m_initMax;
	}

	/**
	 *
	 * @return le remplissage de la jauge de santé
	 */
	int getBlessuresGraves()
	{
	    return m_santeActu;
	}

	/**
	 *
	 * @return le remplissage de la jauge d'initiative
	 */
	int getInitActu()
	{
	    return m_initActu;
	}

	/**
	 *
	 * @return la taille de la jauge de fatigue
	 */
	int getFatigue()
	{
	    return m_fatigueMax;
	}

	/**
	 *
	 * @return la taille de la jauge de force d'âme
	 */
	int getForceDAmeMax()
	{
	    return m_forceDAmeMax;
	}

	/**
	 *
	 * @return le remplissage de la jauge de fatigue
	 */
	int getPtsFatigue()
	{
	    return m_fatigueActu;
	}

	/**
	 *
	 * @return le remplissage de la jauge de force d'âme
	 */
	int getForceDAmeActu()
	{
	    return m_forceDAmeActu;
	}

	/**
	 *
	 * @return la position du point de choc de santé
	 */
	int getPtChocSante()
	{
	    return m_ptChocSante;
	}

	/**
	 *
	 * @return la position du point de choc de fatigue
	 */
	int getPtChocFatigue()
	{
	    return m_ptChocFatigue;
	}

	/**
	 *
	 * @return si le personnage possesseur de cet ensemble de jauges est
	 * sonné
	 */
	boolean isSonne()
	{
	    return m_isSonne;
	}

	/**
	 *
	 * @return si le personnage possesseur de cet ensemble de jauges est
	 * incosncient
	 */
	boolean isInconscient()
	{
	    return m_isInconscient;
	}

	/**
	 *
	 * @return si le personnage possesseur de cet ensemble de jauges est
	 * éliminé
	 */
	boolean isElimine()
	{
	    return m_isElimine;
	}
    }
}
