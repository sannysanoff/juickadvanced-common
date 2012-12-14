package com.juickadvanced.xmpp.messages;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 12/12/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PongFromServer {
    long ts;
    boolean shouldResetConnectionStatistics;

    public PongFromServer(long ts) {
        this.ts = ts;
    }

    public boolean isShouldResetConnectionStatistics() {
        return shouldResetConnectionStatistics;
    }

    public void setShouldResetConnectionStatistics(boolean shouldResetConnectionStatistics) {
        this.shouldResetConnectionStatistics = shouldResetConnectionStatistics;
    }
}
