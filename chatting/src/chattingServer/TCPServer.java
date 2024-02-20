package chattingServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.ClientInfo;


public class TCPServer implements Runnable {
	private ClientInfo clientInfo;
	static List<ClientInfo> list = Collections.synchronizedList(new ArrayList<ClientInfo>());
	static int UserNum = 0;
	static int maxsize = 2;
	private final static int port = 9001;
	static boolean cheif=false;

	public TCPServer(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
		list.add(clientInfo);
	}

	public static void main(String[] args) {
		ExecutorService eService = Executors.newFixedThreadPool(10);
		System.out.println("서버 시작");
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				ClientInfo clientInfo = new ClientInfo();
				clientInfo.setSocket(clientSocket);
				if (cheif == false) {
					clientInfo.setChief(true);
					cheif = true;
				}
				OutputStream out = clientSocket.getOutputStream();
				PrintWriter writer = new PrintWriter(out, true);
				if (list.size() >= maxsize) {
					writer.println("채팅방 인원수가 가득찼습니다.");
					clientSocket.close();
					continue;
				} else {
					writer.println("채팅방에 입장 하셨습니다. 사용할 ID를 입력하세요.");
				}
				TCPServer tes = new TCPServer(clientInfo);
				eService.submit(tes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("서버 종료");
		eService.shutdown();
	}

	@Override
	public void run() {
		BufferedReader br = null;
		PrintWriter myWriter = null;
		PrintWriter writer = null;
		OutputStream out = null;
		try {
			br = new BufferedReader(new InputStreamReader(clientInfo.getSocket().getInputStream()));
			out = clientInfo.getSocket().getOutputStream();
			myWriter = new PrintWriter(out, true);
			writer = new PrintWriter(out, true);
			String UserID = init(br, out, myWriter, writer);
			String inputLine;
			while ((inputLine = br.readLine()) != null && clientInfo.getSocket().isConnected()) {
				// '/'로 들어오면 명령어 취급
				if(inputLine.equals("/a4d2zc29zvc124zfsde2czxb245dzb2")) {
					myWriter.println("/a4d2zc29zvc124zfsde2czxb245dzb2");
					break;
				}
				else if (inputLine.substring(0, 1).equals("/")) {
					command(inputLine, out, myWriter, writer);
				} else if (inputLine.substring(0, 1).equals("#")) {
					chiefCommand(inputLine, out, myWriter, writer);
				}
				// '/'가 아닌경우 일반 메세지
				else {
					for (int i = 0; i < list.size(); i++) {
						if(list.get(i).getId()==null && list.get(0).getSocket().isConnected())
							continue;
						// ArrayList의 사이즈만큼 for문을 돌리면서 모든 클라이언트에게 메시지 전송
						out = list.get(i).getSocket().getOutputStream();
						// ArrayList의 i번째 getOutputStream()요소를 가져와 out객체에 대입한다.
						writer = new PrintWriter(out, true);
						// 새로운 printWriter객체를 생성
						writer.println("[" + UserID + "]" + inputLine);
						// ArrayList의 i번째 요소의 클라이언트에게 송신한다.
					}
				}
			}

			end(out, writer, UserID);

		} catch (IOException e) {
			list.remove(clientInfo);
			cheif=false;
		} finally {
			try {
				if (myWriter != null)
					myWriter.close();
				if (writer != null)
					writer.close();
				if (br != null)
					br.close();
				if (clientInfo.getSocket() != null)
					clientInfo.getSocket().close();
			} catch (IOException e) {
				System.out.println(clientInfo.getSocket() + "연결이 끊겼습니다.");
			}
		}

	}

	private String init(BufferedReader br, OutputStream out, PrintWriter myWriter, PrintWriter writer)
			throws IOException {
		UserNum++;
		System.out.println("클라이언트 : " + UserNum + "(" + Thread.currentThread() + ")" + " 연결");
		String UserID = "";
		while (true) {
			UserID = br.readLine();
			boolean check = true;
			for (ClientInfo ci : list) {
				if (ci.getId() != null && ci.getId().equals(UserID)) {
					check = false;
				}
			}
			if (check) {
				clientInfo.setId(UserID);
				myWriter.println("사용가능한 아이디입니다.");
				break;
			} else {
				myWriter.println("중복된 아이디입니다. 다시입력하세요.");
			}
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == null &&  list.get(0).getSocket().isConnected())
				continue;
			out = list.get(i).getSocket().getOutputStream();
			writer = new PrintWriter(out, true);
			writer.println("[" + UserID + "]님이 접속하셨습니다.");
		}
		System.out.println("[" + UserID + "]님이 접속하셨습니다.");

		if (clientInfo.isChief())
			myWriter.println("당신은 방장입니다.\n방장 권한과 명령어를 보시리면 #help를 입력하세요.");

		return UserID;
	}

	private void end(OutputStream out, PrintWriter writer, String UserID) throws IOException {
		list.remove(clientInfo);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == null && ! list.get(0).getSocket().isConnected())
				continue;
			out = list.get(i).getSocket().getOutputStream();
			writer = new PrintWriter(out, true);
			writer.println("[" + UserID + "]님이 접속을 종료하셨습니다.");
		}
		if (clientInfo.isChief()) {
			cheif = false;
			if(list.size() > 0) {
			list.get(0).setChief(true);
				if (list.get(0).getId() != null && list.get(0).getSocket().isConnected()) {
					out = list.get(0).getSocket().getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println("방장이 되었습니다. 방장명령어를 #help를 통해 확인하세요.");
				}
			}
		}

		System.out.println("[" + UserID + "]님이 접속을 종료하셨습니다.");
	}

	private void command(String inputLine, OutputStream out, PrintWriter myWriter, PrintWriter writer)
			throws IOException {
		if (inputLine.length() > 2 && inputLine.substring(0, 2).equals("/w")) {
			boolean check = true;
			String str[] = inputLine.split(" ");
			if (str.length < 3) {
				myWriter.println("귓속말을 사용하시려면 '/w ID MSG'형식을 사용하세요.");
				return;
			}
			if (str[1].equals(clientInfo.getId())) {
				myWriter.println("본인에게는 귓속말을 보낼수 없습니다.");
				return;
			}
			for (ClientInfo ci : list) {
				if (ci.getId().equals(str[1])) {
					myWriter.println(str[1] + "<<" + inputLine.substring(2 + str[0].length() + str[1].length()));
					out = ci.getSocket().getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println(
							clientInfo.getId() + ">>" + inputLine.substring(2 + str[0].length() + str[1].length()));
					check = false;
				}
			}
			if (check)
				myWriter.println(str[1] + "이라는 ID을 사용하는 사용자가 없습니다.");
		} else if (inputLine.equals("/help")) {
			String msg="/help: 일반 명령어";
			msg+="\n/member: 채팅방 멤버 보기";
			msg+="\n/w ID msg : 귓속말";
			msg+="\n#help : 방장 명령어";
			msg+="\n접속종료!! : 접속종료";
			myWriter.println(msg);
		} else if (inputLine.equals("/member")) {
			String member;
			for (ClientInfo ci : list) {
				if (ci.getId() == null) {
					continue;
				}
				member = ci.getId();
				if (ci.isChief()) {
					member += "(방장)";
				}
				myWriter.println(member);
			}
			myWriter.println("만약 (방장)표시가 없다면 #을 입력하세요. 제일 먼저 들어온사람에게 방장권한이 부여됩니다.");
		} else {
			myWriter.println("옳지 못한 명령어 입니다. 명령어를 보시려면 /help를 입력하세요.");
		}
		return;
	}

	private void chiefCommand(String inputLine, OutputStream out, PrintWriter myWriter, PrintWriter writer)
			throws IOException {
		if(!cheif) {
			list.get(0).setChief(true);
			out = list.get(0).getSocket().getOutputStream();
			writer = new PrintWriter(out, true);
			writer.println("방장이 없어 방장이 되었습니다. 방장명령어를 #help를 통해 확인하세요.");
			cheif=true;
			if(!list.get(0).getId().equals(clientInfo.getId()))
				myWriter.println("옳지 못한 명령어 입니다. 방장 명령어를 보시려면 #help를 입력하세요.");
			return;
		}
		if (!clientInfo.isChief()) {
			myWriter.println("방장이 아닙니다. 방장 명령어를 사용 하실 수 없습니다.");
			return;
		}
		if (inputLine.equals("#help")) {
			String msg = "#room : 방 인원수 조회";
			msg+="\n#chMax n: 방 최대 인원수 n으로 변경";
			msg+="\n#out ID: ID 강퇴";
			myWriter.println(msg);
		} else if (inputLine.equals("#room")) {
			int count = 0;
			for (ClientInfo ci : list) {
				if (ci.getId() == null)
					count++;
			}
			myWriter.println("인원 : " + list.size() + "/" + maxsize + "중 id 생성중인 인원은 " + count + "명입니다.");
			return;
		} else if (inputLine.length() >= 6 && inputLine.substring(0, 6).equals("#chMax")) {
			String str[] = inputLine.split(" ");
			if (str.length != 2) {
				myWriter.println("최대인원수를 설정하시려면 '#chMax n'형식을 사용하세요.");
				return;
			}
			try {
				int num = Integer.parseInt(str[1]);
				if (num < list.size()) {
					myWriter.println(list.size() + "보다 큰 수로 n을 설정해주세요.");
					return;
				} else if( num >= 11) {
					myWriter.println("채팅방의 최대 인원수는 10명을 초과할수 없습니다.");
					return;
				}
				maxsize = num;
				myWriter.println(maxsize + "명으로 채팅방의 최대인원수가 변경되었습니다.");
				return;
			} catch (Exception e) {
				myWriter.println("'#chMax n'의 n은 숫자를 입력하세요.형식을 사용하세요.");
				return;
			}
		} else if (inputLine.length() >=4 && inputLine.substring(0, 4).equals("#out")) {
			String str[] = inputLine.split(" ");
			if (str.length < 2) {
				myWriter.println("강퇴 기능을 사용하시려면 '#out ID'형식을 사용하세요.");
				return;
			}
			String id = inputLine.substring(5);
			if (id.equals(clientInfo.getId())) {
				myWriter.println("본인은 강퇴할 수 없습니다.");
				return;
			}
			boolean check=false;
			for (ClientInfo ci : list) {
				if (id.equals(ci.getId())) {
					check=true;
					continue;
				}
			}
			if(check) {
				for (ClientInfo ci : list) {
					if (id.equals(ci.getId())) {
						out = ci.getSocket().getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println("#out");
					}else {
						out = ci.getSocket().getOutputStream();
						writer = new PrintWriter(out, true);
						writer.println(id+"님이 강퇴 당하셨습니다.");
					}
				}
			}else {
				myWriter.println(id+"가 존재하지않습니다. 다시 확인해주세요.");
			}
		} 
		else {
			myWriter.println("옳지 못한 명령어 입니다. 방장 명령어를 보시려면 #help를 입력하세요.");
		}
	}
}