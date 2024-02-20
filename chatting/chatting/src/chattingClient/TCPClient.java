package chattingClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
	static Socket cSocket;
	static BufferedReader br;
	static PrintWriter out;
	private final String ip = "211.241.150.169";
	private final int  port = 9001;
	Scanner sc = new Scanner(System.in);;

	public TCPClient() {
		SocketThread st = null;
		try {
			cSocket = new Socket(ip, port);
			br = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			String response = br.readLine();
			System.out.println(response);
			if (response.equals("채팅방 인원수가 가득찼습니다.")) {
				return;
			}

			out = new PrintWriter(cSocket.getOutputStream(), true);
			// ID 입력부분
			String UserID;
			while (true) {
				UserID = sc.nextLine();
				if (UserID.equals("")) {
					System.out.println("공백을 ID로 사용할 수 없습니다. 다시 입력해주세요.");
				} else {
					out.println(UserID);
					response = br.readLine();
					System.out.println(response);
					if (response.equals("사용가능한 아이디입니다."))
						break;
				}
			}
			System.out.println("접속을 종료하시려면 '접속종료!!'를 입력하세요.");
			System.out.println("/help를 입력하시면 명령어를 볼 수 있습니다.");
			st = new SocketThread();
			st.start();

			// 입력하는 부분
			while (true) {
				String inputLine = sc.nextLine(); // 사용자에게서 서버로 보낼 값을 입력받는 변수
				if(!st.isAlive())
					break;
				// 입력받은 키보드 값을 서버로 전송
				if (inputLine.equals("접속종료!!")) {
					out.flush();
					st.interrupt();
					break;
				} else {
					out.println(inputLine);
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("서버 통신 에러 발생");
		} catch (IOException e) {
				System.out.println("서버 통신 에러 발생");
		} finally {
			try {
				if (cSocket != null)
					cSocket.close();
				if (br != null)
					br.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class SocketThread extends Thread {
		
		public void run() {
			try {
				String response = null;
				while ((response = br.readLine()) != null) {
					if (response.equals("#out")) {
						System.out.println("강퇴 당하셨습니다.");
						interrupt();
						out.println("/a4d2zc29zvc124zfsde2czxb245dzb2");
						continue;
					} else if (response.equals("/a4d2zc29zvc124zfsde2czxb245dzb2")) {
						throw new IOException("강제 예외발생");
					}
					System.out.println(response);
				}
			} catch (IOException e) {
				if (Thread.interrupted()) {
					if(e.getMessage().equals("강제 예외발생")) {
						System.out.println("엔터키를 눌러 프로그램을 종료하세요.");
					}else {
						System.out.println("채팅 종료!!");
					}
				} else {
					System.out.println("서버 통신 에러 발생");
				}
			} finally {
				try {
					if (cSocket != null)
						cSocket.close();
					if (br != null)
						br.close();
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new TCPClient();
	}

}
