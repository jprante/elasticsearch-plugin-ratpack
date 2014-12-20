![Ratpack](https://github.com/jprante/elasticsearch-plugin-ratpack/raw/master/src/site/resources/ratpack.png)

# Elasticsearch Ratpack plugin

This plugin demonstrates how [Ratpack](http://www.ratpack.io) applications can be embedded into Elasticsearch.

Ratpack is a set of Java libraries that facilitate fast, efficient, evolvable and well tested HTTP applications.

It is built on the highly performant and efficient Netty event-driven networking engine.

Ratpack focuses on allowing HTTP applications to be efficient, modular, adaptive to new requirements and
technologies, and well-tested over time.

This plugin is written in Groovy and uses Gradle as build system. With the
endorsement of Groovy in Elasticsearch, embedding Ratpack applications into Elasticsearch is an interesting
option.

For demonstration purpose, you can issue a HTTP GET request to port 5050 of the Elasticsearch node after plugin
installation.

     curl '0:5050'

As an example result, you will see the Elasticsearch node info, retrieved by Ratpack.

## Versions

| Release date | Plugin version | Elasticsearch version | Ratpack version |
| -------------| ---------------| ----------------------|-----------------|
| Dec 20, 2014 | 1.4.0.0        | 1.4.0                 | 0.9.11          |

## Prerequisites

- Java 8
- Groovy 2.3.6

## Installation

    ./bin/plugin --install ratpack --url http://xbib.org/repository/org/xbib/elasticsearch/plugin/elasticsearch-plugin-ratpack/1.4.0.0/elasticsearch-plugin-ratpack-1.4.0.0-plugin.zip

Do not forget to restart the nodes after installing.

Note: this plugin comes with Groovy 2.3.6. If there is a Groovy jar in the 'lib' folder of Elasticsearch, you will get an error

    org.elasticsearch.ElasticsearchException: Failed to load plugin class [org.xbib.elasticsearch.plugin.ratpack.RatpackPlugin]
    [...]
    Caused by: groovy.lang.GroovyRuntimeException: Conflicting module versions. Module [groovy-all is loaded in version 2.3.2 and you are trying to load version 2.3.6

In that case, use the newer instead of the older Groovy jar, and ensure you have only one Groovy jar in the Elasticsearch classpath.

For example

    rm lib/groovy-all-2.3.2.jar
    mv plugins/ratpack/groovy-all-2.3.6.jar lib/

## Project docs

The Maven project site is available at [Github](http://jprante.github.io/elasticsearch-plugin-ratpack)

## Issues

All feedback is welcome! If you find issues, please post them at
[Github](https://github.com/jprante/elasticsearch-plugin-ratpack/issues)

# License

Elasticsearch Ratpack Plugin

Copyright (C) 2014 Jörg Prante

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
