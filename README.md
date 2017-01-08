# SocialAuth
A library that helps to implement social network authorization (Facebook, Twitter, Instagram, GooglePlus, VK).

###Twitter
Create new application at https://apps.twitter.com<br />

Download via Gradle:
```gradle
compile 'com.github.pavel163.SocialAuth:twitter:1.0.0'
```
In strings.xml
```xml
<string name="twitter_consumer_key">your_consumer_key</string>
<string name="twitter_consumer_secret">your_consumer_secret</string>
```
If you don't use fabric plugin for Android studio, put it into gradle:
```gradle
repositories {
    maven { url 'https://maven.fabric.io/public' }
}

buildscript {
    repositories {
        mavenCentral()
        maven { url 'https://maven.fabric.io/public' }
    }

    dependencies {
        classpath 'io.fabric.tools:gradle:1.+'
    }
}
```
And look at an example <a href="https://github.com/pavel163/SocialAuth/blob/master/app/src/main/java/com/ebr163/socialauth/TwitterActivity.java">TwitterActivity</a>

###Facebook
Create new application at https://developers.facebook.com/apps<br />
Download via Gradle:
```gradle
compile 'com.github.pavel163.SocialAuth:facebook:1.0.0'
```
In strings.xml
```xml
<string name="facebook_app_id">your_app_id</string>
```

Add the Maven Central Repository to build.gradle before dependencies:
```gradle
repositories {
   mavenCentral()
}
```
Add a meta-data element to the application element:
```xml
<application android:label="@string/app_name" ...>
    ...
    <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
    ...
</application>
```
And look at an example <a href="https://github.com/pavel163/SocialAuth/blob/master/app/src/main/java/com/ebr163/socialauth/FacebookActivity.java">FacebookActivity</a>

