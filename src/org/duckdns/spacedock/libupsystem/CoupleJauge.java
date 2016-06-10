/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.libupsystem;

/**
 * représente l'un des couples de jauges vitales d'un personnage : santé/init ou
 * fatigue/force d'âme
 *
 * @author iconoctopus
 */
class CoupleJauge
{

    private int m_taille_externe;
    private int m_taille_interne;
    private int m_remplissage_externe;
    private int m_remplissage_interne;
    private int m_choc;
    private int m_blessuresLegeres;
    private boolean m_inconscient;
    private int m_volonte;

    CoupleJauge(int p_physique, int p_volonte, int p_mental, int p_coordination)//jauge de santé
    {
	instancier(p_physique + p_volonte, UPReference.getInstance().getInitModCoord(p_coordination) + UPReference.getInstance().getInitModMental(p_mental), p_physique);
    }

    CoupleJauge(int p_physique, int p_volonte, int p_traitMinimum)//jauge de fatigue
    {
	instancier(p_physique + p_volonte, p_traitMinimum, p_volonte);
    }

    private void instancier(int p_taille_interne, int p_taille_externe, int p_choc)
    {
	m_taille_externe = p_taille_externe;
	m_taille_interne = p_taille_interne;
	m_choc = p_choc;
	m_remplissage_externe = 0;
	m_remplissage_interne = m_taille_interne;
    }

    void infligerDegats(int p_degats, int p_resultatJet)
    {//TODO après son charcutage ce code est devenu suspect, le reprendre et le tester intensément
	double quotient;
	int blessGraves;

	m_blessuresLegeres += p_degats;
	if(p_resultatJet < m_blessuresLegeres)
	{
	    quotient = ((double) (m_blessuresLegeres) - (double) (p_resultatJet));//TODO remplacer ce calcul par un modulo ou autre, en tout cas quelque chose de plus propre
	    quotient = quotient / 10.0;
	    blessGraves = (int) quotient + 1;
	    m_blessuresLegeres = 0;
	    m_remplissage_interne += blessGraves;
	    if(m_remplissage_interne >= m_choc)
//TODO gérer le vidage de la jauge externe
//TODO gérer la mort/coma auto
	    {
		if(m_remplissage_interne >= m_taille_interne || RollGenerator.lancer(m_volonte, m_volonte, isSonne()) < (5 * m_remplissage_interne))
		{
		    m_inconscient = true;
		}
	    }
	}

    }

    /**
     * @return the m_remplissage_externe
     */
    int getRemplissage_externe()
    {
	return m_remplissage_externe;
    }

    /**
     * @return the m_remplissage_externe
     */
    int getBlessuresLegeres()
    {
	return m_blessuresLegeres;
    }

    /**
     * @return the m_remplissage_interne
     */
    int getRemplissage_interne()
    {
	return m_remplissage_interne;
    }

    /**
     * @return the m_taille_externe
     */
    int getTaille_externe()
    {
	return m_taille_externe;
    }

    /**
     * @return the m_taille_interne
     */
    int getTaille_interne()
    {
	return m_taille_interne;
    }

    Boolean isSonne()
    {
	return (m_remplissage_interne >= m_taille_interne);
    }

    Boolean isInconscient()
    {
	return (m_inconscient);
    }

    Boolean isElimine()
    {
	return (m_remplissage_interne == m_taille_interne);
    }

}
