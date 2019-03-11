package com.jein.mini.biz.common.persistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jein.mini.biz.common.domain.CommonUser;

@Repository
public interface CommonUserRepository extends CrudRepository<CommonUser, Long>{
	public long countByUserIdAndUserPwd(String userId, String userPwd);
	public CommonUser findOneByUserId(String userId);
	
	@Transactional
	@Modifying
	@Query(value="update cm_user set delete_yn = ?1, update_id = ?2, update_dt = now() where user_id = ?3", nativeQuery = true)
	public void deleteUser(String deleteYn, String updateId, String userId);
}
