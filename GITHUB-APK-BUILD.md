# AMOPER Android — GitHub APK Build

This project is preconfigured for:

- API base URL: `https://amoperlogistic.com/`
- Android application ID: `africa.amoper.app`
- Gradle: 8.7
- Android Gradle Plugin: 8.5.2
- Java: 17

## Build on GitHub

1. Upload the **contents of this `AmoperAndroid` folder** to a GitHub repository.
2. Commit/push to `main` or `master`.
3. Open **Actions** in GitHub.
4. Open **Build AMOPER Android APK**.
5. Download the `amoper-apks` artifact.
6. The artifact contains the debug APK and release APK.

The debug APK is the easiest one to install directly on an Android phone.

## API

The app is configured to call:

`https://amoperlogistic.com/api.php`

The API routes are selected with the `r` query parameter, for example:

`https://amoperlogistic.com/api.php?r=ping`

If your PHP API is not deployed at the domain root, change `AMOPER_API_URL` in `app/build.gradle.kts`.

## Local Android Studio build

Open this folder (`AmoperAndroid`) in Android Studio and let it sync Gradle. The project uses Java 17 and Gradle 8.7.
