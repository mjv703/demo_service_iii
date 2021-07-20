package com.medicai.pillpal.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class GoogleApiConfiguration {

    private final Logger log = LoggerFactory.getLogger(GoogleApiConfiguration.class);

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        log.debug("Google API Config");
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
            new ClassPathResource("project-mjv703-pill-pal-0001.json").getInputStream()
        );
        FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
        return FirebaseMessaging.getInstance(app);
    }
}
