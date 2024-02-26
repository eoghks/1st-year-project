package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.HostsDTO;
import dto.PropertyDTO;
import dto.SearchDTO;
import service.CloudService;
import vo.Cloud;

@Controller
public class CloudController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CloudService service;
	@Autowired
	private LocaleResolver localeResolver;
	
	public CloudController() {
		logger.info("MainController");
		logger.info("start test Server...");
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String manage(Model model, @RequestParam(value="lang", required=false, defaultValue="")String lang, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("manage");
		if(lang.equals("")) {
			lang="ko_KR";
			Locale locale = new Locale(lang);
			localeResolver.setLocale(request, response, locale);
		}
		List<Cloud> cloudList = new ArrayList<Cloud>();
		try {
			cloudList = service.selectCloud(new SearchDTO());
		} catch (Exception e) {
			logger.info("\n\n\n 홈 오류발생\n\n\n");
			model.addAttribute("exception", e);
			return "error";
		}
		String jsonCloudList = new ObjectMapper().writeValueAsString(cloudList);
		model.addAttribute("cloudList", jsonCloudList);
		model.addAttribute("lang", lang);
		
		return "manageCloud";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<Cloud> search(@RequestBody SearchDTO searchDto, Model model) throws Exception {
		logger.info("serach");
		List<Cloud> cloudList = new ArrayList<Cloud>();
		try {
			cloudList = service.selectCloud(searchDto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n\n\n 검색 오류발생\n\n\n");
			return null;
		}
		return cloudList;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public List<Cloud> delete(@RequestBody  HostsDTO hostDto, Model model) throws Exception {
		logger.info("delete");
		List<Cloud> cloudList = new ArrayList<Cloud>();
		if (hostDto.getHost_names().size() == 0) {
			cloudList = service.selectCloud(new SearchDTO());
			return cloudList;
		}
		try {
			cloudList=service.deleteCloud(hostDto);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return cloudList;
	}

//	@RequestMapping(value = "/property", method = RequestMethod.GET)
//	public String property(HostDTO hostDTO,  @RequestParam(value="type")String type,Model model) throws Exception {
//		logger.info("property");
//		Locale lang =LocaleContextHolder.getLocale();
//		model.addAttribute("lang", lang);
//		model.addAttribute("type",type);
//		PropertyVO property = new PropertyVO();
//		if(hostDTO.getHost_name()=="") {
//		}
//		if(hostDTO.getHost_name()!=""){
//			try {
//			property = service.propertyCloud(hostDTO);
//			} catch (Exception e) {
//			logger.info("\n\n\n 속성 오류발생\n\n\n");
//			// return property;
//			}
//		}
//		String json_property = new ObjectMapper().writeValueAsString(property);
//		model.addAttribute("property", json_property);
//		return "propertyCloud";
//	}
//
//	@RequestMapping(value = "/process", method = RequestMethod.POST)
//	@ResponseBody
//	public String process(@RequestBody PropertyDTO propertyDto, @RequestParam(value="type")String type, Model model) throws Exception {
//		logger.info("update cloud");
//		try {
//			System.out.println(type);
//			if(type.equals("new")) {
//				service.insertCloud(propertyDto);
//			}else if(type.equals("update")){
//				service.updateCloud(propertyDto);
//			}
//		} catch (Exception e) {
//			logger.info("\n\n\n Process 오류발생\n\n\n");
//			return "error";
//		}
//		return "success";
//	}

}