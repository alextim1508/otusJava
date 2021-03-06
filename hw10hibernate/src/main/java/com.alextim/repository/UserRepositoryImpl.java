package com.alextim.repository;

import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Override
    public void insert(User user) {
        executeInTransaction(session -> {
            List<Phone> phones = user.getPhones();
            phones.forEach(phone -> phone.setUser(user));
            session.save(user);
            return user;
        });
    }

    @Override
    public long getCount() {
        return executeInTransaction(session -> {
            Query query = session.createQuery("select count(u) from User u");
            return (long)query.getSingleResult();
        });
    }

    @Override
    public List<User> getAll(int page, int amountByOnePage) {
        return executeInTransaction(session -> {
            Query<User> query = session.createQuery("select u from User u", User.class);
            query.setFirstResult(page * amountByOnePage);
            query.setMaxResults(amountByOnePage);
            return query.getResultList();
        });
    }

    @Override
    public User findById(long id) {
        return executeInTransaction(session -> session.get(User.class, id));
    }


    @Override
    public List<Phone> getPhonesByUserId(long id) {
        return executeInTransaction(session -> {
            Query query = session.createQuery("select f from Phone f left join f.user u where u.id = ?1");
            query.setParameter(1, id);
            return (List<Phone>)query.getResultList();

//            User byId = session.get(User.class, user.getId());
//            List<Phone> phones = byId.getPhones();
//            Hibernate.initialize(phones);
//            return phones;
        });
    }

    @Override
    public List<User> findByName(String name) {
        return executeInTransaction(session -> {
            Query<User> query = session.createQuery("select u from User u where u.name = :name", User.class);
            query.setParameter("name", name);
            return query.getResultList();
        });
    }

    @Override
    public void update(long id, User user) {
        executeInTransaction(session -> {
            User byId = findById(id);
            byId.setName(user.getName());
            byId.setAddress(user.getAddress());
            byId.setPhones(user.getPhones());
            session.merge(byId);
            return null;
        });
    }

    @Override
    public void delete(User user) {
        executeInTransaction(session -> {
            session.remove(user);
            return null;
        });
    }

    @Override
    public void deleteAll() {
        List<User> all = getAll(0, Integer.MAX_VALUE);
        all.forEach(this::delete);
    }

    @Override
    public User findUserByStreet(String street){
        return executeInTransaction(session -> {
            Query<Address> query = session.createQuery("select a from Address a where a.street = :street", Address.class);
            query.setParameter("street", street);
            Address address = query.uniqueResult();
            System.out.println("address = " + address);

            Address byId = session.get(Address.class, address.getId());
            return byId.getUser();
        });
    }

    @Override
    public List<User> getUsersByPhoneNumber(String number) {
        return executeInTransaction(session -> {
            Query<Phone> query = session.createQuery("select ph from Phone ph where ph.number = :number", Phone.class);
            query.setParameter("number", number);
            List<Phone> phones = query.getResultList();

            List<User> users = new ArrayList<>();
            phones.forEach(phone -> users.add(phone.getUser()));
            return users;
        });
    }

    @Override
    public void close() {
        sessionFactory.close();
    }

    private <R> R executeInTransaction(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.getTransaction().begin();
                R result = function.apply(session);
                session.getTransaction().commit();
                return result;
            }
            catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            }
        }
    }

}
