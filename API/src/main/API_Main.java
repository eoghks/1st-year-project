package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import model.Mapping;
import model.Url;
import service.API_Serive;
import service.MybatisService;

public class API_Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		API_Serive apiService = new API_Serive();
		MybatisService mybatisService = new MybatisService();
		System.out.printf("공공 API 요청 주소 : ");
		String url = sc.next();
		// URL정보 확인 process
		Url urlInfo = mybatisService.getUrlInfo(url);
		HashMap<String, Object> settingInfo = new HashMap<String, Object>();
		if (urlInfo != null) {
			settingInfo = apiService.ShowUrlInfo(urlInfo);
		} else {
			apiService.addUrl(url);// URL 등록 프로세스(생략)
			return;// 구현을 하지 않아 프로그램을 종료하도록 막아놓았습니다.
		}
		String result="";
		try {
			result=apiService.requestAPI(urlInfo, settingInfo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		String parent = mybatisService.getParent(urlInfo);
		List<Mapping> mappingInfo = mybatisService.getMaping(urlInfo);
		ArrayList<Object> insertList = new ArrayList<Object>();
		try {
			insertList=apiService.parseResponse(urlInfo, result, parent, mappingInfo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		mybatisService.save(urlInfo, insertList, mappingInfo);
		System.out.println("다운로드 완료");
	}
}
