package mmovie;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class admin {
    public static void upload_movie(Connection conn, Statement stmt){
    	if(account.ADMIN == false) System.out.println("잘못된 접근 : 관리자가 아닙니다");
		else {
			try {
				String input;
				Scanner sc = new Scanner(System.in);
				String sql = "select max(tconst) from movie";
				ResultSet rs = stmt.executeQuery(sql);
				rs.next();
				String maxconst = rs.getString(1);
				maxconst = maxconst.replace("t", "");
				int temp = Integer.parseInt(maxconst);
				temp++;
				maxconst = String.format("%08d", temp);
				maxconst = "t" + maxconst;
				
				sql = "insert into movie values('" + maxconst + "',";
				System.out.println("영상물 추가를 선택하셨습니다.");
				System.out.println("정보를 입력하세요. *이 붙은 정보는 필수로 입력해야하는 정보입니다. ");
				
				while (true) {
					System.out.println("영상 제목(30자 이하) : ");
					input = sc.next();
					if(input.length() > 30) System.out.println("30자리 이하로 입력하세요.");
		            else {
						sql = sql + "'" + input + "',";
						break;
		            }
				}
				while (true) {
					System.out.println("영상물 타입(20자 이하) : ");
					input = sc.next();
					if(input.length() > 20) System.out.println("20자리 이하로 입력하세요.");
		            else {
						sql = sql + "'" + input + "',";
						break;
		            }
				}
				while (true) {
					System.out.println("관람등급 (성인용:R입력 / 그 외:A입력) :");
					input = sc.next();
					if (input.equals("R")||input.equals("A")||input.equals("")) {
						sql = sql + "'" + input + "',";
						break;
					}
					else {
						System.out.println("R혹은 A를 입력해주세요");
					}
				}
				while (true) {
				System.out.println("개봉일 (yyyy-mm-dd) : ");
				input = sc.next();
					try{
				        SimpleDateFormat  dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
				        dateFormat.setLenient(false);
				        dateFormat.parse(input);
						sql = sql + "to_date('" + input + "','yyyy-mm-dd'),";
						break;			         
			        }catch (ParseException  e){
			        	System.out.println("잘못된 날짜 양식입니다.");
			        	continue;
				    }
				}
				
				while(true) {
					System.out.println("상영시간 (분단위): ");
					try {
						int _input = sc.nextInt();
						input = Integer.toString(_input);
						sql = sql + input + ",";
						break;
					}catch(InputMismatchException e){
						System.out.println("유효하지 않은 숫자 입니다.");
						sc.next();
						continue;
					}
				}
				
				while(true) {
					System.out.println("*장르 (Action/Comedy/Romance/Horror/Drama) :");
					input = sc.next();
					switch(input) {
						case "Action":
							sql = sql + "'g00000001')";
							break;
						case "Comedy":
							sql = sql + "'g00000002')";
							break;
						case "Romance":
							sql = sql + "'g00000003')";
							break;
						case "Horror":
							sql = sql + "'g00000004')";
							break;
						case "Drama":
							sql = sql + "'g00000005')";
							break;
						default:
							System.out.println("유효하지 않은 장르입니다. 다음장르중에서 기입해주세요. Action/Comedy/Romance/Horror/Drama, not null");
							continue;
					}
					break;
				}
				stmt.executeUpdate(sql);
				conn.commit();
				System.out.println("영상물 추가 완료");
			}catch(SQLException ex) {
				System.err.println("영상물 추가 오류 : " + ex.getMessage());
				System.exit(1);
			}
		
		}
	}
}
	
