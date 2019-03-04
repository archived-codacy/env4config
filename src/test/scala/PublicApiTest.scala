/**
  *   Copyright (C) 2011 Typesafe Inc. <http://typesafe.com>
  */
package com.typesafe.config.impl

import org.junit.Assert._
import org.junit._
import com.typesafe.config._
import java.util.TimeZone

class PublicApiTest extends TestUtils {

  @Before
  def before(): Unit = {
    // TimeZone.getDefault internally invokes System.setProperty("user.timezone", <default time zone>) and it may
    // cause flaky tests depending on tests order and jvm options. This method is invoked
    // eg. by URLConnection.getContentType (it reads headers and gets default time zone).
    TimeZone.getDefault
  }

  @Test
  def supportsEnvFirstConfigLoadingStrategy(): Unit = {
    assertEquals("config.strategy is not set", null, System.getProperty("config.strategy"))

    TestEnvFirstStrategy.putEnvVar("CONFIG_a", "5")
    System.setProperty("config.strategy", classOf[com.codacy.config.EnvFirstConfigLoadingStrategy].getCanonicalName)

    try {
      val loaderA1 = new TestClassLoader(
        this.getClass().getClassLoader(),
        Map("reference.conf" -> resourceFile("a_1.conf").toURI.toURL())
      )

      val configA1 = withContextClassLoader(loaderA1) {
        ConfigFactory.load()
      }

      assertEquals(5, configA1.getInt("a"))
    } finally {
      System.clearProperty("config.strategy")
      TestEnvFirstStrategy.removeEnvVar("CONFIG_a")
    }
  }

}

class TestStrategy extends DefaultConfigLoadingStrategy {
  override def parseApplicationConfig(parseOptions: ConfigParseOptions): Config = {
    TestStrategy.increment()
    super.parseApplicationConfig(parseOptions)
  }
}

object TestStrategy {
  private var invocations = 0
  def getIncovations() = invocations
  def increment() = invocations += 1
}

object TestEnvFirstStrategy extends com.codacy.config.EnvFirstConfigLoadingStrategy {

  def putEnvVar(key: String, value: String) =
    com.codacy.config.EnvFirstConfigLoadingStrategy.env.put(key, value)

  def removeEnvVar(key: String) =
    com.codacy.config.EnvFirstConfigLoadingStrategy.env.remove(key)
}
