package test

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.Table3
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.scriptgenerator.valueWithTyping
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing

class TypingsTest : FunSpec({
    val coords = ActionCoords("8398a7", "action-slack", "v3")
    val intSpecialTyping = IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0))
    val enumTyping = EnumTyping(
        "Status",
        listOf("success", "failure", "cancelled", "custom"),
        listOf("Success", "Failure", "Cancelled", "CustomEnum"),
    )
    val dollar = '$'.toString()

    val testCases: Table3<String, String, Typing> = table(
        headers("value", "expected", "typing"),
        row("true", "true", BooleanTyping),
        row("42", "42", IntegerTyping),
        row("$dollar{{ some condition }}", "\"$dollar{'$dollar'}{{ some condition }}\"", IntegerTyping),
        row("42", "ActionSlackV3.FetchDepth.Value(42)", intSpecialTyping),
        row("0", "ActionSlackV3.FetchDepth.Infinite", intSpecialTyping),
        row("success", "ActionSlackV3.Status.Success", enumTyping),
        row("custom", "ActionSlackV3.Status.CustomEnum", enumTyping),
        row(
            "field1,field2",
            """|listOf(
              |  "field1",
              |  "field2",
              |)
              |
            """.trimMargin(),
            ListOfTypings(","),
        ),
        row(
            "tag1\ntag2",
            """|listOf(
              |  "tag1",
              |  "tag2",
              |)
              |
            """.trimMargin(),
            ListOfTypings("\\n"),
        ),
        row(
            "success,custom",
            """|listOf(
              |  ActionSlackV3.Status.Success,
              |  ActionSlackV3.Status.CustomEnum,
              |)
              |
            """.trimMargin(),
            ListOfTypings(",", enumTyping),
        ),
    )

    test("typings") {
        testCases.forAll { value, expected, typing ->
            valueWithTyping(value, typing, coords).toString() shouldBe expected
        }
    }
},)
