package io.collective.restsupport

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.slf4j.LoggerFactory

abstract class BasicApp(val port: Int) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private lateinit var server: Server

    protected abstract fun handlerList(): HandlerList

    open fun start() {
        val list = this.handlerList()
        server = Server(port)
        server.handler = list
        server.stopAtShutdown = true
        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                if (server.isRunning) {
                    server.stop()
                }
                logger.info("App shutdown.")
            } catch (e: Exception) {
                logger.info("Error shutting down app.", e)
            }
        })

        logger.info("App started.")
        server.start()
    }

    fun stop() {
        logger.info("App stopped.")
        server.stop()
    }
}