package project.AirlineSystem.Employee;

import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.jws.WebService;

import beanFiles.PersonBean;

@WebService
public class EmployeeHelper {

	//A private method to establish connection with the DB
	private Connection ConnectionMethod(){
		Connection con = null;

		try{
			System.out.println("Inside Connection method");

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = ConnectionPool.getConnection("jdbc:mysql://localhost:3306/JDBC_LEARN","root","root");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/JDBC_LEARN","root","root");
			System.out.println("Inside Connection method : After init con");
			if(!con.isClosed()){
				System.out.println("Inside Connection method : returning valid con");
				return con;
			}
			System.out.println("Inside Connection method : its closed");
			return con;
		}
		catch(Exception e){
			System.out.println("Connection to DB could not be established");
			return con;
		}

	}

	//A private method to calculate the count of records in any table
	private int getRowCount(ResultSet resultSet) {
		if (resultSet == null)
		{
			return 0;
		}
		try
		{
			resultSet.last();
			return resultSet.getRow();
		}
		catch (SQLException exp) 
		{
			exp.printStackTrace();
		} 
		finally
		{
			try 
			{
				resultSet.beforeFirst();
			}
			catch (SQLException exp)
			{
				exp.printStackTrace();
			}
		}
		return 0;
	}

	//This method inserts a new entry into the Employee table - a new employee
	public boolean createNewEmployee(PersonBean empObj){

		try{

			Date utilDate = new Date();
			java.sql.Date hireDate = new java.sql.Date(utilDate.getTime());

			//int person_id;
			String first_name = empObj.getFirst_name(); 
			String last_name = empObj.getLast_name(); 
			String middle_initial = empObj.getMiddle_initial();
			String gender = empObj.getGender();
			Date date_of_birth = empObj.getDate_of_birth();
			System.out.println(date_of_birth);
			String contact_no = empObj.getContact_no(); 
			String address = empObj.getAddress(); 
			String city = empObj.getCity(); 
			String state = empObj.getState();
			String zipcode = empObj.getZipcode();
			String emailID = empObj.getEmail();
			String userName = empObj.getUsername();
			String password = empObj.getPassWrd();

			String Employee_id = empObj.getEmployee_id(); 
			String id_no = empObj.getId_no(); 
			String id_type = empObj.getId_type(); 
			String work_description = empObj.getWork_description();
			String position = empObj.getPosition();
			Date creation_date = new Date(hireDate.getTime());
			String created_by = "Roopa"; 
			Date last_updated = empObj.getLast_updated();
			String last_updated_by = empObj.getLast_updated_by();
			Date endDate = null;

			Connection con = ConnectionMethod();

			PreparedStatement statement = con.prepareStatement("select * from Employee where id_no = ? and id_type = ?");
			statement.setString(1, id_no);
			statement.setString(2, id_type);
			ResultSet rs = statement.executeQuery();
			if(getRowCount(rs) > 0){
				return false; //id type and id num already exists in Empl table. So, don't insert
			}

			statement = con.prepareStatement("insert into Person values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			statement.setInt(1, 0); //Auto increment column
			statement.setString(2, id_no);
			statement.setString(3, id_type);
			statement.setString(4, first_name);
			statement.setString(5, last_name);
			statement.setString(6, middle_initial);
			statement.setString(7, gender);
			statement.setDate(8, new java.sql.Date(date_of_birth.getTime()));
			statement.setString(9, contact_no);
			statement.setString(10, address);
			statement.setString(11, city);
			statement.setString(12, state);
			statement.setString(13, zipcode);
			statement.setDate(14, hireDate);
			statement.setString(15, created_by);
			statement.setDate(16, hireDate);
			statement.setString(17, "Roopa");
			statement.setString(18, userName);
			statement.setString(19, password);
			statement.setString(20, emailID);

			boolean status = statement.execute();

			if(status == false){
				statement = con.prepareStatement("select Person_ID from Person where id_no = ? and id_type = ?");
				statement.setString(1, id_no);
				statement.setString(2, id_type);
				ResultSet personID = statement.executeQuery();
				personID.next();
				int perID = personID.getInt("Person_ID");

				statement = con.prepareStatement("insert into Employee values(?,?,?,?,?,?,?,?,?,?,?,?)");

				statement.setString(1, Employee_id);
				statement.setString(2, id_no);
				statement.setString(3, id_type);
				statement.setInt(4, perID);
				statement.setDate(5, hireDate);
				statement.setString(6, created_by);
				statement.setDate(7, hireDate);
				statement.setString(8, "Roopa");
				statement.setString(9, work_description);
				statement.setString(10, position);
				statement.setDate(11, hireDate);
				statement.setDate(12, null);

				status = statement.execute();
			}

			return !status;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}

	}

	//This method updates the end date of an employee to current date considering the Employee Id
	//This means the employee is deleted
	public boolean updDeleteEmployee(PersonBean empObj, String emp, boolean isUpdate){
		if(isUpdate)
		{

			try{
				Connection con = ConnectionMethod();

				String first_name = empObj.getFirst_name(); 
				String last_name = empObj.getLast_name(); 
				String middle_initial = empObj.getMiddle_initial();
				String gender = empObj.getGender();
				Date date_of_birth = empObj.getDate_of_birth();
				String contact_no = empObj.getContact_no(); 
				String address = empObj.getAddress(); 
				String city = empObj.getCity(); 
				String state = empObj.getState();
				String zipcode = empObj.getZipcode();

				String Employee_id = empObj.getEmployee_id(); 
				String id_no = empObj.getId_no(); 
				String id_type = empObj.getId_type(); 
				String work_description = empObj.getWork_description();
				String position = empObj.getPosition();
				Date hireDate = empObj.getHire_date();

				String ar[] = new String[30];
				java.sql.Date[] dateArr = new java.sql.Date[2];

				/*The line below is for basic search. Based on the search criteria, each 
					condition is appended to this string
				 */
				String main = "update Employee e, Person p set ";

				//count keeps track of the number of user entered search conditions 
				int count = 0;
				if(!Employee_id.isEmpty()){
					main+="Employee_Id = ?,";
					ar[count] = Employee_id;
					count++;

				}
				
				if((id_no != null) && !id_no.isEmpty()){
					main+="e.id_no = ?, p.id_no = ?,";
					ar[count++] = id_no;
					ar[count] = id_no;
					count++;
				}
				
				if((id_type != null) && (!id_type.isEmpty()) && (!id_type.equals("All"))){
					main+="e.id_type= ?, p.id_type= ?";
					ar[count++] = id_type;
					ar[count] = id_type;
					count++;
				}

				if((work_description != null) && !work_description.isEmpty()){
					main+="Work_Description = ?,";
					ar[count] = work_description;
					count++;

				}

				if((position != null) && !position.isEmpty()){
					main+="Position = ?,";
					ar[count] = position;
					count++;

				}

				if((first_name != null) && !first_name.isEmpty()){
					main += "first_name = ?,";
					ar[count] = first_name;
					count++;

				}

				if((middle_initial != null) && !middle_initial.isEmpty()){
					main += "middle_initial = ?,";
					ar[count] = middle_initial;
					count++;
				}

				if((last_name != null) && !last_name.isEmpty()){
					main += "last_name = ?,";
					ar[count] = last_name;
					count++;
				}
				
				if((gender != null) && (!gender.isEmpty()) && (!gender.equals("All"))){
					main += "gender = ?,";
					ar[count] = gender;
					count++;
				}
				
				if((address != null) && !address.isEmpty()){
					main +=  "address = ?,";
					ar[count] = address;
					count++;
				}
				
				if((city != null) && !city.isEmpty()){
					main += "city = ?,";
					ar[count] = city;
					count++;
				}
				
				if((state != null)&& !state.isEmpty()){
					main = main + "state = ?,";
					ar[count] = state;
					count++;
				}
				
				if((zipcode != null) && !zipcode.isEmpty()){
					main = main + "zipcode = ?,";
					ar[count] = zipcode;
					count++;
				}
				
				if((contact_no != null) && !contact_no.isEmpty()){
					main = main + "contact_no = ?,";
					ar[count] = contact_no;
					count++;
				}
				
				int dateCnt = 0;
				if(date_of_birth != null){
					java.sql.Date dob = new java.sql.Date(date_of_birth.getTime());
					main = main + "date_of_birth = ?,";
					dateArr[dateCnt] = dob;
					dateCnt++;
				}
				
				if(hireDate != null){
					java.sql.Date date = new java.sql.Date(hireDate.getTime());
					main = main + "hire_date = ?";
					dateArr[dateCnt] = date;
					dateCnt++;

				}
				
				main += " where e.person_id = p.person_id and e.Employee_Id = '" + emp + "'";

				System.out.println(main);

				PreparedStatement selStatement = con.prepareStatement(main);
				//replace the question mark as part of conditions - inside setString
				int i =0;
				for(i=0;i<count;i++){
					selStatement.setString(i+1, ar[i]);
				}
				
				//For date parameters
				for(int j =0;j<dateCnt;j++){
					selStatement.setDate(i+1, dateArr[j]);
					System.out.println("DOB " +dateArr[j].toString());
					i++;
				}

				System.out.println("Main query " + main);
				boolean status = selStatement.execute();

				return !status;

			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}

		
		}
		else
		{
		try{

			Date endDate = new Date();
			String employeeId = empObj.getEmployee_id();

			Connection con = ConnectionMethod();

			String temp = "update Employee set End_Date = ? where Employee_Id = ?";
			PreparedStatement delStatement = con.prepareStatement(temp);
			delStatement.setDate(1, new java.sql.Date(endDate.getTime()));
			delStatement.setString(2, employeeId);

			delStatement.execute();

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		}

	}

	//This method lists all employees who are currently employed by the airline system
	public PersonBean[] listEmployee(){
		try{
			Connection con = ConnectionMethod();	

			PreparedStatement selStatement = con.prepareStatement("select * from Employee e, Person p where End_Date is null and e.person_id = p.person_id"); 
			ResultSet rs = selStatement.executeQuery();
			return ConstructResult(rs);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}

	//This method searches specific employees 
	public PersonBean[] searchEmployee(PersonBean empObj){
		try{
			Connection con = ConnectionMethod();

			String first_name = empObj.getFirst_name(); 
			String last_name = empObj.getLast_name(); 
			String middle_initial = empObj.getMiddle_initial();
			String gender = empObj.getGender();
			Date date_of_birth = empObj.getDate_of_birth();
			String contact_no = empObj.getContact_no(); 
			String address = empObj.getAddress(); 
			String city = empObj.getCity(); 
			String state = empObj.getState();
			String zipcode = empObj.getZipcode();
			String emailID = empObj.getEmail();

			String Employee_id = empObj.getEmployee_id(); 
			String id_no = empObj.getId_no(); 
			String id_type = empObj.getId_type(); 
			String work_description = empObj.getWork_description();
			String position = empObj.getPosition();
			Date hireDate = empObj.getHire_date();

			String ar[] = new String[20];
			java.sql.Date[] dateArr = new java.sql.Date[2];

			/*The line below is for basic search. Based on the search criteria, each 
				condition is appended to this string
			 */
			String main = "select * from Employee e, Person p where e.person_ID = p.person_ID and e.end_date is null";

			//count keeps track of the number of user entered search conditions 
			int count = 0;
			if(!Employee_id.isEmpty()){
				main+=" and Employee_Id = ?";
				ar[count] = Employee_id;
				count++;

			}
			
			if((emailID != null) && !emailID.isEmpty()){
				main+=" and email = ?";
				ar[count] = emailID;
				count++;

			}
			
			if((id_no != null) && !id_no.isEmpty()){
				main+=" and e.id_no = ?";
				ar[count] = id_no;
				count++;
			}
			
			if((id_type != null) && (!id_type.isEmpty()) && (!id_type.equals("All"))){
				main+=" and e.id_type= ?";
				ar[count] = id_type;
				count++;
			}

			if((work_description != null) && !work_description.isEmpty()){
				main+=" and Work_Description = ?";
				ar[count] = work_description;
				count++;

			}

			if((position != null) && !position.isEmpty()){
				main+=" and Position = ?";
				ar[count] = position;
				count++;

			}

			if((first_name != null) && !first_name.isEmpty()){
				main = main + " and first_name = ?";
				ar[count] = first_name;
				count++;

			}

			if((middle_initial != null) &&!middle_initial.isEmpty()){
				main = main + " and middle_initial = ?";
				ar[count] = middle_initial;
				count++;
			}

			if((last_name != null) && !last_name.isEmpty()){
				main = main + " and last_name = ?";
				ar[count] = last_name;
				count++;
			}
			
			if((gender != null) && (!gender.isEmpty()) && (!gender.equals("All"))){
				main = main + " and gender = ?";
				ar[count] = gender;
				count++;
			}
			
			if((address != null) && !address.isEmpty()){
				main = main + " and address = ?";
				ar[count] = address;
				count++;
			}
			
			if((city != null) && !city.isEmpty()){
				main = main + " and city = ?";
				ar[count] = city;
				count++;
			}
			
			if((state != null)&& !state.isEmpty()){
				main = main + " and state = ?";
				ar[count] = state;
				count++;
			}
			
			if((zipcode != null) && !zipcode.isEmpty()){
				main = main + " and zipcode = ?";
				ar[count] = zipcode;
				count++;
			}
			
			if((contact_no != null) && !contact_no.isEmpty()){
				main = main + " and contact_no = ?";
				ar[count] = contact_no;
				count++;
			}
			
			int dateCnt = 0;
			if(date_of_birth != null){
				java.sql.Date dob = new java.sql.Date(date_of_birth.getTime());
				main = main + " and date_of_birth = ?";
				dateArr[dateCnt] = dob;
				dateCnt++;
			}
			
			if(hireDate != null){
				java.sql.Date date = new java.sql.Date(hireDate.getTime());
				main = main + " and hire_date = ?";
				dateArr[dateCnt] = date;
				dateCnt++;

			}

			System.out.println(main);

			PreparedStatement selStatement = con.prepareStatement(main);
			//replace the question mark as part of conditions - inside setString
			int i = 0;
			for(i=0;i<count;i++){
				selStatement.setString(i+1, ar[i]);
			}
			
			//For date parameters
			for(int j=0;j<dateCnt;j++){
				selStatement.setDate(i+1, dateArr[j]);
				i++;
			}

			ResultSet rs = selStatement.executeQuery();

			return ConstructResult(rs);

		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}

	private PersonBean[] ConstructResult(ResultSet rs){
		try{
			int count = getRowCount(rs);

			if(count == 0){
				System.out.println("There are no records with end date null");
				return null;
			}
			else
			{
				System.out.println("There are records with end date null");
				PersonBean[] a = new PersonBean[count];
				int i = 0;
				for(i = 0; i< count && rs.next(); i++)
				{
					PersonBean cur = new PersonBean();
					cur.setEmployee_id(rs.getString("Employee_Id"));
					cur.setFirst_name(rs.getString("first_name"));
					cur.setLast_name(rs.getString("last_name"));
					cur.setMiddle_initial(rs.getString("middle_initial"));
					cur.setEmail(rs.getString("email"));
					System.out.println(rs.getString("email"));

					//Date conversion hack
					//'y' for year
					//'M' for month
					//'d' for date
					//Refer for entire SimpleDateFormat list -
					//http://stackoverflow.com/questions/4216745/java-string-to-date-conversion
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(rs.getString("date_of_birth"));
					cur.setDate_of_birth(date);

					cur.setAddress(rs.getString("address"));
					cur.setCity(rs.getString("city"));
					cur.setState(rs.getString("state"));
					cur.setZipcode(rs.getString("zipcode"));

					cur.setContact_no(rs.getString("contact_no"));

					cur.setWork_description(rs.getString("work_description"));
					cur.setPosition(rs.getString("position"));
					cur.setHire_date(rs.getDate("hire_date"));
					cur.setId_type(rs.getString("id_type"));
					cur.setId_no(rs.getString("id_no"));

					a[i] = cur;
				}
				return a;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}
}
