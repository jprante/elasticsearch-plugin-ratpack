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
import ratpack.launch.LaunchConfig
import ratpack.launch.LaunchConfigBuilder
import ratpack.server.RatpackServerBuilder
import groovy.util.logging.Slf4j

import static ratpack.groovy.Groovy.chain

@Slf4j
class RatpackService extends AbstractComponent {

    @Inject
    RatpackService(Settings settings, Client client) {
        super(settings)
        LaunchConfig launchConfig = LaunchConfigBuilder
                .noBaseDir()
                .port(settings.getAsInt("ratpack.port", 5050))
                .build {
            chain(it) {
                get {
                    NodesInfoResponse response = client.admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet()
                    render "Hello, here is Ratpack with the Elasticsearch nodes info: " + response
                }
            }
        }
        RatpackServerBuilder.build(launchConfig).start()
    }
}
