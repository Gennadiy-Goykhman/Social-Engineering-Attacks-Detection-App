# Enable aggressive optimization and obfuscation
-optimizationpasses 10
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively
-repackageclasses 'com.secure.obfuscated'
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# Obfuscation settings
-useuniqueclassmembernames
-dontusemixedcaseclassnames
-flattenpackagehierarchy ''
-adaptclassstrings
-adaptresourcefilenames '**.xml'
-adaptresourcefilecontents '**.xml'

# Keep minimal attributes for debugging (optional, remove for maximum obfuscation)
-keepattributes SourceFile,LineNumberTable

# Remove logging (optional, but recommended for security)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Keep application entry points
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep View bindings and click listeners
-keepclassmembers class * extends android.view.View {
    void set*(***);
    *** get*();
}

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep resource classes
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep annotations (optional, remove for maximum obfuscation)
-keepattributes *Annotation*

# Keep method parameters (useful for reflection)
-keepclassmembernames class * {
    java.lang.reflect.Method getMethod(java.lang.String, java.lang.Class[]);
    java.lang.reflect.Method getDeclaredMethod(java.lang.String, java.lang.Class[]);
}

# Keep third-party libraries (adjust as needed)
-keep class com.google.** { *; }
-keep class com.facebook.** { *; }
-keep class com.android.** { *; }

# Keep custom classes (optional, adjust as needed)
# -keep class com.example.myapp.** { *; }

# Additional security measures
# Rename all classes and packages to obscure names
-repackageclasses ''
-allowaccessmodification

# Remove debug information (optional, but recommended for security)
-keepattributes Exceptions,InnerClasses,Signature

# Remove unused code
-dontshrink
-dontoptimize
-dontobfuscate

# Prevent deobfuscation tools from analyzing the code
-keepclassmembers class * {
    private static final **[] $VALUES;
    private static ** valueOf(java.lang.String);
    private **[] values();
}

# Prevent reflection-based attacks
-keepclassmembers class * {
    public static ** getInstance();
    public static ** valueOf(java.lang.String);
}

# Prevent string decryption
-keepclassmembers class * {
    private static final java.lang.String[] ENCRYPTED_STRINGS;
}