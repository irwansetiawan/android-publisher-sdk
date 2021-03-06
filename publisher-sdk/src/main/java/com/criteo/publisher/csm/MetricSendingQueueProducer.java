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

package com.criteo.publisher.csm;

import androidx.annotation.NonNull;

public class MetricSendingQueueProducer {

  @NonNull
  private final MetricSendingQueue queue;

  public MetricSendingQueueProducer(@NonNull MetricSendingQueue queue) {
    this.queue = queue;
  }

  void pushAllInQueue(@NonNull MetricRepository repository) {
    for (Metric metric : repository.getAllStoredMetrics()) {
      pushInQueue(repository, metric.getImpressionId());
    }
  }

  void pushInQueue(
      @NonNull MetricRepository repository,
      @NonNull String impressionId
  ) {
    repository.moveById(impressionId, new MetricMover() {
      @Override
      public boolean offerToDestination(@NonNull Metric metric) {
        return queue.offer(metric);
      }
    });
  }

}
