package com.mudit.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mudit.common.entity.setting.Setting;
import com.mudit.common.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String>, PagingAndSortingRepository<Setting, String> {

	
	public List<Setting> findByCategory(SettingCategory category);

	@Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
	public List<Setting> findByTwoCategories(SettingCategory catOne, SettingCategory catTwo);
}
