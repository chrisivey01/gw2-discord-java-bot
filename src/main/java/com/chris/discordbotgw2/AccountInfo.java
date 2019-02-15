package com.chris.discordbotgw2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;


@Entity
public class AccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    private String name;
    private int world;
    private int wvw_rank;
    private String api_key;
    private Long discord_uid;
    private String access;
    private int on_yaks_or_links;

    public AccountInfo(String name, int world, int wvw_rank, String api_key, Long discord_uid, String access) {
        this.name = name;
        this.world = world;
        this.wvw_rank = wvw_rank;
        this.api_key = api_key;
        this.discord_uid = discord_uid;
        this.access = access;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public int getWvw_rank() {
        return wvw_rank;
    }

    public void setWvw_rank(int wvw_rank) {
        this.wvw_rank = wvw_rank;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public Long getDiscord_uid() {
        return discord_uid;
    }

    public void setDiscord_uid(Long discord_uid) {
        this.discord_uid = discord_uid;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
