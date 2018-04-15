# Retrofit 2.X
## https://square.github.io/retrofit/ ##

#### OkHttp, Retrofit
# Thanks https://github.com/square/moshi/issues/345 Lousi-Cad
-dontwarn okhttp3.**
-dontwarn retrofit2.Platform$Java8
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}


-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}



-keepnames @kotlin.Metadata class in.jatindhankhar.wlosh.model.**
-keep class in.jatindhankhar.wlosh.model.** { *; }
-keepclassmembers class in.jatindhankhar.wlosh.model.** { *; }