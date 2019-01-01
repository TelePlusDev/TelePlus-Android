-keep public class com.google.android.gms.* { public *; }
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class com.android.* {
    *;
}

-dontwarn com.google.android.gms.**
-dontwarn com.google.common.cache.**
-dontwarn com.google.common.primitives.**

# Use -keep to explicitly keep any other classes shrinking would remove
#-dontoptimize
#-dontobfuscate

-ignorewarnings

################################################################
-verbose
-dontoptimize
-dontpreverify
-dontshrink
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 2
-allowaccessmodification

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep interface com.android.internal.telephony.ITelephony{
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

-keep class com.android.** { *; }
-keep interface com.android.** { *; }

-keep class com.google.android.** { *; }
-keep interface com.google.android.** { *; }

-keep class net.surina.soundtouch.** { *; }
-keep interface net.surina.soundtouch.** { *; }

-keep class org.telegram.** { *; }
-keep interface org.telegram.** { *; }

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn com.google.ads.**

#To repackage classes on a single package
-repackageclasses ''