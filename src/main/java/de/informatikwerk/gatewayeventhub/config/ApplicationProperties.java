package de.informatikwerk.gatewayeventhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import redis.clients.jedis.JedisPoolConfig;

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


    public JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(16);
        poolConfig.setMinIdle(8);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(15).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }
}
