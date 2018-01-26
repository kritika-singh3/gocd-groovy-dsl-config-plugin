/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.contrib.plugins.configrepo.groovy.dsl

import cd.go.contrib.plugins.configrepo.groovy.sandbox.GroovyScriptRunner
import net.javacrumbs.jsonunit.fluent.JsonFluentAssert
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

import static org.assertj.core.api.Assertions.assertThat

class TransformTest {
  GroovyScriptRunner runner

  @ParameterizedTest
  @MethodSource("values")
  void testTransformParts(String path) {
    def engine = getRunner()
    def result = engine.runScript(path + '.groovy')
    assertThat(result).isInstanceOf(Node)

    def actualJson = (result as Node).toJsonString()
    def expectedJSON = new File(path + '.json').getText('utf-8')
    JsonFluentAssert.assertThatJson(actualJson).isEqualTo(expectedJSON)
  }

  private GroovyScriptRunner getRunner() throws IOException {
    if (runner == null) {
      runner = new GroovyScriptRunner(".", Pipeline.class.getPackage().getName())
    }
    return runner
  }

  static Stream<String> values() {
    Files
      .walk(Paths.get("src", "test", "resources"))
      .filter({ Files.isRegularFile(it) && it.toFile().name.endsWith('.groovy') })
      .map { it -> it.toString().replaceAll(/\.groovy$/, '')
    }
  }
}
