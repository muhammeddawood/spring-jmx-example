package test;
/**
 *
 */

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * MBean which allows clients to change or retrieve the logging level for a
 * Log4j Logger at runtime.
 *
 * @author mtedone
 *
 */
@Component
@ManagedResource(objectName = LoggerConfigurator.MBEAN_NAME, //
description = "Allows clients to set the Log4j Logger level at runtime")
public class LoggerConfigurator {

	public static final String MBEAN_NAME = "jemos.mbeans:type=config,name=LoggingConfiguration";

	@ManagedOperation(description = "Returns the Logger LEVEL for the given logger name")
	@ManagedOperationParameters({ @ManagedOperationParameter(description = "The Logger Name", name = "loggerName"), })
	public String getLoggerLevel(String loggerName) {

		Logger logger = Logger.getLogger(loggerName);
		Level loggerLevel = logger.getLevel();

		return loggerLevel == null ? "The logger " + loggerName
				+ " has not level" : loggerLevel.toString();

	}

	@ManagedOperation(description = "Set Logger Level")
	@ManagedOperationParameters({
		@ManagedOperationParameter(description = "The Logger Name", name = "loggerName"),
		@ManagedOperationParameter(description = "The Level to which the Logger must be set", name = "loggerLevel") })
	public void setLoggerLevel(String loggerName, String loggerLevel) {

		Logger thisLogger = Logger.getLogger(this.getClass());
		thisLogger.setLevel(Level.INFO);

		Logger logger = Logger.getLogger(loggerName);

		logger.setLevel(Level.toLevel(loggerLevel, Level.INFO));

		thisLogger.info("Set logger " + loggerName + " to level "
				+ logger.getLevel());

	}

}