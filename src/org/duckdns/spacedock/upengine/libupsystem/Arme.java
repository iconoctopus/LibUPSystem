package org.duckdns.spacedock.upengine.libupsystem;

/**
 * Classe représentant une arme. on laisse volontairement la génération de
 * dégâts dans la classe perso car les get/set de l'arme sont de toute façon
 * nécessaires pour l'affichage plus haut, de plus la classe Perso n'est pas
 * libre de quand et comment elle génère ses dégâts, il n'y a donc pas de vrai
 * intérêt à encapsuler ces éléments ici. La classe Arme est donc une simple
 * structure à laquelle est ajouté la capacité à générer des copies modifiées
 * d'elle-même
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
    private int m_categorie;
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
	return m_physMin;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }

    /**
     * constructeur principal de l'arme, à destination de la classe de référence
     * qui l'alimente par JSON, il vaut mieux faire appel à une copie de l'un de
     * ses templates pour créer une nouvelle arme
     *
     * @param p_lance dés lancés dans la VD
     * @param p_garde ds gardés dans la VD
     * @param p_initBonus bonus à l'init totale
     * @param p_malusAttaque malus aux jets d'attaque
     * @param p_physMin physique minimal pour manier l'arme
     * @param p_categorie catégorie de l'arme (pour les compétences)
     * @param p_type type de l'arme (pour les effets d'armure)
     */
    Arme(int p_lance, int p_garde, int p_initBonus, int p_malusAttaque, int p_physMin, int p_categorie, int p_type, String p_nom)
    {//TODO refaire sur le modèle de piecearmure
	//ajouter la possibilité de spéifier la qualité et l'quilibrage pour spécifier la fabrique d'arme
	m_desLances = p_lance;
	m_desGardes = p_garde;
	m_bonusInit = p_initBonus;
	m_typeArme = p_type;
	m_malusAttaque = p_malusAttaque;
	m_physMin = p_physMin;
	m_nom = p_nom;
    }

}
