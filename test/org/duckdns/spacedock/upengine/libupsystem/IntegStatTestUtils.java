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
import static org.junit.Assert.fail;

/**
 * classe fournissant des utilitaires pour les tests statistiques mis en oeuvre
 * dans les autres classes
 *
 * @author ykonoclast
 */
final class IntegStatTestUtils
{

    static int limiteLAncers = 9999;//dix-mille lancers

    /**
     * effectue un grand nombre de jets d'attaque et répond vrai si la majorité
     * est une réussite
     *
     * @param p_perso
     * @param p_nd
     * @param p_distance
     * @param p_nbCoups
     * @return
     */
    static boolean reussiteStatistiqueAttaque(Perso p_perso, int p_nd, int p_distance, int p_nbCoups)
    {
	p_perso.genInit();
	int nbReussites = 0;
	int nbEchecs = 0;
	for (int i = 0; i <= limiteLAncers; ++i)
	{//boucle d'un grand nombre de lancers, on utilise les actions du personnage, cela teste au passage le bon fonctionnement du système d'init
	    boolean reussite = false;

	    if (p_perso.getActions().isEmpty())
	    {//le tour du perso est terminé, on régènére ses actions
		p_perso.genInit();
	    }

	    if (p_distance > 0)
	    {//cas d'une attaque à distance
		if (p_perso.attaquerDist(p_perso.getActions().get(0), p_nd, p_distance, p_nbCoups).isJetReussi())
		{
		    reussite = true;
		}
		((ArmeDist) p_perso.getInventaire().getArmeCourante()).recharger(p_nbCoups);//pour que le test continue, on recharge l'arme, sinon elle se videra au bout de quelques boucles
	    }
	    else
	    {//cas d'une attaque au corps à corps
		if (p_perso.attaquerCaC(p_perso.getActions().get(0), p_nd).isJetReussi())
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
	}
	return (nbReussites > nbEchecs);//si une majorité des jets est réussie
    }

    /**
     * effectue un grand nombre de jets de trait et répond vrai si la majorité
     * est une réussite
     *
     * @param p_perso
     * @param p_nd
     * @param p_idTrait
     * @return
     */
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

    /**
     * inflige une quantité de dégât donnée un grand nombre de fois de suite à
     * un même nombre d'incarnations de persos et renvoie le nombre de blessures
     * graves qu'ils reçoiventen moyenne
     *
     * @param p_degats
     * @param p_rm
     * @return
     */
    static int nbBlessuresGravesStatistique(int p_degats, int p_rm)
    {
	int nbBlessuresGraves = 0;
	for (int i = 0; i <= limiteLAncers; ++i)
	{//on crée ici un nouveau perso pour chaque test : sinon les blessures s'accumulent entre deux boucles et ils meurrent au final...
	    Perso perso = new Perso(p_rm);
	    perso.etreBlesse(new Degats(p_degats, 0));
	    nbBlessuresGraves += perso.getEtatVital().getBlessuresGraves();
	}
	return (int) (nbBlessuresGraves / (limiteLAncers + 1));
    }
}
