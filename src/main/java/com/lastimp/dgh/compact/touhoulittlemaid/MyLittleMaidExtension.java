package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

@LittleMaidExtension
public class MyLittleMaidExtension implements ILittleMaid {
    public MyLittleMaidExtension() {
    }

    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new TaskBringToBed());
    }
}
