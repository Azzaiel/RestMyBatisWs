package net.virtela.rest.api.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import net.virtela.model.Country;
import net.virtela.test.ApiTest;
import net.virtela.utils.CommonHelper;

public class GeoRestApiTest extends ApiTest {
	
	private static final String MODULE_NAME = "ncop_quotation";
	
	private static final String PATH_GET_ALL_COUNTRIES = "/v1.0/country/list/";

	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}
	
	@Test
	public void getCountries() {
		
		final Response res = this.authConn().get(PATH_GET_ALL_COUNTRIES);
		assertEquals(Status.OK.getStatusCode(), res.getStatusCode());
		
	    final String json = res.asString();
	    assertTrue(json != null);
	    
	    final List<Country> countryList = this.toList(json, Country.class);
	    assertTrue(CommonHelper.hasValidList(countryList));
		
	}

}
