It is a console application that allows you to browse the categories of music and playlists in spotify or on another site that has the same structure of requests and returns. It can be run with the following parameters: -access -resource -page
They are not required.

-access -> authentication server
-resource -> resource server
-page -> number of objects on the displayed page


For example:
-resource https://accounts.hell.com -page 7 -access http://127.0.0.1:3432 

First we need to enter auth for authorization, then enter the link and confirm access.

If all is well then you can use the following commands: new, featured, categories, playlists.
For page navigation we use next and prev and exit to exit the page mode.

To end the program, type exit.
