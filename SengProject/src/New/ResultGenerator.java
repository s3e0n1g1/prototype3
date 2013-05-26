package New;

import java.util.LinkedList;
public class ResultGenerator {

	
	/**
	 * It just convert the result which we have in more readable thing from 
	 * 
	 * @param evaluator
	 */
	public ResultGenerator( EvaluatorLec strategy){
		CRList = strategy.getCR();		
	}
	
	public LinkedList<resultObjectL> getResultList(){
		
		LinkedList<resultObjectL> results = new LinkedList<resultObjectL>();
		
		for ( CoupleReciept CR : CRList ){
			int numberOFTrade = CR.geSellList().size() + CR.getBuyList().size();
			double moneyGet = CR.getCredit();
			double moneyPaid = CR.getDebit();
			results.add( new resultObjectL ( CR.getTime()/numberOFTrade , ((moneyGet - moneyPaid)/ moneyPaid) ));
		}
		
		return results;
	}
	
	
	
	private LinkedList<CoupleReciept> CRList;
}
