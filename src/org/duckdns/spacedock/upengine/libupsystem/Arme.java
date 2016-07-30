package org.duckdns.spacedock.upengine.libupsystem;

/**
 * Classe représentant une arme et permettant de générer des dégâts avec
 * celle-ci.Elle est abstraite car l'on ne doit pouvoir instancier que ses
 * dérivées qui sont porteuses du code signifiant pour le CaC et le CaD
 *
 * @author iconoctopus
 */
public abstract class Arme
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
     * le type de l'arme : simple, perce-amure, pénétrante, perce-blindage ou
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
     * la catégorie d'arme (permet de définir la compétence à utiliser)
     */
    private final int m_categorie;
    /**
     * le mode d'attaque de l'arme
     */
    private final int m_mode;
    /**
     * le nom de l'arme
     */
    private final String m_nom;

    /**
     * constructeur d'arme de corps à corps à parti de la référence UP!
     *
     * @param p_indice
     * @param p_qualite la qualite de l'arme
     * @param p_equilibrage l'equilibrage de l'arme, ignoré si l'arme est de
     * maître
     */
    public Arme(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {

	UPReference reference = UPReference.getInstance();
	String nom = reference.getLblArme(p_indice);
	nom = nom.concat(" ");
	int bonusLances = 0;
	int bonusGardes = 0;
	int bonusInitSup = 0;

	//récupération des éléments liés à la qualité et l'équilibrage de l'arme
	if (p_qualite == QualiteArme.maitre)//traitement spécial des armes de maître
	{
	    nom = nom.concat((String) reference.libelles.libQualite.get(QualiteArme.maitre));
	    ++bonusLances;
	    ++bonusGardes;
	    ++bonusInitSup;
	}
	else
	{
	    nom = nom.concat(reference.libelles.liaison);
	    nom = nom.concat(" ");
	    nom = nom.concat(reference.libelles.qualite);
	    nom = nom.concat(" ");

	    switch (p_qualite)
	    {
		case inferieure:
		    --bonusLances;
		    nom = nom.concat((String) reference.libelles.libQualite.get(QualiteArme.inferieure));
		    break;
		case moyenne:
		    nom = nom.concat((String) reference.libelles.libQualite.get(QualiteArme.moyenne));
		    break;
		case superieure:
		    ++bonusLances;
		    nom = nom.concat((String) reference.libelles.libQualite.get(QualiteArme.superieure));
		    break;
	    }

	    nom = nom.concat(" ");
	    nom = nom.concat(reference.libelles.addition);
	    nom = nom.concat(" ");
	    nom = nom.concat(reference.libelles.equilibrage);
	    nom = nom.concat(" ");

	    switch (p_equilibrage)
	    {
		case mauvais:
		    bonusInitSup = -1;
		    nom = nom.concat((String) reference.libelles.libEquilibrage.get(EquilibrageArme.mauvais));
		    break;
		case normal:
		    nom = nom.concat((String) reference.libelles.libEquilibrage.get(EquilibrageArme.normal));
		    break;
		case bon:
		    bonusInitSup = +1;
		    nom = nom.concat((String) reference.libelles.libEquilibrage.get(EquilibrageArme.bon));
		    break;
	    }
	}

	//récupération et construction des caractéristiques de l'arme
	m_desLances = reference.getNbLancesArme(p_indice) + bonusLances;
	m_desGardes = reference.getNbGardesArme(p_indice) + bonusGardes;
	m_bonusInit = reference.getBonusInitArme(p_indice) + bonusInitSup;

	m_typeArme = reference.getTypeArme(p_indice);
	m_malusAttaque = reference.getMalusAttaqueArme(p_indice);
	m_physMin = reference.getPhysMinArme(p_indice);
	m_nom = nom;
	m_categorie = reference.getCategorieArme(p_indice);
	m_mode = reference.getModArme(p_indice);
    }

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

    public int getMode()
    {
	return m_mode;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }

    /**
     * classe utilisée pour encapsuler les résultats d'une attaque réussie ; des
     * dégâts mais aussi le type. On n'utilise pas de collections clé/valeur
     * comme une EnumMap car l'on veut juste un accès simple à des champs
     * définis : inutile de dégrader les performances avec toute la mécanique
     * des collections.
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
	    if (p_quantite >= 0 && p_typeArme >= 0)
	    {
		m_quantite = p_quantite;
		m_typeArme = p_typeArme;
	    }
	    else
	    {
		ErrorHandler.paramAberrant(PropertiesHandler.getInstance().getString("degats") + ":" + p_quantite + " " + PropertiesHandler.getInstance().getString("type") + ":" + p_typeArme);
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

    /**
     * Enum contenant les niveaux de qualite des armes
     */
    public enum QualiteArme
    {
	inferieure, moyenne, superieure, maitre
    };

    /**
     * Enum contenant les niveaux d'équilibrage
     */
    public enum EquilibrageArme
    {
	mauvais, normal, bon
    };
}
