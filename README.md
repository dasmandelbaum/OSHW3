# OSHW3

## Jeffrey Hagler and David Mandelbaum - PART 1 SUBMISSION (4/15/18)
## A listing of all files/directories in your submission and a brief description of each
  * Fat32Reader class - main command center for parsing file and user-run navigation
  * Boot class - used for info command
  * Directory class - represents directly (currently tree-like, under construction but works)
  * FAT class - represents the FAT table region, under construction
  * Log file prints errors in longer format than will appear on the screen

## Commands that work
 1. info
 2. volume 
 3. quit
 
## Commands that are in progress
 1. ls 
 2. stat
 3. cd

##	Instructions for running program
 1. Navigate into Fat32Reader folder
 2. Run the following command (with maven) from the Fat32Reader folder:
 
    ```
    mvn clean compile exec:java -Dexec.mainClass="os.hw3.Fat32Reader" -Dexec.args="fat32.img" 
    ```
 3. Start inputting commands.
 
##	Challenges encountered along the way
 1. Still deciding on best way to balance using file live and pre-building for navigation
 2. Maven does not print the "/]" properly, printing it after user types in input
 3. Much is riding on perfecting the parseDirectory() method. This is under construction.

##	Sources you used to help you write your program
 1. logging help from: http://www.vogella.com/tutorials/Logging/article.html
 2. hex to decimal help from: //https://stackoverflow.com/a/26738067
 3. http://www.cs.uni.edu/~diesburg/courses/cop4610_fall10/week11/week11.pdf
 4. https://www.pjrc.com/tech/8051/ide/fat32.html
 

