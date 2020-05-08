# Prerequisites
Initially, Maven dependencies declared in ```pom.xml``` should be synchronized.

Afterwards, make sure your setup meets the following criteria:
- The folder ```resources``` should be marked as "Resources root"
- The folder ```src``` should be marked as "Sources root"
- The folder ```test``` should be marked as "Test sources root"

In IntelliJ, folders can be "marked" by right-clicking a folder and selecting ```Mark directory as``` → ```(wanted option)```.

# Running application and application tests
## Running the application
The application can be booted by running ```Main.main(String[] args)``` where ```args``` refer to the arguments given to the application.
The application needs two arguments to run: The initials of the employee signing in and the context.
In the provided set of data, the employees ```HH```, ```JJ```, and ```SS``` are defined.
Currently, the application supports two contexts, ```Emp``` (employee context) and ```PM``` (project manager context).

In IntelliJ, these arguments can be specified by running ```Main.main``` and then pressing ```Edit Configuration``` in the ```Run configurations```-dropdown.
Afterwards, arguments can be specified in ```Program arguments```.

If you intend to run the application using an alternative dataset, a third argument can be specified to the application stating the folder in which the relevant CSV-files are located.
For instance, on a Windows-machine, a third argument could be as follows: ```C:\Users\user\path\to\folder```. This folder should contain the files ```activities.csv```, ```employees.csv```, ```ooo-activities.csv```, ```projects.csv```, and ```workhours.csv```.

## Running the tests
The tests testing the application can be run by running the class ```AcceptanceTest```.
Provided the application project has been configured as stated in "Prerequisites", all tests should pass.

### Note on code coverage in tests
When running the class ```AcceptanceTest``` with coverage, it is important the package ```dk.dtu.SoftEngProjectG18``` is added to "Packages and classes to include in coverage data".
This can be achieved by running ```AcceptanceTest``` and then pressing ```Edit Configuration``` in the ```Run configurations```-dropdown.
Then select tab ```Code Coverage``` and press ```Add package```.

# Application quickstart