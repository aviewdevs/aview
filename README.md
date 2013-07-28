Unofficial ABC iview client for Android
=======================================

This is an unofficial ABC iview client for Android.  As such it may break in 
unexpected and odd ways as the ABC update, upgrade or maintain their official 
iview service and clients.

HTTP Live Streaming
-------------------

The ios iview client uses HTTP Live Streaming, which has some the ability to 
adjust the quality depending on the quality of the network connection (and the API
for it is much lighter weight).  Unfortunately the ABC haven't quite implemented it 
to the specification, so most of their videos won't play all the way through on Android 
devices.  The option can be enabled in the settings menu if you're game though.

Building
--------

1. Install [Maven], eg:

        $ sudo apt-get install maven

2. Install the [Android SDK]
3. Download at least Android 4.3 and the Android support JAR through the SDK manager.
4. Install the Android maven artefacts using the [Maven Android SDK Deployer], eg:

        $ ANDROID_HOME=/path/to/android/sdk; export ANDROID_HOME
        $ mkdir ~/devel && pushd ~/devel
        $ git clone https://github.com/mosabua/maven-android-sdk-deployer.git
        $ cd maven-android-sdk-deployer
        $ mvn clean install -P4.3
        $ cd extras
        $ mvn clean install -N
        $ cd compatibility-v4
        $ mvn clean install
        $ popd

5. Then run `mvn package` in the parent aview directory

Notes
-----

Note that all ABC passwords and https URLs have been omitted from the 
repository.  If you want to use your build against the official ABC servers 
you'll need to figure these out for yourself.

  [Maven]: http://maven.apache.org
  [Android SDK]: http://developer.android.com/sdk/index.html
  [Maven Android SDK Deployer]: https://github.com/mosabua/maven-android-sdk-deployer
