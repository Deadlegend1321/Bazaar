package com.mudit.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mudit.common.entity.Country;
import com.mudit.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer>, PagingAndSortingRepository<State, Integer> {

	public List<State> findByCountryOrderByNameAsc(Country country);
}
