package create_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Pdb {
	
	private  Connection conn = null;
	private  Statement stmt = null;
	private  String ip= null;
	private  String port = null;
	private  Scanner sc = new Scanner(System.in);
	
	private String new_user;
	private String new_pw;
	private String new_db;
	
	private void connect() {
		String msg="DB 연결 시작";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			System.out.println("JDBC Driver error");
			System.exit(-1);
		}
		while(true) {
			System.out.println(msg);
			System.out.printf("ip : ");
			ip = sc.next();
			System.out.printf("port : ");
			port = sc.next();
			System.out.printf("db : ");
			String db = sc.next();
			System.out.printf("user : ");
			String user = sc.next();
			System.out.printf("password : ");
			String pw = sc.next();
			//ip->port->db->user->pw ������ �Է¹ޱ�!!
				  
			String     connurl  = "jdbc:postgresql://"+ip+":"+port+"/"+db;
	        try{
	        	conn = DriverManager.getConnection(connurl, user, pw);
	        }catch (SQLException e) {
	        	System.out.println("user, password, db, ip, port를 잘못입력하였습니다.");
	            msg="DB 재연결";
	            continue;
	        }
	        if(conn==null) {
	        	continue;
	        }
	        System.out.println("DB 연결 성공\n");
	        break;
		}
		
        
	}
	
	protected void create_database() {
		create_user();
		String msg="DB 생성 시작";
		while(true) {
			System.out.println(msg);
			System.out.printf("db_name : ");
			new_db = sc.next();
			try {
				String sql="CREATE DATABASE "+ new_db +" OWNER "+ new_user +";";
				stmt = conn.createStatement();
				if(stmt==null) {
					System.out.println("stmt 연결 오류");
					continue;
				}
				stmt.executeUpdate(sql);		
			} catch (SQLException e) {
				System.out.println("db 생성 오류");
				msg="DB 연결 재시작";
				continue;
			}finally {
				stmt_close();
			}
			System.out.println("db 연결 성공\n");
			break;
		}
		grant_previleges();
	}
	
	private void create_user() {
		connect();
		String msg="유저 생성 시작";
		while(true) {
			System.out.println(msg);
		    System.out.printf("UserName : ");
		    new_user = sc.next();
			System.out.printf("User pass : ");
			new_pw = sc.next();
			try {
				String sql="CREATE USER "+ new_user +" WITH PASSWORD \'"+new_pw+"\';";
				stmt = conn.createStatement();
				if(stmt==null) {
					System.out.println("stmt 연결 오류");
					continue;
				}
				stmt.executeUpdate(sql);	
			} catch (SQLException e) {
				System.out.println("유저 생성 오류 유저 이름을 확인하세요.\n");
				msg="유저 생성 재시작";
				continue;
			}finally {
				stmt_close();
			}
			System.out.println("유저 생성 성공");
			break;
		}
//select * from pg_user; 
//drop role sycros;
	}
	

	private void grant_previleges() {
		while(true) {
			try {
				String sql="GRANT ALL PRIVILEGES ON DATABASE "+ new_db +" TO "+ new_user +";";
				stmt = conn.createStatement();
				if(stmt==null) {
					System.out.println("stmt 연결 오류");
					continue;
				}
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.out.println("새 계정 특권 부여 실패");
				System.exit(-1);
			} finally {
				stmt_close();
			}
			break;
		}
		System.out.println("새 계정 특권 부여 성공");
	}

	private void new_conn() {
		while(true) {
			conn_close();
			System.out.println("새 계정 DB 연결 해제");
			String     connurl  = "jdbc:postgresql://"+ip+":"+port+"/"+new_db;
			try{
	        	conn = DriverManager.getConnection(connurl, new_user, new_pw);
	        }catch (SQLException e) {
	        	System.out.println("새 계정 로그인 실패");
	        	System.exit(-1);
	        }
	        if(conn==null) {
	        	continue;
	        }
	        System.out.println("색 계정 로그인 성공");
	        break;
		}	
	}

	protected  void create_table() {
		new_conn();
		while(true) {
			try {
				stmt = conn.createStatement();
				if(stmt==null) {
					continue;
				}
				//board 테이블
				String sql="CREATE TABLE cloud_account_key("
						+ "host_name varchar(256) PRIMARY KEY, "
						+ "scan_type int,"
						+ "scan_key varchar(256),"
						+ "access_key_id varchar(256),"
						+ "region varchar(256),"
						+ "scan_interval int);";
				stmt.executeUpdate(sql);
				//category 테이블
				sql="CREATE TABLE cloud_namespace("
						+ "host_name varchar(256) NOT NULL,"
						+ "namespace varchar(256) NOT NULL,"
						+ "PRIMARY KEY (host_name, namespace));";
				stmt.executeUpdate(sql);
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(-1);
			} finally {
				stmt_close();	
				
			}
			System.out.println("테이블 생성 성공");
			break;
		}
		conn_close();
		
	}
	

	
	private void conn_close() {
		try {
			if(conn!=null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("conn close error");
			System.exit(-1);
		}
		System.out.println("DB 연결 종료\n");
	}

	private void stmt_close() {

		try {
			if(stmt!= null)
				stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("stmt close error");
			System.exit(-1);
		}
	}


}
