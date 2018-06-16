-verbose

-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.xmlpull.v1.**
-dontwarn org.joda.**
-dontwarn java.lang.ClassValue
-dontwarn rx.**
-dontwarn javax.xml.stream.**
-dontwarn org.codehaus.**
-dontwarn org.simpleframework.**
-dontwarn com.google.**
-dontwarn android.**
-dontwarn com.squareup.**
-dontwarn com.android.**
-dontwarn sun.**

-keep class org.xmlpull.** { *; }

-keep class android.support.v7.widget.SearchView { *; }

-keep class javax.xml.** { *; }
-keep class com.google.** { *; }
-keep class android.** { *; }

-keep class io.realm.** { *; }

-keep class org.joda.** { *; }
-keep class org.simpleframework.** { *; }
-keep class me.zhanghai.** { *; }
-keep class com.squareup.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-keep class org.apache.http.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class okio.** { *; }
-keep class com.intrusoft.** { *; }
-keep class com.bumptech.** { *; }
-keep class de.hdodenhof.** { *; }

-keep class .**.ffmpeg.** { *; }
-keep class .**.avcodec.** { *; }
-keep class tv.** { *; }
-keep class com.google.android.exoplayer2.** { *; }

-keep class java.text.SimpleDateFormat { *; }

-keepattributes *Annotation*
-keep @**annotation** class * { *; }

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keepattributes Signature
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**