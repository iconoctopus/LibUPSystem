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

import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 *
 * @author ykonoclast
 */
public class ArmeCaC extends Arme
{

    /**
     * constructeur identique à celui de la superclasse
     *
     * @param p_indice
     * @param p_qualite
     * @param p_equilibrage
     */
    public ArmeCaC(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	super(p_indice);
	int bonusVD = 0;
	int bonusInitSup = 0;
	UPReferenceSysteme referenceSys = UPReferenceSysteme.getInstance();
	UPReferenceArmes referenceArm = UPReferenceArmes.getInstance();
	m_nom = m_nom.concat(" ");
	//récupération des éléments liés à la qualité et l'équilibrage de l'arme
	if (p_qualite == QualiteArme.maitre)//traitement spécial des armes de maître
	{
	    m_nom = m_nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.maitre));
	    bonusVD = bonusVD + 6;
	    ++bonusInitSup;
	}
	else
	{
	    m_nom = m_nom.concat(referenceSys.getCollectionLibelles().liaison);
	    m_nom = m_nom.concat(" ");
	    m_nom = m_nom.concat(referenceSys.getCollectionLibelles().qualite);
	    m_nom = m_nom.concat(" ");

	    switch (p_qualite)
	    {
		case inferieure:
		    bonusVD = bonusVD - 3;
		    m_nom = m_nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.inferieure));
		    break;
		case moyenne:
		    m_nom = m_nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.moyenne));
		    break;
		case superieure:
		    bonusVD = bonusVD + 3;
		    m_nom = m_nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.superieure));
		    break;
	    }

	    m_nom = m_nom.concat(" ");
	    m_nom = m_nom.concat(referenceSys.getCollectionLibelles().addition);
	    m_nom = m_nom.concat(" ");
	    m_nom = m_nom.concat(referenceSys.getCollectionLibelles().equilibrage);
	    m_nom = m_nom.concat(" ");

	    switch (p_equilibrage)
	    {
		case mauvais:
		    bonusInitSup = -1;
		    m_nom = m_nom.concat((String) referenceArm.getListEquilibrage().get(EquilibrageArme.mauvais));
		    break;
		case normal:
		    m_nom = m_nom.concat((String) referenceArm.getListEquilibrage().get(EquilibrageArme.normal));
		    break;
		case bon:
		    bonusInitSup = +1;
		    m_nom = m_nom.concat((String) referenceArm.getListEquilibrage().get(EquilibrageArme.bon));
		    break;
	    }
	}
	m_bonusInit += bonusInitSup;
	m_vd += bonusVD;
    }
}
