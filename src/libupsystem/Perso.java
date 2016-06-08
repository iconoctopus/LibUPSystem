//TODO: les getters/setters sont encore de mauvaise qualité et nécessitent plus de sécurité
package libupsystem;
//TODO gérer mort dont mort automatique

import java.util.Arrays;

public class Perso
{
	protected int NDPassif;
	protected int[] actions;
	protected String libellePerso;
	protected BiValue jaugeSante;//x est la taille de la jauge ; y le point choc
	protected boolean inconscient;
	protected boolean sonne;
	protected int blessuresGraves;
	protected int blessuresLegeres;
	public Arme arme;//TODO wtf? pqoi public?
	protected int actionCourante;
	protected int physique;
	protected int volonte;
	protected int coordination;
	protected int initiative;
	public Armure armure;//TODO wtf? pqoi public?

	public Perso()
	{
		super();
		blessuresGraves = 0;
		blessuresLegeres = 0;
		inconscient = false;
		sonne = false;
	}

	public String getLibellePerso()
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

	public BiValue getJaugeSante()
	{
		return jaugeSante;
	}

	public boolean isInconscient()
	{
		return inconscient;
	}

	public boolean isSonne()
	{
		return sonne;
	}

        //TODO plutôt renvoyer un "état de santé" via objet dédié [jauge] depuis classe interne que d'avoir ces vilains get/set on pourrait d'ailleurs utiliser cette jauge ailleurs là où les blssures sont directement appelées
	public int getBlessuresGraves()
	{
		return blessuresGraves;
	}

	public int getBlessuresLegeres()
	{
		return blessuresLegeres;
	}

	public boolean isActif(int phaseActuelle)//renvoie vrai si le personnage a une action dans la phase active
	{//TODO gérer la possibilité que les p
		return ((actions.length - actionCourante) > 0 && phaseActuelle == actions[actionCourante]);
	}

	public final void genInit() throws UPRollParameterFormatException
	{//TODO mieux trier le tableau, couteux en l'état
	actionCourante=0;
	int[] tabResult;
	if(initiative > 0)
	{
	    tabResult = new int[initiative];

	    for(int i = 0; i < initiative; i++)
	    {
		tabResult[i] = UPSystem.lancer(1, 1, true);
	    }
	    tabResult[0] -= arme.getBonusInit();//TODO virer ces notions de bonus à l'init: on utilisera l'init totale maintenant
	}
	else
	{
	    tabResult = new int[0];
	}
	Arrays.sort( tabResult );
	actions = tabResult;
	}

        //TODO il vaudrait mieux que l'on compose cet obet avec des objets "attaques" qui utiliseraient directement cette méthode devenue cachée, elle appellerait diretement generer degats au passage
        //TODO d'ailleurs ca permettrait d'ajouter l'attaque de base à mains nues
	 public RollResult agirEnCombat(int phaseActuelle, int ND, boolean nonRelanceDix, int traitCourant, int domaineCourant, Competence compCourante) throws UPRollParameterFormatException
		{//TODO intégrer notion arme actuelle pour gérer les bonus malus aussi modifier les arguments : ce devrait être à partir de la classe pas de l'extérieur
		//TODO la phase actuelle devrait générer une exception pour prévenir plus haut que l'action n'est pas possible, pas gober l'erreur silencieusement en renvoyant faux
                    RollResult result = new RollResult(0, false);
		if((actions.length - actionCourante) > 0 && phaseActuelle == actions[actionCourante])
		{
		    actions[actionCourante] = 11;
		    actionCourante++;
		    result = UPSystem.lancerCompetence(domaineCourant, compCourante, traitCourant, nonRelanceDix, ND);
		}
		return result;
		}


	 public RollResult etreAttaque(int score, int typeArme)//par défaut on n'utilise que la défense passive et on laisse passer si celle-ci est dépassée, overidable pour les casses héritières
	 {
		 BiValue armureEffective = getArmureEffective(typeArme);
		 int AugND = (int)armureEffective.getX();
		 return UPSystem.extraireIncrements(score,NDPassif+AugND);
	 }

	 private BiValue getArmureEffective(int TypeArme)
	 {
		 double quotientArmure = (ReferenceTableGroup.getPointsArmureEffectifs(TypeArme, armure.getType()));
		 int pointsArmureEffectifs = (int)((double)(quotientArmure)*(double)(armure.getPoints()));
		 return ReferenceTableGroup.getEffetsArmure(pointsArmureEffectifs);
	 }
	 //TODO : vérifier que la vieile règle des malus de blessure n'est pas appliquée
	public void etreBlesse(int degats, int typeArme) throws UPRollParameterFormatException//TODO cela ne devrait pas marcher comme cela, il faudrait un objet de type degats incorporant le type d'arme
	{
		 /*BiValue armureEffective = getArmureEffective(typeArme);
		 int redDegats = (int)armureEffective.getY();*/
		int degatsEffectifs = degats/*-redDegats*/;//TODO le code des armures étant suspects il est pour l'instant commenté avant vérification
		if(degatsEffectifs>0){
	double quotient;
	int blessGraves;
	int resultTest = UPSystem.lancer(physique, physique, false);
	blessuresLegeres += degatsEffectifs;
	if(resultTest < blessuresLegeres)
	{
	    quotient = ((double)(blessuresLegeres) - (double) (resultTest));//TODO remplacer ce calcul par un modulo ou autre, en tout cas quelque chose de plus propre
	    quotient = quotient / 10.0;
	    blessGraves = (int) quotient +1;
	    blessuresLegeres = 0;
	    blessuresGraves += blessGraves;
	    if(blessuresGraves >= jaugeSante.getY())
	    {
		sonne = true;
		if(blessuresGraves > jaugeSante.getY())
		{
			if(initiative>0)
			{
				initiative--;//TODO: à terme prendre garde à la régénération de cette valeur avec le système de soin
			}
		    if(blessuresGraves >= jaugeSante.getX() || UPSystem.lancer(volonte, volonte, sonne) < (5 * blessuresGraves))
		    {
			inconscient = true;
		    }
		}
	    }
	}
		}

	}


	public int genererDegats(int increments) throws UPRollParameterFormatException
	{
		return (UPSystem.lancer(arme.getDesLances()+increments+physique,arme.getDesGardes(), sonne));
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
	    result = UPSystem.lancer(((int) attaque.getX()) - malus, (int) attaque.getY(), estSonne);
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
	    result = UPSystem.lancer(((int) defense.getX()) - malus, (int) defense.getY(), estSonne);
	}
	return result;
    }
    */
}
