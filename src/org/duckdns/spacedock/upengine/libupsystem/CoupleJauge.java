package org.duckdns.spacedock.upengine.libupsystem;

/**
 * représente l'un des couples de jauges vitales d'un personnage : santé/init ou
 * fatigue/force d'âme. Abrite les mécanismes d'encaisement des dégâts.
 *
 * @author iconoctopus
 */
class CoupleJauge
{

    /**
     * la taille max de la juge externe (init ou force d'âme)
     */
    private int m_taille_externe;
    /**
     * la taille max de la jauge interne (santé ou fatigue)
     */
    private int m_taille_interne;
    /**
     * le remplissage de la jauge externe (niveau d'init ou de force d'âme
     * actuel)
     */
    private int m_remplissage_externe;
    /**
     * le remplissage de la jauge interne (blessures graves ou point de fatigue)
     */
    private int m_remplissage_interne;
    /**
     * position du point de choc
     */
    private int m_choc;
    /**
     * niveau actuel de blessures légères
     */
    private int m_blessuresLegeres;
    /**
     * statut inconscient ou non du personnge, ce statut est cumulatif avec la
     * mort (être inconscinet n'impliuqe donc pas d'être vivant)
     */
    private boolean m_inconscient;
    /**
     * représente l'élimination d'un personnage : mort ou coma
     */
    private boolean m_elimine;

    /**
     * constructeur de jauge de santé/init
     *
     * @param p_physique le trait physique
     * @param p_volonte le trait volonté
     * @param p_mental le trait mental
     * @param p_coordination le trait coordination
     */
    CoupleJauge(int p_physique, int p_volonte, int p_mental, int p_coordination)//jauge de santé
    {
	if (p_physique >= 0 && p_volonte >= 0 && p_mental >= 0 && p_coordination >= 0)
	{
	    instancier(p_physique + p_volonte, UPReference.getInstance().getInitModCoord(p_coordination) + UPReference.getInstance().getInitModMental(p_mental), p_physique);
	}
	else
	{
	    ErrorHandler.paramAberrant("physique:" + p_physique + " volonte:" + p_volonte + " mental:" + p_mental + " coordination:" + p_coordination);
	}
    }

    /**
     * constructeur de jauge de fatigue/force d'âme
     *
     * @param p_physique le trait physique
     * @param p_volonte le trait volonte
     * @param p_tailleForceDAme le plus faible des traits du perso
     */
    CoupleJauge(int p_physique, int p_volonte, int p_tailleForceDAme)//jauge de fatigue
    {
	if (p_physique >= 0 && p_volonte >= 0 && p_tailleForceDAme >= 0)
	{

	    instancier(p_physique + p_volonte, p_tailleForceDAme, p_volonte);

	}
	else
	{
	    ErrorHandler.paramAberrant("physique:" + p_physique + " volonte:" + p_volonte + " trait minimum:" + p_tailleForceDAme);
	}

    }

    /**
     * commun aux deux constructeurs. Sert à constituer le CoupleJauge dans les
     * faits
     *
     * @param p_taille_interne
     * @param p_taille_externe
     * @param p_choc
     */
    private void instancier(int p_taille_interne, int p_taille_externe, int p_choc)
    {
	m_taille_externe = p_taille_externe;
	m_taille_interne = p_taille_interne;
	m_choc = p_choc;
	m_remplissage_externe = m_taille_externe;
	m_remplissage_interne = 0;//au départ on a pas de blessures graves/ points de fatigue
	m_blessuresLegeres = 0;
	m_elimine = false;
	m_inconscient = false;
    }

    /**
     * méthode permettant d'nevoyer des dégâts à la jauge interne, elle va gérer
     * ces dégâts et se remplir adéquatement, vidant possiblement la jauge
     * externe au passage
     *
     * @param p_degats
     * @param p_resultatJet le resultat du jet d'absorption
     * @param p_volonte
     * @return le niveau de remplissage de la jauge externe, ce qui permet de
     * suivre si celle-ci a été réduite
     */
    int recevoirDegats(int p_degats, int p_resultatJet, int p_volonte)
    {
	if (p_degats >= 0 && p_resultatJet >= 0 && p_volonte >= 0)
	{
	    int quotient;
	    int blessGraves;

	    m_blessuresLegeres += p_degats;
	    if (p_resultatJet < m_blessuresLegeres)//le jet d'absorption est en dessous du ND des blessures légères
	    {
		quotient = ((m_blessuresLegeres) - (p_resultatJet));
		quotient = quotient / 10; //on compte le nombre de tranches entières de 10, la division entre int va correctement tronquer
		blessGraves = (int) quotient + 1;//total des blessures graves : une pour avoir raté le jet, et une par tranche de 10
		m_blessuresLegeres = 0;
		m_remplissage_interne += blessGraves;

		if (m_remplissage_interne > m_choc)//on risque l'inconscience et l'élimination
		{
		    if (m_remplissage_interne >= m_taille_interne || RollUtils.lancer(p_volonte, p_volonte, isSonne()) < (5 * m_remplissage_interne))//jet raté ou jauge remplie
		    {
			m_inconscient = true;
			if (m_remplissage_interne >= m_taille_interne)//jauge remplie
			{
			    m_elimine = true;
			    m_remplissage_interne = m_taille_interne;//on ramène le remplissage au max de la jauge si il dépasse
			}
		    }
		}

		int ecart_IntExt = m_taille_interne - m_taille_externe;
		if (m_remplissage_interne > ecart_IntExt)//on vide la jauge externe
		{
		    m_remplissage_externe -= m_remplissage_interne - ecart_IntExt;
		    if (m_remplissage_externe < 0)//jauge vide, on corrige tout nombre négatif
		    {
			m_remplissage_externe = 0;//TODO à terme gérer le remplissage de cette valeur avec le système de soins
		    }
		}
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant("degats:" + p_degats + " jet:" + p_resultatJet + " volonte:" + p_volonte);
	}
	return m_remplissage_externe;

    }

    /**
     * @return the m_remplissage_externe
     */
    int getRemplissage_externe()
    {
	return m_remplissage_externe;
    }

    /**
     * @return the m_blessuresLegeres
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

    /**
     *
     * @return le statut sonne ou non, calculé en fonction du remplissage et du
     * point choc
     */
    Boolean isSonne()
    {
	return (m_remplissage_interne >= m_choc);
    }

    /**
     *
     * @return si le personnage est inconscient, ce statut est cumulatif avec la
     * mort (être inconscinet n'impliuqe donc pas d'être vivant)
     */
    Boolean isInconscient()
    {
	return (m_inconscient);
    }

    /**
     *
     * @return si le personnage est éliminé (coma ou mort)
     */
    Boolean isElimine()
    {
	return (m_elimine);
    }

}
