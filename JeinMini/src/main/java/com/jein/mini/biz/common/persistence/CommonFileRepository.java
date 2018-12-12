package com.jein.mini.biz.common.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jein.mini.biz.common.domain.CommonFile;

@Repository
public interface CommonFileRepository extends CrudRepository<CommonFile, Long>{
	public CommonFile findOneByFileId(String fileId);
}
