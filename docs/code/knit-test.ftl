<#--@formatter:off-->
<#-- @ftlvariable name="test.name" type="java.lang.String" -->
<#-- @ftlvariable name="test.package" type="java.lang.String" -->
// This file was automatically generated from ${file.name} by Knit tool. Do not edit.
@file:Suppress("PackageDirectoryMismatch")
package ${test.package}

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import kotlinx.knit.test.*
import util.*
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.io.path.Path

class ${test.name} : FunSpec({

<#list cases as case><#assign method = test["mode.${case.param}"]!"custom">
  context("${case.name}") {

    val file = Path("./docs/code/example/example-guide-getting-started-01.main.kts").toFile()

    val actual = captureOutput("${case.name}", true) {
<#--      ${case.knit.package}.${case.knit.name}.main()-->

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
          <#list case.lines as line>
          |${line}
          </#list>
        """.trimMargin().normalize()
      )
    }
  }
<#sep>

</#list>
})
