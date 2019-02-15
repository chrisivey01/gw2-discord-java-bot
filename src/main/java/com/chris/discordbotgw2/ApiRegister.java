package com.chris.discordbotgw2;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;

@Component
public class ApiRegister extends ListenerAdapter {
    private static Gw2Repo gw2Repo;
    public static int yaksBendServer = 1005;

    @Autowired
    public ApiRegister(Gw2Repo gw2Repo) {
        this.gw2Repo = gw2Repo;
    }

    public ApiRegister() {
    }

    WebClient webClient = WebClient.create();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();

        MessageChannel channel = event.getChannel();
        String content = message.getContentDisplay();
        Guild guild = event.getGuild();

        if (content.startsWith("!api")) {

            getInfo(content.substring(5), event, message);

            message
                    .delete()
                    .complete();
            message
                    .getChannel()
                    .sendMessage("Deleted to hide your information!")
                    .complete();
        }
    }

    //9F1DA7B3-F32A-024F-B76A-7A496E9A207F7EAF1AF3-DB60-493B-B4E5-5503BA064F6B
//    E7E236B4-F9D1-394E-BCEC-23885EE5CF535756B9D2-631B-4F28-9CFF-2EA3D93B79BA
//    7BE9D9A7-AABD-A144-8CD7-AAE0DCE7153B44C0F770-6AEA-41C0-9821-A96EF4C6C0BB
    public void getInfo(String api, MessageReceivedEvent event, Message message) {
        webClient
                .get()
                .uri("https://api.guildwars2.com/v2/account?access_token=" + api)
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .doOnSuccess(response -> {
                    AccountInfo accountInfo = new AccountInfo(
                            response.get("name").toString(),
                            (int) response.get("world"),
                            (int) response.get("wvw_rank"),
                            api,
                            event.getMessage()
                                    .getAuthor()
                                    .getIdLong(),
                            response.get("access")
                                    .toString()
                                    .replace("[", "")
                                    .replace("]", ""));
                    gw2Repo.save(accountInfo);

                    //verify users
                    verifyUser(accountInfo, event, message);

                })
                .doOnError(throwable -> {
                    System.out.println(throwable.toString());
                })
                .subscribe();

    }

    public void verifyUser(AccountInfo accountInfo, MessageReceivedEvent event, Message message) {
        if (accountInfo.getWorld() == yaksBendServer) {
            event
                    .getGuild()
                    .getController()
                    .addRolesToMember(event.getMember(),
                            event
                                    .getJDA()
                                    .getRolesByName("Verified", true))
                    .complete();

            message
                    .getChannel()
                    .sendMessage("Verified process is done!")
                    .complete();

        } else if (accountInfo.getWorld() == 1004) {
            message
                    .getChannel()
                    .sendMessage("Welcome linked man.")
                    .complete();
        } else {
            message
                    .getGuild()
                    .getController()
                    .removeRolesFromMember(event.getMember(),
                            event
                                    .getJDA()
                                    .getRolesByName("Verified", true)
                    )
                    .complete();
            message
                    .getChannel()
                    .sendMessage("You're not part of Yaks Bend or Links.")
                    .complete();
        }
    }
}
