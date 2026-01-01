# Calories Calculator

## calculate and estimate calorie intake 

## Proporsal: 
This application helps users **track their daily calorie intake** by selecting foods from a list of commonly consumed items with pre-estimated calorie values and images. Users input the quantity consumed in grams, and the app calculates the total calories. It is designed for **individuals who are on diets or those interested in managing their calorie intake daily**. I was inspired to create this project because I have noticed many people, including me, struggle to keep track and calculate their daily calories due to multiple external reasons. Hence, this application is made to offer a simple, convenient solution to support healthy weight managementy. 

## User Stories: 
**the context of a Calories Counter Application** 

- As a user, I want to be able to input my choice of username 
- As a user, I want to input my own TDEE 
- As a user, I want to have choices of food that I usually consume during weight loss process (a list of common diet food)
- As a user, I want to have specific enough choices for each type of food
- As a user, I want the application to automatically calculate the total consumed calories of mine throughout the day 
- As a user, I want the application to show me how much calories I have left for the day
- As a user, I want to add multiple foods i consumed to account history
- As a user, I want to be shown the list of food i consumed per day (Phase 1)
- As a user, I want to be able to save list of food I consumed of the day to file 
- As a user, I want to be able to load and see my saved data from file (Phase 2)


## Instructions for End User: 
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by select the button displaying the food option at the start
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by choose again any option of food displaying in the menu selection
- You can locate my visual component by accessing the choice of food options
- You can save the state of my application by after choosing the choices of food, press the save button in the menu 
- You can reload the state of my application by after saving the choices of food, press the load button in the menu

## Phase 4: Part 2
Tue Aug 05 17:43:34 PDT 2025
Added new food: Egg (Boiled)
Tue Aug 05 17:43:40 PDT 2025
Added new food: Milk (1% Fat)

## Phase 4: Part 3
**Impreovement 1** 

- In my KcalCounterApp, there are 11 Food constants in the field, and I think this number can be reduce significantly if I have created more classes dedicated to each different type of food that I have, rather than making them into constants. 
- For example, Food can either be an abstract class or a concreate class. So, Fruits, Vegetables, Meat, Dairy and Protein can extends to Food and implement its own method. Apple and Banana can be subclass of Fruits, and etc. I think in this way it will be much easier to keep track of the hihierarchy. 

**Impreovement 2** 
- In my KcalCounterApp, there functionality and structure of the displaying options methods are highly similar. I think I can extract them out into an interface class, called AnalyzeOption , which then the Fruits, Vegetables, Meat, Dairy and Protein would extends AnalyzeOption, which then will lower the amount of code needed top implement and the understanding of the customer. 
