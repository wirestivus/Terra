package com.dfsek.terra.api.command;

import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.platform.CommandSender;

import java.util.List;

public interface CommandManager {
    void execute(String command, CommandSender sender, List<String> args) throws CommandException;

    void register(String name, Class<? extends CommandTemplate> clazz);
}
