package service;

import java.util.List;

import dto.HostDTO;
import dto.HostsDTO;
import dto.PropertyDTO;
import dto.SearchDTO;
import vo.CloudVO;
import vo.PropertyVO;

public interface CloudService {
	public List<CloudVO> searchCloud(SearchDTO searchDTO) throws Exception;

	public void deleteCloud(HostsDTO hostsDto) throws Exception;

	public void insertCloud(PropertyDTO propertyDto) throws Exception;

	public PropertyVO propertyCloud(HostDTO hostDTO) throws Exception;

	public void updateCloud(PropertyDTO propertyDto) throws Exception;
}
