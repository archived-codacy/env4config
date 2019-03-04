
# ENV4CONFIG

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c811f6b557ee4e44ad373084015ba0b3)](https://www.codacy.com/app/Codacy/env2props?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=codacy/env4config&amp;utm_campaign=Badge_Grade)
[![CircleCI](https://circleci.com/gh/codacy/env4config.svg?style=svg)](https://circleci.com/gh/codacy/env4config)
[![](https://img.shields.io/endpoint.svg?url=https://circleci.com/api/v1.1/project/github/codacy/env4config/latest/artifacts/0/version.json)](https://github.com/codacy/env4config/releases)


This is an alternative `config.strategy` for [Typesafe Config](https://github.com/lightbend/config) to enable Environment Variable substitution on every configuration key without the need to explicitly bind the substitution in the configuration file.

Please note that this library is published as a road unblocker until [this PR](https://github.com/lightbend/config/pull/620) find it's way upstream.

## Conventions

Each environment variable is transformed as follows:

 - remove the trailing prefix `config.env_var_prefix` ( Default: `CONFIG_`)
 - trasform all the `_` characters into `.`
 - trasform all the `__` characters into `-`
 - trasform all the `___` characters into `_`

## Usage

Import this library along with Typesafe Config:
```
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % <typesafe-config-version>,
  "com.codacy" % "env4config" % <version>
)
```
Then specify the proper `config.strategy` as a Java property:
```
java -Dconfig.strategy=com.codacy.config.EnvFirstConfigLoadingStrategy <....>
```

## Rationale

According to the [Twelve-factor App](https://12factor.net/config) the configuration should be passed to the applications via environment variables.

The "de-facto" standard for configuration in Scala application is [Typesafe Config](https://github.com/lightbend/config) and env variables are a [supported fallback](https://github.com/lightbend/config#optional-system-or-env-variable-overrides); still it is not possible to override *virtually* any configuration of our application if not properly encoded the binding accordingly.

Although [Typesafe Config](https://github.com/lightbend/config#overview) offers an hook as a `config.strategy` to tune the initial loading order of configurations, leveraging it is possible to load Environment variables first.

This makes it easy to bridge the gap and by having a standard way to convert environment variables into Java system properties make it possible to fully comply with [Twelve-factor App](https://12factor.net/config).

## Compile

You need to have the crystal compiler available in your classpath to succesfully build this utility, otherwise you can compile it statically using a docker image using `make buildStatic`.

## What is Codacy

[Codacy](https://www.codacy.com/) is an Automated Code Review Tool that monitors your technical debt, helps you improve your code quality, teaches best practices to your developers, and helps you save time in Code Reviews.

### Among Codacy’s features

- Identify new Static Analysis issues
- Commit and Pull Request Analysis with GitHub, BitBucket/Stash, GitLab (and also direct git repositories)
- Auto-comments on Commits and Pull Requests
- Integrations with Slack, HipChat, Jira, YouTrack
- Track issues in Code Style, Security, Error Proneness, Performance, Unused Code and other categories

Codacy also helps keep track of Code Coverage, Code Duplication, and Code Complexity.

Codacy supports PHP, Python, Ruby, Java, JavaScript, and Scala, among others.

## Free for Open Source

Codacy is free for Open Source projects.

## License

env4config is available under the Apache 2 license. See the LICENSE file for more info.
