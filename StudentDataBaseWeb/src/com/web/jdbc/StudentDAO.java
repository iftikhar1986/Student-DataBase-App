package com.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * @author Iftikhar
 *
 */
public class StudentDAO {

	// Step 1 : SetUp Referance to the DataSource
	private DataSource dataSource;

	// Step 2 : SetUp a Constructor
	public StudentDAO(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	// Step 3 : Write a Method That List The Students
	public List<Student> getStudents() throws Exception {

		List<Student> students = new ArrayList<Student>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {

			// Getting a connection
			myConn = dataSource.getConnection();

			// Create an SQL statement
			String sql = "SELECT * FROM student ORDER BY last_name";
			myStmt = myConn.createStatement();

			// Excute Query
			myRs = myStmt.executeQuery(sql);

			// Process Result Set
			while (myRs.next()) {

				// Retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				// Create a new student object
				Student tempStudent = new Student(id, firstName, lastName,
						email);

				// Add to the list of student
				students.add(tempStudent);
			}

			return students;

		} finally {

			// Close JDBC Objects
			close(myConn, myStmt, myRs);

		}

	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		// Does Not Really Colse The connention ..Just put back and give the
		// connection pool to thos who needs it
		try {

			if (myRs != null) {
				myRs.close();
			}

			if (myStmt != null) {
				myStmt.close();
			}

			if (myConn != null) {
				myConn.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws SQLException {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// get db connection
			myConn = dataSource.getConnection();

			// Create SQL for Insert
			String sql = "INSERT INTO student "
					+ "(first_name, last_name, email)" + "values(?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// Set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

			// Excute the SQL Query
			myStmt.execute();

		} finally {
			// Clean up the JDBC Object
			close(myConn, myStmt, null);
		}

	}

	public Student getStudents(String theStudentId) {

		return null;
	}

}
