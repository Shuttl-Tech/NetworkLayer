# Network Layer

![Build Status](https://travis-ci.org/ChuckerTeam/chucker.svg?branch=master)  [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-orange.svg)](http://makeapullrequest.com)

A thin wrapper over retrofit that provides some extra functionality  

* [Getting Started](#getting-started-)
* [Features](#features-)
* [Configure](#configure-)
* [FAQ](#faq-)
* [Contributing](#contributing-)

Network Layer simplifies the networking with easy to use interface.

Apps using Wherro will run 2 services responsible for Location Syncing


## Getting Started 👣

To use it you need to add the following **Gradle dependency** to your `build.gradle` file of your android app module (NOT the root file).

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

```groovy
dependencies {
  implementation 'com.github.Shuttl-Tech:NetworkLayer:0.1.0-alpha2'
}
```


## Configure 🎨

To setup Network Layer, app needs to init the Network Layer by adding `Network.init()` and passing in the implementation of `ModuleDependency` interface

```kotlin
Network.init(object : ModuleDependency{...})
```
App needs to add following permissions in app's `AndroidManifest` root

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

with this the Network Layer setup is done.


## Usage 

To use Network Layer just replace retrofit service generation call with the following

```kotlin
val service = HttpServiceGenerator.generateResultService(context, TestService::class.java)
```

wrap your expected response inside `ApiResult` as following

```kotlin
interface HomeService {
    @GET("/api/end/point")
    suspend fun fetchHomeFeeds(): ApiResult<TestResponse>
}
```

and with this you will start getting the expected response or handled error wrapped inside `ApiResult`
**That's it!** 🎉


## Features 🧰

* Uses **OkHTTP 4** | **Retrofit**
* **Coroutine** compatible
* **API >= 16** compatible
* Handles errors and passes in **ApiResult**
* Callbacks to handle **User Validations**
* Provides **Retry Logic** on call failures (WIP)


## FAQ ❓

* **Why is it not working for me?** - dependency issue maybe, create an issue if it doesn't work


## Contributing 🤝

**We're looking for contributors! Don't be shy.** 😁 Feel free to open issues/pull requests to help me improve this project.
