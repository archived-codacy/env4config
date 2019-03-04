/**
  *   Copyright (C) 2011 Typesafe Inc. <http://typesafe.com>
  */
package com.typesafe.config.impl

import org.junit.Assert._
import org.junit._
import com.typesafe.config._

class ConfigTest extends TestUtils {

  @Test
  def testLoadWithEnvSubstitutions() {
    TestEnvFirstStrategy.putEnvVar("CONFIG_42___a", "1")
    TestEnvFirstStrategy.putEnvVar("CONFIG_a_b_c", "2")
    TestEnvFirstStrategy.putEnvVar("CONFIG_a__c", "3")
    TestEnvFirstStrategy.putEnvVar("CONFIG_a___c", "4")

    TestEnvFirstStrategy.putEnvVar("CONFIG_akka_version", "foo")
    TestEnvFirstStrategy.putEnvVar("CONFIG_akka_event__handler__dispatcher_max__pool__size", "10")

    System.setProperty("config.strategy", classOf[com.codacy.config.EnvFirstConfigLoadingStrategy].getCanonicalName)

    try {
      val loader02 = new TestClassLoader(
        this.getClass().getClassLoader(),
        Map("reference.conf" -> resourceFile("test02.conf").toURI.toURL())
      )

      val loader04 = new TestClassLoader(
        this.getClass().getClassLoader(),
        Map("reference.conf" -> resourceFile("test04.conf").toURI.toURL())
      )

      val conf02 = withContextClassLoader(loader02) {
        ConfigFactory.load()
      }

      val conf04 = withContextClassLoader(loader04) {
        ConfigFactory.load()
      }

      assertEquals(1, conf02.getInt("42_a"))
      assertEquals(2, conf02.getInt("a.b.c"))
      assertEquals(3, conf02.getInt("a-c"))
      assertEquals(4, conf02.getInt("a_c"))

      assertEquals("foo", conf04.getString("akka.version"))
      assertEquals(10, conf04.getInt("akka.event-handler-dispatcher.max-pool-size"))
    } finally {
      System.clearProperty("config.strategy")

      TestEnvFirstStrategy.removeEnvVar("CONFIG_42___a")
      TestEnvFirstStrategy.removeEnvVar("CONFIG_a_b_c")
      TestEnvFirstStrategy.removeEnvVar("CONFIG_a__c")
      TestEnvFirstStrategy.removeEnvVar("CONFIG_a___c")

      TestEnvFirstStrategy.removeEnvVar("CONFIG_akka_version")
      TestEnvFirstStrategy.removeEnvVar("CONFIG_akka_event__handler__dispatcher_max__pool__size")
    }
  }

  @Test
  def testLoadWithEnvSubstitutionsFromBuildSbt() {
    System.setProperty("config.strategy", classOf[com.codacy.config.EnvFirstConfigLoadingStrategy].getCanonicalName)

    val conf = ConfigFactory.load()

    val list = conf.getIntList("testList")

    assertEquals(0, list.get(0))
    assertEquals(1, list.get(1))
  }

}
