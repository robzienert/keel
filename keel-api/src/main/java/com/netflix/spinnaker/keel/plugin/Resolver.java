/*
 * Copyright 2019 Netflix, Inc.
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
package com.netflix.spinnaker.keel.plugin;

import com.netflix.spinnaker.keel.api.ApiVersion;
import com.netflix.spinnaker.keel.api.Resource;
import com.netflix.spinnaker.keel.api.ResourceSpec;
import java.util.function.Function;

/**
 * Implementations apply changes to a {@link ResourceSpec} such as adding default values, applying
 * opinions, or resolving references.
 */
public interface Resolver<T extends ResourceSpec> extends Function<Resource<T>, Resource<T>> {

  /** TODO(rz): Document. */
  ApiVersion getApiVersion();

  /** TODO(rz): Document. */
  String getSupportedKind();
}
