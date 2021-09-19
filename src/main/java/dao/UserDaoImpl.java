package dao;

import pojos.User;
import pojos.UserRole;

import static utils.HibernateUtils.getFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.*;

public class UserDaoImpl implements IUserDao {
	// NO data members , no constr , no cleanup

	@Override
	public String registerUser(User user) {
		String mesg = "User reg failed....";
		// user : state : TRANSIENT(not yet persistent)
		// get the Sesssion from SF
		Session session = getFactory().openSession();
		Session session2 = getFactory().openSession();
		System.out.println(session == session2);// false
		// begin a tx
		Transaction tx = session.beginTransaction();// db cn is pooled out --wrapped in Session obj, L1 cache created
		System.out.println("session is open " + session.isOpen() + " is connected to db cn " + session.isConnected());// t
																														// t
		try {
			// org.hibernate.Session API : public Serializable save(Object transientObjRef)
			// throws HibernateExc
			Serializable userId = session.save(user);// user ref is added in the cache : PERSISTENT
			tx.commit();// Upon commit : Hibernate performs "auto dirty checking" :by comparing state of
						// L1 cache with that of DB : DML will be fired (insert)
			mesg = "user registered successfully with ID=" + userId;
			System.out
					.println("session is open " + session.isOpen() + " is connected to db cn " + session.isConnected());// t
																														// t
			// user : PERSISTENT
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();// pooled out DB cn rets to the pool n L1 cache is destroyed
			if (session2 != null)
				session2.close();
		}
		// user : DETACHED
		System.out.println("session is open " + session.isOpen() + " is connected to db cn " + session.isConnected());// f
																														// f

		return mesg;
	} // user : DETACHED

	@Override
	public String registerUserWithGetCurrentSession(User user) {
		String mesg = "User reg failed....";
		// user : state : TRANSIENT
		// get the Sesssion from SF
		Session session = getFactory().getCurrentSession();
		// begin a tx
		Transaction tx = session.beginTransaction();// db cn is pooled out --wrapped in Session obj, L1 cache created
													// t
		try {
			// org.hibernate.Session API : public Serializable save(Object transientObjRef)
			// throws HibernateExc
			// Serializable userId = session.save(user);// user ref is added in the cache :
			// PERSISTENT
			session.persist(user);
			tx.commit();// Upon commit : Hibernate performs "auto dirty checking" :by comparing state of
						// L1 cache with that of DB : DML will be fired (insert)
			// pooled out DB cn rets to the pool n L1 cache is destroyed
			mesg = "user registered successfully with ID=" + user.getUserId();

		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();// Upon rollback : Hibernate DOES NOT performs "auto dirty checking"
// pooled out DB cn rets to the pool n L1 cache is destroyed
			throw e;
		}

		return mesg;
	}

	@Override
	public User getUserDetails(int userId) {
		User user = null;// user : does not exist
		// get Session from SF : getCurrentSession
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// L1 cache : empty
			// get
			user = session.get(User.class, userId);// int ---> Integer --> Serializable : user
			user = session.get(User.class, userId);// int ---> Integer --> Serializable
			user = session.get(User.class, userId);// int ---> Integer --> Serializable
			// select * from users_tbl where user_id=?
			// invalid user id : rets null
			// valid user id : de-serial(re -constructing user object from db data) : user :
			// PERSISTENT
			tx.commit();// auto dirty chking(since no difference between L1 cache n db : NO DML), db cn
						// rets to the pool, L1 cache is destroyed.
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

		return user;// user : DETACHED
	}

	@Override
	public List<User> getAllUserDetails() {
		List<User> users = null;
		String jpql = "select u from User u";
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// session ---> create Query --> getResultList
			users = session.createQuery(jpql, User.class).getResultList();
			// users=session.createQuery(jpql,User.class).getResultList();
			// users : list of PERSISTENT entities
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return users;// users : list of DETACHED entities
	}

	@Override
	public List<User> getSelectedUserDetails(LocalDate strt, LocalDate end, UserRole role) {
		List<User> users = null;
		String jpql = "select u from User u where u.regDate between :strtDate and :endDate and u.userRole=:rl";
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// Session --> Query object --> set 3 named IN params
			users = session.createQuery(jpql, User.class).setParameter("strtDate", strt).setParameter("endDate", end)
					.setParameter("rl", role).getResultList();// users : list of PERSISTENT entities
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return users;
	}

	@Override
	public List<String> getSelectedUserNames(LocalDate strt, LocalDate end, UserRole role) {
		List<String> names = null;
		String jpql = "select u.name from User u where u.regDate between :strtDate and :endDate and u.userRole=:rl";

		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			names = session.createQuery(jpql, String.class).setParameter("strtDate", strt).setParameter("endDate", end)
					.setParameter("rl", role).getResultList();

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return names;
	}

	@Override
	public List<User> getSelectedPartialUserDetails(LocalDate strt, LocalDate end, UserRole role) {
		List<User> users = null;
		String jpql = "select new pojos.User(email,regAmount,regDate) from User u where u.regDate between :strt and :end and userRole=:rl";
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			users = session.createQuery(jpql, User.class).setParameter("strt", strt).setParameter("end", end)
					.setParameter("rl", role).getResultList();
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return users;
	}

	@Override
	public String changePassword(int userId, String newPassword) {
		String mesg = "Password updation failed";
		User user = null;
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			// get user details by PK
			user = session.get(User.class, userId); // int --> Integer ---> Serializable
			if (user != null) {
				// user : PERSISTENT (part of L1 cache + has corresponding rec in DB)
				// valid id
				user.setPassword(newPassword);// changing the state of PERSISTENT entity
				mesg = "User's password changed!!!";
			}

			tx.commit();// Hibernate performs auto dirty checking : finds state of the persistent entity
						// changed -->DML (update) , L1 cache is destroyed , cn rets to pool
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		user.setPassword("1234567890");// user : DETACHED (NO DML will be fired!!!!!!!)
		return mesg;
	}

	@Override
	public String applyDiscount(LocalDate regDate, double discount) {
		StringBuilder mesg = new StringBuilder("Total No of users updated : ");
		String jpql = "update User u set u.regAmount=u.regAmount-:disc where u.regDate < :dt";
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			int updateCount = session.createQuery(jpql).setParameter("disc", discount).setParameter("dt", regDate)
					.executeUpdate();
			mesg.append(updateCount);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return mesg.toString();
	}

	@Override
	public String unsubscribeUser(String email) {
		String mesg = "User unsubscription failed!!!!";
		String jpql = "select u from User u where u.email=:em";
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			User user = session.createQuery(jpql, User.class).setParameter("em", email).getSingleResult();
			// => email is valid , user : PERSISTENT
			session.delete(user);// entity is simply marked for removal!
			tx.commit();// HIbernate performs auto dirty checking --> DML (delete) --> l1 cache is
						// destroyed, cn rets to the pool , user : transient
			mesg = "User un subscribed....";
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return mesg;
	}// user : doesn't exist (marked for GC)

	@Override
	public User validateUser(String email, String password) {
		User user = null;
		String jpql = "select u from User u where u.email=:em and u.password=:pass";
		// get Session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			user = session.createQuery(jpql, User.class).setParameter("em", email).setParameter("pass", password)
					.getSingleResult();
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}

		return user;
	}

	@Override
	public String deleteUserDetails(LocalDate date, double amount) {
		StringBuilder mesg = new StringBuilder("No of users deleted :");
		String jpql = "delete from User u where u.regDate > :dt and u.regAmount < :amt";
		// get session from SF
		Session session = getFactory().getCurrentSession();
		// begin tx
		Transaction tx = session.beginTransaction();
		try {
			int updateCount = session.createQuery(jpql).
					setParameter("dt", date).setParameter("amt", amount)
					.executeUpdate();
			mesg.append(updateCount);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e;
		}
		return mesg.toString();
	}

}
