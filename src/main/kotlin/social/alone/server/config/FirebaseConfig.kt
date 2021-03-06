package social.alone.server.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

import javax.annotation.PostConstruct
import java.io.IOException
import java.io.InputStream

@Configuration
@Profile("!test")
class FirebaseConfig {

    @PostConstruct
    @Throws(IOException::class)
    fun init() {
        val path = "alone-social-club-firebase-adminsdk.json"
        val classLoader = javaClass.classLoader
        val serviceAccount = classLoader.getResourceAsStream(path)


        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount!!))
                .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }

}
