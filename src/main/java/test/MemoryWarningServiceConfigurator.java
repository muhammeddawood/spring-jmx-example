package test;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;


@Component
@ManagedResource(objectName = MemoryWarningServiceConfigurator.MBEAN_NAME, //
description = "Allows clients to set the memory threshold")
public class MemoryWarningServiceConfigurator implements
        ApplicationContextAware {

    

    /** The application logger */
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(MemoryWarningServiceConfigurator.class);

    public static final String MBEAN_NAME = "jemos.mbeans:type=config,name=MemoryWarningServiceConfiguration";

    private ApplicationContext ctx;

    @ManagedOperation(description = "Sets the memory threshold for the memory warning system")
    @ManagedOperationParameters({ @ManagedOperationParameter(description = "The memory threshold", name = "memoryThreshold"), })
    public void setMemoryThreshold(double memoryThreshold) {

        MemoryWarningService memoryWarningService = (MemoryWarningService) ctx
                .getBean("memoryWarningService");
        memoryWarningService.setPercentageUsageThreshold(memoryThreshold);

        LOG.info("Memory threshold set to " + memoryThreshold);
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        ctx = applicationContext;

    }


}

