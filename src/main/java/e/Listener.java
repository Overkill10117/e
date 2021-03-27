package e;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Listener extends ListenerAdapter {
    int seconds;
    Message message;
    String item;

    public void Giveawayy(int time, Message message, String item) {
        seconds = time;
        this.message = message;
        this.item = item;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager;

    public Listener(EventWaiter waiter) {
        manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }


    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        final int memberCount = event.getGuild().getMemberCount();
        String lol = String.format("GoodBye %s we have " + memberCount + " members now",
                event.getUser().getAsMention(), event.getGuild().getName());
        String icon = event.getUser().getEffectiveAvatarUrl();
        if (lol.isEmpty()) {
            return;
        }
        final EmbedBuilder useGuildSpecificSettingsInstead = EmbedUtils.defaultEmbed()
                .setDescription(lol)
                .setThumbnail(icon);


        Objects.requireNonNull(event.getGuild().getSystemChannel()).sendMessage(useGuildSpecificSettingsInstead.build()).queue();

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final String useGuildSpecificSettingsInstead = String.format("Thank you for adding %s to %s!!!'n" +
                        "To know about this bot feel free to type ,help\n" +
                        "To know more about a command type ,help [command name]",
                event.getJDA().getSelfUser().getAsMention(), event.getGuild().getName());

        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(useGuildSpecificSettingsInstead).queue();
    }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }


        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("owner_id"))) {
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());

            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }

    }
}
