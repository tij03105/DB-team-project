
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class admin {
	private static Scanner sc = new Scanner(System.in);

    public static void upload_movie(Connection conn, Statement stmt){
    	if(account.ADMIN == false) System.out.println("잘못된 접근 : 관리자가 아닙니다");
		else {
			try {
				String input;
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
					System.out.println("*영상 제목(30자 이하) : ");
					input = sc.nextLine();
					if(input.equals("")) System.out.println("필수정보는 생략할 수 없습니다.");
					else if(input.length() > 30) System.out.println("30자리 이하로 입력하세요.");
		            else {
						sql = sql + "'" + input + "',";
						break;
		            }
				}
				while (true) {
					System.out.println("영상물 타입(20자 이하) : ");
					input = sc.nextLine();
					if(input.length() > 20) System.out.println("20자리 이하로 입력하세요.");
		            else {
		            	if(!input.equals("")) sql += "'" + input + "',";
						else sql += "NULL,";
						break;
		            }
				}

				while (true) {
					System.out.println("관람등급 (성인용:R입력 / 그 외:A입력) :");
					input = sc.nextLine();
					if (input.equals("R")||input.equals("A")) {
						sql += "'" + input + "',";
						break;
					} else if(input.equals("")) {
						sql += "NULL,";
						break;
					} else {
						System.out.println("R혹은 A를 입력해주세요");
					}
				}

				while (true) {
					System.out.println("개봉일 (yyyy-mm-dd) : ");
					input = sc.nextLine();
					if(input.equals("")) {
						sql += "NULL,";
						break;
					} else {
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							dateFormat.setLenient(false);
							dateFormat.parse(input);
							sql = sql + "to_date('" + input + "','yyyy-mm-dd'),";
							break;
						} catch (ParseException e) {
							System.out.println("잘못된 날짜 양식입니다.");
						}
					}
				}
				
				while(true) {
					System.out.println("상영시간 (분단위): ");
					input = sc.nextLine();
					if(input.equals("")){
						sql += "NULL,";
						break;
					} else if(!input.matches("[0-9]+")){
						System.out.println("유효하지 않은 숫자 입니다.");
					} else {
						sql += input + ",";
						break;
					}
				}
				
				while(true) {
					System.out.println("아래의 장르중 선택하세요.");
					String sub_sql = "SELECT * FROM GENRE";
					rs = stmt.executeQuery(sub_sql);
					String genre;
					int cnt = 1;
					ArrayList<String> gen_list = new ArrayList<>();
					while(rs.next()){
						gen_list.add(rs.getString(1));
						genre = rs.getString(2);
						System.out.println(cnt + "	" + genre);
						cnt++;
					}
					System.out.printf("장르선택(번호), 빈칸 불가능:");
					try{
						int idx = sc.nextInt();
						sc.nextLine();
						input = gen_list.get(idx-1);
						sql += "'" + input + "')";
						break;
					} catch (InputMismatchException e){
						System.out.println("잘못된 입력입니다.");
						sc.next();
						continue;
					} catch (IndexOutOfBoundsException e){
						System.out.println("잘못된 입력입니다.");
						continue;
					}
				}
				stmt.executeUpdate(sql);
				conn.commit();
				System.out.println("영상물 추가 완료");
			} catch(SQLException ex) {
				System.err.println("영상물 추가 오류 : " + ex.getMessage());
				System.exit(1);
			}
		}
	}

	public static void updateMovie(Connection conn, Statement stmt){
		if(account.ADMIN == false) System.out.println("잘못된 접근 : 관리자가 아닙니다");
		else {
			System.out.println("영상 수정을 선택하셨습니다.");
			while (true) {
				System.out.println("수정할 영상의 이름을 입력하세요.");
				String name = "";
				name = sc.nextLine();

				try {
					String sql = "SELECT tconst, start_year FROM MOVIE WHERE title = '" + name + "'";
					ResultSet rs = stmt.executeQuery(sql);
					ResultSetMetaData resultSetMetaData = rs.getMetaData();

					//System.out.println(sql);
					if (!rs.isBeforeFirst()) {
						System.out.println("일치하는 영상이 없습니다.");
						System.out.println("다른 영상을 찾으시겠습니까?(Y:yes, N:no)");
						String q;
						q = sc.nextLine();
						if (q.equals("y") || q.equals("Y") || q.equals("yes") || q.equals("YES")) continue;
						else if (q.equals("n") || q.equals("N") || q.equals("no") || q.equals("NO")) break;
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
					for (int i = 1; i <= cnt; i++) {
						System.out.printf(resultSetMetaData.getColumnName(i));
						if (i != cnt) System.out.printf("			");
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
					try {
						int idx = sc.nextInt();
						sc.nextLine();
						tconst = id_list.get(idx - 1);
					} catch (InputMismatchException e) {
						System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
						break;
					} catch (IndexOutOfBoundsException e) {
						System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
						break;
					}

					String title, titile_type, adult, start_year;
					String runtime, gCode;
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
					runtime = sc.nextLine();

					System.out.println("아래의 장르중 선택하세요.");
					sql = "SELECT * FROM GENRE";
					rs = stmt.executeQuery(sql);
					String genre;
					cnt = 1;
					ArrayList<String> gen_list = new ArrayList<>();
					while (rs.next()) {
						gen_list.add(rs.getString(1));
						genre = rs.getString(2);
						System.out.println(cnt + "	" + genre);
						cnt++;
					}

					System.out.printf("장르선택(번호), 빈칸 불가능:");
					try {
						int idx = sc.nextInt();
						sc.nextLine();
						gCode = gen_list.get(idx - 1);
					} catch (InputMismatchException e) {
						System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
						sc.nextLine();
						break;
					} catch (IndexOutOfBoundsException e) {
						System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
						break;
					}

					sql = "UPDATE MOVIE SET ";
					String setSql = "";

					if (!title.equals("")) setSql += "title = '" + title + "', ";
					if (!titile_type.equals("")) setSql += "titile_type = '" + titile_type + "', ";
					if (!adult.equals("")) setSql += "is_adult = '" + adult + "', ";
					if (!start_year.equals(""))
						setSql += "start_year = TO_DATE('" + start_year.substring(0, 4) + "-" + start_year.substring(4, 6) + "-" + start_year.substring(6) + "', 'yyyy-mm-dd'), ";
					if (!runtime.equals("")) setSql += "runtime_minutes = " + Integer.parseInt(runtime) + ", ";
					if (!gCode.equals("")) setSql += "gcode = '" + gCode + "', ";

					setSql = setSql.substring(0, setSql.length() - 2);
					sql += setSql + " WHERE tconst = '" + tconst + "'";
					//System.out.println(sql);

					if (!setSql.equals("")) {
						stmt.executeUpdate(sql);
						conn.commit();
						System.out.println("영상 정보가 수정되었습니다.");
					} else {
						System.out.println("변경할 정보가 없습니다.");
					}

					System.out.println("다른 영상도 수정하시겠습니까?(Y: yes, N:no)");
					String q;
					q = sc.nextLine();
					if (q.equals("y") || q.equals("Y") || q.equals("yes") || q.equals("YES")) continue;
					else if (q.equals("n") || q.equals("N") || q.equals("no") || q.equals("NO")) break;
					else {
						System.out.println("잘못된 값입니다. 이전메뉴로 돌아갑니다.");
						break;
					}
				} catch (SQLException ex) {
					System.err.println("sql error = " + ex.getMessage());
					System.exit(1);
				}
			}
		}
	}

	public static void manageAccount(Connection conn, Statement stmt){
    	final String[] MEMBERSHIP = {"Basic", "Primium", "Prime"};
    	final int[] ACT_LIMIT = {3, 5, 7};
    	final int[] RAT_LIMIT = {10, 20, 30};

		if(account.ADMIN == false) System.out.println("잘못된 접근 : 관리자가 아닙니다");
    	else {
    		System.out.println("회원 등급 변경을 선택하셨습니다.");
    		String cus_id = "";

    		while(true) {
				System.out.println("변경할 회원의 이름을 입력하세요.");
				cus_id = sc.nextLine();
				try {
					String sql = "SELECT id, membership FROM ACCOUNT WHERE id = '" + cus_id + "'";
					ResultSet rs = stmt.executeQuery(sql);

					if (!rs.isBeforeFirst()) {
						System.out.println("존재하지 않는 회원입니다. 다시입력하세요.");
						continue;
					}
					else{
						rs.next();
						String membership = rs.getString(2);
						if(rs.wasNull()) membership = "관리자";
						System.out.println(cus_id + "회원에 대해 변경할 등급을 선택하세요.(ex 1)");
						System.out.println("현재 회원의 등급 : " + membership);
						while(true) {
							System.out.println("선택 가능한 등급 :");
							for (int i = 0; i < MEMBERSHIP.length; i++) {
								System.out.println((i + 1) + " : " + MEMBERSHIP[i]);
							}
							System.out.println((MEMBERSHIP.length+1) + " : " + "관리자");
							try {
								int idx = sc.nextInt();
								if (idx > MEMBERSHIP.length+1 || idx <= 0) {
									System.out.println("주어진 범위내에서 입력하세요.");
									continue;
								}
								if(idx == MEMBERSHIP.length+1) sql = "UPDATE ACCOUNT SET membership = NULL, act_limit = NULL, rat_limit = NULL WHERE id = '" + cus_id + "'";
								else sql = "UPDATE ACCOUNT SET membership = '" + MEMBERSHIP[idx - 1] + "', act_limit = " + ACT_LIMIT[idx - 1] + ", rat_limit = " + RAT_LIMIT[idx - 1] + " WHERE id = '" + cus_id + "'";
								stmt.executeUpdate(sql);
								conn.commit();
								System.out.println("변경이 완료되었습니다.");
								sc.nextLine();
								if(cus_id.equals(account.ID) && idx != MEMBERSHIP.length) account.ADMIN = false;
								return;
							} catch (InputMismatchException e) {
								System.out.println("입력 형식이 잘못되었습니다.");
								sc.nextLine();
								continue;
							}
						}
					}
				} catch (SQLException ex) {
					System.err.println("sql error = " + ex.getMessage());
					System.exit(1);
				}
			}
		}
	}
}
	