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
To help the understanding of how the application is shaped, we will now demonstrate a few of the available commands.

## In employee context
We now sign in as employee ```HH``` in context ```Emp```.

We run command ```help``` to see available commands:
```
Available commands:
 - hours set {projectID} {activityID} {date} {hours}
 - hours submit {projectID} {activityID} {date} {hours}
 - project assign pm {projectID} {PMID}
 - project create {name} {billable}
 - request assistance {projectID} {activityID} {employeeID}
 - request ooo {type} {start} {end}
 - switch context {contextType}
 - view submissions
```

```HH``` has already been associated with activity 1 in project ```2020-000001```.
We will now submit a couple hours of work using ```hours submit 2020-000001 1 2020-05-08 2```:
```
Hours submitted.
```

However, it seems we still need some help from a colleague, so request assistance from our colleague Jens Jensen (JJ), ```request assistance 2020-000001 1 JJ```:
```
Assistance requested.
```

Let's see how we're doing so far (at the time of writing, today is 2020-05-08), ```view submissions```:
```
---------------------------------------------
| Project ID  | Activity ID | Tracked hours |
---------------------------------------------
| 2020-000001 | 1           | 2.0           |
---------------------------------------------
```

## In project manager context
We now sign in as employee ```HH``` in context ```PM```.

We run command ```help``` to see available commands:
```
Available commands:
 - project activity assign {employeeID} {projectID} {activityID}
 - project activity create {projectID} {activityName}
 - project activity estimate {projectID} {activityID} {workHours}
 - project activity finish {projectID} {activityID}
 - project activity setweeks {projectID} {activityID} {startWeek} {endWeek}
 - project assign pm {projectID} {PMID}
 - switch context {contextType}
 - view activity {projectID} {activityID}
 - view availability {date}
 - view project {projectID}
 - view schedule {employeeID}
```

Let's have a look at our own schedule, ```view schedule```:
```
Employee details:
 - ID: HH
 - Name: Hans Hansen

Working on the following active activities:
------------------------------------------------------------------------------------------------------------------
| ID | Name            | Start week | End week | Estimated work hours (in total) | Tracked work hours (in total) |
------------------------------------------------------------------------------------------------------------------
| 2  | Second Activity | 2020-14    | 2020-18  | 0                               | 2                             |
------------------------------------------------------------------------------------------------------------------
| 1  | First Activity  | 2020-10    | 2020-15  | 0                               | 4                             |
------------------------------------------------------------------------------------------------------------------

Planned out-of-office activities:
---------------------------------------
| Type      | Start      | End        |
---------------------------------------
| Illness   | 2020-04-01 | 2020-04-03 |
---------------------------------------
| Education | 2020-08-01 | 2020-08-05 |
---------------------------------------
| Vacation  | 2020-10-01 | 2020-10-14 |
---------------------------------------
```

Let's add an activity to project 2020-000001, ```project activity create 2020-000001 "New activity"```:
```
Activity created.
```

Now, let's see what is going on regarding project 2020-000001, ```view project 2020-000001```:
```
Project details:
 - ID: 2020-000001
 - Name: Test Project 1
 - Estimated remaining work: 0 work hours

Project activities:
----------------------------------------------------------------------------------------------------------------------
| ID | Name            | Start week  | End week    | Estimated work hours (in total) | Tracked work hours (in total) |
----------------------------------------------------------------------------------------------------------------------
| 1  | First Activity  | 2020-10     | 2020-15     | 0                               | 4                             |
----------------------------------------------------------------------------------------------------------------------
| 2  | Second Activity | 2020-14     | 2020-18     | 0                               | 2                             |
----------------------------------------------------------------------------------------------------------------------
| 3  | New activity    | (not found) | (not found) | 0                               | 0                             |
----------------------------------------------------------------------------------------------------------------------
```