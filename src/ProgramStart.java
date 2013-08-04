//package netbeansversion;


import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramStart {
	
    public static void main(String[] args)
    {
        Map<String, Store> stores = new HashMap<String, Store>();

        String basePath = "Website\\", fileEnd = ".htm";
        
	    LoadingCardsFromFile loader = new LoadingCardsFromFile();
        
        List<PreviousCardInfo> cardsBefore = new ArrayList<PreviousCardInfo>(0);
        Map<String, Double> cardAverageCost = new HashMap<String, Double>(0);
        
        while (loader.CardsAreLeft())
        {
        	String cardName = loader.GetCardName();
        	int wantedQuantity = loader.GetCardNumber();
        	Boolean isHighPriority = loader.GetCardPriority();
        	String filePath = basePath + cardName + fileEnd;
        	Pair<Integer, Double> tempPair = new Pair<Integer, Double>(0, 0.0);
        	
        	if (FileExists(filePath))
        	{
        		try {
					LoadPricesFromWebsite.LoadStoresInfo(filePath, cardName, wantedQuantity, isHighPriority, cardsBefore, stores, tempPair);
					int number = 2;
					
					while (FileExists(filePath = basePath + cardName + " " + number + fileEnd))
					{
						LoadPricesFromWebsite.LoadStoresInfo(filePath, cardName, wantedQuantity, isHighPriority, cardsBefore, stores, tempPair);
						number++;
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
        		
	            cardsBefore.add(new PreviousCardInfo(cardName, wantedQuantity, isHighPriority));
	            cardAverageCost.put(cardName, tempPair.getR() / tempPair.getL());
        	}
        	
        	else
        		System.out.printf("The file for card %s doesn't exist!!\n", cardName);
        	
        }

        ArrayList<ArrayList<Store>> usedStores = new ArrayList<ArrayList<Store>>(1);
        
        OrderingStores.Sort(stores, usedStores, cardAverageCost);     
        
        /*
        for (List<Store currentStore : usedStores)
        {
        	System.out.printf("Store is %s,  its priority percent is %d, its total percent is %d, its priority cost is $%.2f, and its total cost is $%.2f.\n", currentStore.name, 
        			currentStore.PercentOfPriorityCardsUnavailable(), currentStore.PercentOfCardsUnavailable(), currentStore.GetPriorityCost(), currentStore.GetTotalCost());
        	
        	currentStore.PrintAllCardInfo();
        	
        	System.out.println("\n");
        }
        */
   
    }
    
    public static Boolean FileExists(String filePath)
	{
		File f = new File(filePath);
		return f.exists();
	}
}
