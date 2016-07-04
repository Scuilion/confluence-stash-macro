
## Stash Macro
A simple macro for viewing available branches from stash. 

Define the macro  
![alt text](https://raw.githubusercontent.com/Scuilion/confluence-stash-macro/master/img/MacroEditor.png "Editor")

results without a filter  
![alt text](https://raw.githubusercontent.com/Scuilion/confluence-stash-macro/master/img/Branches.png "Branches")

or with a filter  
![alt text](https://raw.githubusercontent.com/Scuilion/confluence-stash-macro/master/img/WithFilter.png "With Filter")

For Development  
atlas-run-standalone --product confluence --settings /usr/share/atlassian-plugin-sdk-6.2.6/apache-maven-3.2.1/conf/settings.xml --http-port 1990
atlas-run-standalone --product stash --settings /usr/share/atlassian-plugin-sdk-6.2.6/apache-maven-3.2.1/conf/settings.xml --http-port 7990

-Datlassian.webresource.disable.minification=true

