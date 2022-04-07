#!/usr/bin/env bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

if [ -z $JAVA_HOME ]; then
   export JAVA_HOME=/home/scmtools/buildkit/java/jdk1.8.0_25
   export PATH=$JAVA_HOME/bin:$PATH
fi

if [ -z $MAVEN_HOME ]; then
   export MAVEN_HOME=/home/scmtools/buildkit/maven/apache-maven-3.3.9
   export PATH=$MAVEN_HOME/bin:$PATH
fi

PLUGIN_DIR=./hugegraph-dist/src/assembly/plugin
mvn install:install-file -Dfile=$PLUGIN_DIR/lib/hugegraph-plugin-1.0.0.jar -DgroupId=com.baidu.hugegraph -DartifactId=hugegraph-plugin -Dversion=1.0.0 -Dpackaging=jar -DpomFile=$PLUGIN_DIR/pom.xml
mvn install:install-file -Dfile=$PLUGIN_DIR/lib/syncgateway-1.0.0.jar -DgroupId=com.baidu.hugegraph -DartifactId=syncgateway -Dversion=1.0.0 -Dpackaging=jar -DpomFile=$PLUGIN_DIR/syncgateway-pom.xml