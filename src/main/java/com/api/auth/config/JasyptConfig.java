package com.api.auth.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        String encryptKey = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");

        System.out.println(encryptKey);

        if (encryptKey == null || encryptKey.isEmpty()) {
            throw new IllegalStateException("JASYPT_ENCRYPTOR_PASSWORD environment variable is not set.");
        }

        config.setPassword(encryptKey); //암호화, 복호화에 사용할 키
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); //암호화에 사용할 알고리즘
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 횟수
        config.setPoolSize("1"); //인스턴스 풀
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");

        encryptor.setConfig(config);
        return encryptor;
    }
}
