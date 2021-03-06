/*
 *    Copyright 2020 Criteo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.criteo.testapp.listener;

import android.util.Log;
import com.google.android.gms.ads.AdListener;

public class TestAppDfpAdListener extends AdListener {

  private final String tag;
  private final String prefix;

  public TestAppDfpAdListener(String tag, String prefix) {
    this.tag = tag;
    this.prefix = prefix;
  }

  @Override
  public void onAdClosed() {
    Log.d(tag, prefix + " - called: onAdClosed");
  }

  @Override
  public void onAdFailedToLoad(int var1) {
    Log.d(tag, prefix + " - called: onAdFailedToLoad");
  }

  @Override
  public void onAdLeftApplication() {
    Log.d(tag, prefix + " - called: onAdLeftApplication");
  }

  @Override
  public void onAdOpened() {
    Log.d(tag, prefix + " - called: onAdOpened");
  }

  @Override
  public void onAdLoaded() {
    Log.d(tag, prefix + " - called: onAdLoaded");
  }

  @Override
  public void onAdClicked() {
    Log.d(tag, prefix + " - called: onAdClicked");
  }

  @Override
  public void onAdImpression() {
    Log.d(tag, prefix + " - called: onAdImpression");
  }

}
