import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class rating {

	public static void viewMyRating(Connection conn, Statement stmt) {
		// 로그인 시 자신이 내린 평가 내역 보기.
		try {
			String sql = "select m.title, r.average_rating, p.rating "
					+"from movie m, rating r, provides p, account a "
					+"where m.tconst = r.tcon and "
					+"r.r_id = p.r_id and "
					+"p.a_id = a.id and "
					+"a.id = "
					+ "'" + account.ID + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.isBeforeFirst()) {
				System.out.println("평가한 영상물이 없습니다.");
				return;
			}
			System.out.println("회원님이 평가하신 영상물들의 목록입니다.\n"
					+"------------------------------------------------------------");
			while(rs.next()) {
				// no impedance mismatch in JDBC
				String title= rs.getString(1);
				double av_rating = rs.getDouble(2);
				double rating = rs.getDouble(3);
				
				System.out.println("영상물 제목 = " + title 
						+", 평균 평점 = " + av_rating 
						+", 내가 내린 평점 = " + rating);
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
						+ "where m.tconst = r.tcon and "
						+ "  r.r_id = p.r_id and "
						+ "  p.a_id = a.id";
				ResultSet rs = stmt.executeQuery(sql);
				System.out.println("모든 영상물에 대한 평가 목록입니다\n"
						+"------------------------------------------------------------");
				while(rs.next()) {
					// no impedance mismatch in JDBC
					String title= rs.getString(1);
					double av_rating = rs.getDouble(2);
					String rating_user_id = rs.getString(3);
					double rating = rs.getDouble(4);
					
					System.out.println("영상물 제목 = " + title 
							+", 평균 평점 = " + av_rating 
							+", 평점 내린 유저의 id = " + rating_user_id
							+", 해당 id의 평가 = " + rating);
				}
				System.out.println("------------------------------------------------------------\n");
			}catch(SQLException ex) {
				System.err.println("sql error2 = " + ex.getMessage());
				System.exit(1);
			}
		}
	}
	
}
