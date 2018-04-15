# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-keep class in.jatindhankhar.model.** { *; }

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}