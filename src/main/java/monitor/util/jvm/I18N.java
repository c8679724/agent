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

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Classe de gestion des traductions et de l'internationalisation (formats de dates et de nombre).
 * La locale pour les rapports vient de la requête et est associée au thread courant.
 * @author Emeric Vernat
 */
final class I18N {
	// RESOURCE_BUNDLE_BASE_NAME vaut "net.bull.javamelody.resource.translations"
	// ce qui charge net.bull.javamelody.resource.translations.properties
	// et net.bull.javamelody.resource.translations_fr.properties
	// (Parameters.getResourcePath("translations") seul ne fonctionne pas si on est dans un jar/war)
	private static final ThreadLocal<Locale> LOCALE_CONTEXT = new ThreadLocal<Locale>();

	private I18N() {
		super();
	}

	/**
	 * Définit la locale (langue et formats dates et nombres) pour le thread courant.
	 * @param locale Locale
	 */
	static void bindLocale(Locale locale) {
		LOCALE_CONTEXT.set(locale);
	}



	/**
	 * Enlève le lien entre la locale et le thread courant.
	 */
	static void unbindLocale() {
		LOCALE_CONTEXT.remove();
	}



	/**
	 * Encode pour affichage en javascript.
	 * @param text message à encoder
	 * @return String
	 */
	static String javascriptEncode(String text) {
		return text.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "%22")
				.replace("'", "%27");
	}

	static String urlEncode(String text) {
		return javascriptEncode(text);
	}

	/**
	 * Encode pour affichage en html.
	 * @param text message à encoder
	 * @param encodeSpace booléen selon que les espaces sont encodés en nbsp (insécables)
	 * @return String
	 */
	static String htmlEncode(String text, boolean encodeSpace) {
		// ces encodages html sont incomplets mais suffisants pour le monitoring
		String result = text.replaceAll("[&]", "&amp;").replaceAll("[<]", "&lt;")
				.replaceAll("[>]", "&gt;").replaceAll("[\n]", "<br/>");
		if (encodeSpace) {
			result = result.replaceAll(" ", "&nbsp;");
		}
		return result;
	}


	static DateFormat createDurationFormat() {
		// Locale.FRENCH et non getCurrentLocale() car pour une durée on veut
		// "00:01:02" (1min 02s) et non "12:01:02 AM"
		final DateFormat durationFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM,
				Locale.FRENCH);
		// une durée ne dépend pas de l'horaire été/hiver du fuseau horaire de Paris
		durationFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return durationFormat;
	}
}
