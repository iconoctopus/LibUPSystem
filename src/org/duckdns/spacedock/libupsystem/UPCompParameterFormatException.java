package org.duckdns.spacedock.libupsystem;

public class UPCompParameterFormatException extends IllegalArgumentException
{

    private int domaine;
    private int trait;
    private int comp;
    private String errorMessage="" ;

    public UPCompParameterFormatException(int domaine, int comp, int trait)
    {
	this.domaine = domaine;
	this.comp = comp;
	this.trait = trait;

	if(domaine <= 0)
	{
	    errorMessage = errorMessage.concat("problem with parameter domaine, must be >0\n");
	}
	if(comp < 0)
	{
	    errorMessage = errorMessage.concat("problem with parameter comp, must be >=0\n");
	}
	if(trait < 0)
	{
	    errorMessage = errorMessage.concat("problem with parameter trait, must be >=0\n");
	}
	errorMessage = errorMessage.concat("domaine=" + getDomaine() + " comp=" + getComp() + " trait=" + getTrait());
    }

    /**
     * @return the domaine
     */
    public int getDomaine()
    {
	return domaine;
    }

    /**
     * @return the trait
     */
    public int getTrait()
    {
	return trait;
    }

    /**
     * @return the comp
     */
    public int getComp()
    {
	return comp;
    }

    public String getErrorMessage()
    {
	return errorMessage;
    }
}