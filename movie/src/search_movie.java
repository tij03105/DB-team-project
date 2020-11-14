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
	
	System.out.print(" 1.���� ��ü����\n 2.�˻��ϱ� \n��ȣ�� �Է��ϼ��� : ");
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
			System.out.print(" 1.�������� �˻��ϱ�\n 2.�������� �˻��ϱ� \n 0.ó������ \n��ȣ�� �Է��ϼ��� : ");
			Scanner scan_a = new Scanner(System.in); 
			button = scan_a.nextInt();
			
			if (button == 0)//ó������ (�Լ� ��ȣ��)
			{
				search_rate(conn, stmt);
			}
			
			if (button == 1) //�������� �˻��ϱ�(���� �Է¹ޱ�)
			{
				String s = "";
				System.out.print(" �˻��� ������ �Է��ϼ��� : ");
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
			
			if (button == 2) // �������� �˻��ϱ�(���� �Է¹ޱ�)
			{
				sql = "SELECT DISTINCT tconst, title FROM MOVIE, GENRE, VERSION";
				String sqlv = "SELECT DISTINCT MOVIE.tconst, VERSION.original_title FROM MOVIE, GENRE, VERSION";
				
				StringBuffer sb = new StringBuffer();
				
				
				//sql = sb.toString();
				
				
				String s = "";
				Scanner scan_option = new Scanner(System.in); 
				
				
				System.out.print(" �˻��� ������ �Է¹޽��ϴ�. \n ");
				
				
				System.out.print("���� ���� (UA, KR, JP, CN, RU, SP, IN, AU )\n ������� ��� 0�� �Է��ϼ���.: ");
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
					option_cnt++; //option_cnt�� �����̱� �� �Ŀ� �������Ѿ��Ѵ�.
				}
				
				
				System.out.print("���� Ÿ�� (movie, tv_series, knuMovieDB_Original)\n ������� ��� 0�� �Է��ϼ���.: ");
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
				
				System.out.print("���� �帣 (Action, Comedy, Romance, Horror, Drama)\n ������� ��� 0�� �Է��ϼ���.: ");
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
					option_cnt++; //option_cnt�� �����̱� �� �Ŀ� �������Ѿ��Ѵ�.
				}
				
				
			
				
				System.out.print("���� Runtime (ex)110 180�Է½� 110��~180�� ������ runtime�� ���� ���� ����\n ������� ��� 0 0�� �Է��ϼ���.: ");
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
					option_cnt++; //option_cnt�� �����̱� �� �Ŀ� �������Ѿ��Ѵ�.
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