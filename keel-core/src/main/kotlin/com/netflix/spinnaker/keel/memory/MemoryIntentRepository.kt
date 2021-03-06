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
package com.netflix.spinnaker.keel.memory

import com.netflix.spinnaker.keel.Intent
import com.netflix.spinnaker.keel.IntentRepository
import com.netflix.spinnaker.keel.IntentSpec
import com.netflix.spinnaker.keel.IntentStatus
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct

class MemoryIntentRepository : IntentRepository {

  private val log = LoggerFactory.getLogger(javaClass)

  private val intents: MutableMap<String, Intent<IntentSpec>> = mutableMapOf()

  @PostConstruct fun init() {
    log.info("Using ${javaClass.simpleName}")
  }

  override fun upsertIntent(intent: Intent<IntentSpec>) {
    intents.put(intent.getId(), intent)
  }

  override fun getIntents() = intents.values.toList()

  override fun getIntents(statuses: List<IntentStatus>)
    = intents.values.filter { statuses.contains(it.status) }.toList()

  override fun getIntent(id: String) = intents[id]
}
