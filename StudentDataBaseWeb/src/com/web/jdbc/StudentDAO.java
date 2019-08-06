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

	public Student getStudents(String theStudentId) throws Exception {

		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		int studentId;

		try {
			// Convert Student Id to int
			studentId = Integer.parseInt(theStudentId);

			// Get Connection to the DataBase
			myConn = dataSource.getConnection();

			// Create SQL to get selected student
			String sql = "SELECT * FROM student where id=?";

			// Create Prepared statement
			myStmt = myConn.prepareStatement(sql);

			// Set Parameters
			myStmt.setInt(1, studentId);

			// Excute Statement
			myRs = myStmt.executeQuery();

			// Retrive Data from result set Row
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				// Using the StudentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			} else {
				throw new Exception("Could Not find the student id: "
						+ studentId);
			}
			return theStudent;

		} finally {
			close(myConn, myStmt, myRs);
		}

	}

	public void updateStudent(Student theStudent) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// Get DB Connection
			myConn = dataSource.getConnection();

			// Creat SQL Statement
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? " + "where id=?";

			// Prepare Statement
			myStmt = myConn.prepareStatement(sql);

			// Set Statement
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());

			// Excute SQL Statement
			myStmt.execute();

		} finally {
			close(myConn, myStmt, null);
		}

	}

	public void deleteStudent(String theStudentId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// Convert student id to int
			int studentId = Integer.parseInt(theStudentId);

			// Get Connection to dataBase
			myConn = dataSource.getConnection();

			// Create sql to Delete student
			String sql = "DELETE FROM student where id=?";

			// Prepare Statement
			myStmt = myConn.prepareStatement(sql);

			// Set Param

			myStmt.setInt(1, studentId);

			// Excute SQL Statement
			myStmt.execute();

		} finally {
			// Clean Up JDBC Code
			close(myConn, myStmt, null);
		}

	}

}
