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

	public HashMap<String, Object> ShowUrlInfo(Url urlInfo) throws Exception {
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
		if(header != null && header.size() > 0 ) {
			System.out.println("------- Header -------");
			for (Header h : header) {
				System.out.println(h.getKey() + " : " + h.getValue());
			}
		}
		
		if(body != null && body.size() > 0) {
			System.out.println("------- Body -------");
			for (Body b : body) {
				System.out.println(b.getKey() + " : " + b.getValue() + "-->" + b.getIsList());
			}
		}
		String msg = "해당 정보가 맞으면 'Y' 또는 'y'를 아니면 'N' 또는 'n'을 입력해주세요. ";
		while (true) {
			System.out.printf(msg);
			String answer = sc.next();
			if (answer.equals("Y") || answer.equals("y")) {
				break;
			} else if (answer.equals("N") || answer.equals("n")) {
				throw new Exception("url not exist");
			} else {
				msg = "잘못 입력하셨습니다. 해당 정보가 맞으면 'Y' 또는 'y'를 아니면 'N' 또는 'n'을 입력해주세요. ";
			}
		}
		HashMap<String, Object> settingInfo = new HashMap<String, Object>();
		settingInfo.put("header", header);
		settingInfo.put("body", body);
		return settingInfo;
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
}
