package net.virtela.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import net.virtela.dao.GeoDao;
import net.virtela.model.Country;
import net.virtela.service.GeoService;

@Controller
public class GeoServiceImpl implements GeoService {

	@Autowired
	public GeoDao geoDao;

	@Override
	public List<Country> getCountries() {
		final List<Country> countryList = this.geoDao.getCountries();
		if (countryList != null) {
			return countryList;
		}
		return Collections.emptyList();
	}

	@Override
	public Country getCountryById(Long id) {
		if (id != null && id > 0) {
			return this.geoDao.getCountryById(id);
		}
		return new Country();
	}

	@Override
	public Long createCountry(Country country) {
		if (country != null && country.getName() != null) {
			return this.geoDao.createCountry(country);
		}
		return 0l;
	}

	@Override
	public int updateCountry(Country country, Long id) {
		if (id > 0 && country != null && country.getName() != null) {
			country.setId(id);
			return this.geoDao.updateCountry(country);
		}
		return 0;
	}

	@Override
	public Long deleteCountryById(Long id) {
		if (id != null && id > 0) {
			return this.geoDao.deleteCountryById(id);
		}
		return 0l;
	}
	
	

}
