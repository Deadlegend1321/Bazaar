package com.mudit.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mudit.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer>, PagingAndSortingRepository<Currency, Integer> {

	public List<Currency> findAllByOrderByNameAsc();
}
