package com.ai.paas.ipaas.mds.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsKafkaLoad;

public interface IMdsUserTopicCustomMapper {

//	@Select("SELECT MDS_CLUSTER_ID as clusterId,COUNT(USER_ID) AS userNum FROM"
//			+ " MDS_USER_TOPIC WHERE STATE=1 GROUP BY MDS_CLUSTER_ID")
	@Select("SELECT a.MDS_CLUSTER_ID AS clusterId, COUNT(a.USER_ID) AS userNum" +
			"  FROM MDS_USER_TOPIC a, mds_resource_pool b " +
			" WHERE a.STATE = 1 and b.cluster_state = 1 " +
			"   AND b.org_code = #{orgCode} " + 
			"   AND b.cluster_id = a.MDS_CLUSTER_ID " +
			" GROUP BY a.MDS_CLUSTER_ID")
	List<MdsKafkaLoad> getClusterLoad(String orgCode);

}