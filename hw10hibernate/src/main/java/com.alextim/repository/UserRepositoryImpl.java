package com.alextim.repository;

import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
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
            try {
                session.getTransaction().begin();
                session.save(user);
                session.getTransaction().commit();
            }
            catch (Exception e) {
                System.out.println("Exception:" + e.getMessage());
                session.getTransaction().rollback();
            }
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
    public List<User> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("select u from User u where u.name = :name", User.class);
            query.setParameter("name", name);
            return query.getResultList();
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
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.remove(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            List<User> all = getAll(0, Integer.MAX_VALUE);
            all.forEach(this::delete);
            session.getTransaction().commit();
        }
    }

    @Override
    public User findUserByStreet(String street){
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery("select a from Address a where a.street = :street", Address.class);
            query.setParameter("street", street);
            Address address = query.uniqueResult();
            System.out.println("address = " + address);

            Address byId = session.get(Address.class, address.getId());
            User user = byId.getUser();
            return user;
        }
    }

    @Override
    public List<User> getUsersByPhoneNumber(String number) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            Query<Phone> query = session.createQuery("select ph from Phone ph where ph.number = :number", Phone.class);
            query.setParameter("number", number);
            List<Phone> phones = query.getResultList();

            List<User> users = new ArrayList<>();
            phones.forEach(phone -> users.addAll(phone.getUser()));
            return users;
        }
    }

}
