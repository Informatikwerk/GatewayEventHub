package de.informatikwerk.gatewayeventhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Properties specific to Gatewayeventhub.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String jedisIp = "127.0.0.1";
    private int jedisPort = 6379;
    private int jedisMsgExpire = 30;


    public String getJedisIp() {
        return jedisIp;
    }

    public void setJedisIp(String jedisIp) {
        this.jedisIp = jedisIp;
    }

    public int getJedisPort() {
        return jedisPort;
    }

    public void setJedisPort(int jedisPort) {
        this.jedisPort = jedisPort;
    }

    public int getJedisMsgExpire() {
        return jedisMsgExpire;
    }

    public void setJedisMsgExpire(int jedisMsgExpire) {
        this.jedisMsgExpire = jedisMsgExpire;
    }

}
