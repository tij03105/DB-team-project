import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
                          "(SELECT tcon FROM RATING R, PROVIDES P WHERE A_ID = '" + account.ID + "' AND P.R_ID = R.R_ID) ORDER BY tconst";
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
        String sql = "SELECT DISTINCT tconst, title FROM MOVIE, GENRE, VERSION WHERE " +
                             "tconst NOT IN (SELECT tcon FROM RATING R, PROVIDES P WHERE A_ID = '" + account.ID + "' AND P.R_ID = R.R_ID)";
        String sqlv = "SELECT DISTINCT MOVIE.tconst, VERSION.original_title FROM MOVIE, GENRE, VERSION WHERE " +
                              "tconst NOT IN (SELECT tcon FROM RATING R, PROVIDES P WHERE A_ID = '" + account.ID + "' AND P.R_ID = R.R_ID)";
        StringBuffer sb = new StringBuffer();
        String s = "";
        Scanner scan_option = new Scanner(System.in);

        System.out.print(" 검색할 조건을 입력받습니다. \n ");
        try {
            String sub_sql = "SELECT DISTINCT nation FROM VERSION";
            ResultSet rs = stmt.executeQuery(sub_sql);

            ArrayList<String> version_list = new ArrayList<>();
            while (rs.next()) {
                version_list.add(rs.getString(1));
            }

            int vCnt = 0;
            while (true) {
                int cnt = 1;
                System.out.println("아래의 버전중 하나를 선택하세요. 순차적인 입력으로 다중 선택 가능(ex 1입력 2입력)");
                for (int i = 0; i < version_list.size(); i++) {
                    System.out.println(cnt + "	" + version_list.get(i));
                    cnt++;
                }

                System.out.printf("번호(버전선택), 공백(선택안함):");
                try {
                    String input = scan_option.nextLine();
                    if (input.equals("")) break;
                    else if (!input.matches("[0-9]+")) System.out.println("잘못된 입력입니다.");
                    else {
                        int idx = Integer.parseInt(input);
                        String nation = version_list.get(idx - 1);
                        version_list.remove(idx - 1);
                        if (vCnt == 0) {
                            sb.append(sqlv);
                            sb.append(" AND (VERSION.nation = '" + nation + "'");
                        } else sb.append(" OR VERSION.nation = '" + nation + "'");
                        sb.append(" AND VERSION.Tcon = MOVIE.Tconst");
                        vCnt++;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("잘못된 입력입니다.");
                    scan_option.nextLine();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("잘못된 입력입니다.");
                }
            }

            if(vCnt > 0) sb.append(")");
            else if(vCnt == 0) sb.append(sql);

            while (true) {
                System.out.println("아래의 영상물 타입 중 하나를 선택하세요.");
                String sql_t = "select distinct title_type from movie";
                rs = stmt.executeQuery(sql_t);
                String m_type;
                int cnt = 1;
                ArrayList<String> type_list = new ArrayList<>();
                while(rs.next()){
                    m_type = rs.getString(1);
                    type_list.add(m_type);
                    System.out.println(cnt + "   " + m_type);
                    cnt++;
                }
                System.out.println("타입선택(번호), 상관 없음 : 0");
                try{
                    int idx = scan_option.nextInt();
                    scan_option.nextLine();
                    if (idx == 0) {
                        break;
                    }
                    m_type = type_list.get(idx-1);
                } catch (InputMismatchException e){
                    System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
                    scan_option.next();
                    continue;
                } catch (IndexOutOfBoundsException e){
                    System.out.println("잘못된 입력입니다. 이전메뉴로 돌아갑니다.");
                    continue;
                }
                sb.append(" AND title_type = '" + m_type + "'");
                break;
            }

            sql = "SELECT * FROM GENRE";
            rs = stmt.executeQuery(sql);

            ArrayList<String> gen_list = new ArrayList<>();
            ArrayList<String> gcode_list = new ArrayList<>();
            while (rs.next()) {
                gcode_list.add(rs.getString(1));
                gen_list.add(rs.getString(2));
            }

            int gCnt = 0;
            while(true) {
                int cnt = 1;
                System.out.println("아래의 장르중 하나를 선택하세요. 순차적인 입력으로 다중 선택 가능(ex 1입력 2입력)");
                for(int i=0;i<gen_list.size();i++) {
                    System.out.println(cnt + "	" + gen_list.get(i));
                    cnt++;
                }

                System.out.printf("번호(장르선택), 공백(선택안함):");
                try {
                    String input = scan_option.nextLine();
                    if(input.equals("")) break;
                    else if(!input.matches("[0-9]+")) System.out.println("잘못된 입력입니다.");
                    else {
                        int idx = Integer.parseInt(input);
                        String gCode = gcode_list.get(idx - 1);
                        gen_list.remove(idx-1);
                        gcode_list.remove(idx-1);
                        if(gCnt == 0) sb.append(" AND (gCode = '" + gCode + "'");
                        else sb.append(" OR gCode = '" + gCode + "'");
                        gCnt++;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("잘못된 입력입니다.");
                    scan_option.nextLine();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("잘못된 입력입니다.");
                }
            }
            if(gCnt > 0) sb.append(")");

             System.out.print("\n영상물 Runtime의 범위를 입력받습니다. ");
        
        int min = 0;
        int max = 0;
        
        String a;
        String b;
        
        while(true){
            while(true){
                System.out.print("\n 최소 러닝타임을 입력하세요. : ");
             
                a = scan_option.nextLine();
                if(a.equals("")) System.out.println("공란입니다. 최소 러닝타임을 입력하세요. 상관없을 시 0을 입력하세요");
                 else if(!a.matches("[0-9]+")) System.out.println(" 0이상의 정수만 입력가능 합니다.");
                 else break;
             }
            min = Integer.parseInt(a);
            if(min < 0) System.out.println("0이상의 값으로 입력하세요");
            else break;
        }
        
       
        while(true){
            while(true){
                System.out.print("\n 최대 러닝타임을 입력하세요. (공란 가능) : ");
             
                b = scan_option.nextLine();
                if(b.equals("")) { System.out.println("최대 러닝타임에 제한을 두지 않습니다."); b="20000"; break;  }
                 else if(!b.matches("[0-9]+")) System.out.println(" 0이상의 정수만 입력가능 합니다.");
                 else break;
             }
            max = Integer.parseInt(b);
            if(max < 0) System.out.println("0이상의 값으로 입력하세요");
            else if(min > max) System.out.println("범위가 잘못되었습니다. 최소 시간보다 크게 입력하세요.");
            else break;
        }
        
        if(min >= 0 && max >= 0 && min <= max){
            sb.append(" AND Runtime_minutes >= " + min + " and Runtime_minutes <= " + max );
        }
            
            
            sb.append(" ORDER BY tconst");

            sql = sb.toString();
            //System.out.println(sql);

            rs = stmt.executeQuery(sql);

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
        Scanner scan = null;
        while(true) {
        try {
           
              System.out.print("\n정보 확인 및 평가할 영상물의 식별번호를 입력하세요 (ex t00000218) : ");
              scan = new Scanner(System.in);
              tconst = scan.nextLine();
              System.out.println(tconst);
              sql = "SELECT tconst FROM MOVIE where tconst = '" + tconst + "'";
              rs = stmt.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
   
            //System.out.println(sql);
            if(!rs.isBeforeFirst()) {
               System.out.println("일치하는 영상이 없습니다.");
               System.out.println("다른 영상을 찾으시겠습니까?(Y:yes, N:no)");
               String q;
               q = scan.nextLine();
               if (q.equals("y") || q.equals("Y") || q.equals("yes") || q.equals("YES")) continue;
               else if(q.equals("n") || q.equals("N") || q.equals("no") || q.equals("NO")) break;
               else {
                  System.out.println("잘못된 값입니다. 이전메뉴로 돌아갑니다.");
                  break;
               }
            }
        }catch(SQLException ex) {
         System.err.println("sql error = " + ex.getMessage());
         System.exit(1);
      }
     
        
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
            if(q.equals("y") || q.equals("Y") || q.equals("YES") || q.equals("yes")) {
               movie_rate(conn, stmt, tconst);
               break;
            }
            else if(q.equals("n") || q.equals("N") || q.equals("no") || q.equals("NO")) break;
         else {
            System.out.println("잘못된 값입니다. 이전메뉴로 돌아갑니다.");
            break;
         }
        } catch(SQLException ex) {
            System.err.println("sql error : " + ex.getMessage());
            System.exit(1);
        }
        }
    }

    public static void movie_rate(Connection conn, Statement stmt, String tconst){
        String sql = "";
        ResultSet rs = null;
        Scanner scan;
        double rating;
        while (true) {
           scan = new Scanner(System.in);
           String ac_ID = account.ID;
           
           System.out.print("\n영상물에 대한 평점을 입력해주세요 (0.0~10.0) : ");
           
           try {
              rating = scan.nextDouble();
               if (rating < 0 || rating >10) {
                 System.out.println("0.0점부터 10.0점 사이의 값을 입력해주세요");
                 continue;
              }
           } catch (InputMismatchException e) {
              System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
              continue;
           }
           String maxconst = "";
           //insert할 테이블 RATING PROVIDES
           //insert into RATING values (   't00000141',   'r00000141',   9Average_Rating   );
           //INSERT INTO PROVIDES VALUES (   4   ,'r00000008'R_ID   ,'Knouted'A_Id   );
   
           //다음순서의 r_ID값을 구하기 위해서, rating에 있는 r_ID값중에서 가장 큰 값에 +1을 한다.
           //이제 tconst를 받았다.RATING에 tconst, 다음순서의 r_ID값, 0(average_rating)을넣고 insert한다.
           // provides에 사용자가 입력한 평점, R_ID(rating에서 쓴것 그대로), A_ID(사용자가 입력한 id), 를 insert한다.
           //tconst가 가진 평점들의 값을 더한 뒤에 평점의 갯수만큼 나누어서 평균 평점을 구한다. 그냥 avg로 바로 구할 수 있다.
           //이제 그 avg값을 tconst의 rating의 Average_Rating에 update한다.
   
           try {
               sql = "SELECT r_id FROM RATING WHERE tcon = '" + tconst + "'";
//               System.out.println(sql);
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
               break;
           } catch (SQLException e) {
               e.printStackTrace();
               System.exit(1);
           }
        }
    }
    
}
