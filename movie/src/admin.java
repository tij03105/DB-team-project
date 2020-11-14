import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class admin {
    public static void signUp(Connection conn, Statement stmt){
    	if(account.ADMIN == false) System.out.println("잘못된 접근 : 관리자가 아닙니다");
		else {
			try {
				String r_id;
				String input;
				Scanner sc = new Scanner(System.in);
				/*
				sql = "select min(tconst) from movie";
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				String minconst = rs.getString(1);

				sql = "select max(tconst) from movie";
				rs = stmt.executeQuery(sql);
				rs.next();
				String maxconst = rs.getString(1);
				*/
				
				String sql = "select max(tconst) from movie";
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				String maxconst = rs.getString(1);
				maxconst = maxconst.replace("t", "");
				int temp = Integer.parseInt(maxconst);
				temp++;
				maxconst = String.format("%08d", temp);
				maxconst = "t" + maxconst;
				r_id = maxconst.replace("t","r");
				
				sql = "insert into movie values('" + maxconst + "',";
				
				System.out.println("삽입하고자 하는 영상물의 정보를 양식에 따라 입력해주세요: ");
				/*
				while (true) {
					System.out.println("Tconst, String[9] not null \n(range in use : " + minconst + " ~ " + maxconst + ") :");
					input = sc.next();
					String sql_t = "select tconst from movie where tconst = '" + input + "'"; // 중복확인
					rs = stmt.executeQuery(sql_t);
					if(rs.next()) {
						System.out.println("duplicated tconst");
						continue;
					}
					sql = sql + "'" + input + "',";
					r_id = input.replace("t","r");
					break;
				}
				*/
				
				System.out.print("Title, String[30] not null : ");
				input = sc.next();
				sql = sql + "'" + input + "',";

				System.out.print("Title_Type String[20] : ");
				input = sc.next();
				if (input.equals("null")) sql = sql + "null,";
				sql = sql + "'" + input + "',";
				
				while (true) {
					System.out.print("Is_Adult (adult : R / all : A) :");
					input = sc.next();
					if (input.equals("R")||input.equals("A")) {
						sql = sql + "'" + input + "',";
						break;
					}
					else if(input.equals("null")) {
						sql = sql + "null,";
						break;
					}
				}
				System.out.print("Start_Year DATE (1900-00-00) : ");
				input = sc.next();
				if (input.equals("null")) sql = sql + "null,";
				sql = sql + "to_date('" + input + "','yyyy-mm-dd'),";
									
				System.out.print("Runtime_Minutes int : ");
				input = sc.next();
				if (input.equals("null")) sql = sql + "null,";
				sql = sql + input + ",";
				
				
				while(true) {
					System.out.print("Genre (Action/Comedy/Romance/Horror/Drama, not null) :");
					input = sc.next();
					switch(input) {
						case "Action":
							sql = sql + "'g00000001',";
							break;
						case "Comedy":
							sql = sql + "'g00000002',";
							break;
						case "Romance":
							sql = sql + "'g00000003',";
							break;
						case "Horror":
							sql = sql + "'g00000004',";
							break;
						case "Drama":
							sql = sql + "'g00000005',";
							break;
						default:
							System.out.println("Wrong input. choose one Action/Comedy/Romance/Horror/Drama, not null");
							continue;
					}
					break;
				}
				
				rs = stmt.executeQuery(sql);
				//conn.commit();
			}catch(SQLException ex) {
				System.err.println("sql error = " + ex.getMessage());
				System.exit(1);
			}
		
		}
	}
}
	
