import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class LoadPricesFromWebsite {
	
	static public void LoadStoresInfo(String filePath, String cardName, int wantedQuantity, Boolean priority, 
			List<PreviousCardInfo> previousCards,  Map<String, Store> stores, Pair<Integer, Double> cardAverage) throws IOException
	{
		File in = new File(filePath);
		
	    Document doc = Jsoup.parse(in, "UTF-8");
	
	    Elements allStoresInfo = doc.select("[class*=vendor]");
		
	    for (Map.Entry<String, Store>  currentStore : stores.entrySet())
	    {
	        currentStore.getValue().NewCard(cardName, wantedQuantity, priority);
	    }
	    
	    double totalCostForUsedCard = 0;
	    int totalNumberForCard = 0;
	    
	    for (Element mainElement : allStoresInfo)
	    {
	        // get the value from href attribute
	
	        String condition = mainElement.select("[class*=condition]").first().text();
	
	        if (!condition.equals("Heavily Played") && !condition.equals("Moderately Played"))
	        {
	                    //System.out.printf("All info: %s\n",	mainElement.toString());
	
	            String storeName = mainElement.select("[class*=seller]").select("a[href]").first().text();
	
	            if (!stores.containsKey(storeName))
	            {
	            	//System.out.println("The store added for card " + cardName + " is " + storeName + ".");
	            	Store tempStore = new Store(storeName, previousCards);
	            	tempStore.NewCard(cardName, wantedQuantity, priority);
	                stores.put(storeName, tempStore);
	            }
	
	            String quantityString = mainElement.select("[class*=quantity]").text();
	            quantityString = quantityString.replaceAll("[^\\d.]", "");
	            int quantity = Integer.parseInt(quantityString);
	
	            String costStringCrude = mainElement.select("[class*=price]").first().text();
	
	            String costString = new String();
	
	            // This is not needed, but will be kept anyway
	            for (int i = 0; i < costStringCrude.length(); i++)
	            {
	                if (costStringCrude.charAt(i) == ' ')
	                    break;
	
	                costString += costStringCrude.charAt(i);
	
	            }
	
	            costString = costString.replaceAll("[^\\d.]", "");
		   
                    if (costString.equals(""))
	            {
	            	costString = mainElement.select("[class*=price]").get(1).text();
		            costString = costString.replaceAll("[^\\d.]", "");
	            }

	            double cost = Double.parseDouble(costString);
	            	
	            int numberUsedTemp = stores.get(storeName).AddCardInfo(cardName, condition, quantity, cost);
	            totalCostForUsedCard += numberUsedTemp * cost;
	            totalNumberForCard += numberUsedTemp;
	        }
	
	    }
	    
	    cardAverage.setL(cardAverage.getL() + totalNumberForCard);
	    cardAverage.setR(cardAverage.getR() + totalCostForUsedCard);
	   
	}
}
