package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.HostDTO;
import dto.PropertyDTO;
import dto.SearchDTO;
import dto.HostsDTO;
import service.CloudService;
import vo.CloudVO;
import vo.PropertyVO;

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
		if(lang=="") {
			lang="ko_KR";
			Locale locale = new Locale(lang);
			localeResolver.setLocale(request, response, locale);
		}
		List<CloudVO> cloudList = new ArrayList<CloudVO>();
		try {
			cloudList = service.searchCloud(new SearchDTO());
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
	public List<CloudVO> search(@RequestBody SearchDTO searchDto, Model model) throws Exception {
		logger.info("serach");
		List<CloudVO> cloudList = new ArrayList<CloudVO>();
		try {
			cloudList = service.searchCloud(searchDto);
		} catch (Exception e) {
			logger.info("\n\n\n 검색 오류발생\n\n\n");
			return null;
		}
		return cloudList;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestBody  HostsDTO hostDto, Model model) throws Exception {
		logger.info("delete");
		if (hostDto.getHost_names().size() == 0) {
			return "success";
		}
		try {
			service.deleteCloud(hostDto);
		} catch (Exception e) {
			logger.info("\n\n\n 삭제 오류발생\n\n\n");
			return "error";
		}
		return "success";
	}

	@RequestMapping(value = "/property", method = RequestMethod.GET)
	public String property(HostDTO hostDTO,  @RequestParam(value="type")String type,Model model) throws Exception {
		logger.info("property");
		Locale lang =LocaleContextHolder.getLocale();
		model.addAttribute("lang", lang);
		model.addAttribute("type",type);
		PropertyVO property = new PropertyVO();
		if(hostDTO.getHost_name()=="") {
		}
		if(hostDTO.getHost_name()!=""){
			try {
			property = service.propertyCloud(hostDTO);
			} catch (Exception e) {
			logger.info("\n\n\n 속성 오류발생\n\n\n");
			// return property;
			}
		}
		String json_property = new ObjectMapper().writeValueAsString(property);
		model.addAttribute("property", json_property);
		return "propertyCloud";
	}

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	@ResponseBody
	public String process(@RequestBody PropertyDTO propertyDto, @RequestParam(value="type")String type, Model model) throws Exception {
		logger.info("update cloud");
		try {
			System.out.println(type);
			if(type.equals("new")) {
				service.insertCloud(propertyDto);
			}else if(type.equals("update")){
				service.updateCloud(propertyDto);
			}
		} catch (Exception e) {
			logger.info("\n\n\n Process 오류발생\n\n\n");
			return "error";
		}
		return "success";
	}

	//개발 종료시 아래 코드 삭제
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	public String test(@RequestBody HostsDTO test, Model model) throws Exception {
		logger.info("test");
		return "success";
	}
}