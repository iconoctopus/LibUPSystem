package org.duckdns.spacedock.upengine.libupsystem;

/**
 * Classe représentant une arme et permettant de générer des dégâts avec
 * celle-ci.
 *
 * @author iconoctopus
 */
public class Arme//TODO transformer ctte classe en interface qu'implémenteront ArmeCac et ArmeDist (ils interrogeront différemment UPReference dans leur constructeur car les indices d'armes des dexu catégories se recouvrent)
{

    /**
     * le nombre de dés lancés dans la VD
     */
    private final int m_desLances;
    /**
     * le nombre de dés gardés dans la VD
     */
    private final int m_desGardes;
    /**
     * le bonus apporté à l'initiative totale
     */
    private final int m_bonusInit;
    /**
     * le type de l'arme : simple, perce-amure, penetrante, perce-blindage ou
     * energétique, respectivement de 0 à 4
     */
    private final int m_typeArme;
    /**
     * le malus donné par l'arme à l'attaque
     */
    private final int m_malusAttaque;
    /**
     * le physique minimal pour manier l'arme.
     */
    private final int m_physMin;
    /**
     * la categorie d'arme (permet de définir la compétence à utiliser)
     */
    private final int m_categorie;
    /**
     * le nom de l'arme
     */
    private final String m_nom;

    public int getDesLances()
    {
	return m_desLances;
    }

    public int getDesGardes()
    {
	return m_desGardes;
    }

    public int getBonusInit()
    {
	return m_bonusInit;
    }

    public int getTypeArme()
    {
	return m_typeArme;
    }

    public int getMalusAttaque()
    {
	return m_malusAttaque;
    }

    public int getphysMin()
    {
	return m_physMin;
    }

    public int getCategorie()
    {
	return m_categorie;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }

    /**
     * constructeur d'arme de corps à corps à parti de la référence UP!
     *
     * @param p_indice
     */
    public Arme(int p_indice)
    {
	//TODO : ajouter la possibilité de spéifier la qualité et l'quilibrage pour spécifier la fabrique d'arme

	UPReference reference = UPReference.getInstance();

	m_desLances = reference.getNbLancesArmeCac(p_indice);
	m_desGardes = reference.getNbGardesArmeCac(p_indice);
	m_bonusInit = reference.getBonusInitArmeCac(p_indice);
	/**
	 * le type technologique (utilisé dans l'interraction avec les armures
	 */
	m_typeArme = reference.getTypeArmeCac(p_indice);
	m_malusAttaque = reference.getMalusAttaqueArmeCac(p_indice);
	m_physMin = reference.getPhysMinArmeCac(p_indice);
	m_nom = reference.getLblArmeCac(p_indice);
	/**
	 * la catégorie utilisée dans l'interraction avec les comps
	 */
	m_categorie = reference.getCategorieArmeCac(p_indice);
    }

    /**
     * génère les dégâts infligés avec cette arme en combat
     *
     * @param p_nbIncrements
     * @param p_physique
     * @param p_isSonne
     * @return
     */
    Degats genererDegats(int p_nbIncrements, int p_physique, boolean p_isSonne)
    {
	int degatsBruts = 0;
	if(p_nbIncrements >= 0 && p_physique >= 0)
	{
	    degatsBruts = (RollGenerator.lancer(m_desLances + p_nbIncrements + p_physique, m_desGardes, p_isSonne));//TODO : ce fonctionnement est bien adapté au corps à corps, voir si on peut surcharger la méthode pour les armes à distances (enlever l'ajout du physique) ou juste mettre un if avec un booléen en paramétre
	}
	else
	{
	    ErrorHandler.paramAberrant("increments:" + p_nbIncrements + " physique:" + p_physique);
	}
	return new Degats(degatsBruts, m_typeArme);
    }

    /**
     * classe utilisée pour encapsuler les résultats d'une attaque réussie ; des
     * dégâts mais aussi le type
     */
    public static class Degats
    {//TODO : c'est ici que la localisation d'une attaque pourra être insérée pour être communiquée à la cible

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
	    if(p_quantite >= 0 && p_typeArme <= 0)
	    {
		m_quantite = p_quantite;
		m_typeArme = p_typeArme;
	    }
	    else
	    {
		ErrorHandler.paramAberrant("degats:" + p_quantite + " type:" + p_typeArme);
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
