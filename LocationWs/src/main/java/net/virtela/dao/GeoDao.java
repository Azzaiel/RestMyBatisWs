package net.virtela.dao;

import java.util.List;

import net.virtela.model.Country;

public interface GeoDao {

	public List<Country> getCountries();
	
	public Country getCountryById(Long id);
	
	public Long createCountry(Country country);
	
	public int updateCountry(Country country);
	
	public Long deleteCountryById(Long id);
	
}
