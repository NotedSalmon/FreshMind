# FreshMind
Intro:
A productivity mobile application that allows users to create/edit/delete tasks and notes. The tasks get inserted into a calendar from which the user can see which tasks/events they have in each day.

How to run:
Download the source code from the main branch and open in Android Studio as you will need the built-in emulator. Build the project and run the program. The program will then start and you can create an account and have a look around.

In the case of an error:
Cleaning and rebuilding the app will work again. This only happens if accessing the code from Onedrive/GoogleDrive

Tech:
- Android Studio IDE
- Kotlin - Fragments + ViewActivities
- Calendar Package - https://github.com/kizitonwose/Calendar
- SQLite
- FireBase
- Using FreshMind_Web

Features:
- Task create/read/update/delete.
- Note create/read/update/delete.
- Calendar displaying all the tasks in their respective dates.
- Welcome page that has pinned Notes and Tasks close to deadline.
- Feedback using FireBase
- User Authentication
- Theme customisation
- User Settings

Process:
This has been my first individual Kotlin project for university. The plan is to create an app that I would use to keep track of all my tasks and make myself accountable. As I have had previous experience of Kotlin, it was simple to get the basic functonality to work such as UI and basic user functionality.
The bigger strugles of this project was deciding a Database to use as I had only used SQLite but wanted to try using Firebase for the first time. This did not go well and resulted in me wasting a good week of progress. Instead I obted for using Firebase to store the users feedback which I can later view in a website I made using JavaScript, HTML and CSS.
The Calendar implementation has also been hard as I had never implemented a third party library which caused challenges however taught me a lot.

The project is a work in progress which means a lot of the features have not been implemented yet and there is always room for improvement.
