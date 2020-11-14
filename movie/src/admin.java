import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class admin {
	private static Scanner sc = new Scanner(System.in);

    public static void upload_movie(Connection conn, Statement stmt){
    	if(account.ADMIN == false) System.out.println("잘못된 접근 : 관리자가 아닙니다");
		else {
			try {
				String r_id;
				String input;

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
					System.out.print("관람등급 (성인용:R입력 / 그 외:A입력) :");
					input = sc.next();
					if (input.equals("R")||input.equals("A")||input.equals("")) {
						sql = sql + "'" + input + "',";
						break;
					}
					else {
						System.out.print("R혹은 A를 입력해주세요");
					}
				}
				while (true) {
				System.out.print("개봉일 (yyyy-mm-dd) : ");
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
				
				System.out.print("상영시간 (분단위: ");
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

	public static void updateMovie(Connection conn, Statement stmt){
		System.out.println("영상 수정을 선택하셨습니다.");
		while(true){
			System.out.println("수정할 영상의 이름을 입력하세요.");
			String name = "";
			name = sc.nextLine();

			try {
				String sql = "SELECT tconst, start_year FROM MOVIE WHERE title = '" + name + "'";
				ResultSet rs = stmt.executeQuery(sql);
				ResultSetMetaData resultSetMetaData = rs.getMetaData();

				//System.out.println(sql);
				if(!rs.isBeforeFirst()) {
					System.out.println("일치하는 영상이 없습니다.");
					System.out.println("다른 영상을 찾으시겠습니까?(Y:yes, N:no)");
					String q;
					q = sc.nextLine();
					if (q.equals("y") || q.equals("Y") || q.equals("yes") || q.equals("YES")) continue;
					else if(q.equals("n") || q.equals("N") || q.equals("no") || q.equals("NO")) break;
					else {
						System.out.println("잘못된 값입니다. 이전메뉴로 돌아갑니다.");
						break;
					}
				}

				String tconst;
				Date date;
				System.out.println("입력한 영상의 이름과 일치하는 목록");
				int cnt = resultSetMetaData.getColumnCount();
				System.out.printf("    ");
				for(int i=1;i<=cnt;i++){
					System.out.printf(resultSetMetaData.getColumnName(i));
					if(i!=cnt) System.out.printf("			");
					else System.out.println("			" + " TITLE");
				}

				cnt = 1;
				ArrayList<String> id_list = new ArrayList<>();
				while (rs.next()) {
					tconst = rs.getString(1);
					date = rs.getDate(2);
					System.out.println(cnt + "  " + tconst + "		" + date + "			" + name);
					cnt++;
					id_list.add(tconst);
				}

				System.out.println("수정할 영상를 선택하세요");
				try{
					int idx = sc.nextInt();
					sc.nextLine();
					tconst = id_list.get(idx-1);
				} catch (InputMismatchException e){
					System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
					break;
				} catch (IndexOutOfBoundsException e){
					System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
					break;
				}

				String title, titile_type, adult, start_year;
				int runtime, gCode;
				System.out.println("수정을 원하지 않는 정보는 비워두세요.");

				System.out.print("Title : ");
				title = sc.nextLine();
				System.out.print("Title type : ");
				titile_type = sc.nextLine();
				while (true) {
					System.out.print("Adult(18세 이상: A, 미만: R) : ");
					adult = sc.nextLine();
					if (adult.equals("A") || adult.equals("R") || adult.equals("")) break;
					else System.out.println("A또는 R만 입력가능합니다.");
				}

				while (true) {
					System.out.print("Start Year(YYYYMMDD) : ");
					start_year = sc.nextLine();
					if (start_year.equals("") || start_year.matches("[0-9]*") && start_year.length() == 8) break;
					else System.out.println("입력 형식을 다시 확인하세요.");
				}

				System.out.print("Run time : ");
				runtime = sc.nextInt();

				System.out.println("아래의 장르중 선택하세요.");
				System.out.println();
				sql = "UPDATE MOVIE SET ";
				String setSql = "";
				/*
				if(!phone.equals("")) setSql += "phone = '" + phone + "', ";
				if(!name.equals("")) setSql += "name = '" + name + "', ";
				if(!address.equals("")) setSql += "address = '" + address + "', ";
				if(sex.equals("0")) setSql += "sex = 'M', ";
				else if(sex.equals("1")) setSql += "sex = 'F', ";
				if(!date.equals("")) setSql += "bdate = TO_DATE('" + date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6) + "', 'yyyy-mm-dd'), ";
				if(!job.equals("")) setSql += "job = '" + job + "', ";
				setSql = setSql.substring(0, setSql.length()-2);
				sql += setSql + " WHERE id = '" + ID + "'";
				//System.out.println(sql);
				 */
				if(!setSql.equals("")) {
					stmt.executeUpdate(sql);
					conn.commit();
					System.out.println("회원정보가 수정되었습니다.");
				}
				else{
					System.out.println("변경할 정보가 없습니다.");
				}
			} catch(SQLException ex) {
				System.err.println("sql error = " + ex.getMessage());
				System.exit(1);
			}
		}
	}
}
	
