package net.shadowfacts.krypton.sass

import io.bit3.jsass.Compiler
import io.bit3.jsass.Options
import io.bit3.jsass.importer.Import
import io.bit3.jsass.importer.Importer
import net.shadowfacts.krypton.Page
import net.shadowfacts.krypton.pipeline.stage.Stage
import net.shadowfacts.krypton.sass.config.sass
import net.shadowfacts.krypton.util.withExtension
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author shadowfacts
 */
class StageCompileSass: Stage() {

	companion object {
		private val PREFIXES = arrayOf("_", "")
		private val SUFFIXES = arrayOf(".scss", ".css", "")
	}

	override val id = "sass"

	private val compiler = Compiler()

	override fun scan(page: Page) {
	}

	override fun apply(page: Page, input: String): String {
		page.output = page.output.withExtension("css")

		val sass = page.krypton.config.sass

		val output = compiler.compileString(input, Options().apply {
			importers = listOf(createImporter(page))
			setIsIndentedSyntaxSrc(sass)
		})
		return output.css
	}

	private fun createImporter(page: Page): Importer {
		return Importer { url, previous ->
			val path = if (url.startsWith("/")) {
				page.krypton.config.source.toPath().resolve(url)
			} else {
				page.source.parentFile.toPath().resolve(url)
			}
			listOf(resolveImport(path))
		}
	}

	private fun resolveImport(path: Path): Import {
		val resource = resolveResource(path)
		val source = resource.toFile().readText(Charsets.UTF_8)
		return Import(resource.toUri(), resource.toUri(), source)
	}

	private fun resolveResource(path: Path): Path {
		val dir = path.parent
		val basename = path.fileName.toString()

		for (prefix in PREFIXES) {
			for (suffix in SUFFIXES) {
				val target = dir.resolve(prefix + basename + suffix)

				if (Files.exists(target)) {
					return target
				}
			}
		}

		throw RuntimeException("Unable to resolve import $path")
	}

}