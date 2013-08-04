import java.io.*;
import java.util.*;

// Will need to have options for when store has multiples of card
// Will add in the option to assign cards a rating (1 to 5) where 1 is really wanted and 5 is meh
public class LoadingCardsFromFile {
	
	private Scanner scanner;
	private Boolean activated = false;
	
	private void Activate()
	{
		try {
			scanner = new Scanner(new File("Cards.txt"));
		}
		catch (Exception e){
			System.out.println("Could not read file");
		}
		
		activated = true;
	}
	
	public String GetCardName()
	{
		if (!activated)
			Activate();
		String returnString = scanner.next();
		
		while (!scanner.hasNextInt())
			returnString += " " + scanner.next();
		//System.out.print("Card is called: " + returnString + " ");
		return returnString;
	}
	
	public int GetCardNumber()
	{
		if (!activated)
			Activate();
		int number = scanner.nextInt();
		//System.out.print("Card is called: " + number + " ");
		return number;
	}
	
	public Boolean CardsAreLeft()
	{
		if (!activated)
			Activate();
		
		return scanner.hasNext();
	}
	
	public Boolean GetCardPriority()
	{
		if (scanner.hasNextInt())
		{
			scanner.nextInt();
			
			return true;
		}
		
		return false;
	}
}
