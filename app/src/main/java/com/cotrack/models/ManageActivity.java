package com.cotrack.models;

import com.cotrack.dbutils.ProviderDetails;
import com.cotrack.dbutils.ServiceDetails;
import com.cotrack.dbutils.ServiceMapping;
import com.cotrack.dbutils.UserDetails;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ManageActivity {

/* ----------------------------------- Method to CREATE User record in the database ---------------------------------- */
	
	public void addUser(String userId, String signOnId, String password, String userName, String userAdd, String city,
			String state, String zipCode, String email, String contactNo, String serviceAvailed, String serviceRequectNo) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			UserDetails user = new UserDetails();
			user.setUserId(userId);
			user.setSignOnId(signOnId);
			user.setPassword(password);
			user.setUserName(userName);
			user.setUserAdd(userAdd);
			user.setCity(city);
			user.setState(state);
			user.setZipCode(zipCode);
			user.setEmail(email);
			user.setContactNo(contactNo);
			user.setServiceAvailed(serviceAvailed);
			user.setServiceRequectNo(serviceRequectNo);
			session.save(user);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	
/* ------------------------------- Method to CREATE Provider record in the database --------------------------------- */
	
	public void addProvider(String spId, String spSignOnId, String loginPassword, String spName, String spAddress, String spCity,
			String spState, String spZipCode, String spEmail, String spContact, String serviceOffered, int noOfAssets,
			String spAssetId, String srNo) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ProviderDetails provider = new ProviderDetails();
			provider.setSpId(spId);
			provider.setSpSignOnId(spSignOnId);
			provider.setLoginPassword(loginPassword);
			provider.setSpName(spName);
			provider.setSpAddress(spAddress);
			provider.setSpCity(spCity);
			provider.setSpState(spState);
			provider.setSpZipCode(spZipCode);
			provider.setSpEmail(spEmail);
			provider.setSpContact(spContact);
			provider.setServiceOffered(serviceOffered);
			provider.setNoOfAssets(noOfAssets);
			provider.setSpAssetId(spAssetId);
			provider.setSrNo(srNo);
			session.save(provider);
			session.getTransaction().commit();
			System.out.println(tx.getStatus());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

/* --------------------------------- Method to CREATE Service record in the database --------------------------------- */
	
	public void addService(String serviceName, String serviceId, String assetId, String srNo) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ServiceDetails service = new ServiceDetails();
			service.setServiceName(serviceName);
			service.setServiceId(serviceId);
			service.setAssetId(assetId);
			service.setSrNo(srNo);
			session.save(service);
			session.getTransaction().commit();
			System.out.println(tx.getStatus());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* -------------------------------- Method to CREATE Service record in the database ----------------------------- */

	public void addServiceRequest(String srNo, String serviceName, String spId, String userId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ServiceMapping mapping = new ServiceMapping();
			mapping.setSrNo(srNo);
			mapping.setServiceName(serviceName);
			mapping.setSpId(spId);
			mapping.setUserId(userId);
			session.save(mapping);
			session.getTransaction().commit();
			System.out.println(tx.getStatus());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/* -------------------------------- Method to UPDATE Service record in the database ----------------------------- */
	
	public void updateServiceRequest(String srNo, String columnValueToBeChanged) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			ServiceMapping mapping = new ServiceMapping();
			mapping = (ServiceMapping)session.get(ServiceMapping.class, "SR001");
			mapping.setServiceName(columnValueToBeChanged);
			session.update(mapping);
			session.getTransaction().commit();
			System.out.println(tx.getStatus());
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
