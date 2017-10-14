package net.shadowfacts.krypton.sass.config

import net.shadowfacts.krypton.config.Configuration
import net.shadowfacts.krypton.config.config

/**
 * @author shadowfacts
 */
var Configuration.sass: Boolean by config(java.lang.Boolean::parseBoolean, fallback = { false })