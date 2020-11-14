import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class admin {
    public static void upload_movie(Connection conn, Statement stmt){
    	if(account.ADMIN == false) System.out.println("�߸��� ���� : �����ڰ� �ƴմϴ�");
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
				System.out.println("���� �߰��� �����ϼ̽��ϴ�.");
				System.out.println("������ �Է��ϼ���. *�� ���� ������ �ʼ��� �Է��ؾ��ϴ� �����Դϴ�. ");
				
				while (true) {
					System.out.println("���� ����(30�� ����) : ");
					input = sc.next();
					if(input.length() > 30) System.out.println("30�ڸ� ���Ϸ� �Է��ϼ���.");
		            else {
						sql = sql + "'" + input + "',";
						break;
		            }
				}
				while (true) {
					System.out.println("���� Ÿ��(20�� ����) : ");
					input = sc.next();
					if(input.length() > 20) System.out.println("20�ڸ� ���Ϸ� �Է��ϼ���.");
		            else {
						sql = sql + "'" + input + "',";
						break;
		            }
				}
				while (true) {
					System.out.print("������� (���ο�:R�Է� / �� ��:A�Է�) :");
					input = sc.next();
					if (input.equals("R")||input.equals("A")||input.equals("")) {
						sql = sql + "'" + input + "',";
						break;
					}
					else {
						System.out.print("RȤ�� A�� �Է����ּ���");
					}
				}
				while (true) {
				System.out.print("������ (yyyy-mm-dd) : ");
				input = sc.next();
					try{
				        SimpleDateFormat  dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
				        dateFormat.setLenient(false);
				        dateFormat.parse(input);
						sql = sql + "to_date('" + input + "','yyyy-mm-dd'),";
						break;			         
			        }catch (ParseException  e){
			        	System.out.println("�߸��� ��¥ ����Դϴ�.");
			        	continue;
				    }
				}
				
				System.out.print("�󿵽ð� (�д���: ");
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
	
