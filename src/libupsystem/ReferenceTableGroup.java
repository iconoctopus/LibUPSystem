package libupsystem;

final class ReferenceTableGroup
{
	 private static int[] tableRMInit = new int[] {1,1,3,3,5};
	 private static int[] tableRMSante = new int[] {1,3,5,7,9};
	 private static int[] tableRMChoc = new int[] {1,2,3,4,5};
	 private static int[] tableRMND = new int[] {10,15,25,30,35};
	 private static BiValue[] tableEffetsArmure = new BiValue[] {(new BiValue((int) 5, (int) 0)),(new BiValue((int) 5, (int) 5)),(new BiValue((int) 10, (int) 5)),(new BiValue((int) 10, (int) 10)),(new BiValue((int) 15, (int) 10)),(new BiValue((int) 15, (int) 15))};//la premiere valeur d'un point est le bonus au ND, la deuxieme la reduction des degats
	 private static int[] tableRangArmurePourPoints= new int[] {5,10,15,20,25,30};//chaque numéro d'élément est le rang d'armure, chaque valeur est le nombre de points max pour le rang idoine
	 
	 private static double[][] tableArmeArmure = new double[][]{
			  { 1, 0.5, 0.3, 0.25, 0.4},
			  { 2, 1, 0.5, 0.3, 0.25 },
			  { 3, 2, 1, 0.5, 0.3 },
			  { 4, 3, 2, 1, 0.5 },
			};
	 
	 static double getPointsArmureEffectifs(int typeArme, int TypeArmure)
	 {
		 return tableArmeArmure[typeArme][TypeArmure];
	 }
	 
	 
	 static int getInit(int RM)
	 {
		 return tableRMInit[RM-1];
	 }
	 
	 static BiValue getSante(int RM)
	 {
		 return new BiValue(tableRMSante[RM-1],tableRMChoc[RM-1]);
	 }
	 
	 static BiValue getEffetsArmure(int points)
	 {//TODO WTF? pkoi le -1? Et alors comment faire pour l'indice????
		 BiValue result;
		 if(points>0)
		 {
			 int i = 0;
			 while(points>tableRangArmurePourPoints[i])
			 {
				 i++;
			 }
		 result = new BiValue(tableEffetsArmure[i-1]);
		 }
		 else
		 {
			 result = new BiValue((int)0,(int)0);
		 }
		 return result;
	 }
	 
	 static int getND(int RM)
	 {
		 return tableRMND[RM-1];
	 }
}