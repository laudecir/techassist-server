package com.hasckel.techassist.config;

import com.hasckel.techassist.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.config.resource.id}")
    private String resourceId;

    @Value("${app.config.oauth2.client}")
    private String client;

    @Value("${app.config.oauth2.secret}")
    private String secret;

    @Value("${app.config.token.expiration.access}")
    private int expirationAccessToken;

    @Value("${app.config.token.expiration.refresh}")
    private int expirationRefreshToken;

    private TokenStore tokenStore = new InMemoryTokenStore();

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(myUserDetailService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(client)
                .secret(passwordEncoder.encode(secret))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("all")
                .resourceIds(resourceId)
                .accessTokenValiditySeconds(expirationAccessToken)
                .refreshTokenValiditySeconds(expirationRefreshToken);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }
}