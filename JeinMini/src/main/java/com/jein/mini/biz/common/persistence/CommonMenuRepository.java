package com.jein.mini.biz.common.persistence;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jein.mini.biz.common.domain.CommonMenu;


@Repository
public interface CommonMenuRepository extends CrudRepository<CommonMenu, Long>{
	public CommonMenu findOneByMenuUrl(String menuUrl);
	public Collection<CommonMenu> findByMenuUrl(String menuUrl); 
}
