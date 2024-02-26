package service;

import java.util.ArrayList;
import java.util.List;

import dto.CloudDto;
import dto.PropertyDto;
import dto.SearchDto;

public interface CloudService {

	public List<CloudDto> selectCloud(SearchDto searchDTO) throws Exception;

	public PropertyDto findCloud(String host) throws Exception;

	public void save(PropertyDto propertyDto) throws Exception;

	public List<CloudDto> deleteCloud(ArrayList<String> host_names) throws Exception;

}
