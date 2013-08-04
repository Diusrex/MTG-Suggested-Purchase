/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package netbeansversion;

/**
 *
 * @author m.redshaw1
 */
public class Card {
    String condition = "";
    int quantity = 0;
    int wantedQuantity = 0;
    double cost = 0.0;
    
    
    
    //Boolean activated = false;
    
    //public Boolean GetActivated() { return activated; }
    
    public void SetValues(String actualCondition, int actualQuantity, int actualWantedQuantity, double actualCost)
    {
        condition = actualCondition;
        quantity = actualQuantity;
        cost = actualCost;
        wantedQuantity = actualWantedQuantity;
    }

    public String GetCondition() { return condition; }
    public int GetQuantity() { return quantity; }
    public void SetWantedQuantity(int newWantedQuantity) { wantedQuantity = newWantedQuantity; }
    public int GetWantedQuantity() { return wantedQuantity; }
    public double GetCost() { return cost; }

}
