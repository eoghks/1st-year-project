package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.CloudDAO;
import dto.HostDTO;
import dto.HostsDTO;
import dto.PropertyDTO;
import dto.SearchDTO;
import vo.CloudVO;
import vo.NamespaceVO;
import vo.PropertyVO;

@Service
public class CloudServiceImpl implements CloudService {
	@Autowired
	private CloudDAO dao;
	@Autowired
	SqlSessionFactory SqlSessionFactory;

	@Override
	public List<CloudVO> searchCloud(SearchDTO searchDTO) throws Exception {
		SqlSession SqlSession = SqlSessionFactory.openSession();
		if(searchDTO.getType()!= null && searchDTO.getType().equals("region")) {
			if(searchDTO.getName()!= null && searchDTO.getName().equals("0")) {
				searchDTO.setName("seoul");
			} else if(searchDTO.getName()!= null && searchDTO.getName().equals("1")) {
				searchDTO.setName("usa");
			}
		}
			
		List<CloudVO> result = new ArrayList<CloudVO>();
		try {
			result = dao.selectCloudList(SqlSession, searchDTO);
		} finally {
			SqlSession.close();
		}
		return result;
	}

	@Override
	@Transactional
	public void deleteCloud(HostsDTO hostsDto) throws Exception {
		SqlSession SqlSession = SqlSessionFactory.openSession();
		try {
			List<String> tableNameList = Arrays.asList("cloud_account_key", "cloud_namespace");
			for (String tableName : tableNameList) {
				hostsDto.setTable(tableName);
				dao.delete(SqlSession, hostsDto);
			}
		} finally {
			SqlSession.close();
		}
	}

	@Override
	@Transactional
	public void insertCloud(PropertyDTO propertyDto) throws Exception {
		SqlSession SqlSession = SqlSessionFactory.openSession();
		try {
			dao.insertAccount(SqlSession, propertyDto);
			if (propertyDto.getNamespaces().size() > 0)
				dao.insertNamespace(SqlSession, propertyDto);
		} finally {
			SqlSession.close();
		}
	}

	@Override

	public PropertyVO propertyCloud(HostDTO hostDTO) throws Exception {
		SqlSession SqlSession = SqlSessionFactory.openSession();
		PropertyVO property = new PropertyVO();
		try {
			property = dao.selectPropertyAccountCloud(SqlSession, hostDTO);
			List<NamespaceVO> na = dao.selectPropertyNamespaceCloudList(SqlSession, hostDTO);
			ArrayList<String> namespaces = new ArrayList<String>();
			if (na.size() > 0) {
				for (NamespaceVO namespace : na) {
					namespaces.add(namespace.getNamespaces());
				}
			}
			property.setNameapces(namespaces);
		} finally {
			SqlSession.close();
		}
		return property;
	}

	@Override
	@Transactional
	public void updateCloud(PropertyDTO propertyDto) throws Exception {
		SqlSession SqlSession = SqlSessionFactory.openSession();
		try {
			dao.updateAccountCloud(SqlSession, propertyDto);
			HostsDTO hostsDto = new HostsDTO();
			ArrayList<String> host_names = new ArrayList<String>();
			host_names.add(propertyDto.getHost_name());
			hostsDto.setHost_names(host_names);
			hostsDto.setTable("cloud_namespace");
			dao.delete(SqlSession, hostsDto);
			if (propertyDto.getNamespaces().size() > 0)
				dao.insertNamespace(SqlSession, propertyDto);
		} finally {
			SqlSession.close();
		}
	}

}
