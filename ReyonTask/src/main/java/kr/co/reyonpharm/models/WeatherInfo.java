package kr.co.reyonpharm.models;

import lombok.Getter;
import lombok.Setter;

public class WeatherInfo {
	private @Getter @Setter String saupname;
	private @Getter @Setter String nx;
	private @Getter @Setter String ny;
	private @Getter @Setter String sky;
	private @Getter @Setter String temperature;
	private @Getter @Setter String precipitation;
	private @Getter @Setter String baseDate;
	private @Getter @Setter String baseTime;
	private @Getter @Setter String weatherText;
	private @Getter @Setter String cssClass;
}
