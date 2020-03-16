package com.example.swtest.hibernate;

import com.example.swtest.entity.UserEntity;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

public class DeadlockTest extends AbstractHibernateTest {
    private Throwable ex;

    @Test
    public void testHelper() throws Throwable {
        Integer user1 = createUser("U1");

        withTx(session -> {
            UserEntity user = session.get(UserEntity.class, user1);
            Assert.assertThat(user.getName(), Matchers.equalTo("U1"));

            updateUserName(session, user1, "NewU1");
        });

        withTx(session -> {
            UserEntity user = session.get(UserEntity.class, user1);
            Assert.assertThat(user.getName(), Matchers.equalTo("NewU1"));
        });
    }

    @Test
    public void testDeadlock() throws Throwable {
        Integer user1 = createUser("U1");
        Integer user2 = createUser("U2");

        Thread t1 = new Thread(() -> {
            withTx(session -> {
                updateUserName(session, user1, "UserT1U1");

                withTx(session2 -> {
                    updateUserName(session2, user2, "UserT1U2");
                });
            });
        });

        Thread t2 = new Thread(() -> {
            withTx(session -> {
                updateUserName(session, user2, "UserT2U2");

                withTx(session2 -> {
                    updateUserName(session2, user1, "UserT2U1");
                });
            });
        });

        t1.setUncaughtExceptionHandler((t, e) -> {
            ex = e;
        });
        t2.setUncaughtExceptionHandler((t, e) -> {
            ex = e;
        });

        t1.start();
        sleep(500);
        t2.start();

        t1.join(3000);
        t2.join(3000);

        if (ex != null) {
            throw ex;
        }
        if (t1.isAlive() || t2.isAlive()) {
            throw new Exception("Thread is still running");
        }
    }

    private Integer createUser(String name) throws Throwable {
        Integer[] ids = new Integer[1];

        withTx(session -> {
            UserEntity user = new UserEntity();
            user.setName(name);
            session.persist(user);
            ids[0] = user.getId();
        });

        return ids[0];
    }

    private void updateUserName(Session session, Integer userId, String name) {
        UserEntity user = session.get(UserEntity.class, userId);
        user.setName(name);
        session.flush();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
