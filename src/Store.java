//package netbeansversion;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Store {
	
    public Store(String nameTemp, List<PreviousCardInfo> previousCards)
    {
    	for (PreviousCardInfo cardPrevious : previousCards)
    	{
    		percentOfCardAvailable.put(cardPrevious.name, 0);
    		numberOfCardsWanted += cardPrevious.wantedNumber;
    		
    		if (cardPrevious.isPriority)
    			numberOfPriorityCardsWanted += cardPrevious.wantedNumber;
    	}
    	
    	name = nameTemp;
    }

    public Store(Store store) {
    	totalCost = store.totalCost;
        
    	priorityCost = store.priorityCost;
        
        numberOfCardsWanted = store.numberOfCardsWanted;
        numberOfCardsAvailable = store.numberOfCardsAvailable;
        
        numberOfPriorityCardsWanted = store.numberOfPriorityCardsWanted;
        numberOfPriorityCardsAvailable = store.numberOfPriorityCardsAvailable;
        
        
        currentCardName = store.currentCardName;
        currentCardIsPriority = store.currentCardIsPriority;
        numberOfCurrentCardWanted = store.numberOfCurrentCardWanted;
        numberOfCurrentCardAvailable = store.numberOfCurrentCardAvailable;
        
        cardsAvailable = new HashMap<String, List<Card>>(store.cardsAvailable);
        
        percentOfCardAvailable = new HashMap<String, Integer>(store.percentOfCardAvailable);
	}

	private double totalCost = 0.0;
    public double GetTotalCost() { return totalCost; }
    
    private double usualTotalCost = 0.0;
    public double GetUsualTotalCost() { return usualTotalCost; }
    
    private double priorityCost = 0.0;
    public double GetPriorityCost() { return priorityCost; }
    
    private int numberOfCardsWanted = 0;
    private int numberOfCardsAvailable = 0;
    
    private int numberOfPriorityCardsWanted = 0;
    private int numberOfPriorityCardsAvailable = 0;
    
    
    private String currentCardName = "";
    private Boolean currentCardIsPriority = false;
    private int numberOfCurrentCardWanted = 0;
    private int numberOfCurrentCardAvailable = 0;
    
    private Map<String, List<Card>> cardsAvailable = new HashMap<String, List<Card>>();
    
    private Map<String, Integer> percentOfCardAvailable = new HashMap<String, Integer>();
   

    public String name;
    
    private void SetUpOldCardInfo()
    {
    	percentOfCardAvailable.put(currentCardName,  numberOfCurrentCardAvailable * 100 / numberOfCurrentCardWanted );
    	
    	numberOfCardsWanted += numberOfCurrentCardWanted;
    	numberOfCardsAvailable += numberOfCurrentCardAvailable;
    	
    	if (currentCardIsPriority)
    	{
    		numberOfPriorityCardsWanted += numberOfCurrentCardWanted;
    		numberOfPriorityCardsAvailable += numberOfCurrentCardAvailable;
    	}
    }
    
    public void NewCard(String cardName, int numberWanted, Boolean isPriority)
    {    	
        if (!cardsAvailable.containsKey(cardName))
        {
        	if (currentCardName != "")
        		SetUpOldCardInfo();
            
            cardsAvailable.put(cardName, new ArrayList<Card>());
            
            // Setting up info for current card
            
            currentCardName = cardName;
            currentCardIsPriority = isPriority;            
            numberOfCurrentCardWanted = numberWanted;
            numberOfCurrentCardAvailable = 0;
        }

    }

    public int PercentOfCardsUnavailable()
    {
        if (currentCardName != "")
        {
        	SetUpOldCardInfo();
	    	
	    	currentCardName = "";
	    	numberOfCurrentCardWanted = 0;
	    	numberOfCurrentCardAvailable = 0;
	        
        }
 
        return numberOfCardsAvailable * 100 / numberOfCardsWanted;
    }
    
    public int PercentOfPriorityCardsUnavailable()
    {
        if (currentCardName != "")
        {
        	SetUpOldCardInfo();
	    	
	    	currentCardName = "";
	    	numberOfCurrentCardWanted = 0;
	    	numberOfCurrentCardAvailable = 0;
        }
        
        if (numberOfPriorityCardsWanted != 0)
        	return numberOfPriorityCardsAvailable * 100 / numberOfPriorityCardsWanted;
        
        return 100;
    }
    
    
    public int AddCardInfo(String cardName, String condition, int quantity, double cost)
    {
        Card tempCard = new Card();
       
    	int numberOfThisOptionUsed = 0;
    	
        if (numberOfCurrentCardAvailable < numberOfCurrentCardWanted)
        	numberOfThisOptionUsed = Math.min(quantity, numberOfCurrentCardWanted - numberOfCurrentCardAvailable);
        
        tempCard.SetValues(condition, quantity, numberOfThisOptionUsed, cost);
    	cardsAvailable.get(cardName).add(tempCard);
    	
    	totalCost += cost * numberOfThisOptionUsed;
    	
    	if (currentCardIsPriority)
    		priorityCost += cost * numberOfThisOptionUsed;
    	
    	numberOfCurrentCardAvailable += numberOfThisOptionUsed;
    	
    	return numberOfThisOptionUsed;
    }
    
    public void CalculateAveragePrice(Map<String, Double> cardAverageCost)
    {
    	for (Map.Entry<String, List<Card>>  cardList : cardsAvailable.entrySet())
        {
    		for (Card card : cardList.getValue())
        	{
    			usualTotalCost += Math.min(card.GetWantedQuantity(), card.GetQuantity()) * cardAverageCost.get(cardList.getKey());
    			//System.out.println(cardList.getKey());
    			//System.out.printf("Usual total cost: $%.2f, most recent cost: $%.2f, average cost: $%.2f.\n", usualTotalCost,  Math.min(card.GetWantedQuantity(), card.GetQuantity()) * cardAverageCost.get(cardList.getKey()),cardAverageCost.get(cardList.getKey()));
        	}
        }
    	/*
    	try {
			//Thread.sleep(40000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    }
    
    /*
    public void PrintUnavaialableCards()
    {	    
    	PrintAllCardInfo();
	    	 
    	System.out.print("\n");
    	
    }
    */
    
    public void PrintAllCardInfo(Map<String, Double> cardAverageCost, PrintWriter writer)
    {
        for (Map.Entry<String, List<Card>>  cardList : cardsAvailable.entrySet())
        {
        	for (Card card : cardList.getValue())
        	{
        		if (Math.min(card.GetWantedQuantity(), card.GetQuantity()) > 0)
        		{
	        		System.out.printf("Card is called: %s, condition is: %s, there are %d of this, %d of this are used, and its cost is: $%.2f per card ($%.2f total paid). " +
	        				"The average cost for one of this card is $%.2f.\n",
	        				cardList.getKey(), card.GetCondition(), card.GetQuantity(), card.GetWantedQuantity(), card.GetCost(), card.GetCost() * Math.min(card.GetWantedQuantity(), card.GetQuantity()),
	        				cardAverageCost.get(cardList.getKey()));
        			writer.printf("Card is called: %s, condition is: %s, there are %d of this, %d of this are used, and its cost is: $%.2f per card ($%.2f total paid). " +
	        				"The average cost for one of this card is $%.2f.",
	        				cardList.getKey(), card.GetCondition(), card.GetQuantity(), card.GetWantedQuantity(), card.GetCost(), card.GetCost() * Math.min(card.GetWantedQuantity(), card.GetQuantity()),
	        				cardAverageCost.get(cardList.getKey()));
        			writer.println("");
        		}
        	}
        }
        
        for (Map.Entry<String, Integer>  card : percentOfCardAvailable.entrySet())
        {
        	if (!card.getValue().equals(100))
        	{
        		System.out.printf("The card %s is unavailable from this store, with only a percent of %d%% available.\n", card.getKey(), card.getValue());
        		writer.printf("The card %s is unavailable from this store, with only a percent of %d%% available.", card.getKey(), card.getValue());
        		writer.println("");
        	}
        }
    }


}
