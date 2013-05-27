package Trading_Engine;

import java.sql.Time;
import java.util.LinkedList;

public class MyAskList {
	private LinkedList<OneData> myList;
	private LinkedList<Long> allID;

	public MyAskList(){
		myList = new LinkedList<OneData>();
		allID = new LinkedList<Long>();
	}

	public void add(long tmpID, double tmpPrice, int tmpVol, Time tmpTime) {
		OneData tmpData = new OneData(tmpID,tmpPrice,tmpVol,tmpTime);
		int len = getLength();
		int indexInsert = 0;
		if(len > 1){
			indexInsert = getPosToInsert(tmpPrice,0,len-1);
		}else if(len == 1){
			if(tmpPrice >= myList.get(0).getPrice()){
				indexInsert = 1;
			}
		}
		myList.add(indexInsert,tmpData);
		allID.add(indexInsert, tmpID);
	}

	private int getPosToInsert(double tmpPrice, int bot, int top) {
		int result = 0;
		if(bot+1 == top){
			if(tmpPrice >= myList.get(bot).getPrice() & tmpPrice < myList.get(top).getPrice()){
				result = top;
			}else if(tmpPrice < myList.get(bot).getPrice()){
				result = bot;
			}else if(tmpPrice >= myList.get(top).getPrice()){
				result = top+1;
			}
		}else if(tmpPrice < myList.get(((top-bot)/2) + bot).getPrice()){
			result = getPosToInsert(tmpPrice,bot,((top-bot)/2) + bot);
		}else if(tmpPrice >= myList.get(((top-bot)/2) + bot).getPrice()){
			//System.out.println("getPosToInsert(" + tmpPrice + ", " +(((top-bot)/2) + bot) + ", " + top + ")");
			result = getPosToInsert(tmpPrice,((top-bot)/2) + bot,top);
		}

		return result;
	}

	public int getLength() {
		return myList.size();
	}

	public OneData get(int i) {
		return myList.get(i);
	}

	public void printAll() {
		for(int i = 0; i < getLength();i++){
			System.out.println(i + " : " + myList.get(i).getPrice() + " - " + myList.get(i).getTime());
		}
	}
	
	public void update(long tmpID, double tmpPrice, int tmpVol, Time tmpTime) {
		int updateIndex = allID.indexOf(tmpID);
		if(updateIndex != -1){
			myList.get(updateIndex).updateValue(tmpID,tmpPrice,tmpVol,tmpTime);
		}
	}

	public void deleteOne(long tmpID) {
		int deleteIndex = allID.indexOf(tmpID);
		if(deleteIndex != -1){
			myList.remove(deleteIndex);
			allID.remove(deleteIndex);
		}
	}

	public void deleteAtIndex(int i) {
		myList.remove(i);
		allID.remove(i);
	}

	public void updateFirst(long tmpID, double tmpPrice, int tmpVol, Time tmpTime) {
		myList.get(0).updateValue(tmpID,tmpPrice,tmpVol,tmpTime);
	}
}