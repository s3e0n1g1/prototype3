package Trading_Engine;

import gui.Mainmenu;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JFileChooser;

import Selecting_Algothrim.Strategy;
import Selecting_Algothrim.Trade;
public class myDatabase {
	public static Connection connection;
	public static Statement statement;
	public static void main(String[]args){
		String dataSourceName = "testSqlDb";
		String dbUrl = "jdbc:odbc:" + dataSourceName;
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			statement = connection.createStatement();
			deleteAllTables(connection,statement);
			Mainmenu newApplication = new Mainmenu();

		} catch(Exception e) {
			System.out.println(e);
		}

		
		/* try{
			//this is what kind of driver we use
			Class.forName("org.sqlite.JDBC");

			//Connection variable or object param: dbPath, userName, password
			Connection con = DriverManager.getConnection("jdbc:sqlite:test.db");

			Statement s = con.createStatement();
			Scanner scanner = new Scanner(System.in);
			deleteAllTables(con,s);
			System.out.println("###################");
			System.out.println("Prototype 1\n Loading csv file:...");
			insertFromFile(con,s);
			System.out.println("Select a strategy:\n 1) Momentum Strategy\n");
			boolean scanexit = true;
			while (scanexit){
				String strategy = scanner.nextLine();
				String[] a = new String[20];
				
				if (strategy.equals("1")) {
					strategy = "Momemtum";
					scanexit = false;
					ResultSet rs = s.executeQuery("SELECT * FROM trade_list ORDER BY Entry_Time DESC limit 20;");
					
					if(rs!=null){
						int i = 0;
						while (rs.next()){
							a[i] = rs.getString(6);
							//System.out.print(a[i]);
							i++;		
						};
						
						Strategy momentum = new Strategy();
						
						Trade fst = new Trade(null, null, null, Double.parseDouble(a[0]), 0, 0, 0, 0, 0, 0, 0, null, 0, null, null, null);
						Trade snd = new Trade(null, null, null, Double.parseDouble(a[1]), 0, 0, 0, 0, 0, 0, 0, null, 0, null, null, null);
						i = 1;			
						while (i < 19){
							momentum.getReturn(fst, snd);
							fst = snd;
							i = i + 1;
							snd = new Trade(null, null, null,Double.parseDouble(a[i]), 0, 0, 0, 0, 0, 0, 0, null, 0, null, null, null);
							
						}
						System.out.println ("Average return of " + Double.toString(momentum.getAverage()));
						String signal = "Buy";
						
						if (momentum.getSignal() == 2)
							signal = "Sell";
						System.out.println("Evaluating strategy based on " +strategy+ ": "+ signal + " Signal");
					} else {
						System.out.println("rs null");
					}
					
				} else {
					System.out.println("Not a valid strategy, please select another");
				}
			}

			//deleteAllTables(con,s);
			s.close();
			con.close();

		}catch(Exception e) {
			System.out.println("Kamalian " + e);
		}
		*/
		
	}

	public static void deleteAllTables(Connection con, Statement s){
		try {
			DatabaseMetaData dbm = con.getMetaData();
			String[] types = {"TABLE"};
			ResultSet allTables = dbm.getTables(null,null,"%",types);
			if(allTables!=null){
				while(allTables.next()){
					String table = allTables.getString("TABLE_NAME");
					s.addBatch("drop table " + table);
					System.out.println("delete table: " + table);
				};
				s.executeBatch();
			}
			allTables.close();
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
			orderBookQuery.executeBatch();
			BidQuery.executeBatch();
			AskQuery.executeBatch();
			TradeQuery.executeBatch();

			orderBookQuery.close();
			BidQuery.close();
			AskQuery.close();
			TradeQuery.close();
		}catch(Exception e){
			System.out.println("In insertDatabase: " + e);
		}
	}

	public static void insertFromFile(Connection con, Statement s, File f) {
		try {
			//JFileChooser chooser = new JFileChooser();
			//int returnVal = chooser.showOpenDialog(null);
			//if(returnVal == JFileChooser.APPROVE_OPTION){
				//File f = chooser.getSelectedFile();
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
						insertDatabase(con,br,newHead);
					} else {
						System.out.println("Error: file content not csv format");
					}
				}else {
					System.out.println("Error: file is empty");
				}
				br.close();
			//}
		}catch (Exception e) {
			System.out.println("In insertFromFile:  " + e);
		}
	}

	public static String getRowCount(Connection connection,
			Statement statement) {
		String reply = null;
		try{
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
		}catch(Exception e){
			System.out.println("In getRowCount:  " + e);
		}
		return reply;
	}
}
