package com.jein.mini.biz.common.persistence;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jein.mini.biz.common.domain.CommonCode;

public interface CommonCodeRepository extends CrudRepository<CommonCode, Long>{
	List<CommonCode> findByCodeLevelOrderByIdAsc(int codeLevel);
}
