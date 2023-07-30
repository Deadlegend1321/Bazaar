package com.mudit.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mudit.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer>, PagingAndSortingRepository<Country, Integer> {

	public List<Country> findAllByOrderByNameAsc();
}
