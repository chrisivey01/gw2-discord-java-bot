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
    public int yaksBendServer = 1003;

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
        long authorID = event.getMessage().getAuthor().getIdLong();


        MessageChannel channel = event.getChannel();
        String content = message.getContentDisplay();
        Guild guild = event.getGuild();

        if (content.startsWith("!api")) {
            String capturedContent = content.substring(5);

            getInfo(authorID, capturedContent, event, message);

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
    public void getInfo(Long id, String api, MessageReceivedEvent event, Message message) {
        webClient
                .get()
                .uri("https://api.guildwars2.com/v2/account?access_token=" + api)
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .doOnSuccess(response -> {
                    AccountInfo accountInfo = new AccountInfo();
                    accountInfo.setDiscord_uid(id);
                    accountInfo.setName((String) response.get("name"));
                    accountInfo.setWorld((Integer) response.get("world"));
                    accountInfo.setWvw_rank((Integer) response.get("wvw_rank"));
                    accountInfo.setApi_key(api);
                    String accessList = response
                            .get("access")
                            .toString()
                            .replace("[", "")
                            .replace("]", "");
                    accountInfo.setAccess(accessList);
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
                    .sendMessage("Verified process is done!");

        } else if (accountInfo.getWorld() == 1004) {
            message
                    .getChannel()
                    .sendMessage("Welcome linked man.");
        } else {
            message
                    .getChannel()
                    .sendMessage("You're not part of Yaks Bend");
        }
    }
}
