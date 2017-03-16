package org.duckdns.spacedock.upengine.libupsystem;

/**
 * classe représentant une armure et encapsulant les traitements des effets de
 * celle-ci sur les attaques entrantes
 *
 * @author iconoctopus
 */
final class Armure
{

    /**
     * le malus d'esquive de l'armure
     */
    private final int m_malusEsquive;
    /**
     * le malus de parade de l'armure
     */
    private final int m_malusParade;
    /**
     * les points d'armure
     */
    private final int m_points;
    /**
     * le type d'armure
     */
    private final int m_type;

    /**
     * constructeur d'armure
     */
    Armure(int p_points, int p_type, int p_malusEsquive, int p_malusParade)
    {
	m_type = p_type;
	m_points = p_points;
	m_malusEsquive = p_malusEsquive;
	m_malusParade = p_malusParade;
    }

    /**
     *
     * @param p_typArm
     * @return renvoie le bonus au ND effectif offert par cette armure contre un
     * type d'arme donné
     */
    int getBonusND(int p_typArm)
    {
	return UPReference.getInstance().getArmureBonusND(m_points, p_typArm, m_type);
    }

    /**
     * @return the m_malusEsquive
     */
    int getMalusEsquive()
    {
	return m_malusEsquive;
    }

    /**
     * @return the m_malusParade
     */
    int getMalusParade()
    {
	return m_malusParade;
    }

    /**
     *
     * @param p_typArm
     * @return renvoie la réduction effective des dégâts offerte par cette
     * armure contre un type d'arme donné
     */
    int getRedDegats(int p_typArm)
    {
	return UPReference.getInstance().getArmureRedDegats(m_points, p_typArm, m_type);
    }
}
