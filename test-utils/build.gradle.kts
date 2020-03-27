plugins {
  id("com.android.library")
  kotlin("android")
}

androidLibModule()

dependencies {
  compileOnly(project(":publisher-sdk"))

  implementation(Deps.JUnit.JUnit)
  implementation(Deps.Mockito.Core)

  implementation(Deps.Android.Support.Annotations)
  implementation(Deps.Android.Test.Runner)
  implementation(Deps.Android.Test.Rules)
}