package annotationtest;

/*
	This is a class extracted from the Hibernate Project.
	It is being used as a test case for Annotation Metrics.
	Regarding Annotation Metrics, check:
	https://www.sciencedirect.com/science/article/pii/S016412121730273X
*/

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.AssociationOverrides;
import javax.persistence.AssociationOverride;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;

import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.security.access.annotation.Secured;

import static org.hibernate.testing.transaction.TransactionUtil.doInHibernate;
import static org.junit.Assert.assertEquals;

/**
 * @author Vlad Mihalcea
 */
public abstract class AnnotationTest {

	@GET
	@Path("/search/{nameLike}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Secured({ "ROLE_ADMIN", "ROLE_USER", "ROLE_ANONYMOUS" })
	protected abstract String getAllResources(@Context SecurityContext sc,
											  @PathParam("nameLike") String nameLike,
											  @QueryParam("start") Integer start,
											  @QueryParam("limit") Integer limit)
			throws BadRequestWebEx;

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Person.class,
				Doctor.class,
				Engineer.class
		};
	}

	@Override
	protected Class<?>[] getAnnotatedClasses(int x) {
		return new Class<?>[] {
				Person.class,
				Doctor.class,
				Engineer.class
		};
	}

	@Override
	protected Configuration constructConfiguration() {
		Configuration configuration = super.constructConfiguration();
		configuration.setProperty( AvailableSettings.HQL_BULK_ID_STRATEGY, getMultiTableBulkIdStrategyClass().getName() );
		return configuration;
	}

	protected abstract Class<? extends MultiTableBulkIdStrategy> getMultiTableBulkIdStrategyClass();

	@Override
	protected boolean isCleanupTestDataRequired() {
		return true;
	}

	@Override
	protected boolean isCleanupTestDataUsingBulkDelete() {
		return true;
	}

	@Before
	public void setUp() {
		doInHibernate( this::sessionFactory, session -> {
			for ( int i = 0; i < entityCount(); i++ ) {
				Doctor doctor = new Doctor();
				doctor.setId( i );
				doctor.setCompanyName( "Red Hat USA" );
				doctor.setEmployed( ( i % 2 ) == 0 );
				session.persist( doctor );
			}

			for ( int i = 0; i < entityCount(); i++ ) {
				Engineer engineer = new Engineer();
				engineer.setId( i );
				engineer.setCompanyName( "Red Hat Europe" );
				engineer.setEmployed( ( i % 2 ) == 0 );
				engineer.setFellow( ( i % 2 ) == 1 );
				session.persist( engineer );
			}
		});
	}

	protected int entityCount() {
		return 100;
	}

	@Test
	public void testUpdate() {
		doInHibernate( this::sessionFactory, session -> {
			int updateCount = session.createQuery( "update Person set name = :name where employed = :employed" )
					.setParameter( "name", "John Doe" )
					.setParameter( "employed", true )
					.executeUpdate();

			assertEquals(entityCount(), updateCount);
		});
	}

	@Test
	public void testDeleteFromPerson() {
		doInHibernate( this::sessionFactory, session -> {
			int updateCount = session.createQuery( "delete from Person where employed = :employed" )
					.setParameter( "employed", false )
					.executeUpdate();
			assertEquals( entityCount(), updateCount );
		});
	}

	@Test
	public void testDeleteFromEngineer() {
		doInHibernate( this::sessionFactory, session -> {
			int updateCount = session.createQuery( "delete from Engineer where fellow = :fellow" )
					.setParameter( "fellow", true )
					.executeUpdate();
			assertEquals( entityCount() / 2, updateCount );
		});
	}

	@AssociationOverrides(value = {
    	@AssociationOverride(name="ex",
 				joinColumns = @JoinColumn(name="EX_ID")),
 		@AssociationOverride(name="other",
 				joinColumns = @JoinColumn(name="O_ID"))})
 	@NamedQuery(name="findByName",
 					query="SELECT c " +
 					"FROM Country c " +
 					"WHERE c.name = :name")
	@Test(bar = @Annotation0, bar2 = @Annotation2, bar3 = 1)
	@Entity(name = "Person", name1 = "Teste")
	@Inheritance(strategy = InheritanceType.JOINED)
	public static class Person implements Serializable {

		@Annotation0
		@Id
		private Integer id;

		@Id
		private String companyName;

		private String name;

		private boolean employed;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isEmployed() {
			return employed;
		}

		public void setEmployed(boolean employed) {
			this.employed = employed;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( !( o instanceof Person ) ) {
				return false;
			}
			Person person = (Person) o;
			return Objects.equals( getId(), person.getId() ) &&
					Objects.equals( getCompanyName(), person.getCompanyName() );
		}

		@Override
		public int hashCode() {
			return Objects.hash( getId(), getCompanyName() );
		}
	}

	@Entity(name = "Doctor")
	public static class Doctor extends Person {
	}

	@Entity(name = "Engineer")
	public static class Engineer extends Person {

		private boolean fellow;

		public boolean isFellow() {
			return fellow;
		}

		public void setFellow(boolean fellow) {
			this.fellow = fellow;
		}
	}

	interface HelloWorld {
		public void greet();
		public void greetSomeone(String someone);
	}

	public void sayHello() {

		class EnglishGreeting implements HelloWorld {
			String name = "world";
			@Override
			public void greet() {
				greetSomeone("world");
			}
			@Override
			public void greetSomeone(String someone) {
				name = someone;
				System.out.println("Hello " + name);
			}
		}

		HelloWorld englishGreeting = new EnglishGreeting();

		HelloWorld frenchGreeting = new HelloWorld() {
			String name = "tout le monde";
			@Override
			public void greet() {
				greetSomeone("tout le monde");
			}
			@Override
			public void greetSomeone(String someone) {
				name = someone;
				System.out.println("Salut " + name);
			}
		};
	}
}