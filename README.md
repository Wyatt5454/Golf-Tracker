# Golf Stat Tracker for the Wenatchee Golf and Country Club
 
 Here is the official beta release for this open source application!  There's still bugs and a ton of code refactoring to do, but that's what we have betas for.
 
 
 Here is the title screen
 
 <img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180903-073830.png" width="250">


## Scorecard Activity

The scorecard activity is where most of the work gets done. Its where scores are tracked during a round and how the user indicates more specific stats like hitting the fairway or sand traps.  This activity will also mark scores accordingly with squares or circles for bogeys and birdies respectively.  Additionally in the center of the screen the user is told how far away they are from the front center and back of the green for the hole they have selected.

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-120340.png" width="250">

The checkbox icon in the action bar will prompt the user if they want to save a round, which is done on a Room database which is maintained locally on the device.

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-120353.png" width="250">


## Main Stats Activity

Here is where the average overall statistics are displayed.  For a golfer this will be essessentially the assessment of how good they are as a player.  These averages are tracked with a file saved to internal storage using a serialized data object.  This activity holds two more buttons for more detailed statistics.

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-120403.png" width="250">


## Look at old rounds

Here the user can go back and look at how they did on past rounds.  You can see the score, fairway and green in regulation percentages, putts, and sand shots on each hole.  Select a hole to get statistics for that hole specifically.

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-120409.png" width="250">

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-120418.png" width="250">

## Track statistics hole by hole

On this page users are able to see how they do on each individual hole over time.  These stats are meant to show the user what holes they need to improve on, and which ones they can count on for low scores.  More detailed statistics will be added.

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-120444.png" width="250">

<img src="https://github.com/Wyatt54/Golf-Tracker/blob/master/Pictures%20for%20Readme/Screenshot_20180823-121722.png" width="250">


## Future work

I will be adding more detailed statistical tracking to the stats pages, and making them easier to understand and view.  I also want to track the best rounds for individual statistics, so the user can look at their glory rounds.  The UI needs some redesign to differentiate the buttons and text boxes.  There is a lot of behind the scenes stuff that can be improved, especially as this is my first real project in Java and my first time working with Android Studio.  I also still need to get official pictures of the logo to use for the app icon and the main activity.  I just took a picture with my phone of the logo, since I'm having trouble getting a hold of the official logo and other official images.
