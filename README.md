# AMOPER Android

GitHub-ready native Android application for AMOPER Logistics / AMOPER Market.

## Production backend

`https://amoperlogistic.com/`

The app uses the PHP API entry point `api.php` under that domain.

## GitHub APK build

Upload the **contents of this directory** directly into the root of a GitHub repository. Do not put the project inside another `AmoperAndroid` folder.

The included workflow is:

`.github/workflows/build-apk.yml`

It automatically builds:

- Debug APK
- Release APK

and uploads both in the `amoper-apks` artifact.

## Repository structure

```text
.
├── .github/workflows/build-apk.yml
├── app/
├── gradle/wrapper/gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## GitHub steps

1. Create a repository.
2. Upload all files/folders from this project to the repository root.
3. Commit to `main`.
4. Open **Actions**.
5. Select **Build AMOPER Android APK**.
6. Wait for the green successful run.
7. Open the run and download **amoper-apks** under Artifacts.

## Build URL override

The default production URL is already set to:

`https://amoperlogistic.com/`

To use another URL locally:

```text
-PAMOPER_API_URL="https://example.com/"
```

The URL should end with `/`.
