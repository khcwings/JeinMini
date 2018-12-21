package com.jein.mini.biz.common.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jein.mini.biz.common.domain.CommonUser;

@Repository
public interface CommonUserRepository extends CrudRepository<CommonUser, Long>{
	public long countByUserIdAndUserPwd(String userId, String userPwd);
	public CommonUser findOneByUserId(String userId);
}
