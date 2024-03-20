The Java application for rental agreement takes as input the tool code, no of rental days, discount percentage and the checkout date. 

It then goes onto calculate the due date and charges in the methods which are explicitly defined in the code. 

There are code blocks which throw an Illegal Argument Exception if the rental days or discount percentage values are not valid. 

The pricing for the specific tool type code and brand names are stored in a Hashmap accordingly. 

The isHoliday method takes into account the two holidays specified in the requirement document and calculates the price accordingly.  

Improvements. 

I had spent only a few hours on this code but given a little bit more time I would add the following 

— Make the date configurable in the input parameters. Right now it’s set to be only today. 
— Add unit tests to cover all the scenarios as mentioned in the document to make sure the tests are working. 

