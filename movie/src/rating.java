import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class rating {

	public static void viewMyRating(Connection conn, Statement stmt) {
		// �α��� �� �ڽ��� ���� �� ���� ����
		try {
			String sql = "select m.title, r.average_rating, p.rating "
					+"from movie m, rating r, provides p, account a "
					+"where m.r_id = r.r_id and "
					+"r.r_id = p.r_id and "
					+"p.a_id = a.id and "
					+"a.id = "
					+ "'" + account.ID + "'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("ȸ������ ���Ͻ� ���󹰵��� ����Դϴ�.\n"
					+"------------------------------------------------------------");
			while(rs.next()) {
				// no impedance mismatch in JDBC
				String title= rs.getString(1);
				int av_rating = rs.getInt(2);
				int rating = rs.getInt(3);
				
				System.out.println("���� ���� = " + title 
						+", ��� ���� = " + av_rating 
						+", ���� ���� ���� = " + rating);
			}
			System.out.println("------------------------------------------------------------\n");

		}catch(SQLException ex) {
			System.err.println("sql error = " + ex.getMessage());
			System.exit(1);
		}
	}
	
	public static void adminViewRating(Connection conn, Statement stmt) {
		if(account.ADMIN == false) System.out.println("Wrong access : not a manager");
		else {
			try {
				String sql = "select m.title, r.average_rating, a.id, p.rating "
						+ "from movie m, rating r, provides p, account a "
						+ "where m.r_id = r.r_id and "
						+ "  r.r_id = p.r_id and "
						+ "  p.a_id = a.id";
				ResultSet rs = stmt.executeQuery(sql);
				System.out.println("��� ���󹰿� ���� �� ����Դϴ�\n"
						+"------------------------------------------------------------");
				while(rs.next()) {
					// no impedance mismatch in JDBC
					String title= rs.getString(1);
					int av_rating = rs.getInt(2);
					String rating_user_id = rs.getString(3);
					int rating = rs.getInt(4);
					
					System.out.println("���� ���� = " + title 
							+", ��� ���� = " + av_rating 
							+", ���� ���� ������ id = " + rating_user_id
							+", �ش� id�� �� = " + rating);
				}
				System.out.println("------------------------------------------------------------\n");
			}catch(SQLException ex) {
				System.err.println("sql error2 = " + ex.getMessage());
				System.exit(1);
			}
		}
	}
	
}
