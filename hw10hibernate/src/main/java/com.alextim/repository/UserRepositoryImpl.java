package com.alextim.repository;

import com.alextim.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;


public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Override
    public void insert(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("select count(u) from User u");
            return (long)query.getSingleResult();
        }
    }

    @Override
    public List<User> getAll(int page, int amountByOnePage) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("select u from User u", User.class);
            query.setFirstResult(page * amountByOnePage);
            query.setMaxResults(amountByOnePage);
            return query.getResultList();
        }
    }

    @Override
    public User findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    public void update(long id, User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            User byId = findById(id);
            byId.setName(user.getName());
            byId.setAddress(user.getAddress());
            byId.setPhones(user.getPhones());
            session.merge(byId);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            Query query = session.createQuery("delete from User u where u.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
