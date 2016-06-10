package org.duckdns.spacedock.libupsystem;

public class BasicNPCFighter extends Perso
{

    private int RM;
    private int domaineCombat;
    private Competence compCombat;

    public BasicNPCFighter(int p_RM)
    {
	super();
	RM = p_RM;

	physique = p_RM;
	coordination = p_RM;
	volonte = p_RM - 1;
	compCombat = new Competence(p_RM, false);//TODO simpliste, il peut etre specialiste si RM3+
	domaineCombat = p_RM;
	UPreference reference = UPreference.getInstance();
	NDPassif = reference.getND(p_RM);
	initiative = reference.getInit(p_RM);
	jaugeSante = reference.getSante(p_RM);
	libellePerso = "PersoRM" + RM;
	//TODO ajouter un constructeur permettant de spécifier ces choses là
	arme = new Arme(2, 3, 0, 0);//2g3 sans bonus ancienne par défaut pour la plupart des PNJ
	armure = new Armure(0, 0);//rien par défaut pour la plupart des PNJ

	genInit();
    }

    public int getRM()
    {
	return RM;
    }

    //TODO complètement revoir cette méthode pour qu'elle génère directement les dégâts
    //TODO probablement la rappatrier dans la superclasse, erichissant au passage la méthode appelée
    public RollResult attaquer(int phaseActuelle, int ND)
    {
	return agirEnCombat(phaseActuelle, ND, sonne, coordination, domaineCombat, compCombat);
    }

    @Override
    public String toString()
    {
	String libelle = "ND=" + NDPassif + " init=" + initiative + " SanteX=" + jaugeSante.getX() + " santeY=" + jaugeSante.getY();
	return libelle;

    }

}
