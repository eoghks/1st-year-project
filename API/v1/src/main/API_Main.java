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
		API_Serive apiService = new API_Serive();
		MybatisService mybatisService = new MybatisService();
		String url = "http://infuser.odcloud.kr/oas/docs";
		System.out.println("공공 API 요청 주소 : " + url);
		Url urlInfo = mybatisService.getUrlInfo(url);
		HashMap<String, Object> settingInfo = new HashMap<String, Object>();
		if (urlInfo != null) {
			try {
				settingInfo = apiService.ShowUrlInfo(urlInfo);
			}catch (Exception e) {
				System.out.println(e.getMessage());
				return;
			}
		} else {
			System.out.println("This url is not registered.");
			return;
		}
		String result="";
		try {
			result=apiService.requestAPI(urlInfo, settingInfo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println(result);
	}
}
