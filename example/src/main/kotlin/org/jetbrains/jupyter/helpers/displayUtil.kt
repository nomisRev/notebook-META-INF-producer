package org.jetbrains.jupyter.helpers

import jupyter.kotlin.ScriptTemplateWithDisplayHelpers
import jupyter.kotlin.notebook
import org.jetbrains.kotlinx.jupyter.api.DisplayResult
import org.jetbrains.kotlinx.jupyter.api.KotlinKernelHost
import org.jetbrains.kotlinx.jupyter.api.MIME
import java.util.UUID

fun MD(markdown: String) = MIME("text/markdown" to markdown)
class UpdatableOutput<AggregatorT, AppendableT>(
    private val host: KotlinKernelHost,
    private val aggregator: AggregatorT,
    private val append: (AggregatorT, AppendableT) -> Unit,
    private val renderer: (AggregatorT) -> DisplayResult,
    private val id: String = UUID.randomUUID().toString()
) {
    private var isAdded = false
    fun append(appendable: AppendableT) {
        append(aggregator, appendable)
        val wasAdded = isAdded
        isAdded = true
        val newOutput = renderer(aggregator)
        if (wasAdded) {
            host.updateDisplay(newOutput, id = id)
        } else {
            host.display(newOutput, id = id)
        }
    }
}

private val ScriptTemplateWithDisplayHelpers.host get() = notebook.executionHost!!

fun KotlinKernelHost.displayMarkdown(markdown: String) = display(MD(markdown), null)
fun ScriptTemplateWithDisplayHelpers.displayMarkdown(markdown: String) = host.displayMarkdown(markdown)

fun KotlinKernelHost.updatableMarkdown() = UpdatableOutput(
    this,
    StringBuilder(),
    { builder, appendable: String -> builder.append(appendable) },
    { MD(it.toString()) }
)
fun ScriptTemplateWithDisplayHelpers.updatableMarkdown() = host.updatableMarkdown()
