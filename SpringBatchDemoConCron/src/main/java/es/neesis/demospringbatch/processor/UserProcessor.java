package es.neesis.demospringbatch.processor;

import es.neesis.demospringbatch.dto.User;
import es.neesis.demospringbatch.model.UserEntity;
import org.springframework.batch.item.ItemProcessor;

import java.util.Random;
import java.util.UUID;

public class UserProcessor implements ItemProcessor<User, UserEntity> {

    @Override
    public UserEntity process(User user) {
        Random r = new Random();
        String id  = String.valueOf(r.nextInt());

        UUID uuid = UUID.randomUUID();

        return UserEntity.builder()
                .id(uuid.toString())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .operation(user.getOperation())
                .build();
    }
}
