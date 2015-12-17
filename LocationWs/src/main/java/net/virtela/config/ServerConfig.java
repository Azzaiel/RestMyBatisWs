package net.virtela.config;

import net.virtela.constants.Constants;
import net.virtela.utils.CommonHelper;

public class ServerConfig {
	
	public static final String BASE_PATH = "/MyAppWs/rest";

	public static final String ENV_KEY_SERVER_TYPE = "SERVER_TYPE";
	public static final int MODE_LOCAL = 1;
	public static final int MODE_TEST = 2;
	public static final int MODE_SANDBOX = 3;
	public static final int MODE_PRDOCUTION = 4;
	public static final int MODE_DEV = 5;

	/**
	 * Change this to point ALL WS requests on desired server
	 */
	private static final int MODE = MODE_LOCAL;

	public static final String WS_PORT = "8080";
	private static final String WS_SCHEAM = "http://";
	private static final String WS_AUTH_NAME = "VirtelaAccountWS/rest";
	private static final String WS_NCOP_NAME = "NcopWS/rest";

	public static final String DIR_PUBLIC = "public";
	public static final String DIR_TEMPLATE = "templates";
	public static final String DIR_IMAGES = "images";
	public static final String DIR_TEMP = "tmp";

	/** HOST **/
	private static final String HOST_LOCAL = "localhost";
	private static final String HOST_DEV = "172.16.17.43";
	private static final String HOST_TEST = "dcoeng1-ncoptst-2";
	private static final String HOST_SBX = "dcoeng1-ncopsbx-2";
	private static final String HOST_PROD = "ncop-ws.virtela.net";

	private static final String HOST_AUTH_PROD = "dcoeng1-ncopws-1";

	public static String getHost() {
		switch (MODE) {
		case MODE_LOCAL:
			return HOST_LOCAL;
		case MODE_TEST:
			return HOST_TEST;
		case MODE_SANDBOX:
			return HOST_SBX;
		case MODE_DEV:
			return HOST_DEV;
		}
		return HOST_PROD;
	}

	/** HOST **/
	private static String LOCAL_NCOP_BASE_URL = "http://dcoeng1-ncoptst-4/VirtelaAccessPricing";
	private static String TEST_NCOP_BASE_URL = "http://dcoeng1-ncoptst-4/VirtelaAccessPricing";
	private static String SBX_NCOP_BASE_URL = "http://dcoeng1-ncopsbx-4/VirtelaAccessPricing";
	private static String LIVE_NCOP_BASE_URL = "http://ncop.virtela.net/VirtelaAccessPricing";

	private static final String PREFIX_LOCAL = "Local Server: ";
	private static final String PREFIX_TEST = "QA Server: ";
	private static final String PREFIX_SBX = "UAT Server: ";
	private static final String PREFIX_DEV = "DEV Server: ";

	private static String ADF_PAGE_PREFIX = "faces/pages";
	private static String NCOP_REQUEST = "autorequest";
	private static String NCOP_VIEW_REQUEST_PM_DASH = "pmviewproject";

	public static String getServerType() {
		// TODO: for Env setting
		return System.getenv(ENV_KEY_SERVER_TYPE);
	}

	public static String getVirteaLoginHostUrl() {
		final StringBuffer authWsUrl = new StringBuffer();
		authWsUrl.append(WS_SCHEAM);

		if (isProdMode()) {
			authWsUrl.append(HOST_AUTH_PROD);
		} else if (isLocalMode()) {
			// TODO: Mod Auth server for local
			// authWsUrl.append(getHost());
			authWsUrl.append(HOST_TEST);
		} else {
			authWsUrl.append(getHost());
		}
		
		authWsUrl.append(Constants.COLON);
		authWsUrl.append(WS_PORT);
		authWsUrl.append(Constants.SLASH);
		authWsUrl.append(WS_AUTH_NAME);
		return authWsUrl.toString();
	}

	public static String getNcopWsHostUrl() {
		final StringBuffer ncopWsUrl = new StringBuffer();
		ncopWsUrl.append(WS_SCHEAM);
		ncopWsUrl.append(getHost());

		if (isProdMode() == false) {
			ncopWsUrl.append(Constants.COLON);
			ncopWsUrl.append(WS_PORT);
		}

		ncopWsUrl.append(Constants.SLASH);
		ncopWsUrl.append(WS_NCOP_NAME);
		return ncopWsUrl.toString();
	}

	public static boolean isLocalMode() {
		return CommonHelper.isEqual(MODE, MODE_LOCAL);
	}

	public static boolean isProdMode() {
		return CommonHelper.isEqual(MODE, MODE_PRDOCUTION);
	}
	
	public static boolean isDevMode() {
		return CommonHelper.isEqual(MODE, MODE_DEV);
	}

	private static boolean isTestMode() {
		return CommonHelper.isEqual(MODE, MODE_TEST);
	}

	private static boolean isSbxMode() {
		return CommonHelper.isEqual(MODE, MODE_SANDBOX);
	}

	public static String getServerTag() {
		if (isLocalMode()) {
			return PREFIX_LOCAL;
		} else if (isTestMode()) {
			return PREFIX_TEST;
		} else if (isSbxMode()) {
			return PREFIX_SBX;
		} else if (isDevMode()) {
			return PREFIX_DEV;
		}
		return Constants.EMPTY_STRING;
	}

	public static String getServerMailRecipients() {
		if (isLocalMode()) {
			return Constants.MAILING_DEV_LIST;
		}
		return Constants.MAILING_LIST;
	}

	public static synchronized String getNcopBaseLink() {
		if (isLocalMode()) {
			return LOCAL_NCOP_BASE_URL;
		} else if (isTestMode()) {
			return TEST_NCOP_BASE_URL;
		} else if (isSbxMode()) {
			return SBX_NCOP_BASE_URL;
		}
		return LIVE_NCOP_BASE_URL;
	}

	public static String getPmDashProjectUrl(String projectId) {
		final StringBuffer pmDashUrl = new StringBuffer();
		if (isLocalMode()) {
			pmDashUrl.append(LOCAL_NCOP_BASE_URL);
		} else if (isTestMode()) {
			pmDashUrl.append(TEST_NCOP_BASE_URL);
		} else if (isSbxMode()) {
			pmDashUrl.append(SBX_NCOP_BASE_URL);
		} else {
			pmDashUrl.append(LIVE_NCOP_BASE_URL);
		}
		pmDashUrl.append(Constants.SLASH);
		pmDashUrl.append(ADF_PAGE_PREFIX);
		pmDashUrl.append(Constants.SLASH);
		pmDashUrl.append(NCOP_REQUEST);
		pmDashUrl.append(Constants.QUESTION_MARK);
		pmDashUrl.append(NCOP_VIEW_REQUEST_PM_DASH);
		pmDashUrl.append(Constants.SEMI_COLON);
		pmDashUrl.append(projectId);

		return pmDashUrl.toString();
	}

}
