package e.command.commands;

import e.command.CommandContext;
import e.command.ICommand;

public class HelloCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("Hello").queue();
    }

    @Override
    public String getName() {
        return "hi";
    }

    @Override
    public String getHelp() {
        return "Says hi";
    }
}
