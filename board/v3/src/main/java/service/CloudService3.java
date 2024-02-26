package service;

import java.util.ArrayList;
import java.util.List;

import dto.CloudDto;
import dto.PropertyDto;
import dto.SearchDto;

public interface CloudService3 {

	public List<CloudDto> selectCloud1(SearchDto searchDTO) throws Exception;
	
	public List<CloudDto> selectCloud2(SearchDto searchDTO) throws Exception;
	
	public List<CloudDto> selectCloud3(SearchDto searchDTO) throws Exception;

	public PropertyDto findCloud(String host) throws Exception;

	public void save(PropertyDto propertyDto) throws Exception;

	public List<CloudDto> deleteCloud(ArrayList<String> host_names) throws Exception;

	public List<CloudDto> searchCloud(SearchDto searchDto) throws Exception;

}
