import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OrderingStores {
	
	static void DetermineSizeOfStore(List<List<Store>> counted)
	{
		int currentNumber = 0;
		
		for (List<Store> colum : counted)
			currentNumber += colum.size();
		
		System.out.println("Current number in array is: " + currentNumber);
	}
	
	static void PutStoreInFront(ArrayList<Store> usedList, int placedInFrontPosition, int placedBehindPosition)
	{
		Store iCopy = usedList.get(placedInFrontPosition);
			 
		for (int q = placedInFrontPosition; q > placedBehindPosition; q--)
		{
			usedList.set(q, usedList.get(q - 1));
		}
			 
		usedList.set(placedBehindPosition, iCopy);
	}
	
	static public void Sort(Map<String, Store> stores, List<ArrayList<Store>> usedStores, Map<String, Double> cardAverageCost)
	{
		List<Integer> bestPercentOfPriorityCards = new ArrayList<Integer>(1);
        
		for (int i = 0; i < 5; i++)
			bestPercentOfPriorityCards.add(-1);
			
        List<List<Store>> storesWithMaxPriority = new ArrayList<List<Store>>(1);
        
        for (int i = 0; i < 5; i++)
        	storesWithMaxPriority.add(new ArrayList<Store>(1));
        
        
        for (Map.Entry<String, Store>  currentStore : stores.entrySet())
        {
        	int currentStorePercent = currentStore.getValue().PercentOfPriorityCardsUnavailable();
        	
        	for (int currentBestPercent = 0; currentBestPercent < 5; ++currentBestPercent)
        	{
	        	if (currentStorePercent > bestPercentOfPriorityCards.get(currentBestPercent))
	        	{
	        		for (int z = 4; z > currentBestPercent; z--)
	        		{
	        			storesWithMaxPriority.get(z).clear();
	        			for (int i = 0; i < storesWithMaxPriority.get(z - 1).size(); i++)
	        				storesWithMaxPriority.get(z).add(new Store(storesWithMaxPriority.get(z - 1).get(i)));
	        			
	        			bestPercentOfPriorityCards.set(z, bestPercentOfPriorityCards.get(z - 1));
	        		}
	        		
	        		storesWithMaxPriority.get(currentBestPercent).clear();
	        		storesWithMaxPriority.get(currentBestPercent).add(currentStore.getValue());
	        		
	        		bestPercentOfPriorityCards.set(currentBestPercent, currentStorePercent);
	        		
	        		break;
	        	}
	        	
	        	else if (currentStorePercent == bestPercentOfPriorityCards.get(currentBestPercent))
	        	{
	        		storesWithMaxPriority.get(currentBestPercent).add(currentStore.getValue());
	        		break;
	        	}

        	}
        	
        }
      
        int highestPercent = -1;
        for (List<Store>  currentStoreList : storesWithMaxPriority)
        {
        	ArrayList<Store> tempList = new ArrayList<Store>(0);
        	
        	for (Store currentStore : currentStoreList)
        	{
        		int currentPosition = tempList.size();
        		boolean added = false;
        		if (currentStore.PercentOfCardsUnavailable() > highestPercent)
        			highestPercent = currentStore.PercentOfCardsUnavailable();
        		if (tempList.size() < 10)
        		{
        			added = true; 
        			tempList.add(currentStore);
        		}
        		
        		else if (tempList.get(currentPosition - 1).PercentOfCardsUnavailable() < currentStore.PercentOfCardsUnavailable())
        		{
        			--currentPosition;
        			added = true;
        			tempList.set(currentPosition, currentStore);
        		}
        		
        		if (added)
        		{
	        		for (int z = 0; z < currentPosition; ++z)
	        		{
	        			if (tempList.get(z).PercentOfCardsUnavailable() < tempList.get(currentPosition).PercentOfCardsUnavailable())
	        					PutStoreInFront(tempList, currentPosition, z);
	        			
	        			else if (tempList.get(z).PercentOfCardsUnavailable() == tempList.get(currentPosition).PercentOfCardsUnavailable())
	        			{
	        				if (tempList.get(z).GetPriorityCost() > tempList.get(currentPosition).GetPriorityCost())
	        					PutStoreInFront(tempList, currentPosition, z);
	        					
	        				else if (tempList.get(z).GetPriorityCost() == tempList.get(currentPosition).GetPriorityCost())
	        				{
	        					if (tempList.get(z).GetTotalCost() > tempList.get(currentPosition).GetTotalCost())
		        					PutStoreInFront(tempList, currentPosition, z); 
	        				}
	        			}
	        		}
        		}
           	}
        	
        	usedStores.add(tempList);
        }
        
        PrintWriter writer = null;
        
		try {
			writer = new PrintWriter("saved store info.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        for (int i = 0; i < 5; i++)
        {
        	System.out.println("Current percent is: " + bestPercentOfPriorityCards.get(i));
        	writer.println("Current percent is: " + bestPercentOfPriorityCards.get(i));
        	System.out.println("There are : " + usedStores.get(i).size() + " stores in this group. (There is a max of 10)");
        	writer.println("There are : " + usedStores.get(i).size() + " stores in this group. (There is a max of 10)");
        	
        	for (Store currentStore : usedStores.get(i))
        	{
				currentStore.CalculateAveragePrice(cardAverageCost);
        		
        		System.out.printf("Store is: %s and its priority percent is: %d and its normal percent is: %d." +
        				"\nIts priority price is: $%.2f and its total price is $%.2f. The usual total price for the cards available would be $%.2f.\n", 
        				currentStore.name, currentStore.PercentOfPriorityCardsUnavailable(), currentStore.PercentOfCardsUnavailable(),
        				currentStore.GetPriorityCost(), currentStore.GetTotalCost(), currentStore.GetUsualTotalCost());
        		
        		writer.printf("Store is: %s and its priority percent is: %d and its normal percent is: %d.", currentStore.name, currentStore.PercentOfPriorityCardsUnavailable(), currentStore.PercentOfCardsUnavailable());
        		writer.println("");
        		
        		writer.printf("Its priority price is: $%.2f and its total price is $%.2f. The usual total price for the cards available would be $%.2f.", 
        				currentStore.GetPriorityCost(), currentStore.GetTotalCost(), currentStore.GetUsualTotalCost());
        		writer.println("");
        		
        		currentStore.PrintAllCardInfo(cardAverageCost, writer);
        		
        		System.out.println("\n");
        		writer.println("\n");
        	}
        	System.out.println("\n");
        	writer.println("\n");
        }
        writer.close();
        System.out.println("Highest percent is: " + highestPercent + ".");
	}
	
}
