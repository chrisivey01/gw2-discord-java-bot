package com.chris.discordbotgw2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@ComponentScan(basePackages = "com.chris.discordbotgw2")
public class DiscordBotGw2Application {

    public static void main(String[] args) throws LoginException {
        SpringApplication.run(DiscordBotGw2Application.class, args);
        Discord discord = new Discord();
        discord.run();
    }
}

