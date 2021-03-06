package net.virtela.service;

import java.util.List;

import net.virtela.model.Country;

public interface GeoService {

	public List<Country> getCountries();

	public Country getCountryById(Long id);

	public Long createCountry(Country country);

	public int updateCountry(Country country, Long id);

	public Long deleteCountryById(Long id);
	
}
