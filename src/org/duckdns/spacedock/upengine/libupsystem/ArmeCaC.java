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

/**
 *
 * @author ykonoclast
 */
public class ArmeCaC extends Arme
{

    /**
     * la VD supplémentaire provenant de la qualité qui doit être ajoutée
     * lorsque getVD est interrogé
     */
    private final int m_bonusVDSup;
    /**
     * le bonus d'init supplémentaire provenant de l'équilibrage qui doit être
     * ajouté lorsque getBonusInit est appelé
     */
    private final int m_bonusInitSup;

    /**
     * constructeur identique à celui de la superclasse, celle-ci emploie la
     * qualité et l'équilibrage pour le nommage, ici on en applique les effets
     *
     * @param p_indice
     * @param p_qualite
     * @param p_equilibrage
     */
    public ArmeCaC(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	super(p_indice, p_qualite, p_equilibrage);

	if (p_qualite == QualiteArme.maitre)//traitement spécial des armes de maître
	{
	    m_bonusVDSup = 6;
	    m_bonusInitSup = +1;
	}
	else
	{
	    switch (p_qualite)
	    {
		case inferieure:
		    m_bonusVDSup = - 3;
		    break;
		case superieure:
		    m_bonusVDSup = +3;
		    break;
		default:
		    m_bonusVDSup = 0;
		    break;
	    }
	    switch (p_equilibrage)
	    {
		case mauvais:
		    m_bonusInitSup = -1;
		    break;
		case bon:
		    m_bonusInitSup = +1;
		    break;
		default:
		    m_bonusInitSup = 0;
		    break;
	    }
	}
    }

    /**
     * ajoute la compétence d'arme au trait physique
     *
     * @param p_Traits
     * @param p_arbreDomComp
     * @return
     */
    @Override
    int extractBonusCarac(GroupeTraits p_Traits, ArbreDomaines p_arbreDomComp)
    {
	int domaine = 3;//corps à corps
	int competence = getCategorie() * 2;//les attaques sont à catégorie *2, les parades à catégorie * 2 +1

	return (p_arbreDomComp.getRangComp(domaine, competence) + p_Traits.getTrait(GroupeTraits.Trait.PHYSIQUE));
    }

    /**
     *
     * @return la valeur de dégâts modifiée par les valeurs d'équilibrage et de
     * qualité
     */
    @Override
    public int getVD()
    {
	return super.getVD() + m_bonusVDSup;
    }

    /**
     *
     * @return le bonus d'init modifié par les valeurs d'équilibrage et de
     * qualité
     */
    @Override
    public int getBonusInit()
    {
	return super.getBonusInit() + m_bonusInitSup;
    }
}
