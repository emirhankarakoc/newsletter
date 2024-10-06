package com.karakoc.enewsletter.oauth2.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2AccountRepository extends JpaRepository<OAuth2Account,String> {
}
