package libupsystem;

public class UPRollParameterFormatException extends IllegalArgumentException
{

    private int lances;
    private int gardes;
    private String errorMessage = "";

    public UPRollParameterFormatException(int nbLances, int nbGardes)
    {
	lances = nbLances;
	gardes = nbGardes;
	if(lances < 0)
	{
	    errorMessage = errorMessage.concat("problem with number of dice rolled, must be >=0\n");
	}
	if(gardes < 0)
	{
	    errorMessage = errorMessage.concat("problem with number of dice kept, must be >=0\n");
	}
	errorMessage = errorMessage.concat("rolled=" + lances + " kept=" + gardes);


    }

    public int getNbLances()
    {
	return lances;
    }

    public int getNbGardes()
    {
	return gardes;
    }

    public String getErrorMessage()
    {
	return errorMessage;
    }
}