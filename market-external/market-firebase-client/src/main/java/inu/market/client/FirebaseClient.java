package inu.market.client;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class FirebaseClient {

    private static final String FIREBASE_CONFIG_PATH = "inu-market-firebase-adminsdk-yuqkp-f72006c43e.json";

    @PostConstruct
    public void init() throws IOException {
        InputStream fcmOptionsInputStream = new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fcmOptionsInputStream)).build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    public void send(String fcmToken, String title, String content) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .putData("title", title)
                .putData("content", content)
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }

}
