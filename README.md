# SkillCourt-7.0

Developers: 

- Alvaro Silva
- Chandan Aubel
- Antonio Riverol


ALL DELIVERABLES will be found under -- "SkillCourt 7.0 folder"

It includes 3 folders:

1) Skillcourt app
2) Server app
3) Webpage

Installation and maintenance:

https://youtu.be/E7BNdbgKH9w?list=PLG3lylzOg8FhtF1NlUoeiVqqH0W0TEDdF

NOTE:
**** IF YOU GET A WARNING **** About not being able to "crunch a file" and it yells about a super silly .png file... To solve this, take the folder of Code and put it under your root directory. This error stems from the path to the .png mentioned is waaaay to long for little andriod studio to handle and so shortening this path (and will probably best to rename this long foldername too as just 'skillcourt') will make your life easier and your code ready to compile! -- Contact us if need me, of course!!! ******

For any questions don't hesitate to contact us through the product owner.

# Android application structure:

I will describe the main folders of the current application, I will not talk about the framework specific ones

- adapters: 

	This folder contains all the classes you will use to show lists or options. Right now only contains GamesPlayedRecyclerViewAdapter which you can find on the main dashboard screen.

- connection:

	Connection contains all the classes related to our wifi connection to our Arduinos. We have a manager that handle each ArduinoConnectionHandler using an Arduino object. All of these belongs to the project. There is no use of a library for this.

- entities:
	All the classes related to our business logic.

- game:
	This folder contains all the classes used during a game. We have the CountDownTimer class which is the timer. We also have an SkillCourtManager used to handle all the games created while using the application	

- interfaces:
	Creation of java interfaces used all over the application

- manager:
	Contains the StepManager, which handles the configuration of the dynamic steps.

- ui:
	This folder contains all of the UI classes from android. It contains the Activities and Fragments divided by functionality. 

- fcm:
	Contains the Notification Service
  
# Arduino application 

For the arduino we are using the following libraries:

- Wifi101
- FastLed

This libraries allows us to connect to the WiFi and change the colors of the LED's

Structure: None. Just one file

# Firebase:

For the android application and storing data, we are using a firebase application. 
You can create one using the following link : https://console.firebase.google.com/

Also if you would like to learn more and what else can you do with firebase: https://firebase.google.com/docs/

Just create a project and replace the file "google-services.json" found on the app folder.

Once you replace it you can use it. If you don't want to create a new one, contact us and we will be adding your email and SHA1 key to our current listing.

# Webpage:

Included in video

# Server:

For notification service, we are using a Node Js Server. For interacting with the server and make changes, you need to have "npm" installed in your computer and read documentation about Express and BodyParser.

# Requirements

- Arduino MKR1000
- Push Buttons
- LED Strips
- Android Studio IDE
- Arduino IDE 
- Android Smartphone
- NPM installed
