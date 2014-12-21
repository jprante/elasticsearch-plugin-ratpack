/**
 *    Copyright 2014 Jörg Prante
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xbib.elasticsearch.module.ratpack

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse
import org.elasticsearch.client.Client
import org.elasticsearch.common.component.AbstractComponent
import org.elasticsearch.common.inject.Inject
import org.elasticsearch.common.settings.Settings
import ratpack.embed.BaseDirBuilder
import ratpack.embed.GroovyEmbeddedApp
import ratpack.jackson.JacksonModule
import groovy.util.logging.Slf4j

@Slf4j
class RatpackService extends AbstractComponent {

    @Inject
    RatpackService(Settings settings, Client client) {
        super(settings)
        GroovyEmbeddedApp.build {
            baseDir {
                BaseDirBuilder.tmpDir().build {
                    it.file "public/foo.txt", "bar"
                }
            }

            launchConfig {
                development true
                port settings.getAsInt("ratpack.port", 5050)
                other "some.other.property": "value"
            }

            // Configure the module registry
            bindings {
                add new JacksonModule()
            }

            // Use the GroovyChain DSL for defining the application handlers
            handlers {
                get {
                    NodesInfoResponse response = client.admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet()
                    render "Hello, here is Ratpack with the Elasticsearch nodes info: " + response
                }
                assets "public"
            }
        }
        .server.start()
    }
}
