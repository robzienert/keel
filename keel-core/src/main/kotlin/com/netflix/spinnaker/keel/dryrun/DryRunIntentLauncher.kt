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
package com.netflix.spinnaker.keel.dryrun

import com.netflix.spectator.api.BasicTag
import com.netflix.spectator.api.Registry
import com.netflix.spinnaker.keel.*
import com.netflix.spinnaker.keel.model.OrchestrationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component(value = "dryRunIntentLauncher")
class DryRunIntentLauncher
@Autowired constructor(
  private val intentProcessors: List<IntentProcessor<*>>,
  private val registry: Registry
): IntentLauncher<DryRunLaunchedIntentResult> {

  private val invocationsId = registry.createId("intent.invocations", listOf(BasicTag("launcher", "dryRun")))

  override fun launch(intent: Intent<IntentSpec>): DryRunLaunchedIntentResult {
    registry.counter(invocationsId).increment()

    val processor = intentProcessor(intentProcessors, intent)

    val tasks = processor.converge(intent)

    return DryRunLaunchedIntentResult(
      reason = tasks.reason,
      steps = collectSteps(tasks.orchestrations)
    )
  }

  private fun collectSteps(orchestrations: List<OrchestrationRequest>): List<DryRunStep>
    = orchestrations.map { (name, _, description, job) ->
        DryRunStep(
          name = name,
          description = description,
          operations = job.map { s -> (s["name"] ?: s["type"]).toString() }
        )
      }
}

data class DryRunLaunchedIntentResult(
  val reason: String?,
  val steps: List<DryRunStep>
) : LaunchedIntentResult
