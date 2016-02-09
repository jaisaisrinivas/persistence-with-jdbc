package com.sap.cloud.sample.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.core.server.csi.IXSSEncoder;
import com.sap.security.core.server.csi.XSSEncoder;

/**
 * Servlet implementing a simple JDBC based persistence sample application for
 * SAP HANA Cloud Platform.
 */
public class PersistenceWithJDBCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PersistenceWithJDBCServlet.class);

	private PersonDAO personDAO;

	/** {@inheritDoc} */
	@Override
	public void init() throws ServletException {
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup("java:comp/env/jdbc/DefaultDB");
			personDAO = new PersonDAO(ds);
		} catch (SQLException e) {
			throw new ServletException(e);
		} catch (NamingException e) {
			throw new ServletException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String iden= request.getParameter("iden");
		
		
		
		response.getWriter().println("<p>Persistence with JDBC!</p>");
		try {
			appendPersonTable(response);
			appendAddForm(response);
		} catch (Exception e) {
			response.getWriter().println(
					"Persistence operation failed with reason: "
							+ e.getMessage());
			LOGGER.error("Persistence operation failed", e);
		}
		
	}

	/** {@inheritDoc} */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String iden= request.getParameter("iden");
			if(iden.equals("login"))
			{
				String role=doCheck(request);
				if(role!=null )
				{
					System.out.println(role);
					if(role.contains("manager"))
					{
					HttpSession sess= request.getSession();
					String [] spl= role.split(",");
					System.out.println(spl[1]+"in servlert");
					sess.setAttribute("uname", spl[1]);	
					request.getRequestDispatcher("manager.jsp").forward(request, response);
					}
					else 
					{
						request.getRequestDispatcher("user.jsp").forward(request, response);
					}
					
				}
				else
				{
					response.getWriter().println(
							"<p> entry not found yahoooooooooooooooooo"
							+ "please check your user name and password "
							+ "</p>");
					
				}
				
			}
			else if(iden.equals("addusers"))
			{
				doAdd(request);
				
			}
			else
			{
				doAdd(request);
				doGet(request, response);
			}
			
		} catch (Exception e) {
			response.getWriter().println(
					"Persistence operation failed with reason: "
							+ e.getMessage());
			LOGGER.error("Persistence operation failed", e);
		}
	}

	private String doCheck(HttpServletRequest request) throws SQLException {
		

		// Extract name of person to be added from request
		String uname = request.getParameter("uname");
		String pwd = request.getParameter("pwd");

		// Add person if name is not null/empty
		if (uname != null && pwd != null
				&& !uname.trim().isEmpty() && !pwd.trim().isEmpty()) {
			Person person = new Person();
			person.setLastName(uname.trim());
			person.setPassword(pwd.trim());
			return personDAO.checkPerson(person);
			
		}
		return null;
	
	}

	private void appendPersonTable(HttpServletResponse response)
			throws SQLException, IOException {
		// Append table that lists all persons
		List<Person> resultList = personDAO.selectAllPersons();
		response.getWriter().println(
				"<p><table border=\"1\"><tr><th colspan=\"3\">"
						+ (resultList.isEmpty() ? "" : resultList.size() + " ")
						+ "Entries in the Database</th></tr>");
		if (resultList.isEmpty()) {
			response.getWriter().println(
					"<tr><td colspan=\"3\">Database is empty</td></tr>");
		} else {
			response.getWriter()
					.println(
							"<tr><th>First name</th><th>Last name</th><th>Id</th></tr>");
		}
		IXSSEncoder xssEncoder = XSSEncoder.getInstance();
		for (Person p : resultList) {
			response.getWriter().println(
					"<tr><td>" + xssEncoder.encodeHTML(p.getFirstName())
							+ "</td><td>"
							+ xssEncoder.encodeHTML(p.getLastName())
							+ "</td><td>"
							+ xssEncoder.encodeHTML(p.getRole())
							
							+ "</td><td>"
							+ xssEncoder.encodeHTML(p.getPassword())
							
							+ "</td><td>" + p.getId() + "</td></tr>");
		}
		response.getWriter().println("</table></p>");
	}

	private void appendAddForm(HttpServletResponse response) throws IOException {
		// Append form through which new persons can be added
		response.getWriter()
				.println(
						"<p><form action=\"\" method=\"post\">"
								+ "First name:<input type=\"text\" name=\"FirstName\">"
								+ "&nbsp;Last name:<input type=\"text\" name=\"LastName\">"
								+ "&nbsp;<input type=\"submit\" value=\"Add Person\">"
								+ "</form></p>");
	}

	private void doAdd(HttpServletRequest request) throws ServletException,
			IOException, SQLException {
		// Extract name of person to be added from request
		String firstName = request.getParameter("fname");
		String lastName = request.getParameter("lname");
		String role=request.getParameter("role");
		String pwd=request.getParameter("pwd");

		// Add person if name is not null/empty
		if (firstName != null && lastName != null && pwd!=null
				&& !firstName.trim().isEmpty() &&  !lastName.trim().isEmpty() && !pwd.trim().isEmpty()) {
			Person person = new Person();
			person.setFirstName(firstName.trim());
			person.setLastName(lastName.trim());
			person.setRole(role);
			person.setPassword(pwd);
			personDAO.addPerson(person);
		}
	}
}
