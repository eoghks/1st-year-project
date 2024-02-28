package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import model.Body;
import model.Header;
import model.Mapping;
import model.Url;

public class API_Serive {
	MybatisService mybatisService = new MybatisService();
	Scanner sc = new Scanner(System.in);

	public HashMap<String, Object> ShowUrlInfo(Url urlInfo) {
		int id = urlInfo.getUrlId();
		int method = urlInfo.getMethod();
		int extension = urlInfo.getExtension();
		List<Header> header = mybatisService.getHeader(id);
		List<Body> body = mybatisService.getBody(id);
		System.out.printf("Method : ");
		if (method == 0) {
			System.out.println("GET");
		} else if (method == 1) {
			System.out.println("POST");
		}
		System.out.printf("Extension : ");
		if (extension == 0) {
			System.out.println("XML");
		} else {
			System.out.println("JSON");
		}
		System.out.println("------- Header -------");
		for (Header h : header) {
			System.out.println(h.getKey() + " : " + h.getValue());
		}
		System.out.println("------- Body -------");
		for (Body b : body) {
			System.out.println(b.getKey() + " : " + b.getValue() + "-->" + b.getIsList());
		}
		String msg = "해당 정보가 맞으면 'Y' 또는 'y'를 아니면 'N' 또는 'n'을 입력해주세요. ";
		while (true) {
			System.out.printf(msg);
			String answer = sc.next();
			if (answer.equals("Y") || answer.equals("y")) {
				break;
			} else if (answer.equals("N") || answer.equals("n")) {
				reviseInfo();
				break;
			} else {
				msg = "잘못 입력하셨습니다. 해당 정보가 맞으면 'Y' 또는 'y'를 아니면 'N' 또는 'n'을 입력해주세요. ";
			}
		}
		HashMap<String, Object> settingInfo = new HashMap<String, Object>();
		settingInfo.put("header", header);
		settingInfo.put("body", body);
		return settingInfo;
	}

	private void reviseInfo() {
		System.out.println("수정 프로세스");
	}

	public void addUrl(String url) {
		System.out.println("URL 등록 프로레스 ---> 구현 x");
	}

	public String requestAPI(Url urlInfo, HashMap<String, Object> settingInfo) throws Exception {
		String result = null;
		if (urlInfo.getMethod() == 0) {
			result = GetAPISetting(urlInfo, settingInfo);
		} else {
			result = PostAPISetting(urlInfo, settingInfo);
		}
		return result;
		
	}

	@SuppressWarnings("unchecked")
	private String PostAPISetting(Url urlInfo, HashMap<String, Object> settingInfo) throws Exception {

		List<Header> header = (List<Header>) settingInfo.get("header");
		List<Body> body = (List<Body>) settingInfo.get("body");
		String url = urlInfo.getUrl();
		HttpPost httpPost = new HttpPost(url);
		// 헤더 입력
		for (Header h : header) {
			httpPost.addHeader(h.getKey(), h.getValue());
		}
		// body 입력
		StringBuilder jsonBuilder = new StringBuilder().append("{");
		for (int i = 0; i < body.size(); i++) {
			jsonBuilder.append("\"" + body.get(i).getKey() + "\": ");
			if (body.get(i).getIsList() == 1) {
				jsonBuilder.append(body.get(i).getValue());
			} else {
				jsonBuilder.append("\"" + body.get(i).getValue() + "\"");
			}
			if (i + 1 < body.size()) {
				jsonBuilder.append(",");
			}
		}
		jsonBuilder.append("}");
		String json = jsonBuilder.toString();
		httpPost.setEntity(new StringEntity(json));
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int status = httpResponse.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			String resultJson = EntityUtils.toString(httpResponse.getEntity());
			return resultJson;
		} else {
			throw new Exception("Info오류");
		}
	}

	@SuppressWarnings("unchecked")
	private String GetAPISetting(Url urlInfo, HashMap<String, Object> settingInfo) throws Exception {
		StringBuilder urlBuilder = new StringBuilder(urlInfo.getUrl());
		List<Header> header = (List<Header>) settingInfo.get("header");
		List<Body> body = (List<Body>) settingInfo.get("body");
		for (int i = 0; i < body.size(); i++) {
			if (i == 0) {
				urlBuilder
						.append("?" + URLEncoder.encode(body.get(i).getKey(), "UTF-8") + "=" + body.get(i).getValue());
			} else {
				urlBuilder
						.append("&" + URLEncoder.encode(body.get(i).getKey(), "UTF-8") + "=" + body.get(i).getValue());
			}
		}
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		for (Header h : header) {
			conn.setRequestProperty(h.getKey(), h.getValue());
		}
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			throw new Exception("Info오류");
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		return sb.toString();
	}

	public ArrayList<Object> parseResponse(Url urlInfo, String result, String parent, List<Mapping> mappingInfo) throws Exception{
		ArrayList<Object> insertList = new ArrayList<Object>();
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		ArrayList<String> colList = new ArrayList<String>();
		ArrayList<String> responseList = new ArrayList<String>();
		for (Mapping m : mappingInfo) {
			responseList.add(m.getResponseName());
			colList.add(m.getColName());
			typeList.add(m.getType());
		}
		///////////////////
		if (urlInfo.getExtension() == 0) {
			insertList = parsingXml(result, parent, typeList, colList, responseList);
		} else {
			insertList = parsingJson(result, parent, typeList, colList, responseList);
		}
		return insertList;
	}
	
	private ArrayList<Object> parsingJson(String result, String parent, ArrayList<Integer> typeList, ArrayList<String> colList, ArrayList<String> responseList) throws Exception {
		ArrayList<Object> insertList = new ArrayList<Object>();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(result);
		if(parent!= null) {
			if(json.get(parent).getClass().getName().equals("org.json.simple.JSONArray")) {
				JSONArray jsonList = (JSONArray) json.get(parent);
				for (int i = 0; i < jsonList.size(); i++) {
					JSONObject ele = (JSONObject) jsonList.get(i);
					String[] arr = new String[responseList.size()];
					for(int j=0; j<arr.length; j++) {
						arr[j]=ele.get(responseList.get(j)).toString();
					}
					ArrayList<Object> row = arrayToList(arr, typeList);
					insertList.add(row);
				}
			}
			else {
				json = (JSONObject) json.get(parent);
				String[] arr = new String[responseList.size()];
				for(int i=0; i<arr.length; i++) {
					arr[i]=json.get(responseList.get(i)).toString();
				}
				ArrayList<Object> row = arrayToList(arr, typeList);
				insertList.add(row);
			}
		}else {
			String[] arr = new String[responseList.size()];
			for(int i=0; i<arr.length; i++) {
				arr[i]=json.get(responseList.get(i)).toString();
			}
			ArrayList<Object> row = arrayToList(arr, typeList);
			insertList.add(row);
		}
		return insertList;

	}

	private ArrayList<Object> parsingXml(String result, String parent, ArrayList<Integer> typeList, ArrayList<String> colList, ArrayList<String> responseList) throws Exception {
		ArrayList<Object> insertList = new ArrayList<Object>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(result)));
		NodeList nodeList = document.getElementsByTagName(parent);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NodeList cNList = nodeList.item(i).getChildNodes();
			String[] arr = new String[responseList.size()];
			for (int j = 0; j < cNList.getLength(); j++) {
				if (responseList.contains(cNList.item(j).getNodeName())) {
					arr[responseList.indexOf(cNList.item(j).getNodeName())] = cNList.item(j).getTextContent();
				}
				
			}
			ArrayList<Object> row = arrayToList(arr, typeList);
			insertList.add(row);
		}
		return insertList;
	}

	private ArrayList<Object> arrayToList(String[] arr, ArrayList<Integer> typeList) throws Exception {
		ArrayList<Object> row= new ArrayList<Object>();
		for(int i=0; i<arr.length; i++) {
			if(typeList.get(i)==0) {
				String data ="'"+arr[i]+"'";
				row.add(data);
			}else if (typeList.get(i)==1) {
				try {
					row.add(Integer.parseInt(arr[i]));
				}catch(Exception e) {
					throw new Exception("String to Int error");
				}
			}else {
				throw new Exception("Type error");
			}
		}
		return row;
	}

}
