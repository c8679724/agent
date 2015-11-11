/*
 * Copyright 2008-2014 by Emeric Vernat
 *
 *     This file is part of Java Melody.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package monitor.util.jvm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Classe d'accès aux paramètres du monitoring.
 * @author Emeric Vernat
 */
final class Parameters {
	static final String PARAMETER_SYSTEM_PREFIX = "javamelody.";
	static final File TEMPORARY_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
	static final String JAVA_VERSION = System.getProperty("java.version");
	static final String JAVAMELODY_VERSION = getJavaMelodyVersion();

	private static String lastConnectUrl;
	private static Properties lastConnectInfo;
	private static boolean dnsLookupsDisabled;

	private Parameters() {
		super();
	}


	static void initJdbcDriverParameters(String connectUrl, Properties connectInfo) {
		Parameters.lastConnectUrl = connectUrl;
		Parameters.lastConnectInfo = connectInfo;
	}

	static String getLastConnectUrl() {
		return lastConnectUrl;
	}

	static Properties getLastConnectInfo() {
		return lastConnectInfo;
	}


	/**
	 * @return nom réseau de la machine
	 */
	static String getHostName() {
		if (dnsLookupsDisabled) {
			return "localhost";
		}

		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException ex) {
			return "unknown";
		}
	}

	/**
	 * @return adresse ip de la machine
	 */
	static String getHostAddress() {
		if (dnsLookupsDisabled) {
			return "127.0.0.1"; // NOPMD
		}

		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException ex) {
			return "unknown";
		}
	}

	/**
	 * @param fileName Nom du fichier de resource.
	 * @return Chemin complet d'une resource.
	 */
	static String getResourcePath(String fileName) {
		final Class<Parameters> classe = Parameters.class;
		final String packageName = classe.getName().substring(0,
				classe.getName().length() - classe.getSimpleName().length() - 1);
		return '/' + packageName.replace('.', '/') + "/resource/" + fileName;
	}


	private static String getJavaMelodyVersion() {
		final Properties properties = new Properties();
		final InputStream inputStream = Parameters.class.getResourceAsStream("/VERSION.properties");
		if (inputStream == null) {
			return null;
		}

		try {
			try {
				properties.load(inputStream);
				return properties.getProperty("version");
			} finally {
				inputStream.close();
			}
		} catch (final IOException e) {
			return e.toString();
		}
	}


}
