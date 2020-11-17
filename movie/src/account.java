import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class account {
    static Scanner sc = new Scanner(System.in);
    public static String ID = "";
    public static boolean LOGIN = false;
    public static boolean ADMIN = false;

    public static void signUp(Connection conn, Statement stmt){

        System.out.println("회원가입을 선택하셨습니다.");
        System.out.println("정보를 입력하세요. *붙은 정보는 필수로 입력해야하는 정보입니다.");

        String id , pw, phone, name; // Not null data
        String address, sex, date, job;
        String membership = "Basic";
        final int act_limit = 3, rat_limit = 10;

        String sql = "";
        ResultSet rs;

        while(true) {
            System.out.print("*ID(15자리 이하) : ");
            id = sc.nextLine();

            if(id.equals("")) System.out.println("필수정보는 생략할 수 없습니다.");
            else if(id.length() > 15) System.out.println("15자리 이하로 입력하세요.");
            else {
                try {
                    sql = "SELECT id FROM ACCOUNT WHERE id = '" + id + "'";
                    rs = stmt.executeQuery(sql);
                    if(rs.next()) System.out.println("id가 이미 존재합니다.");
                    else break;

                } catch(SQLException ex2) {
                    System.err.println("sql error = " + ex2.getMessage());
                    System.exit(1);
                }
            }
        }

        while(true){
            System.out.print("*Password(20자리 이하) : ");
            pw = sc.nextLine();
            if(pw.equals("")) System.out.println("필수정보는 생략할 수 없습니다.");
            else if(pw.length() > 20) System.out.println("20자리 이하로 입력하세요.");
            else break;
        }

        while(true){
            System.out.print("*Phone number(-를 제외하고 입력하세요) : ");
            phone = sc.nextLine();
            if(phone.equals("")) System.out.println("필수정보는 생략할 수 없습니다.");
            else if(!phone.matches("[0-9]+")) System.out.println("숫자만 입력가능 합니다.");
            else if(phone.length() != 11) System.out.println("잘못된 전화번호입니다.");
            else break;
        }

        while(true){
            System.out.print("*Name(20자리 이하) : ");
            name = sc.nextLine();
            if(name.equals("")) System.out.println("필수정보는 생략할 수 없습니다.");
            else if(name.length() > 20) System.out.println("20자리 이하로 입력하세요.");
            else break;
        }

        //System.out.println(id + ", " + pw + ", " + phone + ", " + name);

        System.out.print("Address : ");
        address = sc.nextLine();
        while(true) {
            System.out.print("Sex(남자는 0, 여자는 1) : ");
            sex = sc.nextLine();
            if(sex.equals("0") || sex.equals("1") || sex.equals("")) break;
            else System.out.println("0또는 1만 입력가능합니다.");
        }

        while(true) {
            System.out.print("Date of birth(YYYYMMDD) : ");
            date = sc.nextLine();
            if(date.equals("") || date.matches("[0-9]*") && date.length() == 8) break;
            else System.out.println("입력 형식을 다시 확인하세요.");
        }

        System.out.print("Job : ");
        job = sc.nextLine();

        sql = "INSERT INTO ACCOUNT VALUES (" +
                      "'" + id + "', " + "'" + pw + "', " + "'" + phone + "', " + "'" + name + "', ";
        if(address.equals("")) sql += "NULL, ";
        else sql += "'" + address + "', ";

        if(sex.equals("")) sql += "NULL, ";
        else if(sex.equals("0")) sql += "'M', ";
        else sql += "'F', ";

        if(date.equals("")) sql += "NULL, ";
        else sql += "TO_DATE('" + date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6) + "', 'yyyy-mm-dd'), ";

        if(job.equals("")) sql += "NULL, ";
        else sql += "'" + job + "', ";
        sql += "'" + membership + "', " + act_limit + ", " + rat_limit + ")";

        try {
            //System.out.println(sql);
            int ret = 0;
            ret = stmt.executeUpdate(sql);
            conn.commit();
        } catch(SQLException ex2) {
            System.err.println("sql error = " + ex2.getMessage());
            System.exit(1);
        }
    }

    public static void updatePassWord(Connection conn, Statement stmt){
        System.out.println("비밀번호 수정을 선택하셨습니다.");
        if(!LOGIN) System.out.println("로그인이 필요합니다.");
        else{
            System.out.println("변경할 비밀번호를 입력하세요.");
            String pw, checkPW;

            while(true){
                System.out.print("Password(20자리 이하) : ");
                pw = sc.nextLine();
                if(pw.equals("")) System.out.println("비밀번호를 입력하세요.");
                else if(pw.length() > 20) System.out.println("20자리 이하로 입력하세요.");
                else {
                    System.out.println("한번 더 입력하세요.");
                    System.out.print("Password(20자리 이하) : ");
                    checkPW = sc.nextLine();
                    if(!pw.equals(checkPW)) System.out.println("입력한 비밀번호가 다릅니다.");
                    else break;
                }
            }

            try {
                String sql = "UPDATE ACCOUNT SET pw = '" + pw + "' WHERE id = '" + ID + "'";
                stmt.executeUpdate(sql);
                System.out.println("수정이 완료되었습니다.");
                conn.commit();
            } catch(SQLException ex2) {
                System.err.println("sql error = " + ex2.getMessage());
                System.exit(1);
            }
        }
    }

    public static void login(Connection conn, Statement stmt){
        System.out.println("로그인을 선택하셨습니다.");
        System.out.println("ID와 PASSWORD를 입력하세요.");

        String id, pw;
        String sql = "";
        ResultSet rs;

        while(true){
            System.out.print("ID : ");
            id = sc.nextLine();
            if(!id.equals("")) break;
        }

        while(true){
            System.out.print("Password : ");
            pw = sc.nextLine();
            if(!pw.equals("")) break;
        }

        try {
            String membership;
            sql = "SELECT * FROM ACCOUNT WHERE id = '" + id + "' AND pw = " + "'" + pw + "'";
            rs = stmt.executeQuery(sql);
            if(rs.next()) {
                ID = id;
                LOGIN = true;
                rs.getString(9);
                if(rs.wasNull()) ADMIN = true;
                else ADMIN = false;
                if(!ADMIN) System.out.println("로그인 되었습니다.");
                else System.out.println("관리자계정으로 로그인 되었습니다.");
            }
            else System.out.println("ID가 존재하지 않거나 비밀번호가 다릅니다.");
        } catch(SQLException ex2) {
            System.err.println("sql error = " + ex2.getMessage());
            System.exit(1);
        }
    }

    public static boolean dropAccount(Connection conn, Statement stmt){
        System.out.println("회원탈퇴를 선택하셨습니다.");
        if(!LOGIN) System.out.println("로그인이 필요합니다.");
        else {
            System.out.println("계속 진행하시겠습니까?(Y:yes, N:no)");
            String q;
            q = sc.nextLine();
            if (q.equals("y") || q.equals("Y") || q.equals("yes") || q.equals("YES")) {
                try {
                    int ret = 0;
                    String sql = "DELETE FROM ACCOUNT WHERE id = '" + ID + "'";
                    ret = stmt.executeUpdate(sql);
                    if (ret == 1) {
                        System.out.println("정상적으로 탈퇴되었습니다.");
                        return true;
                    }
                } catch (SQLException ex2) {
                    System.err.println("sql error = " + ex2.getMessage());
                    System.exit(1);
                }
            }
        }
        return false;
    }

    public static void logOut(Connection conn, Statement stmt) {
        if(LOGIN) {
            ID = "";
            LOGIN = false;
            System.out.println("로그아웃 하셨습니다.");
        }
    }

    public static void updateAccount(Connection conn, Statement stmt) {
        System.out.println("회원 정보 수정을 선택하셨습니다.");
        if (!LOGIN) System.out.println("로그인이 필요합니다.");
        else {
            System.out.println("수정을 원하지 않는 정보는 비워두세요.");

            String phone, name; // Not null data
            String address, sex, date, job;
            String membership = "Basic";
            int act_limit = 3, rat_limit = 10;

            try {
                while (true) {
                    System.out.print("Phone number(-를 제외하고 입력하세요) : ");
                    phone = sc.nextLine();
                    if(phone.equals("")) break;
                    if (!phone.matches("[0-9]+")) System.out.println("숫자만 입력가능 합니다.");
                    else if (phone.length() != 11) System.out.println("잘못된 전화번호입니다.");
                    else break;
                }

                while (true) {
                    System.out.print("Name(20자리 이하) : ");
                    name = sc.nextLine();
                    if (name.length() > 20) System.out.println("20자리 이하로 입력하세요.");
                    else break;
                }

                System.out.print("Address : ");
                address = sc.nextLine();

                while (true) {
                    System.out.print("Sex(남자는 0, 여자는 1) : ");
                    sex = sc.nextLine();
                    if (sex.equals("0") || sex.equals("1") || sex.equals("")) break;
                    else System.out.println("0또는 1만 입력가능합니다.");
                }

                while (true) {
                    System.out.print("Date of birth(YYYYMMDD) : ");
                    date = sc.nextLine();
                    if (date.equals("") || date.matches("[0-9]*") && date.length() == 8) break;
                    else System.out.println("입력 형식을 다시 확인하세요.");
                }

                System.out.print("Job : ");
                job = sc.nextLine();

                String sql = "UPDATE ACCOUNT SET ";
                String setSql = "";
                if(!phone.equals("")) setSql += "phone = '" + phone + "', ";
                if(!name.equals("")) setSql += "name = '" + name + "', ";
                if(!address.equals("")) setSql += "address = '" + address + "', ";
                if(sex.equals("0")) setSql += "sex = 'M', ";
                else if(sex.equals("1")) setSql += "sex = 'F', ";
                if(!date.equals("")) setSql += "bdate = TO_DATE('" + date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6) + "', 'yyyy-mm-dd'), ";
                if(!job.equals("")) setSql += "job = '" + job + "', ";

                if(!setSql.equals("")) {
                    setSql = setSql.substring(0, setSql.length()-2);
                    sql += setSql + " WHERE id = '" + ID + "'";
                    stmt.executeUpdate(sql);
                    conn.commit();
                    System.out.println("회원정보가 수정되었습니다.");
                }
                else{
                    System.out.println("변경할 정보가 없습니다.");
                }
            } catch (SQLException ex2) {
                System.err.println("sql error = " + ex2.getMessage());
                System.exit(1);
            }
        }
    }
}
