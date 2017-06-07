package cdccm.utilities;


import org.joda.time.LocalDate;
import org.joda.time.Years;

public class CdccmUtilities {
	
	public static int getAge(String dateOfBirth) {
		//Returns the age by providing DOB.
		String[] dayMonthYear = dateOfBirth.split("/");
		
		LocalDate birthdate = new LocalDate(Integer.parseInt(dayMonthYear[2]), Integer.parseInt(dayMonthYear[1]),
				Integer.parseInt(dayMonthYear[0])); // Birth year,month,date
		LocalDate now = new LocalDate(); // Today's date
		Years age = Years.yearsBetween(birthdate, now);

		return age.getYears();
	}
}
