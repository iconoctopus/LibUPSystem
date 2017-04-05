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

import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;
import org.duckdns.spacedock.upengine.libupsystem.Perso.Degats;

/**
 *
 * @author ykonoclast
 */
final class IntegStatTestUtils
{

    static int limiteLAncers = 9999;//dix-mille lancers

    static boolean reussiteStatistiqueAttaque(Perso p_perso, int p_nd, int p_distance, int p_nbCoups)
    {
	p_perso.genInit();
	int nbReussites = 0;
	int nbEchecs = 0;
	int compteurActions = 0;
	for (int i = 0; i <= limiteLAncers; ++i)
	{
	    boolean reussite = false;

	    if (compteurActions == p_perso.getActions().size())
	    {
		p_perso.genInit();
		compteurActions = 0;
	    }

	    if (p_distance > 0)
	    {
		if (p_perso.attaquerDist(p_perso.getActions().get(compteurActions), p_nd, p_distance, p_nbCoups).isJetReussi())
		{
		    reussite = true;
		}
		((ArmeDist) p_perso.getInventaire().getArmeCourante()).recharger(p_nbCoups);
	    }
	    else
	    {
		if (p_perso.attaquerCaC(p_perso.getActions().get(compteurActions), p_nd).isJetReussi())
		{
		    reussite = true;
		}

	    }
	    if (reussite)
	    {
		nbReussites++;
	    }
	    else
	    {
		nbEchecs++;
	    }

	    compteurActions++;
	}
	return (nbReussites > nbEchecs);
    }

    static boolean reussiteStatistiqueJetsTrait(Perso p_perso, int p_nd, Trait p_idTrait)
    {
	int nbReussites = 0;
	int nbEchecs = 0;
	for (int i = 0; i <= limiteLAncers; ++i)
	{
	    boolean reussite = false;

	    if (p_perso.effectuerJetTrait(p_idTrait, p_nd).isJetReussi())
	    {
		reussite = true;
	    }
	    if (reussite)
	    {
		nbReussites++;
	    }
	    else
	    {
		nbEchecs++;
	    }
	}
	return (nbReussites > nbEchecs);
    }

    static int nbBlessuresGravesStatistique(int p_degats, int p_rm)
    {
	int nbBlessuresGraves = 0;
	for (int i = 0; i <= limiteLAncers; ++i)
	{//on crée ici un nouveau perso pour chaque test : sinon les blessures s'accumulent entre deux boucles et ils meurrent au final...
	    Perso perso = new Perso(p_rm);
	    perso.etreBlesse(new Degats(p_degats, 0));
	    nbBlessuresGraves += perso.getBlessuresGraves();
	}
	return (int) (nbBlessuresGraves / (limiteLAncers + 1));
    }

}
