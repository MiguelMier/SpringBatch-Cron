package es.neesis.demospringbatch.writer;

import es.neesis.demospringbatch.model.UserEntity;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

public class UserWriter implements ItemWriter<UserEntity> {

    private final DataSource dataSource;
    private ExecutionContext executionContext;

    public UserWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getJobExecution().getExecutionContext();
    }

    @Override
    public void write(List<? extends UserEntity> list) throws Exception {
        String query = "";

        for(UserEntity user: list){
            if(user.getOperation().equalsIgnoreCase("INSERT")){
                query = "INSERT INTO users (id, username, password, email) VALUES (:id, :username, :password, :email)";
            } else if (user.getOperation().equalsIgnoreCase("UPDATE")) {
                query = "update users set username=:username, password=:password, email=:email";
            } else if (user.getOperation().equalsIgnoreCase("DELETE")) {
                query = "delete from users where username=:username";
            }
            else{
                throw new InvalidParameterException();
            }
        }
        realizaQuery(list, query);

    }

    private void realizaQuery(List<? extends UserEntity> list, String query) throws Exception {
        JdbcBatchItemWriter<UserEntity> builder = new JdbcBatchItemWriterBuilder<UserEntity>()
                .beanMapped()
                .sql(query)
                .dataSource(dataSource)
                .build();
        builder.afterPropertiesSet();

        List<UserEntity> userEntities = list.stream().map(UserEntity.class::cast).collect(Collectors.toList());
        builder.write(userEntities);
        actualizarContext(userEntities);
    }

    private void actualizarContext(List<UserEntity> userEntities) {
        List<UserEntity> users = (List<UserEntity>) this.executionContext.get("users");
        if(users == null) {
            users = userEntities;
        } else {
            users.addAll(userEntities);
        }
        this.executionContext.put("users", users);
    }




}
