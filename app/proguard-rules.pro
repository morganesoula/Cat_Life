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