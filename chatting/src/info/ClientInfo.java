package info;

import java.net.Socket;

public class ClientInfo {
	Socket socket;
	String id;
	boolean chief=false;
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isChief() {
		return chief;
	}
	public void setChief(boolean chief) {
		this.chief = chief;
	}
	
}
