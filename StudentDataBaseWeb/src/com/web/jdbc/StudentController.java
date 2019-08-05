package com.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/StudentController")
public class StudentController extends HttpServlet {

	// Set a refernace to a DAO
	private StudentDAO studentDAO;

	// JavaEE Source Injection and Define datasource/connection pool for
	// resourse Injection
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// Create our Student db Util and Pass in the conn pool / dataSource
		try {
			studentDAO = new StudentDAO(dataSource);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			// Read the Command Paramter from add-student-form.jsp
			String theCommand = request.getParameter("command");

			// If the Command is missing , than default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}

			// Route to the appropriate method
			switch (theCommand) {

			case "LIST":
				listStudents(request, response);
				break;

			case "ADD":
				addStudent(request, response);
				break;

			case "LOAD":
				loadStudent(request, response);
				break;

			default:
				listStudents(request, response);

			}
			// List the Student in the MVC...Fashion
			listStudents(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	private void loadStudent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Read Student id From form data
		String theStudentId = request.getParameter("studentId");

		// Get student from database (DAO)
		Student theStudent = studentDAO.getStudents(theStudentId);

		// Place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);

		// Send to JSP Page : update-student-form.jsp
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);

	}

	private void addStudent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Read Student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);

		// add the student to the database
		studentDAO.addStudent(theStudent);

		// send back to main page (the student list)
		listStudents(request, response);

	}

	private void listStudents(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Get Student From DB DAO
		List<Student> students = studentDAO.getStudents();

		// Add Students to the request
		request.setAttribute("STUDENT_LIST", students);

		// Send to JSP Pages(View)
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);

	}

}
