package service;

import java.util.List;

import dto.HostsDTO;
import dto.SearchDTO;
import vo.Cloud;

public interface CloudService {
	public List<Cloud> selectCloud(SearchDTO searchDTO) throws Exception;

	public List<Cloud> deleteCloud(HostsDTO hostDto) throws Exception;
}
