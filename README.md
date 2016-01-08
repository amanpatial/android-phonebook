PhoneBook
========

Phonebook app displays the entire existing phone contacts. You can add new phone contact and refresh new entry in display of phonebook

## Feature

1. Display phonebook using android's activity, layouts, fragment, adapter and bind the server data through remote REST API using JSON format
2. Add new phone contact to existing phonebook that uses android's async background service for remote REST API communication
3. Refresh a phonebook display that uses async background service to re-populate the fresh data    

## Screenshot

<!--![alt tag](https://raw.githubusercontent.com/aman-patial/android-phonebook/master/app/src/main/snapshot/phonebook-display.png)-->
![alt-text-1](https://raw.githubusercontent.com/aman-patial/android-phonebook/master/app/src/main/snapshot/phonebook-refresh-add.png "Display") 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 ![alt-text-2](https://raw.githubusercontent.com/aman-patial/android-phonebook/master/app/src/main/snapshot/phonebook-add.png "Add")


## Use

To build a development version of the app from source, follow the instructions outlined below. 

1. Download and install [Android Studio](http://developer.android.com/sdk/index.html)
2. Clone the project or a fork of it to your local development machine
3. Import the project into Android Studio
4. Click on Sync Project with Gradle files to download all the dependencies
5. Open the SDK manager to install the required Android SDK Tools and Android SDK Build-tools
6. Make sure backend REST APIs are up and running [Refer API] (https://github.com/aman-patial/emberjs-expressjs-mongodb-crud/tree/master/express-phonebook)
6. Build the project
