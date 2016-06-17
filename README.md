GraphAware Recommendation Engine
================================

[![Build Status](https://travis-ci.org/graphaware/reco.svg)](https://travis-ci.org/graphaware/reco) | <a href="http://graphaware.com/site/reco/latest/apidocs/" target="_blank">Javadoc</a> | Latest Release: 1.0

GraphAware Recommendation Engine is a skeleton for building high-performance complex recommendation engines. It was originally
written for Neo4j only but later split into a separate repository by popular demand. You can use this to implement a recommendation
engine over your database or search engine of choice.

Please note that we do not support this library. We do support the [Neo4j implementation](https://github.com/graphaware/neo4j-reco), which should be considered the
reference implementation of a recommendation engine using this library, and should be your source of inspiration and documentation.

Key Features:

* Clean and flexible design
* High performance
* Ability to trade off recommendation quality for speed
* Ability to pre-compute recommendations
* Built-in algorithms and functions
* Ability to measure recommendation quality
* Ability to easily run in A/B test environments

The library imposes a specific recommendation engine architecture, which has emerged from our experience building recommendation
engines. In return, it offers high performance and handles most of the plumbing so that you only write
the recommendation business logic specific to your use case.

Getting the Software
--------------------

Releases are synced to <a href="http://search.maven.org/" target="_blank">Maven Central repository</a>. When using Maven for dependency management, include the following dependency in your pom.xml.

    <dependencies>
        ...
        <dependency>
            <groupId>com.graphaware</groupId>
            <artifactId>recommendation-engine</artifactId>
            <version>1.0</version>
        </dependency>
        ...
    </dependencies>

#### Snapshots

To use the latest development version, just clone this repository, run `mvn clean install` and change the version in the
dependency above to 1.1-SNAPSHOT.

License
-------

Copyright (c) 2016 GraphAware

GraphAware is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
