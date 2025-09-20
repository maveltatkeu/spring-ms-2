package com.thecodealchemist.main.service;

import com.thecodealchemist.main.db.KeyStore;
import com.thecodealchemist.main.entity.ApiKey;
import com.thecodealchemist.main.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository repository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String generateNewKey(String owner, int validity) {
        String key = UUID.randomUUID().toString();

        LocalDateTime currTime = LocalDateTime.now();

        //create JPA entity
        ApiKey newKey = new ApiKey();
        newKey.setApiKey(passwordEncoder.encode(key));
        newKey.setOwner(owner);
        newKey.setActive(true);
        newKey.setCreatedAt(currTime);
        newKey.setExpiresAt(currTime.plusDays(validity));

        repository.save(newKey);

        return owner + "##" + key;
    }

    // key = owner##key
    public boolean revokeKey(String key) {
        String[] tokens = key.split("##");
        String owner = tokens[0];

        Optional<ApiKey> keyHolder = repository.findByOwnerAndIsActiveTrue(owner);

        //if key already present then deactivate
        if(keyHolder.isPresent()) {
            ApiKey apiKey = keyHolder.get();
            apiKey.setActive(false);
            repository.save(apiKey);
            return true;
        }
        return false;
    }

    public String rotateKeys(String key) {
        String[] tokens = key.split("##");
        String owner = tokens[0];

        Optional<ApiKey> keyHolder = repository.findByOwnerAndIsActiveTrue(owner);

        //if key already present then deactivate the existing key
        //and create a new one
        if (keyHolder.isPresent()) {

            //deactivating existing key
            ApiKey old = keyHolder.get();
            old.setActive(false);
            repository.save(old);

            String newKey = UUID.randomUUID().toString();
            LocalDateTime currTime = LocalDateTime.now();

            //creating a new key
            ApiKey newApiKey = new ApiKey();
            newApiKey.setActive(true);
            newApiKey.setOwner(old.getOwner());
            newApiKey.setApiKey(passwordEncoder.encode(newKey));
            newApiKey.setCreatedAt(currTime);
            newApiKey.setExpiresAt(currTime.plusDays(30));

            repository.save(newApiKey);

            return owner + "##" + newKey;
        }

        return null;
    }

    public boolean isKeyValid(String key) {
        String[] tokens = key.split("##");
        String owner = tokens[0];
        String rawKey = tokens[1];

        String hashedKey = KeyStore.getKeyByOwner(owner);

        return passwordEncoder.matches(rawKey, hashedKey);
    }
}
