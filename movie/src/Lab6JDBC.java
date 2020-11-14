

package phase3;
/**************************************************
 * Copyright (c) 2020 KNU DEAL Lab. To Present
 * All rights reserved. 
 **************************************************/


import java.sql.*; // import JDBC package
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sample Code for JDBC Practice
 * 
 * @author yksuh
 */
public class Test {
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_UNIVERSITY = "movie";
	public static final String USER_PASSWD = "movie";

	@SuppressWarnings("null")
	public static void main(String[] args) {
		Connection conn = null; // Connection object
		Statement stmt = null; // Statement object
	
		// Driver
		try {
			// Load a JDBC driver for Oracle DBMS
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Get a Connection object
			System.out.println("Success!");
		} catch (ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}

		//  Make a connection
		try {
			conn = DriverManager.getConnection(URL, USER_UNIVERSITY, USER_PASSWD);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Cannot get a connection: " + ex.getMessage());
			System.exit(1);
		}

		
		search_movie(conn, stmt);

		// Release database resources.
		try {
			// Close the Statement object.
			//stmt.close();
			// Close the Connection object.
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	


	public static void search_movie(Connection conn, Statement stmt){
		String sql = "";
		ResultSet rs = null; 
		int button = -1;
		int option_cnt = 0;
		
		System.out.print("\n 1.영상물 전체보기\n 2.검색하기(평가된 영상물은 검색결과에 나타나지 않습니다.) \n번호를 입력하세요 : ");
		Scanner scan = new Scanner(System.in); 
		button = scan.nextInt();
		
		
			if (button == 1) {
				try {
				stmt = conn.createStatement();
				sql = "SELECT tconst, title FROM MOVIE ORDER BY tconst";
				rs = stmt.executeQuery(sql);

				while(rs.next())
	            {
	                System.out.println
	                (
	                    rs.getString(1) + "\t" +
	                    rs.getString(2) 
	                );
	            }
				
//				rs.close();
//				stmt.close();
//				conn.close();
				} catch (SQLException se) {
		            se.printStackTrace();
		        } 
				
				movie_info(conn,stmt);
				
			}
			
			if (button == 2) {
				button = -1;
				System.out.print("\n 1.제목으로 검색하기\n 2.조건으로 검색하기 \n 0.처음으로 \n번호를 입력하세요 : ");
				Scanner scan_a = new Scanner(System.in); 
				button = scan_a.nextInt();
				
				if (button == 0)//처음으로 (함수 재호출)
				{
					search_movie(conn, stmt);
				}
				
				if (button == 1) //제목으로 검색하기(제목 입력받기)
				{
					String s = "";
					System.out.print(" 검색할 제목을 입력하세요 : ");
					Scanner scan_title = new Scanner(System.in); 
					s = scan_title.nextLine();
					try {
						stmt = conn.createStatement();
						sql = "SELECT tconst, title FROM MOVIE WHERE title LIKE '%" + s + "%'";
						rs = stmt.executeQuery(sql);

						while(rs.next())
			            {
			                System.out.println
			                (
			                    rs.getString(1) + "\t" +
			                    rs.getString(2) 
			                );
			            }
						
//						rs.close();
//						stmt.close();
//						conn.close();
						} catch (SQLException se) {
				            se.printStackTrace();
				        }
					movie_info(conn,stmt);
				}
				
				if (button == 2) // 조건으로 검색하기(조건 입력받기)
				{
					sql = "SELECT DISTINCT tconst, title FROM MOVIE, GENRE, VERSION";
					String sqlv = "SELECT DISTINCT MOVIE.tconst, VERSION.original_title FROM MOVIE, GENRE, VERSION";
					
					StringBuffer sb = new StringBuffer();
				
					
					String s = "";
					Scanner scan_option = new Scanner(System.in); 
					
					
					System.out.print(" 검색할 조건을 입력받습니다. \n ");
					
					
					System.out.print("하나의 영상물 버전을 선택하세요 (UA, KR, JP, CN, RU, SP, IN, AU )\n 상관없는 경우 0을 입력하세요.: ");
					s = scan_option.nextLine();
					if( s.equals("0")){
						sb.append(sql);
					}
					else{
						sb.append(sqlv);
						if(option_cnt > 0){
							sb.append(" and ");
						}
						else{
							sb.append(" WHERE ");
						}
						sb.append(" VERSION.nation = '"+s+"' and VERSION.Tcon = MOVIE.Tconst");
						option_cnt++; //option_cnt는 덧붙이기 한 후에 증가시켜야한다.
					}
					
					
					System.out.print("\n하나의 영상물 타입을 선택하세요 (movie, tv_series, knuMovieDB_Original)\n 상관없는 경우 0을 입력하세요.: ");
					s = scan_option.nextLine();
					if( s.equals("0")){
						
					}
					else{
						if(option_cnt > 0){
							sb.append(" and ");
						}
						else{
							sb.append(" WHERE ");
						}
						sb.append(" title_type = '" + s + "'");
						option_cnt++;
					}
					
					System.out.print("\n하나의 영상물 장르를 선택하세요 (Action, Comedy, Romance, Horror, Drama)\n 상관없는 경우 0을 입력하세요.: ");
					s = scan_option.nextLine();
					if( s.equals("0")){
						
					}
					else{
						if(option_cnt > 0){
							sb.append(" and ");
						}
						else{
							sb.append(" WHERE ");
						}
						sb.append(" GENRE.genre = '"+s+"' and GENRE.genre_code = movie.gcode");
						option_cnt++; //option_cnt는 덧붙이기 한 후에 증가시켜야한다.
					}
					
					
				
					System.out.print("\n영상물 Runtime을 입력하세요 (ex)110 180입력시 110분~180분 사이의 runtime을 가진 영상물 선택\n 상관없는 경우 0 0을 입력하세요.: ");
					int a = scan_option.nextInt();
					int b = scan_option.nextInt();
					if( a == 0 && b == 0){
						
					}
					else{
						if(option_cnt > 0){
							sb.append(" and ");
						}
						else{
							sb.append(" WHERE ");
						}
						sb.append(" Runtime_minutes >= " + a + " and Runtime_minutes <= " + b );
						option_cnt++; //option_cnt는 덧붙이기 한 후에 증가시켜야한다.
					}
					
					sb.append(" ORDER BY tconst");
					
					sql = sb.toString();
					System.out.println(sql);
					
					
					try {
						stmt = conn.createStatement();
						rs = stmt.executeQuery(sql);

						while(rs.next())
			            {
			                System.out.println
			                (
			                    rs.getString(1) + "\t" +
			                    rs.getString(2) 
			                );
			            }
						
//						rs.close();
//						stmt.close();
//						conn.close();
						} catch (SQLException se) {
				            se.printStackTrace();
				        } 
					
					movie_info(conn,stmt);
					
				}
			
			}
	}

	
	public static void movie_info(Connection conn, Statement stmt){
		String sql = "";
		ResultSet rs = null; 
		String tconst = "";
		
		System.out.print("\n정보 확인 및 평가할 영상물의 식별번호를 입력하세요 (ex t00000218) : ");
		Scanner scan = new Scanner(System.in); 
		tconst = scan.nextLine();
		//제목, 종류, 재생시간, 상영 년도, 장르 , 버전, 평가 유무
		sql = "SELECT tconst, title, title_type, Start_Year, Runtime_Minutes, genre FROM MOVIE, GENRE WHERE MOVIE.tconst = '"+tconst+"' and GENRE.GENRE_CODE = MOVIE.GCODE";
		System.out.println(sql);
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
            {
                System.out.println
                (
                    "식별번호 : " + rs.getString(1) + "\n" +
                    "영상물 제목 : " + rs.getString(2) + "\n" +
                    "영상물 타입 : " + rs.getString(3) + "\n" +
                    "영상물 상영연도 및 날짜 : " + rs.getString(4) + "\n" +
                    "상영시간(분) : " + rs.getString(5) + "\n" +
                    "영상물 장르 : " + rs.getString(6)
                );
            }
			
//			rs.close();
//			stmt.close();
//			conn.close();
			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		sql = "SELECT DISTINCT  nation  FROM MOVIE, VERSION WHERE VERSION.Tcon = '"+tconst+"' and MOVIE.Tconst = VERSION.Tcon";
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			System.out.print("영상물 버젼 종류 : ");
			
			while(rs.next())
            {
                System.out.print
                (
                   rs.getString(1) + " "
                );
            }
			
//			rs.close();
//			stmt.close();
//			conn.close();
			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		sql = "SELECT DISTINCT Average_Rating FROM MOVIE, RATING WHERE RATING.Tcon = '"+tconst+"' and MOVIE.Tconst = RATING.Tcon";
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			System.out.print("\n영상물 평균 평점 : ");
			
			while(rs.next())
            {
                System.out.print
                (
                   rs.getString(1) + " "
                );
            }
			
//			rs.close();
//			stmt.close();
//			conn.close();
			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		
		
		System.out.print("\n\n 1.영상물 메뉴로 돌아가기  2.평가하기 ");
		int select = scan.nextInt();
		if( select ==1 ){
			search_movie(conn, stmt);
		}
		if( select ==2 ){
			movie_rate(conn, stmt, tconst);
		}
		
		
	}
	
	
	public static void movie_rate(Connection conn, Statement stmt, String tconst){
		String sql = "";
		ResultSet rs = null; 
		Scanner scan = new Scanner(System.in); 
		
		System.out.print("\n당신의 ID를 입력하세요 : "); // 수정해야함 로그인 한 ID 받아올 수 있음. 입력 받을필요없음
		String ac_ID = scan.nextLine();
		
		System.out.print("\n영상물에 대한 평점을 입력해주세요 (0.0~10.0) : ");
		double rating = scan.nextDouble();
		
		String maxconst = "";
		
		//insert할 테이블 RATING PROVIDES
		//insert into RATING values (	't00000141',	'r00000141',	9Average_Rating	);
		//INSERT INTO PROVIDES VALUES (	4	,'r00000008'R_ID	,'Knouted'A_Id	);
		
		//다음순서의 r_ID값을 구하기 위해서, rating에 있는 r_ID값중에서 가장 큰 값에 +1을 한다. 
		//이제 tconst를 받았다.RATING에 tconst, 다음순서의 r_ID값, 0(average_rating)을넣고 insert한다.
		// provides에 사용자가 입력한 평점, R_ID(rating에서 쓴것 그대로), A_ID(사용자가 입력한 id), 를 insert한다.
		//tconst가 가진 평점들의 값을 더한 뒤에 평점의 갯수만큼 나누어서 평균 평점을 구한다. 그냥 avg로 바로 구할 수 있다. 
		//이제 그 avg값을 tconst의 rating의 Average_Rating에 update한다. 

		sql = "select max(R_ID) from RATING";
		try {
			rs = stmt.executeQuery(sql);
			rs.next();
			maxconst = rs.getString(1);
			maxconst = maxconst.replace("r", "");
			System.out.println(maxconst);
			int temp = Integer.parseInt(maxconst);
			temp++;
			maxconst = String.format("%08d", temp);
			maxconst = "r" + maxconst;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		sql = "insert into RATING values('" + tconst + "', '" + maxconst + "', 0 )";
		System.out.println(sql);
		
	
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

//			rs.close();
//			stmt.close();
//			conn.close();
			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		System.out.print("rating insert 완료 !! ");
		
		// provides에 사용자가 입력한 평점, R_ID(rating에서 쓴것 그대로), A_ID(사용자가 입력한 id), 를 insert한다.
				//tconst가 가진 평점들의 값을 provides를 통해 더한 뒤에 평점의 갯수만큼 나누어서 평균 평점을 구한다. 그냥 avg로 바로 구할 수 있다. 
				//이제 그 avg값을 tconst의 rating의 Average_Rating에 update한다. 
		
		sql = "insert into PROVIDES values( " + rating + " ,  '" + maxconst + "', '" + ac_ID + "')"; //여기서 ac_ID만 사용자 이름이면 된다. account table에 없는 이름을 넣으면 참조무결성제약 위반.
		
		System.out.println(sql);
		
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			System.out.print("\n provides insert 진입 @@ ");

			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		System.out.print("provides insert 완료 !! ");
		
		sql = "select AVG(Rating) FROM RATING, PROVIDES WHERE RATING.Tcon = '" + tconst + "' and RATING.R_ID = PROVIDES.R_ID";
		
		Double mean = 0.0;
		System.out.println(sql);
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
            {
               
                  mean = rs.getDouble(1);
                
            }
			
			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		//이제 그 avg값을 tconst의 rating의 Average_Rating에 update한다. 
		//일단 
		
		sql = "update  RATING SET Average_Rating = " + mean + " WHERE  Tcon = '" + tconst  + "'"; 
		System.out.println(sql);
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		
		System.out.println("평가 입력이 완료되었습니다.\n");
		search_movie(conn, stmt);
		//////////////////////////close 해주기.
//		rs.close();
//		stmt.close();
//		conn.close();
	}
	
}

	
