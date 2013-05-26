package New;


import java.util.LinkedList;
public class EvaluatorLec {

	
	public EvaluatorLec(lecMS lecMs, LinkedList<TradeObject> aTradeList){
		listOfCoupleReciept = lecMs.getAllReciept();
		this.tradeList = aTradeList;
	}
	
	
	public void run(){
		TradeObject tempTrade; 
		
		while ( !this.tradeList.isEmpty()){
			tempTrade = tradeList.poll();
			for ( CoupleReciept tempCR : listOfCoupleReciept){
				if ( tempCR.geSellList().contains(tempTrade.getSellID())){ // here we found that  sell belongs to us
					tempCR.addToCredit(tempTrade.getPrice());
					tempCR.addToTime(tempTrade.getTime());
				}
				else if ( tempCR.getBuyList().contains(tempTrade.getBuyID())){
					tempCR.addToDebit(tempTrade.getPrice());
					tempCR.addToDebit(tempTrade.getPrice());
					tempCR.addToTime(tempTrade.getTime());
				}
				//else it doesn't belong to us we don't care 
			}
		}
				
		
			
			
	}

	
	public LinkedList<CoupleReciept> getCR(){
		return listOfCoupleReciept;
		
	}
	
	private LinkedList<TradeObject> tradeList;
	private LinkedList<CoupleReciept> listOfCoupleReciept;
}
