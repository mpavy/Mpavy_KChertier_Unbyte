# Mpavy_KChertier_Unbyte [![Build Status](https://travis-ci.com/mpavy/Mpavy_KChertier_Unbyte.svg?token=YBFWnzKMP11Qg5GWtBzy&branch=master)](https://travis-ci.com/mpavy/Mpavy_KChertier_Unbyte) [![codecov](https://codecov.io/gh/mpavy/Mpavy_KChertier_Unbyte/branch/master/graph/badge.svg?token=85BB1SR64X)](https://codecov.io/gh/mpavy/Mpavy_KChertier_Unbyte)

## Development

#### User manual
<pre><code>Usage: Mpavy_KChertier_Unbyte [-h] [--compress] [--decompress]
                                    [-algo=&lt;algorithm>] [-i=&lt;path>]
                                    [-out=&lt;outputFileName>]

-algo=&lt;algorithm> =  The algorithm you want to use : Huffman (default) or
                            LZW

--compress = Compress the file (default)

--decompress  =      Decompress the file

-h, --help     =         Display a help message

-i, --input=&lt;path>   =   Enter the input file path

-out=&lt;outputFileName> =  Enter the output file name
</code></pre>
                          

#### Development process

The development process was separated in two phases:
 First, we had to understand the theory behind each algorithm, decompose them into steps.
 Then, we had to actually write code, and test.
 
While understanding the algorithms looks complicated, they are mostly straight forward once cut into simpler steps.
As an example, LZW's algorithm can be separated into a few steps :
 - Construct a map of every character and its associated code
 - Go through the file and find the longest prefix not in the map
 - Add the code for the longest prefix in the map to the result
 - Add an entry for the one not in the map, with the next available code.
 - repeat until the file is empty
 
 Because of this, the code is build around this separation, each concern has its associated function.
 
#### Difficulties

The most challenging part is working with individual bits. Writing only three bits into a file is not straight forward.

For LZW, we used a workaround by transforming everything into a string of 0 and 1, and iterating over this. It is however not optimal.

As of right now, the Huffman implementation is not completed. Compression is working until when we need to write the tree and code into a file. Decompression is not reading the input file, nor writing the result. It would work if a tree is provided (by modifying the current code).

## Tests

#### Continuous integration

We chose Travis CI as CI provider. 
After every commit, a build is launched and the tests are run.
Travis was very quick to setup and easy to use. 

Travis builds results are automatically displayed in our Github repository on each commit. An email is also sent to the commit author with the result of CI (build failed or passed).
A badge which displays the latest commit result can also be added to the repository documentation. 

[>Click here to see the build history<](https://travis-ci.com/mpavy/Mpavy_KChertier_Unbyte/builds)

#### Unit tests
The unit test suite was written using JUnit. 

#### Code coverage
Code coverage reports were produced after each commit using [JaCoCo](https://github.com/jacoco/jacoco). We also set up an automatic upload of the reports in [codecov.io](https://codecov.io/).
Using codecov allows us to monitor the code coverage easily. We can also monitor the evolution of coverage between commits.

[>Click here to see the coverage history<](https://codecov.io/gh/mpavy/Mpavy_KChertier_Unbyte/commits)

The code coverage percentage and evolution are also displayed on each Github commit, which makes it easier for monitoring.

The codecov.io checks don't pass if the commit makes the code coverage go lower and if the coverage of the lines added or modified are lower than the previous total coverage of the project.

#### Static analysis
Static analysis is performed thanks to [Spotbugs](https://spotbugs.github.io/).
The build fails if Spotbugs finds any error.

Spotbugs helped us respect good practices and improve our code.

#### Mutation testing
[PIT](https://pitest.org/) mutation analysis is run at each build.
Travis Build log will show us the mutation coverage but we have to run Pitest locally to get the detailed report.

Mutation testing was interesting as it allowed us to rewrite our code in a better way, by simplifying some parts that had failed to kill mutants. 
Thanks to mutation results, we also wrote a more complete unit test suite.

#### Process
- After every build, if the build has failed, an email is send to the person who has pushed on the repository.

- We then create an Github issue with the corresponding errors (could be build fail, unit test fail or static analysis reported bugs).

- Once the issue is solved, we indicate the solving commit in the issue before closing it.

[>Click here to see the issues<](https://github.com/mpavy/Mpavy_KChertier_Unbyte/issues?utf8=%E2%9C%93&q=)

#### Difficulties, solutions, improvement ideas

Following the developement of the algorithms, we had to change our Java version (from 8 to 11). We changed the CI configuration accordingly.

Spotbugs raised the bug "URF_UNREAD_FIELD" concerning the boolean attribute helpRequested from picocli and that made the build fail. 
To make Spotbugs cohabit with picocli, we excluded the attribute helpRequested from the analysis using spotbugs-exclude.xml.

We could have automate more the mutation testing by uploading automatically the reports using SonarQube for example.
Using it may also have allowed us to keep the coverage and mutation reports on the same platform.

Developers : Myriam Pavy and Kévin Chertier, Master 2 ILA ISTIC (2019-2020)
