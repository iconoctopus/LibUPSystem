//TODO: les getters/setters sont encore de mauvaise qualité et nécessitent plus de sécurité
package org.duckdns.spacedock.upengine.libupsystem;
//TODO gérer mort dont mort automatique

import java.util.ArrayList;
import java.util.Collections;

//TODO PARTOUT PARTOUT PARTOUT tester les cas limites des métohdes dans des classes de tests ad hoc (chaque classe doit en avoir une : il y a au moins un constructeur à blinder et dont il faut tester le blindage)
//TODO ajouter malus attaque
//TODO ajouter physique minimum
//TODO gérer l'equive : pour l'instant on ne gère dans la liste des défenses que des références aux comps du domaine corps à corps (donc des parades....) il faut ajouter n traitement spécial (genre une comparaison regardant si esquive vaut mieux)
public class Perso
{

    private ArrayList<Integer> actions;
    private int actionCourante;
    private String libellePerso;
    private CoupleJauge m_jaugeSanteInit;

    private Arme arme;
    private int[] m_traits;
    private Armure armure;

    private int RM;
    private int domaineCombat;//là pour compiler, à revoir
    private Competence compCombat;//là pour compiler, à revoir

    /**
     * constructeur produisant des PNJ générés par rang de menace (RM)
     *
     * @param p_RM
     */
    public Perso(int p_RM)//TODO créer un autre constructeur prenant en paramétre un tableau de traits et de domaines
    {
	RM = p_RM;
	m_traits = new int[5];
	m_traits[0] = p_RM;
	m_traits[1] = p_RM;//TODO tester génération avec RM1, voir si erreurs dans Reference,
	m_traits[2] = p_RM - 1;
	m_traits[3] = p_RM - 1;
	m_traits[4] = p_RM - 1;

	compCombat = new Competence(p_RM, "attaque principale");
	domaineCombat = p_RM;
	UPReference reference = UPReference.getInstance();

	m_jaugeSanteInit = new CoupleJauge(m_traits[0], m_traits[3], m_traits[2], m_traits[1]);

	libellePerso = "PersoRM" + RM;
	//TODO ajouter un constructeur permettant de spécifier ces choses là
	arme = new Arme(2, 3, 0, 0, 0, 0, 0, "rapière mal équilibrée");//2g3 sans bonus ancienne par défaut pour la plupart des PNJ
	armure = new Armure(0, 0);//rien par défaut pour la plupart des PNJ

	genInit();

    }

    @Override
    public String toString()
    {
	return libellePerso;
    }

    public void setLibellePerso(String libellePerso)
    {
	this.libellePerso = libellePerso;
    }

    public Arme getArme()
    {
	return arme;
    }

    public void setArme(Arme arme)
    {
	this.arme = arme;
    }

    public Armure getArmure()
    {
	return armure;
    }

    public void setArmure(Armure armure)
    {
	this.armure = armure;
    }

    public boolean isInconscient()
    {
	return m_jaugeSanteInit.isInconscient();
    }

    public boolean isSonne()//TODO : doit maintenant vérifier LES DEUX jauges
    {
	return m_jaugeSanteInit.isSonne();
    }

    public int getBlessuresGraves()
    {
	return m_jaugeSanteInit.getRemplissage_interne();
    }

    public int getBlessuresLegeres()
    {
	return m_jaugeSanteInit.getBlessuresLegeres();
    }

    public boolean isActif(int phaseActuelle)//renvoie vrai si le personnage a une action dans la phase active comportement indéfini si demande pour pĥase ultérieure ou antérieure
    {

	return ((actions.size() - actionCourante) > 0 && phaseActuelle == actions.get(actionCourante));
    }

    public int getInitTotale()
    {
	//TODO implémenter
	return 0;
    }

    public final void genInit()
    {//TODO mieux trier le tableau, couteux en l'état

	int initiative = m_jaugeSanteInit.getRemplissage_externe();
	actionCourante = 0;
	ArrayList<Integer> tabResult = new ArrayList<>();
	if(initiative > 0)
	{

	    for(int i = 0; i < initiative; i++)
	    {
		tabResult.set(i, RollGenerator.lancer(1, 1, true));
	    }

	}

	Collections.sort(tabResult);
	actions = tabResult;
    }

    //TODO il vaudrait mieux que l'on compose cet obet avec des objets "attaques" qui utiliseraient directement cette méthode devenue cachée, elle appellerait diretement generer degats au passage
    //TODO d'ailleurs ca permettrait d'ajouter l'attaque de base à mains nues
    public RollGenerator.RollResult agirEnCombat(int phaseActuelle, int ND, boolean nonRelanceDix, int traitCourant, int domaineCourant, Competence compCourante)
    {//TODO intégrer notion arme actuelle pour gérer les bonus malus aussi modifier les arguments : ce devrait être à partir de la classe pas de l'extérieur
	//TODO la phase actuelle devrait générer une exception pour prévenir plus haut que l'action n'est pas possible, pas gober l'erreur silencieusement en renvoyant faux
	RollGenerator.RollResult result = new RollGenerator.RollResult(0, false);
	if((actions.size() - actionCourante) > 0 && phaseActuelle == actions.get(actionCourante))
	{
	    actions.set(actionCourante, 11);
	    actionCourante++;
	    result = RollGenerator.jetDeCompetence(domaineCourant, compCourante.getRang(), false, traitCourant, nonRelanceDix, ND);//TODO : placeholder avec specialite tjr fausse
	}
	return result;
    }

    public int getNDPassif()
    {//TODO mieux gérer les armures en s'inspirant de ci-dessous
	//BiValue armureEffective = getArmureEffective(typeArme);
	//int AugND = (int) armureEffective.getX();

	//TODO gérer le malus de parade et d'esquive infligé par l'armure
	int rang = compCombat.getRang();
	int ND = rang * 5 + 5;
	if(rang >= 3)
	{
	    ND += 5;
	}

	return ND;
    }

//TODO : ci-dessous commenté pour que ça compile mais à régler rapidement
    /* private BiValue getArmureEffective(int TypeArme)
    {
	double quotientArmure = (UPReference.getCoeffArmeArmure(TypeArme, armure.getType()));
	int pointsArmureEffectifs = (int) ((double) (quotientArmure) * (double) (armure.getPoints()));
	return UPReference.getEffetsArmure(pointsArmureEffectifs);
    }*/
    //TODO : vérifier que la vieile règle des malus de blessure n'est pas appliquée
    public void etreBlesse(int degats, int typeArme)//TODO objet entrant de type degats incorporant degats et type
    {
	/*BiValue armureEffective = getArmureEffective(typeArme);
		 int redDegats = (int)armureEffective.getY();*/
	int degatsEffectifs = degats/*-redDegats*/;//TODO le code des armures étant suspects il est pour l'instant commenté avant vérification

	if(degatsEffectifs > 0)
	{
	    int resultTest = RollGenerator.lancer(m_traits[0], m_traits[0], false);
	}

    }

    //méthode séparée pour être appelée par le contrôleur qui pourrait bien utiliser les incréments pour autre chose genre cibler
    public int genererDegats(int increments)
    {//TODO retourner objet de type degats incorporant degats et type
	return (RollGenerator.lancer(arme.getDesLances() + increments + m_traits[0], arme.getDesGardes(), m_jaugeSanteInit.isSonne()));
    }

    public RollGenerator.RollResult attaquer(int phaseActuelle, int ND)
    {
	return agirEnCombat(phaseActuelle, ND, m_jaugeSanteInit.isSonne(), m_traits[1], domaineCombat, compCombat);
    }

    /*ATTENTION : ces methodes sont à revoir complètement (et peut être même ne doivent pas exister et leur code être réparti ailleurs)
	//TODO: RollResut pour ces deux methodes et leur passer un ND
	public int defendreActivement(int phaseActuelle) throws UPRollParameterFormatException
    {
	int result = 0;
	if((actions.length - actionCourante) > 0 && phaseActuelle == actions[actionCourante])
	{
	    actions[actionCourante] = 11;
	    actionCourante++;
	    result = RollGenerator.lancer(((int) attaque.getX()) - malus, (int) attaque.getY(), estSonne);
	}
	return result;

    }

    public int defendreInterruption() throws UPRollParameterFormatException
    {
	int result = 0;
	if((actions.length - actionCourante) > 1)
	{
	    actions[actionCourante] = 11;
	    actionCourante++;
	    actions[actionCourante] = 11;
	    actionCourante++;
	    result = RollGenerator.lancer(((int) defense.getX()) - malus, (int) defense.getY(), estSonne);
	}
	return result;
    }
     */
}
