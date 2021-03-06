/*
 * Copyright 2017 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.spinnaker.keel.scheduler

import com.netflix.spinnaker.q.Queue
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

interface SchedulerAgent {
  fun run()
}

@Component
class QueueBackedSchedulerAgent(
  private val queue: Queue
) : SchedulerAgent {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostConstruct fun ensureSchedule() {
    // TODO rz - Not super nice, but works: A deploy may push out a convergence.
    log.info("Ensuring scheduler convergence task exists")
    queue.push(ScheduleConvergence(), Duration.ofSeconds(10))
  }

  @Scheduled(fixedDelayString = "\${scheduler.retry.frequency.ms:30000}")
  override fun run() {
    // TODO rz / keiko - add `ensure`, make sure the message is on the queue, but no-op (no schedule change) if present
    log.info("TODO: Rescheduling convergence task")
//    queue.reschedule(ScheduleConvergence())
  }
}
