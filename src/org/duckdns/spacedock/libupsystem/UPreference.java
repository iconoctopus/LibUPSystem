package org.duckdns.spacedock.libupsystem;

import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

final class UPreference
{

    private static UPreference m_instance;

    static UPreference getInstance()
    {
	if(m_instance == null)
	{
	    m_instance = new UPreference();
	}
	return (m_instance);
    }

    private UPreference()
    {
	InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("JSON/tables_systeme/tab_RM.json");
	JsonReader reader = Json.createReader(in);
	JsonObject object = reader.readObject();
	tableRMInit = object.getJsonArray("init");
	tableRMSante = object.getJsonArray("sante");
	tableRMChoc = object.getJsonArray("choc");
	tableRMND = object.getJsonArray("ND");
    }

    private JsonArray tableRMInit;//new int[] {1,1,3,3,5};
    private JsonArray tableRMSante; //new int[] {1,3,5,7,9};
    private JsonArray tableRMChoc; //new int[] {1,2,3,4,5};
    private JsonArray tableRMND; //new int[] {10,15,25,30,35};*/

    private static BiValue[] tableEffetsArmure = new BiValue[]
    {
	(new BiValue((int) 5, (int) 0)), (new BiValue((int) 5, (int) 5)), (new BiValue((int) 10, (int) 5)), (new BiValue((int) 10, (int) 10)), (new BiValue((int) 15, (int) 10)), (new BiValue((int) 15, (int) 15))
    };//la premiere valeur d'un point est le bonus au ND, la deuxieme la reduction des degats
    private static int[] tableRangArmurePourPoints = new int[]
    {
	5, 10, 15, 20, 25, 30
    };//chaque numéro d'élément est le rang d'armure, chaque valeur est le nombre de points max pour le rang idoine

    private static double[][] tableArmeArmure = new double[][]
    {
	{
	    1, 0.5, 0.3, 0.25, 0.4
	},
	{
	    2, 1, 0.5, 0.3, 0.25
	},
	{
	    3, 2, 1, 0.5, 0.3
	},
	{
	    4, 3, 2, 1, 0.5
	},
    };

    static double getPointsArmureEffectifs(int typeArme, int TypeArmure)
    {
	return tableArmeArmure[typeArme][TypeArmure];
    }

    int getInit(int RM)
    {
	return tableRMInit.getInt(RM - 1);//[RM - 1];
    }

    BiValue getSante(int RM)
    {
	return new BiValue(tableRMSante.getInt(RM - 1), tableRMChoc.getInt(RM - 1));
    }

    static BiValue getEffetsArmure(int points)
    {//TODO WTF? pkoi le -1? Et alors comment faire pour l'indice????
	BiValue result;
	if(points > 0)
	{
	    int i = 0;
	    while(points > tableRangArmurePourPoints[i])
	    {
		i++;
	    }
	    result = new BiValue(tableEffetsArmure[i - 1]);
	}
	else
	{
	    result = new BiValue((int) 0, (int) 0);
	}
	return result;
    }

    int getND(int RM)
    {
	return tableRMND.getInt(RM - 1);
    }
}
