package com.lastimp.dgh.common.event;

public interface ICancelable {
    boolean isCanceled();
    void setCanceled(boolean canceled);
}
