package Trading_Engine;

import java.io.*;
import java.sql.*;

public class myDatabase {
	private static Connection connection;
	
	public myDatabase(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			System.out.println("Could not find JDBCdriver : " + e);
		}
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.out.println("Could not establish connection : " + e);
		}
		deleteAllTables();
	}
	public static void deleteAllTables(){
		try {
			Statement statement = connection.createStatement();
			DatabaseMetaData dbm = connection.getMetaData();
			String[] types = {"TABLE"};
			ResultSet allTables = dbm.getTables(null,null,"%",types);
			if(allTables!=null){
				while(allTables.next()){
					String table = allTables.getString("TABLE_NAME");
					statement.addBatch("drop table " + table);
					System.out.println("delete table: " + table);
				};
				statement.executeBatch();
			}
			allTables.close();
			statement.close();
		}catch(Exception e) {
			System.out.println("in deleteAllTables: " + e);
		}
	}
	public static void insertQuery(PreparedStatement pstmt, String[] insertElement){
		try {
			int lengthOfArray = insertElement.length;
			//System.out.println(st);
			pstmt.setString(1, insertElement[0]);

			String tempDate = insertElement[1].substring(0, 4) + "-" 
					+ insertElement[1].substring(4, 6) + "-" + insertElement[1].substring(6, 8);
			pstmt.setDate(2, Date.valueOf(tempDate));
			pstmt.setTime(3, Time.valueOf(insertElement[2].substring(0, 8)));
			pstmt.setInt(4, Integer.parseInt(insertElement[2].substring(9)));
			pstmt.setString(5, insertElement[3]);
			if(insertElement[4].isEmpty()){
				pstmt.setNull(6, java.sql.Types.FLOAT);
			}else {
				pstmt.setFloat(6, Float.parseFloat(insertElement[4]));
			}
			if(insertElement[5].isEmpty()){
				pstmt.setNull(7, java.sql.Types.INTEGER);
			}else {
				pstmt.setInt(7, Integer.parseInt(insertElement[5]));
			}
			if(insertElement[6].isEmpty()){
				pstmt.setNull(8, java.sql.Types.INTEGER);
			}else {
				pstmt.setInt(8, Integer.parseInt(insertElement[6]));
			}
			if(insertElement[7].isEmpty()){
				pstmt.setNull(9, java.sql.Types.FLOAT);
			}else {
				pstmt.setFloat(9, Float.parseFloat(insertElement[7]));
			}
			if(insertElement[8].isEmpty()){
				pstmt.setNull(10, java.sql.Types.VARCHAR);
			}else {
				pstmt.setString(10, insertElement[8]);
			}
			if(lengthOfArray > 9){
				if(insertElement[9].isEmpty()){
					pstmt.setNull(11, java.sql.Types.INTEGER);
				}else{
					pstmt.setInt(11, Integer.parseInt(insertElement[9]));
				}
			}else{
				pstmt.setNull(11, java.sql.Types.INTEGER);
			}
			if(lengthOfArray > 10){
				if(insertElement[10].isEmpty()){
					pstmt.setNull(12, java.sql.Types.BIGINT);
				}else {
					pstmt.setLong(12, Long.parseLong(insertElement[10]));
				}
			}else {
				pstmt.setNull(12, java.sql.Types.BIGINT);
			}
			if(lengthOfArray > 11){
				if(insertElement[11].isEmpty()){
					pstmt.setNull(13, java.sql.Types.BIGINT);
				}else {
					pstmt.setLong(13, Long.parseLong(insertElement[11]));
				}
			}else {
				pstmt.setNull(13, java.sql.Types.BIGINT);
			}

			if(lengthOfArray > 12){
				if(insertElement[12].isEmpty()){
					pstmt.setNull(14, java.sql.Types.VARCHAR);
				}else {
					pstmt.setString(14, insertElement[12]);
				}
			}else {
				pstmt.setNull(14, java.sql.Types.VARCHAR);
			}
			if(lengthOfArray > 13){
				if(insertElement[13].isEmpty()){
					pstmt.setNull(15, java.sql.Types.TIME);
				}else {
					pstmt.setTime(15, Time.valueOf(insertElement[13]));
				}
			}else {
				pstmt.setNull(15, java.sql.Types.TIME);
			}
			if(lengthOfArray > 14){
				if(insertElement[14].isEmpty()){
					pstmt.setNull(16, java.sql.Types.FLOAT);
				}else {
					pstmt.setFloat(16, Float.parseFloat(insertElement[14]));
				}
			}else {
				pstmt.setNull(16, java.sql.Types.FLOAT);
			}
			if(lengthOfArray > 15){
				if(insertElement[15].isEmpty()){
					pstmt.setNull(17, java.sql.Types.INTEGER);
				}else {
					pstmt.setInt(17, Integer.parseInt(insertElement[15]));
				}
			}else {
				pstmt.setNull(17, java.sql.Types.INTEGER);
			}
			if(lengthOfArray > 16){
				if(insertElement[16].isEmpty()){
					pstmt.setNull(18, java.sql.Types.INTEGER);
				}else {
					pstmt.setInt(18, Integer.parseInt(insertElement[16]));
				}
			}else {
				pstmt.setNull(18, java.sql.Types.INTEGER);
			}
			if(lengthOfArray > 17){
				if(insertElement[17].isEmpty()){
					pstmt.setNull(19, java.sql.Types.INTEGER);
				}else {
					pstmt.setInt(19, Integer.parseInt(insertElement[17]));
				}
			}else {
				pstmt.setNull(19, java.sql.Types.INTEGER);
			}
			pstmt.addBatch();
		}catch(Exception e) {
			System.out.println("In insertQuery:  " + e);
		}
	}
	public static void insertDatabase (Connection con, BufferedReader br, String newHead){
		try {
			String st = "";
			String preInsertQuery = "insert into orderbook(" + newHead + ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String preInsertBid = "insert into bid_list(" + newHead + ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String preInsertAsk = "insert into ask_list(" + newHead + ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String preInsertTrade = "insert into trade_list(" + newHead + ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement orderBookQuery = con.prepareStatement(preInsertQuery);
			PreparedStatement BidQuery = con.prepareStatement(preInsertBid);
			PreparedStatement AskQuery = con.prepareStatement(preInsertAsk);
			PreparedStatement TradeQuery = con.prepareStatement(preInsertTrade);
			con.setAutoCommit(false);
			while ((st=br.readLine())!=null){
				String[] insertElement = st.split(",");
				if(insertElement[3].equalsIgnoreCase("ENTER") ){
					if(insertElement[12].equalsIgnoreCase("A")){
						insertQuery(AskQuery,insertElement);
					}else{
						insertQuery(BidQuery,insertElement);
					}
				}else if(insertElement[3].equalsIgnoreCase("AMEND") ){
					insertQuery(orderBookQuery,insertElement);
				}else if(insertElement[3].equalsIgnoreCase("DELETE") ){
					insertQuery(orderBookQuery,insertElement);
				}else if(insertElement[3].equalsIgnoreCase("TRADE") ){
					insertQuery(TradeQuery,insertElement);
				}else{
					insertQuery(orderBookQuery,insertElement);
				}
			}
			int[] i1 = orderBookQuery.executeBatch();
			int[] i2 = BidQuery.executeBatch();
			int[] i3 = AskQuery.executeBatch();
			int[] i4 = TradeQuery.executeBatch();
			
			System.out.println("i1 length: " + i1.length + " " + i1[0] + " " + i1[1]);
			System.out.println("i2 length: " + i2.length + " " + i2[0] + " " + i2[1]);
			System.out.println("i3 length: " + i3.length + " " + i3[0] + " " + i3[1]);
			System.out.println("i4 length: " + i4.length + " " + i4[0] + " " + i4[1]);
			
			orderBookQuery.close();
			BidQuery.close();
			AskQuery.close();
			TradeQuery.close();
		}catch(Exception e){
			System.out.println("In insertDatabase: " + e);
		}
	}
	public void insertFromFile(File f) {
		try {
			//JFileChooser chooser = new JFileChooser();
			//int returnVal = chooser.showOpenDialog(null);
			//if(returnVal == JFileChooser.APPROVE_OPTION){
			//File f = chooser.getSelectedFile();
			Statement s = connection.createStatement();
			BufferedReader br = new BufferedReader(new FileReader(f));
			String head = "";
			if((head=br.readLine())!=null){
				System.out.println("first character of head: " + head.charAt(0));
				//System.out.println(head);
				if(head.charAt(0)== '#'){
					String[] tableElement = head.split(",");
					tableElement[3] = "Record_Type";
					tableElement[6] = "Undisclosed_Volume";
					tableElement[9] = "Trans_ID";
					tableElement[10] = "Bid_ID";
					tableElement[11] = "Ask_ID";
					tableElement[12] = "Bid_Ask";
					tableElement[13] = "Entry_Time";
					tableElement[14] = "Old_Price";
					tableElement[15] = "Old_Volume";
					tableElement[16] = "Buyer_Broker_ID";
					tableElement[17] = "Seller_Broker_ID";
					String tableQuery = tableElement[0].substring(1) + " varchar(3), " +
							tableElement[1] + " date, " +
							tableElement[2] + " time, " +
							"millisecond" + " integer, "+
							tableElement[3] + " text, " +
							tableElement[4] + " float, " +
							tableElement[5] + " integer, " +
							tableElement[6] + " integer, " +
							tableElement[7] + " float, " +
							tableElement[8] + " text, " +
							tableElement[9] + " integer, " +
							tableElement[10] + " bigint, " +
							tableElement[11] + " bigint, " +
							tableElement[12] + " varchar(1), " +
							tableElement[13] + " time, " +
							tableElement[14] + " float, " +
							tableElement[15] + " integer, " +
							tableElement[16] + " integer, " +
							tableElement[17] + " integer ";
					String newHead = tableElement[0].substring(1);
					for(int i = 1; i < tableElement.length; i++){
						newHead += " , " + tableElement[i];
						if(i == 2){
							newHead += " , " + "millisecond";
						}
					}
					System.out.println(tableQuery);
					s.execute("create table orderbook(" + tableQuery + ")");
					s.execute("create table bid_list(" + tableQuery + ")");
					s.execute("create table ask_list(" + tableQuery + ")");
					s.execute("create table trade_list(" + tableQuery + ")");
					insertDatabase(connection,br,newHead);
				} else {
					System.out.println("Error: file content not csv format");
				}
			}else {
				System.out.println("Error: file is empty");
			}
			br.close();
			s.close();
		}catch (Exception e) {
			System.out.println("In insertFromFile:  " + e);
		}
	}
	public static String getRowCount1(Connection connection) {
		String reply = null;
		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM orderbook;");
			if(rs!=null){
				if(rs.next()){
					reply ="orderbook contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs = statement.executeQuery("SELECT count(*) FROM bid_list;");
			if(rs!=null){
				if(rs.next()){
					reply = reply + "bid_list contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs = statement.executeQuery("SELECT count(*) FROM ask_list;");
			if(rs!=null){
				if(rs.next()){
					reply = reply + "ask_list contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs = statement.executeQuery("SELECT count(*) FROM trade_list;");
			if(rs!=null){
				if(rs.next()){
					reply = reply + "trade_list contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs.close();
			statement.close();
		}catch(Exception e){
			System.out.println("In getRowCount:  " + e);
		}
		return reply;
	}
	public String getRowCount() {
		String reply = null;
		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM orderbook;");
			if(rs!=null){
				if(rs.next()){
					reply ="orderbook contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs = statement.executeQuery("SELECT count(*) FROM bid_list;");
			if(rs!=null){
				if(rs.next()){
					reply = reply + "bid_list contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs = statement.executeQuery("SELECT count(*) FROM ask_list;");
			if(rs!=null){
				if(rs.next()){
					reply = reply + "ask_list contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs = statement.executeQuery("SELECT count(*) FROM trade_list;");
			if(rs!=null){
				if(rs.next()){
					reply = reply + "trade_list contains " + rs.getString(1) + " lines.\n";
				}
			}
			rs.close();
			statement.close();
		}catch(Exception e){
			System.out.println("In getRowCount:  " + e);
		}
		return reply;
	}


	public myTrade getTrade(String query) {
		ResultSet rs = null;
		myTrade trade = new myTrade();
		System.out.println("trade length: " + trade.getLength());
		try{
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(query);
			Double tmp;
			while (rs.next()){
				tmp = rs.getDouble(6);
				System.out.println("tmp: " + tmp);
				trade.addPice(tmp);
			};
			rs.close();
			statement.close();
		}catch(Exception e){
			System.out.println("In getTrade:  " + e);
		}
		return trade;
	}
	public void closeDatabase(){
		deleteAllTables();
		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("Error closing database : " + e);
		}
	}
}
