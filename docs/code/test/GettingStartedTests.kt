// This file was automatically generated from getting_started.md by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch")
package example.test

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*
import util.*
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.io.path.Path

class GettingStartedTests : FunSpec({

  context("ExampleGuideGettingStarted01") {

    val file = Path("./docs/code/example/example-guide-getting-started-01.main.kts").toFile()

    val actual = captureOutput("ExampleGuideGettingStarted01", true) {

      val res = evalFile(file)

      res.reports.forEach {
        if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
          println(it.prettyPrint())
        }
      }
    }.normalizeJoin()

    test("expect actual matches ") {
      actual.shouldBe(
        """
          |todo...
        """.trimMargin().normalize()
      )
    }
  }
})
