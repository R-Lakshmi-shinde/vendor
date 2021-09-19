package tester;

import static utils.HibernateUtils.getFactory;

import java.time.LocalDate;
import java.util.Scanner;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;

import dao.UserDaoImpl;
import pojos.User;
import pojos.UserRole;

public class ValidateUser {

	public static void main(String[] args) {
		try (SessionFactory sf = getFactory(); Scanner sc = new Scanner(System.in)) {
			System.out.println(
					"Enter Email n password for user login ");
			//create dao instance n test API
			UserDaoImpl dao=new UserDaoImpl();
			System.out.println("User Details "+dao.validateUser(sc.next(), sc.next()));
		} 
		catch (NoResultException e) {
			System.out.println(e);//just for debugging!
			System.out.println("Invalid login !!!!!!!!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
