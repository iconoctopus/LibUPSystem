package org.duckdns.spacedock.upengine.libupsystem;

/**
 * Classe représentant une arme et permttant de générer des dégâts avec
 * celle-ci.
 *
 * @author iconoctopus
 */
public class Arme
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
     * energetique, respectivement de 0 à 4
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

    int getDesLances()
    {
	return m_desLances;
    }

    int getDesGardes()
    {
	return m_desGardes;
    }

    int getBonusInit()
    {
	return m_bonusInit;
    }

    int getTypeArme()
    {
	return m_typeArme;
    }

    int getMalusAttaque()
    {
	return m_malusAttaque;
    }

    int getphysMin()
    {
	return m_physMin;
    }

    int getCategorie()
    {
	return m_categorie;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }

    /**
     * constructeur d'arme à parti de la référence UP!
     *
     * @param p_indice
     */
    Arme(int p_indice)
    {
	//TODO : ajouter la possibilité de spéifier la qualité et l'quilibrage pour spécifier la fabrique d'arme

	UPReference reference = UPReference.getInstance();

	m_desLances = reference.getNbLancesArme(p_indice);
	m_desGardes = reference.getNbGardesArme(p_indice);
	m_bonusInit = reference.getBonusInitArme(p_indice);
	/**
	 * le type technologique (utilisé dans l'interraction avec les armures
	 */
	m_typeArme = reference.getTypeArme(p_indice);
	m_malusAttaque = reference.getMalusAttaqueArme(p_indice);
	m_physMin = reference.getPhysMinArme(p_indice);
	m_nom = reference.getLblArme(p_indice);
	/**
	 * la catégorie utilisée dans l'interraction avec les comps
	 */
	m_categorie = reference.getCategorieArme(p_indice);
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
	int degatsBruts = (RollGenerator.lancer(m_desLances + p_nbIncrements + p_physique, m_desGardes, p_isSonne));//TODO : ce fonctionnement est bien adapté au corps à corps, voir si on peut surcharger la méthode pour les armes à distances (enlever l'ajout du physique) ou juste mettre un if avec un booléen en paramétre
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
	Degats(int p_quantite, int p_typeArme)
	{
	    m_quantite = p_quantite;
	    m_typeArme = p_typeArme;
	}

	/**
	 * @return the m_quantite
	 */
	int getQuantite()
	{
	    return m_quantite;
	}

	/**
	 * @return the m_typeArme
	 */
	int getTypeArme()
	{
	    return m_typeArme;
	}
    }
}
