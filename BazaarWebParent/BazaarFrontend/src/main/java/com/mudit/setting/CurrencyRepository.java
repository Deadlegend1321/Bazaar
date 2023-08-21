package com.mudit.setting;

import org.springframework.data.repository.CrudRepository;

import com.mudit.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

}
