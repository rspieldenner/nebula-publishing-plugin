/*
 * Copyright 2015-2017 Netflix, Inc.
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
package nebula.plugin.publishing.maven

import nebula.test.IntegrationTestKitSpec

class MavenManifestPluginIntegrationSpec extends IntegrationTestKitSpec {
    def setup() {
        buildFile << """\
            plugins {
                id 'nebula.maven-manifest'
                id 'nebula.maven-nebula-publish'
            }

            version = '0.1.0'
            group = 'test.nebula'
        """.stripIndent()

        settingsFile << '''\
            rootProject.name = 'manifestpomtest'
        '''.stripIndent()
    }

    def 'manifest created'() {
        buildFile << '''\
            apply plugin: 'java'
            apply plugin: 'nebula.info'
        '''

        when:
        runTasks('generatePomFileForNebulaPublication')

        then:
        def pom = new XmlSlurper().parse(new File(projectDir, 'build/publications/nebula/pom-default.xml'))
        pom.properties.children().size() > 1
        pom.properties.'nebula_Implementation_Version' == '0.1.0'
        pom.properties.'nebula_Implementation_Title' == 'test.nebula#manifestpomtest;0.1.0'
    }
}
