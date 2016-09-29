package org.eclipse.virgo.bundlor.sample.gradle.simple;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SimpleStringDistance {

	enum DistanceAlgorithm {
		LEVENSHTEIN, FUZZY;
	}

	private int distance = Integer.MIN_VALUE;

	private DistanceAlgorithm distanceAlgorithm;
	private Locale locale;
	private String s1;
	private String s2;

	public SimpleStringDistance(DistanceAlgorithm distanceAlgorithm,
			Locale locale, String s1, String s2) {
		Validate.notNull(distanceAlgorithm);
		Validate.notNull(locale);
		Validate.notEmpty(s1);
		Validate.notEmpty(s2);

		this.distanceAlgorithm = distanceAlgorithm;
		this.locale = locale;
		this.s1 = s1;
		this.s2 = s2;

		distance = calculateDistance();
	}

	private int calculateDistance() {
		int dist;
		if (distanceAlgorithm == DistanceAlgorithm.FUZZY) {
			dist = StringUtils.getLevenshteinDistance(s1, s2);
		} else {
			dist = StringUtils.getFuzzyDistance(s1, s2, Locale.ENGLISH);
		}
		return dist;
	}

	public int getDistance() {
		return distance;
	}

	public DistanceAlgorithm getDistanceAlgorithm() {
		return distanceAlgorithm;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getS1() {
		return s1;
	}

	public String getS2() {
		return s2;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
