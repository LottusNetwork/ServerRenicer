package org.lottus;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;

@SuppressWarnings("unused")
public final class ServerRenicer extends JavaPlugin {

    @Nullable
    private Process process = null;

    @Override
    public void onLoad() {
        String command = String.format("ls /proc/%d/task | sudo xargs renice -n -10", ProcessHandle.current().pid());
        try {
            this.process = new ProcessBuilder("/bin/sh", "-c", command)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                    .redirectError(ProcessBuilder.Redirect.DISCARD)
                    .start();

            @NotNull Logger logger = this.getSLF4JLogger();
            logger.atInfo().log("Successfully executed 'renice' command");
            logger.atInfo().log("Check niceness using 'htop' command. If niceness was unaffected, check the guide's considerations:");
            logger.atInfo().log("https://github.com/LottusNetwork/ServerRenicer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        if (this.process != null && this.process.isAlive()) this.process.destroy();
    }
}
