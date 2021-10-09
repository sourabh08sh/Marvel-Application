# Marvel-Application
 Developed an android application using marvel API to show data of marvel characters and comics.

# My Journey while developing this application
-> First step in this journey is to research marvel API how it works, the structure of data it returns, and other things. 
-> After completing the first step I have started the development of the application. I have used MVVM architecture so, this project is divided into different modules, modules are small parts in which I divided my project.

  *Model module* - It is responsible for the abstraction of data sources. This module works with the view model to get and save the data.

  *Network module* - It is responsible for getting the data from the backend. I have used the retrofit library for API calls.

  *Repository module* - It is responsible for handling data fetching from the network and it provides data to view the model. It holds references to the data                         source to execute functions for accessing data.

  *ViewModel module* - It is responsible for communication between the repository and the UI. So basically the reason behind using the view model is to                              survive the configuration changes because the view model holds UI data in a lifecycle conscious way. It takes care of holding and                             processing all the data needed for the UI. I have used Live Data to hold the data so we can get notified every time the data changes.

  *UI module* - It is responsible for displaying data and handling click events. From this module, we observing the live data so if anything changes in live                  data this observer will observe that and make that change. I have also developed an adapter module that is responsible for binding data in                    recycler view items.

-> I have also used coroutines to run input/output operations on the background thread. So while doing I/O operations main thread does not get blocked and the application UI can run smoothly.