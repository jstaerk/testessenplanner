# Testessenplanner
([German version](README.de.md) of this text)


A java software to plan guerilla usability test events (like http://usability-testessen.de/) based on the OptaPlanner library (http://www.optaplanner.org/)

##Build
use
`mvn clean package`


run
`java -jar --add-opens java.base/java.lang=ALL-UNNAMED .\target\testessenPlanner-2.0.0-SNAPSHOT.jar`
Exception in thread "AWT-EventQueue-0" java.lang.RuntimeException: Error creating extended parser class: Could not determine whether class 'org.jtwig.parser.parboiled.base.BooleanParser$$parboiled' has already been loaded
