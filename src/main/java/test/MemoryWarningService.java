package test;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

import javax.annotation.PostConstruct;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A component which sends notifications when the HEAP memory is above a certain
 * threshold.
 * 
 * @author mtedone
 * 
 */
public class MemoryWarningService implements NotificationListener {

    /** This bean's name */
    public static final String MBEAN_NAME = "jemos.mbeans:type=monitoring,name=MemoryWarningService";

    /** The application logger */
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(MemoryWarningService.class);

    @Autowired
    private NotificationEmitter memoryMxBean;

    @Autowired
    private MemoryThreadDumper threadDumper;

    /** A pool of Memory MX Beans specialised in HEAP management */
    private static final MemoryPoolMXBean tenuredGenPool = findTenuredGenPool();

    /**
     * {@inheritDoc}
     */
    public void handleNotification(Notification notification, Object handback) {

        if (notification.getType().equals(
                MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) {
            long maxMemory = tenuredGenPool.getUsage().getMax();
            long usedMemory = tenuredGenPool.getUsage().getUsed();
            LOG.warn("Memory usage low!!!");
            double percentageUsed = (double) usedMemory / maxMemory;
            LOG.warn("percentageUsed = " + percentageUsed);
            threadDumper.dumpStacks();

        } else {
            LOG.info("Other notification received..."
                    + notification.getMessage());
        }

    }

    /**
     * It sets the threshold percentage.
     * 
     * @param percentage
     */
    public void setPercentageUsageThreshold(double percentage) {
        if (percentage <= 0.0 || percentage > 1.0) {
            throw new IllegalArgumentException("Percentage not in range");
        } else {
            LOG.info("Percentage is: " + percentage);
        }
        long maxMemory = tenuredGenPool.getUsage().getMax();
        long warningThreshold = (long) (maxMemory * percentage);
        tenuredGenPool.setUsageThreshold(warningThreshold);
    }

    @PostConstruct
    public void completeSetup() {
        memoryMxBean.addNotificationListener(this, null, null);
        LOG.info("Listener added to JMX bean");
    }

    /**
     * Tenured Space Pool can be determined by it being of type HEAP and by it
     * being possible to set the usage threshold.
     */
    private static MemoryPoolMXBean findTenuredGenPool() {
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            // I don't know whether this approach is better, or whether
            // we should rather check for the pool name "Tenured Gen"?
            if (pool.getType() == MemoryType.HEAP
                    && pool.isUsageThresholdSupported()) {
                return pool;
            }
        }
        throw new AssertionError("Could not find tenured space");
    }

}
