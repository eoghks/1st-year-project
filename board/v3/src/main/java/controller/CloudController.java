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

import dto.CloudDto;
import dto.HostDto;
import dto.HostsDto;
import dto.PageDto;
import dto.PropertyDto;
import dto.ResultDto;
import dto.SearchDto;
import service.CloudService;
import service.CloudService2;
import service.CloudService3;

@Controller
public class CloudController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CloudService service;
	
	@Autowired
	private CloudService2 service2;
	
	@Autowired
	private CloudService3 service3;
	@Autowired
	private LocaleResolver localeResolver;

	public CloudController() {
		logger.info("MainController");
		logger.info("start test Server...");
	}
	/********** END ************/
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String manage(Model model, @RequestParam(value = "lang", required = false, defaultValue = "") String lang,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("manage");
		if (lang.equals("")) {
			lang = "ko_KR";
			Locale locale = new Locale(lang);
			localeResolver.setLocale(request, response, locale);
		}
		List<CloudDto> cloudList = new ArrayList<CloudDto>();
		try {
			//cloudList = service.selectCloud(new SearchDto());
			//cloudList = service2.selectCloud1(new SearchDto());
			//cloudList = service2.selectCloud2(new SearchDto());
			//cloudList = service2.selectCloud3();
			//cloudList = service2.searchCloud(new SearchDto());
			/************************************************/
			//cloudList = service3.selectCloud1(new SearchDto());
			cloudList = service3.selectCloud3(new SearchDto());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n\n\n 홈 오류발생\n\n\n");
			model.addAttribute("exception", e);
			return "error";
		}
		ResultDto result = new ResultDto();
		result.setData(cloudList);
		String jsonResult = new ObjectMapper().writeValueAsString(result);
		model.addAttribute("result", jsonResult);
		model.addAttribute("lang", lang);
		return "manageCloud";
	}

	/********** END ************/
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto search(@RequestBody SearchDto searchDto, Model model) throws Exception {
		logger.info("serach");
		List<CloudDto> cloudList = new ArrayList<CloudDto>();
		try {
//			cloudList = service.selectCloud(searchDto);
			//cloudList = service2.searchCloud(searchDto);
			cloudList = service3.searchCloud(searchDto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n\n\n 검색 오류발생\n\n\n");
			return null;
		}
		ResultDto result = new ResultDto();
		result.setData(cloudList);
		return result;
	}
	/*********Paging*********/
	@RequestMapping(value = "/page", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto page(@RequestBody PageDto pageDto,Model model) throws Exception {
		logger.info("page");
		List<CloudDto> cloudList = new ArrayList<CloudDto>();
		try {
//			cloudList = service.selectCloud(searchDto);
			cloudList = service2.page(pageDto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n\n\n 검색 오류발생\n\n\n");
			return null;
		}
		ResultDto result = new ResultDto();
		result.setData(cloudList);
		return result;
	}
	
	/********** END ************/
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto delete(@RequestBody HostsDto hostDto, Model model) throws Exception {
		logger.info("delete");
		List<CloudDto> cloudList = new ArrayList<CloudDto>();
		try {
			//cloudList = service2.deleteCloud1(hostDto.getHost_names());
			//cloudList = service.deleteCloud(hostDto.getHost_names());
			cloudList = service3.deleteCloud(hostDto.getHost_names());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		ResultDto result = new ResultDto();
		result.setData(cloudList);
		return result;
	}

	/********** END ************/
	@RequestMapping(value = "/property", method = RequestMethod.GET)
	public String property(HostDto hostDTO, @RequestParam(value="type")String type, Model model) throws Exception {
		logger.info("property");
		Locale lang = LocaleContextHolder.getLocale();
		model.addAttribute("lang", lang);
		model.addAttribute("type",type);
		PropertyDto property = new PropertyDto(); // ->find 후 리턴
		if (hostDTO.getHost_name() == "") {
		}
		if (hostDTO.getHost_name() != "") {
			try {
//				property = service2.findCloud1(hostDTO.getHost_name());
				property = service3.findCloud(hostDTO.getHost_name());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("\n\n\n 속성 오류발생\n\n\n");
				return "error";
			}
		}
		ResultDto result = new ResultDto();
		result.setData(property);
		String jsonResult = new ObjectMapper().writeValueAsString(result);
		model.addAttribute("property", jsonResult);
		return "propertyCloud";
	}

	
	@RequestMapping(value = "/process", method = RequestMethod.POST)
	@ResponseBody
	public String process(@RequestBody PropertyDto propertyDto, Model model)
			throws Exception {
		logger.info("update cloud");
		try {
			service3.save(propertyDto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n\n\n Process 오류발생\n\n\n");
			return "error";
		}
		return "success";
	}

}