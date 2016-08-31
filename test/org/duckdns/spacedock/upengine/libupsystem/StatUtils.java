/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

/**
 *
 * @author iconoctopus
 */
final class StatUtils
{

    static boolean reussiteStatistiqueAttaque(Perso p_perso, int p_nd, int p_distance, int p_nbCoups)
    {
	p_perso.genInit();
	int nbReussites = 0;
	int nbEchecs = 0;
	int compteurActions = 0;
	for (int i = 0; i <= 999999; ++i)//un million de lancers
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
		((ArmeDist) p_perso.getArmeCourante()).recharger(p_nbCoups);
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

    static int degatsStatistiques(int p_rm, Arme p_arme, int p_increments)
    {
	int total_degats = 0;
	Perso perso = new Perso(p_rm);
	if (p_arme != null)
	{
	    perso.addArme(p_arme);
	    perso.setArmeCourante(perso.getListArmes().size() - 1);
	}
	else
	{
	    perso.addArme(new ArmeCaC(3, Arme.QualiteArme.maitre, Arme.EquilibrageArme.mauvais));
	    perso.rengainer();//on est dans le cas où la méthode appelante veut tester les mains nues, on en profite pour tester que rangainer fonctionne bien
	}
	for (int i = 0; i <= 99/*999999*/; ++i)//un million de lancers
	{
	    total_degats += perso.genererDegats(p_increments).getQuantite();
	}
	return (int) (total_degats / 100/*0000*/);
    }

    static int nbBlessuresGravesStatistique(int p_degats, int p_rm)
    {
	int nbBlessuresGraves = 0;
	for (int i = 0; i <= 999999; ++i)//un million de lancers
	{//on crée ici un nouveau perso pour chaque test : sinon les blessures s'accumulent entre deux boucles et ils meurrent au final...
	    Perso perso = new Perso(p_rm);
	    perso.etreBlesse(new Arme.Degats(p_degats, 0));
	    nbBlessuresGraves += perso.getBlessuresGraves();
	}
	return (int) (nbBlessuresGraves / 1000000);
    }
}
