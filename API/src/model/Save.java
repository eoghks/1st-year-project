package model;

import java.util.ArrayList;

public class Save {
	private int id;
	private ArrayList<Object> data;
	private ArrayList<String> col;
	
	public ArrayList<String> getCol() {
		return col;
	}
	public void setCol(ArrayList<String> col) {
		this.col = col;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Object> getData() {
		return data;
	}
	public void setData(ArrayList<Object> data) {
		this.data = data;
	}
}
