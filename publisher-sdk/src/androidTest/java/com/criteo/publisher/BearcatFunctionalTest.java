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

package com.criteo.publisher;

import static com.criteo.publisher.CriteoUtil.givenInitializedCriteo;
import static com.criteo.publisher.util.CompletableFuture.completedFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import androidx.test.rule.ActivityTestRule;
import com.criteo.publisher.mock.MockBean;
import com.criteo.publisher.mock.MockedDependenciesRule;
import com.criteo.publisher.mock.SpyBean;
import com.criteo.publisher.model.DeviceInfo;
import com.criteo.publisher.network.PubSdkApi;
import com.criteo.publisher.privacy.UserPrivacyUtil;
import com.criteo.publisher.test.activity.DummyActivity;
import com.criteo.publisher.util.AdvertisingInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class BearcatFunctionalTest {

  @Rule
  public MockedDependenciesRule mockedDependenciesRule = new MockedDependenciesRule();

  @Rule
  public ActivityTestRule<DummyActivity> activityRule = new ActivityTestRule<>(DummyActivity.class);

  @SpyBean
  private PubSdkApi api;

  @MockBean
  private UserPrivacyUtil userPrivacyUtil;

  private DependencyProvider dependencyProvider;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    dependencyProvider = mockedDependenciesRule.getDependencyProvider();
  }

  @Test
  public void init_GivenUserAgentAndLaunchedActivity_SendInitEventWithUserAgent() throws Exception {
    givenAppEventsCanBeCalled();
    when(userPrivacyUtil.getGdprConsentData()).thenReturn("fakeConsentData");

    DeviceInfo deviceInfo = spy(dependencyProvider.provideDeviceInfo());
    doReturn(deviceInfo).when(dependencyProvider).provideDeviceInfo();
    doReturn(completedFuture("expectedUserAgent")).when(deviceInfo).getUserAgent();

    givenInitializedCriteo();
    activityRule.launchActivity(new Intent());
    waitForIdleState();

    verify(api).postAppEvent(anyInt(), any(), any(), any(), anyInt(), eq("expectedUserAgent"),
        eq("fakeConsentData")
    );
  }

  @Test
  public void init_GivenInputAndLaunchedActivity_SendInitEventWithGivenData() throws Exception {
    givenAppEventsCanBeCalled();
    when(userPrivacyUtil.getGdprConsentData()).thenReturn("fakeConsentData");

    AdvertisingInfo advertisingInfo = mock(AdvertisingInfo.class);
    when(advertisingInfo.isLimitAdTrackingEnabled()).thenReturn(false);
    when(advertisingInfo.getAdvertisingId()).thenReturn("myAdvertisingId");

    doReturn(advertisingInfo).when(dependencyProvider).provideAdvertisingInfo();

    givenInitializedCriteo();
    activityRule.launchActivity(new Intent());
    waitForIdleState();

    verify(api).postAppEvent(
        eq(2379),
        eq("com.criteo.publisher.test"),
        eq("myAdvertisingId"),
        eq("Launch"),
        eq(0),
        any(),
        eq("fakeConsentData")
    );
  }

  @Test
  public void init_GivenLimitedAdTracking_SendInitEventWithDummyGaidAndLimitation()
      throws Exception {
    givenAppEventsCanBeCalled();
    when(userPrivacyUtil.getGdprConsentData()).thenReturn("fakeConsentData");

    AdvertisingInfo advertisingInfo = mock(AdvertisingInfo.class);
    when(advertisingInfo.isLimitAdTrackingEnabled()).thenReturn(true);
    when(advertisingInfo.getAdvertisingId()).thenReturn("myAdvertisingId");

    doReturn(advertisingInfo).when(dependencyProvider).provideAdvertisingInfo();

    givenInitializedCriteo();
    activityRule.launchActivity(new Intent());
    waitForIdleState();

    verify(api).postAppEvent(
        anyInt(),
        any(),
        eq("myAdvertisingId"),
        any(),
        eq(1),
        any(),
        eq("fakeConsentData")
    );
  }

  private void waitForIdleState() {
    mockedDependenciesRule.waitForIdleState();
  }

  private void givenAppEventsCanBeCalled() {
    when(userPrivacyUtil.isCCPAConsentGivenOrNotApplicable()).thenReturn(true);
    when(userPrivacyUtil.isMopubConsentGivenOrNotApplicable()).thenReturn(true);
  }
}
