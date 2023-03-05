precondition: install gradle, prereq:( Java JDK version 8 or higher), JDK (along with JVM), JRE
extract downloaded zip into c:\Gradle)
upgrade gradle wrapper with gradlew.bat
add gradle binaries path to system variables PATH in Environment variables (C:\Gradle\gradle-8.0.1\bin)
Compile: gradle build -x test
Technical task1
to run: gradle test --tests Task1
Tips:
    giving unique ids to page elements
    or use unique css because some elements in following pages in test have the same xpath (not appropriate to use in test automation)
    use clickable instead of visible in waits
    Page object model can be used in case of more complex webpages

Technical task2
to run: gradle test --tests Task2
Tips:
    Asserting more values in response or data types (schema)  
    To add delete endpoint to enable clearing of testing data   
correct testing sequence for task2 for both GET and POST endpoints would be not to limit results from one page in GET request (in case it 
is not needed) but rather fetch all the data (without query parameter) and then filter it and use sequence:
1.GET
2.POST
3.GET(assert total=+1)
4.DELETE (assert record is deleted)

or check only 'perPage' amount

For detail report go to \javaqatsTasks\build\reports\tests\test\index.html