package bot.commands.moderation;

import bot.command.CommandContext;
import bot.command.ICommand;
import bot.utils.ModerationUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VUnMuteCommand extends ICommand {

    public VUnMuteCommand() {
        this.name = "vunmute";
        this.help = "unmute voice of the mentioned user on this guild";
        this.usage = "<@member(s)> [reason]";
        this.minArgsCount = 1;
        this.userPermissions = new Permission[]{Permission.VOICE_MUTE_OTHERS};
        this.botPermissions = new Permission[]{Permission.VOICE_MUTE_OTHERS};
    }

    @Override
    public void handle(@NotNull CommandContext ctx) {
        final Message message = ctx.getMessage();
        List<Member> targetMembers = message.getMentionedMembers();

        if (targetMembers.isEmpty()) {
            ctx.reply("Please @mention the member(s) you want to voice unmute!");
            return;
        }

        // Split content at last member mention
        String[] split = message.getContentRaw().split(targetMembers.get(targetMembers.size() - 1).getId() + ">");
        final String reason = (split.length > 1)
                ? String.join(" ", split[1].split("\\s+")).trim()
                : "No reason provided";

        targetMembers
                .stream()
                // Filter out members with which bot and command author can interact
                .filter(target -> ModerationUtils.canInteract(ctx.getMember(), target, "voice unmute", ctx.getChannel()))
                .forEach(member -> ModerationUtils.vunmute(message, member, reason));

    }

}
