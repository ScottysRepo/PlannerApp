# Yahoo Weather API Android
This API retrieve weather information from Yahoo weather

## Exemple
```java
YahooAPI Yahoo = new YahooAPI("Paris France");

String text = "";

try{
    Yahoo.syncData();

	text +=
	        ">> Atmosphere\n" +
	                "Humidity : " + Yahoo.Atmosphere().getHumidity() + " %\n" +
	                "Pressure : " + Yahoo.Atmosphere().getPressure() + " in\n" +
	                "Rising : " + Yahoo.Atmosphere().getRising() + "\n" +
	                "Visibility : " + Yahoo.Atmosphere().getVisibility() + " mi\n";

	text +=
	        ">> Astronomy\n" +
	                "Sunrise : " + Yahoo.Astronomy().getSunrise() + "\n" +
	                "Sunset : " + Yahoo.Astronomy().getSunset() + "\n";

	text +=
	        ">> Condition\n" +
	                "Date : " + Yahoo.Condition().getDate() + "\n" +
	                "Temp : " + Yahoo.Condition().getTemp() + " °F\n" +
	                "Text : " + Yahoo.Condition().getText() + "\n";

	text +=
	        ">> Wind\n" +
	                "Chill : " + Yahoo.Wind().getChill() + "\n" +
	                "Direction : " + Yahoo.Wind().getDirection() + "\n" +
	                "Speed : " + Yahoo.Wind().getSpeed() + " mph \n";

	text += "----------------------\n";
	for(int i = 0; i < Yahoo.ListForecast().size(); i++){
	    text +=
	            "Forecast n° " + String.valueOf(i) + " : " + Yahoo.ListForecast().get(i).getDate() + "\n" +
	                    "Text : " + Yahoo.ListForecast().get(i).getText() + "\n" +
	                    "Hight : " + Yahoo.ListForecast().get(i).getHigh() + " °F\n" +
	                    "Low : " + Yahoo.ListForecast().get(i).getLow() + " °F\n";
	}
} catch(Exception e){
    text = "Error!";
}


txtView.setText(text);
```

## Overview
> ![YahooWeather API Android](http://www.ealen.fr/github/yahoo_over.PNG)

### Version
1.0.0

### Installation
* [Download the most recent release version](https://github.com/Ealenn/YahooWeather-API-Android/releases) 
* Go to the root folder of your project in [ROOT/app/src/main/java]
* Extract the folders "com" in archive into : app \ src \ main \ java
* You must have this : app \ src \ main \ java \ com \ ealen \ ...

Add dependency to your project (app/build.gradle) :

```gradle
compile 'com.google.code.gson:gson:2.6.2'
compile 'com.squareup.okhttp3:okhttp:3.2.0'
```

Add permission to the manifest (app/src/main/AndroidManifest.xml) :

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

### Todos

 - System of "cache" (offline mode)
 - Metric conversion
 - French translation

License
----
[GNU GENERAL PUBLIC LICENSE](https://github.com/Ealenn/YahooWeather-API-Android/blob/master/LICENSE)


