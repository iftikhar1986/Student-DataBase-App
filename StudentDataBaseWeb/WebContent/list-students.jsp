<%-- Using Scriptlet <%@ page import="java.util.*,com.web.jdbc.*"%> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE>
<html>
<head>
<title>Student Tracker App</title>
<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<%-- <%//Get the Students from  the request object(Sent by the Servlet)
List<Student> theStudents = (List<Student>) request.getAttribute("STUDENT_LIST");%> --%>

<body>

	<div id="wrapper">
		<div id="header">
			<h2 align="center">Virtual University</h2>
		</div>

	</div>

	<div id="containar">
		<div id="content">

			<!-- Put new button : Add Student -->

			<input type="button" value="Add Student"
				onclick="window.location.href='add-student-form.jsp'; return false"
				class="add-student-button" />
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>

				</tr>

				<%-- <% for(Student tempStudent : theStudents){ %>

				<tr>
					<td><%= tempStudent.getFirstName() %></td>
					<td><%= tempStudent.getLastName() %></td>
					<td><%= tempStudent.getEmail() %></td>


				</tr>


				<%} %> --%>


				<c:forEach var="tempStudent" items="${STUDENT_LIST}">

					<!-- SetUp a link for each student -->
					<c:url var="templink" value="StudentController">

						<c:param name="command" value="LOAD" />
						<c:param name="studentId" value="${tempStudent.id }" />

					</c:url>
					
					<!-- SetUp a link to Delete a student -->
					<c:url var="deletelink" value="StudentController">

						<c:param name="command" value="DELETE" />
						<c:param name="studentId" value="${tempStudent.id }" />

					</c:url>

					<tr>
						<td>${tempStudent.firstName }</td>
						<td>${tempStudent.lastName }</td>
						<td>${tempStudent.email }</td>
						<td>
						
						<a href="${templink}">Update</a>
						|
						<a href="${deletelink}"
						   onclick="if(!(confirm('Are You Sure TO Delete this Student?')))return false">Delete</a>
						
						
						</td>
					</tr>



				</c:forEach>

			</table>
		</div>
	</div>


</body>


</html>