package es.neesis.demospringbatch.writer;

import es.neesis.demospringbatch.model.UserEntity;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

public class UserWriterProcess implements ItemWriter<UserEntity> {

    private final DataSource dataSource;
    private ExecutionContext executionContext;

    public UserWriterProcess(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getJobExecution().getExecutionContext();
    }

    @Override
    public void write(List<? extends UserEntity> list) throws Exception {
        JdbcBatchItemWriter<UserEntity> builder = new JdbcBatchItemWriterBuilder<UserEntity>()
                .beanMapped()
                .sql("INSERT INTO users (id, username, password, email) VALUES (:id, :username, :password, :email)")
                .dataSource(dataSource)
                .build();
        builder.afterPropertiesSet();

        List<UserEntity> userEntities = list.stream().map(UserEntity.class::cast).collect(Collectors.toList());
        builder.write(userEntities);

        List<UserEntity> users = (List<UserEntity>) this.executionContext.get("users");
        if(users == null) {
            users = userEntities;
        } else {
            users.addAll(userEntities);
        }
        this.executionContext.put("users", users);
    }

    public void update(List<? extends UserEntity> list) throws Exception {
        JdbcBatchItemWriter<UserEntity> builder = new JdbcBatchItemWriterBuilder<UserEntity>()
                .beanMapped()
                .sql("update users set username:username, password:password, email:email")
                .dataSource(dataSource)
                .build();
        builder.afterPropertiesSet();

        List<UserEntity> userEntities = list.stream().map(UserEntity.class::cast).collect(Collectors.toList());
        builder.write(userEntities);

        List<UserEntity> users = (List<UserEntity>) this.executionContext.get("users");
        if(users == null) {
            users = userEntities;
        } else {
            users.addAll(userEntities);
        }
        this.executionContext.put("users", users);
    }

    public void delete(List<? extends UserEntity> list) throws Exception {
        JdbcBatchItemWriter<UserEntity> builder = new JdbcBatchItemWriterBuilder<UserEntity>()
                .beanMapped()
                .sql("delete from users where username:username")
                .dataSource(dataSource)
                .build();
        builder.afterPropertiesSet();

        List<UserEntity> userEntities = list.stream().map(UserEntity.class::cast).collect(Collectors.toList());
        builder.write(userEntities);

        List<UserEntity> users = (List<UserEntity>) this.executionContext.get("users");
        if(users == null) {
            users = userEntities;
        } else {
            users.addAll(userEntities);
        }
        this.executionContext.put("users", users);
    }
}
