package com.chris.discordbotgw2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gw2Repo extends CrudRepository<AccountInfo, Integer> {
}
