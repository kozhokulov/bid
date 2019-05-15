package com.example.bid.component;

import com.example.bid.domain.Ad;
import com.example.bid.domain.Role;
import com.example.bid.domain.User;
import com.example.bid.repository.AdRepository;
import com.example.bid.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger LOG =
            LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    public CommandLineAppStartupRunner(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            AdRepository adRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adRepository = adRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Username: admin, password: admin");

        createCustomUser(
                "admin",
                "admin",
                "admin",
                null,
                true,
                true);

        createCustomUser("lyanna", "vxcbbcfcfn", "Lyanna Mormont",
                "https://awoiaf.westeros.org/images/d/d5/Lyanna_mormont_by_vinciruz-da6z335.jpg",
                true, false);

        createCustomUser("daenerys", "vxfbvcxnc", "Daenerys Targaryen",
                "https://i.pinimg.com/originals/46/03/86/46038600348d54aa5eb35d8792fcf575.jpg",
                true, false);

        createCustomUser("tyrion", "fcnxgmnvb", "Tyrion Lannister",
                "https://i.pinimg.com/736x/11/29/8c/11298cfcec7da54d465cbc1b6165ba40.jpg",
                true, false);

        createCustomUser("sandor", "vfdxcxmm", "Sandor Clegane",
                "https://i.ytimg.com/vi/36b8zNOqb54/maxresdefault.jpg",
                true, false);

        createCustomUser("jorah", "vdfnvcddfh", "Jorah Mormont",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSo9ItFBOuXkGY55Cx-G-4K-4JAewjiQZ31ZfVp6S5wyOdJ2B2V",
                true, false);

        createCustomUser("eddard", "bvmnfdgm", "Eddard Stark",
                "https://i.pinimg.com/736x/c8/67/c0/c867c0273afc1736317a9411233645fd.jpg",
                true, false);

        createCustomUser("arya", "vfngmg", "Arya Stark",
                "https://imagesvc.timeincapp.com/v3/fan/image?url=https%3A%2F%2Fwinteriscoming.net%2Ffiles%2F2017%2F06%2Farya_480.jpg&c=sc&w=736&h=485",
                true, false);

        createCustomUser("sansa", "bfdjfwqtrti", "Sansa Stark",
                "https://hips.hearstapps.com/digitalspyuk.cdnds.net/17/22/1496398380-screen-shot-2017-06-02-at-111652.jpg?resize=480:*",
                true, false);

        createCustomUser("bekzhan", "123", "Bekzhan Satarov",
                "http://chittagongit.com/download/356018",
                true, false);

        createCustomUser("chyngyz", "123", "Chyngyz Kozhokulov",
                "https://24.kg/files/media/79/79159.jpg", true, true);

        createCustomUser("nurzatbek", "123", "Nurzatbek Dunganov",
                "http://cbtkyrgyzstan.kg/wp-content/uploads/2016/12/kr_2m.jpg",
                true, true);

    }

    private void createCustomUser(String username, String password, String fullName, String avatarUrl, boolean isActive, boolean isAdmin) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            LOG.info("User info => (username:" + username +
                    ")-(password:" + password +
                    ")-(active:" + isActive +
                    ")-(admin:" + isAdmin + ")");
        } else {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullName);
            user.setAvatar(avatarUrl);
            user.setActive(isActive);
            user.setRoles(Collections.singleton(isAdmin ? Role.ADMIN : Role.USER));
            userRepository.save(user);
            LOG.info("New user info => (username:" + username +
                    ")-(password:" + password +
                    ")-(active:" + isActive +
                    ")-(admin:" + isAdmin + ")");
        }
    }
}
