import java.sql.*;
import java.util.Scanner;

public class search {
    public static void allMovieSearch(Connection conn, Statement stmt){
        try{
            String sql = "SELECT tconst, title FROM MOVIE WHERE tconst NOT IN " +
                                 "(SELECT tcon FROM RATING R, PROVIDES P WHERE A_ID = '" + account.ID + "' AND P.R_ID = R.R_ID) ORDER BY tconst";
            ResultSet rs = stmt.executeQuery(sql);
            
            if(!rs.isBeforeFirst()) {//검색 결과가 없는 경우
              	 System.out.printf("검색 결과가 없습니다.\n");
              	 return;
              }
            
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int cnt = resultSetMetaData.getColumnCount();
            System.out.printf("    ");
            for(int i=1;i<=cnt;i++){
                System.out.printf(resultSetMetaData.getColumnName(i));
                if(i!=cnt) System.out.printf("			");
                else System.out.println();
            }

            while(rs.next()) {
                System.out.println(rs.getString(1) + "          " + rs.getString(2));
            }
        } catch(SQLException ex) {
            System.err.println("sql error : " + ex.getMessage());
            System.exit(1);
        }
        getMovieInfo(conn, stmt);
    }

    public static void movieSearchUsingName(Connection conn, Statement stmt) {
        ResultSet rs;
        String sql = "";
        String title = "";
        System.out.print(" 검색할 제목을 입력하세요 : ");
        Scanner scan_title = new Scanner(System.in);
        title = scan_title.nextLine();
        try {
            stmt = conn.createStatement();
            sql = "SELECT tconst, title FROM MOVIE WHERE title LIKE '%" + title + "%' AND tconst NOT IN " +
                          "(SELECT tcon FROM RATING R, PROVIDES P WHERE A_ID = '" + account.ID + "' AND P.R_ID = R.R_ID)";
            rs = stmt.executeQuery(sql);
            
            if(!rs.isBeforeFirst()) {//검색 결과가 없는 경우
           	 System.out.printf("검색 결과가 없습니다.\n");
           	 return;
           }
            
            while(rs.next()){
                System.out.println(rs.getString(1) + "\t" +rs.getString(2));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        getMovieInfo(conn,stmt);
    }

    public static void selectMovieSearch(Connection conn, Statement stmt){
        String sql = "SELECT DISTINCT tconst, title FROM MOVIE, GENRE, VERSION";
        String sqlv = "SELECT DISTINCT MOVIE.tconst, VERSION.original_title FROM MOVIE, GENRE, VERSION";
        int option_cnt = 0;

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
        if(!s.equals("0")){
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
        if( !s.equals("0")){
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
        if(a != 0 && b != 0 && a <= b){
            if(option_cnt > 0){
                sb.append(" and ");
            }
            else{
                sb.append(" WHERE ");
            }
            sb.append(" Runtime_minutes >= " + a + " and Runtime_minutes <= " + b );
            option_cnt++; //option_cnt는 덧붙이기 한 후에 증가시켜야한다.
        }
        
	        sb.append( "AND tconst NOT IN "
				+ "(SELECT tcon FROM RATING R, PROVIDES P WHERE A_ID = '" + account.ID + "' AND P.R_ID = R.R_ID)");
	        sb.append(" ORDER BY tconst");

        sql = sb.toString();
        System.out.println(sql);

        try {
            ResultSet rs = stmt.executeQuery(sql);

            if(!rs.isBeforeFirst()) {//검색 결과가 없는 경우
              	 System.out.printf("검색 결과가 없습니다.\n");
              	 return;
              }
            
            while(rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        getMovieInfo(conn,stmt);
    }

    public static void getMovieInfo(Connection conn, Statement stmt){
        String sql = "";
        ResultSet rs = null;
        String tconst = "";

        System.out.print("\n정보 확인 및 평가할 영상물의 식별번호를 입력하세요 (ex t00000218) : ");
        Scanner scan = new Scanner(System.in);
        tconst = scan.nextLine();
        //제목, 종류, 재생시간, 상영 년도, 장르 , 버전, 평가 유무
        sql = "SELECT tconst, title, title_type, Start_Year, Runtime_Minutes, genre FROM MOVIE, GENRE WHERE MOVIE.tconst = '"+tconst+"' and GENRE.GENRE_CODE = MOVIE.GCODE";
        //System.out.println(sql);

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                System.out.println("식별번호 : " + rs.getString(1) + "\n" +
                                   "영상물 제목 : " + rs.getString(2) + "\n" +
                                   "영상물 타입 : " + rs.getString(3) + "\n" +
                                   "영상물 상영연도 및 날짜 : " + rs.getDate(4) + "\n" +
                                   "상영시간(분) : " + rs.getInt(5) + "\n" +
                                   "영상물 장르 : " + rs.getString(6));
            }
        } catch(SQLException ex) {
            System.err.println("sql error : " + ex.getMessage());
            System.exit(1);
        }

        sql = "SELECT DISTINCT  nation  FROM MOVIE, VERSION WHERE VERSION.Tcon = '"+tconst+"' and MOVIE.Tconst = VERSION.Tcon";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            System.out.print("영상물 버젼 종류 : ");
            if(!rs.isBeforeFirst()) System.out.printf("null");
            else {
                while (rs.next()) {
                    System.out.print(rs.getString(1) + " ");
                }
            }
        } catch(SQLException ex) {
            System.err.println("sql error : " + ex.getMessage());
            System.exit(1);
        }

        sql = "SELECT DISTINCT Average_Rating FROM MOVIE, RATING WHERE RATING.Tcon = '"+tconst+"'";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            System.out.print("\n영상물 평균 평점 : ");
            if(!rs.isBeforeFirst()) System.out.printf("null");
            else {
                while (rs.next()) {
                    System.out.print(rs.getDouble(1) + " ");
                }
            }

            System.out.println();
            System.out.println();

            System.out.println("영상물을 평가하시겠습니까?(Y:Yes, N:No)");
            String q = scan.nextLine();
            if(q.equals("y") || q.equals("Y") || q.equals("YES") || q.equals("yes")) movie_rate(conn, stmt, tconst);

        } catch(SQLException ex) {
            System.err.println("sql error : " + ex.getMessage());
            System.exit(1);
        }

    }

    public static void movie_rate(Connection conn, Statement stmt, String tconst){
        String sql = "";
        ResultSet rs = null;
        Scanner scan = new Scanner(System.in);
        String ac_ID = account.ID;
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

        try {
            sql = "SELECT r_id FROM RATING WHERE tcon = '" + tconst + "'";
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(!rs.isBeforeFirst()) {
                sql = "select max(R_ID) from RATING";

                rs = stmt.executeQuery(sql);
                rs.next();
                maxconst = rs.getString(1);
                maxconst = maxconst.replace("r", "");
                int temp = Integer.parseInt(maxconst);
                temp++;
                maxconst = String.format("%08d", temp);
                maxconst = "r" + maxconst;

                sql = "insert into RATING values('" + tconst + "', '" + maxconst + "', 0 )";
                rs = stmt.executeQuery(sql);
                conn.commit();
            }
            else{
                rs.next();
                maxconst = rs.getString(1);
            }

            //System.out.print("rating insert 완료 !! ");

            // provides에 사용자가 입력한 평점, R_ID(rating에서 쓴것 그대로), A_ID(사용자가 입력한 id), 를 insert한다.
            //tconst가 가진 평점들의 값을 provides를 통해 더한 뒤에 평점의 갯수만큼 나누어서 평균 평점을 구한다. 그냥 avg로 바로 구할 수 있다.
            //이제 그 avg값을 tconst의 rating의 Average_Rating에 update한다.

            sql = "insert into PROVIDES values( " + rating + " ,  '" + maxconst + "', '" + ac_ID + "')"; //여기서 ac_ID만 사용자 이름이면 된다. account table에 없는 이름을 넣으면 참조무결성제약 위반.
            rs = stmt.executeQuery(sql);
            conn.commit();
            //ystem.out.print("\n provides insert 진입 @@ ");

            sql = "select AVG(Rating) FROM PROVIDES WHERE R_ID = '" + maxconst + "'";
            rs = stmt.executeQuery(sql);
            rs.next();
            Double mean = rs.getDouble(1);
            mean = Math.round(mean*10.0)/10.0;
            sql = "update RATING SET Average_Rating = " + mean + " WHERE  Tcon = '" + tconst  + "'";
            rs = stmt.executeQuery(sql);
            conn.commit();
            System.out.println("평가 입력이 완료되었습니다.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
