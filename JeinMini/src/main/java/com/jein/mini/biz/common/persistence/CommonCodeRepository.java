package com.jein.mini.biz.common.persistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.jein.mini.biz.common.domain.CommonCode;

public interface CommonCodeRepository extends CrudRepository<CommonCode, Long>{
	@Transactional
	@Modifying
	@Query(value="delete from cm_code where code_grp_id = ?1", nativeQuery=true)
	public void deleteCodeGrpId(String codeGrpId);
}
