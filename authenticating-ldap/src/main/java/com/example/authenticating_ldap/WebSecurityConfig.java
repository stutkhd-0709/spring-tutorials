package com.example.authenticating_ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // 全てのリクエストに対して認証済みである必要がある
      // fromLogin でSpring Securityにデフォルトのログインページを表示させるようにする
      http.authorizeHttpRequests((authorize) -> authorize.anyRequest().fullyAuthenticated())
              .formLogin(Customizer.withDefaults());
      return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                // ユーザーのDN（Distinguished Name）パターンを設定します
                // ユーザーIDが`uid`属性にマッピングされ、`ou=people`の下に存在することを指定しています。
                // `{0}`はユーザー名（例：ログインID）を表します。
                .userDnPatterns("uid={0},ou=people")
                // グループ検索のベースDNを設定します。ここでは、`ou=groups`の下でグループを検索するように指定しています。
                .groupSearchBase("ou=groups")
                //  LDAPコンテキストソースの設定を開始
                .contextSource()
                  .url("ldap://localhost:8389/dc=springframework,dc=org")
                  .and()
                .passwordCompare()
                  // パスワードをエンコードするアルゴリズム
                  .passwordEncoder(new BCryptPasswordEncoder())
                  // LDAPディレクトリ内でパスワードが保存されている属性を指定します。
                  // ここでは、`userPassword`属性にパスワードが保存されていることを示しています。
                  .passwordAttribute("userPassword");
    }

}
