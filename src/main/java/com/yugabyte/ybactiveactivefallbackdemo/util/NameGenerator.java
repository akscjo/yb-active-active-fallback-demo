package com.yugabyte.ybactiveactivefallbackdemo.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NameGenerator {
	public static final String[] maleNames = {
		"Liam",
		"Noah",
		"Oliver",
		"Elijah",
		"William",
		"James",
		"Benjamin",
		"Lucas",
		"Henry",
		"Alexander",
		"Mason",
		"Michael",
		"Ethan",
		"Daniel",
		"Jacob",
		"Logan",
		"Jackson",
		"Levi",
		"Sebastian",
		"Mateo",
		"Jack",
		"Owen",
		"Theodore",
		"Aiden",
		"Samuel",
		"Joseph",
		"John",
		"David",
		"Wyatt",
		"Matthew",
		"Luke",
		"Asher",
		"Carter",
		"Julian",
		"Grayson",
		"Leo",
		"Jayden",
		"Gabriel",
		"Isaac",
		"Lincoln",
		"Anthony",
		"Hudson",
		"Dylan",
		"Ezra",
		"Thomas",
		"Charles",
		"Christopher",
		"Jaxon",
		"Maverick",
		"Josiah",
		"Isaiah",
		"Andrew",
		"Elias",
		"Joshua",
		"Nathan",
		"Caleb",
		"Ryan",
		"Adrian",
		"Miles",
		"Eli",
		"Nolan",
		"Christian",
		"Aaron",
		"Cameron",
		"Ezekiel",
		"Colton",
		"Luca",
		"Landon",
		"Hunter",
		"Jonathan",
		"Santiago",
		"Axel",
		"Easton",
		"Cooper",
		"Jeremiah",
		"Angel",
		"Roman",
		"Connor",
		"Jameson",
		"Robert",
		"Greyson",
		"Jordan",
		"Ian",
		"Carson",
		"Jaxson",
		"Leonardo",
		"Nicholas",
		"Dominic",
		"Austin",
		"Everett",
		"Brooks",
		"Xavier",
		"Kai",
		"Jose",
		"Parker",
		"Adam",
		"Jace",
		"Wesley",
		"Kayden",
		"Silas" };
	
	public static final String[] femaleNames = {
		"Olivia",
		"Emma",
		"Ava",
		"Charlotte",
		"Sophia",
		"Amelia",
		"Isabella",
		"Mia",
		"Evelyn",
		"Harper",
		"Camila",
		"Gianna",
		"Abigail",
		"Luna",
		"Ella",
		"Elizabeth",
		"Sofia",
		"Emily",
		"Avery",
		"Mila",
		"Scarlett",
		"Eleanor",
		"Madison",
		"Layla",
		"Penelope",
		"Aria",
		"Chloe",
		"Grace",
		"Ellie",
		"Nora",
		"Hazel",
		"Zoey",
		"Riley",
		"Victoria",
		"Lily",
		"Aurora",
		"Violet",
		"Nova",
		"Hannah",
		"Emilia",
		"Zoe",
		"Stella",
		"Everly",
		"Isla",
		"Leah",
		"Lillian",
		"Addison",
		"Willow",
		"Lucy",
		"Paisley",
		"Natalie",
		"Naomi",
		"Eliana",
		"Brooklyn",
		"Elena",
		"Aubrey",
		"Claire",
		"Ivy",
		"Kinsley",
		"Audrey",
		"Maya",
		"Genesis",
		"Skylar",
		"Bella",
		"Aaliyah",
		"Madelyn",
		"Savannah",
		"Anna",
		"Delilah",
		"Serenity",
		"Caroline",
		"Kennedy",
		"Valentina",
		"Ruby",
		"Sophie",
		"Alice",
		"Gabriella",
		"Sadie",
		"Ariana",
		"Allison",
		"Hailey",
		"Autumn",
		"Nevaeh",
		"Natalia",
		"Quinn",
		"Josephine",
		"Sarah",
		"Cora",
		"Emery",
		"Samantha",
		"Piper",
		"Leilani",
		"Eva",
		"Everleigh",
		"Madeline",
		"Lydia",
		"Jade",
		"Peyton",
		"Brielle",
		"Adeline"
	};
	
	public static final String[] lastNames = {
		"Smith",
		"Johnson",
		"Williams",
		"Brown",
		"Jones",
		"Garcia",
		"Miller",
		"Davis",
		"Rodriguez",
		"Martinez",
		"Hernandez",
		"Lopez",
		"Gonzales",
		"Wilson",
		"Anderson",
		"Thomas",
		"Taylor",
		"Moore",
		"Jackson",
		"Martin",
		"Lee",
		"Perez",
		"Thompson",
		"White",
		"Harris",
		"Sanchez",
		"Clark",
		"Ramirez",
		"Lewis",
		"Robinson",
		"Walker",
		"Young",
		"Allen",
		"King",
		"Wright",
		"Scott",
		"Torres",
		"Nguyen",
		"Hill",
		"Flores",
		"Green",
		"Adams",
		"Nelson",
		"Baker",
		"Hall",
		"Rivera",
		"Campbell",
		"Mitchell",
		"Carter",
		"Roberts",
		"Gomez",
		"Phillips",
		"Evans",
		"Turner",
		"Diaz",
		"Parker",
		"Cruz",
		"Edwards",
		"Collins",
		"Reyes",
		"Stewart",
		"Morris",
		"Morales",
		"Murphy",
		"Cook",
		"Rogers",
		"Gutierrez",
		"Ortiz",
		"Morgan",
		"Cooper",
		"Peterson",
		"Bailey",
		"Reed",
		"Kelly",
		"Howard",
		"Ramos",
		"Kim",
		"Cox",
		"Ward",
		"Richardson",
		"Watson",
		"Brooks",
		"Chavez",
		"Wood",
		"James",
		"Bennet",
		"Gray",
		"Mendoza",
		"Ruiz",
		"Hughes",
		"Price",
		"Alvarez",
		"Castillo",
		"Sanders",
		"Patel",
		"Myers",
		"Long",
		"Ross",
		"Foster",
		"Jimenez"
	};

	public static enum Gender {
		MALE,
		FEMALE
	}

	public static String getFirstName(Gender gender) {
		Random random = ThreadLocalRandom.current();
		if (Gender.MALE.equals(gender)) {
			return maleNames[random.nextInt(maleNames.length)];
		}
		else if (Gender.FEMALE.equals(gender)) {
			return femaleNames[random.nextInt(femaleNames.length)];
		}
		else {
			int id = random.nextInt(maleNames.length + femaleNames.length);
			if (id < maleNames.length) {
				return maleNames[id];
			}
			else {
				return femaleNames[id - maleNames.length];
			}
		}
	}

	public static String getFirstName() {
		return getFirstName(null);
	}
	
	public static String getLastName() {
		Random random = ThreadLocalRandom.current();
		return lastNames[random.nextInt(lastNames.length)];
	}
	
	public static String getName() {
		return getFirstName(null) + " " +getLastName();
	}
	
	public static String getName(Gender gender) {
		return getFirstName(gender) + " " +getLastName();
	}
}
