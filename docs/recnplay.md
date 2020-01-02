<h2>Record and Play</h2>
<h4>Implementation</h4>
<p>The implementation of the Record and Play (R&P) feature is spread out 
over several classes. </p>
<p>The GUI for R&P is mainly in the InfoPanel class. 
The record and replay GUI is in the ReplayPanel. When the replay button
is clicked on the record button changes to a play/stop button and the replay
button changes to a change speed button. Additionally two new buttons, a next step button 
and an exit button are added to the GUI. When the change speed button is clicked
the main frame creates a pop-up window showing the possible speeds to choose from.
 </p>

<p>The code for R&P is in five classes, Record, Replay, loadJSON, saveJSON and Main. 
Record is used for recording the game, it uses SaveGame in saveJSON to 
save the initial board state of the board. In Main whenever doMove() is called 
i.e. the player moves, the move is sent to the record file by using the SaveMove() method
I created in saveJSON. These moves are loaded later for the replay by using the loadMove()
method in loadJSON. Main also controls the replay in the timer() method. It uses a variable
to control the replay speed, and executes the next move when the next step button is clicked.
It also manipulates the frameRate based on whether the replay is paused or playing. </p>


<p> So overall, recording is done in the Record, Main and saveJSON class, loading the record 
is done in the Replay and loadJSON class, controlling the replay is done in Main and showing the
GUI is done in replayPanel</p>

<h4>Design Pattern</h4>

<p>The record and play feature follows the Command pattern. The Command pattern can be used for
tasks such as Macro recording. Macro recording is "playing back" the same actions by 
executing the same commands again in sequence. The record and play feature does this as well, it saves 
the actions (player moves) using SaveJSON and Main. It "plays back" these moves by calling the same command
(doMove()) with the same parameter (String direction), in the same order (the moves are ordered by the nanosecond they are made) </p>

<h4>Using the Record and Play feature</h4>
<p>There are many features of R&P, here is a guide to use these features: </p>
<ul><li>
To start recording press the record button and to stop recording press the record button again.</li>

<li>To replay a record press the replay button. Once this button is pressed a file chooser will open. Please choose a file that starts with "record-".</li>


<li>Once you have chosen a file you will have four replay control buttons. One button is a play/stop button
used for playing/stopping the replay, the change speed button allows you to change the speed of the recording
(the options are 25%, 50% or 100%). The next step button is used to execute/advance one move. The exit button takes you back to the title screen </li></ul>
