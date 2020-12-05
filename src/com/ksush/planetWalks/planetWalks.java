package com.ksush.planetWalks;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class planetWalks {


	private static int notRecommendedIngredientsCount(List<String> recipe, List<String> notRequired) {
		int count = 0;
		for(int i = 0; i< recipe.size(); i++){
			String ing = recipe.get(i);
			if(notRequired.contains(ing))
				count++;
		}
		return count;
	}

	private static int recommendedIngredientsCount(List<String> recipe, List<String> required) {
		int count = 0;
		for(int i = 0; i< recipe.size(); i++){
			String ing = recipe.get(i);
			if(required.contains(ing))
				count++;
		}
		return count;
	}

	public static void main(String[] args) {
		try{
			Class.forName("java.sql.DriverManager");
			Connection cn= DriverManager.getConnection("jdbc:mysql://localhost:3306/recipeadvisor","root","8574");
			Statement stmt;
			stmt=cn.createStatement();
			String query;
			System.out.println("Database Connected");


			List<String> recipesName = new ArrayList<>();
			Collections.addAll(recipesName, "Golden Milk", "Creamy Green Salad", "Steamed veggie", "Wheat juice", "Salty Wheat Juice", "Lemonade-masala-karela", "Fish Biryani", "Giloy Kadha with cinnamon", "Apple Cinnamon Muffins", "Golden couscous with apricots and crisp onions");

			List<String> recipesTechnique = new ArrayList<>();
			Collections.addAll(recipesTechnique, "Boiled", "Not-Cooked", "Boiled", "Not-cooked", "Not-cooked", "Cooked", "Cooked", "Boiled", "Baked", "Cooked");


			List<List<String>> recipesIngredients = new ArrayList<>();
			List<String> reciipe1 = new ArrayList<>(Arrays.asList("Milk", "Turmeric"));
			List<String> reciipe2 = new ArrayList<>(Arrays.asList("Green beans", "Broccoli", "Kale", "Walnuts", "Cream"));
			List<String> reciipe3 = new ArrayList<>(Arrays.asList("Peppers", "Tomatoes", "Carrots", "Kale", "Green beans"));
			List<String> reciipe4 = new ArrayList<>(Arrays.asList("Wheat", "Pepper", "Sugar Cane Juice"));
			List<String> reciipe5 = new ArrayList<>(Arrays.asList("Wheat", "Black pepper", "Salt", "Lemon"));
			List<String> reciipe6 = new ArrayList<>(Arrays.asList("Lemon", "Botter gourd", "Salt", "Dry Spices", "Onion"));
			List<String> reciipe7 = new ArrayList<>(Arrays.asList("Fish", "Basmati Rice", "spices", "herbs"));
			List<String> reciipe8 = new ArrayList<>(Arrays.asList("Ginger", "Cinnamon", "Holy basil leaves", "Whole peppercorns", "Giloy"));
			List<String> reciipe9 = new ArrayList<>(Arrays.asList("Flour", "Sugar", "Baking powder", "Orange zest", "Apple", "Yogurt", "Eggs", "Cinnamon", "Sugar"));
			List<String> reciipe10 = new ArrayList<>(Arrays.asList("Couscous", "vegetable stock cube", "turmeric", "cinnamon", "dried apricot", "red onions", "Sugar", "Sunflower oil", "lemon", "Olive Oil", "Coriander", "mint"));
			Collections.addAll(recipesIngredients, reciipe1, reciipe2, reciipe3, reciipe4, reciipe5, reciipe6, reciipe7, reciipe8, reciipe9, reciipe10);

//        Algorithm
			Scanner sc = new Scanner(System.in);

			String[] healthCondition = {"diabetics","hypertension","covid"};


			while (true) {
				System.out.println("Enter :\n 1. Diabetics\n 2. Hypertension\n 3. COVID-19\n 4. exit");
				int o = sc.nextInt();
				if (o > 4 || o <= 0) {
					System.out.println("Invalid Input");
				} else if (o == 4) {
					System.out.println("Thank You");
					break;
				} else {
					o--;
					float max = 0;
					int maxIndex = 0;
					float score[] = new float[recipesName.size()];
					List<String> required = new LinkedList<>();
					query = "select * from recommended"+healthCondition[o]+";";
					ResultSet rs = stmt.executeQuery(query);
					while(rs.next()){
						String ingredient = rs.getString("ingredientname");
						required.add(ingredient);
					}
					List<String> notRequired = new LinkedList<>();
					query = "select * from notrecommended"+healthCondition[o]+";";
					rs = stmt.executeQuery(query);
					while(rs.next()){
						String ingredient = rs.getString("ingredientname");
						notRequired.add(ingredient);
					}
					for (int i = 0; i < recipesName.size(); i++) {
						float count = 0;
						int countRec = recommendedIngredientsCount(recipesIngredients.get(i), required);
						int countNotRec = notRecommendedIngredientsCount(recipesIngredients.get(i), notRequired);
						String tech = recipesTechnique.get(i);
						if (notRequired.contains(tech))
							count--;
						else
							count++;

						float countRecIndex = ((float) countRec / recipesIngredients.get(i).size());
						float countNotRecIndex = ((float) countNotRec / recipesIngredients.get(i).size());
						count += countRecIndex + countNotRecIndex;
						score[i] = count;
						if (score[i] > max) {
							max = score[i];
							maxIndex = i;
						}
					}
					System.out.println("Best Suited recipe for Your condition is: " + recipesName.get(maxIndex));
					System.out.println("Ingredients You will require are: " + recipesIngredients.get(maxIndex));
				}
			}
		}
		catch(ClassNotFoundException | SQLException e)
		{
			System.out.println(e.getMessage());
		}




	}
}
