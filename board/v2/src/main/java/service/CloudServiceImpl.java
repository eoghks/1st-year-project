package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.CloudMapping;
import dto.HostsDTO;
import dto.SearchDTO;
import entity.AccessKey;
import entity.Namespace;
import repositories.AccessKeyRepository;
//import repositories.CloudRepository;
import repositories.NamespaceRepository;
import vo.Cloud;

@Service
@Transactional
public class CloudServiceImpl implements CloudService {
	@Autowired
	private AccessKeyRepository accessKeyRepository;

	@Autowired
	private NamespaceRepository namespaceRepository;

	@Transactional
	public List<Cloud> selectCloud(SearchDTO searchDto) throws Exception {
		List<Cloud> results = new ArrayList<Cloud>();
		List<AccessKey> accessKeys = new ArrayList<AccessKey>();
		List<Namespace> namespaces = new ArrayList<Namespace>();
		if (searchDto.getType() == null || searchDto.getName() == "") {
			accessKeys = accessKeyRepository.findAll();
			namespaces = namespaceRepository.findAll();
			results = makeCloud(accessKeys, namespaces);
		} else {
			results = searchCloud(searchDto);
		}

		return results;
	}

	private List<Cloud> makeCloud(List<AccessKey> accessKeys, List<Namespace> namespaces) {
		HashMap<String, Integer> map = countNamespace(namespaces);
		List<Cloud> results = new ArrayList<Cloud>();
		for (AccessKey ac : accessKeys) {
			Cloud cloud = new Cloud();
			cloud.setHost_name(ac.getHostName());
			cloud.setScan_type(ac.getScanType());
			cloud.setAccess_key_id(ac.getAccessKeyId());
			cloud.setScan_key(ac.getScanKey());
			cloud.setRegion(ac.getRegion());
			if (map.containsKey(ac.getHostName())) {
				cloud.setNamespaces(map.get(ac.getHostName()));
			} else {
				cloud.setNamespaces(0);
			}
			results.add(cloud);
		}
		return results;
	}

	private HashMap<String, Integer> countNamespace(List<Namespace> namespaces) {

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Namespace na : namespaces) {
			String host_name = na.getPk().getHost();
			if (map.containsKey(host_name)) {
				map.replace(host_name, map.get(host_name) + 1);
			} else {
				map.put(host_name, 1);
			}
		}
		return map;
	}

	private List<Cloud> searchCloud(SearchDTO searchDto) throws Exception {
		String type = searchDto.getType();
		String name = searchDto.getName();
		List<Cloud> results = new ArrayList<Cloud>();
		List<AccessKey> accessKeys = new ArrayList<AccessKey>();
		List<Namespace> namespaces = new ArrayList<Namespace>();
		if (type.equals("host_name")) {
			accessKeys = accessKeyRepository.findByHostNameContaining(name);
			namespaces = namespaceRepository.findByPkHostContaining(name);
		} else {
			List<CloudMapping> hosts = new ArrayList<CloudMapping>();
			List<String> hostNames = new ArrayList<String>();
			if (type.equals("scan_type")) {
				hosts = accessKeyRepository.findByScanType(Integer.parseInt(name));
			} else if (type.equals("scan_key")) {
				hosts = accessKeyRepository.findByScanKeyContaining(name);
			} else if (type.equals("access_key_id")) {
				hosts = accessKeyRepository.findByAccessKeyIdContaining(name);
			} else if (type.equals("region")) {
					if(name != null && name.equals("0")) {
						name = "seoul";
					} else if(name!= null && name.equals("1")) {
						name = "usa";
					}
				hosts = accessKeyRepository.findByRegionContaining(name);
			} else if (type.equals("namespaces")) {
				hostNames = getHosts(Integer.parseInt(name));
			} else {
				throw new Exception("잘못된 검색 타입");
			}

			for (CloudMapping cm : hosts) {
				hostNames.add(cm.getHostName());
			}
			System.out.println(hostNames);
			accessKeys = accessKeyRepository.findByHostNameIn(hostNames);
			namespaces = namespaceRepository.findByPkHostIn(hostNames);
		}
		results = makeCloud(accessKeys, namespaces);
		return results;
	}

	private List<String> getHosts(int name) {
		List<String> hostNames = new ArrayList<String>();
		List<Namespace> namespaces = new ArrayList<Namespace>();
		namespaces = namespaceRepository.findAll();
		HashMap<String, Integer> map = countNamespace(namespaces);
		for (String key : map.keySet()) {
			if (map.get(key) == name) {
				hostNames.add(key);
			}
		}
		return hostNames;
	}

	@Override
	@Transactional
	public List<Cloud> deleteCloud(HostsDTO hostDto) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(hostDto.getHost_names());
		List<AccessKey> accessKeys = new ArrayList<AccessKey>();
		for (String host_name : hostDto.getHost_names()) {
			AccessKey ac = AccessKey.builder().hostName(host_name).build();
			accessKeys.add(ac);
		}

		accessKeyRepository.deleteAll(accessKeys);
		namespaceRepository.deleteByPkHostIn(hostDto.getHost_names());

		List<Cloud> results = selectCloud(new SearchDTO());
		return results;
	};

}
