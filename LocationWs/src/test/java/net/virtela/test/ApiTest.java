package net.virtela.test;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.type.TypeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import net.virtela.config.ServerConfig;
import net.virtela.constants.Constants;

public abstract class ApiTest {

	/** Extent Report Config **/
	private static final String REPORT_NAME = "MyAppWs_JUNIT.html";
	private static final boolean REPLACE_REPORT = true;

	/** Standard Query Params **/
	public static final String QUERY_PARAM_COUNTRY = "country";
	public static final String QUERY_PARAM_VIRTELA_PRODUCT = "virtelaProduct";
	public static final String MSG_TBC = "Test case is not yet completed";

	private static final String CREDENTIAL_TOKEN = "N0C0P-@PP-T0k3N";
	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final String HEADER_TOKEN = "Security-Token";
	private static final String HEADER_COTENT = "Content-Type";

	private static final String SCHEME = "http://";
	private static final String PORT = "8080";
	private static final String APPLICATION = "MyAppWs";
	private static final String WS_TYPE = "rest";

	public abstract String getModuleName();

	public static ExtentReports extent;
	public ExtentTest exTest;

	@Before
	public void setUp() {

		final StringBuffer basePath = new StringBuffer();
		basePath.append(SCHEME);
		basePath.append(ServerConfig.getHost());
		basePath.append(Constants.COLON).append(PORT);
		basePath.append(Constants.SLASH).append(APPLICATION);
		basePath.append(Constants.SLASH).append(WS_TYPE);
		basePath.append(Constants.SLASH).append(this.getModuleName());
		RestAssured.basePath = basePath.toString();

	}

	/**
	 * Log to Extend in chase assertion fails
	 */

	@Rule
	public TestWatcher testWatcher = new TestWatcher() {
		protected void failed(Throwable e, Description description) {
			if (exTest != null) {
				exTest.log(LogStatus.FAIL, description.getDisplayName() + " failed: " + e.getMessage());
				extent.endTest(exTest);
				extent.flush();
			}

			super.failed(e, description);
		}

	};

	@BeforeClass
	public static void setUpClass() {
		extent = new ExtentReports("/opt/test/reports/" + REPORT_NAME, REPLACE_REPORT);
		extent.assignProject("MyAppSalesWs Rest Unit Test");
		// extent.x("localhost", 27017); TODO: Install XExtentReport on Dev and
		// Test Server
	}

	@After
	public void afterMethod() {
		extent.endTest(this.exTest);
		extent.flush();
	}

	public RequestSpecification authConn() {
		return RestAssured.given().header(HEADER_TOKEN, CREDENTIAL_TOKEN).header(HEADER_COTENT, CONTENT_TYPE_JSON);
	}

	@SuppressWarnings("unchecked")
	public <T> T toObject(String json, Class<?> type) {
		final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		try {
			return (T) mapper.readValue(json, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T toList(String json, Class<?> type) {
		try {
			final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return (T) mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String objectToString(Object obj) {
		final org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (IOException e) {
			return Constants.EMPTY_STRING;
		}
	}

}
