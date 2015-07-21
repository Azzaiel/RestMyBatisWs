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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateCountry(Country country) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long deleteCountryById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
