/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

/**
 *
 * @author iconoctopus
 */
public class ArmeDist extends Arme
{

    /**
     * le malus à portée courte
     */
    private final int m_malusCourt;
    /**
     * le malus à portée longue
     */
    private final int m_malusLong;
    /**
     * le malus à portée longue
     */
    private final int m_portee;
    /**
     * le nombre d'actions pour recharger
     */
    private final int m_nbActionsRecharge;
    /**
     * la capacité du magasin de l'arme
     */
    private final int m_magasinMax;
    /**
     * le nombre de coups présents dans l'arme au départ
     */
    private int m_magasinCourant;

    public ArmeDist(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	super(p_indice, p_qualite, p_equilibrage);

	UPReference reference = UPReference.getInstance();

	m_malusCourt = reference.getMalusCourtArme(p_indice);
	m_malusLong = reference.getMalusLongArme(p_indice);
	m_portee = reference.getPorteeArme(p_indice);
	m_nbActionsRecharge = reference.getNbActionsRechargeArme(p_indice);
	m_magasinMax = reference.getMagasinArme(p_indice);
	m_magasinCourant = 0;//par défaut l'arme n'est pas chargée
    }

    /**
     * génère les dégâts infligés avec cette arme en combat à distance
     *
     * @param p_nbIncrements
     * @param p_physique le physique du personnage
     * @param p_isSonne
     * @return
     */
    Degats genererDegats(int p_nbIncrements, boolean p_isSonne)
    {
	int degatsBruts = 0;
	if (p_nbIncrements >= 0)
	{
	    degatsBruts = (RollUtils.lancer(super.getDesLances() + p_nbIncrements, super.getDesGardes(), p_isSonne));
	}
	else
	{
	    ErrorHandler.paramAberrant("increments:" + p_nbIncrements);
	}
	return new Degats(degatsBruts, super.getTypeArme());
    }

    /**
     * @return the m_malusCourt
     */
    int getMalusCourt()
    {
	return m_malusCourt;
    }

    /**
     * @return the m_malusLong
     */
    int getMalusLong()
    {
	return m_malusLong;
    }

    /**
     * @return the m_portee
     */
    int getPortee()
    {
	return m_portee;
    }

    /**
     * @return the m_nbActionsRecharge
     */
    int getNbActionsRecharge()
    {
	return m_nbActionsRecharge;
    }

    /**
     * @return the m_magasinMax
     */
    int getTailleMAgasin()
    {
	return m_magasinMax;
    }

    /**
     * @return the m_magasinCourant
     */
    int getMunCourantes()
    {
	return m_magasinCourant;
    }

    /**
     * @param m_magasinCourant the m_magasinCourant to set
     */
    void consommerMun(int p_nbMun)
    {
	if (p_nbMun > 0 && p_nbMun <= m_magasinCourant)
	{
	    m_magasinCourant -= p_nbMun;
	}
	else
	{
	    ErrorHandler.paramAberrant("nbMun:" + p_nbMun + " " + "mun courantes:" + m_magasinCourant);
	}
    }

    /**
     * @param p_nbMun quantité de munitions à recharger
     * @return le nombre d'action que prendra la recharge
     */
    int recharger(int p_nbMun)
    {
	if (p_nbMun > 0 && (p_nbMun + m_magasinCourant) <= m_magasinMax)
	{
	    m_magasinCourant += p_nbMun;
	}
	else
	{
	    ErrorHandler.paramAberrant("nbMun:" + p_nbMun + " " + "taille magasin:" + m_magasinMax);
	}
	return getNbActionsRecharge();
    }
}
