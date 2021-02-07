package bot.commands.automod;

import bot.command.CommandCategory;
import bot.command.CommandContext;
import bot.command.ICommand;
import bot.database.DataSource;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class AntiInvitesCommand extends ICommand {

    public AntiInvitesCommand() {
        this.name = "antiinvites";
        this.minArgsCount = 1;
        this.usage = "<ON | OFF>";
        this.aliases = Collections.singletonList("antiinvite");
        this.help = "Allow or disallow sending discord links in message";
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
        this.category = CommandCategory.AUTOMOD;
    }

    @Override
    public void handle(@NotNull CommandContext ctx) {
        final String input = ctx.getArgs().get(0);
        boolean antiinvites;

        if (input.equalsIgnoreCase("none") || input.equalsIgnoreCase("off"))
            antiinvites = true;
        else if (input.equalsIgnoreCase("on"))
            antiinvites = false;
        else {
            ctx.reply("Not a valid input");
            return;
        }

        DataSource.INS.antiInvites(ctx.getGuildId(), antiinvites);

        if (antiinvites)
            ctx.reply("Messages with not be filtered for discord links now");
        else
            ctx.reply("Messages with discord invite links will now be automatically deleted");

    }

}