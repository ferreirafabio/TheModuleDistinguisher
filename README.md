 <h1>Purpose</h1>
 There has been a requirement at work to create a script which accepts two files from a MATLAB environment. Differences
 of the content of those files (the content I'm not allowed to depict is arranged column-wise and stored in ANSI format)
 had to be determined and marked in an Excel workbook, which was also transferred as a parameter from the MATLAB 
 environment.
 
 <h1>Functionality</h1>
 <ul TYPE="CIRCLE">
 <li>Parameters given by the MATLAB environment/GUI are used to read text files and Excel workbook</li>
 <li>regex.Pattern is used to extract metadata from the text files and include it into the Excel file</li>
 <li>Differences between the two text files are determined by using HashSets, HashMaps and entrySet</li>
 <li>For processing the Excel file, algorithms have been implemented to identify the coordinates where the next set of data can be written to. Also there's an implementation for reading a column, which is used as a referrence point to mark changes.</li></ul>
 
 <h1>To be done</h1>
 <ul TYPE="CIRCLE">
 <li>JUnit</li>
 </ul>
