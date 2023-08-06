package com.mudit.shipping;

import org.springframework.data.repository.CrudRepository;

import com.mudit.common.entity.Country;
import com.mudit.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
	
	public ShippingRate findByCountryAndState(Country country, String state);

}
