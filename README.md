# CSV Charmer 
### Python-based CSV Viewer

This Java's programm is show CSV-tables by using Python's intepreter with pandas library over subprocess pipe (using stdin/stdout). 

This program supports such baseline functionality as follows: 
* Opening and showing CSV files
* Showing initial columns state histogram
* Editing cells 
* Saving edited data
* Inserting new row before/after
* Deleting row

All data processing is implemented in Python, Java is only GUI featured. 

#### App example
![main screen](https://github.com/OLavrik/csv-charmer/blob/master/image_main.png)

#### Roadmap
* Support reopening files

## Supported platforms
* macOS (10+ tested)
* Windows 7, 8, 10 (10 tested)
* Linux kernel (3+ tested) (CentOS, Ubuntu)

## User guide
* First when you launch the app will ask you *python3* path if it was not found autoamtically
![Python3 modal](https://github.com/OLavrik/csv-charmer/blob/master/image_modal.png)

* After python3 set up. You will be asked to enter path to the CSV texturally. This file will be opened and shown. 
**You could try files from *test* directory of this repo**

* Editing: 
  1. For editing the cell - double click it and enter the new value
  2. For inserting or deleting row, press RMB(right mouse button or two finger press on MacOS)
  ![Editing popup](https://github.com/OLavrik/csv-charmer/blob/master/image_popup.png)

* Saving
  Currently supported only saving the same file. Click save button and ok in opened message box to save your progress. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
* **JRE ^1.8**
* **Python ^3.6**
* **Pandas ^1.0.3**
* **Maven 4.0.0**
* **junit ^4.12**

### Build
* [Maven](https://maven.apache.org/), including (junit, style-checks)
  
or

* you can use your IDE configured with JRE to build and start the application


## Authors

* **Lavrichenko Olga** - *initial developer* 
