#!/usr/bin/env kotlin

@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.13.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.file.Paths

val workflow = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/hello_world_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/hello_world_workflow.yml")
) {
    job(id = "task-id", name = "test_job", runsOn = UbuntuLatest) {
        uses(name = "Check out", action = CheckoutV3())
        run(name = "Print greeting", command = "echo 'Hello world!'")
    }
}

println(workflow.toYaml())
