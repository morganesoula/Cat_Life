# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn kotlinx.serialization.KSerializer
-dontwarn kotlinx.serialization.Serializable
-dontwarn kotlinx.serialization.internal.AbstractPolymorphicSerializer
-dontwarn io.grpc.internal.DnsNameResolverProvider
-dontwarn io.grpc.internal.PickFirstLoadBalancerProvider
# Gson specific classes
-dontwarn sun.misc.**

-keep class com.google.firebase.** { *; }

# Keep the Play Services classes
-keep class com.google.android.gms.** { *; }

# Keep the Firebase options
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers public class com.google.firebase.options.FirebaseOptions {
    public <fields>;
    public <methods>;
}

# Keep the Firebase database data classes
-keepclassmembers class * extends com.google.firebase.database.core.utilities.encoding.CustomClassMapper$BeanMapper {
    <fields>;
}

# Keep the Firebase Firestore classes
-keepnames class com.google.firebase.firestore.** { *; }

# Keep the Firebase Auth classes
-keepnames class com.google.firebase.auth.** { *; }

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken