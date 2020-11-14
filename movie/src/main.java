import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class main {
    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    public static final String USER_ID ="movie";
    public static final String USER_PASSWD ="movie";

    public static void main(String[] args){
        Connection conn = null; // Connection object
        Statement stmt = null;	// Statement object

        try {
            // Load a JDBC driver for Oracle DBMS
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Get a Connection object
            System.out.println("Success!");
        }catch(ClassNotFoundException e) {
            System.err.println("error = " + e.getMessage());
            System.exit(1);
        }

        // Make a connection
        try{
            conn = DriverManager.getConnection(URL, USER_ID, USER_PASSWD);
        }catch(SQLException ex) {
            ex.printStackTrace();
            System.err.println("Cannot get a connection: " + ex.getMessage());
            System.exit(1);
        }

        try{
            conn.setAutoCommit(false); // auto-commit disabled
            // Create a statement object
            stmt = conn.createStatement();
        }catch(SQLException ex2) {
            System.err.println("sql error = " + ex2.getMessage());
            System.exit(1);
        }

        while(!account.LOGIN) {
            showMenuBeforeLogin(conn, stmt);
            if(account.LOGIN) showMenuAfterLogin(conn, stmt);
        }

        // Release database resources.
        try {
            // Close the Statement object.
            stmt.close();
            // Close the Connection object.
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void showMenuBeforeLogin(Connection conn, Statement stmt){
        Scanner sc = new Scanner(System.in);
        int menu = 0;
        boolean state = true;
        while(state) {
            System.out.println("기능을 선택하세요.");
            System.out.println("1:회원가입, 2:로그인, 0 : 종료");
            try {
                menu = sc.nextInt();
            }catch (InputMismatchException e){
                sc.nextLine();
                System.out.println("메뉴에 포함된 숫자만 입력하세요.");
                continue;
            }
            if(0 > menu || 2 < menu) System.out.println("잘못된 번호입니다.");
            switch (menu) {
                case 0:
                    state = false;
                    System.exit(1);
                case 1:
                    account.signUp(conn, stmt);
                    break;
                case 2:
                    account.login(conn, stmt);
                    state = false;
                    break;
            }
        }
    }

    private static void showMenuAfterLogin(Connection conn, Statement stmt){
        Scanner sc = new Scanner(System.in);
        int menu = 0;
        boolean state = true;
        while(state) {
            System.out.println("기능을 선택하세요.");
            /** INSERT CODE HERE **/
            if(account.ADMIN) { // menu for admin
                System.out.println("1:회원정보 수정, 2:비밀번호 수정, 3:회원탈퇴, 4:로그아웃 0 : 종료");
            }
            else{ // menu for customer
                System.out.println("1:회원정보 수정, 2:비밀번호 수정, 3:회원탈퇴, 4:로그아웃, 5:영상물 메뉴 0 : 종료");
            }

            try {
                menu = sc.nextInt();
            }catch (InputMismatchException e){
                sc.nextLine();
                System.out.println("메뉴에 포함된 숫자만 입력하세요.");
                continue;
            }
            if(account.ADMIN && 0 > menu || menu > 5) System.out.println("잘못된 번호입니다.");
            if(!account.ADMIN && 0 > menu || menu > 6) System.out.println("잘못된 번호입니다.");

            switch (menu) {
                case 0:
                    state = false;
                    break;
                case 1:
                    account.updateAccount(conn,stmt);
                    break;
                case 2:
                    account.updatePassWord(conn, stmt);
                    break;
                case 3:
                    if(account.dropAccount(conn, stmt)) {
                        account.logOut(conn, stmt);
                        state = false;
                    }
                    break;
                case 4:
                    account.logOut(conn, stmt);
                    state = false;
                    break;
               case 5:
                    break;     
            }
        }
    }
}
