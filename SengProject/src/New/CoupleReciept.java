package New;


import java.util.LinkedList;
public class CoupleReciept {
	
	/**
	 * A class to hold the recipt of each couple of buy and sell
	 */
	public CoupleReciept(){
		buyList = new LinkedList<Integer>();
		sellList = new LinkedList<Integer>();
		debit = 0; // bedehkar
		credit = 0; // talabkar
		time = 0;
	}
	
	/**
	 * TO set the buy and sell List
	 * @param aBuyList
	 */
	public void setBuyList(LinkedList<Integer> aBuyList ){
		this.buyList = aBuyList;
	}
	
	public void setSellList( LinkedList<Integer> aSellList){
		this.sellList = aSellList;
	}
	
	public void addToCredit( double aPrice){
		this.credit += aPrice;
	}
	
	public void addToDebit(double aPrice){
		this.debit += aPrice;
	}
	
	public void addToTime( int aTime){
		this.time += aTime;
	}
	
	public LinkedList<Integer> getBuyList(){
		return buyList;
	}
	
	public LinkedList<Integer> geSellList(){
		return sellList;
	}
	
	
	public int getTime(){
		return time;
	}
	
	public double getCredit(){
		return credit;
	}
	
	public double getDebit(){
		return debit;
	}
	int time;
	double credit;
	double debit;
	LinkedList<Integer> buyList;
	LinkedList<Integer> sellList;
}
