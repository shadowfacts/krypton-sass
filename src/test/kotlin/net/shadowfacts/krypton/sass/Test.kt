package net.shadowfacts.krypton.sass

import net.shadowfacts.krypton.Krypton
import net.shadowfacts.krypton.config.Configuration
import net.shadowfacts.krypton.pipeline.selector.PipelineSelectorAnd
import net.shadowfacts.krypton.pipeline.selector.PipelineSelectorExtension
import net.shadowfacts.krypton.pipeline.selector.PipelineSelectorNot
import net.shadowfacts.krypton.pipeline.selector.PipelineSelectorPrefix
import net.shadowfacts.krypton.util.dependencies.Dependencies
import java.io.File

/**
 * @author shadowfacts
 */
fun main(args: Array<String>) {
	val krypton = Krypton(Configuration {
		source = File("source")
		output = File("output")
	})

	krypton.createPipeline {
		selector = PipelineSelectorAnd(
				PipelineSelectorExtension("scss"),
				PipelineSelectorNot(
						PipelineSelectorPrefix("_")
				)
		)
		addStage(StageCompileSass(), Dependencies {
		})
	}

	krypton.createPipeline {
		selector = PipelineSelectorAnd(
				PipelineSelectorExtension("scss"),
				PipelineSelectorPrefix("_")
		)
		final = null
	}

	krypton.generate()
}