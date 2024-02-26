package service;

import java.util.ArrayList;
import java.util.List;

import dto.CloudDto;
import dto.CloudDto2;
import dto.PageDto;
import dto.PropertyDto;
import dto.SearchDto;

public interface CloudService2 {

	public List<CloudDto> selectCloud1() throws Exception;

	public List<CloudDto> selectCloud2() throws Exception;

	public List<CloudDto> selectCloud3() throws Exception;

	public List<CloudDto> searchCloud(SearchDto searchDto) throws Exception;
	
	public List<CloudDto> page(PageDto pageDto) throws Exception;

	public PropertyDto findCloud1(String host) throws Exception;
	
	public PropertyDto findCloud2(String host) throws Exception;

	public void save(PropertyDto propertyDto) throws Exception;
	
	public void save2(PropertyDto propertyDto) throws Exception;

	public List<CloudDto> deleteCloud1(ArrayList<String> host_names) throws Exception;
	
	public List<CloudDto> deleteCloud2(ArrayList<String> host_names) throws Exception;
	

	// public void test();
}
