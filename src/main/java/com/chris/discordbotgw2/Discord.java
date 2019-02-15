package com.chris.discordbotgw2;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class Discord {

    private static String token;
    @Value("${jda.discord.token}")
    public void setToken(String token) {
        this.token = token;
    }

    public void run() throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListener(new ApiRegister());
        builder.buildAsync();
    }

}
