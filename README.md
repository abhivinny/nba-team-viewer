# nba-team-viewer

## Problem
Create an NBA teamview app based on criterias mentioned in https://github.com/scoremedia/nba-team-viewer

## Approach
* The app follows the clean architecture approach with MVVM. The app uses

  * Navigation Component
  * Retrofit
  * Room Database
  * Kotlin Coroutine
  * Kotlin Flow
  * Android ViewModel and LiveData
  * Koin dependency framework
  * Mockito
  * Espresso
  * Junit

## Installation
* Clone this repo on your local machine using
 `git clone https://github.com/abhivinny/nba-team-viewer.git`
 
## Build
* Use `gradlew clean installDebug` to install the app on your device or emulator.

## Test
* Unit test: I have added unit test for `Repository` and `UseCase` classes.

* Robolectric test: I do not have much experience on this. Though, for a very brief period in one of the project I used it but later
we moved to Espresso test. 
 
 Just to demonstrate the skill I have added:
 * Espresso Test: Added one test to demonstarte the skill.
