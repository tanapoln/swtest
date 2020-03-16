package com.example.swtest.hibernate;

import com.example.swtest.entity.UserEntity;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class UserEntityTest extends AbstractHibernateTest {
    private Integer userId;

    @Test
    public void testInsertAutoGenId() {
        withTx(session -> {
            UserEntity user = new UserEntity();
            user.setName("Sharp");
            session.persist(user);

            Assert.assertThat(user.getId(), Matchers.notNullValue());
            userId = user.getId();
        });

        withTx(session -> {
            UserEntity user = session.get(UserEntity.class, userId);
            Assert.assertThat(user.getName(), Matchers.equalTo("Sharp"));
        });
    }
}
