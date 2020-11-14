package phase3;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public static void search_movie(Connection conn, Statement stmt){
	String sql = "";
	ResultSet rs = null; 
	int button = -1;
	int option_cnt = 0;
	
	System.out.print(" 1.영상물 전체보기\n 2.검색하기 \n번호를 입력하세요 : ");
	Scanner scan = new Scanner(System.in); 
	button = scan.nextInt();
	
	
		if (button == 1) {
			try {
			stmt = conn.createStatement();
			sql = "SELECT tconst, title FROM MOVIE";
			rs = stmt.executeQuery(sql);

			while(rs.next())
            {
                System.out.println
                (
                    rs.getString(1) + "\t" +
                    rs.getString(2) 
                );
            }
			
//			rs.close();
//			stmt.close();
//			conn.close();
			} catch (SQLException se) {
	            se.printStackTrace();
	        } 
		}
		
		if (button == 2) {
			button = -1;
			System.out.print(" 1.제목으로 검색하기\n 2.조건으로 검색하기 \n 0.처음으로 \n번호를 입력하세요 : ");
			Scanner scan_a = new Scanner(System.in); 
			button = scan_a.nextInt();
			
			if (button == 0)//처음으로 (함수 재호출)
			{
				search_rate(conn, stmt);
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
					
//					rs.close();
//					stmt.close();
//					conn.close();
					} catch (SQLException se) {
			            se.printStackTrace();
			        } 
			}
			
			if (button == 2) // 조건으로 검색하기(조건 입력받기)
			{
				sql = "SELECT DISTINCT tconst, title FROM MOVIE, GENRE, VERSION";
				String sqlv = "SELECT DISTINCT MOVIE.tconst, VERSION.original_title FROM MOVIE, GENRE, VERSION";
				
				StringBuffer sb = new StringBuffer();
				
				
				//sql = sb.toString();
				
				
				String s = "";
				Scanner scan_option = new Scanner(System.in); 
				
				
				System.out.print(" 검색할 조건을 입력받습니다. \n ");
				
				
				System.out.print("영상물 버전 (UA, KR, JP, CN, RU, SP, IN, AU )\n 상관없는 경우 0을 입력하세요.: ");
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
					sb.append(" VERSION.nation = '"+s+"'");
					option_cnt++; //option_cnt는 덧붙이기 한 후에 증가시켜야한다.
				}
				
				
				System.out.print("영상물 타입 (movie, tv_series, knuMovieDB_Original)\n 상관없는 경우 0을 입력하세요.: ");
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
				
				System.out.print("영상물 장르 (Action, Comedy, Romance, Horror, Drama)\n 상관없는 경우 0을 입력하세요.: ");
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
				
				
			
				
				System.out.print("영상물 Runtime (ex)110 180입력시 110분~180분 사이의 runtime을 가진 영상물 선택\n 상관없는 경우 0 0을 입력하세요.: ");
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
					
//					rs.close();
//					stmt.close();
//					conn.close();
					} catch (SQLException se) {
			            se.printStackTrace();
			        } 
			}
		
		}
	
}