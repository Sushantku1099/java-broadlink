# Changelog

## [Unreleased] — v1.0.1

### Added
- Maven Central badge on README
- Android demo app (`android-demo-app/`)
- GitHub Actions CI/CD pipeline (`.github/workflows/ci-cd.yml`)
- Maven Central publishing guide (`docs/MAVEN_CENTRAL_PUBLISH.md`)
- Profile README for GitHub (`github-profile/`)

### Changed
- Removed `TestSuiteRunner.java` (helper file, not part of library)
- Added `junit-platform-launcher` as test-scoped dependency in `pom.xml`
- Updated `build.sh` to use standard Maven test runner

### Fixed
- `switch` → `power` package rename for JDK compatibility
- Jackson-dependent `JsonCodec` replaced with SPI `JsonSerializer`
- SLF4J-dependent logging replaced with SPI `BroadlinkLogger`
- Jackson annotations stripped from all models
- Zero-dependency `ManualJsonSerializer` (handles all 4 state models)

---

## [1.0.0] — 2026-06-18

### First Public Release

- 19 device classes, 124 type IDs
- Multi-platform: Android, Spring Boot, Desktop, Kotlin
- Multi-module architecture: `broadlink-core`, `broadlink-android`, `broadlink-json-gson`
- Zero-dependency `broadlink-core` module
- SPI pluggable: Logger, JsonSerializer, NetworkProvider
- Reverse-engineered UDP protocol with AES-128-CBC encryption
- 53 tests validating exact packet byte structures
- Complete protocol documentation (5 spec files, 4,400+ lines)
- GitHub Profile README
- Full Maven Central publishing pipeline
